package com.hk.demo.app.model.response.opinion.unitfill;

/**
 * API-204 响应：基层填报提交结果。
 */
public class UnitFillSubmitVO {

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
