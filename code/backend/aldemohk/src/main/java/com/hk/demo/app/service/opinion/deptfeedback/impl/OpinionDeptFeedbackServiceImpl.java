package com.hk.demo.app.service.opinion.deptfeedback.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hk.demo.api.enums.ResultCode;
import com.hk.demo.api.enums.opinion.OpinionActionLogAction;
import com.hk.demo.api.enums.opinion.OpinionAttachmentBizType;
import com.hk.demo.api.enums.opinion.OpinionDeptAuditStatus;
import com.hk.demo.api.enums.opinion.OpinionDeptSubmitStatus;
import com.hk.demo.api.enums.opinion.OpinionMainStatus;
import com.hk.demo.app.mapper.opinion.OpinionAttachmentMapper;
import com.hk.demo.app.mapper.opinion.OpinionItemMapper;
import com.hk.demo.app.mapper.opinion.OpinionSurveyMapper;
import com.hk.demo.app.mapper.opinion.OpinionSurveyModuleMapper;
import com.hk.demo.app.mapper.opinion.OpinionUnitTaskMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionAttachmentDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionDeptTaskDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionFeedbackDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionItemDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionSurveyDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionSurveyModuleDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionUnitTaskDO;
import com.hk.demo.app.model.request.opinion.deptfeedback.DeptFeedbackListRequest;
import com.hk.demo.app.model.request.opinion.deptfeedback.DeptFeedbackSaveRequest;
import com.hk.demo.app.model.response.opinion.deptfeedback.DeptFeedbackDetailVO;
import com.hk.demo.app.model.response.opinion.deptfeedback.DeptFeedbackListItemVO;
import com.hk.demo.app.model.response.opinion.deptfeedback.DeptFeedbackSubmitVO;
import com.hk.demo.app.repository.opinion.deptfeedback.OpinionDeptTaskRepository;
import com.hk.demo.app.repository.opinion.deptfeedback.OpinionFeedbackRepository;
import com.hk.demo.app.service.opinion.deptfeedback.OpinionDeptFeedbackService;
import com.hk.demo.app.service.opinion.log.OpinionActionLogger;
import com.hk.demo.app.service.opinion.mock.OpinionMockMasterDataProvider;
import com.hk.demo.app.service.opinion.mock.OpinionMockMasterDataProvider.DeptInfo;
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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 专业反馈服务实现（R04 专业绩效联络员视角）。
 */
@Service
public class OpinionDeptFeedbackServiceImpl implements OpinionDeptFeedbackService {

    private static final Logger log = LoggerFactory.getLogger(OpinionDeptFeedbackServiceImpl.class);

    private static final String ACTOR_ROLE_R04 = "R04";

    private final OpinionDeptTaskRepository deptTaskRepo;
    private final OpinionFeedbackRepository feedbackRepo;
    private final OpinionItemMapper itemMapper;
    private final OpinionSurveyMapper surveyMapper;
    private final OpinionSurveyModuleMapper moduleMapper;
    private final OpinionAttachmentMapper attachmentMapper;
    private final OpinionUnitTaskMapper unitTaskMapper;
    private final OpinionActionLogger actionLogger;
    private final OpinionMockMasterDataProvider masterData;

    @Autowired
    public OpinionDeptFeedbackServiceImpl(OpinionDeptTaskRepository deptTaskRepo,
                                          OpinionFeedbackRepository feedbackRepo,
                                          OpinionItemMapper itemMapper,
                                          OpinionSurveyMapper surveyMapper,
                                          OpinionSurveyModuleMapper moduleMapper,
                                          OpinionAttachmentMapper attachmentMapper,
                                          OpinionUnitTaskMapper unitTaskMapper,
                                          OpinionActionLogger actionLogger,
                                          OpinionMockMasterDataProvider masterData) {
        this.deptTaskRepo = deptTaskRepo;
        this.feedbackRepo = feedbackRepo;
        this.itemMapper = itemMapper;
        this.surveyMapper = surveyMapper;
        this.moduleMapper = moduleMapper;
        this.attachmentMapper = attachmentMapper;
        this.unitTaskMapper = unitTaskMapper;
        this.actionLogger = actionLogger;
        this.masterData = masterData;
    }

