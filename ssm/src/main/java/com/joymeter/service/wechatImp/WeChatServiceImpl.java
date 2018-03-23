package com.joymeter.service.wechatImp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joymeter.cache.PayOrRefundCache;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.OperateInfo;
import com.joymeter.entity.ReadInfo;
import com.joymeter.entity.Result;
import com.joymeter.entity.User;
import com.joymeter.service.ReadService;
import com.joymeter.service.RechargeService;
import com.joymeter.service.WeChatService;
import com.joymeter.util.JoyDatetime;
import com.joymeter.util.RSACoder;
import com.joymeter.util.RegexMatches;
import com.joymeter.util.WechatApiUtil;

/**
 *
 * @author lijian
 * @version 1.0.0
 */
@Service
public class WeChatServiceImpl implements WeChatService {

    @Resource
    private UserDao userDao;
    @Resource
    private ReadService readService;
    @Resource
    private RechargeService rechargeService;
    @Resource
    private PayOrRefundCache payOrRefundCache;

    /**
     * 根据账号获取能耗
     *
     * @param id_card_no
     * @param startDate
     * @param endDate
     * @param key
     * @return
     */
    @Override
    public Result getCostPowerByIdCardNo(String id_card_no, String startDate, String endDate, String key) {
        Result result = new Result();
        List<Object> list = new ArrayList<>();
        List<User> users = userDao.findUserByIdCardNo(id_card_no);
        if (users == null) {
            return result;
        }
        if ("week".equals(key.toLowerCase())) {
            for (User user : users) {
                list.add(this.queryCostPowerPerWeekByMeterNo(user.getMeter_no(), startDate, endDate).getData());
            }
        }
        if ("month".equals(key.toLowerCase())) {
            for (User user : users) {
                list.add(this.queryCostPowerPerMonthByMeterNo(user.getMeter_no(), startDate, endDate).getData());
            }
        }
        if ("day".equals(key.toLowerCase())) {
            for (User user : users) {
                list.add(this.queryCostPowerPerDayByMeterNo(user.getMeter_no(), startDate, endDate).getData());
            }
        }
        result.setStatus(1);
        result.setData(list);
        return result;
    }

    /**
     * 根据表号查询表的状态
     *
     * @param meter_no
     * @return {"status":1,"data":{"meterNo":"57001240","valueStatus":"0"}}
     */
    @Override
    public Result getValueStatusByMeterNo(String meter_no) {
        Result result = new Result();
        User user = userDao.findUserByMeterNo(meter_no);
        if (user == null) {
            result.setStatus(0);
            result.setData("查询表状态失败,请重新输入!");
            return result;
        }
        JSONObject json = new JSONObject();
        json.put("meterNo", user.getMeter_no());
        json.put("valueStatus", user.getValve_status());
        result.setStatus(1);
        result.setData(json);
        return result;
    }

    /**
     * 根据账号查询该账号下所有表的阀门状态 0-关阀 1-开阀
     *
     * @param id_card_no
     * @return
     */
    @Override
    public Result getValueStatuesByIdCardNo(String id_card_no) {
        Result result = new Result();
        List<User> users = userDao.findUserByIdCardNo(id_card_no);
        if (users.isEmpty()) {
            result.setStatus(0);
            result.setData("查询表状态失败,请重新输入!");
            return result;
        }
        List<Object> list = new ArrayList<>();
        users.stream().map((user) -> {
            JSONObject json = new JSONObject();
            json.put("meterNo", user.getMeter_no());
            json.put("valueStatus", user.getValve_status());
            return json;
        }).forEachOrdered((json) -> {
            list.add(json);
        });
        result.setStatus(1);
        result.setData(list);
        return result;
    }

