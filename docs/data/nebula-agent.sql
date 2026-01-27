/*
 Navicat Premium Dump SQL

 Source Server         : Mysql-阿里云
 Source Server Type    : MySQL
 Source Server Version : 80408 (8.4.8)
 Source Host           : 47.99.103.72:13309
 Source Schema         : nebula-agent

 Target Server Type    : MySQL
 Target Server Version : 80408 (8.4.8)
 File Encoding         : 65001

 Date: 27/01/2026 16:47:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for authentication_historical_password
-- ----------------------------
DROP TABLE IF EXISTS `authentication_historical_password`;
CREATE TABLE `authentication_historical_password`  (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Id',
  `user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `password_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码的哈希值',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户历史密码' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of authentication_historical_password
-- ----------------------------

-- ----------------------------
-- Table structure for authentication_mini_program
-- ----------------------------
DROP TABLE IF EXISTS `authentication_mini_program`;
CREATE TABLE `authentication_mini_program`  (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户小程序id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户微信小程序认证' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of authentication_mini_program
-- ----------------------------

-- ----------------------------
-- Table structure for authentication_password
-- ----------------------------
DROP TABLE IF EXISTS `authentication_password`;
CREATE TABLE `authentication_password`  (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码认证id',
  `user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `password_type` tinyint(1) NOT NULL COMMENT '密码类型，0-初始密码 1-自定义密码',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间,密码修改时间',
  PRIMARY KEY (`id`, `user_id`) USING BTREE,
  UNIQUE INDEX `uk_user_id`(`user_id` ASC) USING BTREE COMMENT '用户id唯一'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户密码认证' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of authentication_password
-- ----------------------------
INSERT INTO `authentication_password` VALUES ('01KCTPBXG6AY0CEEK3056EBXC9', '01KCTPBE8FFZJD49P0BF3WR6G5', '3850a8af4323e701c22adb26b81fffec6b8d7b38df743b41e2a5c606ab45b307a8fd31ab3c850215a8a8a40cd9727e3e', 0, '2025-12-19 14:57:14', '2025-12-19 15:06:31');

-- ----------------------------
-- Table structure for file_library
-- ----------------------------
DROP TABLE IF EXISTS `file_library`;
CREATE TABLE `file_library`  (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `space_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '空间id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名称',
  `mime_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `size` bigint NOT NULL COMMENT '文件大小',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件存储相对路径',
  `thumbnails` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缩略图相对路径',
  `create_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_space_id`(`space_id` ASC) USING BTREE,
  INDEX `idx_name_path`(`name` ASC, `path` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  CONSTRAINT `fx_file_library` FOREIGN KEY (`space_id`) REFERENCES `file_space` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件库' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_library
-- ----------------------------
INSERT INTO `file_library` VALUES ('01KFZ9QTD1NHR1PFZ85JFZ2YT6', NULL, 'IMG_2957.JPG', 'image/jpeg', 96914, '2026/01/27/ef0753bdc35640cbbcea6d7d97fb3448.JPG', '2026/01/27/ef0753bdc35640cbbcea6d7d97fb3448-thumbnail.JPG', '01KCTPBE8FFZJD49P0BF3WR6G5', '2026-01-27 16:39:49', '01KCTPBE8FFZJD49P0BF3WR6G5', '2026-01-27 16:39:49');

-- ----------------------------
-- Table structure for file_share
-- ----------------------------
DROP TABLE IF EXISTS `file_share`;
CREATE TABLE `file_share`  (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分享记录ID',
  `share_type` tinyint NOT NULL COMMENT '分享类型: 1-个人文件, 2-个人空间',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分享名称或描述',
  `enable_password` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否开启密码访问',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问密码，可为空',
  `enable_expire` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否开启过期时间',
  `expire_time` date NULL DEFAULT NULL COMMENT '分享过期时间，可为空',
  `is_expired` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已过期',
  `visit_count` int NOT NULL DEFAULT 0 COMMENT '访问次数',
  `download_count` int NOT NULL DEFAULT 0 COMMENT '下载次数',
  `create_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新者id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件/空间分享记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_share
-- ----------------------------

-- ----------------------------
-- Table structure for file_sharing_association
-- ----------------------------
DROP TABLE IF EXISTS `file_sharing_association`;
CREATE TABLE `file_sharing_association`  (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `share_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联的分享记录ID',
  `file_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联的文件ID，可为空，分享空间时为空',
  `space_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联的空间ID，可为空，分享文件时为空',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_share`(`share_id` ASC) USING BTREE,
  INDEX `fk_file`(`file_id` ASC) USING BTREE,
  INDEX `fk_space`(`space_id` ASC) USING BTREE,
  CONSTRAINT `fk_file` FOREIGN KEY (`file_id`) REFERENCES `file_library` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_share` FOREIGN KEY (`share_id`) REFERENCES `file_share` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_space` FOREIGN KEY (`space_id`) REFERENCES `file_space` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件/空间分享关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_sharing_association
-- ----------------------------

-- ----------------------------
-- Table structure for file_space
-- ----------------------------
DROP TABLE IF EXISTS `file_space`;
CREATE TABLE `file_space`  (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '空间名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '个人空间' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_space
-- ----------------------------

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限id',
  `mark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限标识',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_mark`(`mark` ASC) USING BTREE COMMENT '权限标识唯一'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('01KFYTD61FFAA4W1VCYCAW1W5X', 'basic_permissions', '基础权限', '2026-01-27 12:11:52', '2026-01-27 12:11:52');
INSERT INTO `permission` VALUES ('01KFYTD61MSA1W9236WWQJKWH6', 'management_role', '管理角色权限', '2026-01-27 12:11:52', '2026-01-27 12:11:52');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色id',
  `mark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色标识',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `type` tinyint NOT NULL DEFAULT 1 COMMENT '角色类型 0 内置角色 1 自定义角色',
  `create_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_mark`(`mark` ASC) USING BTREE COMMENT '角色标识唯一'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('01KFYTD6DD5Z5Z04EF2VWG5GRM', 'system_administrator', '系统管理员', '系统管理员角色,负责系统的维护使用', 0, '', '2026-01-27 12:11:52', '', '2026-01-27 12:11:52');
INSERT INTO `role` VALUES ('01KFYTD6DQHP9V6VDWESRJJGS3', 'ordinary_users', '普通用户', '系统普通用户', 0, '', '2026-01-27 12:11:52', '', '2026-01-27 12:11:52');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限角色id',
  `role_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色id',
  `permission_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fx_authority`(`permission_id` ASC) USING BTREE,
  INDEX `fx_role`(`role_id` ASC) USING BTREE,
  CONSTRAINT `fx_permission` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fx_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('01KFYTD6DEV4S712DBN131B5ZS', '01KFYTD6DD5Z5Z04EF2VWG5GRM', '01KFYTD61FFAA4W1VCYCAW1W5X');
INSERT INTO `role_permission` VALUES ('01KFYTD6DP3P8CZCKKKP9QKFPS', '01KFYTD6DD5Z5Z04EF2VWG5GRM', '01KFYTD61MSA1W9236WWQJKWH6');
INSERT INTO `role_permission` VALUES ('01KFYTD6DQ5S33D61FEDG0CMEC', '01KFYTD6DQHP9V6VDWESRJJGS3', '01KFYTD61FFAA4W1VCYCAW1W5X');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `account_non_locked` tinyint NOT NULL DEFAULT 1 COMMENT '是否未锁定 0 锁定 1 解锁',
  `locked_time` datetime NULL DEFAULT NULL COMMENT '锁定时间',
  `enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用 0 禁用 1 启用',
  `create_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE COMMENT '用户名唯一',
  INDEX `id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('01KCTPBE8FFZJD49P0BF3WR6G5', 'yangyi', '杨依', '15715822290', 'yi200822@163.com', 1, NULL, 1, '', '2025-12-19 14:56:58', '', '2026-01-27 16:32:04');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份角色关联id',
  `user_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份id',
  `role_id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_role`(`role_id` ASC) USING BTREE,
  INDEX `fk_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '身份角色关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
