package com.hk.demo.app.service.opinion.summarize.impl;

import com.hk.demo.api.enums.opinion.OpinionMainStatus;
import com.hk.demo.app.mapper.opinion.OpinionFeedbackMapper;
import com.hk.demo.app.mapper.opinion.OpinionItemMapper;
import com.hk.demo.app.mapper.opinion.OpinionSummaryItemMapper;
import com.hk.demo.app.mapper.opinion.OpinionUnitTaskMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionFeedbackDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionItemDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionSummaryItemDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionUnitTaskDO;
import com.hk.demo.app.model.request.opinion.summary.SummaryListRequest;
import com.hk.demo.app.model.request.opinion.summary.SummarySaveRequest;
import com.hk.demo.app.model.response.opinion.summary.SummaryItemVO;
import com.hk.demo.app.service.opinion.summarize.OpinionSummaryService;
import com.hk.demo.data.pagination.PageResult;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 汇总采纳服务实现。
 */
@Service
public class OpinionSummaryServiceImpl implements OpinionSummaryService {

    private static final Logger log = LoggerFactory.getLogger(OpinionSummaryServiceImpl.class);

    private final JdbcTemplate jdbc;
    private final OpinionSummaryItemMapper summaryItemMapper;
    private final OpinionFeedbackMapper feedbackMapper;
    private final OpinionItemMapper itemMapper;
    private final OpinionUnitTaskMapper unitTaskMapper;

    @Autowired
    public OpinionSummaryServiceImpl(JdbcTemplate jdbc,
                                     OpinionSummaryItemMapper summaryItemMapper,
                                     OpinionFeedbackMapper feedbackMapper,
                                     OpinionItemMapper itemMapper,
                                     OpinionUnitTaskMapper unitTaskMapper) {
        this.jdbc = jdbc;
        this.summaryItemMapper = summaryItemMapper;
        this.feedbackMapper = feedbackMapper;
        this.itemMapper = itemMapper;
        this.unitTaskMapper = unitTaskMapper;
    }

    // ===== API-701 汇总采纳清单查询 =====
    @Override
    public PageResult<SummaryItemVO> listItems(Long surveyId, SummaryListRequest request) {
        int page = request.getPage() == null ? 1 : Math.max(1, request.getPage());
        int pageSize = request.getPageSize() == null ? 20 : Math.max(1, Math.min(200, request.getPageSize()));

        String whereClause = " WHERE s.survey_id = ? AND s.deleted_flag = 0";
        List<Object> params = new ArrayList<>();
        params.add(surveyId);

        if (StringUtils.hasText(request.getModuleCode())) {
            whereClause += " AND i.module_code = ?";
            params.add(request.getModuleCode());
        }

        String countSql = "SELECT COUNT(*) FROM ad_opinion_summary_item s" +
            " LEFT JOIN ad_opinion_item i ON i.id = s.item_id AND i.deleted_flag = 0" +
            whereClause;

        Long total = jdbc.queryForObject(countSql, Long.class, params.toArray());

        if (total == null || total == 0) {
            return new PageResult<>(0, List.of());
        }

        int offset = (page - 1) * pageSize;
        String dataSql = "SELECT s.id, s.feedback_id, s.item_id, s.final_is_adopted, s.final_adoption_remark, s.adjusted_content," +
            " f.is_adopted, f.adoption_remark, f.dept_task_id," +
            " i.module_code, i.indicator_category, i.indicator_name, i.factor_name, i.opinion_category, i.opinion_content, i.reason, i.unit_task_id," +
            " dt.department_name" +
            " FROM ad_opinion_summary_item s" +
            " LEFT JOIN ad_opinion_feedback f ON f.id = s.feedback_id AND f.deleted_flag = 0" +
            " LEFT JOIN ad_opinion_item i ON i.id = s.item_id AND i.deleted_flag = 0" +
            " LEFT JOIN ad_opinion_dept_task dt ON dt.id = f.dept_task_id AND dt.deleted_flag = 0" +
            whereClause +
            " ORDER BY i.module_code, i.display_order" +
            " LIMIT " + offset + ", " + pageSize;

        List<SummaryItemVO> rows = jdbc.query(dataSql, (ResultSet rs, int rowNum) -> {
            SummaryItemVO vo = new SummaryItemVO();
            vo.setId(rs.getLong("id"));
            vo.setFeedbackId(rs.getObject("feedback_id", Long.class));
            vo.setItemId(rs.getObject("item_id", Long.class));
            vo.setModuleCode(rs.getString("module_code"));
            vo.setIndicatorCategory(rs.getString("indicator_category"));
            vo.setIndicatorName(rs.getString("indicator_name"));
            vo.setFactorName(rs.getString("factor_name"));
            vo.setOpinionCategory(rs.getString("opinion_category"));
            vo.setOpinionContent(rs.getString("opinion_content"));
            vo.setReason(rs.getString("reason"));
            vo.setDeptName(rs.getString("department_name"));
            vo.setIsAdopted(rs.getObject("is_adopted", Integer.class));
            vo.setAdoptionRemark(rs.getString("adoption_remark"));
            vo.setFinalIsAdopted(rs.getObject("final_is_adopted", Integer.class));
            vo.setFinalAdoptionRemark(rs.getString("final_adoption_remark"));
            vo.setAdjustedContent(rs.getString("adjusted_content"));

            Long unitTaskId = rs.getObject("unit_task_id", Long.class);
            if (unitTaskId != null) {
                vo.setUnitName(queryUnitName(unitTaskId));
            }
            return vo;
        }, params.toArray());

        return new PageResult<>(total, rows);
    }

