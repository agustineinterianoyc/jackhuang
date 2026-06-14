package com.hk.demo.app.service.opinion.unitaudit;

import com.hk.demo.app.model.request.opinion.unitaudit.UnitAuditListRequest;
import com.hk.demo.app.model.request.opinion.unitaudit.UnitAuditRejectRequest;
import com.hk.demo.app.model.response.opinion.unitaudit.UnitAuditDetailVO;
import com.hk.demo.app.model.response.opinion.unitaudit.UnitAuditListItemVO;
import com.hk.demo.app.model.response.opinion.unitaudit.UnitAuditStatusVO;
import com.hk.demo.data.pagination.PageResult;

/**
 * 基层审核服务（R03 人资部主任视角）。
 *
 * 实现 STEP-004 关联接口：
 * - API-301 审核列表查询
 * - API-302 审核详情查询
 * - API-303 审核通过
 * - API-304 审核退回
 */
public interface OpinionUnitAuditService {

    /**
     * API-301 基层审核列表分页查询。
     *
     * @param request 查询条件
     * @return 分页结果
     */
    PageResult<UnitAuditListItemVO> list(UnitAuditListRequest request);

    /**
     * API-302 基层审核详情。
     *
     * @param taskId 基层任务 ID
     * @return 审核详情
     */
    UnitAuditDetailVO detail(Long taskId);

    /**
     * API-303 审核通过。
     *
     * @param taskId 基层任务 ID
     * @return 最新状态
     */
    UnitAuditStatusVO pass(Long taskId);

    /**
     * API-304 审核退回。
     *
     * @param taskId  基层任务 ID
     * @param request 退回原因
     * @return 最新状态
     */
    UnitAuditStatusVO reject(Long taskId, UnitAuditRejectRequest request);
}
