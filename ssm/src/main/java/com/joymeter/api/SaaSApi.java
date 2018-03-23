/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.joymeter.entity.ReadParameter;
import com.joymeter.entity.Result;
import com.joymeter.service.saasImp.SaaSType;
import com.joymeter.service.ReadService;
import com.joymeter.util.JoyDatetime;
import com.joymeter.util.JoyLogger;
import java.util.Date;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 与SaaS平台交互的接口
 *
 * @author Zhong Fuqiang
 * @version 1.0.0
 */
@Controller
@RequestMapping("/joy")
public class SaaSApi {

    @Resource
    private ReadService readService;

    /**
     * 系统事件回调接口
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("event.do")
    public Result event(@RequestBody String param) {
        JoyLogger.LogInfo("saas event: " + param);
        JSONObject jo = JSON.parseObject(param);
        if (jo == null) {
            return null;
        }
        String event = jo.getString("event").toLowerCase();
        JSONObject ja = jo.getJSONObject("device");
        if (ja == null) {
            return null;
        }
        switch (event) {
            case "push":
                return readService.saveReadInfo(this.toJson(param));
            case "ctr":
                return readService.saveReadInfo(this.toJson(param));
//            case "error":
//                return userService.saveMeterEventInfo(param);
            case "warning":
                return readService.saveEventInfo(param);
            case "offline":
                return readService.saveEventInfo(param);
            case "online":
                return readService.saveEventInfo(param);
            default:
                return null;
        }
    }

    /**
     * 转成JSON
     *
     * @param parameter
     * @return
     */
    private String toJson(final String parameter) {
        JSONObject jo = JSON.parseObject(parameter);
        String errcode = jo.getString("errcode");
        if (!"0".equals(errcode)) {
            return null;
        }
        String event = jo.getString("event");
        JSONObject device = jo.getJSONObject("device");
        String meter = device.getString("id");
        String dateTime = device.getString("datetime");
        String data1 = "0";
        String data9 = "-1";
        String isAutoClear = "1";
        String accountName = "system";
        Double balanceWarnning = null;
        Double valveClose = null;
        if (event.toLowerCase().equals("push")) {
            data1 = device.getString("data");
            data9 = device.getString("state");
            isAutoClear = "0"; // 1 不扣费, 0 扣费
            ReadParameter rp = readService.getReadParameters();
            if (rp != null) {
                balanceWarnning = rp.getBalance_warn();
                valveClose = rp.getValve_close();
            }
        }
        if ("ctr".equals(event.toLowerCase())) {
            isAutoClear = "10";
            String state = device.getString("state");
            if (state != null && state.startsWith("04")) {
                event = "_Close";
            }
            if (state != null && state.startsWith("01")) {
                event = "_Open";
            }
        }
        if (event.toLowerCase().equals("down")) {
            isAutoClear = "20";
        }
        String error_code = device.getString("error_code");
        if ("1001".equals(error_code)) {
            errcode = "1";
        }
        return String.format(SaaSType.JSON_FORMAT_SAVE_INFO, meter, "0",
                errcode, data1, "0", "0", "0", "0", "0", "0", "0", data9,
                JoyDatetime.dateFormat(new Date(), JoyDatetime.FORMAT_DATETIME_2),
                isAutoClear, event, balanceWarnning, "2", accountName, valveClose, dateTime);
    }
}
