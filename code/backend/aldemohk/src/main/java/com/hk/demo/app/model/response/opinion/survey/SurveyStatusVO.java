package com.hk.demo.app.model.response.opinion.survey;

import java.time.LocalDateTime;

/**
 * API-101 / API-104 / API-107 等状态推进类接口的统一响应。
 *
 * 设计约定：状态推进类接口必须返回最新主状态 + 中文映射。
 */
public class SurveyStatusVO {

    private Long id;
    private String status;
    private String statusText;
    private LocalDateTime startAt;
    private LocalDateTime publishAt;

    public SurveyStatusVO() {
    }

    public SurveyStatusVO(Long id, String status, String statusText) {
        this.id = id;
        this.status = status;
        this.statusText = statusText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(LocalDateTime publishAt) {
        this.publishAt = publishAt;
    }
}
