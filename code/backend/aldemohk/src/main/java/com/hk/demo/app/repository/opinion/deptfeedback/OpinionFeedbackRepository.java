package com.hk.demo.app.repository.opinion.deptfeedback;

import com.hk.demo.app.mapper.opinion.OpinionFeedbackMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionFeedbackDO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 专业反馈行查询仓库（R04/R05 共用）。
 */
@Repository
public class OpinionFeedbackRepository {

    private final OpinionFeedbackMapper mapper;
    private final JdbcTemplate jdbc;

    public OpinionFeedbackRepository(OpinionFeedbackMapper mapper, JdbcTemplate jdbc) {
        this.mapper = mapper;
        this.jdbc = jdbc;
    }

    /** 用 JDBC 绕过 MyBatis-Plus selectList 兼容问题。 */
    public List<OpinionFeedbackDO> listByDeptTaskId(Long deptTaskId) {
        String sql = "SELECT id, survey_id, dept_task_id, item_id, is_adopted, adoption_remark, remark FROM ad_opinion_feedback WHERE dept_task_id = ? AND deleted_flag = 0";
        return jdbc.query(sql, (rs, rowNum) -> {
            OpinionFeedbackDO f = new OpinionFeedbackDO();
            f.setId(rs.getLong("id"));
            f.setSurveyId(rs.getLong("survey_id"));
            f.setDeptTaskId(rs.getLong("dept_task_id"));
            f.setItemId(rs.getLong("item_id"));
            f.setIsAdopted(rs.getObject("is_adopted", Integer.class));
            f.setAdoptionRemark(rs.getString("adoption_remark"));
            f.setRemark(rs.getString("remark"));
            return f;
        }, deptTaskId);
    }

    /** 使用 INSERT ON DUPLICATE KEY UPDATE 避免多次保存时的唯一键冲突。 */
    public void batchSaveOrUpdate(Long deptTaskId, List<OpinionFeedbackDO> feedbacks) {
        String sql = "INSERT INTO ad_opinion_feedback (survey_id, dept_task_id, item_id, is_adopted, adoption_remark, remark, deleted_flag) VALUES (?, ?, ?, ?, ?, ?, 0) ON DUPLICATE KEY UPDATE is_adopted=VALUES(is_adopted), adoption_remark=VALUES(adoption_remark), remark=VALUES(remark)";
        for (OpinionFeedbackDO f : feedbacks) {
            jdbc.update(sql,
                f.getSurveyId() != null ? f.getSurveyId() : 0,
                deptTaskId,
                f.getItemId(),
                f.getIsAdopted(),
                f.getAdoptionRemark(),
                f.getRemark());
        }
    }
}
