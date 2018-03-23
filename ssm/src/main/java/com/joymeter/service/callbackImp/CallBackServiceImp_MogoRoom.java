/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.service.callbackImp;

import com.joymeter.cache.ReadInfoCache;
import com.joymeter.entity.ReadInfo;
import com.joymeter.util.HttpClient;
import com.joymeter.util.JoyLogger;
import com.joymeter.util.JoyDatetime;
import com.joymeter.util.Util;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yinhf
 */
public class CallBackServiceImp_MogoRoom extends CallBackServiceImp {

    public static String access_token = "mushRoom";
    public static String client_id = "123456789";
    public static String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

    @Override
    public void callBackReadInfo(ReadInfo readInfo) {
        Map<String, String> map = new HashMap<>();
        try {
            String url = values.get("read_callback_url");
            if (readInfo == null || !"0".equals(readInfo.getIsAutoClear()) || url == null || url.isEmpty()) {
                return;
            }
            CallBackServiceImp.callBackJsonFormat = "{\"event\":\"%s\",\"uuid\":\"%s\",\"curr_amount\":\"%s\",\"timestamp\":\"%s\",\"access_token\":\"%s\",\"client_id\":\"%s\",\"code\":\"%s\",\"sign\":\"%s\"}}";
            String uuid = readInfo.getUser_address_original_room() == null ? "" : readInfo.getUser_address_original_room();
            String curr_amount = readInfo.getThis_read() == null ? "" : String.valueOf(readInfo.getThis_read());
            map.put("event", "elemeterRead");
            map.put("uuid", uuid);
            map.put("curr_amount", curr_amount);
            map.put("timestamp", timestamp);
            map.put("access_token", access_token);
            map.put("client_id", client_id);
            String sign = Util.md5(Util.increasByASCII(map, true)).toUpperCase();
            String param = String.format(CallBackServiceImp.callBackJsonFormat,
                    "elemeterRead",
                    uuid,
                    curr_amount,
                    timestamp,
                    access_token,
                    client_id,
                    readInfo.getException().equals("0") ? "1" : "0",
                    sign);
            String res = HttpClient.sendPost(url, param);
            JoyLogger.LogInfo(String.format("callBackReadInfo res: %s, json: %s", res, param));
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void callBackValveState(ReadInfo readInfo) {
        Map<String, String> map = new HashMap<>();
        try {
            String url = values.get("valve_callback_url");
            if (readInfo == null || !"10".equals(readInfo.getIsAutoClear()) || url == null || url.isEmpty()) {
                return;
            }
            CallBackServiceImp.callBackJsonFormat = "{\"event\":\"%s\",\"uuid\":\"%s\",\"operation_type\":\"%s\",\"access_token\":\"%s\",\"client_id\":\"%s\",\"timestamp\":\"%s\",\"sign\":\"%s\",\"code\":\"%s\"}}";
            String uuid = readInfo.getUser_address_original_room() == null ? "" : readInfo.getUser_address_original_room();
            String operation_type = "0".equals(readInfo.getData9()) ? "3" : "4";
            map.put("event", "elemeterControl");
            map.put("uuid", uuid);
            map.put("operation_type", operation_type);
            map.put("timestamp", timestamp);
            map.put("access_token", access_token);
            map.put("client_id", client_id);
            String sign = Util.md5(Util.increasByASCII(map, true)).toUpperCase();
            String params = String.format(CallBackServiceImp.callBackJsonFormat,
                    "elemeterControl",
                    uuid,
                    operation_type,
                    access_token,
                    client_id,
                    timestamp,
                    sign,
                    readInfo.getException().equals("0") ? "1" : "0");
            String res = HttpClient.sendPost(url, params);
            JoyLogger.LogInfo(String.format("callBackValveState res: %s, json: %s", res, params));
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void schedulerCallBackReadInfo(Calendar cal, ReadInfoCache readInfoCache) {
        if (cal.get(Calendar.MINUTE) != 0) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        String url = values.get("data_callback_url");
        CallBackServiceImp.callBackJsonFormat = "{\"event\":\"%s\",\"uuid\":\"%s\",\"curr_amount\":\"%f\",\"time\":\"%s\",\"access_token\":\"%s\",\"client_id\":\"%s\",\"timestamp\":\"%s\",\"sign\":\"%s\"}}";
        Collection<ReadInfo> readInfo = readInfoCache.findReadInfoCurrent();
        readInfo.stream().forEach(r -> {
            try {
                if (url == null || url.isEmpty() || r == null
                        || r.getException().equals("0") || r.getThis_read() == null) {
                    return;
                }
                map.put("event", "elemeterPowerAsync");
                map.put("uuid", r.getUser_address_original_room());
                map.put("curr_amount", String.valueOf(r.getThis_read()));
                map.put("time", r.getOperate_time().toString());
                map.put("timestamp", timestamp);
                map.put("access_token", access_token);
                map.put("client_id", client_id);
                String sign = Util.md5(Util.increasByASCII(map, true)).toUpperCase();
                String param = String.format(CallBackServiceImp.callBackJsonFormat,
                        "elemeterPowerAsync",
                        r.getUser_address_original_room(),
                        r.getThis_read(),
                        JoyDatetime.tenTimeStamp(r.getOperate_time()),
                        access_token,
                        client_id,
                        timestamp,
                        sign);
                String res = HttpClient.sendPost(url, param);
                JoyLogger.LogInfo(String.format("schedulerCallBackReadInfo res: %s, param:%s", res, param));
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        });
    }
}
