package com.hk.demo.app.model.response.opinion.unitaudit;

/**
 * API-303 / API-304 响应：审核操作结果。
 */
public class UnitAuditStatusVO {

    private Long taskId;
    private String fillStatus;
    private String auditStatus;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getFillStatus() {
        return fillStatus;
    }

    public void setFillStatus(String fillStatus) {
        this.fillStatus = fillStatus;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
}
