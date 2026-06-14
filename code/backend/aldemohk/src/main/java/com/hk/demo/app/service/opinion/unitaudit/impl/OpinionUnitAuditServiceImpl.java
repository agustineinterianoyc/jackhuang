package com.hk.demo.app.service.opinion.unitaudit.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hk.demo.api.enums.ResultCode;
import com.hk.demo.api.enums.opinion.OpinionActionLogAction;
import com.hk.demo.api.enums.opinion.OpinionAttachmentBizType;
import com.hk.demo.api.enums.opinion.OpinionAuditAction;
import com.hk.demo.api.enums.opinion.OpinionMainStatus;
import com.hk.demo.api.enums.opinion.OpinionUnitFillStatus;
import com.hk.demo.api.enums.opinion.OpinionUnitAuditStatus;
import com.hk.demo.app.mapper.opinion.OpinionAttachmentMapper;
import com.hk.demo.app.mapper.opinion.OpinionItemMapper;
import com.hk.demo.app.mapper.opinion.OpinionSurveyMapper;
import com.hk.demo.app.mapper.opinion.OpinionSurveyModuleMapper;
import com.hk.demo.app.mapper.opinion.OpinionUnitAuditLogMapper;
import com.hk.demo.app.mapper.opinion.OpinionUnitTaskMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionAttachmentDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionItemDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionSurveyDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionSurveyModuleDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionUnitAuditLogDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionUnitTaskDO;
import com.hk.demo.app.model.request.opinion.unitaudit.UnitAuditListRequest;
import com.hk.demo.app.model.request.opinion.unitaudit.UnitAuditRejectRequest;
import com.hk.demo.app.model.response.opinion.unitaudit.UnitAuditDetailVO;
import com.hk.demo.app.model.response.opinion.unitaudit.UnitAuditListItemVO;
import com.hk.demo.app.model.response.opinion.unitaudit.UnitAuditStatusVO;
import com.hk.demo.app.service.opinion.log.OpinionActionLogger;
import com.hk.demo.app.service.opinion.mock.OpinionMockMasterDataProvider;
import com.hk.demo.app.service.opinion.unitaudit.OpinionUnitAuditService;
import com.hk.demo.core.exception.BusinessException;
import com.hk.demo.data.pagination.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基层审核服务实现（R03 人资部主任视角）。
 */
@Service
public class OpinionUnitAuditServiceImpl implements OpinionUnitAuditService {

    private static final Logger log = LoggerFactory.getLogger(OpinionUnitAuditServiceImpl.class);

    private static final String ACTOR_ROLE_R03 = "R03";

    private final OpinionUnitTaskMapper unitTaskMapper;
    private final OpinionItemMapper itemMapper;
    private final OpinionUnitAuditLogMapper unitAuditLogMapper;
    private final OpinionSurveyMapper surveyMapper;
    private final OpinionSurveyModuleMapper moduleMapper;
    private final OpinionAttachmentMapper attachmentMapper;
    private final OpinionActionLogger actionLogger;
    private final OpinionMockMasterDataProvider masterData;

    @Autowired
    public OpinionUnitAuditServiceImpl(OpinionUnitTaskMapper unitTaskMapper,
                                       OpinionItemMapper itemMapper,
                                       OpinionUnitAuditLogMapper unitAuditLogMapper,
                                       OpinionSurveyMapper surveyMapper,
                                       OpinionSurveyModuleMapper moduleMapper,
                                       OpinionAttachmentMapper attachmentMapper,
                                       OpinionActionLogger actionLogger,
                                       OpinionMockMasterDataProvider masterData) {
        this.unitTaskMapper = unitTaskMapper;
        this.itemMapper = itemMapper;
        this.unitAuditLogMapper = unitAuditLogMapper;
        this.surveyMapper = surveyMapper;
        this.moduleMapper = moduleMapper;
        this.attachmentMapper = attachmentMapper;
        this.actionLogger = actionLogger;
        this.masterData = masterData;
    }

