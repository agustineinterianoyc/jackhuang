package com.hk.demo.app.model.response.opinion.reminder;

/**
 * API-603 响应体：一键提醒结果。
 */
public class BatchReminderVO {

    /** 已通知数量。 */
    private Integer notifiedCount;

    /** 已跳过数量（5分钟内已提醒过）。 */
    private Integer skippedCount;

    public Integer getNotifiedCount() {
        return notifiedCount;
    }

    public void setNotifiedCount(Integer notifiedCount) {
        this.notifiedCount = notifiedCount;
    }

    public Integer getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(Integer skippedCount) {
        this.skippedCount = skippedCount;
    }
}
