/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.service.tokenImp;

import com.alibaba.fastjson.JSONObject;
import com.joymeter.api.TokenApi;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.Result;
import com.joymeter.entity.User;
import com.joymeter.service.ReadService;
import com.joymeter.service.TokenService;
import com.joymeter.service.saasImp.SaaSType;
import com.joymeter.util.HttpRequest;
import com.joymeter.util.JoyDatetime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

/**
 * 码表码的管理(设置码(key change token), 充值码, 换表码, 清零码)
 *
 * @author Zhong Fuqiang
 * @version 1.0.0
 */
@Service
public class TokenServiceImp implements TokenService {

    private final String JSON_REQUEST_TOKEN_FROMAT = "{'method':'%s','oldmeter':'%s', 'meter':'%s', 'ver':'%s','arith':'%s','thistimes':'%s','amount':'%s'}";
    private final String Url = "http://122.225.71.66:1433/ts/token";

    @Resource
    private UserDao userDao;
    @Resource
    private ReadService readService;

    /**
     * 设置码
     *
     * @param meter_no 表号
     * @return 8位返回设置码， 20位返回key change token
     */
    @Override
    public Result setToken(final String meter_no) {
        Result res = new Result();
        res.setStatus(0);
        try {
            User user = userDao.findUserByMeterNo(meter_no);
            if (user == null || user.getProtocol_type() == null || !user.getProtocol_type().equals("4")) {
                res.setStatus(5);
                return res;
            }
            int arith = (int) (user.getHouse_area() / 160) + 1;
            String param = String.format(JSON_REQUEST_TOKEN_FROMAT, "set", meter_no, meter_no, user.getProtocol_type(), arith, 0, 0);
            String result = HttpRequest.sendGet(Url, Base64.encodeBase64String(param.getBytes()));
            if (result == null || result.isEmpty() || "1".equals(result)) {
                res.setStatus(6); // 出码错误
                return res;
            }
            // 去掉首位引号
            String replaceStr = result.substring(1, result.length() - 1);
            JSONObject obj = JSONObject.parseObject(replaceStr);
            result = obj.getString("result");
            if ("0".equals(result)) {
                // 保存设置码
                String token = obj.getString("token");
                Map<String, Object> map = new HashMap<>();
                map.put("meter_no", meter_no);
                map.put("token", token);
                userDao.updateSetToken(map);
                res.setStatus(1);
                res.setData(token);
                return res;
            }
        } catch (Exception ex) {
            Logger.getLogger(TokenApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 充值码
     *
     * @param meter_no 表号
     * @param amount
     * @param operator
     * @return 8位返回充值码， 20位返回credit token
     */
    @Override
    public Result creditToken(final String meter_no, int amount, final String operator) {
        Result res = new Result();
        res.setStatus(0);
        try {
            User user = userDao.findUserByMeterNo(meter_no);
            if (user == null || user.getProtocol_type() == null || !user.getProtocol_type().equals("4")) {
                res.setStatus(5);
                return res;
            }
            int arith = (int) (user.getHouse_area() / 160) + 1;//算法因子
            int thistimes = (int) (user.getHouse_area() % 160) + 1;
            String param = String.format(JSON_REQUEST_TOKEN_FROMAT, "credit", meter_no, meter_no, user.getProtocol_type(), arith, thistimes, amount);
            String result = HttpRequest.sendGet(Url, Base64.encodeBase64String(param.getBytes()));
            if (result == null || result.isEmpty() || "1".equals(result)) {
                res.setStatus(6); // 出码错误
                return res;
            }
            // 去掉首位引号
            String replaceStr = result.substring(1, result.length() - 1);
            JSONObject obj = JSONObject.parseObject(replaceStr);
            result = obj.getString("result");
            if ("0".equals(result)) {
                // 更新购买次数
                Map<String, Object> map = new HashMap<>();
                map.put("meter_no", meter_no);
                map.put("thistimes", thistimes);
                userDao.updateCreditTimes(map);
                // 保存充值数据 
                String token = obj.getString("token");
                String saveInfo = String.format(SaaSType.JSON_FORMAT_SAVE_INFO, meter_no, "token", result, amount, "0", "0", "0", "0", "0", "0", "0", token, JoyDatetime.dateFormat(new Date(), JoyDatetime.FORMAT_DATETIME_2),
                        "1", "", "", "2", operator, "", "");
                readService.saveReadInfo(saveInfo);
                res.setStatus(1);
                res.setData(token);
                return res;
            }
        } catch (Exception ex) {
            Logger.getLogger(TokenApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * 换表码
     *
     * @param meter_no 表号
     * @param new_meter_no 新表号
     * @param amount 表内余量
     * @return 8位返回设置码 + 充值码， 20位返回credit token
     */
    @Override
    public Result changeToken(final String meter_no, final String new_meter_no, int amount) {
        return null;
    }

    /**
     * 清零码
     *
     * @param meter_no 表号
     * @param operator
     * @return 8位返回清零码， 20位返回clear token
     */
    @Override
    public Result clearToken(final String meter_no, final String operator) {
        Result res = new Result();
        res.setStatus(0);
        try {
            User user = userDao.findUserByMeterNo(meter_no);
            if (user == null || user.getProtocol_type() == null || !user.getProtocol_type().equals("4")) {
                res.setStatus(5);
                return res;
            }
            int arith = (int) (user.getHouse_area() / 160) + 1;
            int thistimes = (int) (user.getHouse_area() % 160) + 1;
            String param = String.format(JSON_REQUEST_TOKEN_FROMAT, "clear", meter_no, meter_no, user.getProtocol_type(), arith, thistimes, 0);
            String result = HttpRequest.sendGet(Url, Base64.encodeBase64String(param.getBytes()));
            if (result == null || result.isEmpty() || "1".equals(result)) {
                res.setStatus(6); // 出码错误
                return res;
            }
            // 去掉首位引号
            String replaceStr = result.substring(1, result.length() - 1);
            JSONObject obj = JSONObject.parseObject(replaceStr);
            result = obj.getString("result");
            if ("0".equals(result)) {
                // 保存设置码
                String token = obj.getString("token");
                String saveInfo = String.format(SaaSType.JSON_FORMAT_SAVE_INFO, meter_no, "token", result, 0, "0", "0", "0", "0", "0", "0", "0", token, JoyDatetime.dateFormat(new Date(), JoyDatetime.FORMAT_DATETIME_2),
                        "1", "", "", "2", operator, "", "");
                readService.saveReadInfo(saveInfo);
                res.setStatus(1);
                res.setData(token);
                return res;
            }
        } catch (Exception ex) {
            Logger.getLogger(TokenApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
}
