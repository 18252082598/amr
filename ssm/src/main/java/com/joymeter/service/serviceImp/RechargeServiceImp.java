package com.joymeter.service.serviceImp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.joymeter.cache.PayOrRefundCache;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.Admin;
import com.joymeter.entity.Concentrator;
import com.joymeter.entity.ParentMeterConf;
import com.joymeter.entity.OperateInfo;
import com.joymeter.entity.Result;
import com.joymeter.entity.User;
import com.joymeter.service.RechargeService;
import com.joymeter.task.JoyServletContextListener;
import com.joymeter.util.HttpRequest;
import com.joymeter.util.JoyDatetime;
import com.joymeter.util.JoyLogger;
import com.joymeter.util.ParameterUtil;
import com.joymeter.util.PropertiesUtil;
import com.joymeter.util.RSACoder;
import com.joymeter.util.RegexMatches;
import com.joymeter.util.WechatApiUtil;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Wu Wei
 */
@Service
public class RechargeServiceImp implements RechargeService {

    @Resource
    private UserDao userDao;
    @Resource
    private PayOrRefundCache payOrRefundCache;

    /**
     * 加载所有操作员
     *
     * @return
     */
    @Override
    public Result loadAllOperators() {
        Result result = new Result();
        try {
            List<Admin> admins = userDao.loadAllOperators();
            result.setStatus(1);
            result.setData(admins);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 查询符合条件的全部用户
     *
     * @param request
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
     * @return
     */
    @Override
    public Result findUser(String user_name, String user_address_community, String user_address_building,
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
            List<User> users = userDao.findUser(params);
            int totalNo = userDao.findTotalUser(params);
            params.clear();
            params.put("users", users);
            params.put("totalNo", totalNo);
            if (pageSize > 0 && totalNo > 0) {
                int pages = totalNo % pageSize == 0 ? totalNo / pageSize : (totalNo / pageSize + 1);
                params.put("pages", pages);
            }
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
     * 查询符合条件的母表参数配置
     *
     * @param meter_no
     * @param startNo
     * @param pageSize
     * @return
     */
    @Override
    public Result findMotherMeterConf(String meter_no, int startNo, int pageSize) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("meter_no", meter_no);
        params.put("startNo", startNo);
        params.put("pageSize", pageSize);
        try {
            List<ParentMeterConf> motherMeterConfs = userDao.findMotherMeterConf(params);
            int totalNo = userDao.findTotalMotherMeterConf(params);
            params.clear();
            params.put("motherMeterConfs", motherMeterConfs);
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
     * 充值,退费
     *
     * @param operateInfo
     * @param method
     * @return
     */
    @Override
    public Result payOrRefund(OperateInfo operateInfo, String method) {
        Result result = payOrRefund2(operateInfo, method);
        if (PropertiesUtil.readConfig("cjoyId", "wechat.properties") != null) {
            if (result.getStatus() == 1) {
                Double balance = operateInfo.getBalance();
                String style = "OFFPAY";
                Map<String, String> params = new HashMap<>();
                params.put("operateId", operateInfo.getOperate_id());
                params.put("meterNo", operateInfo.getMeter_no());
                params.put("access_token", RSACoder.getAccess_token());
                params.put("recharge_money", String.valueOf(operateInfo.getRecharge_money()));
                params.put("cjoyId", PropertiesUtil.readConfig("cjoyId", "wechat.properties"));
                params.put("style", style);
                params.put("method", method);
                params.put("time", new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(operateInfo.getOperate_time()));
                params.put("balance", String.valueOf(balance));// 操作后的余额，总的余额
                String msg = WechatApiUtil.payOrRefund(params);
                try {
                    if (!"1000".equals(JSONObject.parseObject(msg).get("code"))) {
                        //System.out.println("接口调用未成功，将信息放入缓存：");
                        payOrRefundCache.putCache(operateInfo.getOperate_id(), params);
                    }
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                    payOrRefundCache.putCache(operateInfo.getOperate_id(), params);
                }
            }
        }
        return result;
    }

    /**
     *
     * @param operateInfo
     * @param method
     * @return
     */
    @Override
    public Result payOrRefund2(OperateInfo operateInfo, String method) {
        Result result = new Result();
        Object object = new Object();
        String meter_no = operateInfo.getMeter_no();
        if (meter_no == null || operateInfo.getRecharge_money() == null || operateInfo.getRecharge_money() == 0) {
            result.setStatus(0);
            return result;
        }
        synchronized (object) {
            User user = userDao.findUserByMeterNo(meter_no);
            if (user == null) {
                result.setStatus(0);
                return result;
            }
            Double last_balance = user.getLast_balance() == null ? 0 : user.getLast_balance();
            Double balance = last_balance - operateInfo.getRecharge_money();
            balance = Math.round(balance * 100) / 100d;
            operateInfo.setBalance(balance);
            try {
                switch (method) {
                    case "pay":
                        balance = last_balance + operateInfo.getRecharge_money();
                        balance = Math.round(balance * 100) / 100d;
                        operateInfo.setBalance(balance);
                        userDao.pay(operateInfo);
                        userDao.update(operateInfo);
                        result.setStatus(1);
                        break;
                    case "refund":
                        userDao.refund(operateInfo);
                        userDao.update(operateInfo);
                        result.setStatus(1);
                        break;
                    case "autoPay":
                        Map<String, Object> map = new HashMap<>();
                        Timestamp last_read_time = new Timestamp(System.currentTimeMillis());
                        map.put("user_id", user.getUser_id());
                        map.put("this_balance", balance);
                        map.put("last_read_time", last_read_time);
                        userDao.refund(operateInfo);
                        userDao.updateBalance(map);
                        result.setStatus(1);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                result.setData(e.getMessage());
                result.setStatus(0);
                SQLException sqle = (SQLException) e.getCause();
                int sqlErrorCode = sqle.getErrorCode();
                if (sqlErrorCode == 1062) {
                    result.setStatus(sqlErrorCode);
                    result.setData(sqle.getMessage());
                }
            }
        }
        return result;
    }

    /**
     * 加载某个用户的全部充值信息
     *
     * @param user_name
     * @param startNo
     * @param pageSize
     * @return
     */
    @Override
    public Result loadOperateInfo(String user_name, int startNo, int pageSize) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("user_name", user_name);
        params.put("startNo", startNo);
        params.put("pageSize", pageSize);
        try {
            List<OperateInfo> information = userDao.loadOperateInfo(params);
            int totalNo = userDao.findTotalOperateInfoNo(user_name);
            params.clear();
            params.put("information", information);
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
     * 加载某个用户的全部充值信息 根据表号
     *
     * @param meter_no
     * @param startNo
     * @param pageSize
     * @return
     */
    @Override
    public Result loadOperateInfoByMeterNo(String meter_no, int startNo, int pageSize) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("meter_no", meter_no);
        params.put("startNo", startNo);
        params.put("pageSize", pageSize);
        try {
            List<OperateInfo> information = userDao.loadOperateInfoByMeterNo(params);
            int totalNo = userDao.findTotalOperateInfoNoByMeterNo(meter_no);
            params.clear();
            params.put("information", information);
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
     * 退款
     *
     * @param operateInfo
     * @return
     */
    @Override
    public Result refund(OperateInfo operateInfo) {
        Result result = new Result();
        try {
            userDao.refund(operateInfo);
            userDao.update(operateInfo);
            result.setStatus(1);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 查询符合条件的全部操作信息
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
    public Result findOperateInfo(String userName, String community, String building, String unit, String room,
            String operatorName, String meterNo, int startNo, int pageSize, HttpServletRequest request) {
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
        params.put("startNo", startNo);
        params.put("pageSize", pageSize);
        try {
            List<OperateInfo> rechargeInfo = userDao.findOperateInfo(params);
            int totalNo = userDao.findAllOperateInfoNo(params);
            params.clear();
            params.put("rechargeInfo", rechargeInfo);
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
     * 根据表号充值
     *
     * @param operateId
     * @param meterNo
     * @param rechargeMoney
     * @param operator
     * @return
     */
    @Override
    public Result rechargeByMeterNo(final String operateId, String meterNo, Double rechargeMoney,
            final String operator) {
        Result result = new Result();
        try {
            if (!RegexMatches.isMeterNo(meterNo)) {
                result.setData("输入的表号格式不正确，请重新输入");
                return result;
            }
            if (rechargeMoney == null || rechargeMoney.isNaN() || rechargeMoney <= 0) {
                result.setData("输入的充值金额不正确，请重新输入");
                return result;
            }
            User user = userDao.findUserByMeterNo(meterNo);
            double balance = user.getLast_balance() + rechargeMoney;
            balance = Math.round(balance * 100) / 100d;
            OperateInfo operateInfo = new OperateInfo();
            operateInfo.setOperate_id(operateId);
            operateInfo.setUser_name(user.getUser_name());
            operateInfo.setProvince(user.getProvince());
            operateInfo.setCity(user.getCity());
            operateInfo.setDistrict(user.getDistrict());
            operateInfo.setUser_address_area(user.getUser_address_area());
            operateInfo.setUser_address_community(user.getUser_address_community());
            operateInfo.setUser_address_building(user.getUser_address_building());
            operateInfo.setUser_address_unit(user.getUser_address_unit());
            operateInfo.setUser_address_room(user.getUser_address_room());
            operateInfo.setContact_info(user.getContact_info());
            operateInfo.setConcentrator_name(user.getConcentrator_name());
            operateInfo.setMeter_model(user.getMeter_model());
            operateInfo.setMeter_type(user.getMeter_type());
            operateInfo.setMeter_no(meterNo);
            operateInfo.setFee_type(user.getFee_type());
            operateInfo.setRecharge_money(rechargeMoney);
            operateInfo.setOperate_type("1");// "充值"
            operateInfo.setBalance(balance);
            operateInfo.setOperator_account(operator);
            operateInfo.setRecharge_loc(user.getSupplier_name());
            operateInfo.setPay_method("现金");
            operateInfo.setIsPrint("0");// "未打印"
            result = this.payOrRefund(operateInfo, "pay");
            String msg = "{meterNo:'" + meterNo + "',money:'" + rechargeMoney + "',balance:'" + balance + "'}";
            if (result.getStatus() != 1) {
                msg = "{meterNo:'" + meterNo + "',money:'" + rechargeMoney + "',balance:'" + user.getLast_balance()
                        + "'}";
                result.setData(JSONObject.parse(msg));
                return result;
            }
            result.setStatus(1);
            result.setData(JSONObject.parse(msg));
            if (user.getLast_balance() > 0) {
                this.sendOpenValve(user, operator);
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 打开阀门
     *
     * @param user
     * @param operator_account
     */
    public void sendOpenValve(User user, final String operator_account) {
        try {
            JoyLogger.LogInfo(String.format("sendOpenValve this_balance: %s, operator_account: %s",
                    user.getLast_balance(), operator_account));
            // 账户余额小于等于阀门关闭值，关闭阀门
            if (user.getValve_protocol() == null || user.getValve_protocol().isEmpty()) {
                return;
            }
            String concentrator_name = user.getConcentrator_name();
            String valve_no = user.getValve_no();
            Concentrator con = userDao.findConcentrator(concentrator_name);
            String id = con.getConcentrator_no();
            String ip = con.getConcentrator_ip() + ":" + con.getConcentrator_port();
            String protocol_type = user.getValve_protocol().replaceAll("\\s", "") + "_Open";
            String sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
            StringBuilder parameter = new StringBuilder("");
            parameter.append("{DTU:[{id:'").append(id).append("',url:'").append(JoyServletContextListener.GetUrl())
                    .append("',options:{concentrator_model:'2', isAutoClear:'").append(10).append("',protocol:'")
                    .append(protocol_type).append("',accountName:'").append(operator_account).append("',sendTime:'")
                    .append(sendTime).append("'},meters:[{meter:'").append(valve_no).append("',category:'")
                    .append(user.getMeter_type()).append("',protocol:'").append(protocol_type).append("'}]}]}");
            HttpRequest.sendPost(HttpRequest.GetUrl(ip), parameter.toString());
            ArrayList<String[]> meterList = new ArrayList<>();
            String[] arr = {id, valve_no, user.getMeter_type(), protocol_type, ip, "2", "sendUserParameters"};
            meterList.add(arr);
            JoyServletContextListener.instanceSaaS(userDao).down(ParameterUtil.GetParameter(meterList));
        } catch (NumberFormatException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * 根据表号充值
     *
     * @param meterNo
     * @param rechargeMoney
     * @param operator
     * @return
     */
    @Override
    public Result rechargeByMeterNo(String meterNo, Double rechargeMoney, final String operator) {
        final String operateId = "CZ" + String.valueOf(JoyDatetime.getTokenID());
        return this.rechargeByMeterNo(operateId, meterNo, rechargeMoney, operator);
    }

    /**
     * 根据表号退费
     *
     * @param meterNo
     * @param refundMoney
     * @param operator
     * @return
     */
    @Override
    public Result refundByMeterNo(String meterNo, Double refundMoney, final String operator) {
        Result result = new Result();
        if (!RegexMatches.isMeterNo(meterNo)) {
            result.setData("输入的表号格式不正确，请重新输入");
            return result;
        }
        if (!RegexMatches.isNumeric(Math.abs(refundMoney))) {
            result.setData("输入的退费金额不正确，请重新输入");
            return result;
        }
        String operateId = "TF" + String.valueOf(JoyDatetime.getTokenID());
        try {
            User user = userDao.findUserByMeterNo(meterNo);
            if (user == null) {
                result.setData("输入的表号格式不存在，请重新输入");
                return result;
            }
            double lastBalance = user.getLast_balance();
            if (refundMoney > lastBalance) {
                result.setData("当前账户余额为" + lastBalance + "，余额不足，请重新输入！");
                return result;
            }
            double balance = lastBalance - refundMoney;
            balance = Math.round(balance * 100) / 100d;
            OperateInfo operateInfo = new OperateInfo();
            operateInfo.setOperate_id(operateId);
            operateInfo.setUser_name(user.getUser_name());
            operateInfo.setProvince(user.getProvince());
            operateInfo.setCity(user.getCity());
            operateInfo.setDistrict(user.getDistrict());
            operateInfo.setUser_address_area(user.getUser_address_area());
            operateInfo.setUser_address_community(user.getUser_address_community());
            operateInfo.setUser_address_building(user.getUser_address_building());
            operateInfo.setUser_address_unit(user.getUser_address_unit());
            operateInfo.setUser_address_room(user.getUser_address_room());
            operateInfo.setContact_info(user.getContact_info());
            operateInfo.setConcentrator_name(user.getConcentrator_name());
            operateInfo.setMeter_model(user.getMeter_model());
            operateInfo.setMeter_type(user.getMeter_type());
            operateInfo.setMeter_no(meterNo);
            operateInfo.setFee_type(user.getFee_type());
            operateInfo.setRecharge_money(refundMoney);
            operateInfo.setOperate_type("0");// "退费"
            operateInfo.setBalance(balance);
            operateInfo.setOperator_account(operator);
            operateInfo.setRecharge_loc(user.getSupplier_name());
            operateInfo.setPay_method("现金");
            operateInfo.setIsPrint("0");// "未打印"
            result = this.payOrRefund(operateInfo, "refund");
            if (result.getStatus() != 1) {
                return result;
            }
            final String msg = "{meterNo:'" + meterNo + "',money:'" + refundMoney + "',balance:'" + balance + "'}";
            result.setStatus(1);
            result.setData(JSONObject.parse(msg));
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 根据表号退费
     *
     * @param meterNo
     * @param operator
     * @return
     */
    @Override
    public Result refundByMeterNo(String meterNo, final String operator) {
        Result result = new Result();
        if (!RegexMatches.isMeterNo(meterNo)) {
            result.setData("输入的表号格式不正确，请重新输入");
            return result;
        }
        try {
            User user = userDao.findUserByMeterNo(meterNo);
            if (user == null) {
                result.setData("输入的表号格式不存在，请重新输入");
                return result;
            }
            Double balance = user.getLast_balance();
            if (balance == 0) {
                result.setStatus(1);
                return result;
            }
            return refundByMeterNo(meterNo, balance, operator);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setData(e.getMessage());
            result.setStatus(0);
        }
        return result;
    }

    /**
     * 获取充值记录
     *
     * @param meterNo
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Result getRechargeInfo(String meterNo, String startTime, String endTime) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("meterNo", meterNo);
        params.put("startTime", startTime + " 00:00:00");
        params.put("endTime", endTime + " 23:59:59");
        try {
            User user = userDao.findUserByMeterNo(meterNo);
            if (user == null) {
                return result;
            }
            List<OperateInfo> operateInfo = userDao.findRechargeInfo(params);
            String onlineSyncCode = user.getId_card_no();
            String dataStr = "{'onlineSyncCode':'" + onlineSyncCode + "','meterNo':'" + meterNo + "','datas':";
            JSONArray array = new JSONArray();
            operateInfo.stream().forEach(info -> {
                double data = info.getRecharge_money();
                Timestamp time = info.getOperate_time();
                array.add(JSONObject.parse("{'data':'" + data + "','date':'" + time + "'}"));
            });
            dataStr += array.toJSONString() + "}";
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
     * 根据房源id查询该房号充值信息
     *
     * @author mengshuai
     * @date 2018-03-05
     * @version v1.0.0
     * @modify 2018-03-15(统一采用jlaa包中的时间格式类)
     *
     * @param onlineSynvCode(房源id)
     * @param operate_type(充值记录)
     * @param startNo(开始查询索引)
     * @param pageSize(每页数目)
     * @return
     */
    @Override
    public Result queryRechargeInfoByRoomId(String onlineSynvCode, String operate_type, int startNo, int pageSize) {
        Result result = new Result();
        try {
            String meter_no = userDao.findMeterNoByOnline_synv_code(onlineSynvCode);
            if (meter_no == null) {
                return null;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("meter_no", meter_no);
            map.put("operate_type", operate_type);
            map.put("startNo", startNo);
            map.put("pageSize", pageSize);
            int total = userDao.findTotalOperateInfoByMeterNoAndOperate_type(map);
            List<OperateInfo> operateInfoList = userDao.loadOperateInfoByMeterNoAndOperate_type(map);
            List<Object> rechargeInfoList = new ArrayList<>();
            final int status = 1;
            operateInfoList.forEach((OperateInfo p) -> {
                Timestamp time = p.getOperate_time();
                time = time == null ? new Timestamp(System.currentTimeMillis()) : time;
                String operate_time = JoyDatetime.dateFormat(time, JoyDatetime.FORMAT_DATETIME_2);
                Map<String, Object> rechargeInfo = new HashMap<>();
                rechargeInfo.put("power", p.getRecharge_money());
                rechargeInfo.put("operate_time", operate_time);
                rechargeInfo.put("status", status);
                rechargeInfoList.add(rechargeInfo);
            });
            map.clear();
            map.put("total", total);
            map.put("items", rechargeInfoList);
            result.setStatus(1);
            result.setData(map);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setStatus(0);
            result.setData("系统异常，请稍后再试");
        }
        return result;
    }

 /**
     * 加载操作类型
     */
    @Override
    public Result loadOperationMethod() {
        Result result = new Result();
        result.setStatus(1);
        result.setData(userDao.loadOperationMethod());
        return result;
    }
    /**
     * 查询左权的操作信息
     */
    @Override
    public Result zuoQuanFindOperateInfo(String user_name, String community, String building, String unit, String room,
            String operator_name, String meter_no, int startNo, int pageSize, String payMethod, String operationMethod,
            String startTime, String endTime, HttpServletRequest request) {
        Result result = new Result();
        String supplier_name = (String) request.getSession().getAttribute("supplier_name");
        HashMap<String, Object> params = new HashMap<>();
        params.put("supplier_name", supplier_name);
        params.put("user_name", user_name);
        params.put("community", community);
        params.put("building", building);
        params.put("unit", unit);
        params.put("room", room);
        params.put("operator_name", operator_name);
        params.put("meter_no", meter_no);
        params.put("startNo", startNo);
        params.put("pageSize", pageSize);
        params.put("pay_method", payMethod);
        params.put("operate_type",operationMethod );
        params.put("startTime",startTime );
        params.put("endTime",endTime );
        try {
            List<OperateInfo> rechargeInfo = userDao.findOperateInfo(params);
            int totalNo = userDao.findAllOperateInfoNo(params);
            params.clear();
            params.put("rechargeInfo", rechargeInfo);
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

}
