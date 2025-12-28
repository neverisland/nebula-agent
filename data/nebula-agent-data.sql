/*
 Navicat Premium Dump SQL

 Source Server         : 本地Mysql
 Source Server Type    : MySQL
 Source Server Version : 90001 (9.0.1)
 Source Host           : localhost:3306
 Source Schema         : nebula-agent

 Target Server Type    : MySQL
 Target Server Version : 90001 (9.0.1)
 File Encoding         : 65001

 Date: 28/12/2025 21:53:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for authentication_historical_password
-- ----------------------------
DROP TABLE IF EXISTS `authentication_historical_password`;
CREATE TABLE `authentication_historical_password` (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Id',
  `user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `password_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码的哈希值',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户历史密码';

-- ----------------------------
-- Records of authentication_historical_password
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for authentication_mini_program
-- ----------------------------
DROP TABLE IF EXISTS `authentication_mini_program`;
CREATE TABLE `authentication_mini_program` (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户小程序id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户微信小程序认证';

-- ----------------------------
-- Records of authentication_mini_program
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for authentication_password
-- ----------------------------
DROP TABLE IF EXISTS `authentication_password`;
CREATE TABLE `authentication_password` (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码认证id',
  `user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `password_type` tinyint(1) NOT NULL COMMENT '密码类型，0-初始密码 1-自定义密码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间,密码修改时间',
  PRIMARY KEY (`id`,`user_id`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE COMMENT '用户id唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户密码认证';

-- ----------------------------
-- Records of authentication_password
-- ----------------------------
BEGIN;
INSERT INTO `authentication_password` (`id`, `user_id`, `password`, `password_type`, `create_time`, `update_time`) VALUES ('01KCTPBXG6AY0CEEK3056EBXC9', '01KCTPBE8FFZJD49P0BF3WR6G5', '3850a8af4323e701c22adb26b81fffec6b8d7b38df743b41e2a5c606ab45b307a8fd31ab3c850215a8a8a40cd9727e3e', 0, '2025-12-19 14:57:14', '2025-12-19 15:06:31');
INSERT INTO `authentication_password` (`id`, `user_id`, `password`, `password_type`, `create_time`, `update_time`) VALUES ('01KCTPWPEA7FXFFQG9Y8154HG2', '01KCTPW8FY8FMHVZYX42M0DVS3', '058e157a3b8674985d74aca776154ca657e9bfb22661d81b573a47c35cf76b4c010423f0282ed37b69474d1088f84bde', 0, '2025-12-19 15:06:24', '2025-12-19 15:06:24');
COMMIT;

-- ----------------------------
-- Table structure for file_library
-- ----------------------------
DROP TABLE IF EXISTS `file_library`;
CREATE TABLE `file_library` (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `space_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '空间id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名称',
  `mime_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件类型',
  `size` bigint NOT NULL COMMENT '文件大小',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件存储相对路径',
  `thumbnails` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '缩略图相对路径',
  `create_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_space_id` (`space_id`) USING BTREE,
  KEY `idx_name_path` (`name`,`path`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE,
  CONSTRAINT `fx_file_library` FOREIGN KEY (`space_id`) REFERENCES `file_space` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='文件库';

-- ----------------------------
-- Records of file_library
-- ----------------------------
BEGIN;
INSERT INTO `file_library` (`id`, `space_id`, `name`, `mime_type`, `size`, `path`, `thumbnails`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES ('01KCTQ1ZKTEF9BBMATM53ABZ72', '01KCTR3ZGYEKN6D7CHNAZQW61Q', 'image.png', 'image/png', 3944, '2025/12/19/97c03352ee55468fa8ce5d2010115d43.png', '2025/12/19/97c03352ee55468fa8ce5d2010115d43-thumbnail.png', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-19 15:09:17', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-19 16:06:35');
INSERT INTO `file_library` (`id`, `space_id`, `name`, `mime_type`, `size`, `path`, `thumbnails`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES ('01KCTTGFKKYBX7VBV7JX8GWJJ7', NULL, 'image.png', 'image/png', 15673, '2025/12/19/44ab91025eb14e7f9d0f0d3735d21944.png', '2025/12/19/44ab91025eb14e7f9d0f0d3735d21944-thumbnail.png', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-19 16:09:38', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-19 16:41:34');
INSERT INTO `file_library` (`id`, `space_id`, `name`, `mime_type`, `size`, `path`, `thumbnails`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES ('01KDFDDJ0MRNH5GYDPWVHAWZGR', NULL, 'favorites_2024_10_26.html', 'text/html', 280048, '2025/12/27/463f8107713849f796fe80b9b0c2e970.html', NULL, '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-27 16:04:52', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-27 16:04:52');
COMMIT;

-- ----------------------------
-- Table structure for file_share
-- ----------------------------
DROP TABLE IF EXISTS `file_share`;
CREATE TABLE `file_share` (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分享记录ID',
  `share_type` tinyint NOT NULL COMMENT '分享类型: 1-个人文件, 2-个人空间',
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分享名称或描述',
  `enable_password` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开启密码访问',
  `password` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '访问密码，可为空',
  `enable_expire` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开启过期时间',
  `expire_time` date DEFAULT NULL COMMENT '分享过期时间，可为空',
  `is_expired` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已过期',
  `visit_count` int NOT NULL DEFAULT '0' COMMENT '访问次数',
  `download_count` int NOT NULL DEFAULT '0' COMMENT '下载次数',
  `create_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新者id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文件/空间分享记录表';

-- ----------------------------
-- Records of file_share
-- ----------------------------
BEGIN;
INSERT INTO `file_share` (`id`, `share_type`, `name`, `enable_password`, `password`, `enable_expire`, `expire_time`, `is_expired`, `visit_count`, `download_count`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES ('01KDJHZE18VZHYFVQ1QZARHK4M', 1, '问问', 0, '', 0, NULL, 0, 0, 0, '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-28 21:22:16', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-28 21:22:16');
INSERT INTO `file_share` (`id`, `share_type`, `name`, `enable_password`, `password`, `enable_expire`, `expire_time`, `is_expired`, `visit_count`, `download_count`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES ('01KDJHZN2ZMS9Y9JK39B2RSQXX', 1, '问问', 0, '', 0, NULL, 0, 0, 0, '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-28 21:22:23', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-28 21:22:23');
INSERT INTO `file_share` (`id`, `share_type`, `name`, `enable_password`, `password`, `enable_expire`, `expire_time`, `is_expired`, `visit_count`, `download_count`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES ('01KDJJ0JZ4SW5WWA96251M05WQ', 1, '22', 0, '', 0, NULL, 0, 0, 0, '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-28 21:22:54', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-28 21:22:54');
INSERT INTO `file_share` (`id`, `share_type`, `name`, `enable_password`, `password`, `enable_expire`, `expire_time`, `is_expired`, `visit_count`, `download_count`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES ('01KDJJ23N6MC0VC77VD82R8QD3', 1, '你好', 0, '', 0, NULL, 0, 0, 0, '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-28 21:23:43', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-28 21:23:43');
COMMIT;

-- ----------------------------
-- Table structure for file_sharing_association
-- ----------------------------
DROP TABLE IF EXISTS `file_sharing_association`;
CREATE TABLE `file_sharing_association` (
  `id` varchar(26) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `share_id` varchar(26) COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联的分享记录ID',
  `file_id` varchar(26) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联的文件ID，可为空，分享空间时为空',
  `space_id` varchar(26) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联的空间ID，可为空，分享文件时为空',
  PRIMARY KEY (`id`),
  KEY `fk_share` (`share_id`),
  KEY `fk_file` (`file_id`),
  KEY `fk_space` (`space_id`),
  CONSTRAINT `fk_file` FOREIGN KEY (`file_id`) REFERENCES `file_library` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_share` FOREIGN KEY (`share_id`) REFERENCES `file_share` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_space` FOREIGN KEY (`space_id`) REFERENCES `file_space` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文件/空间分享关联表';

-- ----------------------------
-- Records of file_sharing_association
-- ----------------------------
BEGIN;
INSERT INTO `file_sharing_association` (`id`, `share_id`, `file_id`, `space_id`) VALUES ('01KDJHZE1F5FGAS14GS2GDBDS2', '01KDJHZE18VZHYFVQ1QZARHK4M', '01KDFDDJ0MRNH5GYDPWVHAWZGR', NULL);
INSERT INTO `file_sharing_association` (`id`, `share_id`, `file_id`, `space_id`) VALUES ('01KDJHZN323WM9MXCR0W06A29J', '01KDJHZN2ZMS9Y9JK39B2RSQXX', '01KDFDDJ0MRNH5GYDPWVHAWZGR', NULL);
INSERT INTO `file_sharing_association` (`id`, `share_id`, `file_id`, `space_id`) VALUES ('01KDJJ0JZ7C1M87JEFDYTDPEPH', '01KDJJ0JZ4SW5WWA96251M05WQ', '01KDFDDJ0MRNH5GYDPWVHAWZGR', NULL);
INSERT INTO `file_sharing_association` (`id`, `share_id`, `file_id`, `space_id`) VALUES ('01KDJJ0JZ7RN89TKN8PPFBW8V4', '01KDJJ0JZ4SW5WWA96251M05WQ', '01KCTTGFKKYBX7VBV7JX8GWJJ7', NULL);
INSERT INTO `file_sharing_association` (`id`, `share_id`, `file_id`, `space_id`) VALUES ('01KDJJ23N9P8P3RADFCAG9M1QR', '01KDJJ23N6MC0VC77VD82R8QD3', '01KCTQ1ZKTEF9BBMATM53ABZ72', NULL);
COMMIT;

-- ----------------------------
-- Table structure for file_space
-- ----------------------------
DROP TABLE IF EXISTS `file_space`;
CREATE TABLE `file_space` (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '空间名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='个人空间';

-- ----------------------------
-- Records of file_space
-- ----------------------------
BEGIN;
INSERT INTO `file_space` (`id`, `name`, `remark`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES ('01KCTR3ZGYEKN6D7CHNAZQW61Q', '图床', '/', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-19 15:27:51', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-19 15:39:22');
COMMIT;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限id',
  `mark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限标识',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_mark` (`mark`) USING BTREE COMMENT '权限标识唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='权限';

-- ----------------------------
-- Records of permission
-- ----------------------------
BEGIN;
INSERT INTO `permission` (`id`, `mark`, `description`, `create_time`, `update_time`) VALUES ('01KCTPABZSEDPM7Z2JRN6RR2AE', 'basic_permissions', '基础权限', '2025-12-19 14:56:23', '2025-12-19 14:56:23');
INSERT INTO `permission` (`id`, `mark`, `description`, `create_time`, `update_time`) VALUES ('01KCTPABZT7ZS1N1S2DD9KKT56', 'management_role', '管理角色权限', '2025-12-19 14:56:23', '2025-12-19 14:56:23');
COMMIT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色id',
  `mark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色标识',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色描述',
  `type` tinyint NOT NULL DEFAULT '1' COMMENT '角色类型 0 内置角色 1 自定义角色',
  `create_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_mark` (`mark`) USING BTREE COMMENT '角色标识唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色';

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` (`id`, `mark`, `name`, `description`, `type`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES ('01KCTPAC1WGY185XA2QJ5S72AJ', 'system_administrator', '系统管理员', '系统管理员角色,负责系统的维护使用', 0, '', '2025-12-19 14:56:23', '', '2025-12-19 14:56:23');
INSERT INTO `role` (`id`, `mark`, `name`, `description`, `type`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES ('01KCTPAC1XDNM6YMCGVQYPYCJ9', 'ordinary_users', '普通用户', '系统普通用户', 0, '', '2025-12-19 14:56:23', '', '2025-12-19 14:56:23');
COMMIT;

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限角色id',
  `role_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色id',
  `permission_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fx_authority` (`permission_id`) USING BTREE,
  KEY `fx_role` (`role_id`) USING BTREE,
  CONSTRAINT `fx_permission` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fx_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色权限关联';

-- ----------------------------
-- Records of role_permission
-- ----------------------------
BEGIN;
INSERT INTO `role_permission` (`id`, `role_id`, `permission_id`) VALUES ('01KCTPAC1X9VAV75JGWVARVPED', '01KCTPAC1XDNM6YMCGVQYPYCJ9', '01KCTPABZSEDPM7Z2JRN6RR2AE');
INSERT INTO `role_permission` (`id`, `role_id`, `permission_id`) VALUES ('01KCTPAC1XKWZJE0T8EBFEQ3FH', '01KCTPAC1WGY185XA2QJ5S72AJ', '01KCTPABZSEDPM7Z2JRN6RR2AE');
INSERT INTO `role_permission` (`id`, `role_id`, `permission_id`) VALUES ('01KCTPAC1XNFDYBHHYM0FTDEQN', '01KCTPAC1WGY185XA2QJ5S72AJ', '01KCTPABZT7ZS1N1S2DD9KKT56');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `account_non_locked` tinyint NOT NULL DEFAULT '1' COMMENT '是否未锁定 0 锁定 1 解锁',
  `locked_time` datetime DEFAULT NULL COMMENT '锁定时间',
  `enabled` tinyint NOT NULL DEFAULT '1' COMMENT '是否启用 0 禁用 1 启用',
  `create_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_username` (`username`) USING BTREE COMMENT '用户名唯一',
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`id`, `username`, `nickname`, `phone`, `email`, `account_non_locked`, `locked_time`, `enabled`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES ('01KCTPBE8FFZJD49P0BF3WR6G5', 'yangyi', '杨依', '15715822290', '1216288809@qq.com', 1, NULL, 1, '', '2025-12-19 14:56:58', '', '2025-12-19 14:56:58');
COMMIT;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份角色关联id',
  `user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份id',
  `role_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_role` (`role_id`) USING BTREE,
  KEY `fk_user` (`user_id`) USING BTREE,
  CONSTRAINT `fk_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='身份角色关联';

-- ----------------------------
-- Records of user_role
-- ----------------------------
BEGIN;
INSERT INTO `user_role` (`id`, `user_id`, `role_id`) VALUES ('01KCTPBE93RA2MG58XK8M8HMJP', '01KCTPBE8FFZJD49P0BF3WR6G5', '01KCTPAC1WGY185XA2QJ5S72AJ');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
