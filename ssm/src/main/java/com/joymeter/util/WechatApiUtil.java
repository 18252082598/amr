package com.joymeter.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.joymeter.entity.Result;
import com.joymeter.entity.User;

public class WechatApiUtil {
    
    /**
     * 
     * @param params
     * @return
     */
    public static String payOrRefund(Map<String,String> params) {
        String pathUrl = PropertiesUtil.readConfig("pathUrl", "wechat.properties");
        String url = pathUrl+PropertiesUtil.readConfig("payOrRefund", "wechat.properties");
        String result = HttpClient.sendPost(url, params);
        return result;
    }
    /**
     * 
     * @return
     */
    public static String getCjoyId() { 
        return PropertiesUtil.readConfig("cjoyId","wechat.properties");
    }
    /**
     * 
     * @return
     */
    public static String getPostUrl() {
        String postUrl =  PropertiesUtil.readConfig("pathUrl","wechat.properties")+PropertiesUtil.readConfig("postUrl","wechat.properties");
        return postUrl;
    }
    /**
     * 
     * @param result
     * @param statusResult
     * @param id_card_no
     */
    public static void sendRealDataByIdCardNo(Result result,Result statusResult,String id_card_no) {
        Map<String, String> params = new HashMap<>();
        params.put("result", JSONObject.toJSONString(result));
        params.put("accountNo", id_card_no);
        params.put("cjoyId", PropertiesUtil.readConfig("cjoyId","wechat.properties"));
        params.put("access_token", RSACoder.getAccess_token());
        params.put("statusResult", JSONObject.toJSONString(statusResult));
        String url = PropertiesUtil.readConfig("pathUrl","wechat.properties")+PropertiesUtil.readConfig("sendRealDataByIdCardNo","wechat.properties");
        HttpClient.sendPost(url, params);
    }
    
    /**
     * 
     * @param result
     * @param statusResult
     * @param meterNo
     */
    public static void sendRealDataByMeterNo(Result result,Result statusResult,String meterNo) {
        Map<String, String> params = new HashMap<>();
        params.put("result", JSONObject.toJSONString(result));
        params.put("meterNo", meterNo);
        params.put("cjoyId", PropertiesUtil.readConfig("cjoyId","wechat.properties"));
        params.put("access_token", RSACoder.getAccess_token());
        params.put("statusResult", JSONObject.toJSONString(statusResult));
        String url = PropertiesUtil.readConfig("pathUrl","wechat.properties")+PropertiesUtil.readConfig("sendRealDataByMeterNo","wechat.properties");
        HttpClient.sendPost(url, params);  
    }
    /**
     * 
     * @param user
     * @param original_meter_no
     */
    public static void sendUpdateUser(User user,String original_meter_no) {
        Map<String, String> params = new HashMap<>();
        params.put("cjoyId", PropertiesUtil.readConfig("cjoyId","wechat.properties"));
        params.put("access_token", RSACoder.getAccess_token());
        params.put("original_meter_no",original_meter_no );
        params.put("meter_no",user.getMeter_no() );
        String url = PropertiesUtil.readConfig("pathUrl","wechat.properties")+PropertiesUtil.readConfig("updateUser","wechat.properties");
        HttpClient.sendPost(url, params); 
    }
 
    
}
