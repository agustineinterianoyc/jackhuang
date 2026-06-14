package com.hk.demo.app.service.opinion.survey;

import com.hk.demo.app.model.request.opinion.survey.SurveyListRequest;
import com.hk.demo.app.model.request.opinion.survey.SurveySaveRequest;
import com.hk.demo.app.model.response.opinion.survey.SurveyDetailVO;
import com.hk.demo.app.model.response.opinion.survey.SurveyListItemVO;
import com.hk.demo.app.model.response.opinion.survey.SurveyPublishVO;
import com.hk.demo.app.model.response.opinion.survey.SurveyStartVO;
import com.hk.demo.app.model.response.opinion.survey.SurveyStatusVO;
import com.hk.demo.data.pagination.PageResult;

/**
 * 征集任务管理服务（P01）。
 *
 * 实现 STEP-003 关联的接口：
 * - API-101 创建征集
 * - API-102 编辑征集
 * - API-103 删除征集（仅 DRAFT）
 * - API-104 开启征集
 * - API-108 列表查询
 * - API-109 详情查询
 */
public interface OpinionSurveyService {

    /**
     * API-101 创建征集任务（草稿）。
     */
    SurveyStatusVO create(SurveySaveRequest request);

    /**
     * API-102 编辑征集任务（仅 DRAFT 可编辑）。
     */
    SurveyStatusVO update(Long id, SurveySaveRequest request);

    /**
     * API-103 删除征集任务（仅 DRAFT 可删除；逻辑删除）。
     */
    void delete(Long id);

    /**
     * API-104 开启征集（DRAFT → WAIT_FILL）。
     */
    SurveyStartVO start(Long id);

    /**
     * API-108 征集列表分页查询。
     */
    PageResult<SurveyListItemVO> list(SurveyListRequest request);

    /**
     * API-109 征集任务详情。
     */
    SurveyDetailVO detail(Long id);

    /**
     * API-110 绩效退回基层（R01 在主状态 FILLING 时退回单个基层）。
     */
    SurveyStatusVO opsRejectUnit(Long surveyId, Long unitTaskId, String rejectReason);

    /**
     * API-105 开启专业反馈（FILLING → DEPT_FEEDBACK）。
     *
     * 前置：全部基层已审核通过（所有 unit_task.audit_status='PASS'）。
     * 动作：推进主状态，初始化 dept_task + feedback 占位行。
     */
    SurveyStatusVO openDeptFeedback(Long surveyId);

    /**
     * API-111 绩效退回专业（R01 在 DEPT_FEEDBACK 时退回单个部门）。
     */
    SurveyStatusVO opsRejectDept(Long surveyId, Long deptTaskId, String rejectReason);

    /**
     * API-106 汇总（自动触发兜底）：检查全部专业审核通过后推进到 DONE，并初始化汇总采纳行。
     */
    SurveyStatusVO summarize(Long surveyId);

    /**
     * API-107 发布：DONE → PUBLISHED，记录发布时间。
     */
    SurveyPublishVO publish(Long surveyId);
}
