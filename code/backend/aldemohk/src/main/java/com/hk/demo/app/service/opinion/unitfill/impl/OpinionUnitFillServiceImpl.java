package com.hk.demo.app.service.opinion.unitfill.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.demo.api.enums.ResultCode;
import com.hk.demo.api.enums.opinion.OpinionActionLogAction;
import com.hk.demo.api.enums.opinion.OpinionAttachmentBizType;
import com.hk.demo.api.enums.opinion.OpinionMainStatus;
import com.hk.demo.api.enums.opinion.OpinionUnitAuditStatus;
import com.hk.demo.api.enums.opinion.OpinionUnitFillStatus;
import com.hk.demo.app.mapper.opinion.OpinionAttachmentMapper;
import com.hk.demo.app.mapper.opinion.OpinionItemMapper;
import com.hk.demo.app.mapper.opinion.OpinionSurveyMapper;
import com.hk.demo.app.mapper.opinion.OpinionSurveyModuleMapper;
import com.hk.demo.app.mapper.opinion.OpinionUnitTaskMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionAttachmentDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionItemDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionSurveyDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionSurveyModuleDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionUnitTaskDO;
import com.hk.demo.app.model.request.opinion.unitfill.UnitFillListRequest;
import com.hk.demo.app.model.request.opinion.unitfill.UnitFillSaveRequest;
import com.hk.demo.app.model.response.opinion.unitfill.UnitFillDetailVO;
import com.hk.demo.app.model.response.opinion.unitfill.UnitFillListItemVO;
import com.hk.demo.app.model.response.opinion.unitfill.UnitFillSubmitVO;
import com.hk.demo.app.service.opinion.log.OpinionActionLogger;
import com.hk.demo.app.service.opinion.mock.OpinionMockMasterDataProvider;
import com.hk.demo.app.service.opinion.state.OpinionStateMachine;
import com.hk.demo.app.service.opinion.unitfill.OpinionUnitFillService;
import com.hk.demo.core.exception.BusinessException;
import com.hk.demo.data.pagination.PageResult;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基层填报服务实现。
 */
@Service
public class OpinionUnitFillServiceImpl implements OpinionUnitFillService {

    private static final Logger log = LoggerFactory.getLogger(OpinionUnitFillServiceImpl.class);

    private static final String ACTOR_ROLE_R02 = "R02";

    private final OpinionUnitTaskMapper unitTaskMapper;
    private final OpinionItemMapper itemMapper;
    private final OpinionSurveyMapper surveyMapper;
    private final OpinionSurveyModuleMapper moduleMapper;
    private final OpinionAttachmentMapper attachmentMapper;
    private final OpinionStateMachine stateMachine;
    private final OpinionActionLogger actionLogger;
    private final OpinionMockMasterDataProvider masterData;
    private final JdbcTemplate jdbc;

    @Autowired
    public OpinionUnitFillServiceImpl(OpinionUnitTaskMapper unitTaskMapper,
                                      OpinionItemMapper itemMapper,
                                      OpinionSurveyMapper surveyMapper,
                                      OpinionSurveyModuleMapper moduleMapper,
                                      OpinionAttachmentMapper attachmentMapper,
                                      OpinionStateMachine stateMachine,
                                      OpinionActionLogger actionLogger,
                                      OpinionMockMasterDataProvider masterData,
                                      JdbcTemplate jdbc) {
        this.unitTaskMapper = unitTaskMapper;
        this.itemMapper = itemMapper;
        this.surveyMapper = surveyMapper;
        this.moduleMapper = moduleMapper;
        this.attachmentMapper = attachmentMapper;
        this.stateMachine = stateMachine;
        this.actionLogger = actionLogger;
        this.masterData = masterData;
        this.jdbc = jdbc;
    }

    // ===== API-201 列表 =====
    @Override
    public PageResult<UnitFillListItemVO> list(UnitFillListRequest request) {
        int page = Math.max(1, request.getPage());
        int size = Math.max(1, Math.min(200, request.getPageSize()));

        Long currentUnitId = currentUserUnitId();

        LambdaQueryWrapper<OpinionUnitTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OpinionUnitTaskDO::getUnitId, currentUnitId);
        if (request.getFillStatus() != null && request.getFillStatus().length > 0) {
            wrapper.in(OpinionUnitTaskDO::getFillStatus, List.of(request.getFillStatus()));
        }

        Page<OpinionUnitTaskDO> p = new Page<>(page, size);
        IPage<OpinionUnitTaskDO> result = unitTaskMapper.selectPage(p, wrapper);

