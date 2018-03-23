package com.joymeter.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joymeter.entity.Result;
import com.joymeter.service.WeChatService;

@Controller
@RequestMapping("/wechatController")
public class WeChatController {
    @Resource
    private WeChatService weChatService;
    
    /**
     * 根据表类型 输入充值的数量，根据费率计算去价格明细
     * @param req
     * @param meterType
     * @param amount
     * @return
     */
    @ResponseBody
    @RequestMapping("/findNeedCost.do")
    public Result findFeeTypes(HttpServletRequest req,
            @RequestParam("meterNo") String meter_no,
            @RequestParam("amount") Double amount) {
        return weChatService.queryMoneyByFeeTypeAndMeterNo(meter_no, amount);
    }
    
    @ResponseBody
    @RequestMapping("/findAmountByPrice.do")
    public Result queryAmountByMoney(HttpServletRequest req,
            @RequestParam("meterNo") String meter_no,
            @RequestParam("totalPrice") Double price) {
        return weChatService.queryAmountByPriceAndMeterNo(meter_no, price);
    }

}
