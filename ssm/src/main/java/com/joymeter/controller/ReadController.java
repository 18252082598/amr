package com.joymeter.controller;

import java.util.ArrayList;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.joymeter.entity.ReadParameter;
import com.joymeter.entity.Result;
import com.joymeter.service.ReadService;

@Controller
@RequestMapping("/user")
public class ReadController {

    @Resource
    private ReadService readService;

    @ResponseBody
    @RequestMapping("/findUsersToRead.do")
    public Result findUsersToRead(
            @RequestParam("user_name") String user_name,
            @RequestParam("user_address_community") String user_address_community,
            @RequestParam("user_address_building") String user_address_building,
            @RequestParam("user_address_unit") String user_address_unit,
            @RequestParam("user_address_room") String user_address_room,
            @RequestParam("operator_name") String operator_name,
            @RequestParam("contact_info") String contact_info,
            @RequestParam("meter_no") String meter_no,
            @RequestParam("status") String status,
            @RequestParam("startNo") int startNo,
            @RequestParam("pageSize") int pageSize,
            HttpServletRequest request) {
        return readService.findUsersToRead(user_name,
                user_address_community, user_address_building,
                user_address_unit, user_address_room, operator_name, contact_info, meter_no,
                status, startNo, pageSize, request);
    }

    @ResponseBody
    @RequestMapping("/sendUserParameters.do")
    public Result sendUserParameters(HttpServletRequest req,
            @RequestParam("list") ArrayList<String[]> list,
            @RequestParam("isAutoClear") int isAutoClear,
            @RequestParam("accountName") String accountName) {
        return readService.sendDevParams(req, list, isAutoClear, accountName);
    }

    @ResponseBody
    @RequestMapping("/findReadInfo.do")
    public Result loadReadInfo(@RequestParam("user_name") String user_name,
            @RequestParam("community") String community,
            @RequestParam("building") String building,
            @RequestParam("unit") String unit,
            @RequestParam("room") String room,
            @RequestParam("operator_name") String operator_name,
            @RequestParam("feeTypeName") String feeTypeName,
            @RequestParam("meter_no") String meter_no,
            @RequestParam("meter_type") String meter_type,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("startNo") int startNo,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("queryTableType") String queryTableType,
            HttpServletRequest request) {
        return readService.findReadInfo(user_name, community,
                building, unit, room, operator_name, feeTypeName, meter_no, meter_type, startTime, endTime,
                startNo, pageSize, queryTableType, request);
    }
    
    /**
     * 左权抄表记录
     */
    @ResponseBody
    @RequestMapping("/zuoQuanFindReadInfo.do")
    public Result zuoQuanLoadReadInfo(@RequestParam("user_name") String user_name,
            @RequestParam("community") String community,
            @RequestParam("building") String building,
            @RequestParam("unit") String unit,
            @RequestParam("room") String room,
            @RequestParam("operator_name") String operator_name,
            @RequestParam("feeTypeName") String feeTypeName,
            @RequestParam("meter_no") String meter_no,
            @RequestParam("meter_type") String meter_type,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("startNo") int startNo,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("minBalance") String minBalance,//最小金额
            @RequestParam("maxBalance") String maxBalance,//最大金额
            @RequestParam("queryTableType") String queryTableType,
            HttpServletRequest request) {
        return readService.zuoQuanFindReadInfo(user_name, community,
                building, unit, room, operator_name, feeTypeName, meter_no, meter_type, startTime, endTime,
                startNo, pageSize,minBalance,maxBalance, queryTableType, request);
    }
    
    
    @ResponseBody
    @RequestMapping("/openOrCloseValve.do")
    public Result openOrCloseValve(HttpServletRequest req,
            @RequestParam("list") ArrayList<String[]> list,
            @RequestParam("accountName") String accountName) {
        return readService.ctrValve(req, list, accountName);
    }

    @ResponseBody
    @RequestMapping("/findValveStatus.do")
    public Result findValveStatus(@RequestParam("sendTime") String sendTime) {
        return readService.findValveStatus(sendTime);
    }

    @ResponseBody
    @RequestMapping("/findReadInfoByMonth.do")
    public Result findReadInfoByMonth(@RequestParam("month") String month,
            @RequestParam("meter_type") String meter_type,
            @RequestParam("startNo") int startNo,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("queryTableType") String queryTableType,
            HttpServletRequest request) {
        return readService.findReadInfoByMonth(month, meter_type, startNo, pageSize, queryTableType, request);
    }

