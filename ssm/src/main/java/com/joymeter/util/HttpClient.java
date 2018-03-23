package com.joymeter.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 1. 通过HttpClient实现Get方法响应<br>
 * 2. 通过HttpClient实现Post方法带参数传入的响应
 */
public class HttpClient {

    private static final RequestConfig REQ_CONFIG = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();

    /**
     * 检测Url的合法性
     *
     * @param url
     * @return
     */
    public static boolean CheckUrl(final String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        final String regEx = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    /**
     * 向服务器发送post请求
     *
     * @param url
     * @param params
     * @return
     */
    public static String sendPost(String url, Map<String, String> params) {
        try {
            if (url == null || url.isEmpty() || params == null || params.isEmpty()) {
                return "ERROR";
            }
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(REQ_CONFIG);
            List<NameValuePair> entitys = new ArrayList<>();
            params.entrySet().forEach((entry) -> {
                entitys.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            });
            httpPost.setEntity(new UrlEncodedFormEntity(entitys));
            HttpResponse res = HttpClients.createDefault().execute(httpPost);
            if (res.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = res.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, "utf-8");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        return "ERROR";
    }

    /**
     * 向服务器发送post请求
     *
     * @param url
     * @param json
     * @return
     */
    public static String sendPost(final String url, final String json) {
        try {
            String msg = String.format("sendPost Url: %s, json: %s", url, json);
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, msg);
            if (json == null || json.isEmpty()) {
                return "ERROR";
            }
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("version", "1.0");
            httpPost.setHeader("s_id", UUID.randomUUID().toString().replace("-", ""));
            httpPost.setEntity(new StringEntity(json, "UTF-8"));
            HttpResponse res = HttpClients.createDefault().execute(httpPost);
            if (res.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = res.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, "utf-8");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        return "ERROR";
    }

    /**
     * 向服务器发送get请求
     *
     * @param url
     * @return
     */
    public static String sendGet(final String url) {
        try {
            if (url == null || url.isEmpty()) {
                return "ERROR";
            }
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(REQ_CONFIG);

            HttpResponse response = HttpClients.createDefault().execute(httpGet); // 获取到response对象
            // 如果返回值为200，则请求成功，可以通过TestNG做判断 HttpStatus.SC_OK
            int status = response.getStatusLine().getStatusCode();
            System.out.println("当前请求status： " + status);
            // 获取Http Headers信息
            Header[] headers = response.getAllHeaders();
            int headerLength = headers.length;
            for (int i = 0; i < headerLength; i++) {
                System.out.println("Header内容为： " + headers[i]);
            }
            // 获取到请求的内容
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } catch (IOException ex) {
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
    }
}
