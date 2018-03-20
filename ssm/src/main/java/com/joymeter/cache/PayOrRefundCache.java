package com.joymeter.cache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * 充值失败的记录
 *
 * @author lijian
 *
 */
@Service
public class PayOrRefundCache {

    private static final Map<String, Map<String, String>> PAY_OR_REFUND_FAILED = new ConcurrentHashMap<>();

    //存入缓存
    public void putCache(String key, Map<String, String> value) {
        PAY_OR_REFUND_FAILED.put(key, value);
    }

    //获取对应缓存
    public Map<String, String> getCacheByKey(String key) {
        if (this.isContains(key)) {
            return PAY_OR_REFUND_FAILED.get(key);
        }
        return null;
    }

    //获取所有缓存
    public Map<String, Map<String, String>> getCacheAll() {
        return PAY_OR_REFUND_FAILED;
    }

    //判断是否在缓存中
    public boolean isContains(String key) {
        return PAY_OR_REFUND_FAILED.containsKey(key);
    }

    //清除所有缓存
    public void clearAll() {
        PAY_OR_REFUND_FAILED.clear();
    }

    //清除对应的缓存
    public void clearByKey(String key) {
        if (this.isContains(key)) {
            PAY_OR_REFUND_FAILED.remove(key);
        }
    }

    //获取所有的key
    public Set<String> getAllKeys() {
        return PAY_OR_REFUND_FAILED.keySet();
    }

}
