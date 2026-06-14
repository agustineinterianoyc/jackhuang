package com.hk.demo.app.model.request.opinion.unitaudit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * API-304 入参：基层审核退回（R03）。
 */
public class UnitAuditRejectRequest {

    /** 退回原因（必填，最长 260 字符）。 */
    @NotBlank(message = "退回原因不能为空")
    @Size(max = 260, message = "退回原因最长 260 字符")
    private String rejectReason;

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
