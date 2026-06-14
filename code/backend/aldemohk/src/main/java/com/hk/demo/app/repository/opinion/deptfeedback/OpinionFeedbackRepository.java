package com.hk.demo.app.repository.opinion.deptfeedback;

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

    public List<OpinionFeedbackDO> listByDeptTaskId(Long deptTaskId) {
        return mapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OpinionFeedbackDO>()
                .eq(OpinionFeedbackDO::getDeptTaskId, deptTaskId));
    }

    /**
     * 直接插入，不做删旧操作。
     * 注意：多次保存会累积多套反馈行（同一 item 有多条记录）。
     * 列表查询按创建时间倒序取最新即可；如后续需要唯一性，需改用 INSERT ON DUPLICATE KEY UPDATE。
     */
    public void batchSaveOrUpdate(Long deptTaskId, List<OpinionFeedbackDO> feedbacks) {
        for (OpinionFeedbackDO f : feedbacks) {
            mapper.insert(f);
        }
    }
}
