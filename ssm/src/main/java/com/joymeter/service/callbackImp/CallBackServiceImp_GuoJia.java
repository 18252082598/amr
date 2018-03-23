/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.service.callbackImp;

import com.joymeter.cache.ReadInfoCache;
import com.joymeter.entity.ReadInfo;
import com.joymeter.util.DESEncrypt;
import com.joymeter.util.HttpClient;
import com.joymeter.util.JoyLogger;
import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yinhf
 */
public class CallBackServiceImp_GuoJia extends CallBackServiceImp {

    DESEncrypt desEncrypt = new DESEncrypt("Pt3OL6xM");
    private final String account = "gj_chao_yi";
    private final String password = desEncrypt.encrypt("123456");
    private final String callBackJson = "{\"status\":%s,\"data\":{\"meterNo\":\"%s\",\"event\":\"%s\",\"data\":\"%s\",\"datetime\":\"%s\",\"account\":\"%s\",\"password\":\"%s\"}}";

    @Override
    public void callBackReadInfo(ReadInfo readInfo) {
        try {
            String url = values.get("read_callback_url");
            if (url == null || url.isEmpty() || readInfo == null || readInfo.getThis_read() == null || readInfo.getException().equals("0")) {
                return;
            }
            final String value = String.format(callBackJson,
                    1,
                    readInfo.getMeter_no(),
                    "data",
                    readInfo.getThis_read(),
                    readInfo.getOperate_time(),
                    account,
                    password);
            final String res = HttpClient.sendPost(url, value);
            JoyLogger.LogInfo(String.format("callBackReadInfo res: %s, json: %s", res, value));
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void schedulerCallBackReadInfo(Calendar cal, ReadInfoCache readInfoCache) {
        if (cal.get(Calendar.MINUTE) != 0) {
            return;
        }
        String url = values.get("read_callback_url");
        Collection<ReadInfo> rfc = readInfoCache.findReadInfoCurrent();
        rfc.stream().forEach(p -> {
            try {
                if (url == null || url.isEmpty() || p == null
                        || p.getThis_read() == null || p.getException().equals("0")) {
                    return;
                }
                String param = String.format(callBackJson,
                        1,
                        p.getMeter_no(),
                        "data",
                        p.getThis_read(),
                        p.getOperate_time(),
                        account,
                        password);
                String res = HttpClient.sendPost(url, param);
                JoyLogger.LogInfo(String.format("schedulerCallBackReadInfo res: %s, param:%s", res, param));
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
