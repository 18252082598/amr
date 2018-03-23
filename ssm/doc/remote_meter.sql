-- MySQL dump 10.13  Distrib 5.5.28, for Win64 (x86)
--
-- Host: localhost    Database: remote_meter
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
-- Table structure for table `building`
--

DROP TABLE IF EXISTS `building`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `building` (
  `building_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `province` varchar(30) NOT NULL,
  `city` varchar(30) NOT NULL,
  `district` varchar(30) NOT NULL,
  `community_name` varchar(50) NOT NULL,
  `building_name` varchar(50) NOT NULL,
  `operator_account` varchar(50) NOT NULL,
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`building_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `community`
--

DROP TABLE IF EXISTS `community`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `community` (
  `community_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `province` varchar(30) NOT NULL,
  `city` varchar(30) NOT NULL,
  `district` varchar(30) NOT NULL,
  `community_name` varchar(50) NOT NULL,
  `operator_account` varchar(50) NOT NULL,
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `community_no` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`community_id`),
  UNIQUE KEY `community_name` (`community_name`),
  UNIQUE KEY `community_no` (`community_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `concentrator`
--

DROP TABLE IF EXISTS `concentrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concentrator` (
  `concentrator_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `concentrator_name` varchar(30) NOT NULL,
  `DTU_sim_no` varchar(30) NOT NULL,
  `user_address_community` varchar(30) NOT NULL,
  `user_address_building` varchar(30) NOT NULL,
  `user_address_unit` varchar(30) NOT NULL,
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `operator_account` varchar(50) NOT NULL,
  `concentrator_state` varchar(30) NOT NULL DEFAULT '0',
  `concentrator_no` varchar(50) NOT NULL,
  `concentrator_ip` varchar(30) NOT NULL,
  `concentrator_port` varchar(30) NOT NULL,
  `gateway_id` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`concentrator_id`),
  UNIQUE KEY `concentrator_name` (`concentrator_name`,`DTU_sim_no`),
  UNIQUE KEY `concentrator_name_1` (`concentrator_name`,`DTU_sim_no`,`concentrator_no`),
  UNIQUE KEY `gateway_id` (`gateway_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fee_type`
--

DROP TABLE IF EXISTS `fee_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fee_type` (
  `feeTypeId` bigint(20) NOT NULL AUTO_INCREMENT,
  `feeTypeName` varchar(50) NOT NULL,
  `meterType` varchar(50) NOT NULL,
  `basicUnitPrice` double NOT NULL,
  `disposingUnitCost` double NOT NULL,
  `otherCost` double NOT NULL,
  `totalUnitCost` double NOT NULL,
  `extraCost` double NOT NULL,
  `paymentMethod` varchar(50) NOT NULL,
  `isLevelPrice` varchar(50) NOT NULL,
  `levelOneStartVolume` double DEFAULT NULL,
  `levelOneUnitPrice` double DEFAULT NULL,
  `levelOneTotalPrice` double DEFAULT NULL,
  `levelTwoStartVolume` double DEFAULT NULL,
  `levelTwoUnitPrice` double DEFAULT NULL,
  `levelTwoTotalPrice` double DEFAULT NULL,
  `levelThreeStartVolume` double DEFAULT NULL,
  `levelThreeUnitPrice` double DEFAULT NULL,
  `levelThreeTotalPrice` double DEFAULT NULL,
  `levelFourStartVolume` double DEFAULT NULL,
  `levelFourUnitPrice` double DEFAULT NULL,
  `levelFourTotalPrice` double DEFAULT NULL,
  `levelFiveStartVolume` double DEFAULT NULL,
  `levelFiveUnitPrice` double DEFAULT NULL,
  `levelFiveTotalPrice` double DEFAULT NULL,
  `feeTypeStatus` varchar(50) NOT NULL,
  `operatorName` varchar(50) NOT NULL,
  `addTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`feeTypeId`),
  UNIQUE KEY `feeTypeName` (`feeTypeName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `house_coefficient`
--

DROP TABLE IF EXISTS `house_coefficient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `house_coefficient` (
  `coefficient_id` int(10) NOT NULL AUTO_INCREMENT,
  `coefficient_name` varchar(50) NOT NULL,
  `coefficient` double NOT NULL,
  `operator_account` varchar(50) NOT NULL,
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`coefficient_id`),
  UNIQUE KEY `coefficient_name` (`coefficient_name`,`coefficient`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `language`
--

DROP TABLE IF EXISTS `language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `language` (
  `language_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `language_name` varchar(50) NOT NULL,
  PRIMARY KEY (`language_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `meter_admin`
--

DROP TABLE IF EXISTS `meter_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meter_admin` (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_account` varchar(20) DEFAULT NULL,
  `admin_name` varchar(20) DEFAULT NULL,
  `admin_tel` varchar(20) DEFAULT NULL,
  `admin_supplier_name` varchar(20) DEFAULT NULL,
  `admin_password` varchar(50) DEFAULT NULL,
  `admin_power` varchar(80) DEFAULT NULL,
  `admin_add_time` varchar(30) DEFAULT NULL,
  `account_status` varchar(5) DEFAULT NULL,
  `last_load_time` varchar(30) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `meter_info`
--

DROP TABLE IF EXISTS `meter_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meter_info` (
  `meter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `meter_type` varchar(30) NOT NULL,
  `meter_model` varchar(30) NOT NULL,
  `meter_no` varchar(30) NOT NULL,
  `submeter_no` varchar(30) NOT NULL,
  `valve_no` varchar(30) NOT NULL,
  `protocol_type` varchar(30) NOT NULL,
  PRIMARY KEY (`meter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `meter_remove_info`
--

DROP TABLE IF EXISTS `meter_remove_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meter_remove_info` (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `user_address` varchar(200) NOT NULL,
  `contact_info` varchar(50) NOT NULL,
  `info_content` text NOT NULL,
  `operator_account` varchar(50) NOT NULL,
  `operate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `meter_no` varchar(30) NOT NULL,
  PRIMARY KEY (`info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `meter_user`
--

DROP TABLE IF EXISTS `meter_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meter_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `user_address_area` varchar(50) NOT NULL,
  `user_address_community` varchar(30) NOT NULL,
  `user_address_building` varchar(30) NOT NULL,
  `user_address_unit` varchar(30) NOT NULL,
  `user_address_room` varchar(30) NOT NULL,
  `user_address_original_room` varchar(30) DEFAULT NULL,
  `id_card_no` varchar(50) NOT NULL,
  `contact_info` varchar(50) NOT NULL,
  `supplier_name` varchar(50) NOT NULL,
  `house_area` double NOT NULL,
  `coefficient_name` varchar(30) NOT NULL DEFAULT '��',
  `auto_deduction` varchar(30) NOT NULL,
  `concentrator_name` varchar(50) NOT NULL,
  `meter_no` varchar(30) NOT NULL,
  `user_status` varchar(30) NOT NULL,
  `meter_status` varchar(30) NOT NULL,
  `operator_account` varchar(50) NOT NULL,
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `meter_type` varchar(30) NOT NULL,
  `meter_model` varchar(30) NOT NULL,
  `submeter_no` varchar(30) NOT NULL,
  `valve_no` varchar(30) NOT NULL,
  `protocol_type` varchar(50) NOT NULL,
  `fee_type` varchar(30) NOT NULL DEFAULT '������ˮһ��',
  `last_read_time` timestamp NOT NULL DEFAULT '2015-10-10 04:00:00',
  `last_balance` double NOT NULL DEFAULT '0',
  `pay_remind` varchar(20) NOT NULL DEFAULT '0',
  `province` varchar(100) NOT NULL,
  `city` varchar(100) NOT NULL,
  `district` varchar(100) NOT NULL,
  `valve_status` varchar(10) NOT NULL DEFAULT '1',
  `valve_protocol` varchar(100) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `meter_no_UNIQUE` (`meter_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operate_info`
--

DROP TABLE IF EXISTS `operate_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operate_info` (
  `operate_id` varchar(30) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `user_address_area` varchar(50) NOT NULL,
  `user_address_community` varchar(30) NOT NULL,
  `user_address_building` varchar(30) NOT NULL,
  `user_address_unit` varchar(30) NOT NULL,
  `user_address_room` varchar(30) NOT NULL,
  `contact_info` varchar(50) NOT NULL,
  `meter_type` varchar(30) NOT NULL,
  `meter_no` varchar(30) NOT NULL,
  `fee_type` varchar(30) NOT NULL,
  `recharge_money` double NOT NULL,
  `recharge_amount_kwh` double DEFAULT NULL,
  `recharge_amount_m3` double DEFAULT NULL,
  `operate_type` varchar(30) NOT NULL,
  `balance` double NOT NULL,
  `operator_account` varchar(50) NOT NULL,
  `pay_method` varchar(30) NOT NULL,
  `isPrint` varchar(30) NOT NULL,
  `operate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `recharge_loc` varchar(50) NOT NULL,
  `meter_model` varchar(50) NOT NULL,
  `concentrator_name` varchar(50) NOT NULL,
  `province` varchar(100) NOT NULL,
  `city` varchar(100) NOT NULL,
  `district` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operator`
--

DROP TABLE IF EXISTS `operator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operator` (
  `operator_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operator_account` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `operator_name` varchar(50) NOT NULL,
  `contact_info` varchar(50) NOT NULL,
  `operator_email` varchar(50) NOT NULL,
  `operator_authority` text NOT NULL,
  `account_status` varchar(50) NOT NULL,
  `loc_name` varchar(50) NOT NULL,
  PRIMARY KEY (`operator_id`),
  UNIQUE KEY `operator_account` (`operator_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `read_info`
--

DROP TABLE IF EXISTS `read_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `read_info` (
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
  `pay_remind` varchar(4) DEFAULT NULL,
  `user_address` varchar(100) DEFAULT NULL,
  `operate_day` char(20) DEFAULT NULL,
  `operate_dayTime` char(25) DEFAULT NULL,
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
  `community_no` varchar(30) DEFAULT '0',
  PRIMARY KEY (`info_id`),
  KEY `index_read_info` (`operate_time`,`meter_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `read_info_fail`
--

DROP TABLE IF EXISTS `read_info_fail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `read_info_fail` (
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `read_parameter`
--

DROP TABLE IF EXISTS `read_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `read_parameter` (
  `parameter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `day` varchar(30) NOT NULL,
  `hour` varchar(30) NOT NULL,
  `minute` varchar(30) NOT NULL,
  `second` varchar(30) NOT NULL,
  `isAutoRead` varchar(30) NOT NULL,
  `isAutoInform` varchar(30) NOT NULL,
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `balance_warn` double DEFAULT NULL,
  `valve_close` double DEFAULT NULL,
  PRIMARY KEY (`parameter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `record`
--

DROP TABLE IF EXISTS `record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `record` (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `record` text NOT NULL,
  `operator_account` varchar(30) NOT NULL,
  `operate_type` varchar(30) NOT NULL,
  `operate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resource`
--

DROP TABLE IF EXISTS `resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource_name` varchar(100) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `parent_ids` varchar(100) DEFAULT NULL,
  `permission` varchar(100) DEFAULT NULL,
  `available` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_resource_parent_id` (`parent_id`),
  KEY `idx_resource_parent_ids` (`parent_ids`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL,
  `permission` varchar(500) DEFAULT NULL,
  `role_add_time` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `supplier` (
  `supplier_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `supplier_name` varchar(50) NOT NULL,
  `supplier_address` varchar(50) NOT NULL,
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `operator_name` varchar(50) NOT NULL,
  `province` varchar(30) NOT NULL,
  `city` varchar(30) NOT NULL,
  `district` varchar(30) NOT NULL,
  PRIMARY KEY (`supplier_id`),
  UNIQUE KEY `supplier_name` (`supplier_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `syslog`
--

DROP TABLE IF EXISTS `syslog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `syslog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `loginName` varchar(30) DEFAULT NULL,
  `operatingcontent` text,
  `operateDate` varchar(30) DEFAULT NULL,
  `userName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `unit`
--

DROP TABLE IF EXISTS `unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unit` (
  `unit_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `province` varchar(30) NOT NULL,
  `city` varchar(30) NOT NULL,
  `district` varchar(30) NOT NULL,
  `community_name` varchar(50) NOT NULL,
  `building_name` varchar(50) NOT NULL,
  `unit_name` varchar(50) NOT NULL,
  `operator_account` varchar(50) NOT NULL,
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`unit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_manage_info`
--

DROP TABLE IF EXISTS `user_manage_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_manage_info` (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `user_address` varchar(200) NOT NULL,
  `contact_info` varchar(50) NOT NULL,
  `meter_no` varchar(30) NOT NULL,
  `info_content` text NOT NULL,
  `operator_account` varchar(50) NOT NULL,
  `operate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `new_meter_no` varchar(30) DEFAULT NULL,
  `closingCost` bigint(20) DEFAULT NULL,
  `province` varchar(100) NOT NULL,
  `city` varchar(100) NOT NULL,
  `district` varchar(100) NOT NULL,
  PRIMARY KEY (`info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-11-02 11:09:23
