package com.joymeter.service;

import com.joymeter.entity.Result;
import java.util.Calendar;

public interface WeChatService {
    // 根据账号获取能耗
    public Result getCostPowerByIdCardNo(String id_card_no, String startDate, String endDate, String key);

    // 根据账号获取水表的开关阀状态 0关闭 1开启
    public Result getValueStatuesByIdCardNo(String id_card_no);

    // 根据表号获取表的状态 0关闭 1开启
    public Result getValueStatusByMeterNo(String meter_no);

    // 根据表号进行充值
    public Result rechargeByMeterNo(final String operateId, String meterNo, String time, Double rechargeMoney,
            String style);

    // 根据表号查询每天的能耗
    public Result queryCostPowerPerDayByMeterNo(String meterNo, String startDate, String endDate);

    // 根据表号查询每周的能耗
    public Result queryCostPowerPerWeekByMeterNo(String meterNo, String startDate, String endDate);

    // 根据表号查询每月的能耗
    public Result queryCostPowerPerMonthByMeterNo(String meterNo, String startDate, String endDate);

    // 根据表号查询出费率类型计算出价格
    public Result queryMoneyByFeeTypeAndMeterNo(String meter_no, Double amount);

    // 根据表号，价格计算出对应充值数量
    public Result queryAmountByPriceAndMeterNo(String meter_no, Double price);


    
    public void scheduler(Calendar cal);

}
