package com.hk.demo.app.service.opinion.unitfill;

import com.hk.demo.app.model.request.opinion.unitfill.UnitFillListRequest;
import com.hk.demo.app.model.request.opinion.unitfill.UnitFillSaveRequest;
import com.hk.demo.app.model.response.opinion.unitfill.UnitFillDetailVO;
import com.hk.demo.app.model.response.opinion.unitfill.UnitFillListItemVO;
import com.hk.demo.app.model.response.opinion.unitfill.UnitFillSubmitVO;
import com.hk.demo.data.pagination.PageResult;

/**
 * 基层填报服务（P02）。
 *
 * 实现 STEP-004 关联的接口：
 * - API-201 基层填报任务列表
 * - API-202 基层填报任务详情
 * - API-203 基层填报意见保存
 * - API-204 基层填报提交
 */
public interface OpinionUnitFillService {

    /**
     * API-201 基层填报任务列表查询。
     */
    PageResult<UnitFillListItemVO> list(UnitFillListRequest request);

    /**
     * API-202 基层填报任务详情。
     */
    UnitFillDetailVO detail(Long taskId);

    /**
     * API-203 基层填报意见保存。
     */
    void save(Long taskId, UnitFillSaveRequest request);

    /**
     * API-204 基层填报提交。
     */
    UnitFillSubmitVO submit(Long taskId);
}
