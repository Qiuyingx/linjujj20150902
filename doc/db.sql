
SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `t_activity_info`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_info`;
CREATE TABLE `t_activity_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `activity_title` varchar(50) NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 NOT NULL,
  `createTime` bigint(20) NOT NULL,
  `managerId` bigint(20) NOT NULL,
  `image` varchar(100) NOT NULL,
  `description` VARCHAR(500) COMMENT '描述，用作分享',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_activity_laud`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_laud`;
CREATE TABLE `t_activity_laud` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avtivity_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `dx_avitvityid` (`avtivity_id`,`user_id`,`courtyard_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_activity_reply`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_reply`;
CREATE TABLE `t_activity_reply` (
  `id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `replyer_id` bigint(20) NOT NULL,
  `content` varchar(140) NOT NULL,
  `at_targetId` bigint(20) NOT NULL,
  `reply_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_activity_id` (`activity_id`)
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
  PRIMARY KEY (`id`),
  KEY `dx_user` (`user_id`,`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_company_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_company_user`;
CREATE TABLE `t_company_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `company_id` bigint(20) NOT NULL,
  `user_name` varchar(10) NOT NULL,
  `password` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_courtyard`
-- ----------------------------
DROP TABLE IF EXISTS `t_courtyard`;
CREATE TABLE `t_courtyard` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `city_id` int(11) NOT NULL,
  `company_id` bigint(20) NOT NULL,
  `courtyard_name` varchar(15) NOT NULL,
  `address` varchar(20) NOT NULL,
  `ally` tinyint(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_cityId` (`city_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_discuss_group_member`
-- ----------------------------
DROP TABLE IF EXISTS `t_discuss_group_member`;
CREATE TABLE `t_discuss_group_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `courtyard_id` bigint(20) NOT NULL,
  `invitation_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_invi_user` (`invitation_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_friends`
-- ----------------------------
DROP TABLE IF EXISTS `t_friends`;
CREATE TABLE `t_friends` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `accept` tinyint(11) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `friend_id` bigint(20) NOT NULL,
  `operate_time` bigint(20) NOT NULL,
  `request_time` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `content` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_cy_user_accept` (`courtyard_id`,`user_id`,`accept`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_houseowners`
-- ----------------------------
DROP TABLE IF EXISTS `t_houseowners`;
CREATE TABLE `t_houseowners` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `courtyard_id` bigint(20) NOT NULL,
  `owner_name` varchar(5) DEFAULT NULL,
  `card_id` varchar(20) DEFAULT NULL,
  `tel` varchar(11) DEFAULT NULL,
  `term_id` int(11) NOT NULL,
  `term` varchar(5) NOT NULL,
  `building_id` int(11) NOT NULL,
  `building` varchar(6) NOT NULL,
  `unit_id` int(11) NOT NULL,
  `house` varchar(6) NOT NULL,
  `unit` varchar(6) NOT NULL,
  `house_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_house` (`courtyard_id`,`term_id`,`building_id`,`unit_id`,`house_id`) USING BTREE,
  KEY `idx_cardNo` (`courtyard_id`,`card_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_im_token`
-- ----------------------------
DROP TABLE IF EXISTS `t_im_token`;
CREATE TABLE `t_im_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `access_token` varchar(255) NOT NULL,
  `application` varchar(255) NOT NULL,
  `expires_in` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `t_invitation`
-- ----------------------------
DROP TABLE IF EXISTS `t_invitation`;
CREATE TABLE `t_invitation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `courtyard_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `invitation_type` int(11) NOT NULL,
  `activity_place` varchar(15) NOT NULL,
  `activity_time` bigint(20) NOT NULL,
  `content` varchar(300) DEFAULT NULL,
  `create_time` bigint(20) NOT NULL,
  `group_id` varchar(20) DEFAULT NULL,
  `group_name` varchar(20) DEFAULT NULL,
  `image_names` varchar(350) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_courtyard_user` (`courtyard_id`,`user_id`),
  KEY `idx_courtyard_type` (`courtyard_id`,`invitation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_invitation_removed`
-- ----------------------------
DROP TABLE IF EXISTS `t_invitation_removed`;
CREATE TABLE `t_invitation_removed` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `courtyard_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `invitation_type` int(11) NOT NULL,
  `activity_place` varchar(15) NOT NULL,
  `activity_time` bigint(20) NOT NULL,
  `content` varchar(140) DEFAULT NULL,
  `create_time` bigint(20) NOT NULL,
  `group_id` varchar(20) DEFAULT NULL,
  `group_name` varchar(20) DEFAULT NULL,
  `image_names` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_courtyard_user` (`courtyard_id`,`user_id`),
  KEY `idx_courtyard_type` (`courtyard_id`,`invitation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_like_invitation`
-- ----------------------------
DROP TABLE IF EXISTS `t_like_invitation`;
CREATE TABLE `t_like_invitation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `courtyard_id` bigint(20) NOT NULL,
  `invitation_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_invitation_user` (`invitation_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_manager_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_manager_user`;
CREATE TABLE `t_manager_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `userName` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL,
  `role` int(11) NOT NULL,
  `courtyardId` bigint(20) NOT NULL DEFAULT '0',
  `banning` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_message_check`
-- ----------------------------
DROP TABLE IF EXISTS `t_message_check`;
CREATE TABLE `t_message_check` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `courtyard_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `last_friend_request` bigint(20) NOT NULL,
  `last_invitation` bigint(20) NOT NULL,
  `last_notice` bigint(20) NOT NULL,
  `last_readed_laud_id` bigint(20) NOT NULL,
  `last_readed_reply_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_couryard` (`user_id`,`courtyard_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_notice`
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `content` varchar(200) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `create_time` bigint(20) NOT NULL,
  `notice_type` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_type_courtyard` (`notice_type`,`courtyard_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_post`
-- ----------------------------
DROP TABLE IF EXISTS `t_post`;
CREATE TABLE `t_post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `courtyard_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `content_type` tinyint(11) NOT NULL,
  `image_names` varchar(350) DEFAULT NULL,
  `priority` tinyint(11) NOT NULL,
  `title` varchar(20) DEFAULT NULL,
  `content` varchar(300) DEFAULT NULL,
  `create_time` bigint(20) NOT NULL,
  `tag` tinyint(4) NOT NULL DEFAULT '0',
  `rewardLindou` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_courtyard_user` (`courtyard_id`,`user_id`),
  KEY `idx_courtyard_circle` (`courtyard_id`,`content_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `t_post_laud`
-- ----------------------------
DROP TABLE IF EXISTS `t_post_laud`;
CREATE TABLE `t_post_laud` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `courtyard_id` bigint(20) NOT NULL,
  `post_id` bigint(20) NOT NULL,
  `post_sender_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_post_user` (`user_id`,`post_id`) USING BTREE,
  KEY `idx_courtyard_sender` (`courtyard_id`,`post_sender_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_post_removed`
-- ----------------------------
DROP TABLE IF EXISTS `t_post_removed`;
CREATE TABLE `t_post_removed` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `courtyard_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `content_type` tinyint(11) NOT NULL,
  `image_names` varchar(300) DEFAULT NULL,
  `priority` tinyint(11) NOT NULL,
  `title` varchar(20) DEFAULT NULL,
  `content` varchar(140) DEFAULT NULL,
  `create_time` bigint(20) NOT NULL,
  `tag` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_courtyard_user` (`courtyard_id`,`user_id`),
  KEY `idx_courtyard_circle` (`courtyard_id`,`content_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_post_reply`
-- ----------------------------
DROP TABLE IF EXISTS `t_post_reply`;
CREATE TABLE `t_post_reply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `post_id` bigint(20) NOT NULL,
  `content_type` int(11) NOT NULL,
  `replyer_id` bigint(20) NOT NULL,
  `content` varchar(300) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  `at_targetId` bigint(20) NOT NULL DEFAULT '0',
  `post_sender_id` bigint(20) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `reply_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_courtyard_sender` (`courtyard_id`,`post_sender_id`),
  KEY `idx_post` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `t_property_company`
-- ----------------------------
DROP TABLE IF EXISTS `t_property_company`;
CREATE TABLE `t_property_company` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company_name` varchar(20) NOT NULL,
  `alipay_acount` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `t_tel_validate`
-- ----------------------------
DROP TABLE IF EXISTS `t_tel_validate`;
CREATE TABLE `t_tel_validate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` bigint(20) NOT NULL,
  `tel` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `tel` char(11) DEFAULT NULL,
  `nickname` varchar(10) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `careerId` int(11) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `gender` int(11) NOT NULL,
  `head_icon` varchar(150) DEFAULT NULL,
  `houseowner_id` bigint(20) NOT NULL,
  `inviteCode` bigint(20) NOT NULL,
  `last_login_time` bigint(20) NOT NULL,
  `password` varchar(40) DEFAULT NULL,
  `platform` int(11) NOT NULL DEFAULT '0',
  `register_time` bigint(20) NOT NULL,
  `signature` varchar(20) DEFAULT NULL,
  `token` varchar(128) DEFAULT NULL,
  `last_login_token` varchar(128) DEFAULT '',
  `last_login_platform` int(11) NOT NULL,
  `externalType` int(11) NOT NULL,
  `openId` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_tel` (`tel`),
  KEY `idx_pf_token` (`last_login_platform`,`last_login_token`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_user_circle`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_circle`;
CREATE TABLE `t_user_circle` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `circle_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `t_user_collect`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_collect`;
CREATE TABLE `t_user_collect` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `collect_time` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `collect_type` int(11) NOT NULL,
  `target_id` bigint(20) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_type_id` (`user_id`,`collect_type`,`target_id`),
  KEY `idx_courtyard_type` (`courtyard_id`,`collect_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_user_error`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_error`;
CREATE TABLE `t_user_error` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `address` varchar(25) NOT NULL,
  `user_name` varchar(10) NOT NULL,
  `user_code` varchar(20) NOT NULL,
  `user_tel` varchar(11) NOT NULL,
  `user_content` varchar(100) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for `t_user_feedback`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_feedback`;
CREATE TABLE `t_user_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `content_back` text DEFAULT NULL,
  `user_tel` varchar(11) DEFAULT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_courtyard_id` (`courtyard_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for `t_user_interest`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_interest`;
CREATE TABLE `t_user_interest` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL,
  `interest_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_user_report`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_report`;
CREATE TABLE `t_user_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL,
  `content_type` tinyint(4) NOT NULL,
  `target_id` bigint(20) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `content` varchar(100) DEFAULT NULL,
  `report_type` int(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_courtyard_type` (`courtyard_id`,`content_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_user_setting`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_setting`;
CREATE TABLE `t_user_setting` (
  `id` bigint(20) NOT NULL,
  `reply` bit(1) NOT NULL,
  `laud` bit(1) NOT NULL,
  `message` bit(1) NOT NULL,
  `system` bit(1) NOT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for `t_user_trade`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_trade`;
CREATE TABLE `t_user_trade` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT '0',
  `alipay_trade_id` varchar(255) DEFAULT NULL,
  `buyer_email` varchar(255) DEFAULT NULL,
  `buyer_id` varchar(255) DEFAULT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  `seller_email` varchar(255) DEFAULT NULL,
  `seller_id` varchar(255) DEFAULT NULL,
  `total_fee` int(11) NOT NULL,
  `trade_id` varchar(255) DEFAULT NULL,
  `trade_status` int(11) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_user_wallet`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_wallet`;
CREATE TABLE `t_user_wallet` (
  `id` bigint(20) NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `amount` int(11) NOT NULL,
  `gain_time` bigint(20) NOT NULL,
  `source` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_validate_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_validate_user`;
CREATE TABLE `t_validate_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `courtyard_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  `validate_type` tinyint(4) NOT NULL,
  `append` varchar(150) DEFAULT NULL,
  `validate_status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_courtyard_user` (`courtyard_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_version`
-- ----------------------------
DROP TABLE IF EXISTS `t_version`;
CREATE TABLE `t_version` (
  `id` bigint(8) NOT NULL,
  `appVersion` varchar(15) NOT NULL,
  `address` varchar(100) NOT NULL,
  `content` varchar(300) NOT NULL,
  `appVersionid` int(10) NOT NULL,
  `version` int(11) NOT NULL,
  `pass` bit(1) NOT NULL,
  `iosverion` varchar(20) DEFAULT NULL,
  `iosaddress` varchar(100) DEFAULT NULL,
  `ioscontent` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

