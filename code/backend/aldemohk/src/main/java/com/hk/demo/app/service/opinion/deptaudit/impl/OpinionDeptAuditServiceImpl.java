package com.hk.demo.app.service.opinion.deptaudit.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hk.demo.api.enums.ResultCode;
import com.hk.demo.api.enums.opinion.OpinionActionLogAction;
import com.hk.demo.api.enums.opinion.OpinionAttachmentBizType;
import com.hk.demo.api.enums.opinion.OpinionAuditAction;
import com.hk.demo.api.enums.opinion.OpinionDeptAuditStatus;
import com.hk.demo.api.enums.opinion.OpinionDeptSubmitStatus;
import com.hk.demo.api.enums.opinion.OpinionMainStatus;
import com.hk.demo.app.mapper.opinion.OpinionAttachmentMapper;
import com.hk.demo.app.mapper.opinion.OpinionItemMapper;
import com.hk.demo.app.mapper.opinion.OpinionSurveyMapper;
import com.hk.demo.app.mapper.opinion.OpinionSurveyModuleMapper;
import com.hk.demo.app.mapper.opinion.OpinionUnitTaskMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionAttachmentDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionDeptAuditLogDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionDeptTaskDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionFeedbackDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionItemDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionSurveyDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionSurveyModuleDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionUnitTaskDO;
import com.hk.demo.app.model.request.opinion.deptaudit.DeptAuditListRequest;
import com.hk.demo.app.model.request.opinion.deptaudit.DeptAuditRejectRequest;
import com.hk.demo.app.model.response.opinion.deptaudit.DeptAuditDetailVO;
import com.hk.demo.app.model.response.opinion.deptaudit.DeptAuditListItemVO;
import com.hk.demo.app.model.response.opinion.deptaudit.DeptAuditStatusVO;
import com.hk.demo.app.repository.opinion.deptaudit.OpinionDeptAuditLogRepository;
import com.hk.demo.app.repository.opinion.deptfeedback.OpinionDeptTaskRepository;
import com.hk.demo.app.repository.opinion.deptfeedback.OpinionFeedbackRepository;
import com.hk.demo.app.service.opinion.deptaudit.OpinionDeptAuditService;
import com.hk.demo.app.service.opinion.log.OpinionActionLogger;
import com.hk.demo.app.service.opinion.mock.OpinionMockMasterDataProvider;
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
 * 专业审核服务实现（R05 专业部门负责人视角）。
 */
@Service
public class OpinionDeptAuditServiceImpl implements OpinionDeptAuditService {

    private static final Logger log = LoggerFactory.getLogger(OpinionDeptAuditServiceImpl.class);

    private static final String ACTOR_ROLE_R05 = "R05";

    private final OpinionDeptTaskRepository deptTaskRepo;
    private final OpinionFeedbackRepository feedbackRepo;
    private final OpinionDeptAuditLogRepository deptAuditLogRepo;
    private final OpinionItemMapper itemMapper;
    private final OpinionSurveyMapper surveyMapper;
    private final OpinionSurveyModuleMapper moduleMapper;
    private final OpinionAttachmentMapper attachmentMapper;
    private final OpinionUnitTaskMapper unitTaskMapper;
    private final OpinionActionLogger actionLogger;
    private final OpinionMockMasterDataProvider masterData;

    @Autowired
    public OpinionDeptAuditServiceImpl(OpinionDeptTaskRepository deptTaskRepo,
                                       OpinionFeedbackRepository feedbackRepo,
                                       OpinionDeptAuditLogRepository deptAuditLogRepo,
                                       OpinionItemMapper itemMapper,
                                       OpinionSurveyMapper surveyMapper,
                                       OpinionSurveyModuleMapper moduleMapper,
                                       OpinionAttachmentMapper attachmentMapper,
                                       OpinionUnitTaskMapper unitTaskMapper,
                                       OpinionActionLogger actionLogger,
                                       OpinionMockMasterDataProvider masterData) {
        this.deptTaskRepo = deptTaskRepo;
        this.feedbackRepo = feedbackRepo;
        this.deptAuditLogRepo = deptAuditLogRepo;
        this.itemMapper = itemMapper;
        this.surveyMapper = surveyMapper;
        this.moduleMapper = moduleMapper;
        this.attachmentMapper = attachmentMapper;
        this.unitTaskMapper = unitTaskMapper;
        this.actionLogger = actionLogger;
        this.masterData = masterData;
    }

