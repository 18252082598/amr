package com.joymeter.service.saasImp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joymeter.entity.User;
import com.joymeter.parser.jlaa.JLAA_TYPE;
import com.joymeter.parser.jlaa.app.JLAA_CJ188_201;
import com.joymeter.util.JoyUtil;

/**
 * 金海版下发指令类
 *
 * @author Zhong Fuqiang
 * @version 1.0.0
 */
public class SaaSServiceImp_Jinhai extends SaaSServiceImp {

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
        if (type == JLAA_TYPE.CMD_CJ188_0X81901F) {
            byte[] bytes = JLAA_CJ188_201.Assembly_81_901F(JoyUtil.hexToBytes(user.getMeter_no()), (byte) 0x15);
            if (bytes == null || bytes.length <= 0) {
                return 1;
            }
            String payload = JoyUtil.bytesToHex(bytes);
            return super.down2SaaS(user, payload);
        }
        if (type == JLAA_TYPE.CMD_CJ188_0X81A001) {
            if (args.length >= 2) {
                int st = Integer.parseInt(args[0]);
                int ed = Integer.parseInt(args[1]);
                byte[] bytes = JLAA_CJ188_201.Assembly_81_A001((byte) 0x15, (byte) st, (byte) ed);
                if (bytes == null || bytes.length <= 0) {
                    return 1;
                }
                String payload = JoyUtil.bytesToHex(bytes);
                return super.down2SaaS(user, payload);
            }
        }
        return 0;
    }

    /**
     *
     * @param json
     */
    @Override
    public void down(final String json) {
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
                if (protocol.toLowerCase().equals("jlaa")) {
                    this.down2SaaS(user, JLAA_TYPE.CMD_CJ188_0X81901F);
                }
                if (protocol.toLowerCase().equals("jlaa_open")) {
                    this.down2SaaS(user, JLAA_TYPE.CMD_CJ188_0X81A001, "8", "9");
                }
                if (protocol.toLowerCase().equals("jlaa_close")) {
                    String payload = JoyUtil.bytesToHex(JLAA_CJ188_201.Assembly_81_901F(JoyUtil.hexToBytes(meter), (byte) 0x15));
                    this.down2SaaS(user, JLAA_TYPE.CMD_CJ188_0X81901F, payload);
                }
            }
        }
    }
}
