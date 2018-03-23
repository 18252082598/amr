package com.joymeter.service.callbackImp;

import com.alibaba.fastjson.JSONObject;
import com.joymeter.entity.ReadInfo;
import com.joymeter.entity.RoomInfo;
import com.joymeter.entity.User;
import com.joymeter.util.HttpClient;
import com.joymeter.util.JoyDatetime;
import com.joymeter.util.JoyLogger;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 数据回调
 *
 * @author Wu Wei
 * @date 2017-7-25 13:49:04
 */
public class CallBackServiceImp_Common extends CallBackServiceImp {

    /**
     * 回调绑定表号的房源信息给客户
     *
     * @param user
     */
    @Override
    public void callBackData(User user) {
        try {
            String url = values.get("data_callback_url");
            if (user == null || url == null || url.isEmpty()) {
                return;
            }
            RoomInfo info = userDao.findRoomInfoByOnlineSyncCode(user.getOnline_synv_code());
            if (info == null) {
                return;
            }
            String json = "{\"onlineSyncCode\":\"%s\",\"meterNo\":\"%s\",\"meterType\":\"%s\",\"datetime\":\"%s\"}";
            String meterType = user.getMeter_type();
            String meterNo = user.getMeter_no();
            String roomId = user.getOnline_synv_code();
            SimpleDateFormat sdf = new SimpleDateFormat(JoyDatetime.FORMAT_DATETIME_2);
            String datetime = sdf.format(user.getAdd_time());
            json = String.format(json, roomId, meterNo, meterType, datetime);
            String result = HttpClient.sendPost(url, json);
            JoyLogger.LogInfo(String.format("callBackData res: %s, meter no.:%s, read: %s", result, meterNo, json));
            if (result != null && !result.isEmpty()) {
                JSONObject obj = JSONObject.parseObject(result);
                String code = obj.getString("success");
                if (code == null) {
                    return;
                }
                // 更新回调状态
                if (code.toLowerCase().equals("true")) {
                    info.setCallBackStatus((short) 0);
                    userDao.updateCallBackStatus(info);
                }
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * 回调抄表返回数据
     *
     * @param readInfo
     */
    @Override
    public void callBackReadInfo(ReadInfo readInfo) {
        try {
            String url = values.get("read_callback_url");
            if (url == null || url.isEmpty() || readInfo == null
                    || readInfo.getException().equals("0") || readInfo.getThis_read() == null) {
                return;
            }
            final String value = String.format(CallBackServiceImp.callBackJsonFormat,
                    1,
                    readInfo.getMeter_no(),
                    "data",
                    readInfo.getThis_read(),
                    readInfo.getOperate_time());
            final String res = HttpClient.sendPost(url, value);
            JoyLogger.LogInfo(String.format("callBackReadInfo res: %s, json: %s", res, value));
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * 余额预警回调
     *
     * @param readInfo
     */
    @Override
    public void callBackBalanceWarn(ReadInfo readInfo) {
        try {
            String url = values.get("read_callback_url");
            if (url == null || url.isEmpty() || readInfo == null
                    || readInfo.getException().equals("0") || readInfo.getThis_read() == null) {
                return;
            }
            final String value = String.format(CallBackServiceImp.callBackJsonFormat,
                    1,
                    readInfo.getMeter_no(),
                    "warning",
                    readInfo.getBalance(),
                    readInfo.getOperate_time());
            final String res = HttpClient.sendPost(url, value);
            JoyLogger.LogInfo(String.format("callBackBalanceWarn res: %s,  json: %s", res, value));
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * 阀门关闭回调
     *
     * @param readInfo
     */
    @Override
    public void callBackValveState(ReadInfo readInfo) {
        try {
            String url = values.get("read_callback_url");
            if (url == null || url.isEmpty() || readInfo == null
                    || readInfo.getException().equals("0") || readInfo.getThis_read() == null) {
                return;
            }
            final String value = String.format(CallBackServiceImp.callBackJsonFormat,
                    1,
                    readInfo.getMeter_no(),
                    "close",
                    readInfo.getBalance(),
                    readInfo.getOperate_time());
            final String res = HttpClient.sendPost(url, value);
            JoyLogger.LogInfo(String.format("callBackValveClose res: %s, json: %s", res, value));
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
}
