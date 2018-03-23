package com.joymeter.api;

import com.alibaba.fastjson.JSONObject;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.Result;
import com.joymeter.entity.User;
import com.joymeter.service.callbackImp.CallBackServiceImp_KaiLi;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 返回从缓存里获取的数据
 *
 * @author Wu Wei
 * @date 2017-8-16 18:26:33
 */
@RestController
@RequestMapping("/joy")
public class KaiLiApi extends BaseApi {
    
    @Resource
    private UserDao userDao;

    //获取未同步的客户信息
    @RequestMapping("/getExistUser.do")
    public Result getExistUser(@RequestParam("access_token") String access_token) {
        Result result = OAuth(access_token);
        if (result.getStatus() != 1) {
            return result;
        }
        try {
            List<User> users = userDao.findAllUser();
            List<String> data = new ArrayList<>();
            Map<String, String> params = new HashMap<>();
            users.stream().forEach(user -> {
                params.put("factoryId", "5");
                params.put("meterAddr", user.getMeter_no());
                params.put("userName", user.getUser_name());
                params.put("phone", user.getContact_info());
                params.put("paperNo", user.getId_card_no());
                String address = user.getUser_address_community() + user.getUser_address_building() + user.getUser_address_unit() + user.getUser_address_room();
                params.put("address", address);
                params.put("caliber", "20");
                params.put("installDate", new Timestamp(System.currentTimeMillis()).toString());
                String protocol = user.getValve_protocol();
                params.put("ifCtrlValve", (protocol == null || protocol.isEmpty()) ? "0" : "1");
                params.put("userCode", user.getSubmeter_no());
                String jsonParams = JSONObject.toJSONString(params);
                data.add(jsonParams);
            });
            result.setData(data);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    //从缓存里获取新增用户信息
    @RequestMapping("/getAddUser.do")
    public Result getAddUser(@RequestParam("access_token") String access_token) {
        Result result = OAuth(access_token);
        if (result.getStatus() != 1) {
            return result;
        }
        LinkedList<String> users = CallBackServiceImp_KaiLi.cacheMap.get("addUser");
        result.setData(users.clone());
        users.clear();
        return result;
    }

    //从缓存里获取更新用户信息
    @RequestMapping("/getUpdateUser.do")
    public Result getUpdateUser(@RequestParam("access_token") String access_token) {
        Result result = OAuth(access_token);
        if (result.getStatus() != 1) {
            return result;
        }
        LinkedList<String> users = CallBackServiceImp_KaiLi.cacheMap.get("updateUser");
        result.setData(users.clone());
        users.clear();
        return result;
    }

    //从缓存里获取换表信息
    @RequestMapping("/getChangeMeter.do")
    public Result getChangeMeter(@RequestParam("access_token") String access_token) {
        Result result = OAuth(access_token);
        if (result.getStatus() != 1) {
            return result;
        }
        LinkedList<String> meterInfo = CallBackServiceImp_KaiLi.cacheMap.get("changeMeter");
        result.setData(meterInfo.clone());
        meterInfo.clear();
        return result;
    }

    //从缓存里获取抄表信息
    @RequestMapping("/getReadData.do")
    public Result getReadData(@RequestParam("access_token") String access_token) {
        Result result = OAuth(access_token);
        if (result.getStatus() != 1) {
            return result;
        }
        LinkedList<String> readData = CallBackServiceImp_KaiLi.cacheMap.get("readData");
        result.setData(readData.clone());
        readData.clear();
        return result;
    }

    //从缓存里获取阀门操作信息
    @RequestMapping("/getValveData.do")
    public Result getValveData(@RequestParam("access_token") String access_token) {
        Result result = OAuth(access_token);
        if (result.getStatus() != 1) {
            return result;
        }
        LinkedList<String> valveData = CallBackServiceImp_KaiLi.cacheMap.get("valveData");
        result.setData(valveData.clone());
        valveData.clear();
        return result;
    }
}
