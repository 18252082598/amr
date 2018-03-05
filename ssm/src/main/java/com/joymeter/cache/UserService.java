package com.joymeter.cache;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private MyCacheManager<User> cache;

    private static Map<String, User> users = new HashMap<String, User>();

    public UserService() {
        cache = new MyCacheManager<User>();
        users.put("41", new User(41, "fan01", "123"));
        users.put("42", new User(42, "fan02", "123"));
        users.put("43", new User(43, "fan03", "123"));
    }

    // 根据id,获取用户信息
    public User getUserById(Integer id) {
        User userCache = cache.getValue(id.toString());
        if (null != userCache) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++cache!!!");
            System.out.println(userCache);
            return userCache;
        }
        User result = users.get(id.toString());
        if (null != result) {
            cache.addOrUpdateCache(id.toString(), result);
            System.out.println("+++++++++++++++++++++++++++++++++++++++数据库!!!");
        }
        return result;
    }

    // 根据id,清除缓存中的信息
    public void evictCache(Integer key) {
        cache.evictCache(key.toString());
    }

    // 清除缓存中的信息
    public void evictCache() {
        cache.evictCache();
    }
}
