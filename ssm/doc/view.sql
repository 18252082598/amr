CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `root`@`%` 
    SQL SECURITY DEFINER
VIEW `meter_user_operate` AS
    SELECT 
        `a`.`supplier_name` AS `supplier_name`,
        `b`.`operate_id` AS `operate_id`,
        `b`.`user_name` AS `user_name`,
        `b`.`user_address_area` AS `user_address_area`,
        `b`.`user_address_community` AS `user_address_community`,
        `b`.`user_address_building` AS `user_address_building`,
        `b`.`user_address_unit` AS `user_address_unit`,
        `b`.`user_address_room` AS `user_address_room`,
        `b`.`contact_info` AS `contact_info`,
        `b`.`concentrator_name` AS `concentrator_name`,
        `b`.`meter_type` AS `meter_type`,
        `b`.`meter_no` AS `meter_no`,
        `b`.`fee_type` AS `fee_type`,
        `b`.`balance` AS `balance`,
        `b`.`operator_account` AS `operator_account`,
        `b`.`operate_time` AS `operate_time`,
        `b`.`recharge_money` AS `recharge_money`,
        `b`.`recharge_amount_kwh` AS `recharge_amount_kwh`,
        `b`.`recharge_amount_m3` AS `recharge_amount_m3`,
        `b`.`operate_type` AS `operate_type`,
        `b`.`recharge_loc` AS `recharge_loc`,
        `b`.`meter_model` AS `meter_model`,
        `b`.`pay_method` AS `pay_method`,
        `b`.`isPrint` AS `isPrint`,
        `b`.`province` AS `province`,
        `b`.`city` AS `city`,
        `b`.`district` AS `district`
    FROM `meter_user` `a` JOIN `operate_info` `b`
   WHERE `a`.`meter_no` = `b`.`meter_no`;
		
		
CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `root`@`%` 
    SQL SECURITY DEFINER
VIEW `meter_user_read` AS
    SELECT 
        `a`.`supplier_name` AS `supplier_name`,
        `b`.`info_id` AS `info_id`,
        `b`.`operate_id` AS `operate_id`,
        `b`.`user_name` AS `user_name`,
        `b`.`user_address_area` AS `user_address_area`,
        `b`.`user_address_community` AS `user_address_community`,
        `b`.`user_address_building` AS `user_address_building`,
        `b`.`user_address_unit` AS `user_address_unit`,
        `b`.`user_address_room` AS `user_address_room`,
        `b`.`contact_info` AS `contact_info`,
        `b`.`concentrator_name` AS `concentrator_name`,
        `b`.`meter_no` AS `meter_no`,
        `b`.`fee_type` AS `fee_type`,
        `b`.`this_read` AS `this_read`,
        `b`.`last_read` AS `last_read`,
        `b`.`this_cost` AS `this_cost`,
        `b`.`last_cost` AS `last_cost`,
        `b`.`fee_need` AS `fee_need`,
        `b`.`balance` AS `balance`,
        `b`.`exception` AS `exception`,
        `b`.`fee_status` AS `fee_status`,
        `b`.`read_type` AS `read_type`,
        `b`.`operator_account` AS `operator_account`,
        `b`.`operate_time` AS `operate_time`,
        `b`.`pay_remind` AS `pay_remind`,
        `b`.`user_address` AS `user_address`,
        `b`.`operate_day` AS `operate_day`,
        `b`.`operate_dayTime` AS `operate_dayTime`,
        `b`.`meter_type` AS `meter_type`,
        `b`.`data2` AS `data2`,
        `b`.`data3` AS `data3`,
        `b`.`data4` AS `data4`,
        `b`.`data5` AS `data5`,
        `b`.`data6` AS `data6`,
        `b`.`data7` AS `data7`,
        `b`.`data8` AS `data8`,
        `b`.`data9` AS `data9`,
        `b`.`isAutoClear` AS `isAutoClear`,
        `b`.`community_no` AS `community_no`
    FROM `meter_user` `a` JOIN `read_info` `b`
   WHERE `a`.`meter_no` = `b`.`meter_no`;
		
CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `root`@`%` 
    SQL SECURITY DEFINER
