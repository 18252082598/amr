/*
Provide interfaces to MogoRoom Rental
 */
package com.joymeter.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.joymeter.cache.EventRecordCache;
import com.joymeter.cache.ReadInfoCache;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.EventRecord;
import com.joymeter.entity.ReadInfo;
import com.joymeter.entity.User;
import com.joymeter.service.ReadService;
import com.joymeter.service.UserService;
import com.joymeter.util.AesCBC;
import com.joymeter.util.JoyDatetime;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author yinhf
 */
@Controller
@RequestMapping("/mogo")
public class MogoRoomApi {

    @Resource
    private UserService userService;
    @Resource
    private ReadService readService;
    @Resource
    private UserDao userDao;
    @Resource
    private EventRecordCache eventRecordCache;
    @Resource
    private ReadInfoCache readInfoCache;

    final String resultMsg = "{\"reqId\":\"%s\", \"code\":\"%s\",\"message\":\"%s\"}";

    /**
     * 根据电表 uuid 获取电表基本信息
     *
     * @param client_secret 鉴权标识
     * @param uuid 电表唯一标识
     * @return 电表基本信息
     */
    @ResponseBody
    @RequestMapping(value = "/elemeter_basic_info.do", method = RequestMethod.GET)
    public Map<String, Object> elemeter_basic_info(
            @RequestParam("client_secret") final String client_secret,
            @RequestParam("uuid") final String uuid) {
        Map<String, Object> rs = this.OAuth(client_secret);
        if ("1".equals(rs.get("code"))) {
            return rs;
        }
        return userService.elemeter_basic_info(uuid);
    }

