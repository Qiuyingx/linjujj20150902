ALTER TABLE t_message_check ADD `last_readed_activity_reply_id` bigint(20) NOT NULL DEFAULT '0',ADD `last_neighbor_validate_time` bigint(20) NOT NULL DEFAULT '0', ADD `last_follow_me_time` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE t_message_check ADD `last_linju_subject_time` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE t_message_check ADD `last_help_time` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE t_message_check ADD `last_accpet_time` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE t_message_check ADD `last_courtyard_time` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE t_message_check ADD `last_system_time` bigint(20) NOT NULL DEFAULT '0'; 
ALTER TABLE t_message_check ADD `last_verify_time` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE t_message_check ADD `last_activity_time` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE t_message_check ADD `last_news_time` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE t_message_check ADD `last_readed_subject_reply_id` bigint(20) NOT NULL DEFAULT '0';

ALTER TABLE t_post ADD `show_around` bit(1) NOT NULL DEFAULT 1;
ALTER TABLE t_invitation ADD `show_around` bit(1) NOT NULL DEFAULT 1;
ALTER TABLE t_courtyard ADD `longitude` double NOT NULL DEFAULT '0',ADD `latitude` double NOT NULL DEFAULT '0';
ALTER TABLE t_notice ADD `append` bigint(20) NOT NULL DEFAULT '0', ADD `title` varchar(50) DEFAULT NULL;

ALTER TABLE t_activity_info ADD COLUMN isAllYards BIT DEFAULT 1 COMMENT '是否面向所有社区 1（是）；0（否）';
ALTER TABLE t_activity_info ADD COLUMN courtyardIds TEXT DEFAULT '' COMMENT '社区IDs';
ALTER TABLE t_activity_info ADD COLUMN cityId INT COMMENT '城市ID';
ALTER TABLE t_activity_info ADD COLUMN courtyardNames TEXT DEFAULT '' COMMENT '活动范围（面向院子名称s）';
ALTER TABLE t_validate_user ADD COLUMN remark VARCHAR(200) DEFAULT '' COMMENT '审核认证备注';

ALTER TABLE t_content ADD COLUMN views BIGINT(20) DEFAULT 0 COMMENT '浏览量';
ALTER TABLE t_content ADD COLUMN ups BIGINT(20) DEFAULT 0 COMMENT '点赞量';
ALTER TABLE t_content ADD COLUMN comments BIGINT(20) DEFAULT 0 COMMENT '评论数';


ALTER TABLE t_activity_info ADD COLUMN views BIGINT(20) DEFAULT 0 COMMENT '浏览量';
ALTER TABLE t_activity_info ADD COLUMN ups BIGINT(20) DEFAULT 0 COMMENT '点赞量';
ALTER TABLE t_activity_info ADD COLUMN comments BIGINT(20) DEFAULT 0 COMMENT '评论数';

ALTER TABLE t_user ADD COLUMN `official` tinyint(4) NOT NULL DEFAULT '0';

ALTER TABLE t_post ADD COLUMN vali_status TINYINT(11) DEFAULT 0 COMMENT '0提交审核 1审核成功 2审核失败';
ALTER TABLE t_post_removed ADD COLUMN acceptId  BIGINT(20) NOT NULL DEFAULT 0 COMMENT '帮帮采纳的回复ID';
ALTER TABLE t_post_removed ADD COLUMN reward INT(11) NOT NULL DEFAULT 0 COMMENT '帮帮悬赏额';
ALTER TABLE t_post_removed ADD COLUMN myself BIT(1) DEFAULT 0 COMMENT '是否是介绍自己';
ALTER TABLE t_post_removed ADD COLUMN vali_status TINYINT(11) DEFAULT 0 COMMENT '0提交审核 1审核成功 2审核失败';

ALTER TABLE t_notice ADD COLUMN push_type INT(11) COMMENT '推送类型';

ALTER TABLE t_version ADD `iosverion` varchar(20) DEFAULT NULL;
ALTER TABLE t_version ADD `iosaddress` varchar(100) DEFAULT NULL;
ALTER TABLE t_version ADD `ioscontent` varchar(300) DEFAULT NULL;

