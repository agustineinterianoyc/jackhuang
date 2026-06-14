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

    public List<OpinionFeedbackDO> listByDeptTaskId(Long deptTaskId) {
        return mapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OpinionFeedbackDO>()
                .eq(OpinionFeedbackDO::getDeptTaskId, deptTaskId));
    }

    /** 使用 JDBC 直连插入，绕过 MyBatis-Plus 所有问题。 */
    public void batchSaveOrUpdate(Long deptTaskId, List<OpinionFeedbackDO> feedbacks) {
        String sql = "INSERT INTO ad_opinion_feedback (survey_id, dept_task_id, item_id, is_adopted, adoption_remark, deleted_flag) VALUES (?, ?, ?, ?, ?, 0) ON DUPLICATE KEY UPDATE is_adopted=VALUES(is_adopted), adoption_remark=VALUES(adoption_remark)";
        for (OpinionFeedbackDO f : feedbacks) {
            jdbc.update(sql, f.getSurveyId() != null ? f.getSurveyId() : 0, deptTaskId, f.getItemId(), f.getIsAdopted(), f.getAdoptionRemark());
        }
    }
}