    /**
     * 根据电表 uuid 向电表发起读取指令，采集最新读数，待采集完成后回调给平台
     *
     * @param req
     * @param client_secret 鉴权标识
     * @param uuid 电表唯一标识
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/elemeter_read.do", method = RequestMethod.POST)
    public String elemeter_read(HttpServletRequest req,
            @RequestParam("client_secret") final String client_secret,
            @RequestParam("uuid") final String uuid) {
        Map<String, Object> rs = this.OAuth(client_secret);
        if ("1".equals(rs.get("code"))) {
            return String.format(resultMsg, rs.get("reqId"), "1", rs.get("message"));
        }
        return readService.readByUUID(req, uuid);
    }

    /**
     * 根据电表 uuid 设置电表合闸，即电表通电
     *
     * @param req
     * @param client_secret 鉴权标识
     * @param uuid 电表唯一标识
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/elemeter_switch_on.do", method = RequestMethod.POST)
    public String elemeter_switch_on(HttpServletRequest req,
            @RequestParam("client_secret") final String client_secret,
            @RequestParam("uuid") final String uuid) {
        Map<String, Object> rs = this.OAuth(client_secret);
        if ("1".equals(rs.get("code"))) {
            return String.format(resultMsg, rs.get("reqId"), "1", rs.get("message"));
        }
        String result = readService.ctrValveByUUID(req, "1", uuid);
        return this.switchMessage(result);
    }

    /**
     * 根据电表 uuid 设置电表跳闸，即电表断电
     *
     * @param req
     * @param client_secret 鉴权标识
     * @param uuid 电表唯一标识
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/elemeter_switch_off.do", method = RequestMethod.POST)
    public String elemeter_switch_off(HttpServletRequest req,
            @RequestParam("client_secret") final String client_secret,
            @RequestParam("uuid") final String uuid) {
        Map<String, Object> rs = this.OAuth(client_secret);
        if ("1".equals(rs.get("code"))) {
            return String.format(resultMsg, rs.get("reqId"), "1", rs.get("message"));
        }
        String result = readService.ctrValveByUUID(req, "0", uuid);
        return this.switchMessage(result);
    }

    /**
     * 根据电表 uuid 列表批量获取服务器已采集的最新电表读数
     *
     * @param client_secret 鉴权标识
     * @param uuids 多个电表唯一标识
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/elemeter_fetch_power_batch.do", method = RequestMethod.POST)
    public Map<String, Object> elemeter_fetch_power_batch(
            @RequestParam("client_secret") final String client_secret,
            @RequestParam("uuids") String[] uuids) {
        Map<String, Object> rs = this.OAuth(client_secret);
        if ("1".equals(rs.get("code"))) {
            return rs;
        }
        Map<String, Object> readResult = new HashMap<>();
        List<Map<String, Object>> listValues = new ArrayList<>();
        readResult.put("code", "1");
        readResult.put("message", "fail");
        readResult.put("reqId", String.valueOf(JoyDatetime.getID()));
        String uuid = "";
        int length = uuids.length;
        try {
            for (int i = 0; i < length; i++) {
                uuid = uuids[i];
                User user = userDao.getMeterByUUID(uuid);
                Map<String, Object> amountResult = new HashMap<>();
                if (user == null) {
                    readResult.put("message", "User " + uuid + " does not exist");
                    return readResult;
                }
                ReadInfo readInfo = readInfoCache.findLastReadInfoByMeterNo(user.getMeter_no());
                amountResult.put("uuid", user.getUser_address_original_room());
                amountResult.put("consume_amount", readInfo.getThis_read());
                amountResult.put("time", JoyDatetime.tenTimeStamp(readInfo.getOperate_time()));
                listValues.add(amountResult);
            }
            readResult.put("amount", listValues);
            readResult.put("code", "0");
            readResult.put("message", "successful");
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }

        return readResult;
    }

    /**
     * 根据电表 uuid 查询异常记录
     *
     * @param client_secret 鉴权标识
     * @param uuid 电表唯一标识
     * @param count 本次拉取的个数，默认是 50
     * @param offset 拉取的偏移量，默认为 0
     * @param start_time 查询起始时间戳 (10 位)， end_time 查询结束时间戳（10 位）
     * @return 1003:设备的电量低 1004 ：设备离线
     */
    @ResponseBody
    @RequestMapping(value = "/elemeter_exceptions.do", method = RequestMethod.GET)
    public Map<String, Object> elemeter_exceptions(
            @RequestParam("client_secret") final String client_secret,
            @RequestParam("uuid") final String uuid,
            @RequestParam(value = "count", required = false) Integer count,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "start_time", required = false) Long start_time,
            @RequestParam(value = "end_time", required = false) Long end_time) {
        Map<String, Object> rs = this.OAuth(client_secret);
        if ("1".equals(rs.get("code"))) {
            return rs;
        }
        Map<String, Object> evResult = new HashMap<>();
        List<Map<String, Object>> listValues = new ArrayList<>();
        final String flag = "exceptions";
        evResult.put("code", "1");
        evResult.put("message", "fail");
        evResult.put("reqId", String.valueOf(JoyDatetime.getID()));
        try {
            User user = userDao.getMeterByUUID(uuid);
            if (user == null) {
                evResult.put("message", "User does not exist");
                return evResult;
            }
            List<EventRecord> er = eventRecordCache.getEventRecord(user, count, offset, start_time, end_time, flag);
            evResult.put("code", "0");
            evResult.put("message", "No operation log ");
            if (!er.isEmpty()) {
                er.forEach((val) -> {
                    Map<String, Object> exceptions = new HashMap<>();
                    exceptions.put("time", JoyDatetime.tenTimeStamp(val.getReport_time()));
                    exceptions.put("excp_id", val.getError_code());
                    exceptions.put("name", val.getEvent());
                    exceptions.put("desc", val.getError_msg());
                    listValues.add(exceptions);
                });
                evResult.put("exceptions", listValues);
                evResult.put("message", "successful");
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return evResult;
    }

    /**
     * 根据电表 uuid 查询电表操作记录
     *
     * @param client_secret 鉴权标识
     * @param uuid 电表唯一标识
     * @param count 本次拉取的个数，默认是 50 ，offset 拉取的偏移量，默认为 0
     * @param start_time 查询起始时间戳 (10 位)， end_time 查询结束时间戳（10 位）
     * @return 0000：读取电表 ，3001 ：开阀 ，3000：关阀
     */
    @ResponseBody
    @RequestMapping(value = "/elemeter_operation_log.do", method = RequestMethod.GET)
    public Map<String, Object> elemeter_operation_log(
            @RequestParam("client_secret") final String client_secret,
            @RequestParam("uuid") final String uuid,
            @RequestParam(value = "count", required = false) Integer count,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "start_time", required = false) Long start_time,
            @RequestParam(value = "end_time", required = false) Long end_time) {
        Map<String, Object> rs = this.OAuth(client_secret);
        if ("1".equals(rs.get("code"))) {
            return rs;
        }
        Map<String, Object> evResult = new HashMap<>();
        List<Map<String, Object>> listValues = new ArrayList<>();
        final String flag = "operations";
        evResult.put("code", "1");
        evResult.put("message", "fail");
        evResult.put("reqId", String.valueOf(JoyDatetime.getID()));
        try {
            User user = userDao.getMeterByUUID(uuid);
            if (user == null) {
                evResult.put("message", "User does not exist");
                return evResult;
            }
            List<EventRecord> er = eventRecordCache.getEventRecord(user, count, offset, start_time, end_time, flag);
            evResult.put("code", "0");
            evResult.put("message", "No operation log ");
            if (!er.isEmpty()) {
                er.forEach((val) -> {
                    Map<String, Object> op_list = new HashMap<>();
                    Map<String, Object> operatorMap = new HashMap<>();
                    op_list.put("time", JoyDatetime.tenTimeStamp(val.getReport_time()));
                    op_list.put("op_id", val.getEvent_id());
                    op_list.put("op_type", val.getError_code());
                    op_list.put("desc", val.getError_msg());
                    op_list.put("operator", operatorMap);
                    operatorMap.put("id", val.getOperator_id());
                    operatorMap.put("name", "");
                    listValues.add(op_list);
                });
                evResult.put("op_list ", listValues);
                evResult.put("message", "successful");
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return evResult;
    }

    /**
     * 返回阀门操作结果
     *
     * @return
     */
    public String switchMessage(final String reslut) {
        JSONObject jo = JSON.parseObject(reslut);
        String code = jo.getString("resultCode");
        String message = jo.getString("resultMsg");
        return String.format(resultMsg, String.valueOf(JoyDatetime.getID()), code, message);
    }

    /**
     * 验证访问有效性
     *
     * @return 0：成功 1：失败
     */
    public Map<String, Object> OAuth(final String client_secret) {
        Map<String, Object> securt = new HashMap<>();
        securt.put("code", "1");
        securt.put("reqId", String.valueOf(JoyDatetime.getID()));
        securt.put("message", "Client_secret is not avaliable");
        try {
            if (client_secret == null || client_secret.isEmpty()) {
                return securt;
            }
            String context = AesCBC.getInstance().decrypt(client_secret, "utf-8");
            if ("01234567890123456456456".equals(context)) {
                securt.put("code", "0");
                return securt;
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return securt;
    }

    @ResponseBody
    @RequestMapping("/detailSync.do")
    public Object detailSync(HttpServletRequest re) throws IOException {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(re.getInputStream()))) {
            String jsonStr = null;
            StringBuilder result = new StringBuilder();
            try {
                while ((jsonStr = reader.readLine()) != null) {
                    result.append(jsonStr);
                }
            } catch (IOException e) {
            }
            System.out.println("返回数据" + result);
            // 关闭输入流
        }

        return null;
    }

}
