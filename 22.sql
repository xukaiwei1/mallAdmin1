/*
Navicat MySQL Data Transfer

Source Server         : mall
Source Server Version : 50724
Source Host           : 39.105.229.235:3306
Source Database       : mall

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-01-20 19:34:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `ml_muser`
-- ----------------------------
DROP TABLE IF EXISTS `ml_muser`;
CREATE TABLE `ml_muser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pin` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `real_name` varchar(255) DEFAULT NULL,
  `activate_status` tinyint(4) DEFAULT NULL COMMENT '是否已经激活 1true 0false',
  `activated` datetime DEFAULT NULL COMMENT '激活认证时间',
  `phone` varchar(50) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL COMMENT '是否超级管理员1为true0为false',
  `is_admin` tinyint(4) DEFAULT NULL,
  `current_mall_id` int(11) DEFAULT NULL COMMENT '当前的teamID',
  `first_login_time` datetime DEFAULT NULL COMMENT '第一次登陆时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近一次登陆时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人erp',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改人erp',
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT NULL COMMENT '删除标记 0：未删除 1：已删除',
  `pinyin_name` varchar(255) DEFAULT NULL COMMENT 'username的拼音',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pin_unique` (`pin`),
  KEY `phone_select` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickName` varchar(50) NOT NULL COMMENT '昵称',
  `real_name` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `userName` varchar(150) NOT NULL COMMENT '用户名',
  `password` varchar(150) NOT NULL COMMENT '密码',
  `salt` varchar(150) NOT NULL COMMENT '盐值',
  `activate_status` tinyint(4) DEFAULT NULL COMMENT '是否已经激活 1true 0false',
  `activated` datetime DEFAULT NULL COMMENT '激活认证时间',
  `phone` varchar(50) DEFAULT NULL COMMENT '手机号',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `is_admin` tinyint(4) DEFAULT NULL COMMENT '是否超级管理员1为true0为false',
  `current_mall_id` int(11) DEFAULT NULL COMMENT '当前的mall ID',
  `first_login_time` datetime DEFAULT NULL COMMENT '第一次登陆时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近一次登陆时间',
  `createAt` datetime NOT NULL COMMENT '创建时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改人',
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT NULL COMMENT '删除标记 0：未删除 1：已删除',
  `pinyin_name` varchar(255) DEFAULT NULL COMMENT 'username的拼音',
  `status` int(11) NOT NULL COMMENT '状态',
  `ip` varchar(100) DEFAULT NULL COMMENT 'ip',
  `avatar` varchar(100) NOT NULL COMMENT '头像',
  `likeCount` int(11) NOT NULL DEFAULT '0' COMMENT '被赞次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

CREATE TABLE `ml_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pin` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
   `weixin_name` varchar(255) DEFAULT NULL COMMENT '微信号',
  `real_name` varchar(255) DEFAULT NULL,
  `activate_status` tinyint(4) DEFAULT NULL COMMENT '是否已经激活 1true 0false',
  `activated` datetime DEFAULT NULL COMMENT '激活认证时间',
  `phone` varchar(50) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL COMMENT '是否超级管理员1为true0为false',
  `current_mall_id` int(11) DEFAULT NULL COMMENT '当前的 mall ID',
  `first_login_time` datetime DEFAULT NULL COMMENT '第一次登陆时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近一次登陆时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人erp',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改人erp',
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT NULL COMMENT '删除标记 0：未删除 1：已删除',
  `pinyin_name` varchar(255) DEFAULT NULL COMMENT 'username的拼音',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pin_unique` (`pin`),
  KEY `phone_select` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `ml_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `resource_name` varchar(100) DEFAULT NULL COMMENT '资源名字',
  `resource_type` int(4) NOT NULL COMMENT '资源类型 0：商城banner',
  `resource_value` varchar(255) DEFAULT NULL COMMENT '资源值',
   `parent_resource` int(11) DEFAULT NULL COMMENT '父资源',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改人',
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT NULL COMMENT '删除标记 0：未删除 1：已删除',
  `current_mall_id` int(11) DEFAULT NULL COMMENT '当前的 mall ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT '资源表';


CREATE TABLE `ml_goods_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `type_name` varchar(100) DEFAULT NULL COMMENT '类型名字',
  `type_code` int(5) NOT NULL AUTO_INCREMENT COMMENT '类型码',
   `parent_type` int(11) DEFAULT NULL COMMENT '父类型',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改人',
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT NULL COMMENT '删除标记 0：未删除 1：已删除',
  `current_mall_id` int(11) DEFAULT NULL COMMENT '当前的 mall ID',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT '商品类型';


CREATE TABLE `ml_coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `coupon_type` int(3) DEFAULT NULL COMMENT '优惠券类型',
  `coupon_amount` int(11) NOT NULL COMMENT '优惠金额',
   `name` varchar(10) DEFAULT NULL COMMENT '优惠券名字',
   `use_amount` int(11) DEFAULT NULL COMMENT '消费达到多少金额可以使用',
   `effective_days` int(11) DEFAULT NULL COMMENT '领取之后有效天数',
   `coupon_number` int(5) DEFAULT NULL COMMENT '优惠券数量',
   `per_receive_number` int(3) DEFAULT NULL COMMENT '每个人限领取优惠券数量',
   `expire_date` datetime DEFAULT NULL COMMENT '显示到小程序的到期时间',
   `target_type` int(2) DEFAULT NULL COMMENT '优惠对象类型',
   `target_number` varchar (20) DEFAULT NULL COMMENT '优惠对象类型值',
   `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改人',
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT NULL COMMENT '删除标记 0：未删除 1：已删除',
  `current_mall_id` int(11) DEFAULT NULL COMMENT '当前的 mall ID',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT '优惠券';


CREATE TABLE `ml_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `addressxy` varchar(10) DEFAULT NULL COMMENT '地址坐标信息',
  `is_default` tinyint(4) NOT NULL COMMENT '是否是默认地址 0:是  1:否',
   `receive_address` varchar(50) DEFAULT NULL COMMENT '详细地址',
   `telphone` varchar(11) DEFAULT NULL COMMENT '手机号',
   `name` varchar(10) DEFAULT NULL COMMENT '收货人姓名',
   `zipcode` varchar(10) DEFAULT NULL COMMENT '邮编',
   `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改人',
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT NULL COMMENT '删除标记 0：未删除 1：已删除',
  `current_mall_id` int(11) DEFAULT NULL COMMENT '当前的 mall ID',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT '收货地址';



CREATE TABLE `ml_goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `goods_code` varchar(10) DEFAULT NULL COMMENT '商品编码',
   `goods_type_id` int(11)  NOT NULL COMMENT '商品类型ID',
   `goods_name` varchar(20) DEFAULT NULL COMMENT '商品名称',
   `goods_introduce` varchar(50) DEFAULT NULL COMMENT '商品介绍',
   `name` varchar(10) DEFAULT NULL COMMENT '收货人姓名',
   `zipcode` varchar(10) DEFAULT NULL COMMENT '邮编',
   `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) DEFAULT NULL COMMENT '修改人',
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT NULL COMMENT '删除标记 0：未删除 1：已删除',
  `current_mall_id` int(11) DEFAULT NULL COMMENT '当前的 mall ID',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT '商品表';






