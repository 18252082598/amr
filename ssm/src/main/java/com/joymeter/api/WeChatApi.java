/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.api;

import com.joymeter.dao.UserDao;
import com.joymeter.entity.Result;
import com.joymeter.entity.User;
import com.joymeter.service.ReadService;
import com.joymeter.service.RechargeService;
import com.joymeter.service.UserService;
import com.joymeter.service.WeChatService;
import com.joymeter.util.WechatApiUtil;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微信端的接口
 *
 * @author Zhong Fuqiang
 * @version 1.0.0
 */
@Controller
@RequestMapping("/wechat")
public class WeChatApi extends BaseApi {

    @Resource
    private UserDao userDao;
    @Resource
    private ReadService readService;
    @Resource
    private WeChatService weChatService;  
    @Resource
    private RechargeService rechargeService;
    @Resource 
    private UserService userService;

    /**
     * 根据户号认证用及查询户信息
     *
     * @param req
     * @param access_token
     * @param id
     *            户号
     * @return
     */
    @ResponseBody
    @RequestMapping("/findUserById.do")
    public Result findUserByMeterNo(HttpServletRequest req, 
            @RequestParam("access_token") String access_token,
            @RequestParam("id") String id) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        List<User> users = userDao.findUserByIdCardNo(id);
        if (users == null || users.isEmpty()) {
            res.setStatus(4);
            return res;
        }
        res.setData(users);
        return res;
    }

    /**
     * 根据账号计算能耗  key: week 按周 month 按月 其他 按日
     * 
     * @param req
     * @param access_token
     * @param id_card_no
     * @param startDate
     * @param endDate
     * @param key
     * @return 
     */
    @ResponseBody
    @RequestMapping("/getCostPowerByIdCardNo.do")
    public Result getCostPowerByIdCardNo(HttpServletRequest req, 
            @RequestParam("access_token") String access_token,
            @RequestParam("id_card_no") String id_card_no,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate, 
            @RequestParam("key") String key) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
       return weChatService.getCostPowerByIdCardNo(id_card_no, startDate, endDate, key);
    }
    
    /**
     * 根据表号查询能耗 key: week 按周 month 按月 其他 按日
     * @param req
     * @param access_token
     * @param meterNo
     * @param startDate
     * @param endDate
     * @param key
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryCostPowerByMeterNo.do")
    public Result queryCostPowerByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("key") String key) {
        Result res =  OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        if ("week".equals(key.toLowerCase())) {
            return weChatService.queryCostPowerPerWeekByMeterNo(meterNo, startDate, endDate);// 按周
        }else if ("month".equals(key.toLowerCase())) {
            return weChatService.queryCostPowerPerMonthByMeterNo(meterNo, startDate, endDate);// 按月
        }else {
            return weChatService.queryCostPowerPerDayByMeterNo(meterNo, startDate, endDate);// 按天
        }
        
    }
    
 
    /**
     * 根据账号查询账号下实时数据
     * 
     * @param req
     * @param access_token
     * @param id_card_no
     */
    @ResponseBody
    @RequestMapping("/readByIdCardNo.do")
    public void sendRealDataByIdCardNo(HttpServletRequest req, 
            @RequestParam("access_token") String access_token,
            @RequestParam("id_card_no") String id_card_no) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            // return res;
        }
        List<User> users = userDao.findUserByIdCardNo(id_card_no);
        String meterNo = "";
        for (User user : users) {
            meterNo += user.getMeter_no() + ",";
        }
        meterNo = meterNo.substring(0, meterNo.length() - 1);
        Result result = readService.readByMeterNo(req, meterNo, res.getData().toString());
        Result statusResult = weChatService.getValueStatuesByIdCardNo(id_card_no);
        WechatApiUtil.sendRealDataByIdCardNo(result, statusResult, id_card_no);
    }
    
    /**
     * 根据表号查询实时数据
     * @param req
     * @param access_token
     * @param meterNo
     */
    @ResponseBody
    @RequestMapping("/readByMeterNo.do")
    public void sendRealDataByMeterNo(HttpServletRequest req, 
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            // return res;
        }      
        Result result = readService.readByMeterNo(req, meterNo, res.getData().toString());
        Result statusResult = weChatService.getValueStatusByMeterNo(meterNo);
        WechatApiUtil.sendRealDataByMeterNo(result, statusResult, meterNo);
        
    }
    
    /**
     * 根据表号查询阀门状态
     * @param req
     * @param access_token
     * @param meterNo
     * @return
     * {"status":1,"data":{"meterNo":"1700760035","valueStatus":"1"}}
     */
    @ResponseBody
    @RequestMapping("/getMeterStatusByMeterNo.do")
    public Result getMeterStatusByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return weChatService.getValueStatusByMeterNo(meterNo);
    }

    /**
     * 根据账号查询账号下所有表的阀门状态
     * @param req
     * @param access_token
     * @param id_card_no
     * @return
     * {"status":1,"data":[{"meterNo":"57001240","valueStatus":"0"},{"meterNo":"1700760035","valueStatus":"1"}]}
     */
    @ResponseBody
    @RequestMapping("/getMeterStatus.do")
    public Result getMeterStatus(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("id_card_no") String id_card_no) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return weChatService.getValueStatuesByIdCardNo(id_card_no);
    }
    
    /**
     * 根据表号充值接口
     * @param req
     * @param access_token
     * @param meterNo 
     * @param time 操作时间
     * @param recharge_money 充值金额
     * @param operateId 业务编号
     * @param style 支付类型
     * @return
     */
    @ResponseBody
    @RequestMapping("/otherPay.do")
    public Result rechargeByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo,
            @RequestParam("time") String time,
            @RequestParam("recharge_money") Double recharge_money,
            @RequestParam("operateId") String operateId,
            @RequestParam("style") String style) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return weChatService.rechargeByMeterNo(operateId, meterNo,time, recharge_money,style);
    }
    
    /**
     * 根据相应的费率计算价格接口
     * @param meterType 表类型
     * @param amount 充值数量
     * 
     *{"status":1,"data":{"otherCost":0.0,        其他费用单机价                 ( 单价 )
     *                    "otherPriceAll":0.0,    其他费用总和                    ( 其他费用单价X数量 )  
     *                    "basicUnitPrice":1.2,   基本费用单价                    ( 单价 )
     *                    "basicPrice":120.0,     基本费用总和                    ( 基本费用单价X数量 )
     *                    "disposingUnitCost":0.0,排污费单价                       ( 单价 )
     *                    "disposingPrice":0.0,   排污费总和                       ( 排污费单价X数量 )
     *                    "totalUnitCost":1.2}}   总单价                              ( 基本费用单价+其他费用单价+排污费单价 )
     *                    "extraCost":0.0,        额外费用
     *                    "totalPrice":120.0,     总的费用                           ( 总单价X数量+额外费用 )                  
     */
    @ResponseBody
    @RequestMapping("/findNeedCost.do")
    public Result findFeeTypes(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meter_no,
            @RequestParam("amount") Double amount) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return weChatService.queryMoneyByFeeTypeAndMeterNo(meter_no, amount);
    }
    
    
    /**
     * 根据钱，表号逆推出该表剩余可用数量以及钱明细
     * @param req
     * @param access_token
     * @param meter_no
     * @param price
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryAmountByPrice.do")
    public Result queryAmountByPrice(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meter_no,
            @RequestParam("price") Double price) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return weChatService.queryAmountByPriceAndMeterNo(meter_no, price);
    }
    
       

}
