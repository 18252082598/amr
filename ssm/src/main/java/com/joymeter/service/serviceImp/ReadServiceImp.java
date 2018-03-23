package com.joymeter.service.serviceImp;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joymeter.cache.EventRecordCache;
import com.joymeter.cache.FeeTypeCache;
import com.joymeter.cache.ReadInfoCache;
import com.joymeter.cache.ValveStateCache;
import com.joymeter.cache.CallBackCache;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.Community;
import com.joymeter.entity.Concentrator;
import com.joymeter.entity.EventRecord;
import com.joymeter.entity.FeeType;
import com.joymeter.entity.OperateInfo;
import com.joymeter.entity.ReadInfo;
import com.joymeter.entity.ReadParameter;
import com.joymeter.entity.Result;
import com.joymeter.entity.User;
import com.joymeter.service.ParentConfService;
import com.joymeter.service.ReadService;
import com.joymeter.service.RechargeService;
import com.joymeter.service.saasImp.SaaSType;
import com.joymeter.util.HttpRequest;
import com.joymeter.task.JoyServletContextListener;
import com.joymeter.util.JoyLogger;
import com.joymeter.util.JoyDatetime;
import com.joymeter.util.ParameterUtil;
import com.joymeter.util.RegexMatches;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Zhong Fuqiang
 * @version 1.0.0
 */
@Service
public class ReadServiceImp implements ReadService {

    @Resource
    private UserDao userDao;
    @Resource
    private RechargeService rechargeService;
    @Resource
    private ReadInfoCache readInfoCache;
    @Resource
    private ParentConfService parentConfService;
    @Resource
    private FeeTypeCache feeTypeCache;
    @Resource
    private EventRecordCache eventRecordCache;
    @Resource
    private ValveStateCache valveCache;

