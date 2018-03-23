-- MySQL dump 10.13  Distrib 5.6.24, for Win32 (x86)
--
-- Host: 122.225.71.66    Database: remote_meter_vlinker
-- ------------------------------------------------------
-- Server version	5.5.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `operator`
--

LOCK TABLES `meter_admin` WRITE;
/*!40000 ALTER TABLE `meter_admin` DISABLE KEYS */;
INSERT INTO `meter_admin` VALUES (1,'admin','administrator','88888888888','','ISMvKXpXpadDiUoOSoAfww==','管理员','2015/12/14 上午10:05:08','1','2016.08.24 14:19:01',1);
/*!40000 ALTER TABLE `meter_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'管理员','recharge,rechargeList,Cb,CbByCommuity,CbByTiming,CbByCycle,selectedCb,ForcedOpeningVavle,ForcedClosingVavle,AutomaticControlVavle,readList,reminderList,mbusManage,userManage,locationManage,addressManage,priceManage,dataAnalyze,rechargeAnalyze,administratorManage,authorityManagement,accountInformation','2016.07.13 10:43:30'),(2,'营业员','recharge,rechargeList,Cb,CbByCommuity,readList,reminderList,mbusManage,userManage,locationManage,addressManage','2016.07.13 10:43:30');
/*!40000 ALTER TABLE `role` ENABLE KEYS*/;
UNLOCK TABLES;

--
-- Dumping data for table `read_parameter`
--