    // ===== API-401 列表 =====
    @Override
    public PageResult<DeptFeedbackListItemVO> list(DeptFeedbackListRequest request) {
        int page = request.getPage();
        int size = request.getPageSize();

        Long currentDeptId = currentUserDeptId();

        LambdaQueryWrapper<OpinionDeptTaskDO> wrapper = new LambdaQueryWrapper<>();
        if (!CollectionUtils.isEmpty(request.getSubmitStatus())) {
            wrapper.in(OpinionDeptTaskDO::getSubmitStatus, request.getSubmitStatus());
        }

        List<OpinionDeptTaskDO> all = deptTaskRepo.selectList(wrapper);
        long total = all.size();
        int from = (page - 1) * size;
        int to = Math.min(from + size, all.size());
        List<OpinionDeptTaskDO> tasks = from < all.size() ? all.subList(from, to) : List.of();
        List<DeptFeedbackListItemVO> rows = new ArrayList<>();
        for (OpinionDeptTaskDO t : tasks) {
            DeptFeedbackListItemVO vo = new DeptFeedbackListItemVO();
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

    // ===== API-402 详情 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeptFeedbackDetailVO detail(Long taskId) {
        OpinionDeptTaskDO task = requireDeptTask(taskId);
        OpinionSurveyDO survey = requireSurvey(task.getSurveyId());

        ensureDeptAccess(task);
        ensureDeptFeedbackStatus(survey);

        DeptFeedbackDetailVO vo = new DeptFeedbackDetailVO();

        DeptFeedbackDetailVO.TaskInfo taskVO = new DeptFeedbackDetailVO.TaskInfo();
        taskVO.setId(task.getId());
        taskVO.setSurveyId(task.getSurveyId());
        taskVO.setSurveyName(survey.getName());
        taskVO.setAssessYear(survey.getAssessYear());
        taskVO.setSurveyStatus(survey.getStatus());
        taskVO.setSubmitStatus(task.getSubmitStatus());
        taskVO.setAuditStatus(task.getAuditStatus());
        taskVO.setDeptDeadline(survey.getDeptDeadline());
        taskVO.setSubmittedAt(task.getSubmittedAt());
        vo.setTask(taskVO);

        List<OpinionSurveyModuleDO> modules = moduleMapper.selectList(
            new LambdaQueryWrapper<OpinionSurveyModuleDO>()
                .eq(OpinionSurveyModuleDO::getSurveyId, task.getSurveyId())
                .orderByAsc(OpinionSurveyModuleDO::getDisplayOrder));

        Map<Long, List<OpinionAttachmentDO>> moduleAttMap = loadModuleAttachments(task.getSurveyId());

        List<DeptFeedbackDetailVO.ModuleInfo> moduleVOs = new ArrayList<>();
        for (OpinionSurveyModuleDO m : modules) {
            DeptFeedbackDetailVO.ModuleInfo mi = new DeptFeedbackDetailVO.ModuleInfo();
            mi.setModuleCode(m.getModuleCode());
            mi.setModuleName(m.getModuleName());
            List<DeptFeedbackDetailVO.AttachmentInfo> atts = new ArrayList<>();
            for (OpinionAttachmentDO a : moduleAttMap.getOrDefault(m.getId(), Collections.emptyList())) {
                DeptFeedbackDetailVO.AttachmentInfo ai = new DeptFeedbackDetailVO.AttachmentInfo();
                ai.setFileId(a.getFileId());
                ai.setFileName(a.getFileName());
                atts.add(ai);
            }
            mi.setAttachments(atts);
            moduleVOs.add(mi);
        }
        vo.setModules(moduleVOs);

        List<OpinionItemDO> items = itemMapper.selectList(
            new LambdaQueryWrapper<OpinionItemDO>()
                .eq(OpinionItemDO::getSurveyId, task.getSurveyId())
                .eq(OpinionItemDO::getModuleCode, task.getModuleCode())
                .orderByAsc(OpinionItemDO::getDisplayOrder));

        List<OpinionFeedbackDO> feedbacks = feedbackRepo.listByDeptTaskId(taskId);
        Map<Long, OpinionFeedbackDO> feedbackMap = new HashMap<>();
        for (OpinionFeedbackDO fb : feedbacks) {
            feedbackMap.put(fb.getItemId(), fb);
        }

        Map<Long, String> unitNameMap = loadUnitNamesForItems(items);

        List<DeptFeedbackDetailVO.FeedbackItemInfo> itemVOs = new ArrayList<>();
        for (OpinionItemDO item : items) {
            DeptFeedbackDetailVO.FeedbackItemInfo fi = new DeptFeedbackDetailVO.FeedbackItemInfo();
            fi.setItemId(item.getId());
            fi.setModuleCode(item.getModuleCode());
            fi.setIndicatorCategory(item.getIndicatorCategory());
            fi.setIndicatorName(item.getIndicatorName());
            fi.setFactorName(item.getFactorName());
            fi.setUnitName(unitNameMap.getOrDefault(item.getUnitTaskId(), ""));
            fi.setOpinionCategory(item.getOpinionCategory());
            fi.setOpinionContent(item.getOpinionContent());
            fi.setReason(item.getReason());

            OpinionFeedbackDO fb = feedbackMap.get(item.getId());
            if (fb != null) {
                fi.setIsAdopted(fb.getIsAdopted());
                fi.setAdoptionRemark(fb.getAdoptionRemark());
            fi.setRemark(fb.getRemark());
            }
            itemVOs.add(fi);
        }
        vo.setItems(itemVOs);

        return vo;
    }

    // ===== API-403 保存 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Long taskId, DeptFeedbackSaveRequest request) {
        OpinionDeptTaskDO task = requireDeptTask(taskId);
        ensureDeptAccess(task);

        if (!OpinionDeptSubmitStatus.PENDING.name().equals(task.getSubmitStatus())) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE);
        }

        OpinionSurveyDO survey = requireSurvey(task.getSurveyId());
        ensureDeptFeedbackStatus(survey);

        List<OpinionFeedbackDO> feedbacks = new ArrayList<>();
        for (DeptFeedbackSaveRequest.FeedbackItem entry : request.getItems()) {
            OpinionFeedbackDO fb = new OpinionFeedbackDO();
            fb.setSurveyId(task.getSurveyId());
            fb.setDeptTaskId(taskId);
            fb.setItemId(entry.getItemId());
            fb.setIsAdopted(entry.getIsAdopted());
            fb.setAdoptionRemark(entry.getAdoptionRemark());
            fb.setRemark(entry.getRemark());
            feedbacks.add(fb);
        }

        feedbackRepo.batchSaveOrUpdate(taskId, feedbacks);

        log.info("[opinion] dept feedback saved: taskId={}, items={}", taskId, feedbacks.size());
    }

    // ===== API-404 提交 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeptFeedbackSubmitVO submit(Long taskId) {
        OpinionDeptTaskDO task = requireDeptTask(taskId);
        ensureDeptAccess(task);

        if (!OpinionDeptSubmitStatus.PENDING.name().equals(task.getSubmitStatus())) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE);
        }

        OpinionSurveyDO survey = requireSurvey(task.getSurveyId());
        ensureDeptFeedbackStatus(survey);

        List<OpinionFeedbackDO> feedbacks = feedbackRepo.listByDeptTaskId(taskId);

        for (OpinionFeedbackDO fb : feedbacks) {
            if (fb.getIsAdopted() != null && fb.getIsAdopted() == 1
                && !StringUtils.hasText(fb.getAdoptionRemark())) {
                throw new BusinessException(ResultCode.OPINION_ADOPTION_REMARK_REQUIRED);
            }
        }

        Long actor = masterData.currentUserId();
        LocalDateTime now = LocalDateTime.now();

        task.setSubmitStatus(OpinionDeptSubmitStatus.SUBMITTED.name());
        task.setAuditStatus(OpinionDeptAuditStatus.PENDING.name());
        task.setSubmittedAt(now);
        task.setSubmittedBy(actor);
        deptTaskRepo.updateById(task);

        actionLogger.log(task.getSurveyId(), OpinionActionLogAction.DEPT_SUBMIT,
            null, null, ACTOR_ROLE_R04, actor,
            "dept_task_id=" + taskId);

        log.info("[opinion] dept feedback submitted: taskId={}, deptId={}", taskId, task.getDepartmentId());

        DeptFeedbackSubmitVO vo = new DeptFeedbackSubmitVO();
        vo.setTaskId(taskId);
        vo.setSubmitStatus(task.getSubmitStatus());
        vo.setSubmitStatusText(submitStatusText(task.getSubmitStatus()));
        return vo;
    }

    // ====================================================================
    // ==================== Helper ========================================
    // ====================================================================

    /**
     * Mock：当前用户的归属部门 ID。
     */
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
        if (!OpinionMainStatus.DEPT_FEEDBACK.name().equals(survey.getStatus())
            && !OpinionMainStatus.DONE.name().equals(survey.getStatus())) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE);
        }
    }

    private Map<Long, OpinionSurveyDO> loadSurveysForTasks(List<OpinionDeptTaskDO> tasks,
                                                           Integer assessYear,
                                                           String name) {
        if (tasks == null || tasks.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Long> surveyIds = tasks.stream()
            .map(OpinionDeptTaskDO::getSurveyId)
            .distinct()
            .collect(Collectors.toList());

        LambdaQueryWrapper<OpinionSurveyDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(OpinionSurveyDO::getId, surveyIds);
        if (assessYear != null) {
            wrapper.eq(OpinionSurveyDO::getAssessYear, assessYear);
        }
        if (StringUtils.hasText(name)) {
            wrapper.like(OpinionSurveyDO::getName, name.trim());
        }

        List<OpinionSurveyDO> surveys = surveyMapper.selectList(wrapper);
        Map<Long, OpinionSurveyDO> map = new HashMap<>();
        for (OpinionSurveyDO s : surveys) {
            map.put(s.getId(), s);
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
}
