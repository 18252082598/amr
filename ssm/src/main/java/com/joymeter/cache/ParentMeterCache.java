package com.joymeter.cache;

import com.joymeter.dao.UserDao;
import com.joymeter.entity.FeeType;
import com.joymeter.entity.ParentMeterConf;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author yinhf
 */
@Service
public class ParentMeterCache {

    @Resource
    private UserDao userDao;
    private static final Map<String, ParentMeterConf> TABLE_PARENT_METER = new ConcurrentHashMap<>();

    /**
     *
     * @param parentMeterConf 缓存中添加母表信息
     */
    public void add(ParentMeterConf parentMeterConf) {
        TABLE_PARENT_METER.put(parentMeterConf.getMeter_no(), parentMeterConf);
    }

    /**
     *
     * @param meterNo 查询母表数据
     * @return parentConInfo
     */
    public ParentMeterConf queryParentMeterConf(String meterNo) {
        if (TABLE_PARENT_METER.containsKey(meterNo)) {
            return TABLE_PARENT_METER.get(meterNo);
        }
        ParentMeterConf parentConInfo = userDao.qryMotherConfInfo(meterNo);
        if (parentConInfo != null) {
            this.add(parentConInfo);
        }

        return parentConInfo;
    }
}
