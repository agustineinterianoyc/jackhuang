package com.hk.demo.app.task.opinion;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hk.demo.api.enums.opinion.OpinionActionLogAction;
import com.hk.demo.api.enums.opinion.OpinionUnitAuditStatus;
import com.hk.demo.api.enums.opinion.OpinionUnitFillStatus;
import com.hk.demo.app.mapper.opinion.OpinionSurveyMapper;
import com.hk.demo.app.mapper.opinion.OpinionUnitTaskMapper;
import com.hk.demo.app.model.dataobject.opinion.OpinionSurveyDO;
import com.hk.demo.app.model.dataobject.opinion.OpinionUnitTaskDO;
import com.hk.demo.app.service.opinion.log.OpinionActionLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 基层零报送定时任务。
 *
 * 每 5 分钟扫描已过基层截止时间且 fill_status='PENDING' 的基层任务，
 * 自动将填报子状态置为 SUBMITTED、审核子状态置为 PASS，并落 auto_zero_report=1。
 */
@Component
public class OpinionAutoZeroReportJob {

    private static final Logger log = LoggerFactory.getLogger(OpinionAutoZeroReportJob.class);

    private final OpinionUnitTaskMapper unitTaskMapper;
    private final OpinionSurveyMapper surveyMapper;
    private final OpinionActionLogger actionLogger;

    public OpinionAutoZeroReportJob(OpinionUnitTaskMapper unitTaskMapper,
                                     OpinionSurveyMapper surveyMapper,
                                     OpinionActionLogger actionLogger) {
        this.unitTaskMapper = unitTaskMapper;
        this.surveyMapper = surveyMapper;
        this.actionLogger = actionLogger;
    }

    /**
     * 每 5 分钟执行一次零报送扫描。
     */
    @Scheduled(fixedDelay = 300000, initialDelay = 60000)
    @Transactional(rollbackFor = Exception.class)
    public void execute() {
        List<OpinionUnitTaskDO> pendingTasks = unitTaskMapper.selectList(
            new LambdaQueryWrapper<OpinionUnitTaskDO>()
                .eq(OpinionUnitTaskDO::getFillStatus, OpinionUnitFillStatus.PENDING.name()));

        int count = 0;
        for (OpinionUnitTaskDO task : pendingTasks) {
            OpinionSurveyDO survey = surveyMapper.selectById(task.getSurveyId());
            if (survey == null) continue;

            LocalDateTime deadline = survey.getUnitDeadline();
            if (deadline == null || deadline.isAfter(LocalDateTime.now())) continue;

            task.setFillStatus(OpinionUnitFillStatus.SUBMITTED.name());
            task.setAuditStatus(OpinionUnitAuditStatus.PASS.name());
            task.setAutoZeroReport(1);
            task.setSubmittedAt(LocalDateTime.now());
            unitTaskMapper.updateById(task);

            actionLogger.log(task.getSurveyId(), OpinionActionLogAction.AUTO_ZERO_REPORT,
                null, null, "SYSTEM", null,
                "unit_task=" + task.getId() + " unit=" + task.getUnitName());
            count++;
        }

        if (count > 0) {
            log.info("[opinion] auto zero report completed: {} tasks", count);
        }
    }
}
