/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.cache;

import com.joymeter.dao.UserDao;
import com.joymeter.entity.ReadInfo;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
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
public class ValveStateCache {

    @Resource
    private UserDao userDao;
    private static final Map<String, ReadInfo> VALVE_STATE_CACHE = new ConcurrentHashMap<>();

    /**
     *
     * @param meter_no
     * @param readInfo
     */
    public void add(final String meter_no, ReadInfo readInfo) {
        if (meter_no == null || meter_no.isEmpty() || readInfo == null) {
            return;
        }
        if ("10".equals(readInfo.getIsAutoClear())) { //
            VALVE_STATE_CACHE.put(meter_no, readInfo);
        }
    }

    /**
     * 查询
     *
     * @param sendTime
     * @return
     */
    public List<ReadInfo> findValveStatus(Timestamp sendTime) {
        try {
            if (sendTime == null || sendTime.toString().isEmpty()) {
                return null;
            }
            Collection<ReadInfo> values = VALVE_STATE_CACHE.values();
            if (values == null || values.size() <= 0) {
                return userDao.findValveStatus(sendTime);
            }
            values = values.stream()
                    .filter(p -> sendTime.compareTo(p.getOperate_time()) <= 0)
                    .filter(p -> "10".equals(p.getIsAutoClear()))
                    .collect(Collectors.toList());
            List<ReadInfo> res = Collections.list(Collections.enumeration(values));
            if (res != null && res.size() > 0) {
                return res;
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return userDao.findValveStatus(sendTime);
    }
}