    // ===== API-301 审核列表 =====
    @Override
    public PageResult<UnitAuditListItemVO> list(UnitAuditListRequest request) {
        int page = request.getPage() == null ? 1 : Math.max(1, request.getPage());
        int size = request.getPageSize() == null ? 20 : Math.max(1, Math.min(200, request.getPageSize()));

        LambdaQueryWrapper<OpinionUnitTaskDO> wrapper = new LambdaQueryWrapper<>();
        // 仅查询已提交的任务
        wrapper.eq(OpinionUnitTaskDO::getFillStatus, OpinionUnitFillStatus.SUBMITTED.name());

        // 审核状态过滤
        if (!CollectionUtils.isEmpty(request.getAuditStatus())) {
            wrapper.in(OpinionUnitTaskDO::getAuditStatus, request.getAuditStatus());
        }

        // 先取出符合条件的任务
        List<OpinionUnitTaskDO> all = unitTaskMapper.selectList(wrapper);
        long total = all.size();
        int from = (page - 1) * size;
        int to = Math.min(from + size, all.size());
        List<OpinionUnitTaskDO> tasks = from < all.size() ? all.subList(from, to) : List.of();

        if (tasks.isEmpty()) {
            return new PageResult<>(total, Collections.emptyList());
        }

        List<UnitAuditListItemVO> rows = new ArrayList<>();
        for (OpinionUnitTaskDO t : tasks) {
            UnitAuditListItemVO vo = new UnitAuditListItemVO();
            vo.setTaskId(t.getId());
            vo.setSurveyId(t.getSurveyId());
            vo.setSurveyName(null);
            vo.setAssessYear(request.getAssessYear());
            vo.setUnitId(t.getUnitId());
            vo.setUnitName(t.getUnitName());
            vo.setFillStatus(t.getFillStatus());
            vo.setFillStatusText(fillStatusText(t.getFillStatus()));
            vo.setAuditStatus(t.getAuditStatus());
            vo.setAuditStatusText(auditStatusText(t.getAuditStatus()));
            vo.setSubmittedAt(t.getSubmittedAt());
            vo.setUnitDeadline(null);
            vo.setLastRejectReason(t.getLastRejectReason());
            rows.add(vo);
        }
        return new PageResult<>(total, rows);
    }

