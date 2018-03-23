package com.joymeter.cache;

import com.joymeter.dao.UserDao;
import com.joymeter.entity.FeeType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author yinhf
 */
@Service
public class FeeTypeCache {

    @Resource
    private UserDao userDao;
    private static final Map<String, FeeType> TABLE_FEE_TYPE = new ConcurrentHashMap<>();

    /**
     *
     * @param feeType 缓存中添加费率
     */
    public void add(FeeType feeType) {
        TABLE_FEE_TYPE.put(feeType.getFeeTypeName(), feeType);
    }

    /**
     * 查询费率
     *
     * @param feeTypeName
     * @return feeType
     */
    public FeeType findFeeTypeByName(String feeTypeName) {
        FeeType feeType;
        if (TABLE_FEE_TYPE.containsKey(feeTypeName)) {
            return TABLE_FEE_TYPE.get(feeTypeName);
        } else {
            feeType = userDao.findFeeTypeByName(feeTypeName);
            this.add(feeType);

        }
        return feeType;
    }
}
