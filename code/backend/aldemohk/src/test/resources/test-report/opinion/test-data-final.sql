mysqldump : mysqldump: [Warning] Using a password on the command line interface can be insecure.
所在位置 行:1 字符: 1
+ mysqldump -u root -p123456 aldemohk --no-create-info --complete-inser ...
+ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    + CategoryInfo          : NotSpecified: (mysqldump: [War...an be insecure.:String) [], RemoteException
    + FullyQualifiedErrorId : NativeCommandError
 
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: aldemohk
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `ad_opinion_survey`
--

LOCK TABLES `ad_opinion_survey` WRITE;
/*!40000 ALTER TABLE `ad_opinion_survey` DISABLE KEYS */;
INSERT INTO `ad_opinion_survey` (`id`, `name`, `assess_year`, `notice_content`, `remark`, `unit_deadline`, `dept_deadline`, `status`, `start_at`, `dept_open_at`, `publish_at`, `created_by`, `created_at`, `updated_by`, `updated_at`, `deleted_flag`) VALUES (1,'2026-R1-KPI-Review',2026,NULL,NULL,'2026-12-31 23:59:59','2028-01-15 23:59:59','PUBLISHED','2026-06-14 14:43:53','2026-06-14 14:44:31','2026-06-14 14:45:03',1,'2026-06-14 14:43:53',1,'2026-06-14 14:43:53',0);
/*!40000 ALTER TABLE `ad_opinion_survey` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_opinion_survey_module`
--

LOCK TABLES `ad_opinion_survey_module` WRITE;
/*!40000 ALTER TABLE `ad_opinion_survey_module` DISABLE KEYS */;
INSERT INTO `ad_opinion_survey_module` (`id`, `survey_id`, `module_code`, `module_name`, `module_alias`, `display_order`, `created_at`, `updated_at`, `deleted_flag`) VALUES (1,1,'KPI','鍏抽敭涓氱哗鎸囨爣',NULL,1,'2026-06-14 14:43:53','2026-06-14 14:43:53',0),(2,1,'SAFETY','瀹夊叏宸ヤ綔鎸囨爣',NULL,2,'2026-06-14 14:43:53','2026-06-14 14:43:53',0);
/*!40000 ALTER TABLE `ad_opinion_survey_module` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_opinion_survey_target`
--

LOCK TABLES `ad_opinion_survey_target` WRITE;
/*!40000 ALTER TABLE `ad_opinion_survey_target` DISABLE KEYS */;
INSERT INTO `ad_opinion_survey_target` (`id`, `survey_id`, `unit_id`, `unit_type`, `created_at`, `deleted_flag`) VALUES (1,1,101,'POWER','2026-06-14 14:43:53',0),(2,1,102,'POWER','2026-06-14 14:43:53',0),(3,1,201,'SUPPORT','2026-06-14 14:43:53',0);
/*!40000 ALTER TABLE `ad_opinion_survey_target` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_opinion_unit_task`
--

LOCK TABLES `ad_opinion_unit_task` WRITE;
/*!40000 ALTER TABLE `ad_opinion_unit_task` DISABLE KEYS */;
INSERT INTO `ad_opinion_unit_task` (`id`, `survey_id`, `unit_id`, `unit_name`, `unit_type`, `fill_status`, `audit_status`, `auto_zero_report`, `submitted_at`, `submitted_by`, `audited_at`, `audited_by`, `last_reject_reason`, `created_at`, `updated_at`, `deleted_flag`) VALUES (1,1,101,'甯傚尯鍏徃','POWER','SUBMITTED','PASS',0,'2026-06-14 14:44:18',1,'2026-06-14 14:44:18',1,'????????????(7.7%),??????????????????????5.0?','2026-06-14 14:43:53','2026-06-14 14:43:53',0),(2,1,102,'甯傚寳鍏徃','POWER','SUBMITTED','PASS',0,'2026-06-14 14:44:07',1,'2026-06-14 14:44:18',1,NULL,'2026-06-14 14:43:53','2026-06-14 14:43:53',0),(3,1,201,'鐢电闄?,'SUPPORT','SUBMITTED','PASS',0,'2026-06-14 14:44:07',1,'2026-06-14 14:44:18',1,NULL,'2026-06-14 14:43:53','2026-06-14 14:43:53',0);
/*!40000 ALTER TABLE `ad_opinion_unit_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_opinion_item`
--

LOCK TABLES `ad_opinion_item` WRITE;
/*!40000 ALTER TABLE `ad_opinion_item` DISABLE KEYS */;
INSERT INTO `ad_opinion_item` (`id`, `survey_id`, `unit_task_id`, `unit_id`, `module_code`, `indicator_category`, `indicator_name`, `factor_name`, `extra_field`, `opinion_category`, `opinion_content`, `reason`, `display_order`, `created_at`, `updated_at`, `deleted_flag`) VALUES (1,1,1,101,'KPI','????','????','???',NULL,'DEFINITION','???2026????????5.2????4.8?,??????????','Q1-Q3????3.1?,????4.6?',1,'2026-06-14 14:44:07','2026-06-14 14:44:18',1),(2,1,1,101,'KPI','????','???','????',NULL,'STANDARD','???????????0.5???0(?????)','?????3????',2,'2026-06-14 14:44:07','2026-06-14 14:44:18',1),(3,1,1,101,'SAFETY','????','????','???',NULL,'DEFINITION','??????????????,??????','???????',3,'2026-06-14 14:44:07','2026-06-14 14:44:18',1),(4,1,2,102,'KPI','????','????','???',NULL,'DATA_SOURCE','?????????????????','????????',1,'2026-06-14 14:44:07','2026-06-14 14:44:07',0),(5,1,2,102,'SAFETY','????','?????','?????',NULL,'DEFINITION','??????????????','?????????',2,'2026-06-14 14:44:07','2026-06-14 14:44:07',0),(6,1,3,201,'KPI','????','????','????',NULL,'DEFINITION','????????????,?????????','???????',1,'2026-06-14 14:44:07','2026-06-14 14:44:07',0),(7,1,1,101,'KPI','????','????','???',NULL,'DEFINITION','?????,??????????5.0?(???4.8?)','??Q1-Q3????:3.1??Q3???15%,?????4.8???',1,'2026-06-14 14:44:18','2026-06-14 14:44:18',0),(8,1,1,101,'KPI','????','???','????',NULL,'STANDARD','?????????','?????3??????',2,'2026-06-14 14:44:18','2026-06-14 14:44:18',0);
/*!40000 ALTER TABLE `ad_opinion_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_opinion_unit_audit_log`
--

LOCK TABLES `ad_opinion_unit_audit_log` WRITE;
/*!40000 ALTER TABLE `ad_opinion_unit_audit_log` DISABLE KEYS */;
INSERT INTO `ad_opinion_unit_audit_log` (`id`, `unit_task_id`, `survey_id`, `action`, `actor_role`, `actor_id`, `reject_reason`, `created_at`) VALUES (1,2,1,'PASS','R03',1,NULL,'2026-06-14 14:44:18'),(2,3,1,'PASS','R03',1,NULL,'2026-06-14 14:44:18'),(3,1,1,'REJECT','R03',1,'????????????(7.7%),??????????????????????5.0?','2026-06-14 14:44:18'),(4,1,1,'PASS','R03',1,NULL,'2026-06-14 14:44:18');
/*!40000 ALTER TABLE `ad_opinion_unit_audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_opinion_dept_task`
--

LOCK TABLES `ad_opinion_dept_task` WRITE;
/*!40000 ALTER TABLE `ad_opinion_dept_task` DISABLE KEYS */;
INSERT INTO `ad_opinion_dept_task` (`id`, `survey_id`, `department_id`, `department_name`, `module_code`, `submit_status`, `audit_status`, `submitted_at`, `submitted_by`, `audited_at`, `audited_by`, `last_reject_reason`, `created_at`, `updated_at`, `deleted_flag`) VALUES (1,1,1,'璐㈠姟閮?,'KPI','SUBMITTED','PASS','2026-06-14 14:44:31',1,'2026-06-14 14:44:31',1,NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(2,1,1,'璐㈠姟閮?,'SAFETY','SUBMITTED','PASS','2026-06-14 14:44:55',1,'2026-06-14 14:44:55',1,NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(3,1,2,'鍙戝睍閮?,'KPI','SUBMITTED','PASS','2026-06-14 14:44:55',1,'2026-06-14 14:44:55',1,NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(4,1,2,'鍙戝睍閮?,'SAFETY','SUBMITTED','PASS','2026-06-14 14:44:55',1,'2026-06-14 14:44:55',1,NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(5,1,3,'鍏氬瀹ｄ紶閮?,'KPI','SUBMITTED','PASS','2026-06-14 14:44:55',1,'2026-06-14 14:44:55',1,NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(6,1,3,'鍏氬瀹ｄ紶閮?,'SAFETY','SUBMITTED','PASS','2026-06-14 14:44:31',1,'2026-06-14 14:44:31',1,NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(7,1,4,'瀹夌洃閮?,'KPI','SUBMITTED','PASS','2026-06-14 14:44:55',1,'2026-06-14 14:44:55',1,NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(8,1,4,'瀹夌洃閮?,'SAFETY','SUBMITTED','PASS','2026-06-14 14:44:55',1,'2026-06-14 14:44:55',1,NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(9,1,5,'璁惧閮?,'KPI','SUBMITTED','PASS','2026-06-14 14:44:55',1,'2026-06-14 14:44:55',1,NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(10,1,5,'璁惧閮?,'SAFETY','SUBMITTED','PASS','2026-06-14 14:44:55',1,'2026-06-14 14:44:55',1,NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0);
/*!40000 ALTER TABLE `ad_opinion_dept_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_opinion_feedback`
--

LOCK TABLES `ad_opinion_feedback` WRITE;
/*!40000 ALTER TABLE `ad_opinion_feedback` DISABLE KEYS */;
INSERT INTO `ad_opinion_feedback` (`id`, `survey_id`, `dept_task_id`, `item_id`, `is_adopted`, `adoption_remark`, `remark`, `created_at`, `updated_at`, `deleted_flag`) VALUES (1,1,1,1,1,'????,?????4.8????,??????????5.0?????','?????????','2026-06-14 14:44:30','2026-06-14 14:44:30',0),(2,1,1,2,1,'???????,?????????',NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(3,1,1,3,1,'??,?????????????',NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(4,1,1,4,1,'????????????',NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(5,1,1,5,1,'???????',NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(6,1,1,6,0,'????????????',NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(7,1,6,1,1,'5.0??????',NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(8,1,6,2,1,'???????????',NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(9,1,6,3,1,'????3??',NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(10,1,6,4,0,'????????????',NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(11,1,6,5,1,'???????',NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0),(12,1,6,6,0,NULL,NULL,'2026-06-14 14:44:30','2026-06-14 14:44:30',0);
/*!40000 ALTER TABLE `ad_opinion_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_opinion_dept_audit_log`
--

LOCK TABLES `ad_opinion_dept_audit_log` WRITE;
/*!40000 ALTER TABLE `ad_opinion_dept_audit_log` DISABLE KEYS */;
INSERT INTO `ad_opinion_dept_audit_log` (`id`, `dept_task_id`, `survey_id`, `action`, `actor_role`, `actor_id`, `reject_reason`, `created_at`) VALUES (1,1,1,'PASS','R05',1,NULL,'2026-06-14 14:44:31'),(2,6,1,'PASS','R05',1,NULL,'2026-06-14 14:44:31'),(3,2,1,'PASS','R05',1,NULL,'2026-06-14 14:44:55'),(4,3,1,'PASS','R05',1,NULL,'2026-06-14 14:44:55'),(5,4,1,'PASS','R05',1,NULL,'2026-06-14 14:44:55'),(6,5,1,'PASS','R05',1,NULL,'2026-06-14 14:44:55'),(7,7,1,'PASS','R05',1,NULL,'2026-06-14 14:44:55'),(8,8,1,'PASS','R05',1,NULL,'2026-06-14 14:44:55'),(9,9,1,'PASS','R05',1,NULL,'2026-06-14 14:44:55'),(10,10,1,'PASS','R05',1,NULL,'2026-06-14 14:44:55');
/*!40000 ALTER TABLE `ad_opinion_dept_audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_opinion_summary_item`
--

LOCK TABLES `ad_opinion_summary_item` WRITE;
/*!40000 ALTER TABLE `ad_opinion_summary_item` DISABLE KEYS */;
INSERT INTO `ad_opinion_summary_item` (`id`, `survey_id`, `feedback_id`, `item_id`, `final_is_adopted`, `final_adoption_remark`, `adjusted_content`, `created_at`, `updated_at`, `deleted_flag`) VALUES (1,1,1,1,0,NULL,NULL,'2026-06-14 14:45:02','2026-06-14 14:45:02',0),(2,1,2,2,0,NULL,NULL,'2026-06-14 14:45:02','2026-06-14 14:45:02',0),(3,1,3,3,0,NULL,NULL,'2026-06-14 14:45:02','2026-06-14 14:45:02',0),(4,1,4,4,0,NULL,NULL,'2026-06-14 14:45:02','2026-06-14 14:45:02',0),(5,1,5,5,0,NULL,NULL,'2026-06-14 14:45:02','2026-06-14 14:45:02',0),(6,1,6,6,0,NULL,NULL,'2026-06-14 14:45:02','2026-06-14 14:45:02',0),(7,1,7,1,0,NULL,NULL,'2026-06-14 14:45:02','2026-06-14 14:45:02',0),(8,1,8,2,0,NULL,NULL,'2026-06-14 14:45:02','2026-06-14 14:45:02',0),(9,1,9,3,0,NULL,NULL,'2026-06-14 14:45:02','2026-06-14 14:45:02',0),(10,1,10,4,0,NULL,NULL,'2026-06-14 14:45:02','2026-06-14 14:45:02',0),(11,1,11,5,0,NULL,NULL,'2026-06-14 14:45:02','2026-06-14 14:45:02',0),(12,1,12,6,0,NULL,NULL,'2026-06-14 14:45:02','2026-06-14 14:45:02',0);
/*!40000 ALTER TABLE `ad_opinion_summary_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_opinion_action_log`
--

LOCK TABLES `ad_opinion_action_log` WRITE;
/*!40000 ALTER TABLE `ad_opinion_action_log` DISABLE KEYS */;
INSERT INTO `ad_opinion_action_log` (`id`, `survey_id`, `action`, `from_status`, `to_status`, `actor_role`, `actor_id`, `payload_summary`, `created_at`) VALUES (1,1,'START_SURVEY','DRAFT','WAIT_FILL','R01',1,'init unit_task=3','2026-06-14 14:43:53'),(2,1,'UNIT_SUBMIT','WAIT_FILL','FILLING','R02',1,'unit taskId=1 submitted','2026-06-14 14:44:07'),(3,1,'UNIT_SUBMIT','FILLING','FILLING','R02',1,'unit taskId=2 submitted','2026-06-14 14:44:07'),(4,1,'UNIT_SUBMIT','FILLING','FILLING','R02',1,'unit taskId=3 submitted','2026-06-14 14:44:07'),(5,1,'UNIT_AUDIT_PASS','PENDING','PASS','R03',1,'unit_task_id=2','2026-06-14 14:44:18'),(6,1,'UNIT_AUDIT_PASS','PENDING','PASS','R03',1,'unit_task_id=3','2026-06-14 14:44:18'),(7,1,'UNIT_AUDIT_REJECT','PENDING','REJECTED','R03',1,'unit_task_id=1 reason=????????????(7.7%),??????????????????????5.0?','2026-06-14 14:44:18'),(8,1,'UNIT_SUBMIT','FILLING','FILLING','R02',1,'unit taskId=1 submitted','2026-06-14 14:44:18'),(9,1,'UNIT_AUDIT_PASS','PENDING','PASS','R03',1,'unit_task_id=1','2026-06-14 14:44:18'),(10,1,'START_DEPT_FEEDBACK','FILLING','DEPT_FEEDBACK','R01',1,'created dept_task=10','2026-06-14 14:44:30'),(11,1,'DEPT_SUBMIT',NULL,NULL,'R04',1,'dept_task_id=1','2026-06-14 14:44:30'),(12,1,'DEPT_SUBMIT',NULL,NULL,'R04',1,'dept_task_id=6','2026-06-14 14:44:30'),(13,1,'DEPT_AUDIT_PASS','PENDING','PASS','R05',1,'dept_task_id=1','2026-06-14 14:44:30'),(14,1,'DEPT_AUDIT_PASS','PENDING','PASS','R05',1,'dept_task_id=6','2026-06-14 14:44:30'),(15,1,'DEPT_SUBMIT',NULL,NULL,'R04',1,'dept_task_id=2','2026-06-14 14:44:54'),(16,1,'DEPT_SUBMIT',NULL,NULL,'R04',1,'dept_task_id=3','2026-06-14 14:44:54'),(17,1,'DEPT_SUBMIT',NULL,NULL,'R04',1,'dept_task_id=4','2026-06-14 14:44:54'),(18,1,'DEPT_SUBMIT',NULL,NULL,'R04',1,'dept_task_id=5','2026-06-14 14:44:54'),(19,1,'DEPT_SUBMIT',NULL,NULL,'R04',1,'dept_task_id=7','2026-06-14 14:44:54'),(20,1,'DEPT_SUBMIT',NULL,NULL,'R04',1,'dept_task_id=8','2026-06-14 14:44:54'),(21,1,'DEPT_SUBMIT',NULL,NULL,'R04',1,'dept_task_id=9','2026-06-14 14:44:54'),(22,1,'DEPT_SUBMIT',NULL,NULL,'R04',1,'dept_task_id=10','2026-06-14 14:44:54'),(23,1,'DEPT_AUDIT_PASS','PENDING','PASS','R05',1,'dept_task_id=2','2026-06-14 14:44:54'),(24,1,'DEPT_AUDIT_PASS','PENDING','PASS','R05',1,'dept_task_id=3','2026-06-14 14:44:54'),(25,1,'DEPT_AUDIT_PASS','PENDING','PASS','R05',1,'dept_task_id=4','2026-06-14 14:44:54'),(26,1,'DEPT_AUDIT_PASS','PENDING','PASS','R05',1,'dept_task_id=5','2026-06-14 14:44:54'),(27,1,'DEPT_AUDIT_PASS','PENDING','PASS','R05',1,'dept_task_id=7','2026-06-14 14:44:54'),(28,1,'DEPT_AUDIT_PASS','PENDING','PASS','R05',1,'dept_task_id=8','2026-06-14 14:44:54'),(29,1,'DEPT_AUDIT_PASS','PENDING','PASS','R05',1,'dept_task_id=9','2026-06-14 14:44:54'),(30,1,'DEPT_AUDIT_PASS','PENDING','PASS','R05',1,'dept_task_id=10','2026-06-14 14:44:54'),(31,1,'SUMMARIZE','DEPT_FEEDBACK','DONE','R01',1,'summary items initialized: 12','2026-06-14 14:45:02'),(32,1,'PUBLISH','DONE','PUBLISHED','R01',1,'published at 2026-06-14T14:45:02.610148100','2026-06-14 14:45:02');
/*!40000 ALTER TABLE `ad_opinion_action_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_opinion_reminder_log`
--

LOCK TABLES `ad_opinion_reminder_log` WRITE;
/*!40000 ALTER TABLE `ad_opinion_reminder_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `ad_opinion_reminder_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ad_opinion_attachment`
--

LOCK TABLES `ad_opinion_attachment` WRITE;
/*!40000 ALTER TABLE `ad_opinion_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `ad_opinion_attachment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-14 14:45:46
