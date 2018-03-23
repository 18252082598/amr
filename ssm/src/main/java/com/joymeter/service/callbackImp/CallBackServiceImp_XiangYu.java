/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.service.callbackImp;

import com.joymeter.cache.ReadInfoCache;
import com.joymeter.entity.ReadInfo;
import com.joymeter.util.HttpClient;
import com.joymeter.util.JoyDatetime;
import com.joymeter.util.JoyLogger;
import com.joymeter.util.JoyDatetime;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zhong Fuqiang
 * @version 1.0.0
 *
 */
public class CallBackServiceImp_XiangYu extends CallBackServiceImp {

    /**
     * 回调抄表返回数据
     *
     * @param readInfoCache
     */
    @Override
    public void schedulerCallBackReadInfo(Calendar cal, ReadInfoCache readInfoCache) {
        if (cal.get(Calendar.MINUTE) != 0) {
            return;
        }
        String url = values.get("read_callback_url");
        String event = "elemeterPowerAsync";
        String elemeterType = "102";
        CallBackServiceImp.callBackJsonFormat = "{\"event\":\"%s\",\"uuid\":\"%s\",\"time\":%d,\"elemeterType\":\"%s\",\"detail\":{\"consume_amount\":%f,\"consume_amount_time\":%d}}";
        Collection<ReadInfo> rfc = readInfoCache.findReadInfoCurrent();
        cal.add(Calendar.HOUR, - 1);
        rfc.stream().filter(r -> r.getOperate_time().compareTo(JoyDatetime.dateToTimestamp(cal.getTime())) >= 0)
                .forEach(p -> {
                    try {
                        if (url == null || url.isEmpty() || p == null
                                || p.getException().equals("0") || p.getThis_read() == null) {
                            return;
                        }
                        Double consume_amount = p.getThis_read();
                        Timestamp operate_time = p.getOperate_time();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date parseTime = sdf.parse(operate_time.toString());
                        long consume_amount_time = parseTime.getTime();
                        String uuid = p.getUser_address_original_room();
                        String param = String.format(CallBackServiceImp.callBackJsonFormat,
                                event,
                                uuid,
                                consume_amount_time,
                                elemeterType,
                                consume_amount,
                                JoyDatetime.TimeEndHour());
                        String res = HttpClient.sendPost(url, param);
                        JoyLogger.LogInfo(String.format("schedulerCallBackReadInfo res: %s, param:%s", res, param));
                    } catch (ParseException ex) {
                        Logger.getLogger(CallBackServiceImp_XiangYu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
    }
}
