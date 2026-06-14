package com.hk.demo.app.model.request.opinion.unitfill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * API-203 入参：基层填报意见保存。
 */
public class UnitFillSaveRequest {

    @NotEmpty(message = "意见列表不能为空")
    private List<ItemEntry> items;

    public List<ItemEntry> getItems() {
        return items;
    }

    public void setItems(List<ItemEntry> items) {
        this.items = items;
    }

    /**
     * 意见条目。
     */
    public static class ItemEntry {

        @NotBlank(message = "模块编码必填")
        private String moduleCode;

        private String indicatorCategory;

        private String indicatorName;

        private String factorName;

        private String extraField;

        private String opinionCategory;

        private String opinionContent;

        private String reason;

        private Integer displayOrder;

        public String getModuleCode() {
            return moduleCode;
        }

        public void setModuleCode(String moduleCode) {
            this.moduleCode = moduleCode;
        }

        public String getIndicatorCategory() {
            return indicatorCategory;
        }

        public void setIndicatorCategory(String indicatorCategory) {
            this.indicatorCategory = indicatorCategory;
        }

        public String getIndicatorName() {
            return indicatorName;
        }

        public void setIndicatorName(String indicatorName) {
            this.indicatorName = indicatorName;
        }

        public String getFactorName() {
            return factorName;
        }

        public void setFactorName(String factorName) {
            this.factorName = factorName;
        }

        public String getExtraField() {
            return extraField;
        }

        public void setExtraField(String extraField) {
            this.extraField = extraField;
        }

        public String getOpinionCategory() {
            return opinionCategory;
        }

        public void setOpinionCategory(String opinionCategory) {
            this.opinionCategory = opinionCategory;
        }

        public String getOpinionContent() {
            return opinionContent;
        }

        public void setOpinionContent(String opinionContent) {
            this.opinionContent = opinionContent;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public Integer getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
        }
    }
}