    @ResponseBody
    @RequestMapping("/sendFailedParameters.do")
    public Result sendFailedParameters(HttpServletRequest req,
            @RequestParam("list") ArrayList<String[]> list,
            @RequestParam("isAutoClear") int isAutoClear,
            @RequestParam("accountName") String accountName) {
        return readService.sendFailedParams(req, list, isAutoClear, accountName);
    }

    @ResponseBody
    @RequestMapping("/inputByHand.do")
    public Result inputByHand(@RequestParam("meter_no") String meter_no,
            @RequestParam("this_read") double this_read,
            @RequestParam("operator_account") String operator_account) {
        return readService.inputByHand(meter_no, this_read, operator_account);
    }

    @ResponseBody
    @RequestMapping("/findInfoByMeterNo.do")
    public Result findInfoByMeterNo(@RequestParam("meter_no") String meter_no) {
        return readService.findInfoByMeterNo(meter_no);
    }

    @ResponseBody
    @RequestMapping("/findCommunityInfo.do")
    public Result findCommunityInfo(
            @RequestParam("community_address") String community_address) {
        return readService.findCommunityInfo(community_address);
    }

    @ResponseBody
    @RequestMapping("/findByCommunity.do")
    public Result readByCommunity(HttpServletRequest req,
            @RequestParam("community_name") String community_name,
            @RequestParam("isAutoClear") int isAutoClear,
            @RequestParam("accountName") String accountName) {
        return readService.readByCommunity(req, community_name, isAutoClear, accountName);
    }

    @ResponseBody
    @RequestMapping("/receive.do")
    public Result saveReadInfo(@RequestParam("parameters") String parameters) {
        return readService.saveReadInfo(parameters);
    }

    @ResponseBody
    @RequestMapping("/findReadResult.do")
    public Result findReadResult(@RequestParam("sendTime") String sendTime) {
        return readService.findReadResult(sendTime);
    }

    @ResponseBody
    @RequestMapping("/findReadResultByOperator.do")
    public Result findReadResultByOperator(@RequestParam("sendTime") String sendTime,
            @RequestParam("accountName") String accountName) {
        return readService.findReadResultByOperator(sendTime, accountName);
    }

    @ResponseBody
    @RequestMapping("/updateCommunityReadResult.do")
    public Result updateCommunityReadResult(@RequestParam("sendTime") String sendTime,
            @RequestParam("accountName") String accountName) {
        return readService.updateCommunityReadResult(sendTime, accountName);
    }

    @ResponseBody
    @RequestMapping("/saveReadParameter.do")
    public Result saveReadParameter(
            ReadParameter readParameter,
            @RequestParam("original_day") String original_day,
            @RequestParam("original_hour") String original_hour,
            @RequestParam("original_minute") String original_minute,
            @RequestParam("original_balance_warn") Double original_balance_warn,
            @RequestParam("original_valve_close") Double original_valve_close,
            @RequestParam("original_isAutoRead") String original_isAutoRead,
            @RequestParam("original_isAutoInform") String original_isAutoInform
    ) {
        return readService.saveReadParams(readParameter, original_day, original_hour, original_minute, original_balance_warn, original_valve_close, original_isAutoRead, original_isAutoInform);
    }

    @ResponseBody
    @RequestMapping("/showReadParameter.do")
    public Result showReadParameter() {
        return readService.showReadParameter();
    }

    @ResponseBody
    @RequestMapping("/loadUsersRemind.do")
    public Result loadUsersRemind(@RequestParam("user_name") String user_name,
            @RequestParam("community") String community,
            @RequestParam("building") String building,
            @RequestParam("unit") String unit,
            @RequestParam("room") String room,
            @RequestParam("operator_name") String operator_name,
            @RequestParam("meter_no") String meter_no,
            @RequestParam("startNo") int startNo,
            @RequestParam("pageSize") int pageSize,
            HttpServletRequest request
    ) {
        return readService.loadUsersRemind(user_name, community,
                building, unit, room, operator_name, meter_no, startNo, pageSize, request);
    }

    @ResponseBody
    @RequestMapping("/markup.do")
    public Result markup(@RequestParam("meter_no") String meter_no,
            @RequestParam("user_name") String user_name) {
        return readService.markup(meter_no, user_name);
    }

    @ResponseBody
    @RequestMapping("/delMarkup.do")
    public Result delMarkup(@RequestParam("meter_no") String meter_no,
            @RequestParam("user_name") String user_name) {
        return readService.delMarkup(meter_no, user_name);
    }
}
