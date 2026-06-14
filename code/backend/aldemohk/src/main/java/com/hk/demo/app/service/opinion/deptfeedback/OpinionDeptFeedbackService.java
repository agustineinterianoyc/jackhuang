package com.hk.demo.app.service.opinion.deptfeedback;

import com.hk.demo.app.model.request.opinion.deptfeedback.DeptFeedbackListRequest;
import com.hk.demo.app.model.request.opinion.deptfeedback.DeptFeedbackSaveRequest;
import com.hk.demo.app.model.response.opinion.deptfeedback.DeptFeedbackDetailVO;
import com.hk.demo.app.model.response.opinion.deptfeedback.DeptFeedbackListItemVO;
import com.hk.demo.app.model.response.opinion.deptfeedback.DeptFeedbackSubmitVO;
import com.hk.demo.data.pagination.PageResult;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 专业反馈服务（R04 专业绩效联络员视角）。
 *
 * 实现 STEP-005 关联接口：
 * - API-401 专业反馈列表
 * - API-402 专业反馈详情
 * - API-403 专业反馈保存
 * - API-404 专业反馈提交
 */
public interface OpinionDeptFeedbackService {

    /**
     * API-401 专业反馈列表分页查询。
     */
    PageResult<DeptFeedbackListItemVO> list(DeptFeedbackListRequest request);

    /**
     * API-402 专业反馈详情。
     */
    DeptFeedbackDetailVO detail(Long taskId);

    /**
     * API-403 专业反馈保存。
     */
    void save(Long taskId, DeptFeedbackSaveRequest request);

    /**
     * API-404 专业反馈提交。
     */
    DeptFeedbackSubmitVO submit(Long taskId);

    /**
     * API-802 专业反馈数据导出（CSV）。
     */
    void exportCsv(Long taskId, HttpServletResponse response);
}