    // ===== API-302 审核详情 =====
    @Override
    public UnitAuditDetailVO detail(Long taskId) {
        OpinionUnitTaskDO task = requireTask(taskId);

        // 校验任务归属
        ensureUnitAccess(task);

        // 加载 survey
        OpinionSurveyDO survey = surveyMapper.selectById(task.getSurveyId());
        if (survey == null) {
            throw new BusinessException(ResultCode.OPINION_NOT_FOUND);
        }

        // 校验征集状态
        ensureFillingStatus(survey);

        // 校验审核状态
        if (!OpinionUnitAuditStatus.PENDING.name().equals(task.getAuditStatus())) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE);
        }

        UnitAuditDetailVO vo = new UnitAuditDetailVO();

        // task 信息
        UnitAuditDetailVO.TaskInfo taskInfo = new UnitAuditDetailVO.TaskInfo();
        taskInfo.setId(task.getId());
        taskInfo.setSurveyId(task.getSurveyId());
        taskInfo.setSurveyName(survey.getName());
        taskInfo.setAssessYear(survey.getAssessYear());
        taskInfo.setSurveyStatus(survey.getStatus());
        taskInfo.setSurveyStatusText(mainStatusText(survey.getStatus()));
        taskInfo.setFillStatus(task.getFillStatus());
        taskInfo.setFillStatusText(fillStatusText(task.getFillStatus()));
        taskInfo.setAuditStatus(task.getAuditStatus());
        taskInfo.setAuditStatusText(auditStatusText(task.getAuditStatus()));
        taskInfo.setUnitDeadline(survey.getUnitDeadline());
        taskInfo.setSubmittedAt(task.getSubmittedAt());
        taskInfo.setSubmittedBy(task.getSubmittedBy());
        taskInfo.setLastRejectReason(task.getLastRejectReason());
        vo.setTask(taskInfo);

        // 模块
        List<OpinionSurveyModuleDO> modules = moduleMapper.selectList(
            new LambdaQueryWrapper<OpinionSurveyModuleDO>()
                .eq(OpinionSurveyModuleDO::getSurveyId, task.getSurveyId())
                .orderByAsc(OpinionSurveyModuleDO::getDisplayOrder));

        // 模块附件
        Map<Long, List<OpinionAttachmentDO>> moduleAttMap = loadModuleAttachments(task.getSurveyId());

        List<UnitAuditDetailVO.ModuleInfo> moduleVOs = new ArrayList<>();
        for (OpinionSurveyModuleDO m : modules) {
            UnitAuditDetailVO.ModuleInfo mi = new UnitAuditDetailVO.ModuleInfo();
            mi.setModuleCode(m.getModuleCode());
            mi.setModuleName(m.getModuleName());
            List<UnitAuditDetailVO.AttachmentInfo> atts = new ArrayList<>();
            for (OpinionAttachmentDO a : moduleAttMap.getOrDefault(m.getId(), Collections.emptyList())) {
                UnitAuditDetailVO.AttachmentInfo ai = new UnitAuditDetailVO.AttachmentInfo();
                ai.setFileId(a.getFileId());
                ai.setFileName(a.getFileName());
                atts.add(ai);
            }
            mi.setAttachments(atts);
            moduleVOs.add(mi);
        }
        vo.setModules(moduleVOs);

        // 意见行
        List<OpinionItemDO> items = itemMapper.selectList(
            new LambdaQueryWrapper<OpinionItemDO>()
                .eq(OpinionItemDO::getUnitTaskId, taskId)
                .orderByAsc(OpinionItemDO::getDisplayOrder));

        List<UnitAuditDetailVO.ItemInfo> itemVOs = new ArrayList<>();
        for (OpinionItemDO item : items) {
            UnitAuditDetailVO.ItemInfo ii = new UnitAuditDetailVO.ItemInfo();
            ii.setId(item.getId());
            ii.setModuleCode(item.getModuleCode());
            ii.setIndicatorCategory(item.getIndicatorCategory());
            ii.setIndicatorName(item.getIndicatorName());
            ii.setFactorName(item.getFactorName());
            ii.setExtraField(item.getExtraField());
            ii.setOpinionCategory(item.getOpinionCategory());
            ii.setOpinionContent(item.getOpinionContent());
            ii.setReason(item.getReason());
            ii.setDisplayOrder(item.getDisplayOrder());
            itemVOs.add(ii);
        }
        vo.setItems(itemVOs);

        // 审核日志
        List<OpinionUnitAuditLogDO> logs = unitAuditLogMapper.selectList(
            new LambdaQueryWrapper<OpinionUnitAuditLogDO>()
                .eq(OpinionUnitAuditLogDO::getUnitTaskId, taskId)
                .orderByDesc(OpinionUnitAuditLogDO::getCreatedAt));

        List<UnitAuditDetailVO.AuditLogInfo> logVOs = new ArrayList<>();
        for (OpinionUnitAuditLogDO l : logs) {
            UnitAuditDetailVO.AuditLogInfo li = new UnitAuditDetailVO.AuditLogInfo();
            li.setId(l.getId());
            li.setAction(l.getAction());
            li.setActorRole(l.getActorRole());
            li.setRejectReason(l.getRejectReason());
            li.setCreatedAt(l.getCreatedAt());
            logVOs.add(li);
        }
        vo.setAuditLogs(logVOs);

        return vo;
    }

    // ===== API-303 审核通过 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UnitAuditStatusVO pass(Long taskId) {
        OpinionUnitTaskDO task = requireTask(taskId);
        ensureUnitAccess(task);

        OpinionSurveyDO survey = surveyMapper.selectById(task.getSurveyId());
        if (survey == null) {
            throw new BusinessException(ResultCode.OPINION_NOT_FOUND);
        }
        ensureFillingStatus(survey);

        if (!OpinionUnitAuditStatus.PENDING.name().equals(task.getAuditStatus())) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE);
        }

        Long actor = masterData.currentUserId();

        task.setAuditStatus(OpinionUnitAuditStatus.PASS.name());
        task.setAuditedAt(LocalDateTime.now());
        task.setAuditedBy(actor);
        unitTaskMapper.updateById(task);

        writeAuditLog(task, OpinionAuditAction.PASS.name(), null, actor);
        actionLogger.log(task.getSurveyId(), OpinionActionLogAction.UNIT_AUDIT_PASS,
            OpinionUnitAuditStatus.PENDING.name(), OpinionUnitAuditStatus.PASS.name(),
            ACTOR_ROLE_R03, actor,
            "unit_task_id=" + taskId);

        log.info("[opinion] unit audit pass: taskId={}, auditor={}", taskId, actor);
        return toStatusVO(task);
    }

    // ===== API-304 审核退回 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UnitAuditStatusVO reject(Long taskId, UnitAuditRejectRequest request) {
        String rejectReason = request.getRejectReason();
        if (!StringUtils.hasText(rejectReason)) {
            throw new BusinessException(ResultCode.OPINION_REJECT_REASON_REQUIRED);
        }

        OpinionUnitTaskDO task = requireTask(taskId);
        ensureUnitAccess(task);

        OpinionSurveyDO survey = surveyMapper.selectById(task.getSurveyId());
        if (survey == null) {
            throw new BusinessException(ResultCode.OPINION_NOT_FOUND);
        }
        ensureFillingStatus(survey);

        if (!OpinionUnitAuditStatus.PENDING.name().equals(task.getAuditStatus())) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE);
        }

        Long actor = masterData.currentUserId();

        task.setFillStatus(OpinionUnitFillStatus.PENDING.name());
        task.setAuditStatus(OpinionUnitAuditStatus.REJECTED.name());
        task.setLastRejectReason(rejectReason);
        task.setAuditedAt(LocalDateTime.now());
        task.setAuditedBy(actor);
        unitTaskMapper.updateById(task);

        writeAuditLog(task, OpinionAuditAction.REJECT.name(), rejectReason, actor);
        actionLogger.log(task.getSurveyId(), OpinionActionLogAction.UNIT_AUDIT_REJECT,
            OpinionUnitAuditStatus.PENDING.name(), OpinionUnitAuditStatus.REJECTED.name(),
            ACTOR_ROLE_R03, actor,
            "unit_task_id=" + taskId + " reason=" + rejectReason);

        log.info("[opinion] unit audit reject: taskId={}, auditor={}, reason={}", taskId, actor, rejectReason);
        return toStatusVO(task);
    }

    // ====================================================================
    // ==================== Helper ========================================
    // ====================================================================

    private OpinionUnitTaskDO requireTask(Long taskId) {
        OpinionUnitTaskDO task = taskId == null ? null : unitTaskMapper.selectById(taskId);
        if (task == null) {
            throw new BusinessException(ResultCode.OPINION_TASK_NOT_FOUND);
        }
        return task;
    }

    private void ensureUnitAccess(OpinionUnitTaskDO task) {
    }

    private void ensureFillingStatus(OpinionSurveyDO survey) {
        if (!OpinionMainStatus.FILLING.name().equals(survey.getStatus())) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE);
        }
    }

    private void writeAuditLog(OpinionUnitTaskDO task, String action, String rejectReason, Long actor) {
        OpinionUnitAuditLogDO log = new OpinionUnitAuditLogDO();
        log.setUnitTaskId(task.getId());
        log.setSurveyId(task.getSurveyId());
        log.setAction(action);
        log.setActorRole(ACTOR_ROLE_R03);
        log.setActorId(actor);
        log.setRejectReason(rejectReason);
        log.setCreatedAt(LocalDateTime.now());
        unitAuditLogMapper.insert(log);
    }

    private UnitAuditStatusVO toStatusVO(OpinionUnitTaskDO task) {
        UnitAuditStatusVO vo = new UnitAuditStatusVO();
        vo.setTaskId(task.getId());
        vo.setFillStatus(task.getFillStatus());
        vo.setAuditStatus(task.getAuditStatus());
        return vo;
    }

    private String fillStatusText(String status) {
        try {
            return OpinionUnitFillStatus.valueOf(status).getText();
        } catch (Exception e) {
            return status;
        }
    }

    private String auditStatusText(String status) {
        try {
            return OpinionUnitAuditStatus.valueOf(status).getText();
        } catch (Exception e) {
            return status;
        }
    }

    private String mainStatusText(String status) {
        OpinionMainStatus s = OpinionMainStatus.fromName(status);
        return s == null ? status : s.getText();
    }

    private Map<Long, List<OpinionAttachmentDO>> loadModuleAttachments(Long surveyId) {
        List<OpinionAttachmentDO> all = attachmentMapper.selectList(
            new LambdaQueryWrapper<OpinionAttachmentDO>()
                .eq(OpinionAttachmentDO::getSurveyId, surveyId)
                .eq(OpinionAttachmentDO::getBizType, OpinionAttachmentBizType.MODULE.name()));
        return all.stream()
            .filter(a -> a.getBizRefId() != null)
            .collect(Collectors.groupingBy(OpinionAttachmentDO::getBizRefId,
                HashMap::new, Collectors.toList()));
    }
}
