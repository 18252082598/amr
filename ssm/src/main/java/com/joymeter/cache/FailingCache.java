/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 正在失败中的设备
 *
 * @author zhong Fuqiang
 * @version 1.0.0
 */
public class FailingCache {

    private final static Map<String, Long> CACHE_DEVICE_FAILING = new ConcurrentHashMap<>();

    /**
     *
     * @param key
     * @param value
     */
    public static void add(final String key, long value) {
        if (CACHE_DEVICE_FAILING.containsKey(key)) {
            CACHE_DEVICE_FAILING.remove(key);
        }
        CACHE_DEVICE_FAILING.put(key, value);
    }

    /**
     * 删除数据
     *
     * @param key
     */
    public static void remove(final String key) {
        if (CACHE_DEVICE_FAILING.containsKey(key)) {
            CACHE_DEVICE_FAILING.remove(key);
        }
    }

    /**
     * 超时的数据
     *
     * @return
     */
    public static Map expire() {
        Map<String, Long> devices = new HashMap<>();
        CACHE_DEVICE_FAILING.forEach((key, value) -> {
            long dif = (new Date().getTime() - value) / 1000 / 60;
            if (dif > 30) {
                devices.put(key, value);
            }
        });
        devices.keySet().stream().forEach(p -> CACHE_DEVICE_FAILING.remove(p));
        return devices;
    }
}
