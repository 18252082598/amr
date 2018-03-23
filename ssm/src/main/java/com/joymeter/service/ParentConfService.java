package com.joymeter.service;

import com.joymeter.entity.OperateInfo;
import com.joymeter.entity.ParentMeterConf;
import com.joymeter.entity.ReadInfo;
import com.joymeter.entity.Result;
import com.joymeter.entity.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author yinhf
 */
public interface ParentConfService {

    //修改母表配置信息
    public Result updateMotherMeterConf(ParentMeterConf mmc, String org_meter_no, 
            String org_meter_type, String org_allot_type);
    //删除母表配置信息
    public Result removeParentMeterConf(final String meterNums);

    //查询公摊扣费信息
    public Result qryShareBilling(String user_name, String community,
            String building, String unit, String room, String meter_no, int startNo, int pageSize);

    public Result upload(HttpServletRequest request) throws Exception;

    public Result downLoadConfExcel(HttpServletRequest request,
            HttpServletResponse response) throws Exception;

    public Result shareBilling(User user, ReadInfo readInfo,OperateInfo parentOperateInfo);
    
    public Result shareBilling(User user, ReadInfo readInfo,OperateInfo parentOperateInfo,String sendTime);
}
