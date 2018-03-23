/*
 * Copyright (c) 2016, Joymeter and/or its affiliates. All rights reserved.
 * JOYMETER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
 * <br>CJoy平台提供的一些常用的功能接口 <br>
 *
 * <br>
 * access_token: 访问token, 有效期为10分钟<br>
 * <p>
 * 规则：<br>
 * <br>
 * {"client_id":"", "datetime":""}<br>
 * client_id - 客户编号<br>
 * datetime - 加密时的时间, 格式为yyyyMMddHHmmss<br>
 * <br>
 * 对其进行 RSA 加密 <br>
 * 其中<br>
 * java密钥：MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJQeFrVhmHoWYNwPkXFVScpdwsZ/BnVhsUuGGvozfgcyde6Q7nFaTmvNBGuxbSqsSmatQLKEZWkPDDzP/Yv7zPcCAwEAAQ==<br>
 * .net密钥：&lt;RSAKeyValue&gt;&lt;Modulus&gt;lB4WtWGYehZg3A+RcVVJyl3Cxn8GdWGxS4Ya+jN+BzJ17pDucVpOa80Ea7FtKqxKZq1AsoRlaQ8MPM/9i/vM9w==&lt;/Modulus&gt;&lt;Exponent&gt;AQAB&lt;/Exponent&gt;&lt;/RSAKeyValue&gt;<br>
 * <br>
 * example:<br>
 * {"client_id":"joy000001","datetime":"20160802175100"}<br>
 * 加密后Base64输出:<br>
 * JJHRTTSyiLUJey+ylylvLCV7Y0hJKGZjZt1edoHB0AvAlY7FBEM93M4CEENEvI+1I6BvPmvQYCEBnJ/WXGRY/w==<br>
 * </p>
 *
 *
 * Java Demo: http://122.225.71.66:8787/api/RSACoder.java<br>
 * <br>
 * C# Demo: http://122.225.71.66:8787/api/RSACoder.cs<br>
 * <br>
 * 测试地址：http://122.225.71.66:8787/amr/<br>
 * <br>
 *
 * @author Zhong Fuqinag
 * @version 1.1.24
 * @since 2017-08-10
 */
@Controller
@RequestMapping("/joy")
public class JoymeterApi_V2 extends BaseApi {

    @Resource
    private RechargeService rechargeService;
    @Resource
    private ReadService readService;

    /**
     * 根据表号抄表<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/readByMeterNo.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"meterNo":"201603117852","access_token":""} <br>
     * meterNo: 表号<br>
     * 如果是多只表,格式为：={"meterNo":"201603117852,12341234,56785678"}<br>
     * access_token: 访问token<br>
     *
     * @param req 抄表请求
     * @param meterNo 表号
     * @param access_token 访问token
     * @param orderId
     * @return 返回JSON数据格式 示例：
     *
     * {"status":1,"data":{"readInfo":[{"info_id":11,"operate_id":"CB20160622174612","user_name":"201603117852","user_address":"上海市辖区浦东新区欢乐颂公寓1幢2202",<br>
     * "user_address_area":"上海市辖区浦东新区","user_address_community_no":"小区编号","user_address_community":"欢乐颂公寓","user_address_building":"1幢","user_address_unit":"1单元",<br>
     * "user_address_room":"2202","contact_info":"18967449402","concentrator_name":"5253E65B","meter_no":"201603117852","fee_type":"电表费率",<br>
     * "this_read":1.18,"last_read":1.18,"this_cost":0.0,"last_cost":0.0,"fee_need":0.0,"balance":-4.54,"exception":"1","fee_status":"1","read_type":"1","pay_remind":null,<br>
     * "operator_account":"admin","operate_time":1466588772000,"operate_day":"20160622","operate_dayTime":"17:46:12","meter_type":"40",<br>
     * "data2":0.0,"data3":0.0,"data4":0.0,"data5":0.0,"data6":0.0,"data7":0.0,"data8":0.0,"data9":""}],"totalNo":1}}<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     * readInfo: 查到的抄表返回数据 <br>
     * info_id: 记录序号<br>
     * operate_id: 抄表编号<br>
     * user_name: 用户名称<br>
     * user_address: 用户地址<br>
     * user_address_area:区<br>
     * user_address_community_no:小区编号<br>
     * user_address_community:小区<br>
     * user_address_building:楼栋<br>
     * user_address_unit:单元<br>
     * user_address_room:房号<br>
     * contact_info: 联系方式<br>
     * concentrator_name: 集中器名称<br>
     * meter_no: 表号<br>
     * fee_type: 费率类型<br>
     * this_read: 本期读数<br>
     * last_read: 上期读数<br>
     * this_cost: 本期消耗量<br>
     * last_cost: 上期消耗量<br>
     * fee_need: 本期扣费<br>
     * balance: 账户结余<br>
     * exception: 抄表状态(1 成功，0 失败)<br>
     * fee_status: 扣费状态(0 失败，1 已扣，2 未扣)<br>
     * read_type: 抄表类型(0 自动，1 手动)<br>
     * pay_remind: 催缴标记(1 已催缴，0/null 未催缴)<br>
     * operator_account: 操作员<br>
     * operate_time: 抄表时间<br>
     * operate_day: 年月日<br>
     * operate_dayTime: 时分秒<br>
     * meter_type: 表类型(10 水表，20 热表，30 气表，40 电表)<br>
     * data2: 累积流量(只适用于热表)<br>
     * data3: 当前功率(只适用于热表)<br>
     * data4: 当前流速(只适用于热表)<br>
     * data5: 当前流量(只适用于热表)<br>
     * data6: 当前进水温度(只适用于热表)<br>
     * data7: 当前回水温度(只适用于热表)<br>
     * data8: 累积工作时间(只适用于热表)<br>
     * data9: {表计的历史记录及状态参数,如：电池正常/欠压 等}(只适用于热表)<br>
     * totalNo: 查询到的记录总数<br>
     */
    @ResponseBody
    @RequestMapping("/v2/readByMeterNo.do")
    public Result v2_readByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo,
            @RequestParam("orderId") String orderId) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.readByMeterNo(req, meterNo, res.getData().toString());
    }

    /**
     * 根据表号充值，带有唯一识别id，如果id存在表示已冲值，如果id不存在，则冲值 <br>
     *
     * 请求地址：http://ip:port/path/joy/v2/rechargeByMeterNo.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"meterNo":"201603117852","money":100,"access_token":"",
     * "id":""} <br>
     * id: 唯一识别码<br>
     * meterNo: 表号<br>
     * money: 充值金额<br>
     * access_token: 访问token<br>
     *
     * @param req 请求上下文
     * @param meterNo 充值表号
     * @param money 充值金额
     * @param access_token 访问token
     * @param orderId 唯一识别码
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":{"meterNo":"201603117852","money":100,"balance":100}}<br>
     * status：0 失败, 1 成功, 2 认证错误, 1062 id已存在<br>
     * meterNo: 表号<br>
     * money: 充值金额<br>
     * balance: 账户余额<br>
     */
    @ResponseBody
    @RequestMapping("/v2/rechargeByMeterNo.do")
    public Result v2_rechargeByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo,
            @RequestParam("money") Double money,
            @RequestParam("orderId") String orderId) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return rechargeService.rechargeByMeterNo(orderId, meterNo, money, res.getData().toString());
    }
}
