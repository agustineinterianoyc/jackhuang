package com.hk.demo.app.model.request.opinion.deptfeedback;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 专业反馈保存入参（R04）。
 */
public class DeptFeedbackSaveRequest {

    @NotEmpty(message = "反馈列表不能为空")
    private List<FeedbackItem> items;

    public List<FeedbackItem> getItems() {
        return items;
    }

    public void setItems(List<FeedbackItem> items) {
        this.items = items;
    }

    /**
     * 反馈条目。
     */
    public static class FeedbackItem {

        private Long itemId;

        /** 是否采纳：1=是 0=否。 */
        private Integer isAdopted;

        private String adoptionRemark;

        private String remark;

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public Integer getIsAdopted() {
            return isAdopted;
        }

        public void setIsAdopted(Integer isAdopted) {
            this.isAdopted = isAdopted;
        }

        public String getAdoptionRemark() {
            return adoptionRemark;
        }

    public void setAdoptionRemark(String adoptionRemark) {
        this.adoptionRemark = adoptionRemark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
}
