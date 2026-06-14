package com.hk.demo.app.service.opinion.log;

import com.hk.demo.app.mapper.opinion.OpinionActionLogMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionActionLogDO;
import org.springframework.stereotype.Component;

/**
 * 业务动作审计日志写入器。
 *
 * 状态机推进、零报送、提醒等动作统一通过本组件落 ad_opinion_action_log。
 */
@Component
public class OpinionActionLogger {

    private final OpinionActionLogMapper mapper;

    public OpinionActionLogger(OpinionActionLogMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 写入一条审计日志。
     *
     * @param surveyId       征集任务 ID
     * @param action         动作编码（OpinionActionLogAction 常量）
     * @param fromStatus     旧状态（可空）
     * @param toStatus       新状态（可空）
     * @param actorRole      操作角色（R01-R05 或 SYSTEM）
     * @param actorId        操作人（系统动作可空）
     * @param payloadSummary 关键信息摘要（可空）
     */
    public void log(Long surveyId, String action, String fromStatus, String toStatus,
                    String actorRole, Long actorId, String payloadSummary) {
        OpinionActionLogDO log = new OpinionActionLogDO();
        log.setSurveyId(surveyId);
        log.setAction(action);
        log.setFromStatus(fromStatus);
        log.setToStatus(toStatus);
        log.setActorRole(actorRole);
        log.setActorId(actorId);
        log.setPayloadSummary(truncate(payloadSummary, 500));
        mapper.insert(log);
    }

    private String truncate(String value, int max) {
        if (value == null) {
            return null;
        }
        return value.length() > max ? value.substring(0, max) : value;
    }
}
