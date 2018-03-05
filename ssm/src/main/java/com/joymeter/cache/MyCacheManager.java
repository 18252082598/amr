package com.joymeter.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyCacheManager<T> {
    private Map<String, T> cache = new ConcurrentHashMap<String,T>();

    // 获取缓存中的记录
    public T getValue(Object key) {
        return cache.get(key);
    }

    // 根据key更新缓存中的记录
    public void addOrUpdateCache(String key, T value) {
        cache.put(key,value);
    }

    // 根据 key 来删除缓存中的一条记录
    public void evictCache(String key) {
        if (cache.containsKey(key)) {
            cache.remove(key);
        }
    }

    // 清空缓存中的所有记录
    public void evictCache() {
        cache.clear();
    }
}
