/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.cache;

import com.joymeter.dao.UserDao;
import com.joymeter.entity.ReadInfo;
import com.joymeter.entity.User;
import com.joymeter.util.JoyDatetime;
import com.joymeter.util.JoyLogger;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Zhong Fuqiang
 * @version 1.0.0
 */
@Service
public class ReadInfoCache {

    @Resource
    private UserDao userDao;
    @Resource
    private MeterUserCache userCache;

    private static final Map<String, ReadInfo> TABLE_READ_INFO = new ConcurrentHashMap<>();

    /**
     * 从缓存中查询数据
     *
     * @param params
     * @return
     */
    public List<ReadInfo> query(HashMap<String, Object> params) {
        Collection<ReadInfo> values = TABLE_READ_INFO.values().stream()
                .filter(p -> p.getMeter_no() != null)
                .filter(p -> !p.getMeter_no().startsWith("-"))
                .sorted(Comparator.comparing(ReadInfo::getOperate_time).reversed())
                .collect(Collectors.toList());
        if (values == null || values.size() <= 0) {
            return null;
        }
        Object userName = params.get("user_name");
        if (userName != null && !userName.toString().isEmpty()) {
            values = values.stream()
                    .filter(p -> p.getUser_name().contains(userName.toString())).collect(Collectors.toList());
        }
        Object community = params.get("community");
        if (community != null && !community.toString().isEmpty()) {
            values = values.stream()
                    .filter(p -> p.getUser_address_community().equals(community.toString())).collect(Collectors.toList());
        }
        Object building = params.get("building");
        if (building != null && !building.toString().isEmpty()) {
            values = values.stream()
                    .filter(p -> p.getUser_address_building().equals(building.toString())).collect(Collectors.toList());
        }
        Object unit = params.get("unit");
        if (unit != null && !unit.toString().isEmpty()) {
            values = values.stream()
                    .filter(p -> p.getUser_address_unit().equals(unit.toString())).collect(Collectors.toList());
        }
        Object room = params.get("room");
        if (room != null && !room.toString().isEmpty()) {
            values = values.stream()
                    .filter(p -> p.getUser_address_room().equals(room.toString())).collect(Collectors.toList());
        }
        Object operator_name = params.get("operator_name");
        if (operator_name != null && !operator_name.toString().isEmpty()) {
            values = values.stream()
                    .filter(p -> p.getOperator_account().equals(operator_name.toString())).collect(Collectors.toList());
        }
        Object fee_type = params.get("fee_type");
        if (fee_type != null && !fee_type.toString().isEmpty()) {
            values = values.stream()
                    .filter(p -> p.getFee_type().equals(fee_type.toString())).collect(Collectors.toList());
        }
        Object meter_no = params.get("meter_no");
        if (meter_no != null && !meter_no.toString().isEmpty()) {
            values = values.stream()
                    .filter(p -> p.getMeter_no().equals(meter_no.toString())).collect(Collectors.toList());
        }
        Object meter_type = params.get("meter_type");
        if (meter_type != null && !meter_type.toString().isEmpty()) {
            values = values.stream()
                    .filter(p -> p.getMeter_type().equals(meter_type.toString())).collect(Collectors.toList());
        }
        Object startTime = params.get("startTime");
        if (startTime != null && !startTime.toString().isEmpty()) {
            values = values.stream()
                    .filter(p -> p.getOperate_time().compareTo(JoyDatetime.dateToTimestamp(JoyDatetime.strToDate(startTime.toString()))) >= 0)
                    .collect(Collectors.toList());
        }
        Object endTime = params.get("endTime");
        if (endTime != null && !endTime.toString().isEmpty()) {
            values = values.stream()
                    .filter(p -> p.getOperate_time().compareTo(JoyDatetime.dateToTimestamp(JoyDatetime.strToDate(endTime.toString()))) <= 0)
                    .collect(Collectors.toList());
        }
        //
        Object minBalance = params.get("minBalance");
        if(minBalance !=null && !minBalance.toString().isEmpty()) {
            values = values.parallelStream()
                    .filter(p -> Double.compare(p.getBalance(), (double) minBalance) >= 0)
                    .collect(Collectors.toList());
        }
        Object maxBalance = params.get("maxBalance");
        if(maxBalance !=null && !maxBalance.toString().isEmpty()) {
            values = values.parallelStream()
                    .filter(p -> Double.compare(p.getBalance(), (double) maxBalance) <= 0)
                    .collect(Collectors.toList());
        }
        //
        List<ReadInfo> res;
        Object pageSize = params.get("pageSize");
        if (pageSize == null || pageSize.equals("0")) {
            res = Collections.list(Collections.enumeration(values));
        } else {
            Object startNo = params.get("startNo");
            int fromIndex = Integer.valueOf(startNo.toString());
            int toIndex = fromIndex + Integer.valueOf(pageSize.toString());
            if (values.size() < toIndex) {
                toIndex = values.size() <= 0 ? 0 : values.size();
            }
            if (fromIndex > toIndex) {
                return null;
            }
            res = Collections.list(Collections.enumeration(values)).subList(fromIndex, toIndex);
        }
        if (res == null || res.isEmpty()) {
            params.put("startNo", "-1");
            params.put("pageSize", "-1");
        }
        return res;
    }

