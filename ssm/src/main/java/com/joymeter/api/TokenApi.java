/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.api;

import com.joymeter.entity.Result;
import com.joymeter.service.TokenService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 代码表API
 *
 * @author Zhong Fuqiang
 * @version 1.0.0
 */
@Controller
@RequestMapping("/joy")
public class TokenApi extends BaseApi {

    @Resource
    private TokenService tokenService;

    /**
     * 根据表号充值 <br>
     *
     * 请求地址：http://ip:port/path/joy/rechargeByMeterNo.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"meterNo":"201603117852","money":100,"access_token":""} <br>
     * meterNo: 表号<br>
     * money: 充值金额<br>
     * access_token: 访问token<br>
     *
     * @param req 请求上下文
     * @param meterNo 充值表号
     * @param method 方式：set、credit 、clear 、change
     * @param amount 充值量
     * @param access_token 访问token
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":{"meterNo":"201603117852","money":100,"balance":100,
     * "code":"12345678"}}<br>
     * status：0 失败, 1 成功, 2 认证错误, 3 出码失败<br>
     * meterNo: 表号<br>
     * money: 充值金额<br>
     * balance: 账户余额<br>
     */
    @ResponseBody
    @RequestMapping("/code/token.do")
    public Result tokenByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo,
            @RequestParam("method") String method,
            @RequestParam("amount") int amount) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        if (method.toLowerCase().equals("set")) {
            return tokenService.setToken(meterNo);
        }
        if (method.toLowerCase().equals("credit")) {
            return tokenService.creditToken(meterNo, amount, res.getData().toString());
        }
        if (method.toLowerCase().equals("clear")) {
            return tokenService.clearToken(meterNo, res.getData().toString());
        }
        return null;
    }
}
