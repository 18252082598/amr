package com.joymeter.cache;

public class TestUserCache {
    
    static UserService userService = new UserService();  
    
    public static void main(String[] args) {  
  
        // 开始查询账号  
        // 第一次查询，应该是数据库查询  
        userService.getUserById(41);  
  
        // 第二次查询，应该直接从缓存返回  
        userService.getUserById(41);  
  
        // 清除缓存中的用户信息  
        userService.evictCache();  
          
        System.out.println("清除缓存后,再次查询用户信息..........");  
          
        // 第一次查询，应该是数据库查询  
        userService.getUserById(41);  
          
        // 第二次查询，应该直接从缓存返回  
        userService.getUserById(41);  
  
    }  
    
}
