package com.joymeter.service.saasImp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.Concentrator;
import com.joymeter.entity.User;
import com.joymeter.parser.jlaa.JLAA_TYPE;
import com.joymeter.parser.jlaa.app.JLAA_Dl645;
import com.joymeter.service.SaaSService;
import com.joymeter.util.HttpClient;
import com.joymeter.util.JoyDatetime;
import com.joymeter.util.JoyLogger;
import com.joymeter.util.JoyUtil;
import com.joymeter.util.RSACoder;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author Zhong Fuqiang
 * @version 1.0.0
 */
@Service
public class SaaSServiceImp implements SaaSService {
    
    protected UserDao userDao;
    protected Map<String, String> values;
    public final static String URI = "http://%s/joy/saas/v1/%s";

    /**
     *
     * @param userDao
     * @param values
     */
    @Override
    public void setParams(UserDao userDao, Map<String, String> values) {
        this.userDao = userDao;
        this.values = values;
    }

    /**
     * 向SaaS平台注册设备
     *
     * @param user
     * @return
     */
    @Override
    public int add2SaaS(User user) {
        try {
            String concentrate_name = user.getConcentrator_name();
            String device_id = user.getMeter_no();
            String type = user.getMeter_type();
            Concentrator con = userDao.findConcentrator(concentrate_name);
            if (con != null) {
                String client_id = con.getConcentrator_no();
                String access_token = SaaSServiceImp.accessToken(client_id, con.getPublic_key());
                String ip = con.getConcentrator_ip() + ":" + con.getConcentrator_port();
                String param = String.format(SaaSType.JSON_FORMAT_SAAS_ADD, access_token, client_id, device_id, type);
                String url = String.format(SaaSServiceImp.URI, ip, "device/add");
                String resultData = HttpClient.sendPost(url, param);
                JSONObject jo = JSON.parseObject(resultData);
                if (jo != null) {
                    int errcode = jo.getInteger("errcode");
                    JoyLogger.LogInfo("add2SaaS errcode " + errcode);
                    if (errcode == 0) {
                        userDao.updateRegisterStatus(user.getMeter_no());
                        return 0;
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return 1;
    }

    /**
     * 如果表号发生变更,则更新SaaS平台上的数据
     *
     * @param original_meter_no
     * @param user
     * @return
     */
    @Override
    public int update2SaaS(User user, String original_meter_no) {
        try {
            String concentrator_name = user.getConcentrator_name();
            Concentrator con = userDao.findConcentrator(concentrator_name);
            if (con != null) {
                String client_id = con.getConcentrator_no();
                String access_token = SaaSServiceImp.accessToken(client_id, con.getPublic_key());
                String ip = con.getConcentrator_ip() + ":" + con.getConcentrator_port();
                String param = String.format(SaaSType.JSON_FORMAT_SAAS_UPDATE, access_token, client_id, original_meter_no, user.getMeter_no(), user.getMeter_type());
                String url = String.format(URI, ip, "device/update");
                String resultData = HttpClient.sendPost(url, param);
                JSONObject jo = JSON.parseObject(resultData);
                if (jo != null) {
                    int errcode = jo.getInteger("errcode");
                    JoyLogger.LogInfo("update2SaaS errcode " + errcode);
                    if (errcode == 0) {
                        userDao.updateRegisterStatus(user.getMeter_no());
                        return 0;
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return 1;
    }

    /**
     * 删除SaaS平台上的数据
     *
     * @param user
     * @return
     */
    @Override
    public int delete2SaaS(User user) {
        try {
            String concentrator_name = user.getConcentrator_name();
            Concentrator con = userDao.findConcentrator(concentrator_name);
            if (con != null) {
                String client_id = con.getConcentrator_no();
                String access_token = SaaSServiceImp.accessToken(client_id, con.getPublic_key());
                String ip = con.getConcentrator_ip() + ":" + con.getConcentrator_port();
                String param = String.format(SaaSType.JSON_FORMAT_SAAS_DEL, access_token, client_id, user.getMeter_no());
                String url = String.format(URI, ip, "device/del");
                String resultData = HttpClient.sendPost(url, param);
                JSONObject jo = JSON.parseObject(resultData);
                int errcode = jo.getInteger("errcode");
                JoyLogger.LogInfo("update2SaaS errcode " + errcode);
                return errcode;
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return 1;
    }

    /**
     * 下发指令给SaaS平台
     *
     * @param user
     * @param payload
     * @return
     */
    @Override
    public int down2SaaS(User user, final String payload) {
        try {
            String concentrator_name = user.getConcentrator_name();
            Concentrator con = userDao.findConcentrator(concentrator_name);
            if (con != null) {
                String client_id = con.getConcentrator_no();
                String access_token = SaaSServiceImp.accessToken(client_id, con.getPublic_key());
                String ip = con.getConcentrator_ip() + ":" + con.getConcentrator_port();
                String param = String.format(SaaSType.JSON_FORMAT_SAAS_DOWN, access_token, client_id, user.getMeter_no(), payload);
                String url = String.format(URI, ip, "device/down");
                String resultData = HttpClient.sendPost(url, param);
                JSONObject jo = JSON.parseObject(resultData);
                int errcode = jo.getInteger("errcode");
                JoyLogger.LogInfo("update2SaaS errcode " + errcode);
                return errcode;
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return 1;
    }

    /**
     * 生成access token
     *
     * @param clinet_id
     * @param public_key
     * @return
     */
    public static String accessToken(final String clinet_id, final String public_key) {
        String plaintext = String.format("{\"id\":\"" + clinet_id + "\",\"datetime\":\"%s\"}",
                JoyDatetime.dateFormat(new Date(), JoyDatetime.FORMAT_DATETIME_6));
        try {
            byte[] data = plaintext.getBytes();
            byte[] encodedData = RSACoder.encryptByPublicKey(data, public_key);
            return RSACoder.encryptBASE64(encodedData);
        } catch (Exception ex) {
            Logger.getLogger(SaaSServiceImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
                if ("jlaa_elec_open".equals(protocol.toLowerCase())) { // 断开继电器
                    this.down2SaaS(user, JLAA_TYPE.CMD_DL645_0X1C1B2B3B);
                }
                if ("jlaa_elec_close".equals(protocol.toLowerCase())) { // 合上继电器
                    this.down2SaaS(user, JLAA_TYPE.CMD_DL645_0X1C1A2B3B);
                }
            }
        }
    }

    /**
     * 下发指令给SaaS平台
     *
     * @param user
     * @param type 类型
     * @param args 可变参数
     * @return
     */
    @Override
    public int down2SaaS(User user, long type, final String... args) {
        if (user == null) {
            return 1;
        }
        if (type == JLAA_TYPE.CMD_DL645_0X1C1A2B3B) {
            byte[] bytes = JLAA_Dl645.Assembly_Open(JoyUtil.addrToBytes(user.getMeter_no(), 6));
            if (bytes == null || bytes.length <= 0) {
                return 1;
            }            String payload = JoyUtil.bytesToHex(bytes);
            return this.down2SaaS(user, payload);
        }
        if (type == JLAA_TYPE.CMD_DL645_0X1C1B2B3B) {
            byte[] bytes = JLAA_Dl645.Assembly_Close(JoyUtil.addrToBytes(user.getMeter_no(), 6));
            if (bytes == null || bytes.length <= 0) {
                return 1;
            }
            String payload = JoyUtil.bytesToHex(bytes);
            return this.down2SaaS(user, payload);
        }
        return 0;
    }
}
