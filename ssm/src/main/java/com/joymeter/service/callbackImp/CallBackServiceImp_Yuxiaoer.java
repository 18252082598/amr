package com.joymeter.service.callbackImp;

import com.alibaba.fastjson.JSONObject;
import com.joymeter.entity.RoomInfo;
import com.joymeter.entity.User;
import com.joymeter.util.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 数据回调
 *
 * @author Wu Wei
 * @date 2017-7-25 13:49:04
 */
public class CallBackServiceImp_Yuxiaoer extends CallBackServiceImp_Common {

    /**
     * 回调绑定表号的房源信息给客户
     *
     * @param user
     */
    @Override
    public void callBackData(User user) {
        if (user == null) {
            return;
        }
        try {
            RoomInfo info = userDao.findRoomInfoByOnlineSyncCode(user.getOnline_synv_code());
            if (info == null) {
                return;
            }
            Map<String, String> params = new HashMap<>();
            params.put("onlineSyncCode", info.getOnlineSyncCode());
            params.put("doorType", info.getDoorType());
            params.put("villageName", info.getVillageName());
            params.put("buildingNumber", info.getBuildingNumber());
            params.put("unitNumber", info.getUnitNumber());
            params.put("houseNumber", info.getHouseNumber());
            params.put("roomName", info.getRoomName());
            params.put("meterNo", user.getMeter_no());
            String jsonParams = JSONObject.toJSONString(params);
            String result = HttpClient.sendPost(values.get("data_callback_url"), jsonParams);
            if (result != null && !result.isEmpty()) {
                JSONObject obj = JSONObject.parseObject(result);
                Short status = Short.valueOf(obj.getString("code"));//0 回调成功；1 回调失败
                //更新回调状态
                if (!Objects.equals(status, info.getCallBackStatus())) {
                    info.setCallBackStatus(status);
                    userDao.updateCallBackStatus(info);
                }
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
}
