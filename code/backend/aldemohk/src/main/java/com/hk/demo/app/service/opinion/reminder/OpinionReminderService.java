package com.hk.demo.app.service.opinion.reminder;

import com.hk.demo.app.model.request.opinion.reminder.BatchReminderRequest;
import com.hk.demo.app.model.request.opinion.reminder.SingleReminderRequest;
import com.hk.demo.app.model.response.opinion.reminder.BatchReminderVO;

/**
 * 提醒服务。
 *
 * 实现 STEP-006 关联的接口：
 * - API-603 一键提醒
 * - API-604 单条提醒
 */
public interface OpinionReminderService {

    /**
     * API-603 一键提醒：对征集任务下所有未提交任务执行提醒。
     */
    BatchReminderVO batchRemind(BatchReminderRequest request);

    /**
     * API-604 单条提醒：对指定目标执行提醒。
     */
    void singleRemind(SingleReminderRequest request);
}
