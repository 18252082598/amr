package com.joymeter.service.callbackImp;

import com.alibaba.fastjson.JSONObject;
import com.joymeter.entity.ReadInfo;
import com.joymeter.entity.User;
import com.joymeter.util.JoyLogger;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 数据回调
 *
 * @author Wu Wei
 * @date 2017-8-14 13:49:04
 */
public class CallBackServiceImp_KaiLi extends CallBackServiceImp {

    //缓存回调数据
    public static ConcurrentHashMap<String, LinkedList<String>> cacheMap;

    static {
        cacheMap = new ConcurrentHashMap<String, LinkedList<String>>() {
            {
                put("addUser", new LinkedList<>());
                put("updateUser", new LinkedList<>());
                put("changeMeter", new LinkedList<>());
                put("readData", new LinkedList<>());
                put("valveData", new LinkedList<>());
            }
        };
    }

    /**
     * 注册用户
     *
     * @param user
     */
    @Override
    public void callBackData(User user) {
        this.callBackUser(user, "addUser");
    }

    /**
     * 更新用户
     *
     * @param user
     */
    @Override
    public void updateUser(User user) {
        this.callBackUser(user, "updateUser");
    }

    /**
     * 换表
     *
     * @param params
     */
    @Override
    public void changeMeterNo(Map<String, Object> params) {
        try {
            Map<String, String> meterParams = new HashMap<>();
            meterParams.put("oldMeterAddr", (String) params.get("meter_no"));
            meterParams.put("meterAddr", (String) params.get("new_meter_no"));
            String jsonParams = JSONObject.toJSONString(meterParams);
            cacheMap.get("changeMeter").add(jsonParams);
            JoyLogger.LogInfo(String.format("changeMeterNo old meter no.:%s, new meter no.", params.get("oldMeterAddr"), params.get("meterAddr")));
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * 抄表或开关阀
     *
     * @param info
     */
    @Override
    public void callBackReadInfo(ReadInfo info) {
        try {
            Map<String, String> params = new HashMap<>();
            if ("10".equals(info.getIsAutoClear())) {//阀门操作
                String processResult = info.getException();
                String processDesc = "1".equals(processResult) ? "成功" : "失败";
                params.put("meterAddr", info.getMeter_no());
                params.put("ifProcess", "2");
                params.put("processDate", info.getOperate_time().toString());
                params.put("processResult", processResult);
                params.put("processDesc", processDesc);
                String jsonParams = JSONObject.toJSONString(params);
                cacheMap.get("valveData").add(jsonParams);
                JoyLogger.LogInfo(String.format("callBackReadInfo res: %s, meter no.:%s", 10, params.get("meterAddr")));
            } else if ("1".equals(info.getException())) {// 抄表成功
                if (info.getThis_read() == null) {
                    return;
                }
                params.put("factoryId", "5");
                params.put("meterAddr", info.getMeter_no());
                params.put("readNumber", info.getThis_read().toString());
                params.put("readDate", info.getOperate_time().toString());
                String jsonParams = JSONObject.toJSONString(params);
                cacheMap.get("readData").add(jsonParams);
                JoyLogger.LogDebug(String.format("callBackReadInfo res: %s, meter no.:%s", 1, params.get("meterAddr")));
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * 注册或修改用户信息
     *
     * @param user
     * @param action
     */
    private void callBackUser(User user, String action) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("factoryId", "5");
            if ("updateUser".equals(action)) {//从催缴字段取出旧表号
                String originalMeterAddr = user.getPay_remind();
                String newMeterAddr = user.getMeter_no();
                params.put("originalMeterAddr", originalMeterAddr);
                if (!Objects.equals(newMeterAddr, originalMeterAddr)) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("meter_no", originalMeterAddr);
                    map.put("new_meter_no", newMeterAddr);
                    this.changeMeterNo(map);
                }
            }
            params.put("meterAddr", user.getMeter_no());
            params.put("userName", user.getUser_name());
            params.put("phone", user.getContact_info());
            params.put("paperNo", user.getId_card_no());
            String address = user.getUser_address_community() + user.getUser_address_building() + user.getUser_address_unit() + user.getUser_address_room();
            params.put("address", address);
            params.put("caliber", "20");
            params.put("installDate", new Timestamp(System.currentTimeMillis()).toString());
            String protocol = user.getValve_protocol();
            params.put("ifCtrlValve", (protocol == null || protocol.isEmpty()) ? "0" : "1");
            params.put("userCode", user.getSubmeter_no());
            String jsonParams = JSONObject.toJSONString(params);
            cacheMap.get(action).add(jsonParams);
            JoyLogger.LogInfo(String.format("callBackUser meter no.:%s", params.get("meterAddr")));
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
}
