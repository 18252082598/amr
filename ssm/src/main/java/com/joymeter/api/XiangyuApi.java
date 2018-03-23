/*
Provide interfaces to Xiangyu
 */
package com.joymeter.api;

import com.joymeter.cache.EventRecordCache;
import com.joymeter.cache.ReadInfoCache;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.EventRecord;
import com.joymeter.entity.User;
import com.joymeter.service.ReadService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author yinhf
 */
@Controller
@RequestMapping("/joy")
public class XiangyuApi {

    @Resource
    private UserDao userDao;
    @Resource
    private ReadService readService;
    @Resource
    private EventRecordCache eventRecordCache;
    @Resource
    ReadInfoCache readInfoCache;

    /**
     * 获取该房源下的所有房间的电表 uuid，电表序列号，房间号等。 返回json说明： home_list：房源对象 house_code：房源编号
     * rooms：房间集合 sn：电表序列号 uuid ：电表uuid room_no ：房间号。0：公摊 。1：房间1
     *
     * @param house_code 房源编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/getMeterInfo.do")
    public Map<String, Object> getMeterByICCard(final String house_code) {
        Map<String, Object> roomsKey = new HashMap<>();
        Map<String, Object> homeList = new HashMap<>();
        List<Map<String, Object>> roomsValues = new ArrayList<>();
        homeList.put("resultCode", "1");
        homeList.put("resultMsg", "fail");
        try {
            List<User> user = userDao.findUserByIdCardNo(house_code);
            homeList.put("resultMsg", "用户不存在");
            int num = user.size();
            if (!user.isEmpty()) {
                for (int i = 0; i < num; i++) {
                    Map<String, Object> roomsValuesMap = new HashMap<>();
                    roomsValuesMap.put("description", "");
                    roomsValuesMap.put("uuid", user.get(i).getUser_address_original_room());
                    roomsValuesMap.put("sn", user.get(i).getMeter_no());
                    roomsValuesMap.put("room_no", user.get(i).getMeter_no().
                            equals(user.get(i).getSubmeter_no()) ? "0" : user.get(i).getUser_address_room());
                    roomsValues.add(roomsValuesMap);
                }
                homeList.put("resultMsg", "successful");
                homeList.put("resultCode", "0");
            }
            roomsKey.put("rooms", roomsValues);
            roomsKey.put("description", "");
            roomsKey.put("house_code", house_code);
            homeList.put("home_list", roomsKey);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return homeList;
    }

    /**
     * 根据电表 UUID 获取电表信息 返回json字符串说明： uuid:电表uuid enable_state：电表状态：1-合闸， 2- 跳闸，
     * -1未知 enable_state_time ：合闸跳闸时间 sn ：电表序列号 onoff_time：最近一次在线时间戳 onoff_line
     * ：电表状态 1- 在线， 2- 离线， 3-未知 house_code：房源编号 room_no ：房间号 0：:公摊，3-1-房间 1，2-房间
     * 2 description：描述
     *
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getMeterByUUID.do")
    public Map<String, Object> getMeterByUUID(final String uuid) {
        Map<String, Object> meterInfo = new HashMap<>();
        meterInfo.put("resultMsg", "fail");
        meterInfo.put("resultCode", "1");
        try {
            User user = userDao.getMeterByUUID(uuid);
            meterInfo.put("resultMsg", "用户不存在");
            String onoff_time = "";
            String room_no = "";
            String value_status = "";
            String meter_status = "";
            if (user != null) {
                String eventOpClo = "1".equals(user.getValve_status()) ? "3001" : "3000";
                EventRecord event = eventRecordCache.getEventRecord(user.getMeter_no(), eventOpClo);
                onoff_time = String.valueOf(System.currentTimeMillis());
                String enable_state_time = event == null ? "" : String.valueOf(event.getReport_time().getTime());
                switch (user.getValve_status()) {
                    case "1":
                        value_status = "1";//合闸
                        break;
                    case "0":
                        value_status = "2";//跳闸
                        break;
                    default:
                        value_status = "-1";//未知
                        break;
                }
                switch (user.getMeter_status()) {
                    case "1":
                        meter_status = "1";//在线
                        break;
                    case "0":
                        meter_status = "2";//离线
                        EventRecord onOffevent = eventRecordCache.getEventRecord(user.getMeter_no(), "1004");
                        onoff_time = onOffevent == null ? "" : String.valueOf(onOffevent.getReport_time().getTime());
                        break;
                    default:
                        meter_status = "3";//未知
                        break;
                }
                room_no = user.getMeter_no().equals(user.getSubmeter_no()) ? "0"
                        : user.getUser_address_room();
                meterInfo.put("resultMsg", "sucessful");
                meterInfo.put("resultCode", "0");
                meterInfo.put("uuid", uuid);
                meterInfo.put("enable_state", value_status);
                meterInfo.put("enable_state_time", enable_state_time);
                meterInfo.put("sn", user.getMeter_no());
                meterInfo.put("onoff_time", onoff_time);
                meterInfo.put("onoff_line", meter_status);
                meterInfo.put("house_code", user.getId_card_no());
                meterInfo.put("room_no", room_no);
                meterInfo.put("description", "");
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return meterInfo;
    }

    /**
     * 根据uuid控制继电器(强制开阀)<br>
     *
     * 请求地址(POST)：http://ip:port/path/joy/openValveByUuid.do<br>
     * 请求参数格式示例: uuid="abc"<br>
     *
     * @param req 请求上下文<br>
     * @param uuid 类似于表号<br>
     * @return 返回JSON数据格式<br>
     *
     * 示例：{"resultMsg ":"","resultCode ":0} <br>
     * resultCode: 0 成功, 其他 失败<br>
     */
    @ResponseBody
    @RequestMapping("/openValveByUUID.do")
    public String openValveByUUID(HttpServletRequest req,
            @RequestParam("uuid") String uuid) {
        return readService.ctrValveByUUID(req, "1", uuid);
    }

    /**
     * 根据uuid控制继电器(强制关阀)<br>
     *
     * @author mengshuai
     * @date 2017-12-29 16:07
     * @version v1.0.0
     *
     * 请求地址(POST)：http://ip:port/path/joy/closeValveByUuid.do<br>
     * 请求参数格式示例: uuid="abc"<br>
     *
     * @param req 请求上下文<br>
     * @param uuid 类似于表号<br>
     * @return 返回JSON数据格式<br>
     *
     * 示例：{"resultMsg ":"","resultCode ":0} <br>
     * resultCode: 0 成功, 其他 失败<br>
     */
    @ResponseBody
    @RequestMapping("/closeValveByUUID.do")
    public String closeValveByUUID(HttpServletRequest req,
            @RequestParam("uuid") String uuid) {
        return readService.ctrValveByUUID(req, "0", uuid);
    }
}