    /**
     * 加载需要抄表的用户
     *
     * @param user_name
     * @param user_address_community
     * @param user_address_building
     * @param user_address_unit
     * @param user_address_room
     * @param operator_name
     * @param contact_info
     * @param meter_no
     * @param status
     * @param startNo
     * @param pageSize
     * @param request
     * @return
     */
    @Override
    public Result findUsersToRead(String user_name, String user_address_community, String user_address_building,
            String user_address_unit, String user_address_room, String operator_name, String contact_info,
            String meter_no, String status, int startNo, int pageSize, HttpServletRequest request) {
        Result result = new Result();
        String supplier_name = (String) request.getSession().getAttribute("supplier_name");
        Map<String, Object> params = new HashMap<>();
        params.put("supplier_name", supplier_name);
        params.put("user_name", user_name);
        params.put("community", user_address_community);
        params.put("building", user_address_building);
        params.put("unit", user_address_unit);
        params.put("room", user_address_room);
        params.put("operator_name", operator_name);
        params.put("contact_info", contact_info);
        params.put("meter_no", meter_no);
        params.put("status", status);
        params.put("startNo", startNo);
        params.put("pageSize", pageSize);
        try {
            List<User> users = userDao.findUsersToRead(params);
            int totalNo = userDao.findTotalUsersNoToRead(params);
            params.clear();
            params.put("users", users);
            params.put("totalNo", totalNo);
            result.setStatus(1);
            result.setData(params);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 按用户抄表
     *
     * @param req
     * @param list
     * @param isAutoClear
     * @param accountName
     * @return
     */
    @Override
    public Result sendDevParams(HttpServletRequest req, ArrayList<String[]> list,
            int isAutoClear,
            String accountName) {
        Result result = new Result();
        Map<String, Object> map = new HashMap<>();
        ArrayList<String[]> meterList = new ArrayList<>();
        // 统计集中器模式为非启用模式的集合
        ArrayList<String> meter_noFalseList = new ArrayList<>();
        // 统计符合抄表条件表的表号集合
        ArrayList<String> meter_noSucList = new ArrayList<>();
        Set<String> ipSet = new HashSet<>();
        try {
            // 根据集中器名称查询集中器id和ip
            for (int i = 0; i < list.size() - 1; i++) {
                String[] ary = list.get(i);
                String concentrator_name = ary[0];
                String meter_no = ary[1];
                Concentrator con = userDao.findConcentrator(concentrator_name);
                if (con == null) {
                    meter_noFalseList.add(meter_no);
                    continue;
                }
                String concentrator_model = con.getConcentrator_model();
                if (concentrator_model == null) {
                    meter_noFalseList.add(meter_no);
                    continue;
                }
                // 当集中器模式为安装,故障模式时不发出抄表请求，并统计集中器对应的表号
                if (concentrator_model.equals("0") || concentrator_model.equals("3")) {
                    meter_noFalseList.add(meter_no);
                    continue;
                }
                // 调试模式可以发出抄表请求，但是并不保存数据到数据库,只是作为调试用, 因此也归位失败一类，前端统一显示为非启用状态
                if (concentrator_model.equals("1")) {
                    meter_noFalseList.add(meter_no);
                }
                if (concentrator_model.equals("2")) {
                    meter_noSucList.add(meter_no);
                }
                String ip = con.getConcentrator_ip() + ":" + con.getConcentrator_port();
                ipSet.add(ip);
                String id = con.getConcentrator_no();
                String meter_type = ary[2];
                String protocol_type = ary[3];
                String method = "sendUserParameters";
                // 如果是开关阀操作，表号参数换成阀门编号
                if (isAutoClear == 10) {
                    String valve_no = userDao.findValveNoByMeterNo(meter_no);
                    String[] arr = {id, valve_no, meter_type, protocol_type, ip, concentrator_model, method};
                    meterList.add(arr);
                } else {
                    String[] arr = {id, meter_no, meter_type, protocol_type, ip, concentrator_model, method};
                    meterList.add(arr);
                }
            }
            // 根据集中器IP,把抄表参数(String ip->List<String[]>)以key-value的形式放进map
            for (String ip : ipSet) {
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
            Double balanceWarnning = null, valveClose = null;
            ReadParameter rp = this.getReadParameters();
            if (rp != null) {
                balanceWarnning = rp.getBalance_warn();
                valveClose = rp.getValve_close();
            }
            String sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
            // 根据集中器的IP,分别向相应的服务器发送抄表请求
            for (Map.Entry entry : map.entrySet()) {
                String ip = entry.getKey().toString();
                meterList = (ArrayList<String[]>) entry.getValue();
                String parameters = ParameterUtil.GetParameter(meterList,
                        JoyServletContextListener.GetUrl(),
                        isAutoClear,
                        accountName,
                        sendTime,
                        balanceWarnning,
                        valveClose);
                HttpRequest.sendPost(HttpRequest.GetUrl(ip), parameters);
                JoyServletContextListener.instanceSaaS(userDao).down(ParameterUtil.GetParameter(meterList));
            }
            map.clear();
            map.put("meter_noFalseList", meter_noFalseList);
            map.put("meter_noSucList", meter_noSucList);
            map.put("sendTime", sendTime);
            result.setStatus(1);
            result.setData(map);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 开阀或关阀
     *
     * @param req
     * @param list
     * @param accountName
     * @return
     */
    @Override
    public Result ctrValve(HttpServletRequest req, ArrayList<String[]> list, String accountName) {
        return sendDevParams(req, list, 10, accountName);
    }

    /**
     * 查询阀门的开闭状态
     *
     * @param sendTime
     * @return
     */
    @Override
    public Result findValveStatus(String sendTime) {
        Result result = new Result();
        try {
            Timestamp time = Timestamp.valueOf(sendTime);
            List<ReadInfo> readInfo = valveCache.findValveStatus(time);
            result.setStatus(1);
            result.setData(readInfo);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 查询小区信息
     *
     * @param community_address
     * @return
     */
    @Override
    public Result findCommunityInfo(String community_address) {
        Result result = new Result();
        List<Object> list = new ArrayList<>();
        try {
            List<Community> communities = userDao.loadCommunity();
            for (int i = 0; i < communities.size(); i++) {
                String district = communities.get(i).getDistrict();
                String community_name = communities.get(i).getCommunity_name();
                int totalUsers = userDao.findTotalUsersByCommunityName(community_name);
                if (totalUsers == 0) {
                    continue;
                }
                if (community_address.equals("") || community_address.equals(district)
                        || community_address.equals(community_name)) {
                    List<Object> li = new ArrayList<>();
                    li.add(community_name);
                    li.add(district);
                    li.add(totalUsers);
                    list.add(li);
                }
            }
            result.setStatus(1);
            result.setData(list);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 按小区抄表
     *
     * @param req
     * @param community_name
     * @param isAutoClear
     * @param accountName
     * @return
     */
    @Override
    public Result readByCommunity(HttpServletRequest req, String community_name, int isAutoClear, String accountName) {
        Result result = new Result();
        Map<String, Object> map = new HashMap<>();
        ArrayList<String[]> meterList = new ArrayList<>();
        Set<String> ips = new HashSet<>();
        try {
            List<User> users = userDao.findUsersByCommunity(community_name);
            for (User user : users) {
                String concentrator_name = user.getConcentrator_name();
                String meter_no = user.getMeter_no();
                String meter_type = user.getMeter_type();
                String protocol_type = user.getProtocol_type();
                Concentrator con = userDao.findConcentrator(concentrator_name);
                if (con == null) {
                    continue;
                }
                String id = con.getConcentrator_no();
                String ip = con.getConcentrator_ip() + ":" + con.getConcentrator_port();
                ips.add(ip);
                String method = "readByCommunity";
                // 没有特殊的意义，只是为了使的arr长度和其他几个抄表请求的arr长度一致
                String concentator_model = "2";
                String[] arr = {id, meter_no, meter_type, protocol_type, ip, concentator_model, method};
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
            Double balanceWarnning = null, valveClose = null;
            ReadParameter rp = this.getReadParameters();
            if (rp != null) {
                balanceWarnning = rp.getBalance_warn();
                valveClose = rp.getValve_close();
            }
            String sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
            // 根据集中器的IP，分别向相应的服务器发送抄表请求
            for (Map.Entry entry : map.entrySet()) {
                String ip = entry.getKey().toString();
                meterList = (ArrayList<String[]>) entry.getValue();
                String parameters = ParameterUtil.GetParameter(meterList, JoyServletContextListener.GetUrl(),
                        isAutoClear, accountName, sendTime, balanceWarnning, valveClose);
                HttpRequest.sendPost(HttpRequest.GetUrl(ip), parameters);
            }
            result.setStatus(1);
            result.setData(sendTime);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 处理抄表返回数据并保存
     *
     * @param parameters
     * @return
     */
    @Override
    public Result saveReadInfo(String parameters) {
        JoyLogger.LogInfo("saveReadInfo: " + parameters);
        if (parameters == null || parameters.isEmpty()) {
            return null;
        }
        // 本次抄表信息
        ReadInfo readInfo = new ReadInfo();
        // 上次抄表信息
        ReadInfo lastReadInfo = null;
        JSONObject jo = JSON.parseObject(parameters);
        JSONObject ja = jo.getJSONObject("options");
        String meter_no = jo.getString("meter");
        int result = jo.getInteger("result");// 抄表返回状态,0表示成功,1表示失败
        String category = jo.getString("category");
        String data9 = jo.getString("date9");
        data9 = (data9 == null || data9.isEmpty()) ? "-1" : data9;
        String isAutoClear = ja.getString("isAutoClear");
        String operator_account = ja.getString("accountName");
        String balanceWarnning = ja.getString("balanceWarnning");
        String valveClose = ja.getString("valveClose");
        String concentrator_model = ja.getString("concentrator_model");
        String protocol = ja.getString("protocol");
        String sendTime = ja.getString("sendTime");
        if (concentrator_model == null || concentrator_model.equals("1")) {
            return null;
        }
        try {
            operator_account = operator_account.equals("computer") ? "system" : operator_account;
            String read_type = "1";//手动
            if (operator_account.equals("system")) {
                read_type = "0";//自动
            }
            Double fee_need = 0d;
            User user = isAutoClear.equals("10") ? userDao.findUserByValveNo(meter_no) : userDao.findUserByMeterNo(meter_no);
            if (user == null) {
                return null;
            }
            if ("10".equals(isAutoClear)) { // 开阀或者关阀返回结果的处理
                Map<String, Object> params = new HashMap<>();
                String valve_status = "1";
                if (protocol.endsWith("_Close")) {
                    valve_status = "0";
                }
                if (protocol.equals("ctr")) {
                    valve_status = "0".equals(user.getValve_status()) ? "1" : "0";
                }
                if (result == 0) { // 如果操作成功，更新阀门的开关状态
                    params.put("valve_no", meter_no);
                    params.put("status_no", valve_status);
                    userDao.updateValveStatus(params);
                    readInfo.setUser_address_original_room(user.getUser_address_original_room());
                }
                meter_no = user.getMeter_no();
                readInfo.setException(result == 0 ? "1" : "0");
                readInfo.setFee_status("");
                readInfo.setThis_read(0d);
                readInfo.setLast_read(0d);
                readInfo.setThis_cost(0d);
                readInfo.setFee_need(0d);
                readInfo.setBalance(0d);
                readInfo.setLast_cost(0d);
                readInfo.setData2(0d);
                readInfo.setData3(0d);
                readInfo.setData4(0d);
                readInfo.setData5(0d);
                readInfo.setData6(0d);
                readInfo.setData7(0d);
                readInfo.setData8(0d);
                readInfo.setIsAutoClear(isAutoClear);
                readInfo.setMeter_no(meter_no);
                readInfo.setBalance(user.getLast_balance());
                readInfo.setData9(valve_status);
                valveCache.add(meter_no, readInfo);
                JoyServletContextListener.instanceCallback(userDao).callBackValveState(readInfo);
            } else {
                // 抄表返回结果的处理
                Double total_amount = jo.getDouble("data1");
                Double data2 = jo.getDouble("data2");
                Double data3 = jo.getDouble("data3");
                Double data4 = jo.getDouble("data4");
                Double data5 = jo.getDouble("data5");
                Double data6 = jo.getDouble("data6");
                Double data7 = jo.getDouble("data7");
                Double data8 = jo.getDouble("data8");
                Double this_amount = null;
                String read_status = "";
                if (result == 0) { // 成功
                    total_amount += user.getIniting_data() == null ? 0d : user.getIniting_data();
                    read_status = "1";
                } else if (result == 1) { // 失败
                    read_status = "0";
                    total_amount = 0d;
                }
                String feeTypeName = user.getFee_type();
                feeTypeName = feeTypeName.replaceAll("\\s", "");
                FeeType feeType = feeTypeCache.findFeeTypeByName(feeTypeName);
                ReadInfo lInfo = readInfoCache.findLastReadInfoByMeterNo(meter_no);
                Double last_cost = lInfo == null ? null : lInfo.getThis_cost();
                if (last_cost != null) {
                    last_cost = Math.round(last_cost * 100) / 100d;
                }
                Double last_read;
                if (lInfo == null) {
                    last_read = user.getIniting_data() == null ? 0d : user.getIniting_data();
                } else {
                    last_read = lInfo.getThis_read() == null ? 0d : lInfo.getThis_read();
                }
                if (total_amount != null) {
                    this_amount = total_amount - last_read;
                    if (this_amount < 0) {
                        lastReadInfo = lInfo;
                    }
                }
                // 如果数据异常（本期用量小于0），保存至异常记录表
                String isLevelPrice = feeType.getIsLevelPrice();
                double totalUnitPrice = feeType.getTotalUnitCost();
                double extraCost = feeType.getExtraCost();
                if (this_amount == null || this_amount == 0) {
                    extraCost = 0;
                }
                double this_balance;
                double last_balance = user.getLast_balance();
                if (read_status.equals("1")) { //抄表成功
                    if (isLevelPrice.equals("0")) { //不启用阶梯水价
                        fee_need = totalUnitPrice * this_amount + extraCost;
                    } else {//启用阶梯水价
                        double levelOneStartVolume = feeType.getLevelOneStartVolume();
                        double levelOneTotalPrice = feeType.getLevelOneTotalPrice();
                        double levelTwoStartVolume = feeType.getLevelTwoStartVolume();
                        double levelTwoTotalPrice = feeType.getLevelTwoTotalPrice();
                        double levelThreeStartVolume = feeType.getLevelThreeStartVolume();
                        double levelThreeTotalPrice = feeType.getLevelThreeTotalPrice();
                        double levelFourStartVolume = feeType.getLevelFourStartVolume();
                        double levelFourTotalPrice = feeType.getLevelFourTotalPrice();
                        double levelFiveStartVolume = feeType.getLevelFiveStartVolume();
                        double levelFiveTotalPrice = feeType.getLevelFiveTotalPrice();
                        if (levelOneStartVolume == 0) {
                            fee_need = totalUnitPrice * this_amount + extraCost;
                        } else if (this_amount <= levelOneStartVolume) {
                            fee_need = totalUnitPrice * this_amount + extraCost;
                        } else if (this_amount > levelOneStartVolume && this_amount <= levelTwoStartVolume || levelTwoStartVolume == 0) {
                            fee_need = totalUnitPrice * levelOneStartVolume
                                    + levelOneTotalPrice
                                    * (this_amount - levelOneStartVolume)
                                    + extraCost;
                        } else if (this_amount > levelTwoStartVolume && this_amount <= levelThreeStartVolume || levelThreeStartVolume == 0) {
                            fee_need = totalUnitPrice * levelOneStartVolume
                                    + levelOneTotalPrice
                                    * (levelTwoStartVolume - levelOneStartVolume)
                                    + levelTwoTotalPrice
                                    * (this_amount - levelTwoStartVolume)
                                    + extraCost;
                        } else if (this_amount > levelThreeStartVolume && this_amount <= levelFourStartVolume || levelFourStartVolume == 0) {
                            fee_need = totalUnitPrice * levelOneStartVolume
                                    + levelOneTotalPrice
                                    * (levelTwoStartVolume - levelOneStartVolume)
                                    + levelTwoTotalPrice
                                    * (levelThreeStartVolume - levelTwoStartVolume)
                                    + levelThreeTotalPrice
                                    * (this_amount - levelThreeStartVolume)
                                    + extraCost;
                        } else if (this_amount > levelFourStartVolume && this_amount <= levelFiveStartVolume || levelFiveStartVolume == 0) {
                            fee_need = totalUnitPrice
                                    * levelOneStartVolume
                                    + levelOneTotalPrice
                                    * (levelTwoStartVolume - levelOneStartVolume)
                                    + levelTwoTotalPrice
                                    * (levelThreeStartVolume - levelTwoStartVolume)
                                    + levelThreeTotalPrice
                                    * (levelFourStartVolume - levelThreeStartVolume)
                                    + levelFourTotalPrice
                                    * (this_amount - levelFourStartVolume)
                                    + extraCost;
                        } else {
                            fee_need = totalUnitPrice
                                    * levelOneStartVolume
                                    + levelOneTotalPrice
                                    * (levelTwoStartVolume - levelOneStartVolume)
                                    + levelTwoTotalPrice
                                    * (levelThreeStartVolume - levelTwoStartVolume)
                                    + levelThreeTotalPrice
                                    * (levelFourStartVolume - levelThreeStartVolume)
                                    + levelFourTotalPrice
                                    * (levelFiveStartVolume - levelFourStartVolume)
                                    + levelFiveTotalPrice
                                    * (this_amount - levelFiveStartVolume)
                                    + extraCost;
                        }
                    }
                } else {
                    fee_need = null;
                }
                if (read_status.equals("1")) {// 成功
                    if ("0".equals(isAutoClear)) {// 自动结算
                        this_balance = last_balance - fee_need;
                        readInfo.setFee_status("1");// 已扣
                    } else {
                        this_balance = last_balance;
                        readInfo.setFee_status("2");// 未扣
                    }
                } else {
                    this_balance = last_balance;
                    readInfo.setFee_status("0");// 失败
                }
                readInfo.setThis_read(total_amount);
                readInfo.setLast_read(last_read);
                if (this_amount != null) {
                    this_amount = Math.round(this_amount * 100) / 100d;
                }
                readInfo.setThis_cost(this_amount);
                if (fee_need != null) {
                    fee_need = Math.round(fee_need * 100) / 100d;
                }
                readInfo.setFee_need(fee_need);
                readInfo.setBalance(Math.round(this_balance * 100) / 100d);
                readInfo.setLast_cost(last_cost);
                readInfo.setData2(data2);
                readInfo.setData3(data3);
                readInfo.setData4(data4);
                readInfo.setData5(data5);
                readInfo.setData6(data6);
                readInfo.setData7(data7);
                readInfo.setData8(data8);
                readInfo.setData9(data9);
            }
            readInfo.setOperate_id("CB" + String.valueOf(JoyDatetime.getTokenID()));
            readInfo.setUser_name(user.getUser_name());
            readInfo.setUser_address_area(user.getUser_address_area());
            readInfo.setUser_address_community(user.getUser_address_community());
            readInfo.setCommunity_no(user.getUser_address_community());
            readInfo.setUser_address_building(user.getUser_address_building());
            readInfo.setUser_address_unit(user.getUser_address_unit());
            readInfo.setUser_address_room(user.getUser_address_room());
            readInfo.setUser_address_original_room(user.getUser_address_original_room());
            readInfo.setSupplier_name(user.getSupplier_name());
            readInfo.setContact_info(user.getContact_info());
            readInfo.setConcentrator_name(user.getConcentrator_name());
            readInfo.setMeter_no(meter_no);
            readInfo.setMeter_type(user.getMeter_type());
            readInfo.setRead_type(read_type);
            readInfo.setFee_type(user.getFee_type());
            readInfo.setException(result == 0 ? "1" : "0");
            readInfo.setOperator_account(operator_account);
            readInfo.setOperate_time(new Timestamp(System.currentTimeMillis()));
            readInfo.setOperate_day(JoyDatetime.dateFormat(JoyDatetime.FORMAT_DATE_1));
            readInfo.setOperate_dayTime(JoyDatetime.dateFormat(JoyDatetime.FORMAT_DATETIME_5));
            readInfo.setUser_address(user.getUser_address_area()
                    + user.getUser_address_community()
                    + user.getUser_address_building()
                    + user.getUser_address_room());
            readInfo.setIsAutoClear(isAutoClear);
            if ("token".equals(category.toLowerCase())) { // 码表
                readInfo.setLast_read(0d);
                readInfo.setThis_cost(readInfo.getThis_read());
            }
            // 保存抄表记录或者阀门操作记录
            JoyServletContextListener.instanceCallback(userDao).callBackReadInfo(readInfo);
            readInfoCache.add(meter_no, readInfo);
            userDao.saveReadInfo(readInfo);
            userDao.delReadInfoFailed(meter_no);
            if ("0".equals(readInfo.getException())) { // 添加失败记录
                userDao.saveReadInfoFailed(readInfo);
            }
            if (lastReadInfo != null) { // 如果数据异常（本期用量小于0）,将本次抄表记录与上次抄表记录保存至可疑记录表
                ArrayList<ReadInfo> doubtfulReadInfo = new ArrayList<>();
                doubtfulReadInfo.add(lastReadInfo);
                doubtfulReadInfo.add(readInfo);
                userDao.saveDoubtfulReadInfo(doubtfulReadInfo);
            }
            this.saveEventInfo(this.toEventJson(parameters));
            this.updateDeviceState(user);
            this.updateValveState(user, data9);

            if (result == 0 && !"10".equals(isAutoClear)) { // "10":阀门操作
                OperateInfo operateInfo = this.getOperateInfo(readInfo, user, fee_need);
                if ("0".equals(isAutoClear)) {
                    rechargeService.payOrRefund(operateInfo, "autoPay");
                }
                this.shareBilling(user, readInfo, operateInfo, sendTime);
                this.sendMessage(user, readInfo, balanceWarnning);
                this.sendValveClose(user, valveClose, readInfo.getBalance(), operator_account);
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * 调用分摊
     *
     * @param user
     * @param readInfo
     * @param operateInfo
     * @param sendTime
     */
    private void shareBilling(User user, ReadInfo readInfo, OperateInfo operateInfo, String sendTime) {
        if (JoyServletContextListener.mode == null || JoyServletContextListener.mode.isEmpty()) {
            return;
        }
        if ("plus".equals(JoyServletContextListener.mode.toLowerCase())) {
            parentConfService.shareBilling(user, readInfo, operateInfo);
        }
        if ("jiangqiao".equals(JoyServletContextListener.mode.toLowerCase())) {
            parentConfService.shareBilling(user, readInfo, operateInfo, sendTime);
        }
    }

    /**
     * 将自动扣费信息添加到操作信息表中
     *
     * @param readInfo
     * @param user
     * @param fee_need
     * @return
     */
    private OperateInfo getOperateInfo(ReadInfo readInfo, User user, Double fee_need) {
        OperateInfo operateInfo = new OperateInfo();
        operateInfo.setBalance(readInfo.getBalance());
        operateInfo.setCity(user.getCity());
        operateInfo.setConcentrator_name(user.getConcentrator_name());
        operateInfo.setContact_info(user.getContact_info());
        operateInfo.setDistrict(user.getDistrict());
        operateInfo.setFee_type(user.getFee_type());
        operateInfo.setIsPrint("0");
        operateInfo.setMeter_model(user.getMeter_model());
        operateInfo.setMeter_no(user.getMeter_no());
        operateInfo.setMeter_type(user.getMeter_type());
        operateInfo.setOperate_id("KF" + String.valueOf(JoyDatetime.getTokenID()));
        operateInfo.setOperate_time(new Timestamp(System.currentTimeMillis()));
        operateInfo.setOperate_type("2");
        operateInfo.setOperator_account(readInfo.getOperator_account());
        operateInfo.setPay_method("现金");
        operateInfo.setProvince(user.getProvince());
        operateInfo.setRecharge_loc(user.getSupplier_name());
        operateInfo.setRecharge_money(fee_need);
        operateInfo.setUser_address_area(user.getUser_address_area());
        operateInfo.setUser_address_community(user.getUser_address_community());
        operateInfo.setUser_address_building(user.getUser_address_building());
        operateInfo.setUser_address_unit(user.getUser_address_unit());
        operateInfo.setUser_address_room(user.getUser_address_room());
        operateInfo.setUser_name(user.getUser_name());
        return operateInfo;
    }

    /**
     * 发送消息
     *
     * @param user
     * @param readInfo
     * @param balanceWarnning
     * @throws NumberFormatException
     */
    private void sendMessage(User user, ReadInfo readInfo, final String balanceWarnning) {
        try {
            Double balance_warn = RegexMatches.isNumeric(balanceWarnning) ? Double.parseDouble(balanceWarnning) : null;
            // 账户余额小于等于余额预警值，发送短信提示
            if (balance_warn != null && readInfo.getBalance() <= balance_warn) {
                String concentrator_name = userDao.findConcentratorNameByMeterNo(readInfo.getMeter_no());
                Concentrator con = userDao.findConcentrator(concentrator_name);
                String ip = con.getConcentrator_ip() + ":" + con.getConcentrator_port();
                StringBuilder parameter = new StringBuilder("");
                parameter.append("{number:'")
                        .append(user.getContact_info())
                        .append("',msg:'您的帐户余额已不足")
                        .append(balance_warn).append("元，请及时缴费，谢谢！'}");
                HttpRequest.sendPost(HttpRequest.GetSmsUrl(ip), parameter.toString());
                JoyServletContextListener.instanceCallback(userDao).callBackBalanceWarn(readInfo);
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * 关闭阀门
     *
     * @param user
     * @param valve_protocol
     * @param operator_account
     */
    private void sendValveClose(User user, String valveClose, Double this_balance, String operator_account) {
        try {
            Double valve_close = RegexMatches.isNumeric(valveClose) ? Double.parseDouble(valveClose) : null;
            // 账户余额小于等于阀门关闭值，关闭阀门
            String valve_protocol = user.getValve_protocol();
            if (valve_protocol == null || valve_protocol.isEmpty()) {
                return;
            }
            if (valve_close != null && this_balance <= valve_close && user.getPay_type() == 0) {
                Map<String, Object> params = new HashMap<>();
                params.put("roomId", user.getUser_address_room());
                params.put("supplier_name", user.getSupplier_name());
                List<User> meters = userDao.findUserByRoomId(params); // 查询该用户的电表表号
                if (meters == null || meters.isEmpty()) {
                    return;
                }
                meters.forEach((p) -> {
                    String concentrator_name = p.getConcentrator_name();
                    String valve_no = p.getValve_no();
                    Concentrator con = userDao.findConcentrator(concentrator_name);
                    String id = con.getConcentrator_no();
                    String ip = con.getConcentrator_ip() + ":" + con.getConcentrator_port();
                    String protocol_type = valve_protocol.replaceAll("\\s", "") + "_Close";
                    String sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
                    StringBuilder parameter = new StringBuilder("");
                    parameter.append("{DTU:[{id:'").append(id).append("',url:'")
                            .append(JoyServletContextListener.GetUrl())
                            .append("',options:{concentrator_model:'2', isAutoClear:'").append(10)
                            .append("',protocol:'").append(protocol_type).append("',accountName:'")
                            .append(operator_account).append("',sendTime:'").append(sendTime)
                            .append("'},meters:[{meter:'").append(valve_no).append("',category:'")
                            .append(user.getMeter_type()).append("',protocol:'").append(protocol_type).append("'}]}]}");
                    HttpRequest.sendPost(HttpRequest.GetUrl(ip), parameter.toString());
                    ArrayList<String[]> meterList = new ArrayList<>();
                    String[] arr = {id, valve_no, p.getMeter_type(), protocol_type, ip, "2", "sendUserParameters"};
                    meterList.add(arr);
                    JoyServletContextListener.instanceSaaS(userDao).down(ParameterUtil.GetParameter(meterList));
                });
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * 查询抄表结果
     *
     * @param sendTime
     * @return
     */
    @Override
    public Result findReadResult(String sendTime) {
        Result result = new Result();
        try {
            Timestamp time = Timestamp.valueOf(sendTime);
            List<ReadInfo> readInfo = readInfoCache.findReadResult(time);
            result.setStatus(1);
            result.setData(readInfo);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 按用户抄表时，根据操作员账号查询抄表结果
     *
     * @param sendTime
     * @param accountName
     * @return
     */
    @Override
    public Result findReadResultByOperator(final String sendTime, final String accountName) {
        Result result = new Result();
        try {
            JoyLogger.LogInfo(String.format("findReadResultByOperator, sendTime: %s", sendTime));
            if (sendTime == null || sendTime.isEmpty()) {
                return result;
            }
            Timestamp time = Timestamp.valueOf(sendTime);
            List<ReadInfo> readInfo = readInfoCache.findReadResult(time);
            result.setStatus(1);
            result.setData(readInfo);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            result.setStatus(0);
            result.setData(ex.toString());
            JoyLogger.LogInfo(String.format("findReadResultByOperator, Exception message: %s", ex.toString()));
        }
        return result;
    }

    /**
     * 按小区抄表时，根据操作员账号查询抄表结果
     *
     * @param sendTime
     * @param accountName
     * @return
     */
    @Override
    public Result updateCommunityReadResult(String sendTime, String accountName) {
        Result result = new Result();
        try {
            Timestamp time = Timestamp.valueOf(sendTime);
            Map<String, Object> params = new HashMap<>();
            params.put("sendTime", time);
            params.put("accountName", accountName);
            List<ReadInfo> readInfo = userDao.findReadResultByOperator(params);
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < readInfo.size(); i++) {
                ReadInfo read_info = readInfo.get(i);
                String communityName = read_info.getUser_address_community();
                if (!map.containsKey(communityName)) {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("count1", 0);// 抄表成功的数量
                    dataMap.put("count2", 0);// 抄表失败的数量
                    dataMap.put("count3", 0);// 电池欠压的数量
                    dataMap.put("count4", 0d);// 抄表读数的总量
                    map.put(communityName, dataMap);
                }
                String exception = read_info.getException();
                Double this_read = read_info.getThis_read();
                if (this_read == null) {
                    this_read = 0d;
                }
                String batteryStatus = read_info.getData9();
                Map dataMap = (Map) map.get(communityName);
                if ("1".equals(exception)) {// 抄表成功
                    int count1 = (int) dataMap.get("count1");
                    count1++;
                    dataMap.put("count1", count1);
                    double count4 = (double) dataMap.get("count4");
                    count4 += this_read;
                    dataMap.put("count4", count4);
                } else {// 抄表失败
                    int count2 = (int) dataMap.get("count2");
                    count2++;
                    dataMap.put("count2", count2);
                }
                if (!"".equals(batteryStatus)) {// 电池欠压
                    int count3 = (int) dataMap.get("count3");
                    count3++;
                    dataMap.put("count3", count3);
                }
                map.put(communityName, dataMap);
            }
            result.setStatus(1);
            result.setData(map);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 保存设置的抄表参数
     *
     * @param readParameter
     * @param original_day
     * @param original_hour
     * @param original_minute
     * @param original_balance_warn
     * @param original_valve_close
     * @param original_isAutoRead
     * @param original_isAutoInform
     * @return
     */
    @Override
    public Result saveReadParams(ReadParameter readParameter, String original_day, String original_hour,
            String original_minute, Double original_balance_warn, Double original_valve_close,
            String original_isAutoRead, String original_isAutoInform) {
        Result result = new Result();
        try {
            String parameter_id = readParameter.getParameter_id();
            userDao.deletePreReadParameter(parameter_id);
            userDao.saveReadParameter(readParameter);
            result.setStatus(1);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 查询设置的抄表参数
     *
     * @return
     */
    @Override
    public Result showReadParameter() {
        Result result = new Result();
        try {
            List<ReadParameter> readParameters = userDao.findReadParameter();
            result.setStatus(1);
            result.setData(readParameters);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 查询抄表成功信息，失败信息，可疑信息
     *
     * @param userName
     * @param community
     * @param building
     * @param unit
     * @param room
     * @param operatorName
     * @param feeTypeName
     * @param meterNo
     * @param meterType
     * @param startTime
     * @param endTime
     * @param startNo
     * @param pageSize 用来判断前端发出的是哪种查表请求
     * @param queryTableType
     * @param request
     * @return
     */
    @Override
    public Result findReadInfo(String userName, String community, String building, String unit, String room,
            String operatorName, String feeTypeName, String meterNo, String meterType, String startTime, String endTime, int startNo,
            int pageSize, String queryTableType, HttpServletRequest request) {
        Result result = new Result();
        String supplier_name = (String) request.getSession().getAttribute("supplier_name");
        HashMap<String, Object> params = new HashMap<>();
        params.put("supplier_name", supplier_name);
        params.put("user_name", userName);
        params.put("community", community);
        params.put("building", building);
        params.put("unit", unit);
        params.put("room", room);
        params.put("operator_name", operatorName);
        params.put("meter_no", meterNo);
        params.put("meter_type", meterType);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("startNo", startNo);
        params.put("pageSize", pageSize);
        params.put("fee_type", feeTypeName);
        List<ReadInfo> readInfo = null;
        int totalNo = 0;
        try {
            switch (queryTableType) {
                case "queryRecord":
                    readInfo = userDao.findReadInfo(params);
                    totalNo = userDao.findReadInfoTotal(params);
                    break;
                case "queryCurrent":
                    readInfoCache.findReadInfoCurrent();
                    readInfo = readInfoCache.query(params);
                    if (!JoyDatetime.dateFormat(JoyDatetime.strToDate(endTime, JoyDatetime.FORMAT_DATETIME_2), JoyDatetime.FORMAT_DATE_1).equals(JoyDatetime.dateFormat(JoyDatetime.FORMAT_DATE_1))
                            || "-1".equals(params.get("pageSize")) || "-1".equals(params.get("startNo"))) {
                        params.put("startNo", startNo);
                        params.put("pageSize", pageSize);
                        readInfo = userDao.findReadInfoCurrent(params);
                        totalNo = userDao.findTotalReadInfoCurrent(params);
                        break;
                    }
                    params.put("startNo", "0");
                    params.put("pageSize", "0");
                    List<ReadInfo> readInfoTotal = readInfoCache.query(params);
                    totalNo = readInfoTotal.size();
                    break;
                case "queryFail":
                    readInfo = userDao.findReadInfoFail(params);
                    totalNo = userDao.findTotalReadInfoNoFail(params);
                    break;
                case "queryDoubtful":
                    readInfo = userDao.findReadInfoDoubtful(params);
                    totalNo = userDao.findTotalReadInfoNoDoubtful(params);
                    break;
                default:
                    break;
            }

            params.clear();
            params.put("readInfo", readInfo);
            params.put("totalNo", totalNo);
            result.setStatus(1);
            result.setData(params);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }
    /**
     * 左权
     */
    @Override
    public Result zuoQuanFindReadInfo(String userName, String community, String building, String unit, String room,
            String operatorName, String feeTypeName, String meterNo, String meterType, String startTime, String endTime, int startNo,
            int pageSize,String minBalance,String maxBalance, String queryTableType, HttpServletRequest request) {
        Result result = new Result();
        String supplier_name = (String) request.getSession().getAttribute("supplier_name");
        HashMap<String, Object> params = new HashMap<>();
        if(minBalance==null||minBalance=="") {
            params.put("minBalance",null);
        }else {
            params.put("minBalance",Double.parseDouble(minBalance));
        }
        if(maxBalance==null||maxBalance=="") {
            params.put("maxBalance", null);
        }else {
            params.put("maxBalance",Double.parseDouble(maxBalance));
        }
        params.put("supplier_name", supplier_name);
        params.put("user_name", userName);
        params.put("community", community);
        params.put("building", building);
        params.put("unit", unit);
        params.put("room", room);
        params.put("operator_name", operatorName);
        params.put("meter_no", meterNo);
        params.put("meter_type", meterType);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("startNo", startNo);
        params.put("pageSize", pageSize);    
        params.put("fee_type", feeTypeName);
        List<ReadInfo> readInfo = null;
        int totalNo = 0;
        try {
            switch (queryTableType) {
                case "queryRecord":
                    readInfo = userDao.findReadInfo(params);
                    totalNo = userDao.findReadInfoTotal(params);
                    break;
                case "queryCurrent":
                    readInfoCache.findReadInfoCurrent();
                    readInfo = readInfoCache.query(params);
                    if (!JoyDatetime.dateFormat(JoyDatetime.strToDate(endTime, JoyDatetime.FORMAT_DATETIME_2), JoyDatetime.FORMAT_DATE_1).equals(JoyDatetime.dateFormat(JoyDatetime.FORMAT_DATE_1))
                            || "-1".equals(params.get("pageSize")) || "-1".equals(params.get("startNo"))) {
                        params.put("startNo", startNo);
                        params.put("pageSize", pageSize);
                        readInfo = userDao.findReadInfoCurrent(params);
                        totalNo = userDao.findTotalReadInfoCurrent(params);
                        break;
                    }
                    params.put("startNo", "0");
                    params.put("pageSize", "0");
                    List<ReadInfo> readInfoTotal = readInfoCache.query(params);
                    totalNo = readInfoTotal.size();
                    break;
                case "queryFail":
                    readInfo = userDao.findReadInfoFail(params);
                    totalNo = userDao.findTotalReadInfoNoFail(params);
                    break;
                case "queryDoubtful":
                    readInfo = userDao.findReadInfoDoubtful(params);
                    totalNo = userDao.findTotalReadInfoNoDoubtful(params);
                    break;
                default:
                    break;
            }

            params.clear();
            params.put("readInfo", readInfo);
            params.put("totalNo", totalNo);
            result.setStatus(1);
            result.setData(params);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }
    
    /**
     * 按月查询抄表信息
     *
     * @param month
     * @param meter_type
     * @param startNo
     * @param pageSize queryTableType用来判断前端发出的是哪种查表请求
     * @param queryTableType
     * @param request
     * @return
     */
    @Override
    public Result findReadInfoByMonth(String month, String meter_type, int startNo, int pageSize, String queryTableType,
            HttpServletRequest request) {
        Result result = new Result();
        long time = System.currentTimeMillis();
        String year = new SimpleDateFormat("yyyy").format(time);
        int totalNo = 0;
        List<ReadInfo> readInfo = null;
        String supplier_name = (String) request.getSession().getAttribute("supplier_name");
        HashMap<String, Object> params = new HashMap<>();
        params.put("supplier_name", supplier_name);
        params.put("meter_type", meter_type);
        params.put("startNo", startNo);
        params.put("pageSize", pageSize);
        try {
            if (!month.equals("all")) {
                int m = Integer.parseInt(month);
                month = year + "-" + (m < 10 ? ("0" + m) : m);
            }
            params.put("month", month);
            switch (queryTableType) {
                case "queryRecord":
                    readInfo = userDao.findReadInfoMonthly(params);
                    totalNo = userDao.findTotalReadInfoNoMonthly(params);
                    break;
                case "queryCurrent":
                    readInfo = userDao.findReadInfoCurrentMonthly(params);
                    totalNo = userDao.findTotalReadInfoCurrentMonthly(params);
                    break;
                case "queryFail":
                    readInfo = userDao.findReadInfoMonthlyFail(params);
                    totalNo = userDao.findTotalReadInfoNoMonthlyFail(params);
                    break;
                case "queryDoubtful":
                    readInfo = userDao.findReadInfoMonthlyDoubtful(params);
                    totalNo = userDao.findTotalReadInfoNoMonthlyDoubtful(params);
                    break;
                default:
                    break;
            }

            params.clear();
            params.put("totalNo", totalNo);
            params.put("readInfo", readInfo);
            result.setStatus(1);
            result.setData(params);
        } catch (NumberFormatException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     *
     * @param req
     * @param list
     * @param isAutoClear
     * @param accountName
     * @return
     */
    @Override
    public Result sendFailedParams(HttpServletRequest req, ArrayList<String[]> list, int isAutoClear,
            String accountName) {
        Result result = new Result();
        StringBuffer reqURL = req.getRequestURL();
        String url = reqURL.substring(0, reqURL.lastIndexOf("/"));
        url += "/receive.do";
        Map<String, Object> map = new HashMap<>();
        ArrayList<String[]> meterList = new ArrayList<>();
        // 统计集中器非启用状态的表号集合
        ArrayList<String> meter_noFalseList = new ArrayList<>();
        // 统计集中器不在线状态表号集合
        ArrayList<String> meter_noFalseList1 = new ArrayList<>();
        // 统计符合抄表条件表的表号集合
        ArrayList<String> meter_noSucList = new ArrayList<>();
        Set<String> ipSet = new HashSet<>();
        try {
            // 根据集中器名称查询集中器id和ip
            for (int i = 0; i < list.size() - 1; i++) {
                String[] ary = list.get(i);
                String concentrator_name = ary[0];
                String meter_no = ary[1];
                // 如果是开关阀操作，表号参数换成阀门编号
                if (isAutoClear == 10) {
                    String valve_no = userDao.findValveNoByMeterNo(meter_no);
                    ary[1] = valve_no;
                }
                Concentrator con = userDao.findConcentrator(concentrator_name);
                if (con == null) {
                    meter_noFalseList.add(meter_no);
                    continue;
                }
                String concentrator_model = con.getConcentrator_model();
                if (concentrator_model == null) {
                    meter_noFalseList.add(meter_no);
                    continue;
                }
                // 当集中器模式处于安装,故障时不发出抄表请求
                if (concentrator_model.equals("0") || concentrator_model.equals("3")) {
                    meter_noFalseList.add(meter_no);
                    continue;
                }
                // 调试模式可以发出抄表请求，但是并不保存数据到数据库,只是作为调试用, 
                // 因此也归位失败一类，前端统一显示为非启用状态
                if (concentrator_model.equals("1")) {
                    meter_noFalseList.add(meter_no);
                } else {
                    meter_noSucList.add(meter_no);
                }
                String ip = con.getConcentrator_ip() + ":" + con.getConcentrator_port();
                ipSet.add(ip);
                String id = con.getConcentrator_no();
                User user = userDao.findUserByMeterNo(meter_no);
                if (user == null) {
                    continue;
                }
                String meter_type = user.getMeter_type();
                String protocol_type = user.getProtocol_type();
                String method = "sendFailedParameters";
                String[] arr = {id, meter_no, meter_type, protocol_type, ip, concentrator_model, method};
                meterList.add(arr);
            }

            // 根据集中器ip，把抄表参数(String ip->List<String[]>)以key-value的形式放进map
            for (String ip : ipSet) {
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
            Double balanceWarnning = null, valveClose = null;
            ReadParameter rp = this.getReadParameters();
            if (rp != null) {
                balanceWarnning = rp.getBalance_warn();
                valveClose = rp.getValve_close();
            }
            String sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
            // 根据集中器的IP，分别向相应的服务器发送抄表请求
            for (Map.Entry entry : map.entrySet()) {
                String ip = entry.getKey().toString();
                meterList = (ArrayList<String[]>) entry.getValue();
                String parameters = ParameterUtil.GetParameter(meterList, url, isAutoClear, accountName, sendTime,
                        balanceWarnning, valveClose);
                HttpRequest.sendPost(HttpRequest.GetUrl(ip), parameters);
            }
            map.clear();
            map.put("meter_noFalseList", meter_noFalseList);
            map.put("meter_noFalseList1", meter_noFalseList1);
            map.put("meter_noSucList", meter_noSucList);
            map.put("sendTime", sendTime);
            result.setStatus(1);
            result.setData(map);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 获取警告，关阀的参数值
     *
     * @return
     */
    @Override
    public ReadParameter getReadParameters() {
        List<ReadParameter> readParameters = userDao.findReadParameter();
        if (readParameters != null && !readParameters.isEmpty()) {
            ReadParameter rp0 = readParameters.get(0);
            ReadParameter rp1 = readParameters.get(0);
            if (rp0 != null) {
                if (rp0.getBalance_warn() == null) {
                    if (rp1 != null && rp1.getBalance_warn() != null) {
                        rp0.setBalance_warn(rp1.getBalance_warn());
                    }
                }
                if (rp0.getValve_close() == null) {
                    if (rp1 != null && rp1.getValve_close() != null) {
                        rp0.setValve_close(rp1.getValve_close());
                    }
                }
                return rp0;
            }
        }
        return null;
    }

    /**
     * 手动输入抄表的数据
     *
     * @param meter_no
     * @param this_read
     * @param operator_account
     * @return
     */
    @Override
    public Result inputByHand(String meter_no, double this_read, String operator_account) {
        Result result = new Result();
        try {
            // 查询用户抄表信息
            ReadInfo read_info = userDao.findLastReadInfoByMeterNo(meter_no);
            Double last_read = read_info == null ? null : read_info.getThis_read();
            Double this_amount = read_info == null ? this_read : this_read - last_read;
            Double last_cost = read_info == null ? null : read_info.getThis_cost();
            if (last_cost != null) {
                last_cost = Math.round(last_cost * 100) / 100d;
            }
            User user = userDao.findUserByMeterNo(meter_no);
            String feeTypeName = user.getFee_type();
            String meter_type = user.getMeter_type();
            FeeType feeType = userDao.findFeeTypeByName(feeTypeName);
            String isLevelPrice = feeType.getIsLevelPrice();
            double totalUnitPrice = feeType.getTotalUnitCost();
            double extraCost = this_amount == 0 ? 0 : feeType.getExtraCost();
            double fee_need;
            double this_balance;
            ReadInfo readInfo = new ReadInfo();
            double last_balance = user.getLast_balance();
            if (isLevelPrice.equals("0")) {// 否
                fee_need = totalUnitPrice * this_amount + extraCost;
            } else {
                double levelOneStartVolume = feeType.getLevelOneStartVolume();
                double levelOneTotalPrice = feeType.getLevelOneTotalPrice();
                double levelTwoStartVolume = feeType.getLevelTwoStartVolume();
                double levelTwoTotalPrice = feeType.getLevelTwoTotalPrice();
                double levelThreeStartVolume = feeType.getLevelThreeStartVolume();
                double levelThreeTotalPrice = feeType.getLevelThreeTotalPrice();
                double levelFourStartVolume = feeType.getLevelFourStartVolume();
                double levelFourTotalPrice = feeType.getLevelFourTotalPrice();
                double levelFiveStartVolume = feeType.getLevelFiveStartVolume();
                double levelFiveTotalPrice = feeType.getLevelFiveTotalPrice();
                if (this_amount <= levelOneStartVolume) {
                    fee_need = totalUnitPrice * this_amount + extraCost;
                } else if (this_amount > levelOneStartVolume && this_amount <= levelTwoStartVolume
                        || levelTwoStartVolume == 0) {
                    fee_need = totalUnitPrice * levelOneStartVolume
                            + levelOneTotalPrice * (this_amount - levelOneStartVolume) + extraCost;
                } else if (this_amount > levelTwoStartVolume && this_amount <= levelThreeStartVolume
                        || levelThreeStartVolume == 0) {
                    fee_need = totalUnitPrice * levelOneStartVolume
                            + levelOneTotalPrice * (levelTwoStartVolume - levelOneStartVolume)
                            + levelTwoTotalPrice * (this_amount - levelTwoStartVolume) + extraCost;
                } else if (this_amount > levelThreeStartVolume && this_amount <= levelFourStartVolume
                        || levelFourStartVolume == 0) {
                    fee_need = totalUnitPrice * levelOneStartVolume
                            + levelOneTotalPrice * (levelTwoStartVolume - levelOneStartVolume)
                            + levelTwoTotalPrice * (levelThreeStartVolume - levelTwoStartVolume)
                            + levelThreeTotalPrice * (this_amount - levelThreeStartVolume) + extraCost;
                } else if (this_amount > levelFourStartVolume && this_amount <= levelFiveStartVolume
                        || levelFiveStartVolume == 0) {
                    fee_need = totalUnitPrice * levelOneStartVolume
                            + levelOneTotalPrice * (levelTwoStartVolume - levelOneStartVolume)
                            + levelTwoTotalPrice * (levelThreeStartVolume - levelTwoStartVolume)
                            + levelThreeTotalPrice * (levelFourStartVolume - levelThreeStartVolume)
                            + levelFourTotalPrice * (this_amount - levelFourStartVolume) + extraCost;
                } else {
                    fee_need = totalUnitPrice * levelOneStartVolume
                            + levelOneTotalPrice * (levelTwoStartVolume - levelOneStartVolume)
                            + levelTwoTotalPrice * (levelThreeStartVolume - levelTwoStartVolume)
                            + levelThreeTotalPrice * (levelFourStartVolume - levelThreeStartVolume)
                            + levelFourTotalPrice * (levelFiveStartVolume - levelFourStartVolume)
                            + levelFiveTotalPrice * (this_amount - levelFiveStartVolume) + extraCost;
                }
            }
            this_balance = last_balance - fee_need;
            readInfo.setFee_status("1");// 成功
            Long time = System.currentTimeMillis();
            Date date = new Date(time);
            Timestamp read_time = new Timestamp(time);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat dayTime = new SimpleDateFormat("HH:mm:ss");
            String operate_id = "CB" + String.valueOf(JoyDatetime.getTokenID());
            String operate_day = day.format(date);
            String operate_dayTime = dayTime.format(date);
            readInfo.setOperate_id(operate_id);
            readInfo.setUser_name(user.getUser_name());
            readInfo.setUser_address_area(user.getUser_address_area());
            String community_name = user.getUser_address_community();
            readInfo.setUser_address_community(community_name);
            String community_no = userDao.findCommunityNoByName(community_name);
            readInfo.setCommunity_no(community_no);
            readInfo.setUser_address_building(user.getUser_address_building());
            readInfo.setUser_address_unit(user.getUser_address_unit());
            readInfo.setUser_address_room(user.getUser_address_room());
            readInfo.setContact_info(user.getContact_info());
            readInfo.setConcentrator_name(user.getConcentrator_name());
            readInfo.setMeter_no(meter_no);
            readInfo.setFee_type(feeTypeName);
            readInfo.setThis_read(this_read);
            readInfo.setLast_read(last_read);
            readInfo.setThis_cost(Math.round(this_amount * 100) / 100d);
            readInfo.setFee_need(Math.round(fee_need * 100) / 100d);
            readInfo.setBalance(Math.round(this_balance * 100) / 100d);
            readInfo.setException("1");// 成功
            readInfo.setLast_cost(last_cost);
            readInfo.setRead_type("1");// 手动
            readInfo.setOperator_account(operator_account);
            readInfo.setOperate_time(read_time);
            readInfo.setUser_address(user.getUser_address_area() + user.getUser_address_community()
                    + user.getUser_address_building() + user.getUser_address_room());
            readInfo.setOperate_day(operate_day);
            readInfo.setOperate_dayTime(operate_dayTime);
            readInfo.setMeter_type(meter_type);
            readInfo.setData2(0.0);
            readInfo.setData3(0.0);
            readInfo.setData4(0.0);
            readInfo.setData5(0.0);
            readInfo.setData6(0.0);
            readInfo.setData7(0.0);
            readInfo.setData8(0.0);
            readInfo.setData9("");

            userDao.saveReadInfo(readInfo);
            userDao.delReadInfoFailed(meter_no);
            long user_id = user.getUser_id();
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", user_id);
            params.put("this_balance", this_balance);
            params.put("last_read_time", read_time);
            userDao.updateBalance(params);
            result.setStatus(1);
            List<ReadParameter> readParameters = userDao.findReadParameter();
            if (readParameters == null) {
                return null;
            }
            Double balanceWarnning = null, valveClose = null;
            ReadParameter rp = this.getReadParameters();
            if (rp != null) {
                balanceWarnning = rp.getBalance_warn();
                valveClose = rp.getValve_close();
            }
            this.sendMessage(user, readInfo, balanceWarnning == null ? "null" : String.valueOf(balanceWarnning));
            this.sendValveClose(user, valveClose == null ? "null" : String.valueOf(valveClose), this_balance, operator_account);

        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 根据表号查询用户信息
     *
     * @param meterNo
     * @return
     */
    @Override
    public Result findInfoByMeterNo(String meterNo) {
        Result result = new Result();
        try {
            User user = userDao.findUserByMeterNo(meterNo);
            result.setStatus(1);
            result.setData(user);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 加载需要催缴的用户信息
     *
     * @param userName
     * @param community
     * @param building
     * @param unit
     * @param room
     * @param operatorName
     * @param meterNo
     * @param startNo
     * @param pageSize
     * @param request
     * @return
     */
    @Override
    public Result loadUsersRemind(String userName, String community, String building, String unit, String room,
            String operatorName, String meterNo, int startNo, int pageSize, HttpServletRequest request) {
        Result result = new Result();
        String supplier = (String) request.getSession().getAttribute("supplier");
        HashMap<String, Object> params = new HashMap<>();
        params.put("supplier", supplier);
        params.put("user_name", userName);
        params.put("community", community);
        params.put("building", building);
        params.put("unit", unit);
        params.put("room", room);
        params.put("operator_name", operatorName);
        params.put("meter_no", meterNo);
        params.put("startNo", startNo);
        params.put("pageSize", pageSize);
        try {
            List<User> users = userDao.findUsersRemind(params);
            int totalNo = userDao.findTotalUsersNoRemind(params);
            params.clear();
            params.put("users", users);
            params.put("totalNo", totalNo);
            result.setStatus(1);
            result.setData(params);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 添加催缴标记
     *
     * @param meterNo
     * @param user_name
     * @return
     */
    @Override
    public Result markup(String meterNo, String user_name) {
        Result result = new Result();
        try {
            userDao.markup(meterNo);
            result.setStatus(1);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 取消催缴标记
     *
     * @param meterNo
     * @param user_name
     * @return
     */
    @Override
    public Result delMarkup(String meterNo, String user_name) {
        Result result = new Result();
        try {
            userDao.delMarkup(meterNo);
            result.setStatus(1);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 根据uuid合闸或者跳闸
     *
     * @param req
     * @param uuid
     * @param action action="0" 跳闸 action="1" 合闸
     * @return
     */
    @Override
    public String ctrValveByUUID(HttpServletRequest req, String action, String uuid) {
        final String resultMsg = "{\"resultCode\":\"%s\", \"resultMsg\":\"%s\"}";
        try {
            String meterNo = userDao.findMeterByUUID(uuid);
            if (meterNo == null) {
                return String.format(resultMsg, 1, "Invalid uuid");
            }
            Result result = this.ctrValveByMeterNo(req, meterNo, action, "joy000001");
            if (result != null && result.getStatus() == 1) {
                return String.format(resultMsg, 0, "action sucessful");
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return String.format(resultMsg, 1, "action fail");
    }

    /**
     * 根据表号控制继电器
     *
     * @param req
     * @param meterNo
     * @param action
     * @param operator
     * @return
     */
    @Override
    public synchronized Result ctrValveByMeterNo(HttpServletRequest req, String meterNo, String action, final String operator) {
        Result result = new Result();
        if (!RegexMatches.isMeterNo(meterNo)) {
            result.setData("输入的表号格式不正确，请重新输入");
            return result;
        }
        try {
            User user = userDao.findUserByMeterNo(meterNo);
            if (user == null) {
                result.setData("用户不存在，不能进行开关阀操作，请重新输入");
                return result;
            }
            String protocolType = user.getValve_protocol();
            if (protocolType == null || protocolType.isEmpty()) {
                result.setData("该表协议有问题，不能进行开关阀操作，请重新输入");
                return result;
            }
            // 如果当前阀门状态和请求阀门状态一致,则返回成功
            //if (action.equals(user.getValve_status())) { 
            //    result.setStatus(1);
            //    return result;
            //}
            switch (action) {
                case "1":
                    protocolType += "_Open";
                    break;
                case "0":
                    protocolType += "_Close";
                    break;
                default:
                    result.setData("输入的协议类型不正确，请重新输入");
                    return result;
            }
            ArrayList<String[]> list = new ArrayList<>();
            String valve_no = userDao.findValveNoByMeterNo(meterNo);
            String[] params = {user.getConcentrator_name(), valve_no, user.getMeter_type(), protocolType};
            list.add(params);
            list.add(new String[]{"", "", "", ""});
            sendDevParams(req, list, 10, operator);
            Timestamp time = new Timestamp(System.currentTimeMillis());
            while (true) {
                long dif = ((new Timestamp(System.currentTimeMillis())).getTime() - time.getTime()) / 1000;
                if (dif > 30) {
                    break;
                }
                this.wait(600);
                List<ReadInfo> readInfos = valveCache.findValveStatus(time);
                if (readInfos == null || readInfos.isEmpty()) {
                    continue;
                }
                if (readInfos.stream().filter(p -> p.getMeter_no().equals(meterNo))
                        .filter(p -> p.getIsAutoClear().equals("10"))
                        .count() > 0) {
                    result.setStatus(1);
                    break;
                }
            }
        } catch (InterruptedException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setStatus(0);
        }
        JoyLogger.LogInfo(String.format("controlByMeterNo meter no: %s, active: %s, res: %s", meterNo, action, JSON.toJSON(result)));
        return result;
    }

    /**
     * 异步控制阀门
     *
     * @param req
     * @param meterNo
     * @param action
     * @param operator
     * @return
     */
    @Override
    public Result ctrValveByMeterNo_Async(HttpServletRequest req, String meterNo, String action, final String operator) {
        Result result = new Result();
        ReadInfo readInfo = new ReadInfo();
        readInfo.setException("0");
        readInfo.setIsAutoClear("10");
        try {
            String[] meterNos = meterNo.split(",");
            if (meterNos == null || meterNos.length <= 0) {
                JoyServletContextListener.instanceCallback(userDao).callBackValveState(readInfo);
                result.setData("输入的表号格式不正确，请重新输入");
                return result;
            }
            for (String meter : meterNos) {
                if (!RegexMatches.isMeterNo(meter)) {
                    readInfo.setMeter_no(meter);
                    readInfo.setData9(action);
                    readInfo.setContact_info("输入的表号格式不正确，请重新输入");
                    CallBackCache.add(readInfo);
                }
            }
            ArrayList<String[]> list = new ArrayList<>();
            for (String meter : meterNos) {
                User user = userDao.findUserByMeterNo(meter);
                if (user == null) {
                    readInfo.setMeter_no(meter);
                    readInfo.setData9(action);
                    readInfo.setContact_info("输入的表号对应的用户不存在，请重新输入");
                    CallBackCache.add(readInfo);
                    continue;
                }
                String protocolType = user.getValve_protocol();
                if (protocolType == null || protocolType.isEmpty()) {
                    readInfo.setMeter_no(meter);
                    readInfo.setData9(action);
                    readInfo.setContact_info("输入的表号对应的协议不存在，请重新输入");
                    CallBackCache.add(readInfo);
                    continue;
                }
                // 如果当前阀门状态和请求阀门状态一致,则返回成功
                //if (action.equals(user.getValve_status())) { 
                //    result.setStatus(1);
                //    return result;
                //}
                switch (action) {
                    case "1":
                        protocolType += "_Open";
                        break;
                    case "0":
                        protocolType += "_Close";
                        break;
                    default:
                        readInfo.setMeter_no(meter);
                        CallBackCache.add(readInfo);
                        continue;
                }
                String valve = user.getValve_no();
                if (!RegexMatches.isMeterNo(valve)) {
                    readInfo.setMeter_no(meter);
                    readInfo.setData9(action);
                    readInfo.setContact_info("输入的表号对应的阀门编号不正确，请重新输入");
                    CallBackCache.add(readInfo);
                    continue;
                }
                String[] params = {user.getConcentrator_name(), valve, user.getMeter_type(),
                    protocolType};
                list.add(params);
                CallBackCache.add(valve);
            }
            list.add(new String[]{"", "", "", ""});
            int isAutoClear = 10; // 阀门处理
            Timestamp time = new Timestamp(System.currentTimeMillis());
            this.sendDevParams(req, list, isAutoClear, operator);
            result.setStatus(1);
            result.setData(time);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            CallBackCache.add(readInfo);
            result.setStatus(0);
        }
        JoyLogger.LogInfo(String.format("mbusControlByMeterNo_Async meter no.:%s, action: %s, res: %s", meterNo, action,
                JSON.toJSON(result)));
        return result;
    }

    /**
     * 根据表号抄表
     *
     * @param req
     * @param meterNo
     * @param operator
     * @return
     */
    @Override
    public synchronized Result readByMeterNo(HttpServletRequest req, final String meterNo, final String operator) {
        String[] meterNos = meterNo.split(",");
        return readByMeterNo(req, meterNos, operator);
    }

    /**
     * 异步 根据表号抄表
     *
     * @param req
     * @param meterNo
     * @param operator
     * @return
     */
    @Override
    public Result readByMeterNo_Async(HttpServletRequest req, final String meterNo, final String operator) {
        String[] meterNos = meterNo.split(",");
        return readByMeterNo_Async(req, meterNos, operator);
    }

    /**
     * 根据表号抄表,同时回调给第三方,回调接口由根据第三方的制定.
     *
     * @param req
     * @param meter
     * @param operator
     * @return
     */
    @Override
    public Result readByMeterNo_Callback(HttpServletRequest req, final String meter, final String operator) {
        String[] meterNos = meter.split(",");
        Result result = new Result();
        if (meterNos == null || meterNos.length <= 0) {
            result.setData("输入的表号格式不正确，请重新输入");
            return result;
        }
        for (String meterNo : meterNos) {
            if (!RegexMatches.isMeterNo(meterNo)) {
                result.setData("输入的表号格式不正确，请重新输入");
                return result;
            }
        }
        ArrayList<String[]> list = new ArrayList<>();
        for (String meterNo : meterNos) {
            User user = userDao.findUserByMeterNo(meterNo);
            if (user == null) {
                continue;
            }
            String[] params = {user.getConcentrator_name(), meterNo, user.getMeter_type(), user.getProtocol_type()};
            list.add(params);
        }
        list.add(new String[]{"", "", "", ""});
        int isAutoClear = 1; // 自动扣费
        this.sendDevParams(req, list, isAutoClear, operator);
        result.setStatus(0);
        return result;
    }

    /**
     * 根据表号抄表
     *
     * @param req
     * @param meterNos
     * @param operator
     * @return
     */
    @Override
    public synchronized Result readByMeterNo(HttpServletRequest req, final String[] meterNos, final String operator) {
        Result result = new Result();
        try {
            if (meterNos == null || meterNos.length <= 0) {
                result.setData("输入的表号格式不正确，请重新输入");
                return result;
            }
            for (String meterNo : meterNos) {
                if (!RegexMatches.isMeterNo(meterNo)) {
                    result.setData("输入的表号格式不正确，请重新输入");
                    return result;
                }
            }
            List<ReadInfo> res = new ArrayList<>();
            ArrayList<String[]> list = new ArrayList<>();
            for (String meterNo : meterNos) {
                User user = userDao.findUserByMeterNo(meterNo);
                if (user == null || user.getProtocol_type() == null) {
                    continue;
                }
                if (user.getProtocol_type().toLowerCase().startsWith("com.joymeter.dtu")) {
                    String[] params = {user.getConcentrator_name(), meterNo, user.getMeter_type(), user.getProtocol_type()};
                    list.add(params);
                    JoyLogger.LogInfo(String.format("ReadServiceImp.readByMeterNo, meterNo: %s", meterNo));
                }
                if (user.getProtocol_type().toLowerCase().startsWith("jlaa")) {
                    ReadInfo ri = readInfoCache.findLastReadInfoByMeterNo(meterNo);
                    if (ri != null) {
                        res.add(ri);
                    }
                }
            }
            if (list.isEmpty() && res.size() > 0) { // 如果不存在有线抄表,则直接返回无线的抄表数据
                result.setStatus(1);
                result.setData(res);
                return result;
            }
            list.add(new String[]{"", "", "", ""});
            int isAutoClear = 1; // 自动扣费
            Timestamp time = new Timestamp(System.currentTimeMillis());
            JoyLogger.LogInfo(String.format("ReadServiceImp.readByMeterNo, send time: %s", time.toString()));
            this.sendDevParams(req, list, isAutoClear, operator);
            List<ReadInfo> readResult;
            while (true) {
                long dif = ((new Timestamp(System.currentTimeMillis())).getTime() - time.getTime()) / 1000;
                JoyLogger.LogInfo(String.format("ReadServiceImp.readByMeterNo, dif: %s", dif));
                if (dif > (20 * (list.size() - 1))) {
                    break;
                }
                this.wait(1000);
                List<ReadInfo> readInfo = readInfoCache.findReadResult(time);
                if (readInfo == null) {
                    continue;
                }
                readResult = this.getData(readInfo, list);
                if (readResult != null && readResult.size() > 0) {
                    res.addAll(readResult);
                    if (readResult.size() >= (list.size() - 1)) {
                        break;
                    }
                }
            }
            if (res.size() > 0) {
                result.setStatus(1);
                result.setData(res);
            }
        } catch (InterruptedException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setStatus(0);
            result.setData(e.toString());
        }
        return result;
    }

    /**
     * 处理返回结果的数据
     *
     * @param readInfo
     * @param meterNos
     * @param result
     * @return
     */
    private List<ReadInfo> getData(List<ReadInfo> readInfo, final ArrayList<String[]> meterNos) {
        if (readInfo == null || readInfo.isEmpty()) {
            return null;
        }
        List<ReadInfo> res = new ArrayList();
        for (String[] meterNo : meterNos) {
            ReadInfo ri = readInfo.stream()
                    .filter((p) -> p.getMeter_no().equals(meterNo[1])).findFirst()
                    .orElse(null);
            if (ri != null) {
                res.add(ri);
            }
        }
        return res.size() <= 0 ? null : res;
    }

    /**
     * 异步 根据表号抄表
     *
     * @param req
     * @param meterNos
     * @param operator
     * @return
     */
    @Override
    public Result readByMeterNo_Async(HttpServletRequest req, final String[] meterNos, final String operator) {
        Result result = new Result();
        try {
            if (meterNos == null || meterNos.length <= 0) {
                result.setData("输入的表号格式不正确，请重新输入");
                return result;
            }
            for (String meterNo : meterNos) {
                if (!RegexMatches.isMeterNo(meterNo)) {
                    result.setData("输入的表号格式不正确，请重新输入");
                    return result;
                }
            }
            ArrayList<String[]> list = new ArrayList<>();
            for (String meterNo : meterNos) {
                User user = userDao.findUserByMeterNo(meterNo);
                String[] params = {user.getConcentrator_name(), meterNo, user.getMeter_type(),
                    user.getProtocol_type()};
                list.add(params);
            }
            list.add(new String[]{"", "", "", ""});
            int isAutoClear = 1; // 自动扣费
            Timestamp time = new Timestamp(System.currentTimeMillis());
            this.sendDevParams(req, list, isAutoClear, operator);
            result.setStatus(1);
            result.setData(time);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 根据时间查询抄表记录
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Result findReadInfoByDate(String startTime, String endTime) {
        Result result = new Result();
        try {
            boolean startIsDate = RegexMatches.isDate(startTime);
            boolean endIsDate = RegexMatches.isDate(endTime);
            if (!(startIsDate && endIsDate)) {
                result.setData("输入的时间格式不正确，请重新输入");
                return result;
            }
            startTime += " 00:00:00";
            endTime += " 23:59:59";
            HashMap<String, Object> params = new HashMap<>();
            params.put("supplier_name", "");
            params.put("user_name", "");
            params.put("community", "");
            params.put("building", "");
            params.put("unit", "");
            params.put("operator_name", "");
            params.put("meter_no", "");
            params.put("meter_type", "");
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            List<ReadInfo> readInfo = userDao.findReadInfo(params);
            int totalNo = readInfo == null ? 0 : readInfo.size();
            params.clear();
            params.put("readInfo", readInfo);
            params.put("totalNo", totalNo);
            result.setStatus(1);
            result.setData(params);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 根据时间查询抄表记录
     *
     * @param startTime
     * @param endTime
     * @param startNo
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public Result findReadInfoByDatePage(String startTime, String endTime, int startNo, int pageSize) {
        Result result = new Result();
        try {
            boolean startIsDate = RegexMatches.isDate(startTime);
            boolean endIsDate = RegexMatches.isDate(endTime);
            if (!(startIsDate && endIsDate)) {
                result.setData("输入的时间格式不正确，请重新输入");
                return result;
            }
            startTime += " 00:00:00";
            endTime += " 23:59:59";
            HashMap<String, Object> params = new HashMap<>();
            params.put("supplier_name", "");
            params.put("user_name", "");
            params.put("community", "");
            params.put("building", "");
            params.put("unit", "");
            params.put("operator_name", "");
            params.put("meter_no", "");
            params.put("meter_type", "");
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            params.put("startNo", startNo);
            params.put("pageSize", pageSize);
            List<ReadInfo> readInfo = userDao.findReadInfo(params);
            int totalNo = readInfo == null ? 0 : readInfo.size();
            params.put("pageSize", 0);
            int total = userDao.findReadInfoTotal(params);
            int pages = total % pageSize == 0 ? total / pageSize : (total / pageSize + 1);
            params.clear();
            params.put("readInfo", readInfo);
            params.put("totalNo", totalNo);
            params.put("pages", pages);
            result.setStatus(1);
            result.setData(params);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 根据时间查询抄表记录
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Result findReadInfoByDateWithoutTotalNo(String startTime, String endTime) {
        Result result = new Result();
        try {
            boolean startIsDate = RegexMatches.isDate(startTime);
            boolean endIsDate = RegexMatches.isDate(endTime);
            if (!(startIsDate && endIsDate)) {
                result.setData("输入的时间格式不正确，请重新输入");
                return result;
            }
            startTime += " 00:00:00";
            endTime += " 23:59:59";
            HashMap<String, Object> params = new HashMap<>();
            params.put("supplier_name", "");
            params.put("user_name", "");
            params.put("community", "");
            params.put("building", "");
            params.put("unit", "");
            params.put("operator_name", "");
            params.put("meter_no", "");
            params.put("meter_type", "");
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            List<ReadInfo> readInfo = userDao.findReadInfo(params);
            result.setStatus(1);
            result.setData(readInfo);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 根据时间查询抄表记录 精确到秒
     *
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    @Override
    public Result findReadInfoByDateTime(String startDateTime, String endDateTime) {
        Result result = new Result();
        boolean startIsDateTime = RegexMatches.isDateTime(startDateTime);
        boolean endIsDateTime = RegexMatches.isDateTime(endDateTime);
        if (!(startIsDateTime && endIsDateTime)) {
            result.setData("输入的时间格式不正确，请重新输入");
            return result;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("supplier_name", "");
        params.put("user_name", "");
        params.put("community", "");
        params.put("building", "");
        params.put("unit", "");
        params.put("operator_name", "");
        params.put("meter_no", "");
        params.put("meter_type", "");
        params.put("startTime", startDateTime);
        params.put("endTime", endDateTime);
        try {
            List<ReadInfo> readInfo = userDao.findReadInfo(params);
            result.setStatus(1);
            result.setData(readInfo);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 根据时间查询某只表的抄表记录
     *
     * @param meterNo
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    @Override
    public Result findReadInfoByMeterNoAndDate(String meterNo, String startDateTime, String endDateTime) {
        Result result = new Result();
        boolean isMeterNo = RegexMatches.isMeterNo(meterNo);
        if (!isMeterNo) {
            result.setData("输入的表号格式不正确，请重新输入");
            return result;
        }
        boolean startIsDate = RegexMatches.isDate(startDateTime);
        boolean startIsDateTime = RegexMatches.isDateTime(startDateTime);
        boolean endIsDate = RegexMatches.isDate(endDateTime);
        boolean endIsDateTime = RegexMatches.isDateTime(endDateTime);
        if (!(startIsDate || startIsDateTime) || !(endIsDate || endIsDateTime)) {
            result.setData("输入的时间格式不正确，请重新输入");
            return result;
        }
        if (startIsDate) {
            startDateTime += " 00:00:00";
        }
        if (endIsDate) {
            endDateTime += " 23:59:59";
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("meter_no", meterNo);
        params.put("startTime", startDateTime);
        params.put("endTime", endDateTime);
        try {
            List<ReadInfo> readInfo = userDao.findReadInfo(params);
            result.setStatus(1);
            result.setData(readInfo);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 根据表号发送短信通知
     *
     * @param meterNo
     * @param operator
     * @return
     */
    @Override
    public Result sendSmsByMeterNo(String meterNo, final String operator) {
        Result result = new Result();
        if (!RegexMatches.isMeterNo(meterNo)) {
            result.setData("输入的表号格式不正确，请重新输入");
            return result;
        }
        try {
            User user = userDao.findUserByMeterNo(meterNo);
            Double balance = user.getLast_balance();
            Concentrator con = userDao.findConcentrator(user.getConcentrator_name());
            String ip = con.getConcentrator_ip() + ":" + con.getConcentrator_port();
            List<ReadParameter> readParameters = userDao.findReadParameter();
            if (readParameters == null) {
                return null;
            }
            ReadParameter readParameter = readParameters.get(0);
            Double balance_warn = readParameter.getBalance_warn();
            StringBuilder parameter = new StringBuilder("");
            if (balance_warn != null && balance <= balance_warn) {
                parameter.append("{number:'").append(user.getContact_info()).append("',msg:'您的帐户余额已不足")
                        .append(balance_warn).append("元，请及时缴费，谢谢！'}");
            } else {
                parameter.append("{number:'").append(user.getContact_info()).append("',msg:'您目前的帐户余额是").append(balance)
                        .append("元。'}");
            }
            HttpRequest.sendPost(HttpRequest.GetSmsUrl(ip), parameter.toString());
            result.setStatus(1);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setStatus(0);
        }
        return result;
    }

    /**
     *
     * @param req
     * @param meterNo
     * @param operator
     * @return
     */
    @Override
    public Result getMeterBalance(HttpServletRequest req, String meterNo, String operator) {
        Result result = new Result();
        final String json = "{'onlineSyncCode':'%s','meterNo':'%s','data':'%.2f',date:'%s'}";
        try {
            User user = userDao.findUserByMeterNo(meterNo);
            if (user == null) {
                return result;
            }
            String onlineSyncCode = user.getId_card_no();
            double balance = user.getLast_balance();
            String dataStr = String.format(json, onlineSyncCode, meterNo, balance, new java.util.Date().getTime());
            result.setStatus(1);
            result.setData(JSONObject.parse(dataStr));
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 查询电表状态(包含表最新读数，最新上报时间以及余额，该功能由复恒麦家公寓提出)
     *
     * @author mengshuai
     * @date 2018-02-21
     * @version v1.0.0
     *
     * @param meterNo:表号
     * @return
     */
    @Override
    public Result queryMeterStatusAndReading(String meterNo) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "查询电表状态，表号为" + meterNo, "");
        ReadInfo readInfo = readInfoCache.findLastReadInfoByMeterNo(meterNo);
        if (readInfo == null) {
            return null;
        }
        Double this_read = readInfo.getThis_read() == null ? 0d : readInfo.getThis_read();
        this_read = Math.round(this_read * 100) / 100d;
        Double balance = readInfo.getBalance() == null ? 0d : readInfo.getBalance();
        balance = Math.round(balance * 100) / 100d;
        Timestamp operate_time = readInfo.getOperate_time() == null ? new Timestamp(System.currentTimeMillis()) : readInfo.getOperate_time();
        String read_time = JoyDatetime.dateFormat(operate_time, JoyDatetime.FORMAT_DATETIME_2);
        Result result = getMeterStatus(meterNo);
        if (result.getStatus() == 0) {
            return result;
        }
        JSONObject jo = (JSONObject) result.getData();
        String net_state = jo.getString("net_state");
        String elec_state = jo.getString("elec_state");
        String meter_status = "{'net_state':'%s','elec_state':'%s','this_read':'%s','balance':'%s','read_time':'%s'}";
        result.setStatus(1);
        result.setData(JSONObject.parseObject(String.format(meter_status, net_state, elec_state, this_read, balance, read_time)));
        return result;
    }

    /**
     * 查询电表状态
     *
     * @param meterNo
     * @return
     */
    @Override
    public Result getMeterStatus(String meterNo) {
        Result result = new Result();
        String meter_status = "{'net_state':'%s','elec_state':'%s'}";
        try {
            User user = userDao.findUserByMeterNo(meterNo);
            if (user == null) {
                return result;
            }
            if (user.getProtocol_type().toLowerCase().startsWith("com.joymeter.dtu")) {
                Concentrator con = userDao.findConcentratorByMeterNo(meterNo);
                if (con == null) {
                    return result;
                }
                String mbus_ip = con.getConcentrator_ip();
                String mbus_port = con.getConcentrator_port();
                String url = "http://" + mbus_ip + ":" + mbus_port + "/arm/api/realTimeOnline";
                String jsonString = HttpRequest.sendPost(url, con.getConcentrator_no());
                JSONObject obj = JSONObject.parseObject(jsonString);
                String net_state = "0".equals(obj.getString("result")) ? "1" : "0";// 0 在线，1 离线
                String elec_state = user.getValve_status(); // 0 通电，1 断电
                result.setStatus(1);
                result.setData(JSONObject.parse(String.format(meter_status, net_state, elec_state)));
            }
            if (user.getProtocol_type().toLowerCase().startsWith("jlaa")) { // 无线
                result.setStatus(1);
                result.setData(
                        JSONObject.parse(String.format(meter_status, user.getMeter_status(), user.getValve_status())));
            }
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 获取天的电量
     *
     * @param meterNo
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Result getPowerCostPerDay(String meterNo, String startDate, String endDate) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("meter_no", meterNo);
        params.put("startTime", startDate + " 00:00:00");
        params.put("endTime", endDate + " 23:59:59");
        params.put("supplier_name", "");
        try {
            User user = userDao.findUserByMeterNo(meterNo);
            if (user == null) {
                return result;
            }
            List<ReadInfo> readInfo = userDao.findReadInfo(params);
            if (readInfo == null || readInfo.isEmpty()) {
                return result;
            }
            String onlineSyncCode = user.getId_card_no();
            String dataStr = "{'onlineSyncCode':'" + onlineSyncCode + "','meterNo':'" + meterNo + "','datas':";
            JSONArray array = new JSONArray();

            // 计算两个日期之间相差的天数
            java.util.Date sDate = JoyDatetime.strToDate(startDate, JoyDatetime.FORMAT_DATE_2);
            java.util.Date eDate = JoyDatetime.strToDate(endDate, JoyDatetime.FORMAT_DATE_2);
            int days = JoyDatetime.getDays(sDate, eDate);
            for (int i = 0; i <= days; i++) {
                java.util.Date date = JoyDatetime.addDay(sDate, i);
                Timestamp start = JoyDatetime.dateToTimestamp(JoyDatetime.getStartDate(date));
                Timestamp end = JoyDatetime.dateToTimestamp(JoyDatetime.getEndDate(date));
                Double data = readInfo.stream()
                        .filter(p -> (p.getOperate_time().getTime() >= start.getTime()
                        && p.getOperate_time().getTime() <= end.getTime()))
                        .mapToDouble(p -> p.getThis_cost() == null ? 0 : p.getThis_cost()).sum();
                ReadInfo ri = readInfo.stream()
                        .filter(p -> (p.getOperate_time().getTime() >= start.getTime()
                        && p.getOperate_time().getTime() <= end.getTime()))
                        .max((a, b) -> (a.getOperate_time().compareTo(b.getOperate_time()))).orElse(null);
                Double balance = ri == null ? 0 : ri.getBalance();
                array.add(JSONObject.parse("{'data':'" + String.format("%.2f", data) + "','date':'"
                        + JoyDatetime.dateFormat(date, JoyDatetime.FORMAT_DATE_2) + "','balance':'" + balance + "'}"));
            }
            dataStr += array.toJSONString() + "}";
            result.setStatus(1);
            result.setData(JSONObject.parse(dataStr));
        } catch (ParseException e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 实现设备在线离线事件的保存及设备状态的更新
     *
     * @param param
     * @return
     */
    @Override
    public Result saveEventInfo(final String param) {
        JSONObject jo = JSON.parseObject(param);
        String event = jo.getString("event").toLowerCase();
        JSONObject device = jo.getJSONObject("device");
        String meter_no = device.getString("id");
        if (meter_no == null) {
            return null;
        }
        String error_code = device.getString("error_code");
        String error_msg = device.getString("error_msg");
        String report_time = device.getString("datetime");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            java.util.Date parseDate = sdf.parse(report_time);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf1.format(parseDate);
            Timestamp timestamp = Timestamp.valueOf(format);
            Map<String, Object> map = new HashMap<>();
            map.put("meter_no", meter_no);
            map.put("meter_status", "1");
            if ("offline".equals(event.toLowerCase())) {
                map.put("meter_status", "0");
            }
            User user = userDao.findUserByMeterNo(meter_no);
            if (user == null) {
                return null;
            }
            if ("offline".equals(event.toLowerCase()) || "online".equals(event.toLowerCase())) {
                userDao.updateUserMeterStatus(map);
            }
            EventRecord er = new EventRecord();
            er.setMeter_no(meter_no);
            er.setEvent(event);
            er.setError_code(error_code);
            er.setError_msg(error_msg);
            er.setReport_time(timestamp);
            er.setProvince(user.getProvince());
            er.setCity(user.getCity());
            er.setDistrict(user.getDistrict());
            er.setAddress(user.getUser_address_area()
                    + user.getUser_address_community()
                    + user.getUser_address_building()
                    + user.getUser_address_unit()
                    + user.getUser_address_room());
            er.setOperator_id(device.getString("operator_id"));
            userDao.addEvent(er);
            eventRecordCache.add(er);
        } catch (ParseException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * 更新设备为在线状态
     *
     * @param user
     */
    private void updateDeviceState(User user) {
        if (user != null && !"1".equals(user.getMeter_status())) {
            Map<String, Object> map = new HashMap<>();
            map.put("meter_no", user.getMeter_no());
            map.put("meter_status", "1");
            userDao.updateUserMeterStatus(map);
        }
    }

    /**
     * 更新设备为在线状态
     *
     * @param user
     */
    private void updateValveState(User user, final String value) {
        if (!RegexMatches.isNumeric(value)) {
            return;
        }
        int state = Integer.valueOf(value);
        if (state < 0) {
            return;
        }
        if (user != null && !String.valueOf(state).equals(user.getValve_status())) {
            Map<String, Object> map = new HashMap<>();
            map.put("valve_no", user.getValve_no());
            map.put("status_no", state);
            userDao.updateValveStatus(map);
        }

    }

    /**
     *
     * @param parameters
     * @return
     */
    public String toEventJson(final String parameters) {
        JSONObject jo = JSON.parseObject(parameters);
        JSONObject options = jo.getJSONObject("options");
        String protocol = options.getString("protocol");
        String meter_no = jo.getString("meter");
        String accountName = options.getString("accountName");
        String event = "read";
        String error_code = "0000";
        String error_msg = "read meter";
        if (protocol != null && !protocol.isEmpty()) {
            if (protocol.endsWith("_Close")) {
                event = "close";
                error_code = "3000";
                error_msg = "vavle close";
            }
            if (protocol.endsWith("_Open")) {
                event = "open";
                error_code = "3001";
                error_msg = "vavle open";
            }
        }
        return String.format(SaaSType.JSON_FORMAT_SAAS_EVENT, event, meter_no, error_code, error_msg,
                JoyDatetime.dateFormat(JoyDatetime.FORMAT_DATETIME_3), accountName, "");
    }

    /**
     *
     * @param req
     * @param uuid
     * @return
     */
    @Override
    public String readByUUID(HttpServletRequest req, String uuid) {
        final String resultMsg = "{\"code\":\"%s\", \"message\":\"%s\",\"reqId\":\"%s\"}";
        try {
            User user = userDao.getMeterByUUID(uuid);
            if (user == null) {
                return String.format(resultMsg, "1", "User does not exist", String.valueOf(JoyDatetime.getID()));
            }
            String[] params = {user.getMeter_no()};
            Result res = this.readByMeterNo(req, params, "system");
            if (res.getStatus() == 1) {
                return String.format(resultMsg, "0", "success", String.valueOf(JoyDatetime.getID()));
            }
            return String.format(resultMsg, "1", "fail", String.valueOf(JoyDatetime.getID()));
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return resultMsg;
    }

    /**
     * 根据表号查询该表某段时间内的抄表记录(带分页)
     *
     * @param meter_no
     * @param start_time
     * @param end_time
     * @param startNo
     * @param pageSize
     * @return
     */
    @Override
    public Result queryHistoryRecordByMeterNo(String meter_no, String start_time, String end_time, int startNo, int pageSize) {
        Result result = new Result();
        Logger.getLogger(getClass().getName()).log(Level.INFO, "查询某段时间内的抄表记录(带分页)表号为:" + meter_no, "");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("meter_no", meter_no);
            map.put("startTime", start_time);
            map.put("endTime", end_time);
            map.put("startNo", startNo);
            map.put("pageSize", pageSize);
            if (meter_no == null || "".equals(meter_no)) {
                return result;
            }
            List<ReadInfo> readInfos = userDao.findReadInfo(map);
            int total = userDao.findReadInfoTotal(map);
            List<Map<String, Object>> collect = readInfos.parallelStream().map(p -> {
                Map<String, Object> params = new HashMap<>();
                params.put("this_read",Math.round(p.getThis_read() * 100) / 100d);
                params.put("read_time", JoyDatetime.dateFormat(p.getOperate_time(), JoyDatetime.FORMAT_DATETIME_2));
                return params;
            }).collect(toList());
            map.clear();
            map.put("total", total);
            map.put("items", collect);
            result.setStatus(1);
            result.setData(map);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            result.setStatus(0);
        }
        return result;
    }
}
