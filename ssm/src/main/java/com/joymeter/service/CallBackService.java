package com.joymeter.service;

import com.joymeter.cache.ReadInfoCache;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.ReadInfo;
import com.joymeter.entity.User;
import java.util.Calendar;
import java.util.Map;

public interface CallBackService {

    public void callBackData(User user);

    public void callBackReadInfo(ReadInfo readInfo);

    public void schedulerCallBackReadInfo(Calendar cal);

    public void schedulerCallBackReadInfo(Calendar cal, ReadInfoCache reacInfoCache);

    public void schedulerCallBackReadInfo(Calendar cal, ReadInfo readInfo);

    public void callBackBalanceWarn(ReadInfo readInfo);

    public void callBackValveState(ReadInfo readInfo);

    public void callBackDeviceState(ReadInfo readInfo);

    public void updateUser(User user);

    public void changeMeterNo(Map<String, Object> params);

    public void setParams(UserDao userDao, Map<String, String> params);
}
