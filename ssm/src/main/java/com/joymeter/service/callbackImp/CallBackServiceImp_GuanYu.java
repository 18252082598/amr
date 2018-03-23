package com.joymeter.service.callbackImp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.joymeter.cache.CallBackCache;
import com.joymeter.entity.ReadInfo;
import com.joymeter.entity.RoomInfo;
import com.joymeter.entity.User;
import com.joymeter.util.HttpClient;
import com.joymeter.util.JoyLogger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 数据回调
 *
 * @author Wu Wei
 * @date 2017-7-25 13:49:04
 */
public class CallBackServiceImp_GuanYu extends CallBackServiceImp {

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
            String data = "[{\"meterType\":\"%s\",\"meterNo\":\"%s\",\"roomId\":\"%s\"}]";
            String meterType = user.getMeter_type();
            String meterNo = user.getMeter_no();
            String roomId = user.getOnline_synv_code();
            Map<String, String> params = getParams(meterNo);
            params.put("data", String.format(data, meterType, meterNo, roomId));
            String res = HttpClient.sendPost(url, params);
            JoyLogger.LogInfo(String.format("callBackData res: %s, meter no.:%s, read: %s", res, params.get("meterNo"), params.get("data")));
            if (res != null && !res.isEmpty()) {
                String code = JSONObject.parseObject(res).getString("success");
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
     * 回调抄表返回数据(只回调电表的自动抄表数据)
     *
     * @param readInfo
     */
    @Override
    public void callBackReadInfo(ReadInfo readInfo) {
        try {
            String url = values.get("read_callback_url");
            if (url == null || url.isEmpty() || readInfo == null || "0".equals(readInfo.getException())
                    || readInfo.getThis_read() == null || !"0".equals(readInfo.getIsAutoClear()) // 0 表示自动抄表
                    || !"40".equals(readInfo.getMeter_type())) { // 40 表示电表
                return;
            }
            Map<String, String> params = getParams(readInfo.getMeter_no());
            params.put("read", String.valueOf(readInfo.getThis_read()));
            String res = HttpClient.sendPost(url, params);
            String code = JSON.parseObject(res).getString("success");
            JoyLogger.LogInfo(String.format("callBackReadInfo res: %s, meter no.:%s, read: %s", res, params.get("meterNo"), params.get("read")));
            if (!"true".equals(code.toLowerCase())) {
                CallBackCache.addReadCallbackFailed(readInfo);
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            CallBackCache.addReadCallbackFailed(readInfo);
        }
    }

    /**
     * 阀门关闭回调
     *
     * @param readInfo
     */
    @Override
    public void callBackValveState(ReadInfo readInfo) {
        String url = values.get("valve_callback_url");
        if (url == null || url.isEmpty() || readInfo == null) {
            return;
        }
        Map<String, String> params = this.getParams(readInfo.getMeter_no());
        params.put("result", "0");
        params.put("action", "1".equals(readInfo.getData9()) ? "2" : "1");
        params.put("desc", "失败");
        try {
            if ("0".equals(readInfo.getException()) || readInfo.getData9() == null || readInfo.getData9().isEmpty()) { // 阀门
                params.put("result", "0");
                params.put("action", "1".equals(readInfo.getData9()) ? "2" : "1");
                params.put("desc", readInfo.getContact_info());
            } else {
                params.put("result", "1");
                params.put("action", "1".equals(readInfo.getData9()) ? "2" : "1");
                params.put("desc", "1".equals(readInfo.getData9()) ? "合闸" : "拉闸");
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            return;
        }
        try {
            String res = HttpClient.sendPost(url, params);
            JoyLogger.LogInfo(String.format("callBackValveState res: %s, result:%s, meter no.:%s, action: %s, desc: %s",
                    res,
                    params.get("result"),
                    params.get("meterNo"),
                    params.get("action"),
                    params.get("desc")));
            CallBackCache.remove(readInfo.getMeter_no());
            if (res == null || res.isEmpty()) {
                CallBackCache.addValveCallbackFailed(readInfo);
                return;
            }
            String code = JSON.parseObject(res).getString("success");
            if (!"true".equals(code.toLowerCase())) {
                CallBackCache.addValveCallbackFailed(readInfo);
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            CallBackCache.addValveCallbackFailed(readInfo);
        }
    }

    /**
     * 执行回调信息
     */
    @Override
    public void schedulerCallBackReadInfo(Calendar cal) {
        if (cal.get(Calendar.MINUTE) % 15 == 0) {
            CallBackCache.CallBack(userDao);
        }
    }

    /**
     * 获取回调的参数
     *
     * @param meterNo
     * @return
     */
    private Map<String, String> getParams(String meterNo) {
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        String randomStr = getRandomStr(8);
        String accToken = DigestUtils.sha1Hex(timestamp + values.get("key") + randomStr);
        Map<String, String> params = new HashMap<>();
        params.put("clientId", values.get("client_id"));
        params.put("timestamp", timestamp);
        params.put("randomStr", randomStr);
        params.put("accToken", accToken);
        params.put("meterNo", meterNo);
        return params;
    }

    /**
     * 获取一个n位的随机字符串（数字+字符）
     *
     * @param n 随机数位数
     * @return
     */
    public static String getRandomStr(int n) {
        if (n <= 0) {
            return "";
        }
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        StringBuilder str = new StringBuilder("");
        int len = chars.length;
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            int index = r.nextInt(len);
            str.append(chars[index]);
        }
        return str.toString();
    }

    @Override
    public void updateUser(User user) {
        this.callBackData(user);
    }
}
