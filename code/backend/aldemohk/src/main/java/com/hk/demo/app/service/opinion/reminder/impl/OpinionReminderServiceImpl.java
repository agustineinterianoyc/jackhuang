package com.hk.demo.app.service.opinion.reminder.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hk.demo.api.enums.opinion.OpinionActionLogAction;
import com.hk.demo.api.enums.opinion.OpinionDeptSubmitStatus;
import com.hk.demo.api.enums.opinion.OpinionReminderMode;
import com.hk.demo.api.enums.opinion.OpinionReminderTargetType;
import com.hk.demo.api.enums.opinion.OpinionUnitFillStatus;
import com.hk.demo.app.mapper.opinion.OpinionDeptTaskMapper;
import com.hk.demo.app.mapper.opinion.OpinionReminderLogMapper;
import com.hk.demo.app.mapper.opinion.OpinionUnitTaskMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionDeptTaskDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionReminderLogDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionUnitTaskDO;
import com.hk.demo.app.model.request.opinion.reminder.BatchReminderRequest;
import com.hk.demo.app.model.request.opinion.reminder.SingleReminderRequest;
import com.hk.demo.app.model.response.opinion.reminder.BatchReminderVO;
import com.hk.demo.app.service.opinion.log.OpinionActionLogger;
import com.hk.demo.app.service.opinion.mock.OpinionMockMasterDataProvider;
import com.hk.demo.app.service.opinion.notification.NotificationService;
import com.hk.demo.app.service.opinion.reminder.OpinionReminderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 提醒服务实现。
 *
 * 频率控制：同一目标（surveyId + targetType + targetId）5 分钟内不重复提醒。
 * 通知方式当前为 console stub，仅插入提醒日志和审计日志。
 */
@Service
public class OpinionReminderServiceImpl implements OpinionReminderService {

    private static final Logger log = LoggerFactory.getLogger(OpinionReminderServiceImpl.class);

    /** 提醒频率控制间隔（分钟）。 */
    private static final int REMINDER_INTERVAL_MINUTES = 5;

    private final OpinionUnitTaskMapper unitTaskMapper;
    private final OpinionDeptTaskMapper deptTaskMapper;
    private final OpinionReminderLogMapper reminderLogMapper;
    private final OpinionActionLogger actionLogger;
    private final OpinionMockMasterDataProvider masterData;
    private final NotificationService notificationService;

    @Autowired
    public OpinionReminderServiceImpl(OpinionUnitTaskMapper unitTaskMapper,
                                      OpinionDeptTaskMapper deptTaskMapper,
                                      OpinionReminderLogMapper reminderLogMapper,
                                      OpinionActionLogger actionLogger,
                                      OpinionMockMasterDataProvider masterData,
                                      NotificationService notificationService) {
        this.unitTaskMapper = unitTaskMapper;
        this.deptTaskMapper = deptTaskMapper;
        this.reminderLogMapper = reminderLogMapper;
        this.actionLogger = actionLogger;
        this.masterData = masterData;
        this.notificationService = notificationService;
    }

    // ===== API-603 一键提醒 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchReminderVO batchRemind(BatchReminderRequest request) {
        Long surveyId = request.getSurveyId();
        String targetType = request.getTargetType();
        Long actorId = masterData.currentUserId();

        int notified = 0;
        int skipped = 0;