    /**
     * 根据表号充值
     *
     * @param operateId
     * @param meterNo
     * @param time
     * @param rechargeMoney
     * @param style
     * @return
     */
    @Override
    public Result rechargeByMeterNo(final String operateId, String meterNo, String time, Double rechargeMoney,
            String style) {
        Result result = new Result();
        OperateInfo operate_info = userDao.findOperateInfoByOperateId(operateId);
        if (operate_info == null) {
            if (!RegexMatches.isMeterNo(meterNo)) {
                result.setStatus(0);
                result.setData("输入的表号格式不正确，请重新输入");
                return result;
            }
            if (rechargeMoney == null || rechargeMoney.isNaN() || rechargeMoney <= 0) {
                result.setStatus(0);
                result.setData("输入的充值金额不正确，请重新输入");
                return result;
            }
            try {
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
                operateInfo.setOperator_account(user.getOperator_account());
                operateInfo.setRecharge_loc(user.getSupplier_name());
                operateInfo.setPay_method(style);
                operateInfo.setIsPrint("0");// "未打印"
                operateInfo.setOperate_time(Timestamp.valueOf(time));
                result = rechargeService.payOrRefund2(operateInfo, "pay");
                // String name = "充值表号:"+operateInfo.getMeter_no();
                // String release =time;
                // String feature = "充值金额:"+operateInfo.getRecharge_money();
                // new MessageUtil(name, release, feature);
                String msg = "{meterNo:'" + meterNo + "',money:'" + rechargeMoney + "',balance:'" + balance + "'}";
                if (result.getStatus() != 1) {
                    msg = "{meterNo:'" + meterNo + "',money:'" + rechargeMoney + "',balance:'" + user.getLast_balance()
                            + "'}";
                    result.setData(JSONObject.parse(msg));
                    return result;
                }
                result.setStatus(1);
                result.setData(JSONObject.parse(msg));
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                result.setData(e.getMessage());
                result.setStatus(0);
            }
            return result;
        } else {
            result.setStatus(0);
            result.setData("充值编码已存在,请勿重复充值.");
            return result;
        }

    }

