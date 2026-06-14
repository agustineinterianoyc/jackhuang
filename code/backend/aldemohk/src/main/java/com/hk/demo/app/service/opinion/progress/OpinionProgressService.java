package com.hk.demo.app.service.opinion.progress;

import com.hk.demo.app.model.request.opinion.progress.ProgressListRequest;
import com.hk.demo.app.model.response.opinion.progress.DeptProgressVO;
import com.hk.demo.app.model.response.opinion.progress.UnitProgressVO;
import com.hk.demo.data.pagination.PageResult;

/**
 * 进度查询服务。
 *
 * 实现 STEP-006 关联的接口：
 * - API-601 基层进度查询
 * - API-602 专业进度查询
 */
public interface OpinionProgressService {

    /**
     * API-601 基层进度列表查询。
     */
    PageResult<UnitProgressVO> listUnits(ProgressListRequest request);

    /**
     * API-602 专业进度列表查询。
     */
    PageResult<DeptProgressVO> listDepts(ProgressListRequest request);
}
