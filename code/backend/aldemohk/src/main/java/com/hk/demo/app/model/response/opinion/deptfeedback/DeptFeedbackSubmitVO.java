package com.hk.demo.app.model.response.opinion.deptfeedback;

/**
 * 专业反馈提交结果（R04）。
 */
public class DeptFeedbackSubmitVO {

    private Long taskId;
    private String submitStatus;
    private String submitStatusText;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(String submitStatus) {
        this.submitStatus = submitStatus;
    }

    public String getSubmitStatusText() {
        return submitStatusText;
    }

    public void setSubmitStatusText(String submitStatusText) {
        this.submitStatusText = submitStatusText;
    }
}
