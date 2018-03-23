package com.joymeter.service.saasImp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joymeter.entity.User;
import com.joymeter.parser.jlaa.JLAA_TYPE;
import com.joymeter.parser.jlaa.app.JLAA_Dl645_Baletu;
import com.joymeter.util.JoyLogger;
import com.joymeter.util.JoyUtil;

/**
 * Ba Le Tu版下发指令类
 *
 * @author Zhong Fuqiang
 * @version 1.0.0
 */
public class SaaSServiceImp_Baletu extends SaaSServiceImp {

    /**
     * 下发指令给SaaS平台
     *
     * @param user
     * @param type 指令的类型
     * @param args 可变参数
     * @return
     */
    @Override
    public int down2SaaS(User user, long type, String... args) {
        if (user == null) {
            return 1;
        }
        super.down2SaaS(user, type, args);
        if (type == JLAA_TYPE.CMD_DL645_0X04BB0112) {
            byte[] bytes = JLAA_Dl645_Baletu.Assembly_Read(JoyUtil.addrToBytes(user.getMeter_no(), 6));
            if (bytes == null || bytes.length <= 0) {
                return 1;
            }
            String payload = JoyUtil.bytesToHex(bytes);
            return this.down2SaaS(user, payload);
        }
        if (type == JLAA_TYPE.CMD_DL645_0X04BB0113) {
            byte[] bytes = JLAA_Dl645_Baletu.Assembly_Token(JoyUtil.addrToBytes(user.getMeter_no(), 6), 0, 1);
            if (bytes == null || bytes.length <= 0) {
                return 1;
            }
            String payload = JoyUtil.bytesToHex(bytes);
            return this.down2SaaS(user, payload);
        }
        if (type == JLAA_TYPE.CMD_DL645_0X04BB0114) {
            byte[] bytes = JLAA_Dl645_Baletu.Assembly_Clear(JoyUtil.addrToBytes(user.getMeter_no(), 6));
            if (bytes == null || bytes.length <= 0) {
                return 1;
            }
            String payload = JoyUtil.bytesToHex(bytes);
            return this.down2SaaS(user, payload);
        }
        if (type == JLAA_TYPE.CMD_DL645_0X04BB0115) {
            byte[] bytes = JLAA_Dl645_Baletu.Assembly_Offline(JoyUtil.addrToBytes(user.getMeter_no(), 6), 0);
            if (bytes == null || bytes.length <= 0) {
                return 1;
            }
            String payload = JoyUtil.bytesToHex(bytes);
            return this.down2SaaS(user, payload);
        }
        return 0;
    }

    /**
     * 下发指令
     *
     * @param json
     */
    @Override
    public void down(String json) {
        JoyLogger.LogInfo("saas down: " + json);
        if (json == null || json.length() <= 0) {
            return;
        }
        JSONObject obj = (JSONObject) JSON.parse(json);
        JSONArray dtus = obj.getJSONArray("DTU");
        if (dtus == null || dtus.size() <= 0) {
            return;
        }
        for (Object item : dtus) {
            JSONObject dtu = (JSONObject) item;
            JSONArray meters = dtu.getJSONArray("meters");
            for (Object p : meters) {
                obj = (JSONObject) p;
                final String meter = obj.getString("meter");
                final String protocol = obj.getString("protocol");
                User user = userDao.findUserByMeterNo(meter);
                if ("jlaa_elec_token".equals(protocol.toLowerCase())) { // 充值
                    this.down2SaaS(user, JLAA_TYPE.CMD_DL645_0X04BB0113);
                }
                if ("jlaa_elec_clear".equals(protocol.toLowerCase())) { // 清零
                    this.down2SaaS(user, JLAA_TYPE.CMD_DL645_0X04BB0114);
                }
                if ("jlaa_elec_offline".equals(protocol.toLowerCase())) { // 清零
                    this.down2SaaS(user, JLAA_TYPE.CMD_DL645_0X04BB0115);
                }
            }
        }
    }
}