    /**
     * 根据表号查询每天的能耗
     *
     * @param meterNo
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Result queryCostPowerPerDayByMeterNo(String meterNo, String startDate, String endDate) {
        Result result = new Result();
        User user = userDao.findUserByMeterNo(meterNo);
        if (user == null) {
            return result;
        }
        Result ress = readService.getPowerCostPerDay(user.getMeter_no(), startDate, endDate);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("datas", JSONObject.parseObject(ress.getData().toString()).get("datas"));
        jsonObject.put("meterNo", user.getMeter_no());
        jsonObject.put("meterType", user.getMeter_type());
        result.setStatus(1);
        result.setData(jsonObject);
        return result;
    }

    /**
     * 根据表号时间可以查询每周的能耗
     */
    /**
     *
     * @param meterNo
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Result queryCostPowerPerWeekByMeterNo(String meterNo, String startDate, String endDate) {
        String startdate = "";
        String enddate = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
            Calendar cal1 = Calendar.getInstance();
            java.util.Date time = sdf.parse(startDate);
            cal1.setTime(time);
            int dayWeek = cal1.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
            if (1 == dayWeek) {
                cal1.add(Calendar.DAY_OF_MONTH, -1);
            }
            cal1.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
            int day = cal1.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
            cal1.add(Calendar.DATE, cal1.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
            startdate = sdf.format(cal1.getTime());
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(sdf.parse(endDate));
            int dada = cal2.get(Calendar.DAY_OF_WEEK);
            if (1 == dada) {
                cal2.add(Calendar.DAY_OF_MONTH, -1);
            }
            cal2.setFirstDayOfWeek(Calendar.MONDAY);
            int da = cal2.get(Calendar.DAY_OF_WEEK);
            cal2.add(Calendar.DATE, cal2.getFirstDayOfWeek() - da);
            cal2.add(Calendar.DATE, 6);
            enddate = sdf.format(cal2.getTime());
        } catch (ParseException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        Result result = new Result();
        User user = userDao.findUserByMeterNo(meterNo);
        if (user == null) {
            return result;
        }
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("meter_no", user.getMeter_no());
            params.put("startTime", startdate + " 00:00:00");
            params.put("endTime", enddate + " 23:59:59");
            params.put("supplier_name", "");
            List<ReadInfo> readInfo = userDao.findReadInfo(params);
            if (readInfo == null || readInfo.isEmpty()) {
                return result;
            }
            String dataStr = "{'meterNo':'" + user.getMeter_no() + "','meterType':'" + user.getMeter_type()
                    + "','datas':";
            JSONArray array = new JSONArray();
            // 计算两个日期之间相差的天数
            java.util.Date sDate = JoyDatetime.strToDate(startdate, JoyDatetime.FORMAT_DATE_2);
            java.util.Date eDate = JoyDatetime.strToDate(enddate, JoyDatetime.FORMAT_DATE_2);
            int days = JoyDatetime.getDays(sDate, eDate);
            Calendar c = Calendar.getInstance();
            for (int i = 0; i <= days; i = i + 7) {
                java.util.Date date = JoyDatetime.addDay(sDate, i);
                Timestamp start = JoyDatetime.dateToTimestamp(JoyDatetime.getStartDate(date));
                c.setTime(date);
                c.add(Calendar.DAY_OF_MONTH, 6);
                Timestamp end = JoyDatetime.dateToTimestamp(JoyDatetime.getEndDate(c.getTime()));
                Double data = readInfo.stream()
                        .filter(p -> (p.getOperate_time().getTime() >= start.getTime()
                                && p.getOperate_time().getTime() <= end.getTime()))
                        .mapToDouble(p -> p.getThis_cost() == null ? 0 : p.getThis_cost()).sum();
                array.add(JSONObject.parse("{'data':'" + String.format("%.2f", data) + "','date':'"
                        + JoyDatetime.dateFormat(date, JoyDatetime.FORMAT_DATE_2) + "'}"));
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
     *
     * @param meterNo
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Result queryCostPowerPerMonthByMeterNo(String meterNo, String startDate, String endDate) {
        Result result = new Result();
        try {
            User user = userDao.findUserByMeterNo(meterNo);
            if (user == null) {
                return result;
            }
            Map<String, Object> params = new HashMap<>();
            params.put("meter_no", user.getMeter_no());
            params.put("startTime", startDate + " 00:00:00");
            params.put("endTime", endDate + " 23:59:59");
            params.put("supplier_name", "");
            List<ReadInfo> readInfo = userDao.findReadInfo(params);
            if (readInfo == null || readInfo.isEmpty()) {
                return result;
            }
            String dataStr = "{'meterNo':'" + user.getMeter_no() + "','meterType':'" + user.getMeter_type()
                    + "','datas':";
            JSONArray array = new JSONArray();
            // 计算两个日期之间相差的月数
            java.util.Date sDate = JoyDatetime.strToDate(startDate, JoyDatetime.FORMAT_DATE_2);
            java.util.Date eDate = JoyDatetime.strToDate(endDate, JoyDatetime.FORMAT_DATE_2);
            int months = JoyDatetime.getMonths(sDate, eDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(startDate));
            for (int i = 0; i <= months; i++) {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                java.util.Date date = calendar.getTime();
                Timestamp start = JoyDatetime.dateToTimestamp(JoyDatetime.getStartDate(calendar.getTime()));
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                Timestamp end = JoyDatetime.dateToTimestamp(JoyDatetime.getEndDate(calendar.getTime()));
                calendar.add(Calendar.MONTH, 1);
                Double data = readInfo.stream()
                        .filter(p -> (p.getOperate_time().getTime() >= start.getTime()
                                && p.getOperate_time().getTime() <= end.getTime()))
                        .mapToDouble(p -> p.getThis_cost() == null ? 0 : p.getThis_cost()).sum();
                array.add(JSONObject.parse("{'data':'" + String.format("%.2f", data) + "','date':'"
                        + JoyDatetime.dateFormat(date, JoyDatetime.FORMAT_DATE_2) + "'}"));
            }
            dataStr += array.toJSONString() + "}";
            result.setStatus(1);
            result.setData(JSONObject.parse(dataStr));
        } catch (ParseException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Result queryMoneyByFeeTypeAndMeterNo(String meter_no, Double amount) {
        String feeType = userDao.findUserByMeterNo(meter_no).getFee_type();
        Result result = new Result();
        JSONObject json = new JSONObject();
        if (userDao.findFeeTypeByName(feeType) == null) {
            result.setStatus(0);
            result.setData("没有此表相关费率类型!");
        } else {
            BigDecimal basicUnitPrice = new BigDecimal(
                    Double.toString(userDao.findFeeTypeByName(feeType).getBasicUnitPrice()));
            BigDecimal disposingUnitCost = new BigDecimal(
                    Double.toString(userDao.findFeeTypeByName(feeType).getDisposingUnitCost()));
            BigDecimal otherCost = new BigDecimal(Double.toString(userDao.findFeeTypeByName(feeType).getOtherCost()));
            BigDecimal totalUnitCost = new BigDecimal(
                    Double.toString(userDao.findFeeTypeByName(feeType).getTotalUnitCost()));
            BigDecimal extraCost = new BigDecimal(Double.toString(userDao.findFeeTypeByName(feeType).getExtraCost()));
            BigDecimal amounts = new BigDecimal(Double.toString(amount == null ? 0 : amount));
            json.put("basicUnitPrice", basicUnitPrice);
            json.put("disposingUnitCost", disposingUnitCost);
            json.put("otherCost", otherCost);
            json.put("totalUnitCost", totalUnitCost);
            json.put("extraCost", extraCost);
            json.put("basicPrice", basicUnitPrice.multiply(amounts).doubleValue());
            json.put("disposingPrice", disposingUnitCost.multiply(amounts).doubleValue());
            json.put("otherPriceAll", otherCost.multiply(amounts).doubleValue());
            json.put("totalPrice", totalUnitCost.multiply(amounts).add(extraCost).doubleValue());
            result.setStatus(1);
            result.setData(json);
        }
        return result;
    }

    /**
     *
     * @param cal
     */
    public void scheduler(Calendar cal) {
        if (cal.get(Calendar.MINUTE) % 30 != 0) {
            return;
        }
        payOrRefundCache.getAllKeys().stream().map((key) -> {
            payOrRefundCache.getCacheByKey(key).replace("access_token", RSACoder.getAccess_token());
            return key;
        }).forEachOrdered((key) -> {
            String msg = WechatApiUtil.payOrRefund(payOrRefundCache.getCacheByKey(key));
            // System.out.println("获取缓存中的信息"+payOrRefundCache.getCacheByKey(key));
            try {
                if ("1000".equals(JSONObject.parseObject(msg).get("code"))
                        || "1010".equals(JSONObject.parseObject(msg).get("code"))) {
                    payOrRefundCache.clearByKey(key);
                }
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        });
    }

    @Override
    public Result queryAmountByPriceAndMeterNo(String meter_no, Double price) {
        String feeType = userDao.findUserByMeterNo(meter_no).getFee_type();
        Result result = new Result();
        JSONObject json = new JSONObject();
        if (userDao.findFeeTypeByName(feeType) == null) {
            result.setStatus(0);
            result.setData("没有此表相关费率类型!");
        } else {
            // 基本水价
            BigDecimal basicUnitPrice = new BigDecimal(Double.toString(userDao.findFeeTypeByName(feeType).getBasicUnitPrice()));
            // 排污费单价
            BigDecimal disposingUnitCost = new BigDecimal(Double.toString(userDao.findFeeTypeByName(feeType).getDisposingUnitCost()));
            // 其他费用单价
            BigDecimal otherCost = new BigDecimal(Double.toString(userDao.findFeeTypeByName(feeType).getOtherCost()));
            // 附加费
            BigDecimal extraCost = new BigDecimal(Double.toString(userDao.findFeeTypeByName(feeType).getExtraCost()));
            // 总单价
            BigDecimal totalUnitCost = new BigDecimal(Double.toString(userDao.findFeeTypeByName(feeType).getTotalUnitCost()));

            // 总金额-附加费 = 总单价X数量
            double TotalUnitMoney = new BigDecimal(Double.toString(price)).subtract(extraCost).doubleValue();
            // 数量
            double amount = new BigDecimal(Double.toString(TotalUnitMoney)).divide(totalUnitCost, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 基本费用 = 数量X单价
            BigDecimal amounts = new BigDecimal(Double.toString(amount));
            json.put("basicUnitPrice", basicUnitPrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());// 基本水单价
            json.put("disposingUnitCost", disposingUnitCost.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());// 排污单价
            json.put("otherCost", otherCost.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());// 其他费用单价
            json.put("totalUnitCost", totalUnitCost.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());// 总的基本单价
            json.put("extraCost", extraCost.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()); // 附加费
            json.put("basicPrice",basicUnitPrice.multiply(amounts).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());// 总水费
            json.put("disposingPrice",disposingUnitCost.multiply(amounts).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());// 总排污费
            json.put("otherPriceAll", otherCost.multiply(amounts).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());// 总其他费用
            json.put("amount", amounts.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            json.put("totalPrice",totalUnitCost.multiply(amounts).add(extraCost).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            result.setStatus(1);
            result.setData(json);
        }
        return result;
    }
}
