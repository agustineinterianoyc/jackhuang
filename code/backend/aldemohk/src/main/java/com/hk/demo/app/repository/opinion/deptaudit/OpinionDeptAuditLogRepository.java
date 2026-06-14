package com.hk.demo.app.repository.opinion.deptaudit;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hk.demo.app.mapper.opinion.OpinionDeptAuditLogMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionDeptAuditLogDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 专业审核日志查询仓库（R05 共用）。
 */
@Repository
public class OpinionDeptAuditLogRepository {

    private final OpinionDeptAuditLogMapper mapper;

    public OpinionDeptAuditLogRepository(OpinionDeptAuditLogMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 按专业任务 ID 查询全部审核日志，按创建时间倒序。
     */
    public List<OpinionDeptAuditLogDO> listByDeptTaskId(Long deptTaskId) {
        LambdaQueryWrapper<OpinionDeptAuditLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OpinionDeptAuditLogDO::getDeptTaskId, deptTaskId);
        wrapper.orderByDesc(OpinionDeptAuditLogDO::getCreatedAt);
        return mapper.selectList(wrapper);
    }

    public void insert(OpinionDeptAuditLogDO entity) {
        mapper.insert(entity);
    }
}
