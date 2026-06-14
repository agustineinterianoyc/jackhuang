-- ============================================================================
-- aldemohk（aldemohk）— 指标体系意见征集模块（opinion）
-- 模块编码：opinion
-- 表前缀：ad_opinion_
-- 设计基线：docs/设计/指标体系意见征集模块/数据库设计说明书 指标体系意见征集模块.md
-- 需求包：2026-06-opinion-baseline / STEP-001（数据库基线）
-- 数据库方言：MySQL
-- 字符集：utf8mb4，排序规则 utf8mb4_general_ci
-- 约定：
--   - 主键统一 BIGINT UNSIGNED AUTO_INCREMENT
--   - 状态字段统一 VARCHAR(32)，使用业务枚举常量
--   - 所有表必含 deleted_flag + created_at + updated_at（除日志型表）
--   - 唯一约束统一带 deleted_flag，避免逻辑删除后冲突
--   - 不创建物理外键，仅在应用层维护一致性
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 4.1 ad_opinion_survey 征集任务主表
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ad_opinion_survey` (
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`           VARCHAR(100) NOT NULL COMMENT '征集名称',
    `assess_year`    INT          NOT NULL COMMENT '考核年份',
    `notice_content` VARCHAR(1000) DEFAULT NULL COMMENT '通知内容',
    `remark`         VARCHAR(1000) DEFAULT NULL COMMENT '备注',
    `unit_deadline`  DATETIME     NOT NULL COMMENT '基层截止时间',
    `dept_deadline`  DATETIME     NOT NULL COMMENT '专业截止时间',
    `status`         VARCHAR(32)  NOT NULL DEFAULT 'DRAFT' COMMENT '主状态：DRAFT/WAIT_FILL/FILLING/DEPT_FEEDBACK/DONE/PUBLISHED',
    `start_at`       DATETIME     DEFAULT NULL COMMENT '开启征集时间',
    `dept_open_at`   DATETIME     DEFAULT NULL COMMENT '开启专业反馈时间',
    `publish_at`     DATETIME     DEFAULT NULL COMMENT '发布时间',
    `created_by`     BIGINT       NOT NULL COMMENT '创建人 ID',
    `created_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by`     BIGINT       DEFAULT NULL COMMENT '最后更新人',
    `updated_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `deleted_flag`   TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除 / 1 已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_year_name` (`assess_year`, `name`, `deleted_flag`),
    KEY `idx_status_year` (`status`, `assess_year`, `created_at`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '征集任务主表';

-- ----------------------------------------------------------------------------
-- 4.2 ad_opinion_survey_module 征集模块明细表
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ad_opinion_survey_module` (
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `survey_id`     BIGINT UNSIGNED NOT NULL COMMENT '征集任务 ID',
    `module_code`   VARCHAR(32) NOT NULL COMMENT '模块编码：KPI/BONUS/PARTY/SAFETY/OTHER',
    `module_name`   VARCHAR(50) NOT NULL COMMENT '模块标准名',
    `module_alias`  VARCHAR(50) DEFAULT NULL COMMENT '模块别称（用户自定义）',
    `display_order` INT         NOT NULL DEFAULT 0 COMMENT '显示顺序（升序）',
    `created_at`    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `deleted_flag`  TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_survey_module` (`survey_id`, `module_code`, `deleted_flag`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '征集模块明细表';

-- ----------------------------------------------------------------------------
-- 4.3 ad_opinion_survey_target 征集对象表
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ad_opinion_survey_target` (
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `survey_id`    BIGINT UNSIGNED NOT NULL COMMENT '征集任务 ID',
    `unit_id`      BIGINT      NOT NULL COMMENT '单位 ID（引用组织主数据）',
    `unit_type`    VARCHAR(32) NOT NULL COMMENT '单位类型：POWER/SUPPORT/MARKET/OTHER',
    `created_at`   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted_flag` TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_survey_unit` (`survey_id`, `unit_id`, `deleted_flag`),
    KEY `idx_unit_type` (`unit_type`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '征集对象表';

-- ----------------------------------------------------------------------------
-- 4.4 ad_opinion_unit_task 基层任务表
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ad_opinion_unit_task` (
    `id`                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `survey_id`           BIGINT UNSIGNED NOT NULL COMMENT '征集任务 ID',
    `unit_id`             BIGINT       NOT NULL COMMENT '单位 ID',
    `unit_name`           VARCHAR(200) NOT NULL COMMENT '单位名称冗余',
    `unit_type`           VARCHAR(32)  NOT NULL COMMENT '单位类型',
    `fill_status`         VARCHAR(32)  NOT NULL DEFAULT 'PENDING' COMMENT '填报子状态：PENDING/SUBMITTED',
    `audit_status`        VARCHAR(32)  NOT NULL DEFAULT 'NONE' COMMENT '审核子状态：NONE/PENDING/PASS/REJECTED',
    `auto_zero_report`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否零报送',
    `submitted_at`        DATETIME     DEFAULT NULL COMMENT '基层提交时间',
    `submitted_by`        BIGINT       DEFAULT NULL COMMENT '基层提交人',
    `audited_at`          DATETIME     DEFAULT NULL COMMENT '审核时间',
    `audited_by`          BIGINT       DEFAULT NULL COMMENT '审核人',
    `last_reject_reason`  VARCHAR(260) DEFAULT NULL COMMENT '最近一次退回原因',
    `created_at`          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `deleted_flag`        TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_survey_unit_task` (`survey_id`, `unit_id`, `deleted_flag`),
    KEY `idx_status` (`survey_id`, `fill_status`, `audit_status`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基层任务表';

-- ----------------------------------------------------------------------------
-- 4.5 ad_opinion_item 基层意见行表
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ad_opinion_item` (
    `id`                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `survey_id`           BIGINT UNSIGNED NOT NULL COMMENT '征集任务 ID（冗余）',
    `unit_task_id`        BIGINT UNSIGNED NOT NULL COMMENT '基层任务 ID',
    `unit_id`             BIGINT       NOT NULL COMMENT '单位 ID（冗余）',
    `module_code`         VARCHAR(32)  NOT NULL COMMENT '所属模块（Tab）',
    `indicator_category`  VARCHAR(100) DEFAULT NULL COMMENT '指标类别 / 加分事项 / 考核内容 / 方案条款',
    `indicator_name`      VARCHAR(200) DEFAULT NULL COMMENT '指标名称 / 内容',
    `factor_name`         VARCHAR(200) DEFAULT NULL COMMENT '因子名称 / 评价标准 / 考评方式',
    `extra_field`         VARCHAR(500) DEFAULT NULL COMMENT 'Tab 特有字段冗余',
    `opinion_category`    VARCHAR(32)  NOT NULL COMMENT '意见分类：DEFINITION/STANDARD/DATA_SOURCE/OTHER',
    `opinion_content`     VARCHAR(260) NOT NULL COMMENT '意见内容',
    `reason`              VARCHAR(260) DEFAULT NULL COMMENT '原因说明',
    `display_order`       INT          NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `created_at`          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `deleted_flag`        TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_unit_task` (`unit_task_id`, `module_code`, `display_order`),
    KEY `idx_survey_module` (`survey_id`, `module_code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基层意见行表';

-- ----------------------------------------------------------------------------
-- 4.6 ad_opinion_unit_audit_log 基层审核日志表
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ad_opinion_unit_audit_log` (
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `unit_task_id`   BIGINT UNSIGNED NOT NULL COMMENT '基层任务 ID',
    `survey_id`      BIGINT UNSIGNED NOT NULL COMMENT '征集任务 ID（冗余）',
    `action`         VARCHAR(32)  NOT NULL COMMENT '动作：PASS/REJECT/OPS_REJECT',
    `actor_role`     VARCHAR(32)  NOT NULL COMMENT '操作角色：R03/R01',
    `actor_id`       BIGINT       NOT NULL COMMENT '操作人 ID',
    `reject_reason`  VARCHAR(260) DEFAULT NULL COMMENT '退回原因',
    `created_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`),
    KEY `idx_unit_task_log` (`unit_task_id`, `created_at`),
    KEY `idx_survey_log` (`survey_id`, `created_at`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基层审核日志表';

-- ----------------------------------------------------------------------------
-- 4.7 ad_opinion_dept_task 专业任务表
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ad_opinion_dept_task` (
    `id`                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `survey_id`           BIGINT UNSIGNED NOT NULL COMMENT '征集任务 ID',
    `department_id`       BIGINT       NOT NULL COMMENT '部门 ID',
    `department_name`     VARCHAR(200) NOT NULL COMMENT '部门名称冗余',
    `module_code`         VARCHAR(32)  NOT NULL COMMENT '该部门负责的模块',
    `submit_status`       VARCHAR(32)  NOT NULL DEFAULT 'PENDING' COMMENT '提交子状态：PENDING/SUBMITTED',
    `audit_status`        VARCHAR(32)  NOT NULL DEFAULT 'NONE' COMMENT '审核子状态：NONE/PENDING/PASS/REJECTED',
    `submitted_at`        DATETIME     DEFAULT NULL COMMENT '提交时间',
    `submitted_by`        BIGINT       DEFAULT NULL COMMENT '提交人',
    `audited_at`          DATETIME     DEFAULT NULL COMMENT '审核时间',
    `audited_by`          BIGINT       DEFAULT NULL COMMENT '审核人',
    `last_reject_reason`  VARCHAR(260) DEFAULT NULL COMMENT '最近一次退回原因',
    `created_at`          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `deleted_flag`        TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_survey_dept_module` (`survey_id`, `department_id`, `module_code`, `deleted_flag`),
    KEY `idx_dept_status` (`survey_id`, `submit_status`, `audit_status`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '专业任务表';

-- ----------------------------------------------------------------------------
-- 4.8 ad_opinion_feedback 专业反馈行表
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ad_opinion_feedback` (
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `survey_id`       BIGINT UNSIGNED NOT NULL COMMENT '征集任务 ID（冗余）',
    `dept_task_id`    BIGINT UNSIGNED NOT NULL COMMENT '专业任务 ID',
    `item_id`         BIGINT UNSIGNED NOT NULL COMMENT '基层意见 ID',
    `is_adopted`      TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否采纳：0/1',
    `adoption_remark` VARCHAR(260) DEFAULT NULL COMMENT '意见采纳说明（采纳时必填）',
    `remark`         VARCHAR(500) DEFAULT NULL COMMENT '备注（非必填）',
    `created_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `deleted_flag`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dept_task_item` (`dept_task_id`, `item_id`, `deleted_flag`),
    KEY `idx_feedback_survey` (`survey_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '专业反馈行表';

-- ----------------------------------------------------------------------------
-- 4.9 ad_opinion_dept_audit_log 专业审核日志表
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ad_opinion_dept_audit_log` (
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `dept_task_id`   BIGINT UNSIGNED NOT NULL COMMENT '专业任务 ID',
    `survey_id`      BIGINT UNSIGNED NOT NULL COMMENT '征集任务 ID（冗余）',
    `action`         VARCHAR(32)  NOT NULL COMMENT '动作：PASS/REJECT/OPS_REJECT',
    `actor_role`     VARCHAR(32)  NOT NULL COMMENT '操作角色：R05/R01',
    `actor_id`       BIGINT       NOT NULL COMMENT '操作人 ID',
    `reject_reason`  VARCHAR(260) DEFAULT NULL COMMENT '退回原因',
    `created_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`),
    KEY `idx_dept_task_log` (`dept_task_id`, `created_at`),
    KEY `idx_dept_survey_log` (`survey_id`, `created_at`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '专业审核日志表';

-- ----------------------------------------------------------------------------
-- 4.10 ad_opinion_summary_item 汇总采纳行表
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ad_opinion_summary_item` (
    `id`                    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `survey_id`             BIGINT UNSIGNED NOT NULL COMMENT '征集任务 ID',
    `feedback_id`           BIGINT UNSIGNED NOT NULL COMMENT '关联反馈 ID',
    `item_id`               BIGINT UNSIGNED NOT NULL COMMENT '关联意见 ID（冗余）',
    `final_is_adopted`      TINYINT(1)    NOT NULL DEFAULT 0 COMMENT '最终是否采纳',
    `final_adoption_remark` VARCHAR(260)  DEFAULT NULL COMMENT '最终采纳说明',
    `adjusted_content`      VARCHAR(1000) DEFAULT NULL COMMENT '调整后内容',
    `created_at`            DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`            DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `deleted_flag`          TINYINT(1)    NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_survey_feedback` (`survey_id`, `feedback_id`, `deleted_flag`),
    KEY `idx_summary_item` (`item_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '汇总采纳行表';

-- ----------------------------------------------------------------------------
-- 4.11 ad_opinion_attachment 附件表
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ad_opinion_attachment` (
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `survey_id`    BIGINT UNSIGNED NOT NULL COMMENT '征集任务 ID',
    `biz_type`     VARCHAR(32)  NOT NULL COMMENT '业务类型：MODULE/SUMMARY',
    `biz_ref_id`   BIGINT UNSIGNED DEFAULT NULL COMMENT '业务关联 ID（模块附件指向 survey_module.id；汇总附件指向 summary_item.id）',
    `file_id`      VARCHAR(64)  NOT NULL COMMENT '文件服务 ID',
    `file_name`    VARCHAR(200) NOT NULL COMMENT '文件名',
    `file_size`    BIGINT       NOT NULL DEFAULT 0 COMMENT '文件大小（字节）',
    `created_by`   BIGINT       NOT NULL COMMENT '上传人',
    `created_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `deleted_flag` TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_biz` (`biz_type`, `biz_ref_id`),
    KEY `idx_attachment_survey` (`survey_id`),
    KEY `idx_attachment_file` (`file_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '附件表';

-- ----------------------------------------------------------------------------
-- 4.12 ad_opinion_reminder_log 提醒日志表
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ad_opinion_reminder_log` (
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `survey_id`    BIGINT UNSIGNED NOT NULL COMMENT '征集任务 ID',
    `target_type`  VARCHAR(16)  NOT NULL COMMENT '目标类型：UNIT/DEPT',
    `target_id`    BIGINT       NOT NULL COMMENT '目标 ID（单位 ID 或部门 ID）',
    `mode`         VARCHAR(16)  NOT NULL COMMENT '提醒方式：BATCH/SINGLE',
    `actor_id`     BIGINT       NOT NULL COMMENT '操作人',
    `created_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提醒时间',
    PRIMARY KEY (`id`),
    KEY `idx_freq_check` (`survey_id`, `target_type`, `target_id`, `created_at`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '提醒日志表';

-- ----------------------------------------------------------------------------
-- 4.13 ad_opinion_action_log 业务动作审计表
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ad_opinion_action_log` (
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `survey_id`       BIGINT UNSIGNED NOT NULL COMMENT '征集任务 ID',
    `action`          VARCHAR(64)  NOT NULL COMMENT '动作编码',
    `from_status`     VARCHAR(32)  DEFAULT NULL COMMENT '旧状态',
    `to_status`       VARCHAR(32)  DEFAULT NULL COMMENT '新状态',
    `actor_role`     VARCHAR(32)   NOT NULL COMMENT '操作角色：R01-R05 或 SYSTEM',
    `actor_id`       BIGINT        DEFAULT NULL COMMENT '操作人（系统动作可空）',
    `payload_summary` VARCHAR(500) DEFAULT NULL COMMENT '关键信息摘要',
    `created_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '动作时间',
    PRIMARY KEY (`id`),
    KEY `idx_survey_action` (`survey_id`, `action`, `created_at`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '业务动作审计表';
