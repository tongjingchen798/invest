/*
 Navicat Premium Dump SQL

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : renren_security

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 14/08/2025 20:16:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for issues
-- ----------------------------
DROP TABLE IF EXISTS `issues`;
CREATE TABLE `issues`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '描述',
  `images_addr` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '链接地址',
  `type` tinyint NOT NULL COMMENT '广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0=禁用 1=启用',
  `create_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_type`(`type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_date`(`create_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '广告/图片管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of issues
-- ----------------------------
INSERT INTO `issues` VALUES (1, '首页Logo', '网站Logo图片', 'https://example.com/logo.png', 1, 1, 1, '2025-08-08 00:04:37', '2025-08-08 00:04:37', NULL, NULL);
INSERT INTO `issues` VALUES (2, '轮播图1', '首页轮播图1', 'https://example.com/banner1.jpg', 2, 1, 1, '2025-08-08 00:04:37', '2025-08-08 00:04:37', NULL, NULL);
INSERT INTO `issues` VALUES (3, '轮播图2', '首页轮播图2', 'https://example.com/banner2.jpg', 2, 2, 1, '2025-08-08 00:04:37', '2025-08-08 00:04:37', NULL, NULL);
INSERT INTO `issues` VALUES (4, '个人中心背景', '个人中心背景图片', 'https://example.com/profile-bg.jpg', 3, 1, 1, '2025-08-08 00:04:37', '2025-08-08 00:04:37', NULL, NULL);
INSERT INTO `issues` VALUES (5, '弹窗广告', '弹窗广告图片', 'https://example.com/popup.jpg', 4, 1, 1, '2025-08-08 00:04:37', '2025-08-08 00:04:37', NULL, NULL);

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单ID',
  `invest_id` bigint NULL DEFAULT NULL COMMENT '投资ID',
  `order_amount` decimal(20, 8) NULL DEFAULT NULL COMMENT '订单金额',
  `coupon_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '优惠券类型',
  `coupon_id` bigint NULL DEFAULT NULL COMMENT '优惠券ID',
  `coupon_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `order_describe` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '订单描述',
  `coupon_amount` decimal(20, 8) NULL DEFAULT NULL COMMENT '优惠券金额',
  `user_coupon_id` bigint NULL DEFAULT NULL COMMENT '用户优惠券ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `project_type` int NULL DEFAULT 0 COMMENT '项目类型',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  `salesman_id` bigint NULL DEFAULT NULL COMMENT '业务员ID',
  `salesman_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务员姓名',
  `invest_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '投资名称',
  `order_date` datetime NULL DEFAULT NULL COMMENT '订单时间',
  `invest_count` int NULL DEFAULT NULL COMMENT '投资数量',
  `actual_amount` decimal(20, 8) NULL DEFAULT NULL COMMENT '实际金额',
  `agent_id` bigint NULL DEFAULT NULL COMMENT '代理ID',
  `agent_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代理名称',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签',
  `invite_code_status` tinyint NULL DEFAULT 1 COMMENT '邀请码状态',
  `fission_status` tinyint NULL DEFAULT 0 COMMENT '裂变状态：0-否，1-是',
  `expiration_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `history_gm` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '历史购买记录(JSON格式)',
  `abbreviation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目简称',
  `remaining_purchase_count` int NULL DEFAULT 0 COMMENT '剩余购买份数',
  `pay_hot_flag` tinyint NULL DEFAULT 0 COMMENT '支付热门标志',
  `yifan` tinyint NULL DEFAULT 0 COMMENT '已返标志',
  `type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型名称',
  `superior_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上级手机号',
  `create_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1927659873540751361, 0, '918888888888', 1748403763980, 'Doris', 'Happy Weekend', '2025-07-18 14:49:29', 1, 2000.00000000, NULL, NULL, NULL, 1, 0, '2025-08-02 14:49:29', '[{\"abbreviation\":\"JHNDI MUNDA\",\"expiration_time\":\"未到期\",\"invest_count\":1,\"sygmfs\":2},{\"abbreviation\":\"ANDAR BAHAR\",\"expiration_time\":\"未到期\",\"invest_count\":1,\"sygmfs\":2},{\"abbreviation\":\"ANDAR BAHAR\",\"expiration_time\":\"未到期\",\"invest_count\":2,\"sygmfs\":0},{\"abbreviation\":\"CRASH\",\"expiration_time\":\"未到期\",\"invest_count\":3,\"sygmfs\":0},{\"abbreviation\":\"JHNDI MUNDA\",\"expiration_time\":\"未到期\",\"invest_count\":2,\"sygmfs\":0},{\"abbreviation\":\"HORSE RACING\",\"expiration_time\":\"未到期\",\"invest_count\":3,\"sygmfs\":0},{\"abbreviation\":\"Wingo Lottery\",\"expiration_time\":\"未到期\",\"invest_count\":3,\"sygmfs\":0},{\"abbreviation\":\"Fortune Wheel\",\"expiration_time\":\"未到期\",\"invest_count\":3,\"sygmfs\":0},{\"abbreviation\":\"BIG BATTLE\",\"expiration_time\":\"未到期\",\"invest_count\":3,\"sygmfs\":0},{\"abbreviation\":\"ROULETTE\",\"expiration_time\":\"未到期\",\"invest_count\":3,\"sygmfs\":0},{\"abbreviation\":\"BACCARAT\",\"expiration_time\":\"未到期\",\"invest_count\":3,\"sygmfs\":0},{\"abbreviation\":\"Happy Weekend 2\",\"expiration_time\":\"未到期\",\"invest_count\":1,\"sygmfs\":0}]', 'Happy Weekend 3', 0, 0, 0, NULL, NULL, '2025-08-12 17:41:10', '2025-08-12 17:41:10');

-- ----------------------------
-- Table structure for pay_merchant
-- ----------------------------
DROP TABLE IF EXISTS `pay_merchant`;
CREATE TABLE `pay_merchant`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `merchant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户ID',
  `merchant_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户号',
  `merchant_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户名',
  `channel_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密钥',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `ds_free` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '代收费率%',
  `df_free` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '代付费率%',
  `one_free` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '单笔手续费',
  `houtai_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后台管理地址',
  `channel_type_ds` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代收-通道代码',
  `channel_type_df` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代付-通道代码',
  `degree_heat` int NULL DEFAULT 0 COMMENT '优先级',
  `status` int NULL DEFAULT 1 COMMENT '状态（0:下架,1:上架）',
  `create_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_merchant_id`(`merchant_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_merchant_no`(`merchant_no` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_date`(`create_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付商户配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_merchant
-- ----------------------------
INSERT INTO `pay_merchant` VALUES (1, '1881579249933836289', '431', 'csmpay', 'CQju4CA1UEC76ZA', '', 7.50, 3.00, 600.00, '', '', '', 0, 1, '2025-01-21 13:47:06', NULL);
INSERT INTO `pay_merchant` VALUES (2, '1904088754190761985', 'usdt', 'usdt', 'usdt', 'usdt', 5.00, 0.00, 0.00, 'usdt', 'usdt', 'usdt', 0, 1, '2025-03-24 14:01:50', '2025-03-24 14:03:47');
INSERT INTO `pay_merchant` VALUES (3, '1929771840485085185', '100669543', 'allpayds', '81f7a1a1dc75418fa6bd5db43fdf3fc7', '', 7.50, 3.00, 600.00, '', '122', '122', 0, 1, '2025-06-03 13:27:15', NULL);
INSERT INTO `pay_merchant` VALUES (4, '1933761948129419266', '91613824', 'sangepay', '6e097cddd7cdfe5a15d6bd79a18b4000', '', 7.00, 3.00, 600.00, '', '', '', 0, 1, '2025-06-14 13:42:31', NULL);
INSERT INTO `pay_merchant` VALUES (5, '1945370075832385537', 'K656', 'k2pay', 'ee01f4e191354dbfa1dabedf61dabfb9', '', 6.00, 3.00, 600.00, '', '', '', 0, 1, '2025-07-16 14:29:04', NULL);
INSERT INTO `pay_merchant` VALUES (6, '1948278686040588289', '611026286', 'pepay', 'BC925FC40DB58220718E6CCB74CDD3EA', '', 6.00, 3.00, 600.00, '', '', '', 0, 1, '2025-07-24 15:06:51', NULL);

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE,
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `FIRED_TIME` bigint NOT NULL,
  `SCHED_TIME` bigint NOT NULL,
  `PRIORITY` int NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME` ASC, `INSTANCE_NAME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME` ASC, `INSTANCE_NAME` ASC, `REQUESTS_RECOVERY` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_J_G`(`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_JG`(`SCHED_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_T_G`(`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_TG`(`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME` ASC, `REQUESTS_RECOVERY` ASC) USING BTREE,
  INDEX `IDX_QRTZ_J_GRP`(`SCHED_NAME` ASC, `JOB_GROUP` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('RenrenScheduler', 'STATE_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint NOT NULL,
  `CHECKIN_INTERVAL` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
INSERT INTO `qrtz_scheduler_state` VALUES ('RenrenScheduler', 'DESKTOP-JK3MJJQ1754580857231', 1754581068661, 15000);

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `REPEAT_COUNT` bigint NOT NULL,
  `REPEAT_INTERVAL` bigint NOT NULL,
  `TIMES_TRIGGERED` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `INT_PROP_1` int NULL DEFAULT NULL,
  `INT_PROP_2` int NULL DEFAULT NULL,
  `LONG_PROP_1` bigint NULL DEFAULT NULL,
  `LONG_PROP_2` bigint NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint NULL DEFAULT NULL,
  `PRIORITY` int NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `START_TIME` bigint NOT NULL,
  `END_TIME` bigint NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_J`(`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_JG`(`SCHED_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_C`(`SCHED_NAME` ASC, `CALENDAR_NAME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_G`(`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_STATE`(`SCHED_NAME` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_N_STATE`(`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME` ASC, `NEXT_FIRE_TIME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME` ASC, `TRIGGER_STATE` ASC, `NEXT_FIRE_TIME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for rebate_ratio_config
-- ----------------------------
DROP TABLE IF EXISTS `rebate_ratio_config`;
CREATE TABLE `rebate_ratio_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `areward` int NULL DEFAULT 0 COMMENT '1级返佣比例',
  `breward` int NULL DEFAULT 0 COMMENT '2级返佣比例',
  `creward` int NULL DEFAULT 0 COMMENT '3级返佣比例',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态：0-禁用，1-启用',
  `creator` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `updater` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '返佣比例配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rebate_ratio_config
-- ----------------------------
INSERT INTO `rebate_ratio_config` VALUES (1, 7, 3, 0, 0, NULL, NULL, '2023-02-04 20:30:04', NULL);

-- ----------------------------
-- Table structure for schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job`;
CREATE TABLE `schedule_job`  (
  `id` bigint NOT NULL COMMENT 'id',
  `bean_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'spring bean名称',
  `params` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数',
  `cron_expression` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `status` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '任务状态  0：暂停  1：正常',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_date`(`create_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule_job
-- ----------------------------

-- ----------------------------
-- Table structure for schedule_job_log
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job_log`;
CREATE TABLE `schedule_job_log`  (
  `id` bigint NOT NULL COMMENT 'id',
  `job_id` bigint NOT NULL COMMENT '任务id',
  `bean_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'spring bean名称',
  `params` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数',
  `status` tinyint UNSIGNED NOT NULL COMMENT '任务状态    0：失败    1：成功',
  `error` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失败信息',
  `times` int NOT NULL COMMENT '耗时(单位：毫秒)',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_job_id`(`job_id` ASC) USING BTREE,
  INDEX `idx_create_date`(`create_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint NOT NULL COMMENT 'id',
  `pid` bigint NULL DEFAULT NULL COMMENT '上级ID',
  `pids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所有上级ID，用逗号分开',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门名称',
  `sort` int UNSIGNED NULL DEFAULT NULL COMMENT '排序',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_pid`(`pid` ASC) USING BTREE,
  INDEX `idx_sort`(`sort` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1067246875800000062, 1067246875800000063, '1067246875800000066,1067246875800000063', '技术部', 2, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');
INSERT INTO `sys_dept` VALUES (1067246875800000063, 1067246875800000066, '1067246875800000066', '长沙分公司', 1, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');
INSERT INTO `sys_dept` VALUES (1067246875800000064, 1067246875800000066, '1067246875800000066', '上海分公司', 0, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');
INSERT INTO `sys_dept` VALUES (1067246875800000065, 1067246875800000064, '1067246875800000066,1067246875800000064', '市场部', 0, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');
INSERT INTO `sys_dept` VALUES (1067246875800000066, 0, '0', '人人开源集团', 0, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');
INSERT INTO `sys_dept` VALUES (1067246875800000067, 1067246875800000064, '1067246875800000066,1067246875800000064', '销售部', 0, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');
INSERT INTO `sys_dept` VALUES (1067246875800000068, 1067246875800000063, '1067246875800000066,1067246875800000063', '产品部', 1, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` bigint NOT NULL COMMENT 'id',
  `dict_type_id` bigint NOT NULL COMMENT '字典类型ID',
  `dict_label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典标签',
  `dict_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典值',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `sort` int UNSIGNED NULL DEFAULT NULL COMMENT '排序',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_dict_type_value`(`dict_type_id` ASC, `dict_value` ASC) USING BTREE,
  INDEX `idx_sort`(`sort` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1160061112075464705, 1160061077912858625, '男', '0', '', 0, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');
INSERT INTO `sys_dict_data` VALUES (1160061146967879681, 1160061077912858625, '女', '1', '', 1, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');
INSERT INTO `sys_dict_data` VALUES (1160061190127267841, 1160061077912858625, '保密', '2', '', 2, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');
INSERT INTO `sys_dict_data` VALUES (1225814069634195457, 1225813644059140097, '公告', '0', '', 0, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');
INSERT INTO `sys_dict_data` VALUES (1225814107559092225, 1225813644059140097, '会议', '1', '', 1, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');
INSERT INTO `sys_dict_data` VALUES (1225814271879340034, 1225813644059140097, '其他', '2', '', 2, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` bigint NOT NULL COMMENT 'id',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典类型',
  `dict_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `sort` int UNSIGNED NULL DEFAULT NULL COMMENT '排序',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1160061077912858625, 'gender', '性别', '', 0, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');
INSERT INTO `sys_dict_type` VALUES (1225813644059140097, 'notice_type', '站内通知-类型', '', 1, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');

-- ----------------------------
-- Table structure for sys_log_error
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_error`;
CREATE TABLE `sys_log_error`  (
  `id` bigint NOT NULL COMMENT 'id',
  `request_uri` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求URI',
  `request_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方式',
  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户代理',
  `ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作IP',
  `error_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '异常信息',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_date`(`create_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '异常日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log_error
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log_login
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_login`;
CREATE TABLE `sys_log_login`  (
  `id` bigint NOT NULL COMMENT 'id',
  `operation` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '用户操作   0：用户登录   1：用户退出',
  `status` tinyint UNSIGNED NOT NULL COMMENT '状态  0：失败    1：成功    2：账号已锁定',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户代理',
  `ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作IP',
  `creator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_date`(`create_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '登录日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log_login
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log_operation
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_operation`;
CREATE TABLE `sys_log_operation`  (
  `id` bigint NOT NULL COMMENT 'id',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户操作',
  `request_uri` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求URI',
  `request_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方式',
  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `request_time` int UNSIGNED NOT NULL COMMENT '请求时长(毫秒)',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户代理',
  `ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作IP',
  `status` tinyint UNSIGNED NOT NULL COMMENT '状态  0：失败   1：成功',
  `creator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_date`(`create_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log_operation
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL COMMENT 'id',
  `pid` bigint NULL DEFAULT NULL COMMENT '上级ID，一级菜单为0',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `permissions` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：sys:user:list,sys:user:save)',
  `menu_type` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '类型   0：菜单   1：按钮',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_pid`(`pid` ASC) USING BTREE,
  INDEX `idx_sort`(`sort` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1067246875800000002, 0, '权限管理', NULL, NULL, 0, 'icon-safetycertificate', 0, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000003, 1067246875800000055, '新增', NULL, 'sys:user:save,sys:dept:list,sys:role:list', 1, NULL, 1, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000004, 1067246875800000055, '修改', NULL, 'sys:user:update,sys:dept:list,sys:role:list', 1, NULL, 2, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000005, 1067246875800000055, '删除', NULL, 'sys:user:delete', 1, NULL, 3, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000006, 1067246875800000055, '导出', NULL, 'sys:user:export', 1, NULL, 4, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000007, 1067246875800000002, '角色管理', 'sys/role', NULL, 0, 'icon-team', 2, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000008, 1067246875800000007, '查看', NULL, 'sys:role:page,sys:role:info', 1, NULL, 0, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000009, 1067246875800000007, '新增', NULL, 'sys:role:save,sys:menu:select,sys:dept:list', 1, NULL, 1, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000010, 1067246875800000007, '修改', NULL, 'sys:role:update,sys:menu:select,sys:dept:list', 1, NULL, 2, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000011, 1067246875800000007, '删除', NULL, 'sys:role:delete', 1, NULL, 3, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000012, 1067246875800000002, '部门管理', 'sys/dept', NULL, 0, 'icon-apartment', 1, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000014, 1067246875800000012, '查看', NULL, 'sys:dept:list,sys:dept:info', 1, NULL, 0, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000015, 1067246875800000012, '新增', NULL, 'sys:dept:save', 1, NULL, 1, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000016, 1067246875800000012, '修改', NULL, 'sys:dept:update', 1, NULL, 2, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000017, 1067246875800000012, '删除', NULL, 'sys:dept:delete', 1, NULL, 3, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000025, 1067246875800000035, '菜单管理', 'sys/menu', NULL, 0, 'icon-unorderedlist', 0, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000026, 1067246875800000025, '查看', NULL, 'sys:menu:list,sys:menu:info', 1, NULL, 0, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000027, 1067246875800000025, '新增', NULL, 'sys:menu:save', 1, NULL, 1, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000028, 1067246875800000025, '修改', NULL, 'sys:menu:update', 1, NULL, 2, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000029, 1067246875800000025, '删除', NULL, 'sys:menu:delete', 1, NULL, 3, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000030, 1067246875800000035, '定时任务', 'job/schedule', NULL, 0, 'icon-dashboard', 3, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000031, 1067246875800000030, '查看', NULL, 'sys:schedule:page,sys:schedule:info', 1, NULL, 0, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000032, 1067246875800000030, '新增', NULL, 'sys:schedule:save', 1, NULL, 1, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000033, 1067246875800000030, '修改', NULL, 'sys:schedule:update', 1, NULL, 2, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000034, 1067246875800000030, '删除', NULL, 'sys:schedule:delete', 1, NULL, 3, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000035, 0, '系统设置', NULL, NULL, 0, 'icon-setting', 1, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000036, 1067246875800000030, '暂停', NULL, 'sys:schedule:pause', 1, NULL, 4, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000037, 1067246875800000030, '恢复', NULL, 'sys:schedule:resume', 1, NULL, 5, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000038, 1067246875800000030, '立即执行', NULL, 'sys:schedule:run', 1, NULL, 6, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000039, 1067246875800000030, '日志列表', NULL, 'sys:schedule:log', 1, NULL, 7, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000040, 1067246875800000035, '参数管理', 'sys/params', '', 0, 'icon-fileprotect', 1, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000041, 1067246875800000035, '字典管理', 'sys/dict-type', NULL, 0, 'icon-golden-fill', 2, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000042, 1067246875800000041, '查看', NULL, 'sys:dict:page,sys:dict:info', 1, NULL, 0, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000043, 1067246875800000041, '新增', NULL, 'sys:dict:save', 1, NULL, 1, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000044, 1067246875800000041, '修改', NULL, 'sys:dict:update', 1, NULL, 2, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000045, 1067246875800000041, '删除', NULL, 'sys:dict:delete', 1, NULL, 3, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000046, 0, '日志管理', NULL, NULL, 0, 'icon-container', 2, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000047, 1067246875800000035, '文件上传', 'oss/oss', 'sys:oss:all', 0, 'icon-upload', 4, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000048, 1067246875800000046, '登录日志', 'sys/log-login', 'sys:log:login', 0, 'icon-filedone', 0, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000049, 1067246875800000046, '操作日志', 'sys/log-operation', 'sys:log:operation', 0, 'icon-solution', 1, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000050, 1067246875800000046, '异常日志', 'sys/log-error', 'sys:log:error', 0, 'icon-file-exception', 2, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000051, 1067246875800000053, 'SQL监控', '{{ window.SITE_CONFIG[\"apiURL\"] }}/druid/sql.html', NULL, 0, 'icon-database', 0, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000053, 0, '系统监控', NULL, NULL, 0, 'icon-desktop', 3, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000055, 1067246875800000002, '用户管理', 'sys/user', NULL, 0, 'icon-user', 0, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000056, 1067246875800000055, '查看', NULL, 'sys:user:page,sys:user:info', 1, NULL, 0, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000057, 1067246875800000040, '新增', NULL, 'sys:params:save', 1, NULL, 1, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000058, 1067246875800000040, '导出', NULL, 'sys:params:export', 1, NULL, 4, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000059, 1067246875800000040, '查看', '', 'sys:params:page,sys:params:info', 1, NULL, 0, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000060, 1067246875800000040, '修改', NULL, 'sys:params:update', 1, NULL, 2, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1067246875800000061, 1067246875800000040, '删除', '', 'sys:params:delete', 1, '', 3, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11');
INSERT INTO `sys_menu` VALUES (1156748733921165314, 1067246875800000053, '接口文档', '{{ window.SITE_CONFIG[\"apiURL\"] }}/doc.html', '', 0, 'icon-file-word', 1, 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `id` bigint NOT NULL COMMENT 'id',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'URL地址',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_date`(`create_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件上传' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_params
-- ----------------------------
DROP TABLE IF EXISTS `sys_params`;
CREATE TABLE `sys_params`  (
  `id` bigint NOT NULL COMMENT 'id',
  `param_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数编码',
  `param_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数值',
  `param_type` tinyint UNSIGNED NULL DEFAULT 1 COMMENT '类型   0：系统参数   1：非系统参数',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_param_code`(`param_code` ASC) USING BTREE,
  INDEX `idx_create_date`(`create_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '参数管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_params
-- ----------------------------
INSERT INTO `sys_params` VALUES (1, 'apk', 'https://www.baidu.com', 1, '第三方 Games apk 地址', 1067246875800000001, '2025-06-03 14:04:11', 1067246875800000001, '2025-06-03 15:33:16');
INSERT INTO `sys_params` VALUES (2, 'salereply', 'Investment Assistant {} will be at your service, please be patient.', 1, '客服第一条回复', 1067246875800000001, '2025-05-19 14:37:14', 1067246875800000001, '2025-05-19 14:37:14');
INSERT INTO `sys_params` VALUES (3, 'pixels', '1106777267882557|1#1106777267882557|2', 1, '像素ID', 1067246875800000001, '2024-04-17 10:34:45', 1067246875800000001, '2024-04-17 10:34:45');
INSERT INTO `sys_params` VALUES (4, 'googlecodestate', 'close', 1, 'goog验证码开关 open/close', 1067246875800000001, '2024-11-20 18:47:49', 1067246875800000001, '2024-11-20 18:47:49');
INSERT INTO `sys_params` VALUES (5, 'usdtsysprice', '97', 1, 'USDT系统价格', 1067246875800000001, '2024-11-20 18:47:49', 1067246875800000001, '2024-11-20 18:47:49');
INSERT INTO `sys_params` VALUES (6, 'usdtrealprice', '86', 1, 'USDT实际价格', 1067246875800000001, '2024-11-20 18:47:49', 1067246875800000001, '2024-11-20 18:47:49');
INSERT INTO `sys_params` VALUES (7, 'groupbuystate', 'close', 1, '拼团开关 open/close', 1067246875800000001, '2024-10-14 18:47:49', 1067246875800000001, '2024-10-14 18:47:49');
INSERT INTO `sys_params` VALUES (1067246875800000073, 'CLOUD_STORAGE_CONFIG_KEY', '{\"type\":1,\"qiniuDomain\":\"http://test.oss.renren.io\",\"qiniuPrefix\":\"upload\",\"qiniuAccessKey\":\"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ\",\"qiniuSecretKey\":\"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV\",\"qiniuBucketName\":\"renren-oss\",\"aliyunDomain\":\"\",\"aliyunPrefix\":\"\",\"aliyunEndPoint\":\"\",\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qcloudBucketName\":\"\"}', 0, '云存储配置信息', 1067246875800000001, '2023-01-06 18:01:12', 1067246875800000001, '2023-01-06 18:01:12');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_data_scope
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_data_scope`;
CREATE TABLE `sys_role_data_scope`  (
  `id` bigint NOT NULL COMMENT 'id',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色数据权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_data_scope
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint NOT NULL COMMENT 'id',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint NULL DEFAULT NULL COMMENT '菜单ID',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `idx_menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user`  (
  `id` bigint NOT NULL COMMENT 'id',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色用户关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL COMMENT 'id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `head_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '性别   0：男   1：女    2：保密',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `super_admin` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '超级管理员   0：否   1：是',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态  0：停用   1：正常',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '渠道',
  `type` int NULL DEFAULT 0 COMMENT '类型',
  `agent` bigint NULL DEFAULT NULL COMMENT '代理ID',
  `domainname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '域名',
  `dowload_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下载码',
  `wsimage` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信图片',
  `wsnumber` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信号',
  `wsname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信名称',
  `tgnumber` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '推广号',
  `two_factor_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '二次验证码',
  `agent_invite_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代理邀请码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  INDEX `idx_create_date`(`create_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1748403717627, 'xiaolaohu', NULL, '小老虎', NULL, NULL, NULL, NULL, NULL, 0, 1, NULL, '2025-05-28 09:11:58', NULL, NULL, '5114157', 1, NULL, NULL, 'apkxiaolaohu', NULL, NULL, NULL, NULL, NULL, 'registerxiaolaohu');
INSERT INTO `sys_user` VALUES (1748403763980, 'Doris', '$2a$10$012Kx2ba5jzqr9gLlG4MX.bnQJTD9UWqF57XDo2N3.fPtLne02u/m', 'Doris', NULL, 0, NULL, NULL, NULL, 0, 1, NULL, '2025-05-28 09:12:44', NULL, NULL, '7114243', 2, 1748403717627, '', 'apkDoris', NULL, NULL, NULL, NULL, NULL, 'registerDoris');
INSERT INTO `sys_user` VALUES (1067246875800000001, 'admin', '$2a$10$012Kx2ba5jzqr9gLlG4MX.bnQJTD9UWqF57XDo2N3.fPtLne02u/m', '管理员', NULL, 0, 'root@renren.io', '13612345678', NULL, 1, 1, 1067246875800000001, '2023-01-06 18:01:11', 1067246875800000001, '2023-01-06 18:01:11', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`  (
  `id` bigint NOT NULL COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户token',
  `expire_date` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE,
  UNIQUE INDEX `token`(`token` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户Token' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------

-- ----------------------------
-- Table structure for tb_bank
-- ----------------------------
DROP TABLE IF EXISTS `tb_bank`;
CREATE TABLE `tb_bank`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `blank_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '银行简称',
  `blank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '银行名称',
  `channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '渠道',
  `currency` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'INR' COMMENT '货币',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `state` tinyint NULL DEFAULT 1 COMMENT '状态 0：停用 1：正常',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `state_time` datetime NULL DEFAULT NULL COMMENT '状态时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_blank_code`(`blank_code` ASC) USING BTREE,
  INDEX `idx_state`(`state` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1714971938352 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '银行管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_bank
-- ----------------------------
INSERT INTO `tb_bank` VALUES (1714970293591, 'State Bank of India', 'State Bank of India', NULL, 'RS', '', 1, '2024-05-06 12:38:14', '2024-05-06 12:38:14');
INSERT INTO `tb_bank` VALUES (1714970310220, 'ICICI Bank', 'ICICI Bank', NULL, 'RS', '', 1, '2024-05-06 12:38:30', '2024-05-06 12:38:30');
INSERT INTO `tb_bank` VALUES (1714970328235, 'Punjab National Bank', 'Punjab National Bank', NULL, 'RS', '', 1, '2024-05-06 12:38:48', '2024-05-06 12:38:48');
INSERT INTO `tb_bank` VALUES (1714970385481, 'Bank of India', 'Bank of India', NULL, 'RS', '', 1, '2024-05-06 12:39:45', '2024-05-06 12:39:45');
INSERT INTO `tb_bank` VALUES (1714970422291, 'Bank of Baroda', 'Bank of Baroda', NULL, 'RS', '', 1, '2024-05-06 12:40:22', '2024-05-06 12:40:22');
INSERT INTO `tb_bank` VALUES (1714970473710, 'HDFC Bank', 'HDFC Bank', NULL, 'RS', '', 1, '2024-05-06 12:41:14', '2024-05-06 12:41:14');
INSERT INTO `tb_bank` VALUES (1714970514045, 'Canara Bank', 'Canara Bank', NULL, 'RS', '', 1, '2024-05-06 12:41:54', '2024-05-06 12:41:54');
INSERT INTO `tb_bank` VALUES (1714970534529, 'Union Bank of India', 'Union Bank of India', NULL, 'RS', '', 1, '2024-05-06 12:42:15', '2024-05-06 12:42:15');
INSERT INTO `tb_bank` VALUES (1714970549228, 'Axis Bank', 'Axis Bank', NULL, 'RS', '', 1, '2024-05-06 12:42:29', '2024-05-06 12:42:29');
INSERT INTO `tb_bank` VALUES (1714970576673, 'Kotak Mahindra Bank', 'Kotak Mahindra Bank', NULL, 'RS', '', 1, '2024-05-06 12:42:57', '2024-05-06 12:42:57');
INSERT INTO `tb_bank` VALUES (1714970606758, 'PAYTM', 'PAYTM', NULL, 'RS', '', 1, '2024-05-06 12:43:27', '2024-05-06 12:43:27');
INSERT INTO `tb_bank` VALUES (1714971044589, 'IndusInd Bank', 'IndusInd Bank', NULL, 'RS', '', 1, '2024-05-06 12:50:45', '2024-05-06 12:50:45');
INSERT INTO `tb_bank` VALUES (1714971194070, 'IDFC Bank', 'IDFC Bank', NULL, 'RS', '', 1, '2024-05-06 12:53:14', '2024-05-06 12:53:14');
INSERT INTO `tb_bank` VALUES (1714971801771, 'Indian Overseas Bank', 'Indian Overseas Bank', NULL, 'RS', '', 1, '2024-05-06 13:03:22', '2024-05-06 13:03:22');
INSERT INTO `tb_bank` VALUES (1714971938351, 'RBL BANK', 'RBL BANK', NULL, 'RS', '--', 1, '2024-05-06 13:05:38', '2024-05-06 13:05:38');

-- ----------------------------
-- Table structure for tb_coupon
-- ----------------------------
DROP TABLE IF EXISTS `tb_coupon`;
CREATE TABLE `tb_coupon`  (
  `coupon_id` bigint NOT NULL,
  `coupon_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '优惠券名称',
  `coupon_describe` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '优惠券描述',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `open_date` date NULL DEFAULT NULL COMMENT '开启时间',
  `end_date` date NULL DEFAULT NULL COMMENT '结束时间',
  `update_date` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `sys_update_user_id` bigint NULL DEFAULT NULL COMMENT '修改人',
  `sys_create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人id',
  `amount` bigint NULL DEFAULT NULL COMMENT '优惠券使用金额单位分',
  `invest_id` bigint NOT NULL COMMENT '投资项目id',
  PRIMARY KEY (`coupon_id`) USING BTREE,
  INDEX `优惠券关联项目`(`invest_id` ASC) USING BTREE,
  CONSTRAINT `优惠券关联项目` FOREIGN KEY (`invest_id`) REFERENCES `tb_invest_project` (`invest_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_coupon
-- ----------------------------
INSERT INTO `tb_coupon` VALUES (1, 'xxx理财投资产品优惠券', '描述描述描述描述描述描述描述描述描述描述描述描述', '2023-01-11 23:07:53', '2023-01-11', '2023-02-05', '2023-01-12 00:37:59', 1, 1, 5000, 1611305052584599554);
INSERT INTO `tb_coupon` VALUES (2, 'xxx流彩李新宇看了几个防控论文集', '平稳见公婆而进入该管家婆二级各个人陪乳杆菌陪人几个人屁股', '2023-01-11 23:10:18', '2023-01-11', '2023-01-24', '2023-01-12 00:37:56', 1, 1, 10000, 1611305052584599555);
INSERT INTO `tb_coupon` VALUES (3, 'xxxxx这是一个过期的优惠券', '优惠券过期了过期了过期了', '2022-12-30 23:22:12', '2022-12-31', '2023-01-10', '2023-01-12 00:37:54', 1, 1, 20000, 1611305052584599556);

-- ----------------------------
-- Table structure for tb_invest_project
-- ----------------------------
DROP TABLE IF EXISTS `tb_invest_project`;
CREATE TABLE `tb_invest_project`  (
  `invest_id` bigint NOT NULL,
  `project_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '项目类型（1:固定金额投资,2:进度周期投资）',
  `cycle_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '周期类型（1:每天返还,2:到期返还）',
  `invest_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目名称',
  `scale_amount` bigint NOT NULL DEFAULT 0 COMMENT '项目规模金额(分为单位)',
  `conversion` int NOT NULL DEFAULT 0 COMMENT '项目转化',
  `cycle` int NOT NULL DEFAULT 1 COMMENT '项目周期',
  `repayment_mode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '还款方式',
  `mechanism` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '担保机构',
  `progress_amount` bigint NOT NULL COMMENT '进度金额，如果project_type等于1 无需理会',
  `project_describe` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '项目描述',
  `project_explain` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '项目说明',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态  0：停用   1：正常',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `sys_update_user_id` bigint NULL DEFAULT NULL COMMENT '修改人',
  `sys_create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人id',
  `type_id` bigint NULL DEFAULT NULL COMMENT '项目分类id',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目图片',
  `copies_type` int NULL DEFAULT 0 COMMENT '是否可购买多分 0不可购买 1可购买',
  PRIMARY KEY (`invest_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_invest_project
-- ----------------------------
INSERT INTO `tb_invest_project` VALUES (1611305052584599554, 1, 1, '医疗器材啥啥的', 200000000, 2, 3, '按天付收益，到期还本金', '某某大帅哥担保有限公司', 0, '结算时间：今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的', '结算时间：今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的', 1, '2023-01-11 21:16:17', '2023-01-11 21:41:31', 1, 1, 1611305052584599554, 'https://up.enterdesk.com/edpic_source/b0/d1/f3/b0d1f35504e4106d48c84434f2298ada.jpg', 0);
INSERT INTO `tb_invest_project` VALUES (1611305052584599555, 2, 1, '健生器材啥啥的', 3000000, 5, 10, '按天付收益，到期还本金', '某某某担保有限公司', 30000, '结算时间：今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的', '结算时间：今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的', 1, '2023-01-11 21:16:23', '2023-01-11 21:42:54', 1, 1, 1611305052584599555, 'https://up.enterdesk.com/edpic/75/dc/50/75dc50577d3d3d2bd5fd8db728e7bf77.jpg', 0);
INSERT INTO `tb_invest_project` VALUES (1611305052584599556, 2, 2, '农家猪脚饭投资', 300000000, 20, 30, '按天付收益，到期还本金', '大帅哥担保公司', 5000000, '结算时间：今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的', '结算时间：今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的', 1, '2023-01-11 21:16:23', '2023-01-11 21:43:13', 1, 1, 1611305052584599556, 'https://img1.baidu.com/it/u=3323688473,1779373562&fm=253&fmt=auto?w=500&h=281', 0);
INSERT INTO `tb_invest_project` VALUES (1611305052584599557, 1, 2, '哥的魔力投资', 500000, 10, 20, '按天付收益，到期还本金', '猪脚饭担保', 0, '结算时间：今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的', '结算时间：今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的今天天气很清凉是啥啥啥啥的', 1, '2023-01-11 21:16:23', '2023-01-11 21:43:15', 1, 1, 1611305052584599557, 'https://up.enterdesk.com/edpic_source/be/c9/63/bec9639dd358ab674ed3735da64d78c7.jpg', 0);

-- ----------------------------
-- Table structure for tb_invest_project_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_invest_project_type`;
CREATE TABLE `tb_invest_project_type`  (
  `type_id` bigint NOT NULL,
  `type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态  0：停用   1：正常',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `sys_update_user_id` bigint NULL DEFAULT NULL COMMENT '修改人',
  `sys_create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人id',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_invest_project_type
-- ----------------------------
INSERT INTO `tb_invest_project_type` VALUES (1611305052584599554, '新手专区', 1, '2023-01-11 21:31:24', '2023-01-11 22:46:26', 1, 1, 1);
INSERT INTO `tb_invest_project_type` VALUES (1611305052584599555, '精选专区', 1, '2023-01-11 21:31:47', '2023-01-11 22:39:50', 1, 1, 2);
INSERT INTO `tb_invest_project_type` VALUES (1611305052584599556, 'VIP专区', 1, '2023-01-11 21:32:07', '2023-01-11 22:39:52', 1, 1, 4);
INSERT INTO `tb_invest_project_type` VALUES (1611305052584599557, '福利专区', 1, '2023-01-11 21:32:29', '2023-01-11 22:39:52', 1, 1, 3);

-- ----------------------------
-- Table structure for tb_investment_profit_detail
-- ----------------------------
DROP TABLE IF EXISTS `tb_investment_profit_detail`;
CREATE TABLE `tb_investment_profit_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `investment_id` bigint NOT NULL COMMENT '投资记录ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `project_id` bigint NOT NULL COMMENT '项目ID',
  `profit_type` tinyint NULL DEFAULT 1 COMMENT '收益类型 1:利息 2:本金 3:其他',
  `profit_amount` bigint NOT NULL COMMENT '收益金额(分)',
  `profit_date` datetime NOT NULL COMMENT '收益日期',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态 0:未到账 1:已到账',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_investment_id`(`investment_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_project_id`(`project_id` ASC) USING BTREE,
  INDEX `idx_profit_date`(`profit_date` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '投资收益明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_investment_profit_detail
-- ----------------------------

-- ----------------------------
-- Table structure for tb_investment_record
-- ----------------------------
DROP TABLE IF EXISTS `tb_investment_record`;
CREATE TABLE `tb_investment_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `project_id` bigint NOT NULL COMMENT '项目ID',
  `investment_amount` bigint NOT NULL COMMENT '投资金额(分)',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_abbr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单号简称',
  `order_date` datetime NOT NULL COMMENT '投资日期',
  `profit_amount` bigint NULL DEFAULT 0 COMMENT '收益金额(分)',
  `profit_date` datetime NULL DEFAULT NULL COMMENT '收益日期',
  `profit_interest` bigint NULL DEFAULT 0 COMMENT '收益利息(分)',
  `profit_principal` bigint NULL DEFAULT 0 COMMENT '收益本金(分)',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态 0:未收益 1:已收益',
  `cycle` int NULL DEFAULT NULL COMMENT '项目周期(天)',
  `cycle_type` tinyint NULL DEFAULT 1 COMMENT '周期类型 1:到期收益含本金 2:每日返本金到期收益',
  `ddsy` bigint NULL DEFAULT 0 COMMENT '等待收益(分)',
  `invest_count` int NULL DEFAULT 1 COMMENT '投资次数',
  `rush_minute` int NULL DEFAULT 0 COMMENT '抢购分钟数',
  `agent` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代理',
  `salesmanid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '销售员ID',
  `create_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_project_id`(`project_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_order_date`(`order_date` ASC) USING BTREE,
  INDEX `idx_user_project`(`user_id` ASC, `project_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户投资记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_investment_record
-- ----------------------------

-- ----------------------------
-- Table structure for tb_pay_channel
-- ----------------------------
DROP TABLE IF EXISTS `tb_pay_channel`;
CREATE TABLE `tb_pay_channel`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `channel_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '渠道ID',
  `channel_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '渠道名称',
  `channel_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '渠道类型',
  `merchantid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户ID',
  `merchant_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户名称',
  `status` int NULL DEFAULT 0 COMMENT '状态（0:禁用,1:启用）',
  `usdt_gift_ratio` decimal(15, 0) NULL DEFAULT NULL COMMENT 'usdt赠送比例',
  `usdt_local_currency_rate` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'usdt兑当地货币汇率	',
  `chargeorwithdraw` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '充提类型（1:充值,2:提现,3:充提）',
  `create_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_channel_id`(`channel_id` ASC) USING BTREE,
  INDEX `idx_merchant_id`(`merchantid` ASC) USING BTREE,
  INDEX `idx_channel_type`(`channel_type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付渠道表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_pay_channel
-- ----------------------------
INSERT INTO `tb_pay_channel` VALUES (2, '1787712160752496642', 'PAY 1', 'UPI', '1881579249933836289', 'csmpay', 1, NULL, NULL, '1', '2025-08-12 16:56:08', '2025-08-12 16:56:08');
INSERT INTO `tb_pay_channel` VALUES (3, '1787712247524257793', 'PAY 2', 'UPI', '1929771840485085185', 'allpayds', 1, NULL, NULL, '1', '2025-08-12 16:56:08', '2025-08-12 16:56:08');
INSERT INTO `tb_pay_channel` VALUES (4, '1790271140901277697', 'PAY 3', 'UPI', '1933761948129419266', 'sangepay', 1, NULL, NULL, '1', '2025-08-12 16:56:08', '2025-08-12 16:56:08');
INSERT INTO `tb_pay_channel` VALUES (5, '1869296896939253761', 'PAY 4', 'UPI', '1948278686040588289', 'pepay', 1, NULL, NULL, '1', '2025-08-12 16:56:08', '2025-08-12 16:56:08');
INSERT INTO `tb_pay_channel` VALUES (6, '1904083922675515393', 'USDT', 'USDT', '1904088754190761985', 'usdt', 1, NULL, NULL, '1', '2025-08-12 16:56:08', '2025-08-12 16:56:08');

-- ----------------------------
-- Table structure for tb_pay_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_pay_info`;
CREATE TABLE `tb_pay_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `blank_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '银行简称',
  `blank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '银行名称',
  `pay_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收款人姓名',
  `pay_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '银行账号',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收款人手机号码',
  `ifsc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'ifsc',
  `state` tinyint NULL DEFAULT 1 COMMENT '状态 0：停用 1：正常',
  `sort_v` int NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `oper_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作工号',
  `state_time` datetime NULL DEFAULT NULL COMMENT '状态时间',
  `channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '渠道',
  `agent` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代理',
  `salesmanid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务员ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户支付信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_pay_info
-- ----------------------------
INSERT INTO `tb_pay_info` VALUES (1, 1, 'ICICI', 'ICICI Bank', '张三', '1234567890123456', '13800138000', 'ICIC0001234', 1, 1, '2025-08-08 02:05:37', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_pay_info` VALUES (2, 1, 'HDFC', 'HDFC Bank', '李四', '9876543210987654', '13900139000', 'HDFC0005678', 1, 2, '2025-08-08 02:05:37', NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for tb_profit
-- ----------------------------
DROP TABLE IF EXISTS `tb_profit`;
CREATE TABLE `tb_profit`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `invest_id` bigint NOT NULL COMMENT '投资项目ID',
  `invest_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目名称',
  `investment_amount` bigint NOT NULL COMMENT '投资金额(分)',
  `profit_amount` bigint NULL DEFAULT 0 COMMENT '收益金额(分)',
  `profit_interest` bigint NULL DEFAULT 0 COMMENT '收益利息(分)',
  `profit_principal` bigint NULL DEFAULT 0 COMMENT '收益本金(分)',
  `ddsy` bigint NULL DEFAULT 0 COMMENT '等待收益(分)',
  `cycle` int NOT NULL COMMENT '项目周期(天)',
  `cycle_type` tinyint NOT NULL DEFAULT 1 COMMENT '周期类型（1:到期收益含本金,2:每日返本金到期收益）',
  `order_date` datetime NOT NULL COMMENT '投资日期',
  `profit_date` datetime NULL DEFAULT NULL COMMENT '收益日期',
  `order_id` bigint NOT NULL COMMENT '下单ID',
  `order_abbr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单号简称',
  `invest_count` int NULL DEFAULT 1 COMMENT '投资总数',
  `img` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态 0：未收益 1：已收益',
  `agent` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代理ID',
  `salesmanid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务员ID',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `rush_minute` int NULL DEFAULT NULL COMMENT '抢购分钟',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_invest_id`(`invest_id` ASC) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_order_date`(`order_date` ASC) USING BTREE,
  INDEX `idx_profit_date`(`profit_date` ASC) USING BTREE,
  INDEX `idx_agent`(`agent` ASC) USING BTREE,
  INDEX `idx_salesmanid`(`salesmanid` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '付息还本表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_profit
-- ----------------------------
INSERT INTO `tb_profit` VALUES (1, 1, 1001, 'ANDAR BAHAR项目', 100000, 1500, 1500, 0, 5000, 30, 2, '2025-01-20 10:00:00', '2025-01-27 10:00:00', 10001, 'AB001', 1, 'project1.jpg', 1, 'agent001', 'sales001', '13800138000', 30, '2025-08-08 03:22:50', '2025-08-08 03:22:50');
INSERT INTO `tb_profit` VALUES (2, 1, 1002, 'Teen Patti项目', 200000, 3000, 3000, 0, 8000, 45, 1, '2025-01-15 14:30:00', '2025-01-25 14:30:00', 10002, 'TP001', 2, 'project2.jpg', 1, 'agent001', 'sales001', '13800138000', 45, '2025-08-08 03:22:50', '2025-08-08 03:22:50');
INSERT INTO `tb_profit` VALUES (3, 1, 1003, 'Rummy项目', 150000, 0, 0, 0, 3000, 60, 2, '2025-01-10 09:15:00', NULL, 10003, 'RM001', 1, 'project3.jpg', 0, 'agent001', 'sales001', '13800138000', 60, '2025-08-08 03:22:50', '2025-08-08 03:22:50');

-- ----------------------------
-- Table structure for tb_project
-- ----------------------------
DROP TABLE IF EXISTS `tb_project`;
CREATE TABLE `tb_project`  (
  `invest_id` bigint NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `invest_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目名称',
  `abbreviation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目简称',
  `status` int NULL DEFAULT 1 COMMENT '项目状态（0:下架,1:上架,2:删除）',
  `invest_repeat` int NULL DEFAULT 1 COMMENT '可买台数',
  `project_type` int NULL DEFAULT 0 COMMENT '项目类型（0:默认类型,1:固定金额投资,2:众筹）',
  `cycle_type` int NULL DEFAULT NULL COMMENT '回款方式（1:到期返还,2:每日返利,4:其他）',
  `scale_amount` bigint NULL DEFAULT NULL COMMENT '项目规模金额',
  `cycle` int NULL DEFAULT NULL COMMENT '投资周期(天)',
  `conversion` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日收益率(%)',
  `principal_profit` bigint NULL DEFAULT NULL COMMENT '每日收益',
  `total_profit` bigint NULL DEFAULT NULL COMMENT '总收益',
  `total_cost` bigint NULL DEFAULT NULL COMMENT '总成本(本金+收益)',
  `type_id` bigint NULL DEFAULT NULL COMMENT '分类ID',
  `img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目图片地址',
  `coupon_id` bigint NULL DEFAULT NULL COMMENT '优惠券ID',
  `coupon_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `project_describe` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '项目描述',
  `sort` int NULL DEFAULT 0 COMMENT '排序权重',
  `vip` int NULL DEFAULT 0 COMMENT 'VIP等级要求',
  `hour` int NULL DEFAULT 0 COMMENT '限时抢购小时数',
  `expire` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '抢购结束时间',
  `sj_date` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '指定上架日期',
  `xj_date` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '指定下架日期',
  `member_registration_time` int NULL DEFAULT 1 COMMENT '会员注册时间要求（全部会员可购、特定会员可购）',
  `register_start_date` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注册开始时间',
  `register_end_date` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注册结束时间',
  `return_principal` int NULL DEFAULT -1 COMMENT '是否立返本金（-1:否,0:否,1:是）',
  `return_to` int NULL DEFAULT -1 COMMENT '返还到谁',
  `return_ratio` decimal(10, 4) NULL DEFAULT NULL COMMENT '返还比例',
  `return_project` int NULL DEFAULT -1 COMMENT '是否立返项目',
  `return_to_sup` int NULL DEFAULT -1 COMMENT '返还给上级',
  `return_ratio_sup` decimal(10, 4) NULL DEFAULT NULL COMMENT '上级返还比例',
  `return_project_to` int NULL DEFAULT -1 COMMENT '项目返还给谁',
  `return_project_to_sup` int NULL DEFAULT -1 COMMENT '项目返还给上级',
  `return_invest` bigint NULL DEFAULT NULL COMMENT '返还投资项目ID',
  `return_invest_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '返还投资项目名称',
  `ret` int NULL DEFAULT -1 COMMENT '其他返还设置',
  `market_date` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '二级市场开放时间',
  `rush_week` int NULL DEFAULT NULL COMMENT '一级市场开放周几',
  `rush_hour` int NULL DEFAULT NULL COMMENT '一级市场开放时间(小时)',
  `rush_minute` int NULL DEFAULT NULL COMMENT '一级市场可抢购时间(分钟)',
  `dis_flag` int NULL DEFAULT NULL COMMENT '折扣标志',
  `discount` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '折扣信息',
  `discount_start` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '折扣开始时间',
  `discount_end` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '折扣结束时间',
  `discount_num` int NULL DEFAULT NULL COMMENT '折扣数量',
  `discount_vip` int NULL DEFAULT NULL COMMENT '折扣VIP等级',
  `join_num` int NULL DEFAULT 0 COMMENT '参与人数',
  `group_leader_rebate` int NULL DEFAULT NULL COMMENT '团长返利',
  `group_member_rebate` int NULL DEFAULT NULL COMMENT '团员返利',
  `group_time` int NULL DEFAULT NULL COMMENT '团购时间',
  `rushbuy_falg` int NULL DEFAULT NULL COMMENT '是否限时抢购（0:否,1:是）',
  `rushbuy_start` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '限时抢购开始时间',
  `rushbuy_end` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '限时抢购结束时间',
  `rushbuy_num` int NULL DEFAULT NULL COMMENT '限时可抢份数',
  `project_explain` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '项目说明',
  `create_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`invest_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1754643567800 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '投资项目表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_project
-- ----------------------------
INSERT INTO `tb_project` VALUES (1748669931581, 'ANDAR BAHAR', 'ANDAR BAHAR', 1, 3, 0, 2, 100000, 180, '1.5', 1500, 270000, 370000, 1677040832376, 'http://8.216.132.154/images/tools/e7dce898-5012-4289-b00e-d845b68b3b7a.png', NULL, NULL, '\n\n<p>1.You can withdraw your daily profits.</p>\n\n<p>2. The principal will be returned upon maturity of the investment.</p>', 1, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, 1748669931581, 'ANDAR BAHAR', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1748670182280, 'CRASH', 'CRASH', 1, 3, 0, 2, 240000, 180, '1.55', 3720, 669600, 909600, 1677040832376, 'http://8.216.132.154/images/tools/22f5166e-ede8-47ce-9c64-9f73b5c4365c.png', NULL, NULL, '', 2, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, 1748670182280, 'CRASH', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1748670322219, 'JHNDI MUNDA', 'JHNDI MUNDA', 1, 3, 0, 2, 500000, 210, '1.6', 8000, 1680000, 2180000, 1677040832376, 'http://8.216.132.154/images/tools/3bf7cdeb-1aba-4206-a130-bd9c8c90b28d.png', NULL, NULL, '', 3, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1748670437603, 'HORSE RACING', 'HORSE RACING', 1, 3, 0, 2, 1200000, 210, '1.65', 19800, 4158000, 5358000, 1677040832376, 'http://8.216.132.154/images/tools/9c2b2781-a91a-4165-85f9-e7c4f21c97f9.png', NULL, NULL, '', 4, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1748670560038, 'Wingo Lottery', 'Wingo Lottery', 1, 3, 0, 2, 2500000, 240, '1.7', 42500, 10200000, 12700000, 1677040832376, 'http://8.216.132.154/images/tools/ef87343e-2cee-4ecd-b1b2-1bc3e707736d.png', NULL, NULL, '', 5, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1748670738747, 'Fortune Wheel', 'Fortune Wheel', 1, 3, 0, 2, 5200000, 240, '1.75', 91000, 21840000, 27040000, 1677040832376, 'http://8.216.132.154/images/tools/0d0e5e98-816e-43c7-b55b-bcc309d7bea0.png', NULL, NULL, '', 6, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1748670837639, 'BIG BATTLE', 'BIG BATTLE', 1, 3, 0, 2, 12000000, 270, '1.8', 216000, 58320000, 70320000, 1677040832376, 'http://8.216.132.154/images/tools/4d4477fd-1cd2-4db4-8d85-8890f322d0fd.png', NULL, NULL, '', 7, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1748670979855, 'ROULETTE', 'ROULETTE', 1, 3, 0, 2, 25000000, 270, '1.9', 475000, 128250000, 153250000, 1677040832376, 'http://8.216.132.154/images/tools/d6cdc262-9b88-4af4-8d3a-32a10acfc7ce.png', NULL, NULL, '', 8, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1748671064038, 'BACCARAT', 'BACCARAT', 1, 3, 0, 2, 39000000, 300, '2.0', 780000, 234000000, 273000000, 1677040832376, 'http://8.216.132.154/images/tools/1eb4997e-8ead-4361-89cc-42c3570556e9.png', NULL, NULL, '', 9, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1748672015397, '7 UP DOWN', '7 UP DOWN', 1, 3, 0, 4, 100000, 360, '3.0', 3000, 1080000, 1180000, 1684386752251, 'http://8.216.132.154/images/tools/f3bc8df7-cab7-4490-bc9e-1f303c24a95b.png', NULL, NULL, '', 1, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1748672203949, 'Aviator', 'Aviator', 1, 3, 0, 4, 240000, 360, '3.1', 7440, 2678400, 2918400, 1684386752251, 'http://8.216.132.154/images/tools/6bef1cbb-ea02-4039-aedc-f9907405ff59.png', NULL, NULL, '', 2, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1748672311184, 'Aviatrix', 'Aviatrix', 1, 3, 0, 4, 500000, 360, '3.2', 16000, 5760000, 6260000, 1684386752251, 'http://8.216.132.154/images/tools/e16beae2-8dfe-4587-a09e-f0219a566a3f.png', NULL, NULL, '', 3, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1748672386578, 'CRASHX', 'CRASHX', 1, 3, 0, 4, 1200000, 360, '3.3', 39600, 14256000, 15456000, 1684386752251, 'http://8.216.132.154/images/tools/2d0cfad3-f071-4fce-96be-b7985e59af5a.png', NULL, NULL, '', 4, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1748672483338, 'Jetx', 'Jetx', 1, 3, 0, 4, 2500000, 360, '3.4', 85000, 30600000, 33100000, 1684386752251, 'http://8.216.132.154/images/tools/4ed84c23-a406-4ad0-b6b2-d028cac21c83.png', NULL, NULL, '', 5, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1748672574237, 'ZEPPELIN', 'ZEPPELIN', 1, 3, 0, 4, 5200000, 360, '3.5', 182000, 65520000, 70720000, 1684386752251, 'http://8.216.132.154/images/tools/9490c0d2-3d78-4aeb-9c87-03dbe02322f8.png', NULL, NULL, '', 6, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1752072416084, 'Happy Weekend', 'Happy Weekend', 0, 3, 0, 2, 200000, 15, '2', 4000, 60000, 260000, 1677040832376, 'http://8.216.132.154/images/tools/b7355c9c-dff8-49b6-9fe7-ac3576d5e8cb.png', NULL, NULL, '', 1, 0, 2730, '2025-07-11 20:19:00', NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, 1752072416084, 'Happy Weekend', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1752821238754, 'Happy Weekend', 'Happy Weekend 2', 1, 1, 0, 2, 200000, 15, '2', 4000, 60000, 260000, 1677040832376, 'http://8.216.132.154/images/tools/610bcad6-1eb7-4293-af18-3fdcbd129fcb.png', NULL, NULL, '', -10000, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, 1752821238754, 'Happy Weekend', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1752821340304, 'Happy Weekend', 'Happy Weekend 3', 1, 1, 0, 2, 200000, 15, '2', 4000, 60000, 260000, 1677040832376, 'http://8.216.132.154/images/tools/eac96402-72b9-4820-8dee-dfeb3f9a45ab.png', NULL, NULL, '', 0, 0, 0, NULL, NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, 1752821340304, 'Happy Weekend', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');
INSERT INTO `tb_project` VALUES (1754643303380, 'Happy Weekend B', 'Happy Weekend B1', 0, 1, 0, 4, 200000, 360, '5', 10000, 3600000, 3800000, 1677040832376, 'http://8.216.132.154/images/tools/bf37bbcb-32fb-4b4b-add1-2d9a646811f5.png', NULL, NULL, '\n\n<p><br></p>', -1000, 0, 2754, '2025-08-10 14:50:00', NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:03:13', '2025-08-12 16:03:13');
INSERT INTO `tb_project` VALUES (1754643567799, 'Happy Weekend A', 'Happy Weekend A1', 0, 1, 0, 2, 200000, 15, '2', 4000, 60000, 260000, 1677040832376, 'http://8.216.132.154/images/tools/0c0ecbc0-65b6-4fe3-9b90-257603af7689.png', NULL, NULL, '', -100, 0, 2870, '2025-08-10 16:50:00', NULL, NULL, 1, NULL, NULL, -1, -1, NULL, -1, -1, NULL, -1, -1, NULL, '商品已被删除或不是返还设备类型', -1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-12 16:08:09', '2025-08-12 16:08:09');

-- ----------------------------
-- Table structure for tb_project_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_project_type`;
CREATE TABLE `tb_project_type`  (
  `type_id` bigint NOT NULL AUTO_INCREMENT COMMENT '项目分类id',
  `type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `status` int NULL DEFAULT 1 COMMENT '状态 0=禁用 1=启用',
  `create_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `sys_create_user_id` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `sys_update_user_id` bigint NULL DEFAULT NULL COMMENT '更新者ID',
  PRIMARY KEY (`type_id`) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_sort`(`sort` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1677040832381 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '投资项目分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_project_type
-- ----------------------------
INSERT INTO `tb_project_type` VALUES (1677040832376, 'General', 1, 1, '2023-02-22 12:40:32', '2025-08-12 15:01:23', NULL, NULL);
INSERT INTO `tb_project_type` VALUES (1677040832377, 'Compound', 1, 1, '2025-08-08 01:27:12', '2025-08-12 15:01:27', NULL, NULL);

-- ----------------------------
-- Table structure for tb_red_packet
-- ----------------------------
DROP TABLE IF EXISTS `tb_red_packet`;
CREATE TABLE `tb_red_packet`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '红包ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '红包名称',
  `total_amount` bigint NOT NULL COMMENT '总金额(分)',
  `number` int NOT NULL COMMENT '红包个数',
  `amount` bigint NOT NULL COMMENT '单个红包金额(分)',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '口令码',
  `collated` int NULL DEFAULT 0 COMMENT '已领取红包数',
  `create_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `locking` tinyint NULL DEFAULT 0 COMMENT '锁定状态 0:未锁定 1:已锁定',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态 0:正常 1:已过期 2:已领完',
  `validity_period` int NOT NULL COMMENT '有效期(分钟)',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `creator_id` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `creator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者姓名',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_creator_id`(`creator_id` ASC) USING BTREE,
  INDEX `idx_create_date`(`create_date` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_password`(`password` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '红包主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_red_packet
-- ----------------------------

-- ----------------------------
-- Table structure for tb_red_packet_record
-- ----------------------------
DROP TABLE IF EXISTS `tb_red_packet_record`;
CREATE TABLE `tb_red_packet_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `red_packet_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '红包ID',
  `user_id` bigint NOT NULL COMMENT '领取用户ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '领取用户姓名',
  `amount` bigint NOT NULL COMMENT '领取金额(分)',
  `receive_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `device_info` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备信息',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 1:正常 0:异常',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_red_packet_user`(`red_packet_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_red_packet_id`(`red_packet_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_receive_time`(`receive_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '红包领取记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_red_packet_record
-- ----------------------------

-- ----------------------------
-- Table structure for tb_sign_reward_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_sign_reward_config`;
CREATE TABLE `tb_sign_reward_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `oneDay` bigint NULL DEFAULT 100 COMMENT '第1天奖励金额(分)',
  `twoDay` bigint NULL DEFAULT 200 COMMENT '第2天奖励金额(分)',
  `threeDay` bigint NULL DEFAULT 300 COMMENT '第3天奖励金额(分)',
  `fourDay` bigint NULL DEFAULT 400 COMMENT '第4天奖励金额(分)',
  `fiveDay` bigint NULL DEFAULT 500 COMMENT '第5天奖励金额(分)',
  `sixDay` bigint NULL DEFAULT 600 COMMENT '第6天奖励金额(分)',
  `sevenDay` bigint NULL DEFAULT 800 COMMENT '第7天奖励金额(分)',
  `firstsevenDay` bigint NULL DEFAULT 700 COMMENT '首次7天奖励金额(分)',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
  `oneDayqdtype` tinyint NULL DEFAULT 2 COMMENT '第1天签到类型 1:积分 2:余额',
  `twoDayqdtype` tinyint NULL DEFAULT 2 COMMENT '第2天签到类型 1:积分 2:余额',
  `threeDayqdtype` tinyint NULL DEFAULT 2 COMMENT '第3天签到类型 1:积分 2:余额',
  `fourDayqdtype` tinyint NULL DEFAULT 2 COMMENT '第4天签到类型 1:积分 2:余额',
  `fiveDayqdtype` tinyint NULL DEFAULT 2 COMMENT '第5天签到类型 1:积分 2:余额',
  `sixDayqdtype` tinyint NULL DEFAULT 2 COMMENT '第6天签到类型 1:积分 2:余额',
  `sevenDayqdtype` tinyint NULL DEFAULT 2 COMMENT '第7天签到类型 1:积分 2:余额',
  `firstsevenDayqdtype` tinyint NULL DEFAULT 2 COMMENT '首次7天签到类型 1:积分 2:余额',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '签到奖励配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_sign_reward_config
-- ----------------------------
INSERT INTO `tb_sign_reward_config` VALUES (1, 100, 200, 300, 400, 500, 600, 800, 700, 1, 2, 2, 2, 2, 2, 2, 2, 2, '2025-08-14 00:45:55', '2025-08-14 00:45:55');

-- ----------------------------
-- Table structure for tb_token
-- ----------------------------
DROP TABLE IF EXISTS `tb_token`;
CREATE TABLE `tb_token`  (
  `id` bigint NOT NULL COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `token` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'token',
  `expire_date` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE,
  UNIQUE INDEX `token`(`token` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户Token' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_token
-- ----------------------------
INSERT INTO `tb_token` VALUES (1611305052584599554, 1067246875900000001, '72b2e63c958e408da4047a74da453ab8', '2023-01-07 10:23:31', '2023-01-06 22:23:31');
INSERT INTO `tb_token` VALUES (1611659311389917186, 1611659311192784898, 'e56e8e8ee0a640e09e2a605cec0a258a', '2023-01-08 05:41:38', '2023-01-07 17:41:38');
INSERT INTO `tb_token` VALUES (1613221669551828993, 1613221669270810626, 'e95795d408f64f35819c6e9d9df7a51c', '2023-01-12 13:09:53', '2023-01-12 01:09:53');

-- ----------------------------
-- Table structure for tb_transaction_detail
-- ----------------------------
DROP TABLE IF EXISTS `tb_transaction_detail`;
CREATE TABLE `tb_transaction_detail`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `transaction_date` datetime NOT NULL COMMENT '交易时间',
  `stream_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流水号',
  `busi_type` int NOT NULL COMMENT '业务类型',
  `original_amount` bigint NOT NULL COMMENT '原始金额(分)',
  `use_amount` bigint NOT NULL COMMENT '使用金额(分)',
  `yhq_amount` bigint NULL DEFAULT 0 COMMENT '优惠券金额(分)',
  `transaction_amount` bigint NOT NULL COMMENT '交易后金额(分)',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 1:成功 0:失败',
  `remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '渠道',
  `formuserid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来源用户ID',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `agent` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代理ID',
  `salesmanid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务员ID',
  `agent_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代理姓名',
  `salesman_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务员姓名',
  `biaoqian` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签',
  `invite_code_status` tinyint NULL DEFAULT 1 COMMENT '邀请码状态 1:有效 0:无效',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_mobile`(`mobile` ASC) USING BTREE,
  INDEX `idx_transaction_date`(`transaction_date` ASC) USING BTREE,
  INDEX `idx_busi_type`(`busi_type` ASC) USING BTREE,
  INDEX `idx_stream_id`(`stream_id` ASC) USING BTREE,
  INDEX `idx_agent`(`agent` ASC) USING BTREE,
  INDEX `idx_salesmanid`(`salesmanid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '账变明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_transaction_detail
-- ----------------------------
INSERT INTO `tb_transaction_detail` VALUES ('1754582404188441957', '1928428619637129218', '2025-08-08 00:00:04', '1754582404188', 10, 418280, 1500, 0, 419780, 1, '投资收益【ANDAR BAHAR】', '1', NULL, '911110000003', '1748403717627', '1748403763980', 'xiaolaohu', 'Doris', NULL, 1, '2025-08-08 03:06:50', '2025-08-08 03:06:50');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `two_pwd` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '二级密码',
  `invite_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邀请码',
  `upinvite_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上级邀请码',
  `agent` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代理信息',
  `agent_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代理名称',
  `salesmanid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务员ID',
  `salesman_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务员名称',
  `superior_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上级名称',
  `superior_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上级邀请码',
  `channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户渠道号',
  `equipment` tinyint NULL DEFAULT 4 COMMENT '登录端口(1:安卓, 2:ios, 3:pc, 4:未知)',
  `register_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注册IP',
  `last_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `last_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `extension_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '推广名称',
  `today_investment` bigint NULL DEFAULT 0 COMMENT '今日投资(分)',
  `history_investment` bigint NULL DEFAULT 0 COMMENT '历史投资(分)',
  `today_profit` bigint NULL DEFAULT 0 COMMENT '今日收益(分)',
  `history_profit` bigint NULL DEFAULT 0 COMMENT '历史收益(分)',
  `today_recharge` bigint NULL DEFAULT 0 COMMENT '今日充值(分)',
  `today_withdraw` bigint NULL DEFAULT 0 COMMENT '今日提现(分)',
  `today_recharge_cnt` bigint NULL DEFAULT 0 COMMENT '今日充值次数',
  `today_withdraw_cnt` bigint NULL DEFAULT 0 COMMENT '今日提现次数',
  `charge_sum` bigint NULL DEFAULT 0 COMMENT '累计充值(分)',
  `withdraw_sum` bigint NULL DEFAULT 0 COMMENT '累计提现(分)',
  `historychargecnt` bigint NULL DEFAULT 0 COMMENT '历史充值次数',
  `historywithdrawcnt` bigint NULL DEFAULT 0 COMMENT '历史提现次数',
  `history_coupon_balance` bigint NULL DEFAULT 0 COMMENT '历史优惠券余额(分)',
  `coupon_cnt` bigint NULL DEFAULT 0 COMMENT '优惠券数量',
  `coupon_balance` bigint NULL DEFAULT 0 COMMENT '优惠券余额(分)',
  `history_privilege_cnt` bigint NULL DEFAULT 0 COMMENT '历史特权券数量',
  `privilege_cnt` bigint NULL DEFAULT 0 COMMENT '特权券数量',
  `balance` bigint NULL DEFAULT 0 COMMENT '余额(分)',
  `cashwithdrawable` bigint NULL DEFAULT 0 COMMENT '可提现余额(分)',
  `freeze_balance` bigint NULL DEFAULT 0 COMMENT '冻结余额(分)',
  `uacnt` bigint NULL DEFAULT 0 COMMENT '1级会员数(分)',
  `ubcnt` bigint NULL DEFAULT 0 COMMENT '2级会员数(分)',
  `uccnt` bigint NULL DEFAULT 0 COMMENT '3级会员数(分)',
  `ua_profit` bigint NULL DEFAULT 0 COMMENT '1级今日收益(分)',
  `ub_profit` bigint NULL DEFAULT 0 COMMENT '2级今日收益(分)',
  `uc_profit` bigint NULL DEFAULT 0 COMMENT '3级今日收益(分)',
  `history_ua_profit` bigint NULL DEFAULT 0 COMMENT '历史1级账户收益(分)',
  `history_ub_profit` bigint NULL DEFAULT 0 COMMENT '历史2级账户收益(分)',
  `history_uc_profit` bigint NULL DEFAULT 0 COMMENT '历史3级账户收益(分)',
  `to_daywithdraw_count` bigint NULL DEFAULT 0 COMMENT '今日提现次数',
  `to_daywithdraw_quota` bigint NULL DEFAULT 0 COMMENT '今日提现额度(分)',
  `withdraw_count` bigint NULL DEFAULT 0 COMMENT '提现次数',
  `withdraw_quota` bigint NULL DEFAULT 0 COMMENT '提现额度(分)',
  `commission_balance` bigint NULL DEFAULT 0 COMMENT '佣金余额(分)',
  `tgrs` bigint NULL DEFAULT 0 COMMENT '推广人数',
  `sy_sum` bigint NULL DEFAULT 0 COMMENT '收益总额(分)',
  `to_dayctc` bigint NULL DEFAULT 0 COMMENT '今日CTC(分)',
  `historyctc` bigint NULL DEFAULT 0 COMMENT '历史CTC(分)',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0：禁用 1：正常',
  `invite_code_status` tinyint NULL DEFAULT 1 COMMENT '邀请码状态 0：禁用 1：正常',
  `tz_withdraw_status` tinyint NULL DEFAULT 0 COMMENT '投资提现状态 0：禁用 1：正常',
  `reward_withdraw_status` tinyint NULL DEFAULT 1 COMMENT '奖励提现状态 0：禁用 1：正常',
  `biaoqian` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签',
  `vip` tinyint NULL DEFAULT 0 COMMENT 'VIP等级',
  `viplr` bigint NULL DEFAULT 0 COMMENT 'VIP利率(分)',
  `assets` bigint NULL DEFAULT NULL,
  `ended_items` bigint NULL DEFAULT NULL,
  `ended_principal` bigint NULL DEFAULT NULL COMMENT '已结束本金',
  `ended_profit` bigint NULL DEFAULT NULL COMMENT '已结束项目数',
  `flag` bigint NULL DEFAULT NULL COMMENT '标识',
  `itmes` bigint NULL DEFAULT NULL COMMENT '项目数',
  `jr_profit` bigint NULL DEFAULT NULL COMMENT '今日收益',
  `login_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录时间',
  `total_principal` bigint NULL DEFAULT NULL COMMENT '总本金',
  `total_profit` bigint NULL DEFAULT 0 COMMENT '总收益(分)',
  `valid3user` bigint NULL DEFAULT 0 COMMENT '有效3个月用户数',
  `valid6user` bigint NULL DEFAULT 0 COMMENT '有效6个月用户数',
  `valid9user` bigint NULL DEFAULT 0 COMMENT '有效9个月用户数',
  `vip1state` bigint NULL DEFAULT 0 COMMENT 'VIP1状态',
  `vip2state` bigint NULL DEFAULT 0 COMMENT 'VIP2状态',
  `vip3state` bigint NULL DEFAULT 0 COMMENT 'VIP3状态',
  `vip4state` bigint NULL DEFAULT 0 COMMENT 'VIP4状态',
  `vip5state` bigint NULL DEFAULT 0 COMMENT 'VIP5状态',
  `vip6state` bigint NULL DEFAULT 0 COMMENT 'VIP6状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_mobile`(`mobile` ASC) USING BTREE,
  UNIQUE INDEX `uk_invite_code`(`invite_code` ASC) USING BTREE,
  INDEX `idx_agent`(`agent` ASC) USING BTREE,
  INDEX `idx_salesmanid`(`salesmanid` ASC) USING BTREE,
  INDEX `idx_superior_code`(`superior_code` ASC) USING BTREE,
  INDEX `idx_channel`(`channel` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_date`(`create_date` ASC) USING BTREE,
  INDEX `idx_last_date`(`last_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1938603271541919747 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1938603271541919746, 'NICO', '917777777713', 'e10adc3949ba59abbe56e057f20f883e', NULL, 'BE1H0Z', NULL, '1748403717627', 'xiaolaohu', '1748403763980', 'Doris', '917777777777', 'IGPIAP', NULL, 2, '182.239.114.217', '46.232.121.223', '2025-08-05 21:32:43', '2025-08-11 10:51:06', '1', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 797900, 67500, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

-- ----------------------------
-- Table structure for tb_user_balance
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_balance`;
CREATE TABLE `tb_user_balance`  (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL COMMENT '用户id',
  `balance` bigint NOT NULL DEFAULT 0 COMMENT '用户余额分为单位',
  `assets` bigint NOT NULL DEFAULT 0 COMMENT '可用余额分为单位',
  `state` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态0正常1被封禁',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次操作时间',
  `locking` int NULL DEFAULT 0 COMMENT '操作锁0正常 1锁定',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_balance
-- ----------------------------
INSERT INTO `tb_user_balance` VALUES (1611677099370303489, 1611659311192784898, 0, 0, 0, '2023-01-07 18:52:19', NULL, 0);

-- ----------------------------
-- Table structure for tb_user_coupon
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_coupon`;
CREATE TABLE `tb_user_coupon`  (
  `user_coupon_id` bigint NOT NULL DEFAULT 1,
  `coupon_id` bigint NOT NULL COMMENT '优惠券id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `distribute_reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '派发理由，如注册送',
  `distribute_date` datetime NOT NULL COMMENT '派发时间',
  `coupon_usage` tinyint(1) NOT NULL COMMENT '使用情况（1未使用 2已使用）',
  `distribute_sys_id` bigint NULL DEFAULT NULL COMMENT '派发人id，如果是系统派发则null',
  `end_date` date NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`user_coupon_id`) USING BTREE,
  INDEX `客户id`(`user_id` ASC) USING BTREE,
  INDEX `优惠券id`(`coupon_id` ASC) USING BTREE,
  CONSTRAINT `优惠券id` FOREIGN KEY (`coupon_id`) REFERENCES `tb_coupon` (`coupon_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `客户id` FOREIGN KEY (`user_id`) REFERENCES `tb_user_old` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_coupon
-- ----------------------------
INSERT INTO `tb_user_coupon` VALUES (1, 1, 1067246875900000001, '注册派发', '2023-01-11 23:19:02', 1, NULL, '2023-02-05');
INSERT INTO `tb_user_coupon` VALUES (2, 2, 1067246875900000001, '后台派送', '2023-01-11 23:20:02', 2, 1, '2023-01-24');
INSERT INTO `tb_user_coupon` VALUES (3, 2, 1067246875900000001, '注册派发', '2023-01-11 23:20:57', 1, NULL, '2023-01-24');
INSERT INTO `tb_user_coupon` VALUES (4, 3, 1067246875900000001, '后台派送', '2022-12-31 23:23:52', 1, 1, '2023-01-10');
INSERT INTO `tb_user_coupon` VALUES (5, 3, 1067246875900000001, '后台派送', '2022-12-31 23:24:13', 2, 1, '2023-01-10');

-- ----------------------------
-- Table structure for tb_user_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_log`;
CREATE TABLE `tb_user_log`  (
  `id` bigint NOT NULL COMMENT 'id',
  `login_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录时间记录',
  `channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '渠道号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_log
-- ----------------------------

-- ----------------------------
-- Table structure for tb_user_old
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_old`;
CREATE TABLE `tb_user_old`  (
  `id` bigint NOT NULL COMMENT 'id',
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '真实名称',
  `mobile` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '手机号',
  `password` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '密码',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `register_ip` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '注册ip',
  `channel` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户渠道号',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态  0：停用   1：正常',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `mobile`(`mobile` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_old
-- ----------------------------
INSERT INTO `tb_user_old` VALUES (1067246875900000001, 'mark', '13612345678', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '2023-01-06 18:13:25', NULL, '2', 1);
INSERT INTO `tb_user_old` VALUES (1611387524714422274, '小城', '12322211122', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '2023-01-06 23:41:39', '0:0:0:0:0:0:0:1', '2', 1);
INSERT INTO `tb_user_old` VALUES (1611388104761499650, 'gegerg', '13122334441', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '2023-01-06 23:43:57', '0:0:0:0:0:0:0:1', '1', 1);
INSERT INTO `tb_user_old` VALUES (1611389717051932674, 'gggerg', '13222344431', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '2023-01-06 23:50:21', '0:0:0:0:0:0:0:1', '2', 1);
INSERT INTO `tb_user_old` VALUES (1611389978189299713, 'gggeFrg', '13422344431', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '2023-01-06 23:51:24', '0:0:0:0:0:0:0:1', '1', 1);
INSERT INTO `tb_user_old` VALUES (1611657806549151745, '小城', '13700000000', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '2023-01-07 17:35:39', '0:0:0:0:0:0:0:1', '2', 1);
INSERT INTO `tb_user_old` VALUES (1611659311192784898, '小城1', '13700000001', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '2023-01-07 17:41:38', '0:0:0:0:0:0:0:1', '2', 0);
INSERT INTO `tb_user_old` VALUES (1613221669270810626, '大幅度', '15222222222', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '2023-01-12 01:09:53', '192.168.0.104', '2', 1);

-- ----------------------------
-- Table structure for tb_user_sign_in
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_sign_in`;
CREATE TABLE `tb_user_sign_in`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `sign_date` date NOT NULL COMMENT '签到日期',
  `sign_time` datetime NOT NULL COMMENT '签到时间',
  `continuous_days` int NULL DEFAULT 1 COMMENT '连续签到天数',
  `reward_amount` bigint NULL DEFAULT 0 COMMENT '奖励金额(分)',
  `reward_type` tinyint NULL DEFAULT 2 COMMENT '奖励类型 1:积分 2:余额',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0:无效 1:有效',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_date`(`user_id` ASC, `sign_date` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_sign_date`(`sign_date` ASC) USING BTREE,
  INDEX `idx_user_sign_user_date`(`user_id` ASC, `sign_date` ASC) USING BTREE,
  INDEX `idx_user_sign_continuous`(`user_id` ASC, `continuous_days` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户签到记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_sign_in
-- ----------------------------

-- ----------------------------
-- Table structure for tb_user_sign_statistics
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_sign_statistics`;
CREATE TABLE `tb_user_sign_statistics`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_sign_days` int NULL DEFAULT 0 COMMENT '总签到天数',
  `continuous_days` int NULL DEFAULT 0 COMMENT '当前连续签到天数',
  `max_continuous_days` int NULL DEFAULT 0 COMMENT '历史最大连续签到天数',
  `total_reward_amount` bigint NULL DEFAULT 0 COMMENT '累计获得奖励金额(分)',
  `last_sign_date` date NULL DEFAULT NULL COMMENT '最后签到日期',
  `last_sign_time` datetime NULL DEFAULT NULL COMMENT '最后签到时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_user_statistics_user`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户签到统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_sign_statistics
-- ----------------------------

-- ----------------------------
-- Table structure for tb_withdraw_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_withdraw_order`;
CREATE TABLE `tb_withdraw_order`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提现渠道',
  `msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '消息',
  `withdraw_time` datetime NULL DEFAULT NULL COMMENT '提现时间',
  `amount` bigint NULL DEFAULT 0 COMMENT '提现到账金额（分）',
  `inputamount` bigint NULL DEFAULT 0 COMMENT '输入金额（分）',
  `rate` decimal(10, 4) NULL DEFAULT 1.0000 COMMENT '汇率',
  `hand_fee` bigint NULL DEFAULT 0 COMMENT '手续费（分）',
  `real_amount` bigint NULL DEFAULT 0 COMMENT '实际到账金额（分）',
  `channel_amount` bigint NULL DEFAULT 0 COMMENT '渠道金额（分）',
  `blank_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '银行代码',
  `blank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '银行名称',
  `pay_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收款人姓名',
  `pay_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收款账号',
  `oper_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作代码',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `state_time` datetime NULL DEFAULT NULL COMMENT '状态时间',
  `state` tinyint NULL DEFAULT 0 COMMENT '状态：0-待审核，1-审核通过，2-已提现，3-驳回，4-提现失败，5-无效订单',
  `withdraw_type` tinyint NULL DEFAULT 1 COMMENT '提现类型：1-余额提现，2-佣金提现',
  `part_mon` int NULL DEFAULT 0 COMMENT '部分金额',
  `orderno` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `threeorder_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方订单号',
  `sourcetype_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来源类型名称',
  `info_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '信息IP',
  `ifsc` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IFSC代码',
  `agent_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代理名称',
  `agent` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代理ID',
  `salesmanid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务员ID',
  `salesman_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务员姓名',
  `channelid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '渠道ID',
  `merchantid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户ID',
  `merchantname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户名称',
  `biaoqian` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签',
  `invite_code_status` tinyint NULL DEFAULT 1 COMMENT '邀请码状态：0-禁用，1-启用',
  `liebian` tinyint NULL DEFAULT 0 COMMENT '是否裂变：0-否，1-是',
  `ctc` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT 'CTC',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_orderno`(`orderno` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_state`(`state` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_withdraw_time`(`withdraw_time` ASC) USING BTREE,
  INDEX `idx_agent`(`agent` ASC) USING BTREE,
  INDEX `idx_salesmanid`(`salesmanid` ASC) USING BTREE,
  INDEX `idx_channelid`(`channelid` ASC) USING BTREE,
  INDEX `idx_merchantid`(`merchantid` ASC) USING BTREE,
  INDEX `idx_user_state`(`user_id` ASC, `state` ASC) USING BTREE,
  INDEX `idx_create_state`(`create_time` ASC, `state` ASC) USING BTREE,
  INDEX `idx_withdraw_type_state`(`withdraw_type` ASC, `state` ASC) USING BTREE,
  INDEX `idx_agent_state`(`agent` ASC, `state` ASC) USING BTREE,
  INDEX `idx_salesman_state`(`salesmanid` ASC, `state` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '提现订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_withdraw_order
-- ----------------------------
INSERT INTO `tb_withdraw_order` VALUES ('1749881135685', '1927581495798161409', '917777777777', 'panda', '1', '商户可提现余额不足', '2025-06-14 14:02:00', 30000, 30000, 1.0000, 0, 31500, 1500, 'State Bank of India', 'State Bank of India', 'panda', '8877777777', 'null', '三方提现商户可提现余额不足', '2025-06-14 14:05:36', '2025-06-14 14:06:16', 2, 2, 6, 'W2025061414053581709', NULL, '', '182.239.92.47', 'ABCD0ABCDEF', NULL, '1748403717627', '1748403763980', 'Doris', '1785184156860125186', '1933761948129419266', 'sangepay', NULL, 1, 0, '0');

-- ----------------------------
-- Table structure for u_address_config
-- ----------------------------
DROP TABLE IF EXISTS `u_address_config`;
CREATE TABLE `u_address_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地址',
  `img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片地址',
  `balance` decimal(20, 8) NULL DEFAULT 0.00000000 COMMENT '余额',
  `state` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'U地址配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of u_address_config
-- ----------------------------
INSERT INTO `u_address_config` VALUES (1, 'test', 'TVsCfPDWy8EZCFBgjzEXRtSZvr5r96w3U3', NULL, 0.00000000, 1, '2025-08-12 17:00:38', '2025-08-12 17:00:38');

-- ----------------------------
-- Table structure for usdtrecord
-- ----------------------------
DROP TABLE IF EXISTS `usdtrecord`;
CREATE TABLE `usdtrecord`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `transaction_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易Hash',
  `from_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '转账地址',
  `to_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收款地址',
  `contract_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合约地址',
  `block_ts` bigint NULL DEFAULT NULL COMMENT '区块时间戳',
  `block_number` bigint NULL DEFAULT NULL COMMENT '区块号',
  `amount` decimal(20, 8) NULL DEFAULT NULL COMMENT '金额',
  `contract_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合约类型',
  `is_risk` tinyint NULL DEFAULT 0 COMMENT '是否风险：0-否，1-是',
  `is_process` tinyint NULL DEFAULT 0 COMMENT '是否已处理：0-否，1-是',
  `block_time` datetime NULL DEFAULT NULL COMMENT '区块时间',
  `order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单号',
  `create_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'U收款记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of usdtrecord
-- ----------------------------
INSERT INTO `usdtrecord` VALUES (1953764246683357186, 'e97d7d79396eb1dd9e16d73469e3e2d466d7f3e6fe1498ebc3edde35fcdf94f6', 'TNXoiAJ3dct8Fjg4M9fkLFh9S2v9TXc32G', 'TVsCfPDWy8EZCFBgjzEXRtSZvr5r96w3U3', NULL, 1754648667000, NULL, 18.46000000, 'USDT', 0, 0, '2025-08-08 18:24:27', NULL, '2025-08-08 18:24:30', '2025-08-08 18:24:30');

-- ----------------------------
-- Table structure for user_balance_detail
-- ----------------------------
DROP TABLE IF EXISTS `user_balance_detail`;
CREATE TABLE `user_balance_detail`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `transaction_date` datetime NULL DEFAULT NULL COMMENT '交易时间',
  `agent_id` bigint NULL DEFAULT NULL COMMENT '代理ID',
  `agent_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代理名称',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签',
  `business_type` int NULL DEFAULT NULL COMMENT '业务类型',
  `channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '渠道',
  `form_user_id` bigint NULL DEFAULT NULL COMMENT '表单用户ID',
  `invite_code_status` tinyint NULL DEFAULT NULL COMMENT '邀请码状态',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  `original_amount` bigint NULL DEFAULT NULL COMMENT '原始金额',
  `remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `salesman_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务员姓名',
  `salesman_id` bigint NULL DEFAULT NULL COMMENT '业务员ID',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `stream_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流水ID',
  `amount_after_transaction` bigint NULL DEFAULT NULL COMMENT '交易后金额',
  `transaction_amount` bigint NULL DEFAULT NULL COMMENT '交易金额',
  `coupon_amount` bigint NULL DEFAULT NULL COMMENT '优惠券金额',
  `create_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '账变详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_balance_detail
-- ----------------------------
INSERT INTO `user_balance_detail` VALUES (1754928006425582651, 1928428619637129218, '2025-08-12 00:00:06', 1748403717627, 'xiaolaohu', NULL, 10, '1', NULL, 1, '911110000003', 4354, '投资收益【CRASH】', 'Doris', 1748403763980, 1, '1754928006425', 4392, 37, 0, '2025-08-12 17:21:31', '2025-08-12 17:21:31');

-- ----------------------------
-- Table structure for user_bank_card
-- ----------------------------
DROP TABLE IF EXISTS `user_bank_card`;
CREATE TABLE `user_bank_card`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `blank_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '银行代码',
  `blank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '银行名称',
  `agent` bigint NULL DEFAULT NULL COMMENT '代理ID',
  `channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '渠道',
  `ifsc` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IFSC代码',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  `pay_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账户持有人姓名',
  `pay_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '银行卡号',
  `salesman_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务员姓名',
  `salesmanid` bigint NULL DEFAULT NULL COMMENT '业务员ID',
  `oper_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作代码',
  `sort_v` int NULL DEFAULT 0 COMMENT '排序值',
  `state` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `oper_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `state_time` datetime NULL DEFAULT NULL COMMENT '状态更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户银行卡配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_bank_card
-- ----------------------------
INSERT INTO `user_bank_card` VALUES (1928078663466303489, 1928078180525752322, 'Punjab National Bank', 'Punjab National Bank', 1748403717627, '1', '', '911234567899', 'luck', '1394502786', 'Doris', 1748403763980, 'sys', 0, 1, '2025-05-29 21:19:10', '2025-05-29 21:19:10', '2025-05-29 21:19:10');
INSERT INTO `user_bank_card` VALUES (1928372945112739842, 1927720543468634113, 'ICICI Bank', 'ICICI Bank', 1748403717627, '1', '', '911516631688', 'Yush', '5243745358', 'Doris', 1748403763980, 'sys', 0, 1, '2025-05-30 16:48:32', '2025-05-30 16:48:32', '2025-05-30 16:48:32');
INSERT INTO `user_bank_card` VALUES (1928373107302281218, 1927720543468634113, 'Punjab National Bank', 'Punjab National Bank', 1748403717627, '1', '', '911516631688', 'Yush', '5689789798', 'Doris', 1748403763980, 'sys', 0, 1, '2025-05-30 16:49:11', '2025-05-30 16:49:10', '2025-05-30 16:49:11');
INSERT INTO `user_bank_card` VALUES (1928452379505278977, 1928428253046571009, 'State Bank of India', 'State Bank of India', 1748403717627, '1', '', '911110000002', 'Alma', '8888888888', 'Doris', 1748403763980, 'sys', 0, 1, '2025-05-30 22:04:11', '2025-05-30 22:04:10', '2025-05-30 22:04:11');
INSERT INTO `user_bank_card` VALUES (1928666237348036610, 1927659412695793665, 'State Bank of India', 'State Bank of India', 1748403717627, '1', '', '919234567890', 'HUIHUII', '6656465456', 'Doris', 1748403763980, 'sys', 0, 1, '2025-05-31 12:13:59', '2025-05-31 12:13:58', '2025-05-31 12:13:59');
INSERT INTO `user_bank_card` VALUES (1930239723984093186, 1927581495798161409, 'State Bank of India', 'State Bank of India', 1748403717627, '1', 'ABCD0ABCDEF', '917777777777', 'panda', '8877777777', 'Doris', 1748403763980, 'sys', 0, 1, '2025-06-04 20:26:27', '2025-06-04 20:26:27', '2025-06-04 20:26:27');

SET FOREIGN_KEY_CHECKS = 1;