    /**
     * 查询抄表结果
     *
     * @param sendTime
     * @return
     */
    public List<ReadInfo> findReadResult(Timestamp sendTime) {
        try {
            if (sendTime == null || sendTime.toString().isEmpty()) {
                return null;
            }
            JoyLogger.LogInfo(String.format("ReadInfoCache.findReadResult, send time: %s", sendTime.toString()));
            Collection<ReadInfo> values = TABLE_READ_INFO.values();
            if (values == null || values.size() <= 0) {
                return userDao.findReadResult(sendTime);
            }
            values = values.stream()
                    .filter(p -> p.getOperate_time() != null && p.getOperate_time().getTime() >= sendTime.getTime())
                    .collect(Collectors.toList());
            List<ReadInfo> res = Collections.list(Collections.enumeration(values));
            if (res != null && res.size() > 0) {
                JoyLogger.LogInfo(String.format("res of size: %s", res.size()));
                return res;
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            JoyLogger.LogInfo(String.format("ReadInfoCache.findReadResult, exception: %s", ex.toString()));
        }
        return userDao.findReadResult(sendTime);
    }

    /**
     * 添加到抄表数据到缓存中
     *
     * @param meter_no
     * @param readInfo
     */
    public void add(final String meter_no, ReadInfo readInfo) {
        if (meter_no == null || meter_no.isEmpty() || readInfo == null) {
            return;
        }
        if ("0".equals(readInfo.getIsAutoClear()) || "1".equals(readInfo.getIsAutoClear())) { //
            if (TABLE_READ_INFO.containsKey(meter_no)) {
                ReadInfo entity = this.setData8(meter_no);
                if (entity.getData8() != null) {
                    BigDecimal b = new BigDecimal(readInfo.getData8() == null ? entity.getData8() : readInfo.getData8() + entity.getData8());
                    double data8 = b.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                    readInfo.setData8(data8);
                }
            }
            if (readInfo.getOperate_time() == null) {
                readInfo.setOperate_time(new Timestamp(System.currentTimeMillis()));
            }
            JoyLogger.LogInfo(String.format("ReadInfoCache add :%s, time: %s", meter_no, readInfo.getOperate_time().toString()));
            TABLE_READ_INFO.put(meter_no, readInfo);
        }
    }

    /**
     *
     * @param meter_no
     * @return
     */
    private ReadInfo setData8(final String meter_no) {
        ReadInfo entity = TABLE_READ_INFO.get(meter_no);
        if (entity == null) {
            return entity;
        }
        if (entity.getData8() == null || entity.getData8() <= 0) {
            Double sumData8 = userDao.findReadInfoSumData8(meter_no);
            entity.setData8(sumData8);
        }
        return entity;
    }

    /**
     * 查询某只表最新的记录
     *
     * @param meter_no
     * @return
     */
    public ReadInfo findLastReadInfoByMeterNo(final String meter_no) {
        if (TABLE_READ_INFO.containsKey(meter_no)) {
            return TABLE_READ_INFO.get(meter_no);
        }
        Double sumData8 = userDao.findReadInfoSumData8(meter_no);
        ReadInfo readInfo = userDao.findLastReadInfoByMeterNo(meter_no);
        if (readInfo != null) {
            readInfo.setData8(sumData8);
            this.add(meter_no, readInfo);
            return readInfo;
        }
        return null;
    }

    /**
     *
     * @return
     */
    public int size() {
        return TABLE_READ_INFO.size();
    }

    /**
     *
     * @return
     */
    public Collection<ReadInfo> findReadInfoCurrent() {
        userCache.init();
        if (userCache.size() != this.size()) {
            Collection<User> users = userCache.values();
            users.stream().forEach(p -> {
                if (TABLE_READ_INFO.containsKey(p.getMeter_no())) {
                    return;
                }
                ReadInfo readInfo = userDao.findLastReadInfoByMeterNo(p.getMeter_no());
                if (readInfo != null) {
                    readInfo.setUser_address_original_room(p.getUser_address_original_room());
                    readInfo.setSupplier_name(p.getSupplier_name());
                    this.add(p.getMeter_no(), readInfo);
                } else {
                    TABLE_READ_INFO.put("-" + p.getMeter_no(), new ReadInfo());
                }
            });
        }
        return TABLE_READ_INFO.values();
    }

    /**
     * 查询子表的最新抄表读数
     *
     * @param params
     * @return
     *
     */
    public List<ReadInfo> qryLatestReadInfos(Map<String, Object> params) {
        List<String> subMeterList = userCache.getSubMterNo(params.get("meter_no").toString());
        if (!TABLE_READ_INFO.isEmpty() && subMeterList != null ) {
            List<ReadInfo> readInfos = TABLE_READ_INFO.entrySet().stream()
                    .filter(r -> subMeterList.contains(r.getKey()))
                    .map(m -> m.getValue())
                    .filter(rds -> rds.getOperate_time().
                    compareTo(JoyDatetime.dateToTimestamp(JoyDatetime.strToDate(params.get("sendTime").toString(), JoyDatetime.FORMAT_DATETIME_3))) > 0)
                    .collect(Collectors.toList());
            return readInfos;
        }
        return userDao.qryLatestReadInfos(params);
    }

    /**
     * @param params
     * @return
     */
    public List<ReadInfo> qrysubLatestReadInfos(Map<String, Object> params) {
        List<String> meterList = userCache.getMterNo(params.get("meter_no").toString());
        if (!TABLE_READ_INFO.isEmpty() && meterList != null) {
            List<ReadInfo> readInfos = TABLE_READ_INFO.entrySet().stream()
                    .filter(r -> meterList.contains(r.getKey()))
                    .map(m -> m.getValue())
                    .filter(rds -> rds.getOperate_time().
                    compareTo(JoyDatetime.dateToTimestamp(JoyDatetime.strToDate(params.get("sendTime").toString(), JoyDatetime.FORMAT_DATETIME_3))) > 0)
                    .collect(Collectors.toList());
            return readInfos;
        }
        return userDao.qrysubLatestReadInfos(params);
    }
}
