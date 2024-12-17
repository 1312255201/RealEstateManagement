/*
 Navicat Premium Data Transfer

 Source Server         : 本地数据库
 Source Server Type    : MySQL
 Source Server Version : 50726 (5.7.26-log)
 Source Host           : localhost:3306
 Source Schema         : realestate

 Target Server Type    : MySQL
 Target Server Version : 50726 (5.7.26-log)
 File Encoding         : 65001

 Date: 17/12/2024 16:32:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for email_config
-- ----------------------------
DROP TABLE IF EXISTS `email_config`;
CREATE TABLE `email_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标识，主键',
  `email_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送邮箱地址',
  `email_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱的验证码',
  `smtp_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'smtp.qq.com' COMMENT 'smtp服务器地址',
  `smtp_port` int(11) NOT NULL DEFAULT 465 COMMENT 'smtp服务器端口',
  `is_ssl_enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否开启ssl安全验证',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '表设计人软件22-7班王赟昊\r\n用于存储发送重置邮件的stmp授权密码，以及邮箱' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id，用于唯一表名用户id',
  `role` enum('user','merchant','admin') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'user' COMMENT '用户角色，枚举类型，user用户merchant房产商admin管理员',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '请补全个人信息' COMMENT '实名认证信息，本人真名',
  `idnumber` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '请补全个人信息' COMMENT '实名认证信息，身份证号',
  `phonenumber` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱地址，绑定账号，用于找回',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码，加密存储',
  `finish_info` enum('yes','no') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'no' COMMENT '是否完成实名认证标识',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建账户时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phonenumber`(`phonenumber`) USING BTREE COMMENT '用于确保没有两个人电话一样，顺便加速查询速度，使用BTREE实现唯一索引',
  UNIQUE INDEX `email`(`email`) USING BTREE COMMENT '用于确保没有两个人邮箱一样，顺便加速查询速度，使用BTREE实现唯一索引',
  UNIQUE INDEX `idnumber`(`idnumber`) USING BTREE COMMENT '用于确保没有两个人身份证号一样，顺便加速查询速度，使用BTREE实现唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目全部用户储存所在的表\r\n——王赟昊' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
