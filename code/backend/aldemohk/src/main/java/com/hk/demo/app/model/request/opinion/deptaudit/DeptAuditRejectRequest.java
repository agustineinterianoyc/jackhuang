package com.hk.demo.app.model.request.opinion.deptaudit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 专业审核退回入参（R05）。
 */
public class DeptAuditRejectRequest {

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
