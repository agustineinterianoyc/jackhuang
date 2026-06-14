package com.hk.demo.app.model.request.opinion.summary;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * API-702 入参：汇总采纳保存。
 */
public class SummarySaveRequest {

    @NotEmpty(message = "汇总采纳项不能为空")
    @Valid
    private List<ItemEntry> items;

    public List<ItemEntry> getItems() {
        return items;
    }

    public void setItems(List<ItemEntry> items) {
        this.items = items;
    }

    /**
     * 单条汇总采纳保存项。
     */
    public static class ItemEntry {

        private Long feedbackId;

        private Integer finalIsAdopted;

        private String finalAdoptionRemark;

        private String adjustedContent;

        public Long getFeedbackId() {
            return feedbackId;
        }

        public void setFeedbackId(Long feedbackId) {
            this.feedbackId = feedbackId;
        }

        public Integer getFinalIsAdopted() {
            return finalIsAdopted;
        }

        public void setFinalIsAdopted(Integer finalIsAdopted) {
            this.finalIsAdopted = finalIsAdopted;
        }

        public String getFinalAdoptionRemark() {
            return finalAdoptionRemark;
        }

        public void setFinalAdoptionRemark(String finalAdoptionRemark) {
            this.finalAdoptionRemark = finalAdoptionRemark;
        }

        public String getAdjustedContent() {
            return adjustedContent;
        }

        public void setAdjustedContent(String adjustedContent) {
            this.adjustedContent = adjustedContent;
        }
    }
}
