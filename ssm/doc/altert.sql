ALTER TABLE `read_info` 
CHANGE COLUMN `exception` `exception` VARCHAR(5) NOT NULL ,
CHANGE COLUMN `meter_type` `meter_type` VARCHAR(5) NOT NULL ,
CHANGE COLUMN `isAutoClear` `isAutoClear` VARCHAR(5) NOT NULL DEFAULT '' ;

ALTER TABLE `read_info` 
DROP INDEX `operate_time_exception_isAutoClear_meter_no_fee_status` ,
ADD INDEX `index_operate_time_exception_isAutoClear_meter_no` (`operate_time` ASC, `exception` ASC, `isAutoClear` ASC, `meter_no` ASC);

ALTER TABLE `read_info` 
ADD INDEX `index_meterno` (`meter_no` DESC);