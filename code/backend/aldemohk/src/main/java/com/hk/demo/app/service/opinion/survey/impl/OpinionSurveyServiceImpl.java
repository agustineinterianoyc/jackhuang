package com.hk.demo.app.service.opinion.survey.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hk.demo.api.enums.ResultCode;
import com.hk.demo.api.enums.opinion.OpinionActionLogAction;
import com.hk.demo.api.enums.opinion.OpinionAttachmentBizType;
import com.hk.demo.api.enums.opinion.OpinionAuditAction;
import com.hk.demo.api.enums.opinion.OpinionMainStatus;
import com.hk.demo.api.enums.opinion.OpinionModuleCode;
import com.hk.demo.api.enums.opinion.OpinionUnitFillStatus;
import com.hk.demo.api.enums.opinion.OpinionUnitAuditStatus;
import com.hk.demo.app.mapper.opinion.OpinionAttachmentMapper;
import com.hk.demo.app.mapper.opinion.OpinionDeptTaskMapper;
import com.hk.demo.app.mapper.opinion.OpinionFeedbackMapper;
import com.hk.demo.app.mapper.opinion.OpinionItemMapper;
import com.hk.demo.app.mapper.opinion.OpinionSummaryItemMapper;
import com.hk.demo.app.mapper.opinion.OpinionSurveyMapper;
import com.hk.demo.app.mapper.opinion.OpinionSurveyModuleMapper;
import com.hk.demo.app.mapper.opinion.OpinionSurveyTargetMapper;
import com.hk.demo.app.mapper.opinion.OpinionUnitAuditLogMapper;
import com.hk.demo.app.mapper.opinion.OpinionUnitTaskMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionAttachmentDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionDeptTaskDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionFeedbackDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionItemDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionSummaryItemDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionSurveyDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionSurveyModuleDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionSurveyTargetDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionUnitAuditLogDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionUnitTaskDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionDeptAuditLogDO;
import com.hk.demo.app.model.request.opinion.survey.SurveyListRequest;
import com.hk.demo.app.model.request.opinion.survey.SurveySaveRequest;
import com.hk.demo.app.model.response.opinion.survey.SurveyDetailVO;
import com.hk.demo.app.model.response.opinion.survey.SurveyListItemVO;
import com.hk.demo.app.model.response.opinion.survey.SurveyPublishVO;
import com.hk.demo.app.model.response.opinion.survey.SurveyStartVO;
import com.hk.demo.app.model.response.opinion.survey.SurveyStatusVO;
import com.hk.demo.app.service.opinion.log.OpinionActionLogger;
import com.hk.demo.app.service.opinion.mock.OpinionMockMasterDataProvider;
import com.hk.demo.app.service.opinion.mock.OpinionMockMasterDataProvider.DeptInfo;
import com.hk.demo.app.service.opinion.mock.OpinionMockMasterDataProvider.UnitInfo;
import com.hk.demo.app.service.opinion.state.OpinionStateMachine;
import com.hk.demo.app.service.opinion.survey.OpinionSurveyService;
import com.hk.demo.core.exception.BusinessException;
import com.hk.demo.data.pagination.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 征集任务管理服务实现。
 */
@Service
public class OpinionSurveyServiceImpl implements OpinionSurveyService {

    private static final Logger log = LoggerFactory.getLogger(OpinionSurveyServiceImpl.class);

    private static final String ACTOR_ROLE_R01 = "R01";
    private static final String ACTOR_ROLE_SYSTEM = "SYSTEM";

    private final OpinionSurveyMapper surveyMapper;
    private final OpinionSurveyModuleMapper moduleMapper;
    private final OpinionSurveyTargetMapper targetMapper;
    private final OpinionAttachmentMapper attachmentMapper;
    private final OpinionUnitTaskMapper unitTaskMapper;
    private final OpinionUnitAuditLogMapper unitAuditLogMapper;
    private final OpinionDeptTaskMapper deptTaskMapper;
    private final OpinionFeedbackMapper feedbackMapper;
    private final OpinionSummaryItemMapper summaryItemMapper;
    private final OpinionItemMapper itemMapper;
    private final OpinionStateMachine stateMachine;
    private final OpinionActionLogger actionLogger;
    private final OpinionMockMasterDataProvider masterData;
    private final JdbcTemplate jdbc;

