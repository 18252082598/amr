/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.joymeter.entity.Result;
import com.joymeter.util.RSACoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zhong Fuqiang
 * @version 1.0.0
 */
public class BaseApi {

    private final String privateKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAlB4WtWGYehZg3A+RcVVJyl3Cxn8GdWGxS4Ya+jN+BzJ17pDucVpOa80Ea7FtKqxKZq1AsoRlaQ8MPM/9i/vM9wIDAQABAkBYL23fweKRVb+HZbF3Y4sSdC5iFgDXZDm1uHtZWc0AJQ+RLuMdnf8pINksn2qKaTbKnZPkTsdFXKDaImzgdB8hAiEA0FM8ytDWbAkXoVKfUnKu4JsqZ9V9WDhNcOWCiFDkrAcCIQC2A5QXya8ioRA23HhmpITL1fDHQTHvth0psCD87/l7kQIhAKuxfm/1UFM35ybJDpAH/sMUfw2WsTiNJp3+OeeZK+3vAiEAqITrqpKbSo6hOooqF66fHTK5uWJNX8nXem+OO2HcNWECIBFcYqXCrErUOZBYexishcMqGP/XoUEGJAqn4bEY6Tf0";

    /**
     * 对access_tokne 进行认证
     *
     * @param access_token
     * @return 0 失败, 1 成功, 2 过期
     */
    protected Result OAuth(final String access_token) {
        Result res = new Result();
        res.setStatus(2);
        try {
            if (access_token == null || access_token.isEmpty()) {
                return res;
            }
            byte[] encodedData = RSACoder.decryptBASE64(access_token);
            byte[] decodedData = RSACoder.decryptByPrivateKey(encodedData, privateKey);
            String resutl = new String(decodedData);
            JSONObject obj = (JSONObject) JSON.parse(resutl);
            final String client_id = obj.getString("client_id");
            final String datetime = obj.getString("datetime");
            if (!client_id.equals("joy000001")) {
                return res;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = sdf.parse(datetime);
            long dif = (System.currentTimeMillis() - date.getTime()) / 1000 / 60;
            if (dif > 10 || dif < -10) {
                res.setStatus(2);
                return res;
            }
            res.setData(client_id);
            res.setStatus(1);
            return res;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        final String access_token = "V7PTQogFTJ1mmkcEi/gXO7N59HESGpKeMHg3ArpH+sasugRVWaTONQTg3veMtr33RLOjMcOu2ts3Xs5npeSLrw==";
        BaseApi api  = new BaseApi();
        Result res = api.OAuth(access_token);
        System.err.println("加密前: " + res.getData());
    }
}
