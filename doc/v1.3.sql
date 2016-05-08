#1.3
ALTER TABLE t_post_removed ADD `show_around` BIT DEFAULT 1 COMMENT '是否周围可见';


ALTER TABLE t_user ADD COLUMN `pageImage` varchar(60) DEFAULT NULL;
ALTER TABLE t_user ADD COLUMN `skill` varchar(10) DEFAULT NULL COMMENT '技能标签';

ALTER TABLE t_message_check ADD `last_readed_reply_time` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE t_message_check ADD `last_readed_atTime` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE t_message_check ADD `last_readed_laud_time` bigint(20) NOT NULL DEFAULT '0';

ALTER TABLE t_push_message_status ADD `last_readed_reply_time` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE t_push_message_status ADD `last_readed_atTime` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE t_push_message_status ADD `last_readed_laud_time` bigint(20) NOT NULL DEFAULT '0';

-- ----------------------------
-- Table structure for `t_at_relations`
-- ----------------------------
DROP TABLE IF EXISTS `t_at_relations`;
CREATE TABLE `t_at_relations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `at_target_id` bigint(20) NOT NULL,
  `at_nickname` varchar(255) NOT NULL,
  `scene` tinyint(4) NOT NULL,
  `append` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_acene_append` (`scene`,`append`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for `t_top_post`
-- ----------------------------
DROP TABLE IF EXISTS `t_top_post`;
CREATE TABLE `t_top_post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `post_id` bigint(20) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `topType` tinyint(4) NOT NULL,
  `priority` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for `t_train_info`
-- ----------------------------
DROP TABLE IF EXISTS `t_train_info`;
CREATE TABLE `t_train_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `userId` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `managerId` bigint(20) DEFAULT NULL COMMENT '后台管理账号ID',
  `courtyardId` bigint(20) NOT NULL DEFAULT '0' COMMENT '院子ID',
  `cityId` int(11) NOT NULL DEFAULT '0' COMMENT '城市ID',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `content` longtext COMMENT '内容',
  `banner_img` varchar(100) DEFAULT NULL COMMENT 'banner图',
  `views` int(11) unsigned DEFAULT '0' COMMENT '浏览量',
  `businessHours` varchar(100) DEFAULT NULL COMMENT '营业时间',
  `tel` varchar(13) DEFAULT NULL COMMENT '电话号码',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `latitude` double DEFAULT '0' COMMENT '纬度',
  `longitude` double DEFAULT '0' COMMENT '经度',
  `category` int(11) DEFAULT NULL COMMENT '类别',
  `categoryName` varchar(500) DEFAULT NULL COMMENT '类别名称',
  `create_time` bigint(20) DEFAULT NULL COMMENT '信息完善时间（第一次完善）',
  `status` int(11) DEFAULT '0' COMMENT '0未完善资料 1开通  2下线（禁用）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for `t_train_open`
-- ----------------------------
DROP TABLE IF EXISTS `t_train_open`;
CREATE TABLE `t_train_open` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `userId` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
  `courtyardId` bigint(20) NOT NULL DEFAULT '0' COMMENT '院子id',
  `category` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '类别',
  `introduction` varchar(2500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '介绍',
  `tel` varchar(13) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱地址',
  `passed` tinyint(4) DEFAULT '0' COMMENT '状态 0已申请 1已通过 2未通过 ',
  `create_time` bigint(20) DEFAULT NULL COMMENT '申请时间',
  `vali_time` bigint(20) DEFAULT NULL COMMENT '审核时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '审核备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `t_user_star` 技能申请
-- ----------------------------
DROP TABLE IF EXISTS `t_user_star`;
CREATE TABLE `t_user_star` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `tel` varchar(13) DEFAULT NULL,
  `content` text,
  `image_names` varchar(300) DEFAULT NULL,
  `skill` varchar(255) DEFAULT NULL COMMENT '技能标签',
  `status` tinyint(4) NOT NULL COMMENT '0 申请 1 通过 2 审核不通过',
  `create_time` bigint(20) NOT NULL,
  `remark` varchar(300) COMMENT '审核备注（不通过原因）',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for `t_train_laud`
-- ----------------------------
DROP TABLE IF EXISTS `t_train_laud`;
CREATE TABLE `t_train_laud` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `train_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `courtyard_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_train_user` (`train_id`,`user_id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

/**
 * 回复视图
 */
CREATE OR REPLACE VIEW v_reply(
replyId,
replyerId,
at_targetId,
senderId,
content,
createTime,
targetId,
contentType) 
AS (SELECT id,replyer_id,at_targetId,post_sender_id,content,create_time,post_id,content_type FROM t_post_reply WHERE content_type<>1) 
UNION ALL (SELECT id,replyer_id,at_targetId,0,content,create_time,activity_id,4 FROM t_activity_reply) 
UNION ALL (SELECT id,replyer_id,at_targetId,0,content,create_time,content_id,5 FROM t_content_reply);


/**
 * 感谢视图，不包括邀约感谢
 */
CREATE OR REPLACE VIEW v_laud(
id,
lauder_id,
beLauder_id,
content_id,
create_time,laud_type) 
AS (SELECT id,user_id,post_sender_id,post_id,create_time,1 FROM t_post_laud) 
-- UNION ALL (SELECT id,user_id,invitation_sender_id,invitation_id,create_time,3 FROM t_like_invitation)
UNION ALL (SELECT id,user_id,replyer_id,reply_id,create_time,2 FROM t_thank_reply WHERE content_type<>1);

/*
 * 建立学堂视图 
 */
CREATE OR REPLACE VIEW v_train_view
AS (SELECT id,cityId,longitude,latitude FROM t_train_info where status=1);

/**
 * 每个帖子的回复数统计视图 
 */
CREATE OR REPLACE VIEW v_post_reply_count 
AS (SELECT p.*,((SELECT count(*) FROM t_post_laud WHERE post_id=p.id)+(SELECT count(*) FROM t_post_reply WHERE post_id=p.id AND content_type=p.content_type AND post_sender_id<>replyer_id AND reply_id=0)) AS replys FROM t_post AS p);


/**
 * 设置初始的最后回复时间和感谢时间为当前时间
 */
UPDATE t_message_check SET last_readed_reply_time=unix_timestamp() * 1000;
UPDATE t_push_message_status SET last_readed_reply_time=unix_timestamp() * 1000;
UPDATE t_message_check SET last_readed_laud_time=unix_timestamp() * 1000;
UPDATE t_push_message_status SET last_readed_laud_time=unix_timestamp() * 1000;