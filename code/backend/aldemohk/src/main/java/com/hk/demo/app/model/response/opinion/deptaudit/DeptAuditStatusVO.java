package com.hk.demo.app.model.response.opinion.deptaudit;

/**
 * 专业审核操作结果（R05）。
 */
public class DeptAuditStatusVO {

    private Long taskId;
    private String auditStatus;
    private String auditStatusText;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatusText() {
        return auditStatusText;
    }

    public void setAuditStatusText(String auditStatusText) {
        this.auditStatusText = auditStatusText;
    }
}
