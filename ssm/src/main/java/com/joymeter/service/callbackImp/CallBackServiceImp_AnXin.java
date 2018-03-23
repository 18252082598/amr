package com.joymeter.service.callbackImp;

import com.alibaba.fastjson.JSONObject;
import com.joymeter.entity.RoomInfo;
import com.joymeter.entity.User;
import com.joymeter.util.HttpClient;
import com.joymeter.util.JoyDatetime;
import com.joymeter.util.JoyLogger;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 数据回调
 *
 * @author Wu Wei
 * @date 2017-7-25 13:49:04
 */
public class CallBackServiceImp_AnXin extends CallBackServiceImp_Common {

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
     * 换表
     *
     * @param params
     */
    @Override
    public void changeMeterNo(Map<String, Object> params) {
        try {
            if (params == null) {
                return;
            }
            String old_meter_no = (String) params.get("meter_no");
            String new_meter_no = (String) params.get("new_meter_no");
            User user = userDao.findUserByMeterNo(new_meter_no);
            this.callBackData(user);
            JoyLogger.LogInfo("old_meter_no=" + old_meter_no + " new_meter_no=" + new_meter_no);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
}
