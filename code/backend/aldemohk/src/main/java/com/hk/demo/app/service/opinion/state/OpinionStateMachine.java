package com.hk.demo.app.service.opinion.state;

import com.hk.demo.api.enums.ResultCode;
import com.hk.demo.api.enums.opinion.OpinionMainStatus;
import com.hk.demo.core.exception.BusinessException;

import java.util.EnumSet;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * 指标体系意见征集主流程状态机。
 *
 * 集中收口所有主状态推进规则；service 层不允许直接 update survey.status 字段。
 *
 * 主流程（与设计基线 v1.0 一致）：
 * <pre>
 *   DRAFT
 *     -- start() -->            WAIT_FILL
 *   WAIT_FILL
 *     -- markFilling() -->      FILLING        （任一基层动作触发；STEP-004 实现）
 *   FILLING
 *     -- openDeptFeedback() --> DEPT_FEEDBACK  （前置：全部基层已审核通过；STEP-005 实现）
 *   DEPT_FEEDBACK
 *     -- markDone() -->         DONE           （全部专业已审核通过自动推进；STEP-007 实现）
 *   DONE
 *     -- publish() -->          PUBLISHED      （STEP-007 实现）
 * </pre>
 */
@Component
public class OpinionStateMachine {

    /**
     * 合法转移表：from -> 允许的 to 集合。
     */
    private static final Map<OpinionMainStatus, EnumSet<OpinionMainStatus>> ALLOWED = Map.of(
        OpinionMainStatus.DRAFT, EnumSet.of(OpinionMainStatus.WAIT_FILL),
        OpinionMainStatus.WAIT_FILL, EnumSet.of(OpinionMainStatus.FILLING),
        OpinionMainStatus.FILLING, EnumSet.of(OpinionMainStatus.DEPT_FEEDBACK),
        OpinionMainStatus.DEPT_FEEDBACK, EnumSet.of(OpinionMainStatus.DONE),
        OpinionMainStatus.DONE, EnumSet.of(OpinionMainStatus.PUBLISHED),
        OpinionMainStatus.PUBLISHED, EnumSet.noneOf(OpinionMainStatus.class)
    );

    /**
     * 校验主状态推进是否合法；不合法时抛出业务异常。
     *
     * 实现层在执行任何状态推进前必须先调用本方法。
     *
     * @param from 当前状态
     * @param to   目标状态
     */
    public void assertTransition(OpinionMainStatus from, OpinionMainStatus to) {
        if (from == null || to == null) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE);
        }
        EnumSet<OpinionMainStatus> allowed = ALLOWED.get(from);
        if (allowed == null || !allowed.contains(to)) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE.getCode(),
                String.format("当前状态 %s 不允许推进到 %s", from.getText(), to.getText()));
        }
    }

    /**
     * 是否为合法转移。
     */
    public boolean canTransition(OpinionMainStatus from, OpinionMainStatus to) {
        if (from == null || to == null) {
            return false;
        }
        EnumSet<OpinionMainStatus> allowed = ALLOWED.get(from);
        return allowed != null && allowed.contains(to);
    }

    /**
     * 校验当前状态是否为指定值；否则抛业务异常。
     */
    public void assertCurrent(OpinionMainStatus current, OpinionMainStatus expected) {
        if (current != expected) {
            throw new BusinessException(ResultCode.OPINION_ILLEGAL_STATE.getCode(),
                String.format("当前状态 %s 不允许执行该操作（仅 %s 状态可执行）",
                    current == null ? "-" : current.getText(),
                    expected.getText()));
        }
    }
}
