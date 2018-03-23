/*
Query Event_record table and put records into memery.
 */
package com.joymeter.cache;

import com.joymeter.dao.UserDao;
import com.joymeter.entity.EventRecord;
import com.joymeter.entity.ReadInfo;
import com.joymeter.entity.User;
import com.joymeter.util.JoyDatetime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author yinhf
 */
@Service
public class EventRecordCache {

    @Resource
    private UserDao userDao;
    private static final Map<String, EventRecord> TABLE_EVENT_RECORDS = new ConcurrentHashMap<>();

    /**
     *
     * @param eventRecord
     */
    public void add(EventRecord eventRecord) {
        TABLE_EVENT_RECORDS.put(eventRecord.getEvent() + eventRecord.getMeter_no(), eventRecord);
    }

    /**
     *
     * @param meterNo
     * @param eventValue
     * @return
     */
    public EventRecord getEventRecord(final String meterNo, final String eventValue) {
        if (TABLE_EVENT_RECORDS.containsKey(eventValue + meterNo)) {
            return TABLE_EVENT_RECORDS.get(eventValue + meterNo);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("meter_no", meterNo);
        map.put("error_code", eventValue);
        EventRecord event = userDao.getEventInfo(map);
        return event;
    }

    /**
     * 根据电表 uuid 查询电表操作记录
     *
     * @param uuid
     * @param count 本次拉取的个数，默认是 50
     * @param offset 拉取的偏移量，默认为 0
     * @param start_time 10位时间戳
     * @param end_time 10位时间戳
     * @return
     */
    public List<EventRecord> getEventRecord(User user,Integer count,
            Integer offset, Long start_time, Long end_time,String flag) {
        Map<String, Object> map = new HashMap<>();
        Date start = start_time == null ? null:JoyDatetime.timestampToDate(start_time*1000);
        Date end = start_time == null ? null:JoyDatetime.timestampToDate(start_time*1000);
        int countNum = count == null ? 50 : count;
        int offsetNum = offset == null ? 0 : offset;
        map.put("meter_no", user.getMeter_no());
        map.put("start_time",JoyDatetime.dateFormat(start, JoyDatetime.FORMAT_DATETIME_2));
        map.put("end_time", JoyDatetime.dateFormat(end, JoyDatetime.FORMAT_DATETIME_2)); 
        map.put("pageSize", countNum+offsetNum);
        return "operations".equals(flag)? userDao.getEleExceptions(map):userDao.getEleOperations(map);
    }
}
