/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.service;

import com.joymeter.dao.UserDao;
import com.joymeter.entity.User;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public interface SaaSService {

    /**
     *
     * @param userDao
     * @param values
     */
    void setParams(UserDao userDao, Map<String, String> values);

    /**
     * 向SaaS平台注册设备
     *
     * @param user
     * @return
     */
    int add2SaaS(User user);

    /**
     * 删除SaaS平台上的数据
     *
     * @param user
     * @return
     */
    int delete2SaaS(User user);

    /**
     * 下发指令给SaaS平台
     *
     * @param user
     * @param payload
     * @return
     */
    int down2SaaS(User user, final String payload);

    /**
     * 下发指令给SaaS平台
     *
     * @param user
     * @param type
     * @param args
     * @return
     */
    int down2SaaS(User user, long type, final String... args);

    /**
     * 如果表号发生变更,则更新SaaS平台上的数据
     *
     * @param original_meter_no
     * @param user
     * @return
     */
    int update2SaaS(User user, String original_meter_no);
    
    /**
     * 
     * @param json 
     */
    public void down(final String json);

}
