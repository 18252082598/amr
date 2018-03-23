package com.joymeter.controller;

import java.math.BigDecimal;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.joymeter.entity.OperateInfo;
import com.joymeter.entity.PayResult;
import com.joymeter.entity.Result;

import com.joymeter.service.RechargeService;
import com.joymeter.service.UserService;
import com.joymeter.service.WeChatService;
import com.joymeter.util.JoyDatetime;

import com.joymeter.util.PropertiesUtil;
import com.joymeter.util.RSACoder;
import com.joymeter.util.WechatApiUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户充值
 *
 * @author Joymeter
 */
@Controller
@RequestMapping("/user")
public class RechargeController {

    @Resource
    private RechargeService rechargeService;
    @Resource
    private WeChatService weChatService;
    @Resource
    private UserService userService;

    /**
     * 加载所有操作员
     *
     * @return
     */
    @RequestMapping("/loadAllOperators.do")
    @ResponseBody
    public Result loadAllOperators() {
        return rechargeService.loadAllOperators();
    }

    /**
     * 根据查询条件查找用户
     *
     * @param meter_no
     * @param startNo
     * @param pageSize
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/findMotherMeterConf.do")
    public Result findMotherMeterConf(
            @RequestParam("meter_no") String meter_no,
            @RequestParam("startNo") int startNo,
            @RequestParam("pageSize") int pageSize,
            HttpServletRequest request) {
        return rechargeService.findMotherMeterConf(meter_no, startNo, pageSize);
    }

    /**
     * 根据查询条件查找母表参数配置
     *
     * @param user_name
     * @param user_address_community
     * @param user_address_building
     * @param user_address_unit
     * @param user_address_room
     * @param operator_name
     * @param contact_info
     * @param meter_no
     * @param status
     * @param startNo
     * @param pageSize
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/findUser.do")
    public Result findUser(
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
        return rechargeService.findUser(user_name, user_address_community,
                user_address_building, user_address_unit, user_address_room, operator_name,
                contact_info, meter_no, status, startNo, pageSize, request);
    }

    /**
     * 在线充值支付
     *
     * @param operateInfo
     * @param req
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/payOnLine.do")
    public PayResult payOnLine(OperateInfo operateInfo, HttpServletRequest req) throws Exception {
        final String operateId = "CZ" + String.valueOf(JoyDatetime.getTokenID());
        PayResult payResult = new PayResult();
        String style = "";
        if (operateInfo.getPay_method().equals("现金") || operateInfo.getPay_method().equals("Cash")) {
            style = "OFFPAY";
        }
        if (operateInfo.getPay_method().equals("微信") || operateInfo.getPay_method().equals("WeChat Pay")) {
            style = "SCANPAY";
        }
        if (operateInfo.getPay_method().equals("支付宝") || operateInfo.getPay_method().equals("Alipay")) {
            style = "DIRECTPAY";
        }
        String returnUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/" + PropertiesUtil.readConfig("payCallBackUrl", "wechat.properties");

        payResult.setMoney(new BigDecimal(operateInfo.getRecharge_money()));
        payResult.setOperateId(operateId);
        payResult.setStyle(style);
        payResult.setAccess_token(RSACoder.getAccess_token());
        payResult.setCjoyId(Integer.valueOf(WechatApiUtil.getCjoyId()));
        payResult.setMeterNo((operateInfo.getMeter_no()));
        payResult.setPostUrl(WechatApiUtil.getPostUrl());
        payResult.setReturnUrl(returnUrl);
        payResult.setStatus(1);
        return payResult;
    }

    /**
     * 现金充值支付
     *
     * @param operateInfo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/payOnCash.do")
    public Result payOnCash(OperateInfo operateInfo) throws Exception {
        final String operateId = "CZ" + String.valueOf(JoyDatetime.getTokenID());
        operateInfo.setOperate_id(operateId);
        // return weChatService.payOrRefund(operateInfo,"pay");
        return rechargeService.payOrRefund(operateInfo, "pay");
    }

    /**
     * 查询操作信息
     *
     * @param user_name
     * @param startNo
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadOperateInfo.do")
    public Result loadOperateInfo(
            @RequestParam("user_name") String user_name,
            @RequestParam("startNo") int startNo,
            @RequestParam("pageSize") int pageSize) {
        return rechargeService.loadOperateInfo(user_name, startNo, pageSize);
    }

    /**
     * 根据表号查询操作信息
     *
     * @param meter_no
     * @param startNo
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadOperateInfoByMeterNo.do")
    public Result loadOperateInfoByMeterNo(
            @RequestParam("meter_no") String meter_no,
            @RequestParam("startNo") int startNo,
            @RequestParam("pageSize") int pageSize) {
        return rechargeService.loadOperateInfoByMeterNo(meter_no, startNo, pageSize);
    }

    /**
     * 退款
     *
     * @param operateInfo
     * @return
     */
    @ResponseBody
    @RequestMapping("/refund.do")
    public Result refund(OperateInfo operateInfo) {
        //return weChatService.payOrRefund(operateInfo,"refund");
        return rechargeService.payOrRefund(operateInfo, "refund");
    }

    /**
     * 根据查询条件查找操作信息
     *
     * @param user_name
     * @param community
     * @param building
     * @param unit
     * @param room
     * @param operator_name
     * @param meter_no
     * @param startNo
     * @param pageSize
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/findOperateInfo.do")
    public Result findOperateInfo(@RequestParam("user_name") String user_name,
            @RequestParam("community") String community,
            @RequestParam("building") String building,
            @RequestParam("unit") String unit,
            @RequestParam("room") String room,
            @RequestParam("operator_name") String operator_name,
            @RequestParam("meter_no") String meter_no,
            @RequestParam("startNo") int startNo,
            @RequestParam("pageSize") int pageSize,
            HttpServletRequest request) {
        return rechargeService.findOperateInfo(user_name, community,
                building, unit, room, operator_name, meter_no, startNo, pageSize, request);
    }
    
    /**
     * 左权:充值记录页面查询
     * @param user_name
     * @param community
     * @param building
     * @param unit
     * @param room
     * @param operator_name
     * @param meter_no
     * @param startNo
     * @param pageSize
     * @param payMethod
     * @param operationMethod
     * @param startTime
     * @param endTime
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/zuoQuanFindOperateInfo.do")
    public Result findOperateInfoZuoQuan(
            @RequestParam("user_name") String user_name,
            @RequestParam("community") String community,
            @RequestParam("building") String building,
            @RequestParam("unit") String unit,
            @RequestParam("room") String room,
            @RequestParam("operator_name") String operator_name,
            @RequestParam("meter_no") String meter_no,
            @RequestParam("startNo") int startNo,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("payMethod") String payMethod,
            @RequestParam("operationMethod") String operationMethod,    
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            HttpServletRequest request) {
        return rechargeService.zuoQuanFindOperateInfo(user_name, community,
                building, unit, room, operator_name, meter_no, startNo, pageSize,payMethod, operationMethod,startTime,endTime,request);
    }
    
    
    /**
     * 加载所有的操作方式
     * @return
     * @author joymeter
     */
    @ResponseBody
    @RequestMapping("/loadOperationMethod.do")
    public Result loadOperationMethod() {
        return rechargeService.loadOperationMethod();
    }
    
    

}