    @Autowired
    public OpinionSurveyServiceImpl(OpinionSurveyMapper surveyMapper,
                                    OpinionSurveyModuleMapper moduleMapper,
                                    OpinionSurveyTargetMapper targetMapper,
                                    OpinionAttachmentMapper attachmentMapper,
                                    OpinionUnitTaskMapper unitTaskMapper,
                                    OpinionUnitAuditLogMapper unitAuditLogMapper,
                                    OpinionDeptTaskMapper deptTaskMapper,
                                    OpinionFeedbackMapper feedbackMapper,
                                    OpinionSummaryItemMapper summaryItemMapper,
                                    OpinionItemMapper itemMapper,
                                    OpinionStateMachine stateMachine,
                                    OpinionActionLogger actionLogger,
                                    OpinionMockMasterDataProvider masterData,
                                    JdbcTemplate jdbc) {
        this.surveyMapper = surveyMapper;
        this.moduleMapper = moduleMapper;
        this.targetMapper = targetMapper;
        this.attachmentMapper = attachmentMapper;
        this.unitTaskMapper = unitTaskMapper;
        this.unitAuditLogMapper = unitAuditLogMapper;
        this.deptTaskMapper = deptTaskMapper;
        this.feedbackMapper = feedbackMapper;
        this.summaryItemMapper = summaryItemMapper;
        this.itemMapper = itemMapper;
        this.stateMachine = stateMachine;
        this.actionLogger = actionLogger;
        this.masterData = masterData;
        this.jdbc = jdbc;
    }

    // ===== API-101 创建 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SurveyStatusVO create(SurveySaveRequest request) {
        validateSaveRequest(request);
        ensureNameUnique(request.getAssessYear(), request.getName(), null);

        Long actor = masterData.currentUserId();

        OpinionSurveyDO entity = new OpinionSurveyDO();
        entity.setName(request.getName());
        entity.setAssessYear(request.getAssessYear());
        entity.setNoticeContent(request.getNoticeContent());
        entity.setRemark(request.getRemark());
        entity.setUnitDeadline(request.getUnitDeadline());
        entity.setDeptDeadline(request.getDeptDeadline());
        entity.setStatus(OpinionMainStatus.DRAFT.name());
        entity.setCreatedBy(actor);
        entity.setUpdatedBy(actor);
        surveyMapper.insert(entity);

        saveModulesAndTargets(entity.getId(), request, actor);

