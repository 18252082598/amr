package com.joymeter.service;

import com.joymeter.entity.OperateInfo;
import com.joymeter.entity.ParentMeterConf;
import com.joymeter.entity.Result;
import javax.servlet.http.HttpServletRequest;

public interface RechargeService {

    public Result loadAllOperators();

    public Result findUser(String user_name, String user_address_community,
            String user_address_building, String user_address_unit, String user_address_room,
            String operator_name, String contact_info, String meter_no,
            String status, int startNo, int pageSize, HttpServletRequest request);
    
    public Result findMotherMeterConf(String meter_no, int startNo, int pageSize);

    public Result payOrRefund(OperateInfo operateInfo, String method);
    
    public Result payOrRefund2(OperateInfo operateInfo, String method);//原有

    public Result loadOperateInfo(String user_name, int startNo, int pageSize);

    public Result loadOperateInfoByMeterNo(String meter_no, int startNo, int pageSize);

    public Result refund(OperateInfo operateInfo);

    public Result findOperateInfo(String userName, String community, String building,
            String unit, String room, String operatorName, String meterNo, int startNo, int pageSize, HttpServletRequest request);

    public Result rechargeByMeterNo(final String operateId, String meterNo, Double rechargeMoney, final String operator);

    public Result rechargeByMeterNo(String meterNo, Double rechargeMoney, final String operator);

    public Result refundByMeterNo(String meterNo, Double refundMoney, final String operator);

    public Result refundByMeterNo(String meterNo, final String operator);

    public Result getRechargeInfo(String meterNo, String startTime, String endTime);
    
    public Result queryRechargeInfoByRoomId(String onlineSynvCode, String operate_type, int startNo, int pageSize);
  //加载所有的操作类型
    public Result loadOperationMethod();
    
    public Result zuoQuanFindOperateInfo(String user_name, String community, String building, String unit, String room,
            String operator_name, String meter_no, int startNo, int pageSize, String payMethod, String operationMethod,
            String startTime, String endTime, HttpServletRequest request);
    
    
  
    
    
    
}