        List<OpinionUnitTaskDO> tasks = result.getRecords();
        Map<Long, OpinionSurveyDO> surveyMap = loadSurveysForTasks(tasks, request.getAssessYear(), request.getName());

        List<UnitFillListItemVO> rows = new ArrayList<>();
        for (OpinionUnitTaskDO t : tasks) {
            OpinionSurveyDO s = surveyMap.get(t.getSurveyId());
            if (s == null) {
                continue;
            }
            UnitFillListItemVO vo = new UnitFillListItemVO();
            vo.setTaskId(t.getId());
            vo.setSurveyId(t.getSurveyId());
            vo.setSurveyName(s.getName());
            vo.setAssessYear(s.getAssessYear());
            vo.setUnitId(t.getUnitId());
            vo.setUnitName(t.getUnitName());
            vo.setFillStatus(t.getFillStatus());
            vo.setFillStatusText(fillStatusText(t.getFillStatus()));
            vo.setAuditStatus(t.getAuditStatus());
            vo.setAuditStatusText(auditStatusText(t.getAuditStatus()));
            vo.setUnitDeadline(s.getUnitDeadline());
            vo.setSubmittedAt(t.getSubmittedAt());
            rows.add(vo);
        }
        return new PageResult<>(result.getTotal(), rows);
    }

    // ===== API-202 详情 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UnitFillDetailVO detail(Long taskId) {
        OpinionUnitTaskDO task = requireTask(taskId);
        OpinionSurveyDO survey = requireSurvey(task.getSurveyId());

        tryAutoTransitionToFilling(survey);

        UnitFillDetailVO vo = new UnitFillDetailVO();

        UnitFillDetailVO.TaskVO taskVO = new UnitFillDetailVO.TaskVO();
        taskVO.setId(task.getId());
        taskVO.setSurveyId(task.getSurveyId());
        taskVO.setSurveyName(survey.getName());
        taskVO.setAssessYear(survey.getAssessYear());
        taskVO.setSurveyStatus(survey.getStatus());
        taskVO.setSurveyStatusText(surveyStatusText(survey.getStatus()));
        taskVO.setFillStatus(task.getFillStatus());
        taskVO.setFillStatusText(fillStatusText(task.getFillStatus()));
        taskVO.setAuditStatus(task.getAuditStatus());
        taskVO.setAuditStatusText(auditStatusText(task.getAuditStatus()));
        taskVO.setUnitDeadline(survey.getUnitDeadline());
        vo.setTask(taskVO);

        List<OpinionSurveyModuleDO> modules = moduleMapper.selectList(
            new LambdaQueryWrapper<OpinionSurveyModuleDO>()
                .eq(OpinionSurveyModuleDO::getSurveyId, task.getSurveyId())
                .orderByAsc(OpinionSurveyModuleDO::getDisplayOrder));

        Map<Long, List<OpinionAttachmentDO>> moduleAttMap = loadModuleAttachments(task.getSurveyId());

        List<UnitFillDetailVO.ModuleVO> moduleVOs = new ArrayList<>();
        for (OpinionSurveyModuleDO m : modules) {
            UnitFillDetailVO.ModuleVO mv = new UnitFillDetailVO.ModuleVO();
            mv.setModuleCode(m.getModuleCode());
            mv.setModuleName(m.getModuleName());
            List<UnitFillDetailVO.AttachmentVO> atts = new ArrayList<>();
            for (OpinionAttachmentDO a : moduleAttMap.getOrDefault(m.getId(), Collections.emptyList())) {
                UnitFillDetailVO.AttachmentVO av = new UnitFillDetailVO.AttachmentVO();
                av.setFileId(a.getFileId());
                av.setFileName(a.getFileName());
                atts.add(av);
            }
            mv.setAttachments(atts);
            moduleVOs.add(mv);
        }
        vo.setModules(moduleVOs);

        List<OpinionItemDO> items = itemMapper.selectList(
            new LambdaQueryWrapper<OpinionItemDO>()
                .eq(OpinionItemDO::getUnitTaskId, taskId)
                .orderByAsc(OpinionItemDO::getDisplayOrder));
        vo.setItems(toItemVOs(items));

        if (OpinionMainStatus.PUBLISHED.name().equals(survey.getStatus())) {
            vo.setAdjustedItems(toItemVOs(items));
        }

        return vo;
    }

    // ===== API-203 保存 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Long taskId, UnitFillSaveRequest request) {
        OpinionUnitTaskDO task = requireTask(taskId);

        if (!OpinionUnitFillStatus.PENDING.name().equals(task.getFillStatus())) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE);
        }

        Long actor = masterData.currentUserId();

        itemMapper.delete(new LambdaQueryWrapper<OpinionItemDO>()
            .eq(OpinionItemDO::getUnitTaskId, taskId));

        int order = 0;
        for (UnitFillSaveRequest.ItemEntry entry : request.getItems()) {
            OpinionItemDO item = new OpinionItemDO();
            item.setSurveyId(task.getSurveyId());
            item.setUnitTaskId(taskId);
            item.setUnitId(task.getUnitId());
            item.setModuleCode(entry.getModuleCode());
            item.setIndicatorCategory(entry.getIndicatorCategory());
            item.setIndicatorName(entry.getIndicatorName());
            item.setFactorName(entry.getFactorName());
            item.setExtraField(entry.getExtraField());
            item.setOpinionCategory(entry.getOpinionCategory());
            item.setOpinionContent(entry.getOpinionContent());
            item.setReason(entry.getReason());
            item.setDisplayOrder(entry.getDisplayOrder() == null ? order++ : entry.getDisplayOrder());
            itemMapper.insert(item);
        }

        log.info("[opinion] unit fill saved: taskId={}, items={}", taskId, request.getItems().size());
    }

    // ===== API-204 提交 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UnitFillSubmitVO submit(Long taskId) {
        OpinionUnitTaskDO task = requireTask(taskId);

        if (!OpinionUnitFillStatus.PENDING.name().equals(task.getFillStatus())) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE);
        }

        OpinionSurveyDO survey = requireSurvey(task.getSurveyId());

        long itemCount = itemMapper.selectCount(
            new LambdaQueryWrapper<OpinionItemDO>()
                .eq(OpinionItemDO::getUnitTaskId, taskId));
        if (itemCount == 0) {
            LocalDateTime now = LocalDateTime.now();
            if (survey.getUnitDeadline() != null && now.isBefore(survey.getUnitDeadline())) {
                throw new BusinessException(ResultCode.OPINION_EMPTY_NOT_ALLOWED);
            }
        }

        Long actor = masterData.currentUserId();
        LocalDateTime now = LocalDateTime.now();

        OpinionMainStatus fromSurveyStatus = OpinionMainStatus.fromName(survey.getStatus());
        boolean transitioned = false;
        if (OpinionMainStatus.WAIT_FILL == fromSurveyStatus) {
            tryAutoTransitionToFilling(survey);
            transitioned = true;
        }

        task.setFillStatus(OpinionUnitFillStatus.SUBMITTED.name());
        task.setAuditStatus(OpinionUnitAuditStatus.PENDING.name());
        task.setSubmittedAt(now);
        task.setSubmittedBy(actor);
        unitTaskMapper.updateById(task);

        actionLogger.log(task.getSurveyId(), OpinionActionLogAction.UNIT_SUBMIT,
            fromSurveyStatus.name(),
            transitioned ? OpinionMainStatus.FILLING.name() : survey.getStatus(),
            ACTOR_ROLE_R02, actor,
            "unit taskId=" + taskId + " submitted");

        log.info("[opinion] unit fill submitted: taskId={}, unitId={}", taskId, task.getUnitId());

        UnitFillSubmitVO vo = new UnitFillSubmitVO();
        vo.setTaskId(taskId);
        vo.setFillStatus(task.getFillStatus());
        vo.setAuditStatus(task.getAuditStatus());
        return vo;
    }

    // ===== API-801 导出 =====
    @Override
    public void exportCsv(Long taskId, HttpServletResponse response) {
        OpinionUnitTaskDO task = requireTask(taskId);

        String sql = "SELECT i.module_code, i.indicator_category, i.indicator_name, i.factor_name," +
            " i.extra_field, i.opinion_category, i.opinion_content, i.reason" +
            " FROM ad_opinion_item i" +
            " WHERE i.unit_task_id = ? AND i.deleted_flag = 0" +
            " ORDER BY i.display_order";

        try {
            response.setContentType("text/csv;charset=UTF-8");
            response.setHeader("Content-Disposition",
                "attachment;filename=unit_fill_" + taskId + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".csv");
            response.setCharacterEncoding("UTF-8");

            PrintWriter writer = response.getWriter();
            writer.write("\uFEFF");
            writer.println("模块编码,指标大类,指标名称,要素名称,补充字段,意见分类,意见内容,理由");

            jdbc.query(sql, (ResultSet rs) -> {
                writer.print(csvEscape(rs.getString("module_code"))); writer.print(",");
                writer.print(csvEscape(rs.getString("indicator_category"))); writer.print(",");
                writer.print(csvEscape(rs.getString("indicator_name"))); writer.print(",");
                writer.print(csvEscape(rs.getString("factor_name"))); writer.print(",");
                writer.print(csvEscape(rs.getString("extra_field"))); writer.print(",");
                writer.print(csvEscape(rs.getString("opinion_category"))); writer.print(",");
                writer.print(csvEscape(rs.getString("opinion_content"))); writer.print(",");
                writer.println(csvEscape(rs.getString("reason")));
            }, taskId);

            writer.flush();
            log.info("[opinion] unit fill CSV exported: taskId={}", taskId);
        } catch (Exception e) {
            log.error("[opinion] unit fill CSV export failed: taskId={}", taskId, e);
            throw new RuntimeException("导出失败", e);
        }
    }

    // ====================================================================
    // ==================== Helper ========================================
    // ====================================================================

    /**
     * Mock：当前 R02 用户的归属单位 ID。
     */
    private Long currentUserUnitId() {
        return 101L;
    }

    private OpinionUnitTaskDO requireTask(Long taskId) {
        OpinionUnitTaskDO task = taskId == null ? null : unitTaskMapper.selectById(taskId);
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

    /**
     * 如果征集状态为 WAIT_FILL，自动推进到 FILLING。
     */
    private void tryAutoTransitionToFilling(OpinionSurveyDO survey) {
        if (!OpinionMainStatus.WAIT_FILL.name().equals(survey.getStatus())) {
            return;
        }
        stateMachine.assertTransition(OpinionMainStatus.WAIT_FILL, OpinionMainStatus.FILLING);
        survey.setStatus(OpinionMainStatus.FILLING.name());
        survey.setUpdatedBy(masterData.currentUserId());
        surveyMapper.updateById(survey);
        log.info("[opinion] survey auto transition WAIT_FILL -> FILLING: surveyId={}", survey.getId());
    }

    /**
     * 批量加载任务关联的征集信息，同时按 assessYear/name 过滤。
     */
    private Map<Long, OpinionSurveyDO> loadSurveysForTasks(List<OpinionUnitTaskDO> tasks,
                                                           Integer assessYear,
                                                           String name) {
        if (tasks == null || tasks.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Long> surveyIds = tasks.stream()
            .map(OpinionUnitTaskDO::getSurveyId)
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
        Map<Long, OpinionSurveyDO> map = new LinkedHashMap<>();
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

    private List<UnitFillDetailVO.ItemVO> toItemVOs(List<OpinionItemDO> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }
        List<UnitFillDetailVO.ItemVO> vos = new ArrayList<>();
        for (OpinionItemDO item : items) {
            UnitFillDetailVO.ItemVO vo = new UnitFillDetailVO.ItemVO();
            vo.setId(item.getId());
            vo.setModuleCode(item.getModuleCode());
            vo.setIndicatorCategory(item.getIndicatorCategory());
            vo.setIndicatorName(item.getIndicatorName());
            vo.setFactorName(item.getFactorName());
            vo.setExtraField(item.getExtraField());
            vo.setOpinionCategory(item.getOpinionCategory());
            vo.setOpinionContent(item.getOpinionContent());
            vo.setReason(item.getReason());
            vo.setDisplayOrder(item.getDisplayOrder());
            vos.add(vo);
        }
        return vos;
    }

    private String fillStatusText(String fillStatus) {
        if (OpinionUnitFillStatus.PENDING.name().equals(fillStatus)) {
            return OpinionUnitFillStatus.PENDING.getText();
        }
        if (OpinionUnitFillStatus.SUBMITTED.name().equals(fillStatus)) {
            return OpinionUnitFillStatus.SUBMITTED.getText();
        }
        return fillStatus;
    }

    private String auditStatusText(String auditStatus) {
        if (OpinionUnitAuditStatus.NONE.name().equals(auditStatus)) {
            return OpinionUnitAuditStatus.NONE.getText();
        }
        if (OpinionUnitAuditStatus.PENDING.name().equals(auditStatus)) {
            return OpinionUnitAuditStatus.PENDING.getText();
        }
        if (OpinionUnitAuditStatus.PASS.name().equals(auditStatus)) {
            return OpinionUnitAuditStatus.PASS.getText();
        }
        if (OpinionUnitAuditStatus.REJECTED.name().equals(auditStatus)) {
            return OpinionUnitAuditStatus.REJECTED.getText();
        }
        return auditStatus;
    }

    private String surveyStatusText(String status) {
        OpinionMainStatus s = OpinionMainStatus.fromName(status);
        return s == null ? status : s.getText();
    }

    private String csvEscape(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
