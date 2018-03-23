package com.joymeter.service.callbackImp;

import com.alibaba.fastjson.JSONObject;
import com.joymeter.entity.User;
import com.joymeter.util.HttpClient;
import com.joymeter.util.JoyLogger;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 数据回调
 *
 * @author Wu Wei
 * @date 2017-7-25 13:49:04
 */
public class CallBackServiceImp_BiGuiYuan extends CallBackServiceImp_Common {

    /**
     * 更新用户
     *
     * @param user
     */
    @Override
    public void updateUser(User user) {
        if (user == null) {
            return;
        }
        if (user.getMeter_no() == null || user.getMeter_no().isEmpty()) {
            return;
        }
        if (user.getPay_remind() == null || user.getPay_remind().isEmpty()) {
            return;
        }
        if (!user.getMeter_no().equals(user.getPay_remind())) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("meter_no", user.getPay_remind());
        params.put("new_meter_no", user.getMeter_no());
        this.changeMeterNo(params);
    }

    /**
     * 换表
     *
     * @param params
     */
    @Override
    public void changeMeterNo(Map<String, Object> params) {
        try {
            String url = values.get("read_callback_url");
            if (params == null) {
                return;
            }
            Map<String, String> meterParams = new HashMap<>();
            meterParams.put("old", (String) params.get("meter_no"));
            meterParams.put("new", (String) params.get("new_meter_no"));
            String value = JSONObject.toJSONString(meterParams);
            final String res = HttpClient.sendPost(url, value);
            JoyLogger.LogInfo(String.format("changeMeterNo res:%s, old meter no.:%s, new meter no.", res, params.get("oldMeterAddr"), params.get("meterAddr")));
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
}
