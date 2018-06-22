SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `tbl_user`;
CREATE TABLE `tbl_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长id',
  `username` varchar(255) NOT NULL COMMENT '用户登录账户-唯一性约束',
  `password` varchar(255) NOT NULL COMMENT '密码-MD5存储',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `sex` int(1) DEFAULT NULL COMMENT '性别',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号-唯一性约束',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱-唯一性约束',
  `register_date` datetime NOT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `tbl_order`;
CREATE TABLE  `tbl_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长id',
  `orderId` varchar(255) NOT NULL COMMENT '商户订单id',
  `transactionId` varchar(255) NOT NULL COMMENT '支付平台订单id',
  `userId` varchar(255) NOT NULL COMMENT '用户id',
  `payType` varchar(255) NOT NULL COMMENT '支付标识',
  `tradeState` int(1) DEFAULT NULL COMMENT '交易状态',
  `time_create` datetime DEFAULT NULL COMMENT '订单创建时间',
  `fee` float(50) DEFAULT NULL COMMENT '订单金额',
  `refundFee` float(50) DEFAULT NULL COMMENT '退款金额',
  `time_payment` datetime DEFAULT NULL COMMENT '支付完成时间',
  `orderRefundId` varchar(255) DEFAULT NULL COMMENT '退款商户订单号',
  `refundId` varchar(255) DEFAULT NULL COMMENT '支付平台退款订单号',
  `time_refund` datetime DEFAULT NULL COMMENT '退款完成时间',
  `body` varchar(255) DEFAULT NULL COMMENT '商品描述',
  `detail` varchar(255) DEFAULT NULL COMMENT '商品详情',
  `attach` varchar(255) DEFAULT NULL COMMENT '附加数据',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for tbl_bookmark_classify(书签分类表)
-- ----------------------------
DROP TABLE  IF EXISTS `tbl_bookmark_classify`;
CREATE TABLE `tbl_bookmark_classify` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键-自增长id',
  `name` varchar(255) NOT NULL COMMENT '分类-名称',
  `time_create` datetime NOT NULL COMMENT '分类-创建日期',
  `userid` int(11) NOT NULL COMMENT '分类-用户id-外键',
   UNIQUE KEY `classify_name` (`name`,`userid`), 
   FOREIGN KEY (userid) REFERENCES tbl_user(id),
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bookmark(书签表)
-- ----------------------------
DROP TABLE IF EXISTS `tbl_bookmark`;
CREATE TABLE `tbl_bookmark` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长id',
  `name` varchar(255) NOT NULL COMMENT '书签-名称',
  `url` varchar(255) NOT NULL COMMENT '书签-URL',
  `describe` varchar(255) DEFAULT NULL COMMENT '书签-描述',
  `time_create` datetime NOT NULL COMMENT '书签-创建日期',
  `userid` int(11) DEFAULT NULL COMMENT '书签-用户id-外键',
  `classifyid` int(11) DEFAULT NULL COMMENT '书签-分类id-外键',
  FOREIGN KEY (userid) REFERENCES tbl_user(id),
  FOREIGN KEY (classifyid) REFERENCES tbl_bookmark_classify(id),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for tbl_sockets(socket 连接表)
-- ----------------------------
DROP TABLE IF EXISTS `tbl_sockets`;
CREATE TABLE `tbl_sockets` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长id',
  `socket_ip` varchar(255) NOT NULL COMMENT '客户端ip地址',
  `socket_online_time` datetime NOT NULL COMMENT '客户端上线时间',
  `socket_offline_time` datetime DEFAULT NULL COMMENT '客户端下线时间',
  `socket_state` int(1) NOT NULL COMMENT 'socket连接状态-1:上线  0:离线',
  `userid` int(11) DEFAULT NULL COMMENT 'socket-用户id-外键',
  FOREIGN KEY (userid) REFERENCES tbl_user(id),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 添加唯一性约束条件
ALTER TABLE `tbl_sockets` ADD unique(`socket_ip`);
-- ----------------------------
-- Table structure for tbl_sockets_messages(socket消息记录表)
-- ----------------------------
DROP TABLE IF EXISTS `tbl_sockets_messages`;
CREATE TABLE `tbl_sockets_messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长id',
  `socket_data` varchar(255) NOT NULL COMMENT '消息文本',
  `socket_time` datetime NOT NULL COMMENT '消息接收： 0,发送时间：1',
  `socket_type` int(1) NOT NULL COMMENT '消息类型 0：接收1：发送',
  `socket_id` varchar(255) DEFAULT NULL COMMENT '客户端socket唯一标识：外键',
  `userid` int(11) DEFAULT NULL COMMENT '消息-用户id-外键',
  FOREIGN KEY (userid) REFERENCES tbl_user(id),
  FOREIGN KEY (socket_id) REFERENCES tbl_sockets(socket_ip),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