ALTER TABLE t_content ADD COLUMN banner_img VARCHAR(500) COMMENT 'banner图';
ALTER TABLE t_activity_info ADD COLUMN description VARCHAR(500) COMMENT '描述，用作分享';
-- ----------------------------
-- Table structure for `t_user_follow`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_follow`;
CREATE TABLE `t_user_follow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `target_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_userId` (`user_id`,`target_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_invitation_signup`
-- ----------------------------
DROP TABLE IF EXISTS `t_invitation_signup`;
CREATE TABLE `t_invitation_signup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `invitation_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_invitation` (`user_id`,`invitation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_channel`;

/*==============================================================*/
/* Table: t_channel                                             */
/*==============================================================*/
CREATE TABLE `t_channel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `managerId` bigint(20) NOT NULL COMMENT '创建者ID',
  `channel_name` varchar(50) NOT NULL COMMENT '栏目名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

drop table IF exists `t_content`;

/*==============================================================*/
/* Table: t_content                                             */
/*==============================================================*/
CREATE TABLE `t_content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `managerId` bigint(20) NOT NULL COMMENT '创建者ID',
  `authorId` bigint(20) DEFAULT NULL COMMENT '作者ID',
  `cityId` int(11) DEFAULT NULL COMMENT '城市ID  (0表示所有城市)',
  `title` varchar(300) NOT NULL COMMENT '标题',
  `description` varchar(500) DEFAULT NULL COMMENT '简述',
  `title_img` varchar(200) DEFAULT NULL COMMENT '封面图',
  `create_time` bigint(20) DEFAULT NULL COMMENT '发布时间',
  `content` text COMMENT '内容',
  `channel_id` int(11) NOT NULL COMMENT '栏目ID',
  `status` int(11) NOT NULL COMMENT '状态(0草稿 1发布 2删除)',
  `banner_img` VARCHAR(500) COMMENT 'banner图',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
-- ----------------------------
-- Table structure for `t_black_list`
-- ----------------------------
DROP TABLE IF EXISTS `t_black_list`;
CREATE TABLE `t_black_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `target_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_target` (`user_id`,`target_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_push_message_status`
-- ----------------------------
DROP TABLE IF EXISTS `t_push_message_status`;
CREATE TABLE `t_push_message_status` (
  `id` bigint(20) NOT NULL,
  `version` int(11) NOT NULL,
  `last_readed_reply_id` bigint(20) NOT NULL,
  `last_readed_activity_reply_id` bigint(20) NOT NULL,
  `last_readed_subject_reply_id` bigint(20) NOT NULL,
  `last_readed_laud_id` bigint(20) NOT NULL,
  `last_readed_invitation_like_id` bigint(20) NOT NULL,
  `last_think_reply` bigint(20) NOT NULL,
  `last_linju_subject_time` bigint(20) NOT NULL,
  `last_follow_me_time` bigint(20) NOT NULL,
  `last_help_time` bigint(20) NOT NULL,
  `last_accpet_time` bigint(20) NOT NULL,
  `last_courtyard_time` bigint(20) NOT NULL,
  `last_system_time` bigint(20) NOT NULL,
  `last_verify_time` bigint(20) NOT NULL,
  `last_activity_time` bigint(20) NOT NULL,
  `last_news_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for `t_content_laud`
-- ----------------------------
DROP TABLE IF EXISTS `t_content_laud`;
CREATE TABLE `t_content_laud` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_content_user` (`content_id`,`user_id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_content_reply`
-- ----------------------------
DROP TABLE IF EXISTS `t_content_reply`;
CREATE TABLE `t_content_reply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_id` bigint(20) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `replyer_id` bigint(20) NOT NULL,
  `content` varchar(300) NOT NULL,
  `at_targetId` bigint(20) DEFAULT '0',
  `reply_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_content_courtyard_reply` (`content_id`,`courtyard_id`,`reply_id`) USING BTREE,
  KEY `idx_content_reply` (`content_id`,`reply_id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;

