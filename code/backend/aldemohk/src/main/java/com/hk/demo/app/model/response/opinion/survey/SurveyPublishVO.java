package com.hk.demo.app.model.response.opinion.survey;

import java.time.LocalDateTime;

/**
 * API-107 发布响应。
 */
public class SurveyPublishVO {

    private Long id;
    private String status;
    private String statusText;
    private LocalDateTime publishAt;

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

    public LocalDateTime getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(LocalDateTime publishAt) {
        this.publishAt = publishAt;
    }
}
