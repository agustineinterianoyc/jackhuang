package com.hk.demo.app.task.opinion;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hk.demo.api.enums.opinion.OpinionActionLogAction;
import com.hk.demo.api.enums.opinion.OpinionMainStatus;
import com.hk.demo.app.mapper.opinion.OpinionDeptTaskMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionDeptTaskDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionSurveyDO;
import com.hk.demo.app.mapper.opinion.OpinionSurveyMapper;
import com.hk.demo.app.service.opinion.log.OpinionActionLogger;
import com.hk.demo.app.service.opinion.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 专业截止提醒定时任务。
 *
 * 每 5 分钟扫描专业任务，对截止时间临近（≤24h）或已过期
 * 且 submit_status='PENDING' 的专业任务写入提醒审计日志。
 */
@Component
public class OpinionDeptDeadlineRemindJob {

    private static final Logger log = LoggerFactory.getLogger(OpinionDeptDeadlineRemindJob.class);

    private final OpinionDeptTaskMapper deptTaskMapper;
    private final OpinionSurveyMapper surveyMapper;
    private final OpinionActionLogger actionLogger;
    private final NotificationService notificationService;

    public OpinionDeptDeadlineRemindJob(OpinionDeptTaskMapper deptTaskMapper,
                                        OpinionSurveyMapper surveyMapper,
                                        OpinionActionLogger actionLogger,
                                        NotificationService notificationService) {
        this.deptTaskMapper = deptTaskMapper;
        this.surveyMapper = surveyMapper;
        this.actionLogger = actionLogger;
        this.notificationService = notificationService;
    }

    /**
     * 每 5 分钟执行一次提醒扫描。
     */
    @Scheduled(fixedDelay = 300000, initialDelay = 120000)
    @Transactional(rollbackFor = Exception.class)
    public void execute() {
        List<OpinionDeptTaskDO> pendingTasks = deptTaskMapper.selectList(
            new LambdaQueryWrapper<OpinionDeptTaskDO>()
                .eq(OpinionDeptTaskDO::getSubmitStatus, "PENDING"));

        if (CollectionUtils.isEmpty(pendingTasks)) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.plusHours(24);

        int count = 0;
        for (OpinionDeptTaskDO task : pendingTasks) {
            OpinionSurveyDO survey = surveyMapper.selectById(task.getSurveyId());
            if (survey == null) {
                continue;
            }

            OpinionMainStatus status = OpinionMainStatus.fromName(survey.getStatus());
            if (status != OpinionMainStatus.DEPT_FEEDBACK) {
                continue;
            }

            LocalDateTime deadline = survey.getDeptDeadline();
            if (deadline == null) {
                continue;
            }

            if (deadline.isAfter(threshold) && !deadline.isBefore(now)) {
                continue;
            }

            String summary = deadline.isBefore(now)
                ? "专业任务已逾期: deptTaskId=" + task.getId() + " dept=" + task.getDepartmentName() + " deadline=" + deadline
                : "专业任务即将截止: deptTaskId=" + task.getId() + " dept=" + task.getDepartmentName() + " deadline=" + deadline;

            actionLogger.log(task.getSurveyId(), OpinionActionLogAction.REMIND_BATCH,
                null, null, "SYSTEM", null, summary);
            notificationService.send("DEPT", task.getDepartmentId(), "截止提醒", summary);
            count++;
        }

        if (count > 0) {
            log.info("[opinion] dept deadline remind completed: {} tasks", count);
        }
    }
}
