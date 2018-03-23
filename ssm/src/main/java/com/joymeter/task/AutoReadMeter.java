package com.joymeter.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.joymeter.cache.ReadInfoCache;
import com.joymeter.cache.CallBackCache;
import com.joymeter.util.HttpRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.ReadParameter;
import com.joymeter.entity.User;
import com.joymeter.entity.Community;
import com.joymeter.entity.Concentrator;
import com.joymeter.service.WeChatService;
import com.joymeter.service.saasImp.SaaSServiceImp;
import com.joymeter.service.saasImp.SaaSType;
import com.joymeter.util.HttpClient;
import com.joymeter.util.ParameterUtil;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class AutoReadMeter {

    @Resource
    private UserDao userDao;
    @Resource
    private ReadInfoCache readInfoCache;
    @Resource
    private WeChatService wechatService;

    /**
     * 根据设置的抄表时间自动抄表 read meter automatically by the parameter set previously
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void autoReadMeter() {
        try {
            Calendar cal = Calendar.getInstance();
            this.execSchedule(cal);
            CallBackCache.doExpired(userDao);
            // 正点上报
            JoyServletContextListener.instanceCallback(userDao)
                    .schedulerCallBackReadInfo(cal, readInfoCache);
            
            this.registerUser2SaaS(cal);
            this.autoCallBackUsers(cal);

            // 15分钟上报没有发送成功的数据
            JoyServletContextListener.instanceCallback(userDao)
                    .schedulerCallBackReadInfo(cal);
            wechatService.scheduler(cal);
        } catch (NumberFormatException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * 执行计划
     *
     * @param cal
     * @throws NumberFormatException
     */
    private void execSchedule(Calendar cal) throws NumberFormatException {
        String day, hour, minute, isAutoRead, timeStr;
        SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        // 格式化当前日期用来作为每月抄一次表的判断
        String now = sdf.format(date);
        // 格式化当前日期用来作为每天抄一次表的判断
        String now1 = sdf1.format(date);
        List<ReadParameter> readParameters = userDao.findReadParameter();
        if (readParameters == null) {
            return;
        }
        ReadParameter readParameter;
        for (int i = 0; i < readParameters.size(); i++) {
            readParameter = readParameters.get(i);
            if (readParameter == null) {
                continue;
            }
            day = readParameter.getDay();
            hour = readParameter.getHour();
            minute = readParameter.getMinute();
            isAutoRead = readParameter.getIsAutoRead();
            timeStr = hour + ":" + minute;
            String parameter_id = readParameter.getParameter_id();
            if ("1".equals(parameter_id)) { // 定时发送抄表命令
                boolean isEveryDay = "00".equals(day) && now1.equals(timeStr);
                if ("1".equals(isAutoRead)) {
                    if (isEveryDay || now.equals(day + " " + timeStr)) {
                        this.sendMeterParameters(readParameter.getBalance_warn(), readParameter.getValve_close());
                    }
                }
            }
            if ("2".equals(parameter_id)) { // 周期性发送抄表命令
                if ("1".equals(isAutoRead)) {
                    long minute0 = cal.get(Calendar.DAY_OF_YEAR) * 24 * 60 + cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
                    long minute1 = Integer.valueOf(day) * 24 * 60 + Integer.valueOf(hour) * 60 + Integer.valueOf(minute);
                    if (minute1 == 0) {
                        continue;
                    }
                    if (minute0 % minute1 == 0) {
                        this.sendMeterParameters(readParameter.getBalance_warn(), readParameter.getValve_close());
                    }
                }
            }
        }
    }

    /**
     * 组合自动抄表的数据, 发送给抄表服务器
     */
    private void sendMeterParameters(Double balanceWarnning, Double valveClose) {
        try {
            userDao.delReadInfoFail(); // 每次自动抄表前清除失败抄表中的内容
            List<Community> communities = userDao.loadCommunity();
            communities.stream().map((community) -> community.getCommunity_name())
                    .map((community_name) -> userDao.findUsersByCommunity(community_name))
                    .filter((users) -> !(users.isEmpty()))
                    .forEachOrdered((users) -> {
                        Map<String, Object> map = new HashMap<>();
                        ArrayList<String[]> meterList = new ArrayList<>();
                        Set<String> ips = new HashSet<>();
                        for (User user : users) {
                            String concentrator_name = user.getConcentrator_name();
                            String meter_no = user.getMeter_no();
                            String meter_type = user.getMeter_type();
                            String protocol_type = user.getProtocol_type();
                            //当协议类型为空值时，不对该用户进行抄表
                            if (protocol_type == null) {
                                continue;
                            }
                            //去除协议两端及中间的空格
                            protocol_type = protocol_type.replaceAll("\\s", "");
                            Concentrator con = userDao.findConcentrator(concentrator_name);
                            if (con == null) {
                                continue;
                            }
                            String concentrator_model = con.getConcentrator_model();
                            if (concentrator_model == null) {
                                continue;
                            }
                            //当集中器处于安装.故障时候不对该用户进行抄表
                            if (concentrator_model.equals("0") || concentrator_model.equals("3")) {
                                continue;
                            }
                            String id = con.getConcentrator_no();
                            String ip = con.getConcentrator_ip() + ":" + con.getConcentrator_port();
                            ips.add(ip);
                            //当进入ParameterUtil.GetParameter()方法时，用它来判断是哪个方法向其发出的调用，当ParameterUtil.GetParameter()
                            //判断method=readByCommunity时，直接将其concentrator_model值设为2,其余的按其传来的值计算
                            String method = "SendMeterParameters";
                            String[] arr = {id, meter_no, meter_type, protocol_type, ip, concentrator_model, method};
                            meterList.add(arr);
                        }
                        // 根据集中器ip，把抄表参数(String ip->List<String[]>)以key-value的形式放进map
                        for (String ip : ips) {
                            List<String[]> li = new ArrayList<>();
                            for (int i = 0; i < meterList.size(); i++) {
                                String[] ary = meterList.get(i);
                                String concentrator_ip = ary[4];
                                if (ip.equals(concentrator_ip)) {
                                    li.add(ary);
                                }
                            }
                            map.put(ip, li);
                        }
                        String sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
                        // 根据集中器的IP，分别向相应的服务器发送抄表请求
                        for (Map.Entry entry : map.entrySet()) {
                            String ip = entry.getKey().toString();
                            meterList = (ArrayList<String[]>) entry.getValue();
                            String parameters = ParameterUtil.GetParameter(meterList, JoyServletContextListener.GetUrl(), 0, "system", sendTime, balanceWarnning, valveClose);
                            HttpRequest.sendPost(HttpRequest.GetUrl(ip), parameters);
                        }
                    });
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * 发送网关信息给抄表服务器
     */
    private void sendGateParameters(Calendar cal) {
        try {
            if (cal.get(Calendar.MINUTE) != 0 || cal.get(Calendar.MINUTE) == 30) {
                return;
            }
            List<Concentrator> concentrators = userDao.findAllConcentrators();
            if (concentrators == null) {
                return;
            }
            String url = JoyServletContextListener.GetUrl();
            Map<String, Object> map = new HashMap<>();
            Set<String> ips = new HashSet<>();
            concentrators.stream().map((concentrator) -> {
                String gatewayId = concentrator.getGateway_id();
                String ip = concentrator.getConcentrator_ip();
                String postUrl = String.format("http://%s:%s/arm/api/registerGateway", ip, concentrator.getConcentrator_port());
                String account = concentrator.getOperator_account();
                String param = "{DTU: [{"
                        + "id:'" + gatewayId + "',"
                        + "url:'" + url + "',"
                        + "options:{isAutoClear:'0',accountName:'" + account + "'}" + "}]}";
                if (map.get(postUrl) == null) {
                    map.put(postUrl, param);
                } else {
                    String temp = (String) map.get(postUrl);
                    String tempParam = "{"
                            + "id:'" + gatewayId + "',"
                            + "url:'" + url + "',"
                            + "options:{isAutoClear:'0',accountName:'" + account + "'}" + "}]}";
                    temp = temp.replace("]}", "," + tempParam);
                    map.put(postUrl, temp);
                }
                return postUrl;
            }).forEachOrdered((postUrl) -> {
                ips.add(postUrl);
            });
            ips.forEach((postUrl) -> {
                HttpRequest.sendPost(postUrl, (String) map.get(postUrl));
            });
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * 每隔半个小时间处理在SaaS平台上没有注册成功的表计
     */
    private void registerUser2SaaS(Calendar cal) {
        try {
            if (cal.get(Calendar.MINUTE) != 0 && cal.get(Calendar.MINUTE) != 30) {
                return;
            }
            List<User> users = userDao.findUnregisterUsers();
            if (users == null || users.isEmpty()) {
                return;
            }
            users.forEach((user) -> {
                String concentrator_name = user.getConcentrator_name();
                Concentrator con = userDao.findConcentrator(concentrator_name);
                if (con == null) {
                    return;
                }
                String access_token = SaaSServiceImp.accessToken(con.getConcentrator_no(), con.getPublic_key());
                String concentrator_ip = con.getConcentrator_ip();
                String concentrator_port = con.getConcentrator_port();
                String ip = concentrator_ip + ":" + concentrator_port;
                String client_id = con.getConcentrator_no();
                String device_id = user.getMeter_no();
                String type = user.getMeter_type();
                String param = String.format(SaaSType.JSON_FORMAT_SAAS_ADD, access_token, client_id, device_id, type);
                String url = String.format(SaaSServiceImp.URI, ip, "device/add");
                String resultData = HttpClient.sendPost(url, param);
                JSONObject jo = JSON.parseObject(resultData);
                if (jo != null) {
                    String errcode = jo.getString("errcode");
                    if (errcode.equals("0")) {
                        try {
                            userDao.updateRegisterStatus(device_id);
                        } catch (Exception e) {
                            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                        }
                    }
                }
            });
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * 回调失败的用户，定时继续回调
     *
     * @param cal
     */
    private void autoCallBackUsers(Calendar cal) {
        try {
            if (cal.get(Calendar.MINUTE) != 0 && cal.get(Calendar.MINUTE) != 30) {
                return;
            }
            List<User> users = userDao.findUnCallBackUsers();
            users.stream().forEach((user) -> {
                JoyServletContextListener.instanceCallback(userDao).callBackData(user);
            });
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
}
