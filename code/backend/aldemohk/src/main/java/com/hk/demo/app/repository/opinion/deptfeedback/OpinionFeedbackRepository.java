package com.hk.demo.app.repository.opinion.deptfeedback;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hk.demo.app.mapper.opinion.OpinionFeedbackMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionFeedbackDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 专业反馈行查询仓库（R04/R05 共用）。
 */
@Repository
public class OpinionFeedbackRepository {

    private final OpinionFeedbackMapper mapper;

    public OpinionFeedbackRepository(OpinionFeedbackMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 按 dept_task_id 查询全部反馈行。
     */
    public List<OpinionFeedbackDO> listByDeptTaskId(Long deptTaskId) {
        LambdaQueryWrapper<OpinionFeedbackDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OpinionFeedbackDO::getDeptTaskId, deptTaskId);
        return mapper.selectList(wrapper);
    }

    /**
     * 整批替换：先删该 dept_task 的旧行，再批量插入新行。
     */
    public void batchSaveOrUpdate(Long deptTaskId, List<OpinionFeedbackDO> feedbacks) {
        mapper.delete(new LambdaQueryWrapper<OpinionFeedbackDO>()
            .eq(OpinionFeedbackDO::getDeptTaskId, deptTaskId));
        for (OpinionFeedbackDO f : feedbacks) {
            mapper.insert(f);
        }
    }
}
