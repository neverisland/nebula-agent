/*
 Navicat Premium Dump SQL

 Source Server         : Mysql-本地
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : 192.168.3.122:3306
 Source Schema         : nebula-agent

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 19/12/2025 16:42:33
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
INSERT INTO `authentication_password` VALUES ('01KCTPWPEA7FXFFQG9Y8154HG2', '01KCTPW8FY8FMHVZYX42M0DVS3', '058e157a3b8674985d74aca776154ca657e9bfb22661d81b573a47c35cf76b4c010423f0282ed37b69474d1088f84bde', 0, '2025-12-19 15:06:24', '2025-12-19 15:06:24');

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
INSERT INTO `file_library` VALUES ('01KCTQ1ZKTEF9BBMATM53ABZ72', '01KCTR3ZGYEKN6D7CHNAZQW61Q', 'image.png', 'image/png', 3944, '2025/12/19/97c03352ee55468fa8ce5d2010115d43.png', '2025/12/19/97c03352ee55468fa8ce5d2010115d43-thumbnail.png', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-19 15:09:17', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-19 16:06:35');
INSERT INTO `file_library` VALUES ('01KCTTGFKKYBX7VBV7JX8GWJJ7', NULL, 'image.png', 'image/png', 15673, '2025/12/19/44ab91025eb14e7f9d0f0d3735d21944.png', '2025/12/19/44ab91025eb14e7f9d0f0d3735d21944-thumbnail.png', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-19 16:09:38', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-19 16:41:34');

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
INSERT INTO `file_space` VALUES ('01KCTR3ZGYEKN6D7CHNAZQW61Q', '图床', '/', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-19 15:27:51', '01KCTPBE8FFZJD49P0BF3WR6G5', '2025-12-19 15:39:22');

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
INSERT INTO `permission` VALUES ('01KCTPABZSEDPM7Z2JRN6RR2AE', 'basic_permissions', '基础权限', '2025-12-19 14:56:23', '2025-12-19 14:56:23');
INSERT INTO `permission` VALUES ('01KCTPABZT7ZS1N1S2DD9KKT56', 'management_role', '管理角色权限', '2025-12-19 14:56:23', '2025-12-19 14:56:23');

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
INSERT INTO `role` VALUES ('01KCTPAC1WGY185XA2QJ5S72AJ', 'system_administrator', '系统管理员', '系统管理员角色,负责系统的维护使用', 0, '', '2025-12-19 14:56:23', '', '2025-12-19 14:56:23');
INSERT INTO `role` VALUES ('01KCTPAC1XDNM6YMCGVQYPYCJ9', 'ordinary_users', '普通用户', '系统普通用户', 0, '', '2025-12-19 14:56:23', '', '2025-12-19 14:56:23');

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
INSERT INTO `role_permission` VALUES ('01KCTPAC1X9VAV75JGWVARVPED', '01KCTPAC1XDNM6YMCGVQYPYCJ9', '01KCTPABZSEDPM7Z2JRN6RR2AE');
INSERT INTO `role_permission` VALUES ('01KCTPAC1XKWZJE0T8EBFEQ3FH', '01KCTPAC1WGY185XA2QJ5S72AJ', '01KCTPABZSEDPM7Z2JRN6RR2AE');
INSERT INTO `role_permission` VALUES ('01KCTPAC1XNFDYBHHYM0FTDEQN', '01KCTPAC1WGY185XA2QJ5S72AJ', '01KCTPABZT7ZS1N1S2DD9KKT56');

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
INSERT INTO `user` VALUES ('01KCTPBE8FFZJD49P0BF3WR6G5', 'yangyi', '杨依', '15715822290', '1216288809@qq.com', 1, NULL, 1, '', '2025-12-19 14:56:58', '', '2025-12-19 14:56:58');

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
INSERT INTO `user_role` VALUES ('01KCTPBE93RA2MG58XK8M8HMJP', '01KCTPBE8FFZJD49P0BF3WR6G5', '01KCTPAC1WGY185XA2QJ5S72AJ');

SET FOREIGN_KEY_CHECKS = 1;