LOCK TABLES `read_parameter` WRITE;
/*!40000 ALTER TABLE `read_parameter` DISABLE KEYS */;
INSERT INTO `read_parameter` VALUES (1,'00','00','00','00','0','0','2016-12-27 08:55:41',NULL,NULL),(2,'00','00','00','00','0','0','2016-12-23 07:56:38',NULL,NULL);
/*!40000 ALTER TABLE `read_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'remote_meter_vlinker'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-08-25  9:46:11

LOCK TABLES `read_info` WRITE;
ALTER TABLE read_info ADD INDEX operate_time_exception_isAutoClear_meter_no_fee_status(operate_time,exception,isAutoClear,meter_no,fee_status);
UNLOCK TABLES;

LOCK TABLES `meter_user` WRITE;
ALTER TABLE meter_user ADD INDEX meter_no_user_status_meter_status_last_balance(meter_no,user_status,meter_status,last_balance);
UNLOCK TABLES;

LOCK TABLES `operate_info` WRITE;
ALTER TABLE operate_info ADD INDEX operate_type_operate_time_recharge_money_community_name(operate_type,operate_time,recharge_money,user_address_community);
UNLOCK TABLES;

LOCK TABLES `concentrator` WRITE;
ALTER TABLE concentrator ADD concentrator_model varchar(20) not null default "2";
UNLOCK TABLES;

alter table role add unique(role_name);

DROP TABLE IF EXISTS `event_record`;
CREATE TABLE `event_record` (
   `event_id` int(11) NOT NULL AUTO_INCREMENT,
   `meter_no` varchar(45) DEFAULT NULL,
   `event` varchar(45) DEFAULT NULL,
   `error_code` varchar(45) DEFAULT NULL,
   `error_msg` varchar(45) DEFAULT NULL,
   `report_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `province` varchar(45) DEFAULT NULL,
   `city` varchar(45) DEFAULT NULL,
   `district` varchar(45) DEFAULT NULL,
   `address` varchar(500) DEFAULT NULL,
   `read_state` int(11) DEFAULT '0',
   PRIMARY KEY (`event_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `read_info_doubtful`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `read_info_doubtful` (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operate_id` varchar(30) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `user_address_area` varchar(50) NOT NULL,
  `user_address_community` varchar(30) NOT NULL,
  `user_address_building` varchar(30) NOT NULL,
  `user_address_unit` varchar(30) NOT NULL,
  `user_address_room` varchar(30) NOT NULL,
  `contact_info` varchar(50) NOT NULL,
  `concentrator_name` varchar(30) NOT NULL,
  `meter_no` varchar(30) NOT NULL,
  `fee_type` varchar(30) NOT NULL,
  `this_read` double DEFAULT NULL,
  `last_read` double DEFAULT NULL,
  `this_cost` double DEFAULT NULL,
  `last_cost` double DEFAULT NULL,
  `fee_need` double DEFAULT NULL,
  `balance` double NOT NULL,
  `exception` varchar(30) NOT NULL,
  `fee_status` varchar(30) NOT NULL,
  `read_type` varchar(30) NOT NULL,
  `operator_account` varchar(50) NOT NULL,
  `operate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_address` varchar(100) DEFAULT NULL,
  `operate_day` varchar(30) DEFAULT NULL,
  `operate_dayTime` varchar(30) DEFAULT NULL,
  `meter_type` varchar(30) NOT NULL,
  `data2` double NOT NULL,
  `data3` double NOT NULL,
  `data4` double NOT NULL,
  `data5` double NOT NULL,
  `data6` double NOT NULL,
  `data7` double NOT NULL,
  `data8` double NOT NULL,
  `data9` varchar(30) NOT NULL,
  `isAutoClear` varchar(10) NOT NULL DEFAULT '',
  PRIMARY KEY (`info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table room_info(
  roomId int primary key auto_increment,
  onlineSyncCode varchar(100) not null unique,
  doorType varchar(10) not null,
  provinceName varchar(50) not null,
  cityName varchar(50) not null,
  cityAreaName varchar(50) not null,
  streetName varchar(50) not null,
  streetAddress varchar(100) not null,
  villageName varchar(100) not null,
  buildingNumber varchar(30) not null,
  unitNumber varchar(30) not null,
  houseNumber varchar(30) not null,
  roomName varchar(30) not null,
  meterNo varchar(100) unique,
  callBackUrl varchar(300) not null,
  addTime timestamp not null default current_timestamp
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table room_info add column callBackStatus smallint;

alter table meter_user add column pay_type int not null default 0;

alter table meter_user add column register_status int not null default 0;

alter table concentrator add column public_key varchar(500) not null default "";

/*!40101 SET character_set_client = @saved_cs_client */;
CREATE TABLE `mother_meter_conf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `meter_no` varchar(45) NOT NULL,
  `meter_type` varchar(45) DEFAULT NULL,
  `allot_type` varchar(45) DEFAULT NULL,
  `add_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `operator_account` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `meter_no_UNIQUE` (`meter_no`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

/* add by yinhf*/
CREATE TABLE `pub_deduction_fee` (
  `pef_bussinessNum` varchar(19) NOT NULL COMMENT '业务编号',
  `pef_userName` varchar(20) DEFAULT NULL COMMENT '用户名称',
  `pef_meterNo` varchar(12) NOT NULL COMMENT '表号',
  `pef_belongPar` varchar(12) DEFAULT NULL COMMENT '所属母表',
  `pef_parType` varchar(1) DEFAULT NULL COMMENT '母表类型',
  `pef_allotType` varchar(1) DEFAULT NULL COMMENT '分摊类型',
  `pef_applyTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上传时间',
  `pef_shareRadio` varchar(20) DEFAULT NULL COMMENT '分摊比例',
  `pef_shareSize` double DEFAULT NULL COMMENT '分摊量：子表需要承担的量',
  `pef_shareFee` double DEFAULT NULL COMMENT '应扣费',
  `pef_refund` double DEFAULT NULL COMMENT '母表公摊退费',
  `pef_Total` double DEFAULT NULL COMMENT '母表用量，指的是母表记录的总量',
  `pef_community` varchar(50) DEFAULT NULL COMMENT '所属小区',
  `pef_buildNo` varchar(20) DEFAULT NULL COMMENT '所属楼栋',
  `pef_unitNo` varchar(20) DEFAULT NULL COMMENT '所属单元',
  `pef_roomNo` varchar(10) DEFAULT NULL COMMENT '所属房间',
  `pef_rateType` varchar(10) DEFAULT NULL COMMENT '费率类型',
  `pef_accountBalance` double DEFAULT NULL COMMENT '账户余额',
  PRIMARY KEY (`pef_bussinessNum`),
  KEY `index_pef_meterNo` (`pef_meterNo`),
  KEY `index_pef_applyTime` (`pef_applyTime`),
  KEY `index_pef_community` (`pef_community`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* 修改*/
alter table meter_user  modify column user_address_original_room varchar(40);
alter table meter_user add unique(user_address_original_room);
alter table meter_user add column initing_data double null;
alter table event_record add column operator_name varchar(20);
alter table event_record add column operator_id varchar(20);
alter table read_info add column supplier_name varchar(40);
alter table meter_user add unique(user_address_original_room);
alter table meter_user add column online_synv_code varchar(45) null,add unique index online_synv_code_unique(online_synv_code asc) ;
alter table meter_user add column expect_status varchar(45) null;