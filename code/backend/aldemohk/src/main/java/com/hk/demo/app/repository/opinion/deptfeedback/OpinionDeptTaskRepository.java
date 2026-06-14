package com.hk.demo.app.repository.opinion.deptfeedback;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.demo.app.mapper.opinion.OpinionDeptTaskMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionDeptTaskDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 专业任务查询仓库（R04/R05 共用）。
 */
@Repository
public class OpinionDeptTaskRepository {

    private final OpinionDeptTaskMapper mapper;

    public OpinionDeptTaskRepository(OpinionDeptTaskMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 按条件分页查询 dept_task。
     */
    public IPage<OpinionDeptTaskDO> pageQuery(LambdaQueryWrapper<OpinionDeptTaskDO> wrapper, int page, int size) {
        Page<OpinionDeptTaskDO> p = new Page<>(page, size);
        return mapper.selectPage(p, wrapper);
    }

    /**
     * 按 ID 查询。
     */
    public OpinionDeptTaskDO findById(Long id) {
        return id == null ? null : mapper.selectById(id);
    }

    /**
     * 按征集 ID 查询全部 dept_task。
     */
    public List<OpinionDeptTaskDO> listBySurveyId(Long surveyId) {
        LambdaQueryWrapper<OpinionDeptTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OpinionDeptTaskDO::getSurveyId, surveyId);
        return mapper.selectList(wrapper);
    }

    public void insert(OpinionDeptTaskDO entity) {
        mapper.insert(entity);
    }

    public void updateById(OpinionDeptTaskDO entity) {
        mapper.updateById(entity);
    }
}