    // ===== API-501 列表 =====
    @Override
    public PageResult<DeptAuditListItemVO> list(DeptAuditListRequest request) {
        int page = request.getPage();
        int size = request.getPageSize();

        Long currentDeptId = currentUserDeptId();

        LambdaQueryWrapper<OpinionDeptTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OpinionDeptTaskDO::getSubmitStatus, OpinionDeptSubmitStatus.SUBMITTED.name());
        if (!CollectionUtils.isEmpty(request.getAuditStatus())) {
            wrapper.in(OpinionDeptTaskDO::getAuditStatus, request.getAuditStatus());
        }

        List<OpinionDeptTaskDO> all = deptTaskRepo.selectList(wrapper);
        long total = all.size();
        int from = (page - 1) * size;
        int to = Math.min(from + size, all.size());
        List<OpinionDeptTaskDO> tasks = from < all.size() ? all.subList(from, to) : List.of();
        if (tasks.isEmpty()) {
            return new PageResult<>(total, Collections.emptyList());
        }

        List<DeptAuditListItemVO> rows = new ArrayList<>();
        for (OpinionDeptTaskDO t : tasks) {
            DeptAuditListItemVO vo = new DeptAuditListItemVO();
            vo.setTaskId(t.getId());
            vo.setSurveyId(t.getSurveyId());
            vo.setSurveyName(null);
            vo.setAssessYear(request.getAssessYear());
            vo.setModuleCode(t.getModuleCode());
            vo.setModuleName(moduleNameText(t.getModuleCode()));
            vo.setDeptDeadline(null);
            vo.setSubmitStatus(t.getSubmitStatus());
            vo.setSubmitStatusText(submitStatusText(t.getSubmitStatus()));
            vo.setAuditStatus(t.getAuditStatus());
            vo.setAuditStatusText(deptAuditStatusText(t.getAuditStatus()));
            vo.setSurveyStatus(null);
            vo.setSurveyStatusText(null);
            rows.add(vo);
        }
        return new PageResult<>(total, rows);
    }

    // ===== API-502 详情 =====
    @Override
    public DeptAuditDetailVO detail(Long taskId) {
        OpinionDeptTaskDO task = requireDeptTask(taskId);
        ensureDeptAccess(task);

        OpinionSurveyDO survey = requireSurvey(task.getSurveyId());
        ensureDeptFeedbackStatus(survey);

        DeptAuditDetailVO vo = new DeptAuditDetailVO();

        DeptAuditDetailVO.TaskInfo taskInfo = new DeptAuditDetailVO.TaskInfo();
        taskInfo.setId(task.getId());
        taskInfo.setSurveyId(task.getSurveyId());
        taskInfo.setSurveyName(survey.getName());
        taskInfo.setAssessYear(survey.getAssessYear());
        taskInfo.setSurveyStatus(survey.getStatus());
        taskInfo.setSurveyStatusText(mainStatusText(survey.getStatus()));
        taskInfo.setSubmitStatus(task.getSubmitStatus());
        taskInfo.setSubmitStatusText(submitStatusText(task.getSubmitStatus()));
        taskInfo.setAuditStatus(task.getAuditStatus());
        taskInfo.setAuditStatusText(deptAuditStatusText(task.getAuditStatus()));
        taskInfo.setDeptDeadline(survey.getDeptDeadline());
        taskInfo.setSubmittedAt(task.getSubmittedAt());
        taskInfo.setLastRejectReason(task.getLastRejectReason());
        vo.setTask(taskInfo);

        List<OpinionSurveyModuleDO> modules = moduleMapper.selectList(
            new LambdaQueryWrapper<OpinionSurveyModuleDO>()
                .eq(OpinionSurveyModuleDO::getSurveyId, task.getSurveyId())
                .orderByAsc(OpinionSurveyModuleDO::getDisplayOrder));

        Map<Long, List<OpinionAttachmentDO>> moduleAttMap = loadModuleAttachments(task.getSurveyId());

        List<DeptAuditDetailVO.ModuleInfo> moduleVOs = new ArrayList<>();
        for (OpinionSurveyModuleDO m : modules) {
            DeptAuditDetailVO.ModuleInfo mi = new DeptAuditDetailVO.ModuleInfo();
            mi.setModuleCode(m.getModuleCode());
            mi.setModuleName(m.getModuleName());
            List<DeptAuditDetailVO.AttachmentInfo> atts = new ArrayList<>();
            for (OpinionAttachmentDO a : moduleAttMap.getOrDefault(m.getId(), Collections.emptyList())) {
                DeptAuditDetailVO.AttachmentInfo ai = new DeptAuditDetailVO.AttachmentInfo();
                ai.setFileId(a.getFileId());
                ai.setFileName(a.getFileName());
                atts.add(ai);
            }
            mi.setAttachments(atts);
            moduleVOs.add(mi);
        }
        vo.setModules(moduleVOs);

        List<OpinionFeedbackDO> feedbacks = feedbackRepo.listByDeptTaskId(taskId);
        Map<Long, OpinionFeedbackDO> feedbackMap = new HashMap<>();
        for (OpinionFeedbackDO fb : feedbacks) {
            feedbackMap.put(fb.getItemId(), fb);
        }

        List<Long> itemIds = feedbacks.stream()
            .map(OpinionFeedbackDO::getItemId)
            .distinct()
            .collect(Collectors.toList());
        List<OpinionItemDO> items = itemIds.isEmpty() ? Collections.emptyList()
            : itemMapper.selectBatchIds(itemIds);

        Map<Long, OpinionItemDO> itemMap = new HashMap<>();
        for (OpinionItemDO item : items) {
            itemMap.put(item.getId(), item);
        }

        Map<Long, String> unitNameMap = loadUnitNamesForItems(items);

        List<DeptAuditDetailVO.FeedbackItemInfo> itemVOs = new ArrayList<>();
        for (OpinionFeedbackDO fb : feedbacks) {
            DeptAuditDetailVO.FeedbackItemInfo fi = new DeptAuditDetailVO.FeedbackItemInfo();
            fi.setItemId(fb.getItemId());
            fi.setIsAdopted(fb.getIsAdopted());
            fi.setAdoptionRemark(fb.getAdoptionRemark());
            fi.setRemark(fb.getRemark());

            OpinionItemDO item = itemMap.get(fb.getItemId());
            if (item != null) {
                fi.setModuleCode(item.getModuleCode());
                fi.setIndicatorCategory(item.getIndicatorCategory());
                fi.setIndicatorName(item.getIndicatorName());
                fi.setFactorName(item.getFactorName());
                fi.setUnitName(unitNameMap.getOrDefault(item.getUnitTaskId(), ""));
                fi.setOpinionCategory(item.getOpinionCategory());
                fi.setOpinionContent(item.getOpinionContent());
                fi.setReason(item.getReason());
            }
            itemVOs.add(fi);
        }
        vo.setItems(itemVOs);

        List<OpinionDeptAuditLogDO> logs = deptAuditLogRepo.listByDeptTaskId(taskId);
        List<DeptAuditDetailVO.AuditLogInfo> logVOs = new ArrayList<>();
        for (OpinionDeptAuditLogDO l : logs) {
            DeptAuditDetailVO.AuditLogInfo li = new DeptAuditDetailVO.AuditLogInfo();
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

    // ===== API-503 审核通过 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeptAuditStatusVO pass(Long taskId) {
        OpinionDeptTaskDO task = requireDeptTask(taskId);
        ensureDeptAccess(task);

        OpinionSurveyDO survey = requireSurvey(task.getSurveyId());
        ensureDeptFeedbackStatus(survey);

        if (!OpinionDeptAuditStatus.PENDING.name().equals(task.getAuditStatus())) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE);
        }

        Long actor = masterData.currentUserId();

        task.setAuditStatus(OpinionDeptAuditStatus.PASS.name());
        task.setAuditedAt(LocalDateTime.now());
        task.setAuditedBy(actor);
        deptTaskRepo.updateById(task);

        writeAuditLog(task, OpinionAuditAction.PASS.name(), null, actor);
        actionLogger.log(task.getSurveyId(), OpinionActionLogAction.DEPT_AUDIT_PASS,
            OpinionDeptAuditStatus.PENDING.name(), OpinionDeptAuditStatus.PASS.name(),
            ACTOR_ROLE_R05, actor,
            "dept_task_id=" + taskId);

        log.info("[opinion] dept audit pass: taskId={}, auditor={}", taskId, actor);
        return toStatusVO(task);
    }

    // ===== API-504 审核退回 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeptAuditStatusVO reject(Long taskId, DeptAuditRejectRequest request) {
        String rejectReason = request.getRejectReason();
        if (!StringUtils.hasText(rejectReason)) {
            throw new BusinessException(ResultCode.OPINION_REJECT_REASON_REQUIRED);
        }

        OpinionDeptTaskDO task = requireDeptTask(taskId);
        ensureDeptAccess(task);

        OpinionSurveyDO survey = requireSurvey(task.getSurveyId());
        ensureDeptFeedbackStatus(survey);

        if (!OpinionDeptAuditStatus.PENDING.name().equals(task.getAuditStatus())) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE);
        }

        Long actor = masterData.currentUserId();

        task.setSubmitStatus(OpinionDeptSubmitStatus.PENDING.name());
        task.setAuditStatus(OpinionDeptAuditStatus.REJECTED.name());
        task.setLastRejectReason(rejectReason);
        task.setAuditedAt(LocalDateTime.now());
        task.setAuditedBy(actor);
        deptTaskRepo.updateById(task);

        writeAuditLog(task, OpinionAuditAction.REJECT.name(), rejectReason, actor);
        actionLogger.log(task.getSurveyId(), OpinionActionLogAction.DEPT_AUDIT_REJECT,
            OpinionDeptAuditStatus.PENDING.name(), OpinionDeptAuditStatus.REJECTED.name(),
            ACTOR_ROLE_R05, actor,
            "dept_task_id=" + taskId + " reason=" + truncateReason(rejectReason));

        log.info("[opinion] dept audit reject: taskId={}, auditor={}, reason={}", taskId, actor, rejectReason);
        return toStatusVO(task);
    }

    // ====================================================================
    // ==================== Helper ========================================
    // ====================================================================

    private Long currentUserDeptId() {
        return 1L;
    }

    private OpinionDeptTaskDO requireDeptTask(Long taskId) {
        OpinionDeptTaskDO task = taskId == null ? null : deptTaskRepo.findById(taskId);
        if (task == null) {
            throw new BusinessException(ResultCode.OPINION_TASK_NOT_FOUND);
        }
        return task;
    }

    private OpinionSurveyDO requireSurvey(Long surveyId) {
        OpinionSurveyDO survey = surveyId == null ? null : surveyMapper.selectById(surveyId);
        if (survey == null) {
            throw new BusinessException(ResultCode.OPINION_NOT_FOUND);
        }
        return survey;
    }

    private void ensureDeptAccess(OpinionDeptTaskDO task) {
    }

    private void ensureDeptFeedbackStatus(OpinionSurveyDO survey) {
        if (!OpinionMainStatus.DEPT_FEEDBACK.name().equals(survey.getStatus())) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE);
        }
    }

    private void writeAuditLog(OpinionDeptTaskDO task, String action, String rejectReason, Long actor) {
        OpinionDeptAuditLogDO log = new OpinionDeptAuditLogDO();
        log.setDeptTaskId(task.getId());
        log.setSurveyId(task.getSurveyId());
        log.setAction(action);
        log.setActorRole(ACTOR_ROLE_R05);
        log.setActorId(actor);
        log.setRejectReason(rejectReason);
        log.setCreatedAt(LocalDateTime.now());
        deptAuditLogRepo.insert(log);
    }

    private DeptAuditStatusVO toStatusVO(OpinionDeptTaskDO task) {
        DeptAuditStatusVO vo = new DeptAuditStatusVO();
        vo.setTaskId(task.getId());
        vo.setAuditStatus(task.getAuditStatus());
        vo.setAuditStatusText(deptAuditStatusText(task.getAuditStatus()));
        return vo;
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

    private Map<Long, String> loadUnitNamesForItems(List<OpinionItemDO> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Long> unitTaskIds = items.stream()
            .map(OpinionItemDO::getUnitTaskId)
            .distinct()
            .collect(Collectors.toList());
        if (unitTaskIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<OpinionUnitTaskDO> unitTasks = unitTaskMapper.selectBatchIds(unitTaskIds);
        Map<Long, String> map = new HashMap<>();
        for (OpinionUnitTaskDO ut : unitTasks) {
            map.put(ut.getId(), ut.getUnitName());
        }
        return map;
    }

    private String submitStatusText(String status) {
        try {
            return OpinionDeptSubmitStatus.valueOf(status).getText();
        } catch (Exception e) {
            return status;
        }
    }

    private String deptAuditStatusText(String status) {
        try {
            return OpinionDeptAuditStatus.valueOf(status).getText();
        } catch (Exception e) {
            return status;
        }
    }

    private String mainStatusText(String status) {
        OpinionMainStatus s = OpinionMainStatus.fromName(status);
        return s == null ? status : s.getText();
    }

    private String moduleNameText(String moduleCode) {
        com.hk.demo.api.enums.opinion.OpinionModuleCode mc =
            com.hk.demo.api.enums.opinion.OpinionModuleCode.fromName(moduleCode);
        return mc == null ? moduleCode : mc.getText();
    }

    private String truncateReason(String reason) {
        if (reason == null) return null;
        return reason.length() > 100 ? reason.substring(0, 100) : reason;
    }
}