VIEW `meter_user_read_doubtful` AS
    SELECT 
        `a`.`supplier_name` AS `supplier_name`,
        `b`.`info_id` AS `info_id`,
        `b`.`operate_id` AS `operate_id`,
        `b`.`user_name` AS `user_name`,
        `b`.`user_address_area` AS `user_address_area`,
        `b`.`user_address_community` AS `user_address_community`,
        `b`.`user_address_building` AS `user_address_building`,
        `b`.`user_address_unit` AS `user_address_unit`,
        `b`.`user_address_room` AS `user_address_room`,
        `b`.`contact_info` AS `contact_info`,
        `b`.`concentrator_name` AS `concentrator_name`,
        `b`.`meter_no` AS `meter_no`,
        `b`.`fee_type` AS `fee_type`,
        `b`.`this_read` AS `this_read`,
        `b`.`last_read` AS `last_read`,
        `b`.`this_cost` AS `this_cost`,
        `b`.`last_cost` AS `last_cost`,
        `b`.`fee_need` AS `fee_need`,
        `b`.`balance` AS `balance`,
        `b`.`exception` AS `exception`,
        `b`.`fee_status` AS `fee_status`,
        `b`.`read_type` AS `read_type`,
        `b`.`operator_account` AS `operator_account`,
        `b`.`operate_time` AS `operate_time`,
        `b`.`user_address` AS `user_address`,
        `b`.`operate_day` AS `operate_day`,
        `b`.`operate_dayTime` AS `operate_dayTime`,
        `b`.`meter_type` AS `meter_type`,
        `b`.`data2` AS `data2`,
        `b`.`data3` AS `data3`,
        `b`.`data4` AS `data4`,
        `b`.`data5` AS `data5`,
        `b`.`data6` AS `data6`,
        `b`.`data7` AS `data7`,
        `b`.`data8` AS `data8`,
        `b`.`data9` AS `data9`,
        `b`.`isAutoClear` AS `isAutoClear`
    FROM `meter_user` `a` JOIN `read_info_doubtful` `b`
   WHERE `a`.`meter_no` = `b`.`meter_no`;
		
CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `root`@`%` 
    SQL SECURITY DEFINER
VIEW `meter_user_read_fail` AS
    SELECT 
        `a`.`supplier_name` AS `supplier_name`,
        `b`.`info_id` AS `info_id`,
        `b`.`operate_id` AS `operate_id`,
        `b`.`user_name` AS `user_name`,
        `b`.`user_address_area` AS `user_address_area`,
        `b`.`user_address_community` AS `user_address_community`,
        `b`.`user_address_building` AS `user_address_building`,
        `b`.`user_address_unit` AS `user_address_unit`,
        `b`.`user_address_room` AS `user_address_room`,
        `b`.`contact_info` AS `contact_info`,
        `b`.`concentrator_name` AS `concentrator_name`,
        `b`.`meter_no` AS `meter_no`,
        `b`.`fee_type` AS `fee_type`,
        `b`.`this_read` AS `this_read`,
        `b`.`last_read` AS `last_read`,
        `b`.`this_cost` AS `this_cost`,
        `b`.`last_cost` AS `last_cost`,
        `b`.`fee_need` AS `fee_need`,
        `b`.`balance` AS `balance`,
        `b`.`exception` AS `exception`,
        `b`.`fee_status` AS `fee_status`,
        `b`.`read_type` AS `read_type`,
        `b`.`operator_account` AS `operator_account`,
        `b`.`operate_time` AS `operate_time`,
        `b`.`user_address` AS `user_address`,
        `b`.`operate_day` AS `operate_day`,
        `b`.`operate_dayTime` AS `operate_dayTime`,
        `b`.`meter_type` AS `meter_type`,
        `b`.`data2` AS `data2`,
        `b`.`data3` AS `data3`,
        `b`.`data4` AS `data4`,
        `b`.`data5` AS `data5`,
        `b`.`data6` AS `data6`,
        `b`.`data7` AS `data7`,
        `b`.`data8` AS `data8`,
        `b`.`data9` AS `data9`,
        `b`.`isAutoClear` AS `isAutoClear`
    FROM `meter_user` `a` JOIN `read_info_fail` `b`
   WHERE `a`.`meter_no` = `b`.`meter_no`;