/*
 Put user into memory
 */
package com.joymeter.cache;

import com.joymeter.dao.UserDao;
import com.joymeter.entity.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Zhong Fuqiang
 * @version 1.0.0
 */
@Service
public class MeterUserCache {

    @Resource
    private UserDao userDao;
    private static final Map<String, User> TABLE_USER = new ConcurrentHashMap<>();

    /**
     * 初始化用户数据
     *
     */
    public void init() {
        Map<String, Object> params = new HashMap<>();
        params.put("supplier_name", "");
        params.put("user_name", "");
        params.put("community", "");
        params.put("building", "");
        params.put("unit", "");
        params.put("room", "");
        params.put("operator_name", "");
        params.put("contact_info", "");
        params.put("meter_no", "");
        params.put("status", "");
        int total = userDao.findTotalUser(params);
        if (total != TABLE_USER.size()) {
            int pageSize = 1000;
            for (int i = 0; i < (total + pageSize) / pageSize; i++) {
                int startNo = i * pageSize;
                params.put("startNo", startNo);
                params.put("pageSize", pageSize);
                List<User> users = userDao.findUser(params);
                if (users != null && !users.isEmpty()) {
                    users.stream().forEach(p -> TABLE_USER.put(p.getMeter_no(), p));
                }
            }
        }
    }

    /**
     * 获取母表下的子表表号
     *
     * @param meterNo
     * @return
     */
    public List<String> getSubMterNo(final String meterNo) {
        this.init();
        if (this.values() != null && !this.values().isEmpty()) {
            List<String> subMeterNo = this.values().stream()
                    .filter(u -> u.getSubmeter_no().equals(meterNo))
                    .filter(r -> !r.getSubmeter_no().equals(r.getMeter_no()))
                    .map(m -> m.getMeter_no())
                    .collect(Collectors.toList());
            return subMeterNo;
        }
        return null;
    }

    /**
     *
     * @param user
     */
    public void add(User user) {
        if (user == null) {
            return;
        }
        TABLE_USER.put(user.getMeter_no(), user);
    }

    /**
     *
     * @return
     */
    public Collection<User> values() {
        return TABLE_USER.values();
    }

    /**
     *
     * @param meterNos
     */
    public void remove(final String[] meterNos) {
        if (meterNos == null || meterNos.length <= 0) {
            return;
        }
        for (String meterNo : meterNos) {
            TABLE_USER.remove(meterNo);
        }
    }

    /**
     * 获取母表表号和子表号
     *
     * @param meterNo
     * @return
     */
    public List<String> getMterNo(final String meterNo) {
        this.init();
        if (TABLE_USER.containsKey(meterNo)) {
            List<String> subMeterNo = this.values()
                    .stream().filter(u -> u.getSubmeter_no().equals(TABLE_USER.get(meterNo).getSubmeter_no()))
                    .map(m -> m.getMeter_no())
                    .collect(Collectors.toList());
            return subMeterNo;
        }
        return null;
    }

    /* *
     * @return
     */
    public int size() {
        return TABLE_USER.size();
    }

    /**
     *
     * @param meterNo
     * @return
     */
    public User getUserByMeterNo(final String meterNo) {
        User user = TABLE_USER.get(meterNo);
        if (user == null) {
            user = userDao.findUserByMeterNo(meterNo);
            this.add(user);
        }
        return user;
    }

    /**
     *
     * @param params
     * @return
     */
    public List<User> getSubUsers(Map<String, Object> params) {
        this.init();
        if (!this.values().isEmpty()) {
            List<User> nums = this.values().stream()
                    .filter(u -> u.getSubmeter_no().equals(params.get("meter_no")))
                    .filter(r -> !r.getMeter_no().equals(params.get("meter_no")))
                    .filter(e -> "0".equals(e.getMeter_status()) || "1".equals(e.getMeter_status()))
                    .collect(Collectors.toList());
            return nums;
        }
        return userDao.qrySubMeter(params);
    }
}
