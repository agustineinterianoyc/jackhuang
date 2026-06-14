package com.hk.demo.app.opinion.state;

import com.hk.demo.api.enums.opinion.OpinionMainStatus;
import com.hk.demo.app.service.opinion.state.OpinionStateMachine;
import com.hk.demo.core.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 主状态机单元测试。
 *
 * 验证 6 个主状态间合法 / 非法转移的判定。
 */
class OpinionStateMachineTest {

    private final OpinionStateMachine machine = new OpinionStateMachine();

    // ===== 合法转移 =====
    @Test
    void shouldAllowDraftToWaitFill() {
        assertTrue(machine.canTransition(OpinionMainStatus.DRAFT, OpinionMainStatus.WAIT_FILL));
        assertDoesNotThrow(() ->
            machine.assertTransition(OpinionMainStatus.DRAFT, OpinionMainStatus.WAIT_FILL));
    }

    @Test
    void shouldAllowWaitFillToFilling() {
        assertTrue(machine.canTransition(OpinionMainStatus.WAIT_FILL, OpinionMainStatus.FILLING));
    }

    @Test
    void shouldAllowFillingToDeptFeedback() {
        assertTrue(machine.canTransition(OpinionMainStatus.FILLING, OpinionMainStatus.DEPT_FEEDBACK));
    }

    @Test
    void shouldAllowDeptFeedbackToDone() {
        assertTrue(machine.canTransition(OpinionMainStatus.DEPT_FEEDBACK, OpinionMainStatus.DONE));
    }

    @Test
    void shouldAllowDoneToPublished() {
        assertTrue(machine.canTransition(OpinionMainStatus.DONE, OpinionMainStatus.PUBLISHED));
    }

    // ===== 非法转移 =====
    @Test
    void shouldDenyDraftToFilling() {
        assertFalse(machine.canTransition(OpinionMainStatus.DRAFT, OpinionMainStatus.FILLING));
        assertThrows(BusinessException.class, () ->
            machine.assertTransition(OpinionMainStatus.DRAFT, OpinionMainStatus.FILLING));
    }

    @Test
    void shouldDenyDraftToPublished() {
        assertFalse(machine.canTransition(OpinionMainStatus.DRAFT, OpinionMainStatus.PUBLISHED));
    }

    @Test
    void shouldDenyPublishedToAnyTarget() {
        // PUBLISHED 是终态
        for (OpinionMainStatus to : OpinionMainStatus.values()) {
            assertFalse(machine.canTransition(OpinionMainStatus.PUBLISHED, to),
                "PUBLISHED 不应允许推进到 " + to);
        }
    }

    @Test
    void shouldDenyBackwardTransitions() {
        // 回退禁止
        assertFalse(machine.canTransition(OpinionMainStatus.WAIT_FILL, OpinionMainStatus.DRAFT));
        assertFalse(machine.canTransition(OpinionMainStatus.FILLING, OpinionMainStatus.WAIT_FILL));
        assertFalse(machine.canTransition(OpinionMainStatus.DEPT_FEEDBACK, OpinionMainStatus.FILLING));
    }

    @Test
    void shouldRejectNullStatus() {
        assertThrows(BusinessException.class, () ->
            machine.assertTransition(null, OpinionMainStatus.WAIT_FILL));
        assertThrows(BusinessException.class, () ->
            machine.assertTransition(OpinionMainStatus.DRAFT, null));
        assertFalse(machine.canTransition(null, OpinionMainStatus.WAIT_FILL));
        assertFalse(machine.canTransition(OpinionMainStatus.DRAFT, null));
    }

    @Test
    void shouldEnforceCurrentStatus() {
        assertDoesNotThrow(() ->
            machine.assertCurrent(OpinionMainStatus.DRAFT, OpinionMainStatus.DRAFT));
        assertThrows(BusinessException.class, () ->
            machine.assertCurrent(OpinionMainStatus.WAIT_FILL, OpinionMainStatus.DRAFT));
        assertThrows(BusinessException.class, () ->
            machine.assertCurrent(null, OpinionMainStatus.DRAFT));
    }
}
