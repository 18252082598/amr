package com.joymeter.service;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import com.joymeter.entity.ReadParameter;
import com.joymeter.entity.Result;

public interface ReadService {

    public Result findUsersToRead(String user_name,
            String user_address_community, String user_address_building,
            String user_address_unit, String user_address_room, String operator_name,
            String contact_info, String meter_no, String status, int startNo,
            int pageSize, HttpServletRequest request);

    public Result sendDevParams(HttpServletRequest req, ArrayList<String[]> list,
            int isAutoClear, String accountName);

    public Result findReadInfo(String userName, String community,
            String building, String unit, String room, String operatorName, String feeTypeName, String meterNo,
            String meterType, String startTime, String endTime, int startNo, int pageSize, String queryTableType, HttpServletRequest request);
   //左权 
    public Result zuoQuanFindReadInfo(String userName, String community,
            String building, String unit, String room, String operatorName, String feeTypeName, String meterNo,
            String meterType, String startTime, String endTime, int startNo, int pageSize,String minBalance,String maxBalance, String queryTableType, HttpServletRequest request);

    public Result ctrValve(HttpServletRequest req, ArrayList<String[]> list,
            String accountName);

    public Result findValveStatus(String sendTime);

    public Result findReadInfoByMonth(String month, String meter_type, int startNo, int pageSize, String queryTableType, HttpServletRequest request);

    public Result sendFailedParams(HttpServletRequest req, ArrayList<String[]> list,
            int isAutoClear, String accountName);

    public Result inputByHand(String meter_no, double this_read,
            String operator_account);

    public Result findInfoByMeterNo(String meter_no);

    public Result findCommunityInfo(String community_address);

    public Result readByCommunity(HttpServletRequest req, String community_name,
            int isAutoClear, String accountName);

    public Result saveReadInfo(String parameters);

    public Result findReadResult(String sendTime);

    public Result findReadResultByOperator(String sendTime, String accountName);

    public Result updateCommunityReadResult(String sendTime, String accountName);

    public Result saveReadParams(ReadParameter readParameter,
            String original_day,
            String original_hour,
            String original_minute,
            Double original_balance_warn,
            Double original_valve_close,
            String original_isAutoRead,
            String original_isAutoInform
    );

    public Result showReadParameter();

    public Result loadUsersRemind(String userName, String community,
            String building, String unit, String room, String operatorName, String meterNo,
            int startNo, int pageSize, HttpServletRequest request);

    public Result markup(String meterNo, String user_name);

    public Result delMarkup(String meterNo, String user_name);

    public Result ctrValveByMeterNo(HttpServletRequest req, String meterNo, String action, final String operator);

    public String ctrValveByUUID(HttpServletRequest req, String action, String uuid);

    public Result ctrValveByMeterNo_Async(HttpServletRequest req, String meterNo, String action, final String operator);

    public Result readByMeterNo(HttpServletRequest req, final String meterNo, final String operator);

    public Result readByMeterNo_Callback(HttpServletRequest req, final String meterNo, final String operator);

    public Result readByMeterNo_Async(HttpServletRequest req, final String meterNo, final String operator);

    public Result readByMeterNo(HttpServletRequest req, final String[] meterNo, final String operator);

    public Result readByMeterNo_Async(HttpServletRequest req, final String[] meterNos, final String operator);

    public Result sendSmsByMeterNo(String meterNo, final String operator);

    public Result findReadInfoByDate(String startTime, String endTime);

    public Result findReadInfoByDatePage(String startTime, String endTime, int page, int pageSize);

    public Result findReadInfoByDateWithoutTotalNo(String startDateTime, String endDateTime);

    public Result findReadInfoByDateTime(String startDateTime, String endDateTime);

    public Result findReadInfoByMeterNoAndDate(String meterNo, String startDateTime, String endDateTime);

    public Result getMeterBalance(HttpServletRequest req, String meterNo, String operator);

    public Result getMeterStatus(String meterNo);

    public Result queryMeterStatusAndReading(String meterNo);

    public Result getPowerCostPerDay(String meterNo, String startDate, String endDate);

    public Result saveEventInfo(String param);

    public ReadParameter getReadParameters();

    public String readByUUID(HttpServletRequest req, String uuid);

    public Result queryHistoryRecordByMeterNo(String meter_no, String start_time, String end_time, int startNo, int pageSize);
}