        if (OpinionReminderTargetType.UNIT.name().equals(targetType)) {
            // 查询所有未提交的基层任务
            List<OpinionUnitTaskDO> tasks = unitTaskMapper.selectList(
                new LambdaQueryWrapper<OpinionUnitTaskDO>()
                    .eq(OpinionUnitTaskDO::getSurveyId, surveyId)
                    .ne(OpinionUnitTaskDO::getFillStatus, OpinionUnitFillStatus.SUBMITTED.name()));
            for (OpinionUnitTaskDO task : tasks) {
                if (isInCooldown(surveyId, targetType, task.getUnitId())) {
                    skipped++;
                    continue;
                }
                doSingleRemind(surveyId, targetType, task.getUnitId(), OpinionReminderMode.BATCH, actorId);
                notified++;
            }
        } else if (OpinionReminderTargetType.DEPT.name().equals(targetType)) {
            List<OpinionDeptTaskDO> tasks = deptTaskMapper.selectList(
                new LambdaQueryWrapper<OpinionDeptTaskDO>()
                    .eq(OpinionDeptTaskDO::getSurveyId, surveyId)
                    .ne(OpinionDeptTaskDO::getSubmitStatus, OpinionDeptSubmitStatus.SUBMITTED.name()));
            for (OpinionDeptTaskDO task : tasks) {
                if (isInCooldown(surveyId, targetType, task.getDepartmentId())) {
                    skipped++;
                    continue;
                }
                doSingleRemind(surveyId, targetType, task.getDepartmentId(), OpinionReminderMode.BATCH, actorId);
                notified++;
            }
        }

        log.info("[opinion] batch remind: surveyId={}, targetType={}, notified={}, skipped={}",
            surveyId, targetType, notified, skipped);

        BatchReminderVO vo = new BatchReminderVO();
        vo.setNotifiedCount(notified);
        vo.setSkippedCount(skipped);
        return vo;
    }

    // ===== API-604 单条提醒 =====
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void singleRemind(SingleReminderRequest request) {
        Long surveyId = request.getSurveyId();
        String targetType = request.getTargetType();
        Long targetId = request.getTargetId();
        Long actorId = masterData.currentUserId();

        if (isInCooldown(surveyId, targetType, targetId)) {
            log.info("[opinion] single remind skipped (cooldown): surveyId={}, targetType={}, targetId={}",
                surveyId, targetType, targetId);
            return;
        }

        doSingleRemind(surveyId, targetType, targetId, OpinionReminderMode.SINGLE, actorId);
    }

    // ===== 内部方法 =====

    /**
     * 检查目标是否在 5 分钟冷却期内。
     */
    private boolean isInCooldown(Long surveyId, String targetType, Long targetId) {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(REMINDER_INTERVAL_MINUTES);
        Long count = reminderLogMapper.selectCount(
            new LambdaQueryWrapper<OpinionReminderLogDO>()
                .eq(OpinionReminderLogDO::getSurveyId, surveyId)
                .eq(OpinionReminderLogDO::getTargetType, targetType)
                .eq(OpinionReminderLogDO::getTargetId, targetId)
                .ge(OpinionReminderLogDO::getCreatedAt, threshold));
        return count != null && count > 0;
    }

    /**
     * 执行单次提醒：写入提醒日志、审计日志，控制台打桩。
     */
    private void doSingleRemind(Long surveyId, String targetType, Long targetId,
                                 OpinionReminderMode mode, Long actorId) {
        OpinionReminderLogDO logDO = new OpinionReminderLogDO();
        logDO.setSurveyId(surveyId);
        logDO.setTargetType(targetType);
        logDO.setTargetId(targetId);
        logDO.setMode(mode.name());
        logDO.setActorId(actorId);
        logDO.setCreatedAt(LocalDateTime.now());
        reminderLogMapper.insert(logDO);

        String action = mode == OpinionReminderMode.BATCH
            ? OpinionActionLogAction.REMIND_BATCH
            : OpinionActionLogAction.REMIND_SINGLE;

        actionLogger.log(surveyId, action, null, null, "SYSTEM", actorId,
            "targetType=" + targetType + ", targetId=" + targetId);

        log.info("[opinion] remind sent (stub): surveyId={}, targetType={}, targetId={}, mode={}",
            surveyId, targetType, targetId, mode.name());

        // 发送通知
        String title = mode == OpinionReminderMode.BATCH ? "批量提醒" : "提交提醒";
        notificationService.send(targetType, targetId, title,
            String.format("请及时完成征集任务(#%d)的提交工作", surveyId));
    }
}
