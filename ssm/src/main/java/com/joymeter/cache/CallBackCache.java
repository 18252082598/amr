/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.cache;

import com.joymeter.dao.UserDao;
import com.joymeter.entity.ReadInfo;
import com.joymeter.task.JoyServletContextListener;
import com.joymeter.util.JoyDatetime;
import com.joymeter.util.JoyLogger;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Zhong Fuqiang
 * @version 1.0.0
 */
public class CallBackCache {

    private static final Map<String, Long> MAP_VALVE_CTR = new ConcurrentHashMap<>();
    private static final Queue<ReadInfo> QUEUE_VALVE_CTR_ERROR = new ConcurrentLinkedQueue<>();
    private static final Queue<ReadInfo> QUEUE_VALVE_CTR_CALLBACK_FAILD = new ConcurrentLinkedQueue<>();
    private static final Queue<ReadInfo> QUEUE_READ_CALLBACK_FAILD = new ConcurrentLinkedQueue<>();

    /**
     * 添加控制设备的表号,用于处理过期抄表是否过期
     *
     * @param meter_no
     */
    public static void add(final String meter_no) {
        if (meter_no == null || meter_no.isEmpty()) {
            return;
        }
        Date dt = JoyDatetime.addSecond(new Date().getTime(), 10 * MAP_VALVE_CTR.size());
        String key = String.format("%s~%s", meter_no, dt.getTime());
        MAP_VALVE_CTR.put(key, dt.getTime());
    }

    /**
     * 加入控制错误的对象到缓存中
     *
     * @param readInfo
     */
    public static void add(ReadInfo readInfo) {
        QUEUE_VALVE_CTR_ERROR.add(readInfo);
    }

    /**
     * 加入回调失败的对象到缓存中
     *
     * @param readInfo
     */
    public static void addValveCallbackFailed(ReadInfo readInfo) {
        if (readInfo == null) {
            return;
        }
        readInfo.setInfo_id((readInfo.getInfo_id() == null || readInfo.getInfo_id() > 5) ? 0 : readInfo.getInfo_id() + 1);
        QUEUE_VALVE_CTR_CALLBACK_FAILD.stream()
                .filter((entry) -> entry.getMeter_no().equals(readInfo.getMeter_no()))
                .map((entry) -> {
                    return entry;
                }).forEachOrdered((entry) -> {
            QUEUE_VALVE_CTR_CALLBACK_FAILD.remove(entry);
        });
        JoyLogger.LogInfo(String.format("addReadCallbackFailed times: %s", readInfo.getInfo_id()));
        if (readInfo.getInfo_id() > 5) { // 最多回调5次
            return;
        }
        QUEUE_VALVE_CTR_CALLBACK_FAILD.add(readInfo);
    }

    /**
     * 加入回调失败的对象到缓存中
     *
     * @param readInfo
     */
    public static void addReadCallbackFailed(ReadInfo readInfo) {
        if (readInfo == null) {
            return;
        }
        readInfo.setInfo_id((readInfo.getInfo_id() == null || readInfo.getInfo_id() > 5) ? 0 : readInfo.getInfo_id() + 1);
        QUEUE_READ_CALLBACK_FAILD.stream()
                .filter((entry) -> entry.getMeter_no().equals(readInfo.getMeter_no()))
                .map((entry) -> {
                    return entry;
                }).forEachOrdered((entry) -> {
            QUEUE_READ_CALLBACK_FAILD.remove(entry);
        });
        JoyLogger.LogInfo(String.format("addReadCallbackFailed times: %s", readInfo.getInfo_id()));
        if (readInfo.getInfo_id() > 5) {// 最多回调5次
            return;
        }
        QUEUE_READ_CALLBACK_FAILD.add(readInfo);
    }

    /**
     * 删除成功回调的数据
     *
     * @param meter_no
     */
    public static void remove(final String meter_no) {
        Optional<Entry<String, Long>> opt = MAP_VALVE_CTR.entrySet().stream()
                .filter((entry) -> entry.getKey().contains(meter_no))
                .findFirst();
        if (!opt.isPresent()) {
            return;
        }
        MAP_VALVE_CTR.remove(opt.get().getKey());
    }

    /**
     * 请求超时的数据
     *
     * @param userDao
     */
    public static void doExpired(UserDao userDao) {
        while (!QUEUE_VALVE_CTR_ERROR.isEmpty()) {
            JoyServletContextListener.instanceCallback(userDao).callBackValveState(QUEUE_VALVE_CTR_ERROR.poll());
        }
        MAP_VALVE_CTR.entrySet().stream()
                .filter((entry) -> (((new Date().getTime() - entry.getValue()) / 1000) > 10))
                .map((entry) -> {
                    return entry;
                }).forEachOrdered((entry) -> {
            ReadInfo readInfo = new ReadInfo();
            readInfo.setException("0");
            readInfo.setIsAutoClear("10");
            readInfo.setMeter_no(CallBackCache.getMeterNumber(entry.getKey()));
            readInfo.setData9("请求超时,请重新请求");
            JoyServletContextListener.instanceCallback(userDao).callBackValveState(readInfo);
            MAP_VALVE_CTR.remove(entry.getKey());
        });
    }

    /**
     *
     * @param value
     * @return
     */
    public static String getMeterNumber(final String value) {
        if (value == null || value.isEmpty()) {
            String[] datas = StringUtils.splitByWholeSeparator(value, "~");
            if (datas == null || datas.length != 2) {
                return "";
            }
            return datas[0];
        }
        return "";
    }

    /**
     * 从新发送失败的数据
     *
     * @param userDao
     */
    public static void CallBack(UserDao userDao) {
        while (!QUEUE_VALVE_CTR_CALLBACK_FAILD.isEmpty()) {
            JoyServletContextListener.instanceCallback(userDao)
                    .callBackValveState(QUEUE_VALVE_CTR_CALLBACK_FAILD.poll());
        }
        while (!QUEUE_READ_CALLBACK_FAILD.isEmpty()) {
            JoyServletContextListener.instanceCallback(userDao)
                    .callBackReadInfo(QUEUE_READ_CALLBACK_FAILD.poll());
        }
    }
}
