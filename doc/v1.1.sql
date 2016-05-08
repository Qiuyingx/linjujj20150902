ALTER TABLE t_activity_info ADD `image` varchar(150) DEFAULT NULL,ADD `peoplesLimit` int(11) NOT NULL DEFAULT '0',ADD `signDisable` bit(1) NOT NULL;
ALTER TABLE t_like_invitation ADD `invitation_sender_id` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE t_message_check ADD `last_readed_invitation_like_id` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE t_post ADD `acceptId` bigint(20) NOT NULL DEFAULT '0',ADD `reward` int(11) NOT NULL DEFAULT '0';
ALTER TABLE t_post_reply ADD `accepted` bit(1) NOT NULL DEFAULT 0;
ALTER TABLE t_user ADD `banningTime` bigint(20) NOT NULL DEFAULT '0',ADD `exp` int(11) NOT NULL DEFAULT '0',ADD `level` int(11) NOT NULL DEFAULT '0';
ALTER TABLE t_post ADD `myself` bit(1) NOT NULL DEFAULT 0;
ALTER TABLE t_user_feedback ADD `feedBackImages` varchar(300) DEFAULT NULL;
ALTER TABLE t_message_check ADD `last_think_reply` bigint(20) NOT NULL DEFAULT '0';


SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `t_activity_laud`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_laud`;
CREATE TABLE `t_activity_laud` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activity_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_activity_user` (`activity_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_activity_reply`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_reply`;
CREATE TABLE `t_activity_reply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activity_id` bigint(20) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `replyer_id` bigint(20) NOT NULL,
  `content` varchar(300) NOT NULL,
  `at_targetId` bigint(20) DEFAULT '0',
  `reply_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_activity_courtyard_reply` (`activity_id`,`courtyard_id`,`reply_id`),
  KEY `idx_activity_reply` (`activity_id`,`reply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_activity_signup`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_signup`;
CREATE TABLE `t_activity_signup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  `name` varchar(10) NOT NULL,
  `tel` varchar(13) NOT NULL,
  `content` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_activityid` (`user_id`,`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_lindou_detail`
-- ----------------------------
DROP TABLE IF EXISTS `t_lindou_detail`;
CREATE TABLE `t_lindou_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `changeAmount` int(11) NOT NULL,
  `remark` varchar(64) DEFAULT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_shop`
-- ----------------------------
DROP TABLE IF EXISTS `t_shop`;
CREATE TABLE `t_shop` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `goodsName` varchar(64) NOT NULL,
  `price` int(11) NOT NULL,
  `stockLimit` int(11) NOT NULL,
  `listImage` varchar(300) DEFAULT NULL,
  `image` varchar(150) DEFAULT NULL,
  `remark` text DEFAULT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `t_thank_reply`
-- ----------------------------
DROP TABLE IF EXISTS `t_thank_reply`;
CREATE TABLE `t_thank_reply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `reply_id` bigint(20) NOT NULL,
  `replyer_id` bigint(20) NOT NULL,
  `content_type` tinyint(4) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_reply` (`reply_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_user_daily`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_daily`;
CREATE TABLE `t_user_daily` (
  `id` bigint(20) NOT NULL,
  `version` int(11) NOT NULL,
  `day_of_year` int(11) NOT NULL,
  `last_check_time` bigint(20) NOT NULL,
  `check_continue_days` tinyint(4) NOT NULL,
  `send_post_count` tinyint(4) NOT NULL,
  `send_invitation_count` tinyint(4) NOT NULL,
  `send_help_count` tinyint(4) NOT NULL,
  `help_reply_count` tinyint(4) NOT NULL,
  `be_thank_count` tinyint(4) NOT NULL,
  `accepted_count` tinyint(4) NOT NULL,
  `send_introduce` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_user_lindou`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_lindou`;
CREATE TABLE `t_user_lindou` (
  `id` bigint(20) NOT NULL,
  `version` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_user_order`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_order`;
CREATE TABLE `t_user_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `userName` varchar(10) NOT NULL,
  `tel` varchar(13) NOT NULL,
  `address` varchar(100) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `order_id` varchar(100) NOT NULL,
  `goods_id` bigint(20) NOT NULL,
  `amount` int(11) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_userid` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `t_exp_detail`
-- ----------------------------
DROP TABLE IF EXISTS `t_exp_detail`;
CREATE TABLE `t_exp_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `changeAmount` int(11) NOT NULL,
  `remark` varchar(64) DEFAULT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_couryard_of_activity`
-- ----------------------------
DROP TABLE IF EXISTS `t_couryard_of_activity`;
CREATE TABLE `t_couryard_of_activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `courtyard_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_shop_courtyard`
-- ----------------------------
DROP TABLE IF EXISTS `t_shop_courtyard`;
CREATE TABLE `t_shop_courtyard` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `courtyard_id` bigint(20) NOT NULL,
  `goods_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

