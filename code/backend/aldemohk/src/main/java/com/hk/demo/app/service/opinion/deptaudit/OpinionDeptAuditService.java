package com.hk.demo.app.service.opinion.deptaudit;

import com.hk.demo.app.model.request.opinion.deptaudit.DeptAuditListRequest;
import com.hk.demo.app.model.request.opinion.deptaudit.DeptAuditRejectRequest;
import com.hk.demo.app.model.response.opinion.deptaudit.DeptAuditDetailVO;
import com.hk.demo.app.model.response.opinion.deptaudit.DeptAuditListItemVO;
import com.hk.demo.app.model.response.opinion.deptaudit.DeptAuditStatusVO;
import com.hk.demo.data.pagination.PageResult;

/**
 * 专业审核服务（R05 专业部门负责人视角）。
 *
 * 实现 STEP-005 关联接口：
 * - API-501 专业审核列表
 * - API-502 专业审核详情
 * - API-503 专业审核通过
 * - API-504 专业审核退回
 */
public interface OpinionDeptAuditService {

    /**
     * API-501 专业审核列表分页查询。
     */
    PageResult<DeptAuditListItemVO> list(DeptAuditListRequest request);

    /**
     * API-502 专业审核详情。
     */
    DeptAuditDetailVO detail(Long taskId);

    /**
     * API-503 审核通过。
     */
    DeptAuditStatusVO pass(Long taskId);

    /**
     * API-504 审核退回。
     */
    DeptAuditStatusVO reject(Long taskId, DeptAuditRejectRequest request);
}
