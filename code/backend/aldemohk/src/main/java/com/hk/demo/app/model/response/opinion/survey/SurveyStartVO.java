package com.hk.demo.app.model.response.opinion.survey;

/**
 * API-104 开启征集响应。
 */
public class SurveyStartVO extends SurveyStatusVO {

    /** 初始化的基层任务数量。 */
    private Integer unitTaskCount;

    public Integer getUnitTaskCount() {
        return unitTaskCount;
    }

    public void setUnitTaskCount(Integer unitTaskCount) {
        this.unitTaskCount = unitTaskCount;
    }

    public static SurveyStartVO of(Long id, String status, String statusText, int count) {
        SurveyStartVO vo = new SurveyStartVO();
        vo.setId(id);
        vo.setStatus(status);
        vo.setStatusText(statusText);
        vo.setUnitTaskCount(count);
        return vo;
    }
}