        log.info("[opinion] survey created: id={}, name={}", entity.getId(), entity.getName());
        return toStatusVO(entity);
    }

    // ===== API-102 编辑 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SurveyStatusVO update(Long id, SurveySaveRequest request) {
        validateSaveRequest(request);
        OpinionSurveyDO entity = requireExisting(id);
        ensureDraft(entity);
        ensureNameUnique(request.getAssessYear(), request.getName(), id);

        entity.setName(request.getName());
        entity.setAssessYear(request.getAssessYear());
        entity.setNoticeContent(request.getNoticeContent());
        entity.setRemark(request.getRemark());
        entity.setUnitDeadline(request.getUnitDeadline());
        entity.setDeptDeadline(request.getDeptDeadline());
        entity.setUpdatedBy(masterData.currentUserId());
        surveyMapper.updateById(entity);

        replaceModulesAndTargets(id, request, entity.getUpdatedBy());

        return toStatusVO(entity);
    }

    // ===== API-103 删除 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        OpinionSurveyDO entity = requireExisting(id);
        ensureDraft(entity);

        surveyMapper.deleteById(id); // @TableLogic 自动逻辑删除
        actionLogger.log(id, OpinionActionLogAction.DELETE_SURVEY,
            entity.getStatus(), entity.getStatus(),
            ACTOR_ROLE_R01, masterData.currentUserId(),
            "delete draft survey: " + entity.getName());
    }

    // ===== API-104 开启征集 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SurveyStartVO start(Long id) {
        OpinionSurveyDO entity = requireExisting(id);

        OpinionMainStatus from = OpinionMainStatus.fromName(entity.getStatus());
        OpinionMainStatus to = OpinionMainStatus.WAIT_FILL;
        stateMachine.assertCurrent(from, OpinionMainStatus.DRAFT);
        stateMachine.assertTransition(from, to);

        // 校验配置完整：模块 + 对象 至少各 1
        long moduleCount = moduleMapper.selectCount(new LambdaQueryWrapper<OpinionSurveyModuleDO>()
            .eq(OpinionSurveyModuleDO::getSurveyId, id));
        long targetCount = targetMapper.selectCount(new LambdaQueryWrapper<OpinionSurveyTargetDO>()
            .eq(OpinionSurveyTargetDO::getSurveyId, id));
        if (moduleCount == 0 || targetCount == 0) {
            throw new BusinessException(ResultCode.OPINION_INCOMPLETE_CONFIG);
        }

        Long actor = masterData.currentUserId();

        // 推进状态 + 时间
        entity.setStatus(to.name());
        entity.setStartAt(LocalDateTime.now());
        entity.setUpdatedBy(actor);
        surveyMapper.updateById(entity);

        // 批量初始化 ad_opinion_unit_task（PENDING / NONE）
        List<OpinionSurveyTargetDO> targets = targetMapper.selectList(
            new LambdaQueryWrapper<OpinionSurveyTargetDO>().eq(OpinionSurveyTargetDO::getSurveyId, id));
        int created = 0;
        for (OpinionSurveyTargetDO t : targets) {
            UnitInfo info = masterData.findUnit(t.getUnitId());
            OpinionUnitTaskDO task = new OpinionUnitTaskDO();
            task.setSurveyId(id);
            task.setUnitId(t.getUnitId());
            task.setUnitName(info != null ? info.name() : ("单位 #" + t.getUnitId()));
            task.setUnitType(t.getUnitType());
            task.setFillStatus(OpinionUnitFillStatus.PENDING.name());
            task.setAuditStatus(OpinionUnitAuditStatus.NONE.name());
            task.setAutoZeroReport(0);
            unitTaskMapper.insert(task);
            created++;
        }

        actionLogger.log(id, OpinionActionLogAction.START_SURVEY,
            from.name(), to.name(), ACTOR_ROLE_R01, actor,
            "init unit_task=" + created);

        // TODO(STEP-006): 通知中心打桩；首期仅写审计日志
        return SurveyStartVO.of(id, to.name(), to.getText(), created);
    }

    // ===== API-110 绩效退回基层 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SurveyStatusVO opsRejectUnit(Long surveyId, Long unitTaskId, String rejectReason) {
        OpinionSurveyDO survey = requireExisting(surveyId);
        OpinionMainStatus mainStatus = OpinionMainStatus.fromName(survey.getStatus());
        stateMachine.assertCurrent(mainStatus, OpinionMainStatus.FILLING);

        OpinionUnitTaskDO task = unitTaskMapper.selectById(unitTaskId);
        if (task == null || !task.getSurveyId().equals(surveyId)) {
            throw new BusinessException(ResultCode.OPINION_TASK_NOT_FOUND);
        }
        if (!OpinionUnitAuditStatus.PENDING.name().equals(task.getAuditStatus())) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE.getCode(),
                "仅待审核状态的基层任务可被退回");
        }
        if (rejectReason == null || rejectReason.isBlank()) {
            throw new BusinessException(ResultCode.OPINION_REJECT_REASON_REQUIRED);
        }

        Long actor = masterData.currentUserId();
        task.setFillStatus(OpinionUnitFillStatus.PENDING.name());
        task.setAuditStatus(OpinionUnitAuditStatus.REJECTED.name());
        task.setLastRejectReason(rejectReason);
        unitTaskMapper.updateById(task);

        OpinionUnitAuditLogDO auditLog = new OpinionUnitAuditLogDO();
        auditLog.setUnitTaskId(unitTaskId);
        auditLog.setSurveyId(surveyId);
        auditLog.setAction(OpinionAuditAction.OPS_REJECT.name());
        auditLog.setActorRole(ACTOR_ROLE_R01);
        auditLog.setActorId(actor);
        auditLog.setRejectReason(rejectReason);
        unitAuditLogMapper.insert(auditLog);

        actionLogger.log(surveyId, OpinionActionLogAction.OPS_REJECT_UNIT,
            null, null, ACTOR_ROLE_R01, actor,
            "taskId=" + unitTaskId + " reason=" + truncateReason(rejectReason));

        return toStatusVO(survey);
    }

    // ===== API-105 开启专业反馈 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SurveyStatusVO openDeptFeedback(Long surveyId) {
        OpinionSurveyDO survey = requireExisting(surveyId);

        OpinionMainStatus from = OpinionMainStatus.fromName(survey.getStatus());
        OpinionMainStatus to = OpinionMainStatus.DEPT_FEEDBACK;
        stateMachine.assertTransition(from, to);

        List<OpinionUnitTaskDO> unitTasks = unitTaskMapper.selectList(
            new LambdaQueryWrapper<OpinionUnitTaskDO>()
                .eq(OpinionUnitTaskDO::getSurveyId, surveyId));
        for (OpinionUnitTaskDO ut : unitTasks) {
            if (!OpinionUnitAuditStatus.PASS.name().equals(ut.getAuditStatus())) {
                throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE.getCode(),
                    "存在尚未通过审核的基层意见，不可开启专业反馈");
            }
        }

        Long actor = masterData.currentUserId();
        LocalDateTime now = LocalDateTime.now();

        survey.setStatus(to.name());
        survey.setDeptOpenAt(now);
        survey.setUpdatedBy(actor);
        surveyMapper.updateById(survey);

        List<OpinionSurveyModuleDO> modules = moduleMapper.selectList(
            new LambdaQueryWrapper<OpinionSurveyModuleDO>()
                .eq(OpinionSurveyModuleDO::getSurveyId, surveyId));

        List<OpinionItemDO> items = itemMapper.selectList(
            new LambdaQueryWrapper<OpinionItemDO>()
                .eq(OpinionItemDO::getSurveyId, surveyId)
                .eq(OpinionItemDO::getModuleCode, modules.stream().map(OpinionSurveyModuleDO::getModuleCode).collect(Collectors.toList())));

        int created = 0;
        List<DeptInfo> deptList = masterData.listAllDepts();
        for (DeptInfo dept : deptList) {
            for (OpinionSurveyModuleDO mod : modules) {
                OpinionDeptTaskDO deptTask = new OpinionDeptTaskDO();
                deptTask.setSurveyId(surveyId);
                deptTask.setDepartmentId(dept.id());
                deptTask.setDepartmentName(dept.name());
                deptTask.setModuleCode(mod.getModuleCode());
                deptTask.setSubmitStatus("PENDING");
                deptTask.setAuditStatus("NONE");
                deptTaskMapper.insert(deptTask);

                for (OpinionItemDO item : items) {
                    if (item.getModuleCode().equals(mod.getModuleCode())) {
                        OpinionFeedbackDO fb = new OpinionFeedbackDO();
                        fb.setSurveyId(surveyId);
                        fb.setDeptTaskId(deptTask.getId());
                        fb.setItemId(item.getId());
                        fb.setIsAdopted(null);
                        fb.setAdoptionRemark(null);
                        feedbackMapper.insert(fb);
                    }
                }
                created++;
            }
        }

        actionLogger.log(surveyId, OpinionActionLogAction.START_DEPT_FEEDBACK,
            from.name(), to.name(), ACTOR_ROLE_R01, actor,
            "created dept_task=" + created);

        log.info("[opinion] dept feedback opened: surveyId={}, deptTasks={}", surveyId, created);
        return toStatusVO(survey);
    }

    // ===== API-111 绩效退回专业 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SurveyStatusVO opsRejectDept(Long surveyId, Long deptTaskId, String rejectReason) {
        OpinionSurveyDO survey = requireExisting(surveyId);
        OpinionMainStatus mainStatus = OpinionMainStatus.fromName(survey.getStatus());
        stateMachine.assertCurrent(mainStatus, OpinionMainStatus.DEPT_FEEDBACK);

        OpinionDeptTaskDO task = deptTaskMapper.selectById(deptTaskId);
        if (task == null || !task.getSurveyId().equals(surveyId)) {
            throw new BusinessException(ResultCode.OPINION_TASK_NOT_FOUND);
        }
        if (!"PENDING".equals(task.getAuditStatus())) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE.getCode(),
                "仅待审核状态的专业任务可被退回");
        }
        if (rejectReason == null || rejectReason.isBlank()) {
            throw new BusinessException(ResultCode.OPINION_REJECT_REASON_REQUIRED);
        }

        Long actor = masterData.currentUserId();
        task.setSubmitStatus("PENDING");
        task.setAuditStatus("REJECTED");
        task.setLastRejectReason(rejectReason);
        deptTaskMapper.updateById(task);

        actionLogger.log(surveyId, OpinionActionLogAction.OPS_REJECT_DEPT,
            null, null, ACTOR_ROLE_R01, actor,
            "deptTaskId=" + deptTaskId + " reason=" + truncateReason(rejectReason));

        return toStatusVO(survey);
    }

    // ===== API-108 列表 =====
    @Override
    public PageResult<SurveyListItemVO> list(SurveyListRequest request) {
        int page = request.getPage() == null ? 1 : Math.max(1, request.getPage());
        int size = request.getPageSize() == null ? 20 : Math.max(1, Math.min(200, request.getPageSize()));

        LambdaQueryWrapper<OpinionSurveyDO> wrapper = new LambdaQueryWrapper<>();
        if (request.getAssessYear() != null) {
            wrapper.eq(OpinionSurveyDO::getAssessYear, request.getAssessYear());
        }
        if (StringUtils.hasText(request.getName())) {
            wrapper.like(OpinionSurveyDO::getName, request.getName().trim());
        }
        if (!CollectionUtils.isEmpty(request.getStatus())) {
            wrapper.in(OpinionSurveyDO::getStatus, request.getStatus());
        }
        wrapper.orderByDesc(OpinionSurveyDO::getCreatedAt);

        List<OpinionSurveyDO> all = surveyMapper.selectList(wrapper);
        long total = all.size();
        int from = (page - 1) * size;
        int to = Math.min(from + size, all.size());
        List<OpinionSurveyDO> records = from < all.size() ? all.subList(from, to) : List.of();

        List<SurveyListItemVO> rows = new ArrayList<>();
        Map<Long, ProgressPair> progressMap = loadProgress(records.stream().map(OpinionSurveyDO::getId).toList());

        for (OpinionSurveyDO s : records) {
            SurveyListItemVO vo = new SurveyListItemVO();
            vo.setId(s.getId());
            vo.setName(s.getName());
            vo.setAssessYear(s.getAssessYear());
            vo.setStatus(s.getStatus());
            vo.setStatusText(statusText(s.getStatus()));
            vo.setUnitDeadline(s.getUnitDeadline());
            vo.setDeptDeadline(s.getDeptDeadline());
            vo.setCreatedAt(s.getCreatedAt());

            ProgressPair pp = progressMap.getOrDefault(s.getId(), ProgressPair.EMPTY);
            vo.setUnitProgress(pp.unitTotal == 0 ? "-" : (pp.unitSubmitted + "/" + pp.unitTotal));
            vo.setDeptProgress(pp.deptTotal == 0 ? "-" : (pp.deptSubmitted + "/" + pp.deptTotal));
            rows.add(vo);
        }
        return new PageResult<>(total, rows);
    }

    // ===== API-109 详情 =====
    @Override
    public SurveyDetailVO detail(Long id) {
        OpinionSurveyDO entity = requireExisting(id);

        SurveyDetailVO vo = new SurveyDetailVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setAssessYear(entity.getAssessYear());
        vo.setNoticeContent(entity.getNoticeContent());
        vo.setRemark(entity.getRemark());
        vo.setUnitDeadline(entity.getUnitDeadline());
        vo.setDeptDeadline(entity.getDeptDeadline());
        vo.setStatus(entity.getStatus());
        vo.setStatusText(statusText(entity.getStatus()));
        vo.setStartAt(entity.getStartAt());
        vo.setPublishAt(entity.getPublishAt());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setUpdatedAt(entity.getUpdatedAt());

        // 模块
        List<OpinionSurveyModuleDO> modules = moduleMapper.selectList(
            new LambdaQueryWrapper<OpinionSurveyModuleDO>()
                .eq(OpinionSurveyModuleDO::getSurveyId, id)
                .orderByAsc(OpinionSurveyModuleDO::getDisplayOrder));

        // 模块附件（一次取出按 biz_ref_id 分组）
        Map<Long, List<OpinionAttachmentDO>> moduleAttMap = loadModuleAttachments(id);

        List<SurveyDetailVO.ModuleVO> moduleVOs = new ArrayList<>();
        for (OpinionSurveyModuleDO m : modules) {
            SurveyDetailVO.ModuleVO mv = new SurveyDetailVO.ModuleVO();
            mv.setId(m.getId());
            mv.setModuleCode(m.getModuleCode());
            mv.setModuleName(m.getModuleName());
            mv.setModuleAlias(m.getModuleAlias());
            mv.setDisplayOrder(m.getDisplayOrder());
            List<SurveyDetailVO.AttachmentVO> atts = new ArrayList<>();
            for (OpinionAttachmentDO a : moduleAttMap.getOrDefault(m.getId(), Collections.emptyList())) {
                SurveyDetailVO.AttachmentVO av = new SurveyDetailVO.AttachmentVO();
                av.setId(a.getId());
                av.setFileId(a.getFileId());
                av.setFileName(a.getFileName());
                av.setFileSize(a.getFileSize());
                atts.add(av);
            }
            mv.setAttachments(atts);
            moduleVOs.add(mv);
        }
        vo.setModules(moduleVOs);

        // 征集对象
        List<OpinionSurveyTargetDO> targets = targetMapper.selectList(
            new LambdaQueryWrapper<OpinionSurveyTargetDO>()
                .eq(OpinionSurveyTargetDO::getSurveyId, id));
        List<SurveyDetailVO.TargetVO> targetVOs = new ArrayList<>();
        for (OpinionSurveyTargetDO t : targets) {
            UnitInfo info = masterData.findUnit(t.getUnitId());
            SurveyDetailVO.TargetVO tv = new SurveyDetailVO.TargetVO();
            tv.setUnitId(t.getUnitId());
            tv.setUnitName(info != null ? info.name() : ("单位 #" + t.getUnitId()));
            tv.setUnitType(t.getUnitType());
            tv.setUnitTypeText(info != null ? info.type().getText() : t.getUnitType());
            targetVOs.add(tv);
        }
        vo.setTargets(targetVOs);

        return vo;
    }

    // ===== API-106 汇总 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SurveyStatusVO summarize(Long surveyId) {
        OpinionSurveyDO survey = requireExisting(surveyId);
        OpinionMainStatus from = OpinionMainStatus.fromName(survey.getStatus());
        OpinionMainStatus to = OpinionMainStatus.DONE;
        stateMachine.assertTransition(from, to);

        // 用 JDBC 绕过 MyBatis-Plus selectList 兼容问题
        String checkSql = "SELECT COUNT(*) as total, SUM(CASE WHEN audit_status='PASS' THEN 1 ELSE 0 END) as passed FROM ad_opinion_dept_task WHERE survey_id = ? AND deleted_flag = 0";
        Map<String, Object> check = jdbc.queryForMap(checkSql, surveyId);
        long total = ((Number) check.get("total")).longValue();
        long passed = ((Number) check.get("passed")).longValue();
        if (total == 0 || passed != total) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE.getCode(),
                "存在尚未审核通过的专业任务，不可汇总");
        }

        Long actor = masterData.currentUserId();
        LocalDateTime now = LocalDateTime.now();

        survey.setStatus(to.name());
        survey.setUpdatedBy(actor);
        surveyMapper.updateById(survey);

        // 先逻辑删除旧汇总行，再初始化
        jdbc.update("UPDATE ad_opinion_summary_item SET deleted_flag = 1 WHERE survey_id = ? AND deleted_flag = 0",
            surveyId);

        String insertSql = "INSERT INTO ad_opinion_summary_item (survey_id, feedback_id, item_id, final_is_adopted, final_adoption_remark, adjusted_content, deleted_flag) VALUES (?, ?, ?, 0, NULL, NULL, 0) ON DUPLICATE KEY UPDATE deleted_flag = 0";

        int created = 0;
        // 用 JDBC 查询反馈 ID
        List<Map<String, Object>> feedbackRows = jdbc.queryForList(
            "SELECT id, item_id FROM ad_opinion_feedback WHERE survey_id = ? AND deleted_flag = 0", surveyId);
        for (Map<String, Object> row : feedbackRows) {
            jdbc.update(insertSql, surveyId, row.get("id"), row.get("item_id"));
            created++;
        }

        actionLogger.log(surveyId, OpinionActionLogAction.SUMMARIZE,
            from.name(), to.name(), ACTOR_ROLE_R01, actor,
            "summary items initialized: " + created);

        log.info("[opinion] survey summarized: id={}, summaryItems={}", surveyId, created);
        return toStatusVO(survey);
    }

    // ===== API-107 发布 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SurveyPublishVO publish(Long surveyId) {
        OpinionSurveyDO survey = requireExisting(surveyId);

        OpinionMainStatus from = OpinionMainStatus.fromName(survey.getStatus());
        OpinionMainStatus to = OpinionMainStatus.PUBLISHED;
        stateMachine.assertCurrent(from, OpinionMainStatus.DONE);
        stateMachine.assertTransition(from, to);

        Long actor = masterData.currentUserId();
        LocalDateTime now = LocalDateTime.now();

        survey.setStatus(to.name());
        survey.setPublishAt(now);
        survey.setUpdatedBy(actor);
        surveyMapper.updateById(survey);

        actionLogger.log(surveyId, OpinionActionLogAction.PUBLISH,
            from.name(), to.name(), ACTOR_ROLE_R01, actor,
            "published at " + now);

        log.info("[opinion] survey published: id={}", surveyId);

        SurveyPublishVO vo = new SurveyPublishVO();
        vo.setId(surveyId);
        vo.setStatus(to.name());
        vo.setStatusText(to.getText());
        vo.setPublishAt(now);
        return vo;
    }

    // ====================================================================
    // ==================== Helper ========================================
    // ====================================================================

    private void validateSaveRequest(SurveySaveRequest request) {
        if (request.getUnitDeadline() == null || request.getDeptDeadline() == null) {
            throw new BusinessException(ResultCode.OPINION_DEADLINE_INVALID);
        }
        if (!request.getDeptDeadline().isAfter(request.getUnitDeadline())) {
            throw new BusinessException(ResultCode.OPINION_DEADLINE_INVALID.getCode(),
                "专业截止时间必须晚于基层截止时间");
        }
        // 模块编码合法性
        Set<String> moduleCodes = new java.util.HashSet<>();
        for (SurveySaveRequest.ModuleItem m : request.getModules()) {
            if (OpinionModuleCode.fromName(m.getModuleCode()) == null) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),
                    "未知模块编码：" + m.getModuleCode());
            }
            if (!moduleCodes.add(m.getModuleCode())) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),
                    "模块编码不能重复：" + m.getModuleCode());
            }
        }
        // 征集对象不能重复
        Set<Long> unitIds = new java.util.HashSet<>();
        for (SurveySaveRequest.TargetItem t : request.getTargets()) {
            if (masterData.findUnit(t.getUnitId()) == null) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),
                    "未知单位 ID：" + t.getUnitId());
            }
            if (!unitIds.add(t.getUnitId())) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),
                    "征集对象不能重复：" + t.getUnitId());
            }
        }
    }

    private void ensureNameUnique(Integer year, String name, Long excludeId) {
        LambdaQueryWrapper<OpinionSurveyDO> q = new LambdaQueryWrapper<>();
        q.eq(OpinionSurveyDO::getAssessYear, year);
        q.eq(OpinionSurveyDO::getName, name);
        if (excludeId != null) {
            q.ne(OpinionSurveyDO::getId, excludeId);
        }
        Long count = surveyMapper.selectCount(q);
        if (count != null && count > 0) {
            throw new BusinessException(ResultCode.OPINION_DUPLICATE_NAME);
        }
    }

    private OpinionSurveyDO requireExisting(Long id) {
        OpinionSurveyDO entity = id == null ? null : surveyMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ResultCode.OPINION_NOT_FOUND);
        }
        return entity;
    }

    private void ensureDraft(OpinionSurveyDO entity) {
        if (!OpinionMainStatus.DRAFT.name().equals(entity.getStatus())) {
            throw new BusinessException(ResultCode.OPINION_NOT_DRAFT);
        }
    }

    private void saveModulesAndTargets(Long surveyId, SurveySaveRequest request, Long actor) {
        // 模块
        for (SurveySaveRequest.ModuleItem m : request.getModules()) {
            OpinionSurveyModuleDO mo = new OpinionSurveyModuleDO();
            mo.setSurveyId(surveyId);
            mo.setModuleCode(m.getModuleCode());
            mo.setModuleName(OpinionModuleCode.valueOf(m.getModuleCode()).getText());
            mo.setModuleAlias(m.getModuleAlias());
            mo.setDisplayOrder(m.getDisplayOrder() == null ? 0 : m.getDisplayOrder());
            moduleMapper.insert(mo);

            if (!CollectionUtils.isEmpty(m.getAttachments())) {
                for (SurveySaveRequest.AttachmentItem a : m.getAttachments()) {
                    OpinionAttachmentDO ad = new OpinionAttachmentDO();
                    ad.setSurveyId(surveyId);
                    ad.setBizType(OpinionAttachmentBizType.MODULE.name());
                    ad.setBizRefId(mo.getId());
                    ad.setFileId(a.getFileId());
                    ad.setFileName(a.getFileName());
                    ad.setFileSize(a.getFileSize() == null ? 0L : a.getFileSize());
                    ad.setCreatedBy(actor);
                    attachmentMapper.insert(ad);
                }
            }
        }
        // 对象
        for (SurveySaveRequest.TargetItem t : request.getTargets()) {
            UnitInfo info = masterData.findUnit(t.getUnitId());
            OpinionSurveyTargetDO td = new OpinionSurveyTargetDO();
            td.setSurveyId(surveyId);
            td.setUnitId(t.getUnitId());
            td.setUnitType(info != null ? info.type().name() : "OTHER");
            targetMapper.insert(td);
        }
    }

    private void replaceModulesAndTargets(Long surveyId, SurveySaveRequest request, Long actor) {
        // 1. 物理删除（MyBatis-Plus 这里走逻辑删除）旧模块、旧附件、旧对象
        moduleMapper.delete(new LambdaQueryWrapper<OpinionSurveyModuleDO>()
            .eq(OpinionSurveyModuleDO::getSurveyId, surveyId));
        attachmentMapper.delete(new LambdaQueryWrapper<OpinionAttachmentDO>()
            .eq(OpinionAttachmentDO::getSurveyId, surveyId)
            .eq(OpinionAttachmentDO::getBizType, OpinionAttachmentBizType.MODULE.name()));
        targetMapper.delete(new LambdaQueryWrapper<OpinionSurveyTargetDO>()
            .eq(OpinionSurveyTargetDO::getSurveyId, surveyId));
        // 2. 重新写入
        saveModulesAndTargets(surveyId, request, actor);
    }

    private SurveyStatusVO toStatusVO(OpinionSurveyDO entity) {
        SurveyStatusVO vo = new SurveyStatusVO();
        vo.setId(entity.getId());
        vo.setStatus(entity.getStatus());
        vo.setStatusText(statusText(entity.getStatus()));
        vo.setStartAt(entity.getStartAt());
        vo.setPublishAt(entity.getPublishAt());
        return vo;
    }

    private String statusText(String status) {
        OpinionMainStatus s = OpinionMainStatus.fromName(status);
        return s == null ? status : s.getText();
    }

    /**
     * 一次性加载多个征集任务的进度（基层 / 专业）。
     */
    private Map<Long, ProgressPair> loadProgress(List<Long> surveyIds) {
        if (surveyIds == null || surveyIds.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, ProgressPair> map = new LinkedHashMap<>();
        for (Long id : surveyIds) {
            map.put(id, new ProgressPair());
        }

        // 基层
        List<OpinionUnitTaskDO> unitTasks = unitTaskMapper.selectList(
            new LambdaQueryWrapper<OpinionUnitTaskDO>()
                .in(OpinionUnitTaskDO::getSurveyId, surveyIds));
        for (OpinionUnitTaskDO t : unitTasks) {
            ProgressPair pp = map.get(t.getSurveyId());
            if (pp == null) continue;
            pp.unitTotal++;
            if (Objects.equals(t.getFillStatus(), OpinionUnitFillStatus.SUBMITTED.name())) {
                pp.unitSubmitted++;
            }
        }

        // 专业
        List<OpinionDeptTaskDO> deptTasks = deptTaskMapper.selectList(
            new LambdaQueryWrapper<OpinionDeptTaskDO>()
                .in(OpinionDeptTaskDO::getSurveyId, surveyIds));
        for (OpinionDeptTaskDO t : deptTasks) {
            ProgressPair pp = map.get(t.getSurveyId());
            if (pp == null) continue;
            pp.deptTotal++;
            // SUBMITTED 视为已反馈
            if ("SUBMITTED".equals(t.getSubmitStatus())) {
                pp.deptSubmitted++;
            }
        }
        return map;
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

    private String truncateReason(String reason) {
        if (reason == null) return null;
        return reason.length() > 100 ? reason.substring(0, 100) : reason;
    }

    private static class ProgressPair {
        static final ProgressPair EMPTY = new ProgressPair();
        int unitSubmitted;
        int unitTotal;
        int deptSubmitted;
        int deptTotal;
    }
}