    // ===== API-702 汇总采纳保存 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveItems(Long surveyId, SummarySaveRequest request) {
        String sql = "INSERT INTO ad_opinion_summary_item (survey_id, feedback_id, item_id, final_is_adopted, final_adoption_remark, adjusted_content, deleted_flag) VALUES (?, ?, ?, ?, ?, ?, 0) ON DUPLICATE KEY UPDATE final_is_adopted = VALUES(final_is_adopted), final_adoption_remark = VALUES(final_adoption_remark), adjusted_content = VALUES(adjusted_content)";

        for (SummarySaveRequest.ItemEntry entry : request.getItems()) {
            OpinionFeedbackDO fb = feedbackMapper.selectById(entry.getFeedbackId());
            if (fb == null) {
                continue;
            }
            jdbc.update(sql,
                surveyId,
                entry.getFeedbackId(),
                fb.getItemId(),
                entry.getFinalIsAdopted(),
                entry.getFinalAdoptionRemark(),
                entry.getAdjustedContent());
        }

        log.info("[opinion] summary items saved: surveyId={}, count={}", surveyId, request.getItems().size());
    }

    // ===== API-703 汇总数据导出（CSV） =====
    @Override
    public void exportCsv(Long surveyId, String moduleCode, HttpServletResponse response) {
        List<Object> params = new ArrayList<>();
        params.add(surveyId);

        StringBuilder sql = new StringBuilder(
            "SELECT s.id, s.feedback_id, s.item_id, s.final_is_adopted, s.final_adoption_remark, s.adjusted_content," +
            " f.is_adopted, f.adoption_remark," +
            " i.module_code, i.indicator_category, i.indicator_name, i.factor_name, i.opinion_category, i.opinion_content, i.reason, i.unit_task_id," +
            " dt.department_name" +
            " FROM ad_opinion_summary_item s" +
            " LEFT JOIN ad_opinion_feedback f ON f.id = s.feedback_id AND f.deleted_flag = 0" +
            " LEFT JOIN ad_opinion_item i ON i.id = s.item_id AND i.deleted_flag = 0" +
            " LEFT JOIN ad_opinion_dept_task dt ON dt.id = f.dept_task_id AND dt.deleted_flag = 0" +
            " WHERE s.survey_id = ? AND s.deleted_flag = 0");

        if (StringUtils.hasText(moduleCode)) {
            sql.append(" AND i.module_code = ?");
            params.add(moduleCode);
        }

        sql.append(" ORDER BY i.module_code, i.display_order");

        try {
            response.setContentType("text/csv;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=summary_" + surveyId + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".csv");
            response.setCharacterEncoding("UTF-8");

            PrintWriter writer = response.getWriter();
            writer.write("\uFEFF");
            writer.println("ID,反馈行ID,意见行ID,模块编码,指标大类,指标名称,要素名称,基层单位,意见分类,意见内容,理由,专业部门,是否采纳,采纳说明,终审是否采纳,终审采纳说明,调整后内容");

            jdbc.query(sql.toString(), (ResultSet rs) -> {
                Long unitTaskId = rs.getObject("unit_task_id", Long.class);
                String unitName = unitTaskId != null ? queryUnitName(unitTaskId) : "";

                writer.print(rs.getLong("id")); writer.print(",");
                writer.print(nvl(rs.getObject("feedback_id"))); writer.print(",");
                writer.print(nvl(rs.getObject("item_id"))); writer.print(",");
                writer.print(csvEscape(rs.getString("module_code"))); writer.print(",");
                writer.print(csvEscape(rs.getString("indicator_category"))); writer.print(",");
                writer.print(csvEscape(rs.getString("indicator_name"))); writer.print(",");
                writer.print(csvEscape(rs.getString("factor_name"))); writer.print(",");
                writer.print(csvEscape(unitName)); writer.print(",");
                writer.print(csvEscape(rs.getString("opinion_category"))); writer.print(",");
                writer.print(csvEscape(rs.getString("opinion_content"))); writer.print(",");
                writer.print(csvEscape(rs.getString("reason"))); writer.print(",");
                writer.print(csvEscape(rs.getString("department_name"))); writer.print(",");
                writer.print(nvl(rs.getObject("is_adopted"))); writer.print(",");
                writer.print(csvEscape(rs.getString("adoption_remark"))); writer.print(",");
                writer.print(nvl(rs.getObject("final_is_adopted"))); writer.print(",");
                writer.print(csvEscape(rs.getString("final_adoption_remark"))); writer.print(",");
                writer.println(csvEscape(rs.getString("adjusted_content")));
            }, params.toArray());

            writer.flush();
            log.info("[opinion] summary CSV exported: surveyId={}, moduleCode={}", surveyId, moduleCode);
        } catch (Exception e) {
            log.error("[opinion] summary CSV export failed: surveyId={}", surveyId, e);
            throw new RuntimeException("导出失败", e);
        }
    }

    // ===== API-704 汇总数据导入（打桩） =====
    @Override
    public void importExcel(Long surveyId) {
        log.info("[opinion] summary import stub called: surveyId={}", surveyId);
    }

    // ====================================================================
    // ==================== Helper ========================================
    // ====================================================================

    private String queryUnitName(Long unitTaskId) {
        if (unitTaskId == null) return "";
        try {
            String sql = "SELECT unit_name FROM ad_opinion_unit_task WHERE id = ? AND deleted_flag = 0";
            return jdbc.queryForObject(sql, String.class, unitTaskId);
        } catch (Exception e) {
            return "";
        }
    }

    private String csvEscape(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String nvl(Object value) {
        return value == null ? "" : value.toString();
    }
}
