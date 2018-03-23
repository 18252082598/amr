package com.joymeter.service.callbackImp;

import java.util.Calendar;
import java.util.Map;

import com.joymeter.cache.ReadInfoCache;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.ReadInfo;
import com.joymeter.entity.User;
import com.joymeter.service.CallBackService;

public class CallBackServiceImp implements CallBackService{

    protected UserDao userDao;
    protected Map<String, String> values;
    public static String callBackJsonFormat = "{\"status\":%s,\"data\":{\"meterNo\":\"%s\",\"event\":\"%s\",\"data\":\"%s\",\"datetime\":\"%s\"}}";


    @Override
    public void setParams(UserDao userDao, Map<String, String> values) {
        this.userDao=userDao;
        this.values = values;
        
    }

    /**
     * 回调绑定表号的房源信息给客户
     *
     * @param user
     */
    @Override
    public void callBackData(User user) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 抄表数据回调
     *
     * @param readInfo
     */
    @Override
    public void callBackReadInfo(ReadInfo readInfo) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 定时回调
     *
     * @param cal
     */
    @Override
    public void schedulerCallBackReadInfo(Calendar cal) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 定时回调
     *
     * @param cal
     * @param reacInfoCache
     */
    @Override
    public void schedulerCallBackReadInfo(Calendar cal, ReadInfoCache reacInfoCache) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 定时回调
     *
     * @param cal
     * @param readInfo
     */
    @Override
    public void schedulerCallBackReadInfo(Calendar cal, ReadInfo readInfo) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 余额预警回调
     *
     * @param readInfo
     */
    @Override
    public void callBackBalanceWarn(ReadInfo readInfo) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 阀门关闭回调
     *
     * @param readInfo
     */
    @Override
    public void callBackValveState(ReadInfo readInfo) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 设备的状态
     *
     * @param readInfo
     */
    @Override
    public void callBackDeviceState(ReadInfo readInfo) {
        // TODO Auto-generated method stub
        
    }

    /**
    *
    * @param user
    */
    @Override
    public void updateUser(User user) {
        // TODO Auto-generated method stub
        
    }

    /**
    *
    * @param params
    */
    @Override
    public void changeMeterNo(Map<String, Object> params) {
        // TODO Auto-generated method stub
        
    }


}
