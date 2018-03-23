/*
 * Copyright (c) 2016, Joymeter and/or its affiliates. All rights reserved.
 * JOYMETER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.joymeter.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.Result;
import com.joymeter.entity.User;
import com.joymeter.service.ReadService;
import com.joymeter.service.RechargeService;
import com.joymeter.service.UserService;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class JoymeterApi extends BaseApi {

    @Resource
    private RechargeService rechargeService;
    @Resource
    private ReadService readService;
    @Resource
    private UserService userService;
    @Resource
    private UserDao userDao;

    /**
     * 注册房源信息 <br>
     *
     * 请求地址：http://ip:port/path/joy/registRoomInfo.do<br>
     *
     * 请求参数格式示例：{"access_token":"","roomInfo":[{"onlineSyncCode":"201707131542",<br>
     * "doorType":"1","provinceName":"浙江省","cityNam":"e杭州市","cityAreaName":"西湖区",<br>
     * "streetName":"申花街道","streetAddress":"申花路789号","villageName":"剑桥公社",<br>
     * "buildingNumber":"1栋","unitNumber":"1单元","houseNumber":"101房","roomName":"1011室",<br>
     * "callBackUrl":""},"meterNo":"123456780"}]}<br>
     *
     * 注：roomInfo中，不需要的参数，可以不写<br>
     * access_token: 访问token<br>
     * onlineSyncCode: 房源id<br>
     * doorType: 房间类型 1:大门；2:内门<br>
     * provinceName: 省名<br>
     * cityName: 市名<br>
     * cityAreaName: 区名<br>
     * streetName: 街道名<br>
     * streetAddress: 街道地址<br>
     * villageName: 小区名<br>
     * buildingNumber: 第几栋<br>
     * unitNumber: 单元<br>
     * houseNumber: 第几室<br>
     * roomName: 内房名<br>
     * callBackUrl: 回调地址<br>
     * meterNo: 表号
     *
     * @param req 请求上下文
     * @param roomInfo
     * @param access_token 访问token
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":{}}<br>
     * status：0 失败, 1 成功<br>
     */
    @ResponseBody
    @RequestMapping("/registRoomInfo.do")
    public Result registRoomInfo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("roomInfo") String roomInfo) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return userService.registRoomInfo(roomInfo);
    }

    /**
     * 根据房源id查询该房源所有的充值记录，要求带分页(安歆公寓提出)<br>
     *
     * @author mengshuai
     * @date 2018-03-05
     * @version v1.0.0
     *
     * @param req
     * @param access_token
     * @param onlineSynvCode
     * @param operate_type
     * @param page
     * @param pageSize
     * @return
     *
     * 请求地址: http://ip:port/path/joy/queryRechargeInfoByRoomId.do <br>
     *
     * 请求参数格式示例:
     * {"access_token":"","onlineSyncCode":"","operate_type":"","page":1,"pageSize":10}
     * <br>
     *
     * access_token:访问token<br>
     * onlineSynvCode:房源id(相当于数据库中的user_address_original_room字段)
     * operate_type:充值类型(值为""返回所有对余额的操作记录，为0表示返回退费操作记录，1表示返回充值操作记录，2表示返回自动扣费记录)
     * page:当前页数 pageSize:每页显示数目
     */
    @ResponseBody
    @RequestMapping("/queryRechargeInfoByRoomId.do")
    public Result queryRechargeInfoByRoomId(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("onlineSyncCode") String onlineSynvCode,
            @RequestParam("operate_type") String operate_type,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        int startNo = (page - 1) * pageSize;
        if (startNo <= 0) {
            startNo = 0;
        }
        return rechargeService.queryRechargeInfoByRoomId(onlineSynvCode, operate_type, startNo, pageSize);
    }

    /**
     * 查询电表状态（电表的连网状态和通电状态）<br>
     *
     * 请求地址：http://ip:port/path/joy/queryMeterStatus.do<br>
     *
     * 请求参数格式示例：{"access_token":"","meterNo":"12345"}<br>
     *
     * access_token: 访问token<br>
     * meterNo: 表号<br>
     *
     * @param req 请求上下文
     * @param meterNo 表号
     * @param access_token 访问token
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":{"net_state":"0","elec_state":"0"}}<br>
     * status：0 失败, 1 成功<br>
     * net_state：0 离线, 1 在线<br>
     * elec_state：0 断电, 1 通电<br>
     */
    @ResponseBody
    @RequestMapping("/queryMeterStatus.do")
    public Result queryMeterStatus(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.getMeterStatus(meterNo);
    }

    /**
     * 查询电表状态（电表的连网状态和通电状态,表最新读数,余额及上传时间） 备注:该需求由复恒麦家公寓提出<br>
     *
     * 请求地址：http://ip:port/path/joy/queryMeterStatusAndReading.do<br>
     *
     * 请求参数格式示例：{"access_token":"","meterNo":"12345"}<br>
     *
     * access_token: 访问token<br>
     * meterNo: 表号<br>
     *
     * @param req 请求上下文
     * @param meterNo 表号
     * @param access_token 访问token
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":{"net_state":"0","elec_state":"0","balance",23.4,"this_read",2.1,"read_time":"2017-01-01
     * 17:20:20"}}<br>
     * status：0 失败, 1 成功<br>
     * net_state：0 离线, 1 在线<br>
     * elec_state：0 断电, 1 通电<br>
     * balance：余额<br>
     * this_read：最新读数<br>
     * read_time 最新抄表时间<br>
     */
    @ResponseBody
    @RequestMapping("/queryMeterStatusAndReading.do")
    public Result queryMeterStatusAndReading(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.queryMeterStatusAndReading(meterNo);
    }

    /**
     * 查询电表每天的耗电量<br>
     *
     * 请求地址：http://ip:port/path/joy/queryPowerCostPerDay.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"access_token":"","meterNo":"12345","start_time":"2017-07-01","end_time":"2017-07-10"}<br>
     *
     * @param req 请求上下文
     * @param meterNo 表号
     * @param access_token 访问token
     * @param start_time 开始时间
     * @param end_time 结束时间
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":{"onlineSyncCode":"12345","meterNo":"123456", <br>
     * "datas":[{"data":"100", "balance":"100.00", "date":"2017-07-18"}]}}<br>
     * status：0 失败, 1 成功<br>
     * onlineSyncCode：同步房源id<br>
     * meterNo：表号<br>
     * data：耗电量<br>
     * date：日期<br>
     */
    @ResponseBody
    @RequestMapping("/queryPowerCostPerDay.do")
    public Result queryPowerCostPerDay(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo,
            @RequestParam("start_time") String start_time,
            @RequestParam("end_time") String end_time) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.getPowerCostPerDay(meterNo, start_time, end_time);
    }

    /**
     * 查询电表的剩余电量<br>
     *
     * 请求地址：http://ip:port/path/joy/queryMeterBalance.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"access_token":"","meterNo":"12345"}<br>
     *
     *
     * @param req 请求上下文
     * @param meterNo 表号
     * @param access_token 访问token
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":{"onlineSyncCode":"12345","meterNo":"123456",<br>
     * "data":"100","date":"2017-07-18"}}<br>
     * status：0 失败, 1 成功<br>
     * onlineSyncCode：同步房源id<br>
     * meterNo：表号<br>
     * data：耗电量<br>
     * date：日期<br>
     */
    @ResponseBody
    @RequestMapping("/queryMeterBalance.do")
    public Result queryMeterBalance(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.getMeterBalance(req, meterNo, res.getData().toString());
    }

    /**
     * 分页查询所有已注册的设备信息<br>
     * 请求地址：http://ip:port/path/joy/queryMeters.do<br>
     *
     * @param req 请求上下文
     * @param access_token 访问token
     * @param pageSize 每页数量
     * @param page 当前页
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":[{"user_id":15,"user_name":"测试7","province":"上海",<br>
     * "city":"上海市","district":"奉贤区","user_address_area":"上海上海市奉贤区",<br>
     * "user_address_community":"新梅淞南苑","user_address_building":"33",<br>
     * "user_address_unit":"2","user_address_room":"407","user_address_original_room":"407",<br>
     * "id_card_no":"HR2017071900184","contact_info":"2","supplier_name":"2",<br>
     * "house_area":2.0,"coefficient_name":"2","auto_deduction":"1",<br>
     * "concentrator_name":"地盛中路3","meter_no":"201603162194","meter_status":"1",<br>
     * "user_status":"1","meter_type":"40","meter_model":"JOY","submeter_no":"2",<br>
     * "valve_no":"201603162194","valve_status":"1",<br>
     * "protocol_type":"com.joymeter.dtu.data.other.ParseElecData_001",<br>
     * "valve_protocol":"com.joymeter.dtu.data.other.valve.ParseElecValveData_001",<br>
     * "fee_type":"测试","operator_account":"admin","add_time":1501564856000,<br>
     * "last_balance":0.0,"last_read_time":1444449600000,"pay_remind":"0"}],
     * "pages":10}<br>
     * status：0 失败, 1 成功, 2 认证错误<br>
     * user_id: 用户id<br>
     * user_name: 账户余额<br>
     * province: 省<br>
     * city: 市<br>
     * district: 区<br>
     * user_address_area: 地址<br>
     * user_address_community: 小区<br>
     * user_address_building: 楼栋<br>
     * user_address_unit: 单元号<br>
     * user_address_room: 房号<br>
     * user_address_original_room: 原房号<br>
     * id_card_no: 身份证号<br>
     * contact_info: 联系电话<br>
     * supplier_name: 供水/电网点<br>
     * house_area: 房子面积<br>
     * coefficient_name: 户型系数名称<br>
     * auto_deduction: 自动扣费<br>
     * concentrator_name: 集中器名称<br>
     * meter_no: 表号<br>
     * meter_status: 仪表状态<br>
     * user_status: 用户状态<br>
     * meter_type: 仪表类型<br>
     * meter_model: 仪表型号<br>
     * submeter_no: 子表号<br>
     * valve_no: 阀门编号<br>
     * valve_status: 阀门状态<br>
     * protocol_type: 协议类型<br>
     * valve_protocol: 阀门协议<br>
     * fee_type: 费率类型<br>
     * operator_account: 操作员账户<br>
     * add_time: 注册时间<br>
     * last_balance: 账户余额<br>
     * last_read_time: 上次抄表时间<br>
     * pay_remind: 催缴提醒（0未提醒；1已提醒）<br>
     * pages: 总的页数<br>
     */
    @ResponseBody
    @RequestMapping("/queryMeters.do")
    public Result queryMeters(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("page") int page) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        int startNo = (page - 1) * pageSize;
        if (startNo <= 0) {
            startNo = 0;
        }
        return rechargeService.findUser("", "", "", "", "", "", "", "", "", startNo, pageSize, req);
    }

    /**
     * 查询设备在一段时间内的充值信息(充值电量)<br>
     *
     * 请求地址：http://ip:port/path/joy/queryRechargeInfo.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"access_token":"","meterNo":"12345",<br>
     * "start_time":"2017-07-01","end_time":"2017-07-10"}<br>
     *
     * access_token: 访问token<br>
     * meterNo: 表号<br>
     *
     * @param req 请求上下文
     * @param meterNo 表号
     * @param start_time 起始时间
     * @param end_time 结束时间
     * @param access_token 访问token
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":{"onlineSyncCode":"12345","meterNo":"123456",<br>
     * "datas":[{"data":"100","date":"2017-07-18"}]}}<br>
     * status：0 失败, 1 成功<br>
     * onlineSyncCode：同步房源id<br>
     * meterNo：表号<br>
     * data：充值电量<br>
     * date：日期<br>
     */
    @ResponseBody
    @RequestMapping("/queryRechargeInfo.do")
    public Result queryRechargeInfo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo,
            @RequestParam("start_time") String start_time,
            @RequestParam("end_time") String end_time) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return rechargeService.getRechargeInfo(meterNo, start_time, end_time);
    }

    /**
     * 根据表号查询用户信息<br>
     *
     * 请求地址：http://ip:port/path/joy/findUserByMeterNo.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"meterNo":"201603117852","access_token":""}<br>
     * meterNo: 表号<br>
     * access_token: 访问token<br>
     *
     * @param req 请求上下文
     * @param meterNo 表号
     * @param access_token 访问token
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":{"user_id":15,"user_name":"测试7","province":"上海",<br>
     * "city":"上海市","district":"奉贤区","user_address_area":"上海上海市奉贤区",<br>
     * "user_address_community":"新梅淞南苑","user_address_building":"33",<br>
     * "user_address_unit":"2","user_address_room":"407","user_address_original_room":"407",<br>
     * "id_card_no":"HR2017071900184","contact_info":"2","supplier_name":"2",<br>
     * "house_area":2.0,"coefficient_name":"2","auto_deduction":"1",<br>
     * "concentrator_name":"地盛中路3","meter_no":"201603162194","meter_status":"1",<br>
     * "user_status":"1","meter_type":"40","meter_model":"JOY","submeter_no":"2",<br>
     * "valve_no":"201603162194","valve_status":"1",<br>
     * "protocol_type":"com.joymeter.dtu.data.other.ParseElecData_001",<br>
     * "valve_protocol":"com.joymeter.dtu.data.other.valve.ParseElecValveData_001",<br>
     * "fee_type":"测试","operator_account":"admin","add_time":1501564856000,<br>
     * "last_balance":0.0,"last_read_time":1444449600000,"pay_remind":"0"}}<br>
     * status：0 失败, 1 成功, 2 认证错误<br>
     * user_id: 用户id<br>
     * user_name: 账户余额<br>
     * province: 省<br>
     * city: 市<br>
     * district: 区<br>
     * user_address_area: 地址<br>
     * user_address_community: 小区<br>
     * user_address_building: 楼栋<br>
     * user_address_unit: 单元号<br>
     * user_address_room: 房号<br>
     * user_address_original_room: 原房号<br>
     * id_card_no: 身份证号<br>
     * contact_info: 联系电话<br>
     * supplier_name: 供水/电网点<br>
     * house_area: 房子面积<br>
     * coefficient_name: 户型系数名称<br>
     * auto_deduction: 自动扣费<br>
     * concentrator_name: 集中器名称<br>
     * meter_no: 表号<br>
     * meter_status: 仪表状态<br>
     * user_status: 用户状态<br>
     * meter_type: 仪表类型<br>
     * meter_model: 仪表型号<br>
     * submeter_no: 子表号<br>
     * valve_no: 阀门编号<br>
     * valve_status: 阀门状态<br>
     * protocol_type: 协议类型<br>
     * valve_protocol: 阀门协议<br>
     * fee_type: 费率类型<br>
     * operator_account: 操作员账户<br>
     * add_time: 注册时间<br>
     * last_balance: 账户余额<br>
     * last_read_time: 上次抄表时间<br>
     * pay_remind: 催缴提醒（0未提醒；1已提醒）<br>
     */
    @ResponseBody
    @RequestMapping("/findUserByMeterNo.do")
    public Result findUserByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return userService.findUserByMeterNo(meterNo);
    }

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
     * @param money 充值金额
     * @param access_token 访问token
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":{"meterNo":"201603117852","money":100,"balance":100}}<br>
     * status：0 失败, 1 成功, 2 认证错误<br>
     * meterNo: 表号<br>
     * money: 充值金额<br>
     * balance: 账户余额<br>
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
     * 根据表号退费 <br>
     *
     * 请求地址：http://ip:port/path/joy/refundByMeterNo.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"meterNo":"201603117852","money":100,"access_token":""} <br>
     * meterNo: 表号<br>
     * money: 退费金额<br>
     * access_token: 访问token<br>
     *
     * @param req 请求上下文
     * @param meterNo 退费表号
     * @param money 退费金额
     * @param access_token 访问token
     * @return 返回JSON数据格式
     * <br>
     * 示例：{"status":1,"data":{"meterNo":"201603117852","money":100,"balance":100}}<br>
     * status：0 失败, 1 成功, 2 认证错误<br>
     * meterNo: 表号<br>
     * money: 退费金额<br>
     * balance: 账户余额<br>
     */
    @ResponseBody
    @RequestMapping("/refundByMeterNo.do")
    public Result refundByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo,
            @RequestParam("money") Double money) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return rechargeService.refundByMeterNo(meterNo, money, res.getData().toString());
    }

    /**
     * 根据表号清空余额 <br>
     *
     * 请求地址：http://ip:port/path/joy/clearBalanceByMeterNo.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"meterNo":"201603117852", "access_token":""} <br>
     *
     * @param req 请求上下文
     * @param meterNo 表号
     * @param access_token 访问token
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":{"meterNo":"201603117852","money":100,"balance":0}}<br>
     * status：0 失败, 1 成功, 2 认证错误, 3 查询到多个表计，拒绝退费, 4 用户不存在<br>
     * meterNo: 表号<br>
     * money: 退费金额<br>
     * balance: 账户余额<br>
     */
    @ResponseBody
    @RequestMapping("/clearBalanceByMeterNo.do")
    public Result clearBalanceByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo) {
        Result res = OAuth(access_token);
        try {
            if (res.getStatus() != 1) {
                return res;
            }
            final String operator = res.getData().toString();
            return rechargeService.refundByMeterNo(meterNo, operator);
        } catch (Exception ex) {
            res.setStatus(0);
            res.setData(null);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 根据表号控制继电器<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/controlByMeterNo.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"meterNo":"201603117852","action":"1","access_token":""}<br>
     * meterNo:表号<br>
     * action:动作(1: 强制开阀（闸） 0: 强制关阀（闸）)<br>
     * access_token: 访问token<br>
     *
     * @param req 请求上下文
     * @param meterNo 表号
     * @param action 强制开阀（闸）/ 强制关阀（闸）
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * 示例：{"status":1,"data":null} <br>
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/mbusControlByMeterNo.do")
    public Result mbusControlByMeterNo(HttpServletRequest req,
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
     * 根据表号异步控制继电器<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/controlByMeterNo_Async.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"meterNo":"201603117852","action":"1","access_token":""}<br>
     * meterNo:表号<br>
     * action:动作(1: 强制开阀（闸） 0: 强制关阀（闸）)<br>
     * access_token: 访问token<br>
     *
     * @param req 请求上下文
     * @param meterNo 表号
     * @param action 强制开阀（闸）/ 强制关阀（闸）
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * 示例：{"status":1,"data":null} <br>
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/mbusControlByMeterNo_Async.do")
    public Result mbusControlByMeterNo_Async(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo,
            @RequestParam("action") String action) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.ctrValveByMeterNo_Async(req, meterNo, action, res.getData().toString());
    }

    /**
     * 根据表号发送短信通知<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/sendSmsByMeterNo.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"meterNo":"201603117852","access_token":""} <br>
     * meterNo:表号<br>
     * access_token: 访问token<br>
     *
     * @param req
     * @param meterNo 表号
     * @param access_token 访问token
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":null}<br>
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/sendSmsByMeterNo.do")
    public Result sendSmsByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.sendSmsByMeterNo(meterNo, res.getData().toString());
    }

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
     * 根据批量异步根据表号抄表<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/readByMeterNo_Async.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"meterNo":"201603117852","access_token":""} <br>
     * meterNo: 表号<br>
     * 如果是多只表,格式为：={"meterNo":"201603117852,12341234,56785678"}<br>
     * access_token: 访问token<br>
     *
     * @param req 抄表请求
     * @param meterNo 表号
     * @param access_token 访问token
     * @return {"status":1,"data":"897604653"} status：0 失败, 1 成功, 2 认证错误, 4
     * 用户不存在<br>
     * data：起始抄表时间的时间戳, 根据这个时间戳通过 findReadResult 接口查询抄表数据<br>
     */
    @ResponseBody
    @RequestMapping("/readByMeterNo_Async.do")
    public Result readByMeterNo_Async(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.readByMeterNo_Async(req, meterNo, res.getData().toString());
    }

    /**
     * 根据表号抄表,同时回调给第三方,回调接口由第三方的指定,由我方负责实现.<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/readByMeterNo_Callback.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"meterNo":"201603117852","access_token":""} <br>
     * meterNo: 表号<br>
     * 如果是多只表,格式为：={"meterNo":"201603117852,12341234,56785678"}<br>
     * access_token: 访问token<br>
     *
     * @param req 抄表请求
     * @param meterNo 表号
     * @param access_token 访问token
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
    @RequestMapping("/readByMeterNo_Callback.do")
    public Result readByMeterNo_Callback(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.readByMeterNo_Callback(req, meterNo, res.getData().toString());
    }

    /**
     * 根据时间查询抄表记录<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/findReadInfoByDate.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"startDate":"2016-05-05","endDate":"2016-06-06","access_token":""}<br>
     *
     * @param req
     * @param startDate 起始时间
     * @param endDate 结束时间
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/findReadInfoByDate.do")
    public Result findReadInfoByDate(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.findReadInfoByDate(startDate, endDate);
    }

    /**
     * 根据时间查询抄表记录<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/page/findReadInfoByDate.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"startDate":"2016-05-05","endDate":"2016-06-06","access_token":"","page":1,"pageSize":3000}<br>
     *
     * @param req
     * @param startDate 起始时间
     * @param endDate 结束时间
     * @param access_token 访问token
     * @param page 当前页数
     * @param pageSize 每页显示数目
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/findReadInfoByDatePage.do")
    public Result findReadInfoByDatePage(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        int startNo = (page - 1) * pageSize;
        if (startNo <= 0) {
            startNo = 0;
        }
        return readService.findReadInfoByDatePage(startDate, endDate, startNo, pageSize);
    }

    /**
     * 根据时间查询抄表记录,不带总的记录条数<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/v2/findReadInfoByDate.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"startDate":"2016-05-05","endDate":"2016-06-06","access_token":""}<br>
     *
     * @param req
     * @param startDate 起始时间
     * @param endDate 结束时间
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/v2/findReadInfoByDate.do")
    public Result findReadInfoByDateWithoutTotalNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.findReadInfoByDateWithoutTotalNo(startDate, endDate);
    }

    /**
     * 根据时间(年月日时分秒)查询抄表记录<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/findReadInfoByDateTime.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"startDateTime":"2016-05-05 01:02:03",<br>
     * "endDateTime":"2016-06-06 11:02:03","access_token":""}<br>
     *
     * @param req
     * @param startDateTime 查询的起始时间
     * @param endDateTime 查询的结束时间
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/findReadInfoByDateTime.do")
    public Result findReadInfoByDateTime(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("startDateTime") String startDateTime,
            @RequestParam("endDateTime") String endDateTime) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.findReadInfoByDateTime(startDateTime, endDateTime);
    }

    /**
     * 根据时间(年月日时分秒)查询抄表记录<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/findReadInfoByDateTime.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"startDateTime":"2016-05-05 01:02:03",<br>
     * "endDateTime":"2016-06-06 11:02:03","access_token":""}<br>
     *
     * @param req
     * @param startDateTime 查询的起始时间
     * @param endDateTime 查询的结束时间
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/v2/findReadInfoByDateTime.do")
    public Result findReadInfoByDateTimeWithoutTotalNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("startDateTime") String startDateTime,
            @RequestParam("endDateTime") String endDateTime) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.findReadInfoByDateTime(startDateTime, endDateTime);
    }

    /**
     * 根据时间戳查询抄表记录<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/findReadResult.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"sendTime":"1234567890","access_token":""}<br>
     *
     * @param req
     * @param sendTime 时间戳
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/findReadResult.do")
    public Result findReadResult(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("sendTime") String sendTime) {
        Result res = OAuth(access_token);
        try {
            if (res.getStatus() != 1) {
                return res;
            }
            if (sendTime == null || sendTime.isEmpty()) {
                res.setStatus(0);
                res.setData(null);
                return res;
            }
            sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(sendTime));
            return readService.findReadResult(sendTime);
        } catch (NumberFormatException ex) {
            res.setStatus(0);
            res.setData(null);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 根据时间查询某只表的抄表记录<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/findReadInfoByMeterNoAndDate.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"meterNo":"12345","startDateTime":"2016-05-05 01:02:03",<br>
     * "endDateTime":"2016-06-06 11:02:03","access_token":""}<br>
     *
     * @param req
     * @param meterNo 查询的表号
     * @param startDateTime 查询的起始时间(格式：yyyy-MM-dd或yyyy-MM-dd HH:mm:ss)
     * @param endDateTime 查询的结束时间(格式：yyyy-MM-dd或yyyy-MM-dd HH:mm:ss)
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/findReadInfoByMeterNoAndDate.do")
    public Result findReadInfoByMeterNoAndDate(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meterNo") String meterNo,
            @RequestParam("startDateTime") String startDateTime,
            @RequestParam("endDateTime") String endDateTime) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return readService.findReadInfoByMeterNoAndDate(meterNo, startDateTime, endDateTime);
    }

    /**
     * 根据 省，市，区 查询小区<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/findCommunity.do<br>
     * 表单形式提交<br>
     * 请求参数格式示例：{"province":"","city":"", "district":"","access_token":""}<br>
     *
     * @param req
     * @param province 省
     * @param city 市
     * @param district 区
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/findCommunity.do")
    public Result findCommunity(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return userService.findAllCommunities(province, city, district);
    }

    /**
     * 根据 省，市，区 查询楼栋<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/findBilding.do<br>
     * 表单形式提交<br>
     *
     * 请求参数格式示例：{"province":"","city":"", "district":"", "community_name":"",
     * "access_token":""}<br>
     *
     * @param req
     * @param province 省
     * @param city 市
     * @param district 区
     * @param community_name 小区名称
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/findBilding.do")
    public Result findBilding(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("community_name") String community_name) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return userService.loadBuilding(province, city, district, community_name);
    }

    /**
     * 根据 省，市，区 查询单元
     *
     * 请求地址(POST)：http://ip:port/path/joy/findUnit.do<br>
     * 表单形式提交<br>
     *
     * 请求参数格式示例：{"province":"","city":"", "district":"",
     * "community_name":"","building_name":"", "access_token":""}<br>
     *
     * @param req 请求上下文
     * @param province 省
     * @param city 市
     * @param district 区
     * @param community_name 小区
     * @param building_name 楼栋
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/findUnit.do")
    public Result findUnit(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("community_name") String community_name,
            @RequestParam("building_name") String building_name) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return userService.loadUnit(province, city, district, community_name, building_name);
    }

    /**
     * 查询网关 <br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/findGateway.do<br>
     * 表单形式提交<br>
     *
     * 请求参数格式示例：{"community_name":"", "building_name":"", "unit_name":"",
     * "access_token":""}<br>
     *
     * @param req 请求上下文
     * @param community_name 小区名称
     * @param building_name 楼栋
     * @param unit_name 单元
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     * concentrator_name：网关名称<br>
     * DTU_sim_no：SIM卡号<br>
     * user_address_community：小区<br>
     * user_address_building：楼栋<br>
     * user_address_unit：单元<br>
     * add_time：添加时间<br>
     * operator_account：操作员<br>
     * concentrator_state：网关状态<br>
     * concentrator_no：网关编号<br>
     * concentrator_ip：网关服务器IP<br>
     * concentrator_port：网关服务器端口<br>
     * gateway_id：与网关服务器通讯的ID<br>
     * concentrator_model：网关模式(安装，启用，故障)<br>
     * public_key：与网关服务器通讯的公钥<br>
     */
    @ResponseBody
    @RequestMapping("/findGateway.do")
    public Result findGateway(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("community_name") String community_name,
            @RequestParam("building_name") String building_name,
            @RequestParam("unit_name") String unit_name) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return userService.loadConcentrators(community_name, building_name, unit_name, 0, 0);
    }

    /**
     * 查询网点 <br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/findPoint.do<br>
     * 表单形式提交<br>
     *
     * @param req 请求上下文
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/findPoint.do")
    public Result findPoint(HttpServletRequest req,
            @RequestParam("access_token") String access_token) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return userService.loadSuppliers("", "", "", 0, 0);
    }

    /**
     * 查询费率 <br>
     * 请求地址(POST)：http://ip:port/path/joy/findFeeTypes.do<br>
     * 表单形式提交<br>
     *
     * @param req 请求上下文
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/findFeeTypes.do")
    public Result findFeeTypes(HttpServletRequest req,
            @RequestParam("access_token") String access_token) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return userService.load_feeTypes();
    }

    /**
     * 根据 小区编号, 房间号, 表计类型 控制继电器<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/controlMeter.do<br>
     * 表单形式提交<br>
     *
     * 请求参数格式示例：{"communityNo":"201603117852", "roomNo":"201", "meterType":"10",
     * "action":"1","access_token":""}<br>
     *
     * @param req 请求上下文
     * @param communityNo 小区编号
     * @param roomNo 房间号
     * @param meterType 表计类型(10 冷水表，11 热水表，20 热表，30 气表，40 电表)
     * @param action 强制开阀（闸）/ 强制关阀（闸）
     * @param access_token 访问token
     * @return 返回JSON数据格式<br>
     *
     * 示例：{"status":1,"data":null} <br>
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/controlMeter.do")
    public Result controlMeter(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("communityNo") String communityNo,
            @RequestParam("roomNo") String roomNo,
            @RequestParam("meterType") String meterType,
            @RequestParam("action") String action) {
        Result res = OAuth(access_token);
        try {
            if (res.getStatus() != 1) {
                return res;
            }
            final String operator = res.getData().toString();
            res = userService.findMetersByRoomNo(communityNo, roomNo, meterType);
            if (res.getStatus() != 1) {
                return res;
            }
            List<User> meters = (List<User>) res.getData();
            if (res.getData() == null || meters.size() <= 0) {
                res.setStatus(0);
                return res;
            }
            for (User meter : meters) {
                res = readService.ctrValveByMeterNo(req, meter.getMeter_no(), action, operator);
            }
            return res;
        } catch (Exception ex) {
            res.setStatus(0);
            res.setData(null);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 根据 小区编号, 房间号, 表计类型 读表<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/readMeter.do<br>
     * 表单形式提交<br>
     *
     * 请求参数格式示例：{"communityNo":"201603117852", "roomNo":"201", "meterType":"10",
     * "access_token":""}<br>
     *
     * @param req 请求上下文
     * @param communityNo 小区编号
     * @param roomNo 房间号
     * @param meterType 表计类型(10 冷水表，11 热水表，20 热表，30 气表，40 电表)
     * @param access_token 访问token
     * @return 返回JSON数据格式 示例：
     *
     * {"status":1,"data":{"readInfo":[{"info_id":11,"operate_id":"CB20160622174612","user_name":"201603117852","user_address":"上海市辖区浦东新区欢乐颂公寓1幢2202",<br>
     * "user_address_area":"上海市辖区浦东新区","user_address_community":"欢乐颂公寓","user_address_building":"1幢","user_address_unit":"1单元",<br>
     * "user_address_room":"2202","contact_info":"18967449402","concentrator_name":"5253E65B","meter_no":"201603117852","fee_type":"电表费率",<br>
     * "this_read":1.18,"last_read":1.18,"this_cost":0.0,"last_cost":0.0,"fee_need":0.0,"balance":-4.54,"exception":"1","fee_status":"1","read_type":"1","pay_remind":null,<br>
     * "operator_account":"admin","operate_time":1466588772000,"operate_day":"20160622","operate_dayTime":"17:46:12","meter_type":"40",<br>
     * "data2":0.0,"data3":0.0,"data4":0.0,"data5":0.0,"data6":0.0,"data7":0.0,"data8":0.0,"data9":""}],"totalNo":1}}<br>
     *
     * status: status：0 失败, 1 成功, 2 认证错误, 4 用户不存在<br>
     * readInfo: 查到的抄表返回数据 <br>
     * info_id: 记录序号<br>
     * operate_id: 抄表编号<br>
     * user_name: 用户名称<br>
     * user_address: 用户地址<br>
     * user_address_area:区<br>
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
     */
    @ResponseBody
    @RequestMapping("/readMeter.do")
    public Result readMeter(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("communityNo") String communityNo,
            @RequestParam("roomNo") String roomNo,
            @RequestParam("meterType") String meterType) {
        Result res = OAuth(access_token);
        try {
            if (res.getStatus() != 1) {
                return res;
            }
            final String operator = res.getData().toString();
            res = userService.findMetersByRoomNo(communityNo, roomNo, meterType);
            if (res.getStatus() != 1) {
                return res;
            }
            List<User> meters = (List<User>) res.getData();
            if (res.getData() == null || meters.size() <= 0) {
                res.setStatus(4);
                return res;
            }
            StringBuilder buf = new StringBuilder();
            meters.forEach((meter) -> {
                if (!buf.toString().isEmpty()) {
                    buf.append(",");
                }
                buf.append(meter.getMeter_no());
            });
            return readService.readByMeterNo(req, buf.toString(), operator);
        } catch (Exception ex) {
            res.setStatus(0);
            res.setData(null);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 多表抄表 请求地址(POST)：http://ip:port/path/joy/readMeters.do<br>
     *
     * @param req 请求上下文
     * @param jsonObj 格式{"rooms":[{"communityNo":"201603117852", "roomNo":"201",
     * "meterType":"10"}, ... ,{"communityNo":"201603117852", "roomNo":"201",
     * "meterType":"10"}]}
     * @param access_token 访问token
     * @return 返回结构同上
     */
    @ResponseBody
    @RequestMapping("/readMeters.do")
    public Result readMeters(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("jsonObj") String jsonObj) {
        Result res = OAuth(access_token);
        try {
            if (res.getStatus() != 1) {
                return res;
            }
            final String operator = res.getData().toString();
            JSONObject obj = JSONObject.parseObject(jsonObj);
            JSONArray rooms = obj.getJSONArray("rooms");
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < rooms.size(); i++) {
                JSONObject room = (JSONObject) rooms.get(i);
                String communityNo = room.getString("communityNo");
                String roomNo = room.getString("roomNo");
                String meterType = room.getString("meterType");
                res = userService.findMetersByRoomNo(communityNo, roomNo, meterType);
                if (res.getStatus() != 1 || res.getData() == null) {
                    continue;
                }
                List<User> meters = (List<User>) res.getData();
                meters.stream()
                        .filter((meter) -> !(meter.getMeter_no() == null || meter.getMeter_no().length() <= 0))
                        .forEachOrdered((meter) -> {
                            if (!buf.toString().isEmpty()) {
                                buf.append(",");
                            }
                            buf.append(meter.getMeter_no());
                        });
            }
            return readService.readByMeterNo(req, buf.toString(), operator);
        } catch (Exception ex) {
            res.setStatus(0);
            res.setData(null);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 多表抄表 请求地址(POST)：http://ip:port/path/joy/readMeters_Async.do<br>
     *
     * @param req 请求上下文
     * @param jsonObj 格式{"rooms":[{"communityNo":"201603117852", "roomNo":"201",
     * "meterType":"10"}, ... ,{"communityNo":"201603117852", "roomNo":"201",
     * "meterType":"10"}]}
     * @param access_token 访问token
     * @return 返回JSON数据格式 示例：
     *
     * {"status":1,"data":"897604653"} status：0 失败, 1 成功, 2 认证错误, 4 用户不存在<br>
     * data：起始抄表时间的时间戳
     */
    @ResponseBody
    @RequestMapping("/readMeters_Async.do")
    public Result readMeters_Async(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("jsonObj") String jsonObj) {
        Result res = OAuth(access_token);
        try {
            if (res.getStatus() != 1) {
                return res;
            }
            final String operator = res.getData().toString();
            JSONObject obj = JSONObject.parseObject(jsonObj);
            JSONArray rooms = obj.getJSONArray("rooms");
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < rooms.size(); i++) {
                JSONObject room = (JSONObject) rooms.get(i);
                String communityNo = room.getString("communityNo");
                String roomNo = room.getString("roomNo");
                String meterType = room.getString("meterType");
                res = userService.findMetersByRoomNo(communityNo, roomNo, meterType);
                if (res.getStatus() != 1 || res.getData() == null) {
                    continue;
                }
                List<User> meters = (List<User>) res.getData();
                meters.stream().filter((meter) -> !(meter.getMeter_no() == null || meter.getMeter_no().length() <= 0)).forEachOrdered((meter) -> {
                    if (!buf.toString().isEmpty()) {
                        buf.append(",");
                    }
                    buf.append(meter.getMeter_no());
                });
            }
            return readService.readByMeterNo_Async(req, buf.toString(), operator);
        } catch (Exception ex) {
            res.setStatus(0);
            res.setData(null);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 根据 小区编号, 房间号, 表计类型 充值 <br>
     *
     * 请求地址：http://ip:port/path/joy/recharge.do<br>
     * 表单形式提交<br>
     *
     * 请求参数格式示例：{"communityNo":"201603117852", "roomNo":"201",
     * "meterType":"10","money":100,"access_token":""} <br>
     *
     * @param req 请求上下文
     * @param communityNo 小区编号
     * @param roomNo 房间号
     * @param meterType 表计类型(10 冷水表，11 热水表，20 热表，30 气表，40 电表)
     * @param money 充值金额
     * @param access_token 访问token
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":{"meterNo":"201603117852","money":100,"balance":100}}<br>
     * status：0 失败, 1 成功, 2 认证错误, 3 查询到多个表计，拒绝充值, 4 用户不存在<br>
     * meterNo: 表号<br>
     * money: 充值金额<br>
     * balance: 账户余额<br>
     */
    @ResponseBody
    @RequestMapping("/recharge.do")
    public Result recharge(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("communityNo") String communityNo,
            @RequestParam("roomNo") String roomNo,
            @RequestParam("meterType") String meterType,
            @RequestParam("money") Double money) {
        Result res = OAuth(access_token);
        try {
            if (res.getStatus() != 1) {
                return res;
            }
            final String operator = res.getData().toString();
            res = userService.findMetersByRoomNo(communityNo, roomNo, meterType);
            if (res.getStatus() != 1) {
                return res;
            }
            List<User> meters = (List<User>) res.getData();
            if (res.getData() == null || meters.size() <= 0) {
                res.setStatus(4);
                return res;
            }
            if (meters.size() > 1) {
                res.setStatus(3);
                return res;
            }
            User user = meters.get(0);
            return rechargeService.rechargeByMeterNo(user.getMeter_no(), money, operator);
        } catch (Exception ex) {
            res.setStatus(0);
            res.setData(null);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 根据 小区编号, 房间号, 表计类型 退费 <br>
     *
     * 请求地址：http://ip:port/path/joy/refund.do<br>
     * 表单形式提交<br>
     *
     * 请求参数格式示例：{"communityNo":"201603117852", "roomNo":"201",
     * "meterType":"10","money":100,"access_token":""} <br>
     *
     * @param req 请求上下文
     * @param communityNo 小区编号
     * @param roomNo 房间号
     * @param meterType 表计类型(10 冷水表，11 热水表， 20 热表，30 气表，40 电表)
     * @param money 退费金额
     * @param access_token 访问token
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":{"meterNo":"201603117852","money":100,"balance":100}}<br>
     * status：0 失败, 1 成功, 2 认证错误, 3 查询到多个表计，拒绝退费, 4 用户不存在<br>
     * meterNo: 表号<br>
     * money: 退费金额<br>
     * balance: 账户余额<br>
     */
    @ResponseBody
    @RequestMapping("/refund.do")
    public Result refund(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("communityNo") String communityNo,
            @RequestParam("roomNo") String roomNo,
            @RequestParam("meterType") String meterType,
            @RequestParam("money") Double money) {
        Result res = OAuth(access_token);
        try {
            if (res.getStatus() != 1) {
                return res;
            }
            final String operator = res.getData().toString();
            res = userService.findMetersByRoomNo(communityNo, roomNo, meterType);
            if (res.getStatus() != 1) {
                return res;
            }
            List<User> meters = (List<User>) res.getData();
            if (res.getData() == null || meters.size() <= 0) {
                res.setStatus(4);
                return res;
            }
            if (meters.size() > 1) {
                res.setStatus(3);
                return res;
            }
            User user = meters.get(0);
            return rechargeService.refundByMeterNo(user.getMeter_no(), money, operator);
        } catch (Exception ex) {
            res.setStatus(0);
            res.setData(null);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 根据 小区编号, 房间号, 表计类型 清空余额 <br>
     *
     * 请求地址：http://ip:port/path/joy/clearBalance.do<br>
     * 表单形式提交<br>
     *
     * 请求参数格式示例：{"communityNo":"201603117852", "roomNo":"201", "meterType":"10",
     * "access_token":""} <br>
     *
     * @param req 请求上下文
     * @param communityNo 小区编号
     * @param roomNo 房间号
     * @param meterType 表计类型(10 冷水表，11 热水表， 20 热表，30 气表，40 电表)
     * @param access_token 访问token
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":{"meterNo":"201603117852","money":100,"balance":0}}<br>
     * status：0 失败, 1 成功, 2 认证错误, 3 查询到多个表计，拒绝退费, 4 用户不存在<br>
     * meterNo: 表号<br>
     * money: 退费金额<br>
     * balance: 账户余额<br>
     */
    @ResponseBody
    @RequestMapping("/clearBalance.do")
    public Result clearBalance(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("communityNo") String communityNo,
            @RequestParam("roomNo") String roomNo,
            @RequestParam("meterType") String meterType) {
        Result res = OAuth(access_token);
        try {
            if (res.getStatus() != 1) {
                return res;
            }
            final String operator = res.getData().toString();
            res = userService.findMetersByRoomNo(communityNo, roomNo, meterType);
            if (res.getStatus() != 1) {
                return res;
            }
            List<User> meters = (List<User>) res.getData();
            if (res.getData() == null || meters.size() <= 0) {
                res.setStatus(4);
                return res;
            }
            if (meters.size() > 1) {
                res.setStatus(3);
                return res;
            }
            User user = meters.get(0);
            return rechargeService.refundByMeterNo(user.getMeter_no(), operator);
        } catch (Exception ex) {
            res.setStatus(0);
            res.setData(null);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 根据 小区编号, 房间号, 表计类型 发送短信<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/sendSms.do<br>
     * 表单形式提交<br>
     *
     * 请求参数格式示例：{"communityNo":"201603117852", "roomNo":"201", "meterType":"10",
     * "access_token":""}<br>
     *
     * @param req 请求上下文
     * @param communityNo 小区编号
     * @param roomNo 房间号
     * @param meterType 表计类型(10 冷水表，11 热水表，20 热表，30 气表，40 电表)
     * @param access_token 访问token
     * @return 返回JSON数据格式
     *
     * 示例：{"status":1,"data":null} <br>
     * status: 0 失败, 1 成功, 2 认证错误, 4 用户不存在<br>
     */
    @ResponseBody
    @RequestMapping("/sendSms.do")
    public Result sendSms(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("communityNo") String communityNo,
            @RequestParam("roomNo") String roomNo,
            @RequestParam("meterType") String meterType) {
        Result res = OAuth(access_token);
        try {
            if (res.getStatus() != 1) {
                return res;
            }
            final String operator = res.getData().toString();
            res = userService.findMetersByRoomNo(communityNo, roomNo, meterType);
            if (res.getStatus() != 1) {
                return res;
            }
            List<User> meters = (List<User>) res.getData();
            if (res.getData() == null || meters.size() <= 0) {
                res.setStatus(4);
                return res;
            }
            for (User meter : meters) {
                res = readService.sendSmsByMeterNo(meter.getMeter_no(), operator);
            }
            return res;
        } catch (Exception ex) {
            res.setStatus(0);
            res.setData(null);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 根据 小区编号, 房间号, 表计类型 更新房间信息<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/updateRoom.do<br>
     * 表单形式提交<br>
     *
     * @param req 请求上下文
     * @param communityNo 小区编号
     * @param roomNo 房间号
     * @param meterType 表类型
     * @param meter_no 表号
     * @param origin_room 原房间号
     * @param access_token 访问 token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误, 3 查询到多个表计，拒绝更新, 4 用户不存在<br>
     */
    @ResponseBody
    @RequestMapping("/updateRoomByNo.do")
    public Result updateRoomByNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("communityNo") String communityNo,
            @RequestParam("roomNo") String roomNo,
            @RequestParam("meterType") String meterType,
            @RequestParam("meter_no") String meter_no,
            @RequestParam("origin_room") String origin_room) {
        Result res = OAuth(access_token);
        try {
            if (res.getStatus() != 1) {
                return res;
            }
            final String operator = res.getData().toString();
            res = userService.findMetersByRoomNo(communityNo, roomNo, meterType);
            if (res.getStatus() != 1) {
                return res;
            }
            List<User> meters = (List<User>) res.getData();
            if (res.getData() == null || meters.size() <= 0) {
                res.setStatus(4);
                return res;
            }
            if (meters.size() > 1) {
                res.setStatus(3);
                return res;
            }
            User user = meters.get(0);
            user.setMeter_no(meter_no);
            user.setUser_address_original_room(origin_room);
            user.setOperator_account(operator);
            return userService.updateRoom(user);
        } catch (Exception ex) {
            res.setStatus(0);
            res.setData(null);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 根据 小区名称, 房间号, 表计类型 更新房间信息<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/updateRoom.do<br>
     * 表单形式提交<br>
     *
     * @param req 请求上下文
     * @param communityName 小区名称
     * @param roomNo 房间号
     * @param meterType 表类型
     * @param meter_no 表号
     * @param origin_room 原房间号
     * @param access_token 访问 token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误, 3 查询到多个表计，拒绝更新, 4 用户不存在<br>
     */
    @ResponseBody
    @RequestMapping("/updateRoomByName.do")
    public Result updateRoomByName(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("communityName") String communityName,
            @RequestParam("roomNo") String roomNo,
            @RequestParam("meterType") String meterType,
            @RequestParam("meter_no") String meter_no,
            @RequestParam("origin_room") String origin_room) {
        Result res = OAuth(access_token);
        try {
            if (res.getStatus() != 1) {
                return res;
            }
            final String operator = res.getData().toString();
            res = userService.findMetersByRoomName(communityName, origin_room, meterType);
            if (res.getStatus() != 1) {
                return res;
            }
            List<User> meters = (List<User>) res.getData();
            if (res.getData() == null || meters.size() <= 0) {
                res.setStatus(4);
                return res;
            }
            if (meters.size() > 1) {
                res.setStatus(3);
                return res;
            }
            User user = meters.get(0);
            user.setMeter_no(meter_no);
            user.setUser_address_room(roomNo);
            user.setOperator_account(operator);
            return userService.updateRoom(user);
        } catch (Exception ex) {
            res.setStatus(0);
            res.setData(null);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 根据 表号 更新房间信息<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/updateRoomByMeterNo.do<br>
     * 表单形式提交<br>
     *
     * @param req 请求上下文
     * @param roomNo 房间号
     * @param meter_no 表号
     * @param access_token 访问 token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误, 3 查询到多个表计，拒绝更新, 4 用户不存在<br>
     */
    @ResponseBody
    @RequestMapping("/updateRoomByMeterNo.do")
    public Result updateRoomByMeterNo(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("meter_no") String meter_no,
            @RequestParam("roomNo") String roomNo) {
        Result res = OAuth(access_token);
        try {
            if (res.getStatus() != 1) {
                return res;
            }
            final String operator = res.getData().toString();
            User user = userDao.findUserByMeterNo(meter_no);
            if (user == null) {
                res.setStatus(4);
                return res;
            }
            user.setMeter_no(meter_no);
            user.setUser_address_room(roomNo);
            user.setOperator_account(operator);
            return userService.updateRoom(user);
        } catch (Exception ex) {
            res.setStatus(0);
            res.setData(null);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 根据 楼栋编号或名称 查询房间号及房间对应的表号<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/findMeterNoByBulidingId.do<br>
     * 表单形式提交<br>
     *
     * @param req 请求上下文
     * @param bulidingId 楼栋编号或名称
     * @param access_token 访问 token
     * @return 返回JSON数据格式<br>
     *
     * 示例：{"status":1,"data":[{"user_address_room":"11",
     * "meter_no":"12345678","meter_type":"10"}]} <br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/findMeterNoByBulidingId.do")
    public Result findRoomAndMeterNoByBulidingId(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("bulidingId") String bulidingId) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return userService.findRoomAndMeterNoByBulid(bulidingId);
    }

    /**
     * 根据 小区编号 查询房间信息<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/findRoom.do<br>
     * 表单形式提交<br>
     *
     * @param req 请求上下文
     * @param communityNo 小区编号
     * @param access_token 访问 token
     * @return 返回JSON数据格式<br>
     *
     * status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/findRoom.do")
    public Result findRoom(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("communityNo") String communityNo) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        return userService.findUsersByRoom(communityNo);
    }

    /**
     * 推送入住,退房的信息
     *
     * @param req
     * @param access_token 访问token
     * @param json
     * {"shopId":"门店ID"，"roomId":"房间ID","action":"in/out","peoples":"1",
     * "datatime":"2017-10-29 16:14:15"}<br>
     * @return status: 0 失败, 1 成功, 2 认证错误<br>
     */
    @ResponseBody
    @RequestMapping("/checkInOut.do")
    public Result checkInOut(HttpServletRequest req,
            @RequestParam("access_token") String access_token,
            @RequestParam("json") String json) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        res.setStatus(1);
        return res;
    }

    /**
     * 根据表号查询该表某段时间内的用水历史记录(带分页功能，需求由复恒麦家公寓提出)
     *
     * @author mengshuai
     * @date 2018-03-21
     * @version v1.0.0
     *
     * 返回内容格式示例:{"status":1,"data":{"total":23,"items":[{"this_read":12,"read_time":"2017-08-09
     * 13:23:23"},{...}]}}
     *
     * @param access_token:授权认证
     * @param meter_no:表号
     * @param start_time:开始时间 格式为yyyy-MM-dd HH:mm:ss
     * @param end_time:结束时间 格式同上
     * @param page:当前页数
     * @param pageSize:每页显示数目
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryHistoryRecordByMeterNo.do")
    public Result queryHistoryRecordByMeterNo(@RequestParam("access_token") String access_token,
            @RequestParam("meter_no") String meter_no,
            @RequestParam("start_time") String start_time,
            @RequestParam("end_time") String end_time,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize) {
        Result res = OAuth(access_token);
        if (res.getStatus() != 1) {
            return res;
        }
        int startNo = (page - 1) * pageSize;
        if (startNo < 0) {
            startNo = 0;
        }
        return readService.queryHistoryRecordByMeterNo(meter_no, start_time, end_time, startNo, pageSize);
    }
}
