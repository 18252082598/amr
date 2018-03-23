/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.api;

import com.joymeter.entity.Result;
import com.joymeter.service.ReadService;
import com.joymeter.service.RechargeService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 巴乐兔API主要功能<br>
 * 控制功能，1.抄表，2.继电器控制，3.充值，4.余额清零，5.离线N天关闸配置<br>
 *
 * @author Zhong Fuqiang
 * @version 1.0.0
 */
@Controller
@RequestMapping("/baletu")
public class BaletuApi extends BaseApi {

    @Resource
    private RechargeService rechargeService;
    @Resource
    private ReadService readService;

    /**
     * 抄表
     *
     * @param req
     * @param access_token
     * @param meterNo
     * @return
     */
    @ResponseBody
    @RequestMapping("/readByMeterNo.do")
    public Result readByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.readByMeterNo(req, meterNo, res.getData().toString());
    }

    /**
     * 操作阀门
     *
     * @param req
     * @param access_token
     * @param meterNo
     * @param action
     * @return
     */
    @ResponseBody
    @RequestMapping("/controlByMeterNo.do")
    public Result controlByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo,
            @RequestParam("action") String action) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.ctrValveByMeterNo(req, meterNo, action, res.getData().toString());
    }

    /**
     * 充值
     *
     * @param req
     * @param access_token
     * @param meterNo
     * @param money
     * @return
     */
    @ResponseBody
    @RequestMapping("/rechargeByMeterNo.do")
    public Result rechargeByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo,
            @RequestParam("money") Double money) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return rechargeService.rechargeByMeterNo(meterNo, money, res.getData().toString());
    }

    /**
     * 余额清零
     *
     * @param req
     * @param access_token
     * @param meterNo
     * @return
     */
    @ResponseBody
    @RequestMapping("/clearByMeterNo.do")
    public Result clearByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return rechargeService.refundByMeterNo(meterNo, res.getData().toString());
    }

    /**
     * 离线N天关闸配置
     *
     * @param req
     * @param access_token
     * @param meterNo
     * @param days
     * @return
     */
    @ResponseBody
    @RequestMapping("/offlineDaysByMeterNo.do")
    public Result offlineDaysByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo,
            @RequestParam("days") int days) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return rechargeService.refundByMeterNo(meterNo, res.getData().toString());
    }
}
