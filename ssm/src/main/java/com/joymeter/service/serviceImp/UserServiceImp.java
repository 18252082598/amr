package com.joymeter.service.serviceImp;

import com.alibaba.fastjson.JSONArray;
import com.joymeter.cache.EventRecordCache;
import com.joymeter.cache.FeeTypeCache;
import com.joymeter.cache.MeterUserCache;
import com.joymeter.cache.ParentMeterCache;
import com.joymeter.cache.ReadInfoCache;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.stereotype.Service;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.Admin;
import com.joymeter.entity.Building;
import com.joymeter.entity.Community;
import com.joymeter.entity.Concentrator;
import com.joymeter.entity.EventRecord;
import com.joymeter.service.UserService;
import com.joymeter.entity.FeeType;
import com.joymeter.entity.HouseCoefficient;
import com.joymeter.entity.ParentMeterConf;
import com.joymeter.entity.ReadInfo;
import com.joymeter.entity.Result;
import com.joymeter.entity.Room;
import com.joymeter.entity.RoomInfo;
import com.joymeter.entity.SysLog;
import com.joymeter.entity.Unit;
import com.joymeter.entity.User;
import com.joymeter.entity.UserManageInfo;
import com.joymeter.entity.WaterSupplier;
import com.joymeter.service.SaaSService;
import com.joymeter.util.HttpRequest;
import com.joymeter.task.JoyServletContextListener;
import com.joymeter.util.JoyDatetime;
import com.joymeter.util.Util;
import com.joymeter.util.WechatApiUtil;
import com.joymeter.velocity.VelocityToHtml;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserServiceImp implements UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private SaaSService saaSService;
    @Resource
    private FeeTypeCache feeTypeCache;
    @Resource
    private ParentMeterCache parentMeterCache;
    @Resource
    private MeterUserCache userCache;
    @Resource
    private EventRecordCache eventRecordCache;
    @Resource
    private ReadInfoCache readInfoCache;

    /**
     * 操作员登录检查
     *
     * @param account_name
     * @param password
     * @return
     */
    @Override
    public Result checkLogin(String account_name, String password) {
        Result result = new Result();
        try {
            Admin admin = userDao.findByName(account_name);
            if (admin == null) {
                result.setStatus(0);
            } else {
                String md5_password = Util.md5(password);
                if (md5_password.equals(admin.getAdmin_password())) {
                    result.setStatus(1);
                } else {
                    result.setStatus(2);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setStatus(3);
        }
        return result;
    }

    /**
     * 查询本月抄表报告
     *
     * @return
     */
    @Override
    public Result findReadSuccessNo() {
        Result result = new Result();
        try {
            //Map<String, Object> map = new HashMap<>();
            //本月抄表成功数量
            int readSuccessNo = userDao.findReadSuccessNo();
            //本月抄表失败数量
            //int readFailNo = userDao.findReadFailNo();
            //本月扣费失败数量
            //int deductFailNo = userDao.findDeductFailNo();
            //本月电池欠压数量
            //int batteryLowNo = userDao.findBatteryLowNo();
            //map.put("readSuccessNo", readSuccessNo);
            //map.put("readFailNo", readFailNo);
            //map.put("deductFailNo", deductFailNo);
            //map.put("batteryLowNo", 0);
            result.setStatus(1);
            result.setData(readSuccessNo);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Result findReadFailNo() {
        Result result = new Result();
        try {
            //本月抄表失败数量
            int readFailNo = userDao.findReadFailNo();
            result.setStatus(1);
            result.setData(readFailNo);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Result findDeductFailNo() {
        Result result = new Result();
        try {
            //本月扣费失败数量
            int deductFailNo = userDao.findDeductFailNo();
            result.setStatus(1);
            result.setData(deductFailNo);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 查询待处理信息
     *
     * @return
     */
    @Override
    public Result toDo() {
        Result result = new Result();
        try {
            Map<String, Object> map = new HashMap<>();
            //换表数量
            int meterChangeNo = userDao.findMeterChangeNo();
            //销户数量
            int userDelNo = userDao.findUserDelNo();
            //欠费数量
            int overdueNo = userDao.findOverdueNo();
            map.put("meterChangeNo", meterChangeNo);
            map.put("userDelNo", userDelNo);
            map.put("overdueNo", overdueNo);
            result.setStatus(1);
            result.setData(map);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 查询楼栋单元等信息
     *
     * @return
     */
    @Override
    public Result findInfo() {
        Result result = new Result();
        Map<String, Object> map = new HashMap<>();
        try {
            int communityNumber = userDao.findCommunityNumber();
            int buildingNumber = userDao.findBuildingNumber();
            int unitNumber = userDao.findUnitNumber();
            int mbusNumber = userDao.findMbusNumber();
            int meterNumber = userDao.findMeterNumber();
            map.put("communityNumber", communityNumber);
            map.put("buildingNumber", buildingNumber);
            map.put("unitNumber", unitNumber);
            map.put("mbusNumber", mbusNumber);
            map.put("meterNumber", meterNumber);
            result.setStatus(1);
            result.setData(map);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 查询本月已完成的业务信息
     *
     * @return
     */
    @Override
    public Result beenDone() {
        Result result = new Result();
        try {
            Map<String, Object> map = new HashMap<>();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            String month = df.format(System.currentTimeMillis());
            int rechargeNo = userDao.findRechargeNo(month);
            int refundNo = userDao.findRefundNo(month);
            int changeMeterNo = userDao.findChangeMeterNo(month);
            int delUserNo = userDao.findDelUserNo(month);
            map.put("rechargeNo", rechargeNo);
            map.put("refundNo", refundNo);
            map.put("changeMeterNo", changeMeterNo);
            map.put("delUserNo", delUserNo);
            result.setStatus(1);
            result.setData(map);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 查询充值业务信息
     *
     * @return
     */
    @Override
    public Result rechargeReport() {
        Result result = new Result();
        Map<String, Object> map = new HashMap<>();
        try {
            Double totalRecharge = userDao.findTotalRecharge();
            totalRecharge = totalRecharge == null ? 0 : totalRecharge;
            Double totalRefund = userDao.findTotalRefund();
            totalRefund = totalRefund == null ? 0 : totalRefund;
            map.put("totalRecharge", totalRecharge);
            map.put("totalRefund", totalRefund);
            result.setStatus(1);
            result.setData(map);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 查询一周充值信息
     *
     * @return
     */
    @Override
    public Result rechargeReportAtOneWeek() {
        Result result = new Result();
        try {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < 7; i++) {
                Double rechargeAmount = userDao.findRechargeAmount(i);
                rechargeAmount = rechargeAmount == null ? 0 : rechargeAmount;
                map.put("rechargeAmount" + i, rechargeAmount);
            }
            result.setStatus(1);
            result.setData(map);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 查询充值排行信息
     *
     * @return
     */
    @Override
    public Result rechargeRank() {
        Result result = new Result();
        try {
            List<Object> list = new ArrayList<>();
            List<Community> communities = userDao.loadCommunity();
            for (int i = 0; i < communities.size(); i++) {
                String community_name = communities.get(i).getCommunity_name();
                Double communityRecharge = userDao
                        .findCommunityRecharge(community_name);
                communityRecharge = communityRecharge == null ? 0
                        : communityRecharge;
                List<Object> li = new ArrayList<>();
                li.add(community_name);
                li.add(communityRecharge);
                list.add(li);
            }
            result.setStatus(1);
            result.setData(list);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    @Override
    public Result regist(User user) {
        Result result = new Result();
        HashMap<String, Object> params = new HashMap<>();
        try {
            if (user.getMeter_no() != null && user.getMeter_no().length() > 0) {
                if (userDao.checkMeterNoExist(user.getMeter_no()) != null) {
                    result.setStatus(1062);
                    return result;
                }
            }
            user.setUser_address_original_room(UUID.randomUUID().toString().replace("-", ""));
            // 如果阀门编号为空或0，则用表号代替
            if (user.getValve_no() == null || user.getValve_no().isEmpty()
                    || user.getValve_no().equals("0")) {
                user.setValve_no(user.getMeter_no());
            }
            user.setAdd_time(new Timestamp(System.currentTimeMillis()));
            userDao.regist(user);
            userCache.add(user);
            JoyServletContextListener.instanceCallback(userDao).callBackData(user);
            String feeTypeName = user.getFee_type();
            params.put("feeTypeName", feeTypeName);
            params.put("feeTypeStatus", "1");
            userDao.updateFeeTypeStatus(params);
            result.setStatus(1);
            // 如果采用的是JLAA协议,则向SaaS平台注册数据
            if (user.getProtocol_type().toLowerCase().startsWith("jlaa")) {
                user.setRegister_status(1);
                saaSService.setParams(userDao, null);
                saaSService.add2SaaS(user);
            }
        } catch (Exception e) {
            SQLException sqle = (SQLException) e.getCause();
            int sqlErrorCode = sqle.getErrorCode();
            if (sqlErrorCode == 1062) {
                result.setStatus(sqlErrorCode);
                result.setData(sqle.getMessage());
            } else {
                result.setStatus(0);
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return result;
    }

    /**
     * 添加母表参数配置
     *
     * @param mmc
     * @return
     */
    @Override
    public Result addMotherMeterConf(ParentMeterConf mmc) {
        Result result = new Result();
        try {
            userDao.addMotherMeterConf(mmc);
            parentMeterCache.add(mmc);
            result.setStatus(1);
        } catch (Exception e) {
            SQLException sqle = (SQLException) e.getCause();
            int sqlErrorCode = sqle.getErrorCode();
            if (sqlErrorCode == 1062) {
                result.setStatus(sqlErrorCode);
                result.setData(sqle.getMessage());
            } else {
                result.setStatus(0);
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return result;
    }

    /**
     * 更新用户状态
     *
     * @param meter_no
     * @param user_status
     * @param meter_status
     * @return
     */
    @Override
    public Result updateStatus(String meter_no, String user_status,
            String meter_status) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("meter_no", meter_no);
        params.put("user_status", user_status);
        params.put("meter_status", meter_status);
        try {
            userDao.updateStatus(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 查询用户的操作时间
     *
     * @param meterNo
     * @return
     */
    @Override
    public Result findOperateTime(String meterNo) {
        Result result = new Result();
        try {
            List<UserManageInfo> info = userDao.findOperateTime(meterNo);
            if (info.isEmpty()) {
                info = userDao.queryOperateTime(meterNo);
                info = userDao.findOperateTime(info.get(0).getMeter_no());
            }
            result.setStatus(1);
            result.setData(info);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 申请换表
     *
     * @param info
     * @return
     */
    @Override
    public Result applyChangeMeter(UserManageInfo info) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("meter_no", info.getMeter_no());
        params.put("user_status", "1");//正常
        params.put("meter_status", "2");//申请换表
        try {
            userDao.addManageInfo(info);
            userDao.updateStatus(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 换表拆表
     *
     * @param info
     * @return
     */
    @Override
    public Result removeMeter(UserManageInfo info) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("meter_no", info.getMeter_no());
        params.put("user_status", "1");//正常
        params.put("meter_status", "3");//已拆表
        try {
            userDao.addManageInfo(info);
            userDao.updateStatus(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 换表
     *
     * @param info
     * @param original_meter_no
     * @return
     */
    @Override
    public Result changeMeter(UserManageInfo info, String original_meter_no) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("meter_no", info.getMeter_no());
        params.put("new_meter_no", info.getNew_meter_no());
        params.put("user_status", "1");//正常
        params.put("meter_status", "1");//正常
        Double initing_data;
        try {
            User user = userDao.findUserByMeterNo(info.getMeter_no());
            if (user == null) {
                return null;
            }
            ReadInfo readInfo = userDao.findLastReadInfoByMeterNo(info.getMeter_no());
            if (readInfo == null || readInfo.getThis_read() == null) {
                initing_data = 0d;
            } else {
                initing_data = readInfo.getThis_read();
            }
            String meter_no = user.getMeter_no();
            String submeter_no = user.getSubmeter_no();
            userDao.addManageInfo(info);
            user.setMeter_no(info.getNew_meter_no());
            user.setValve_no(info.getNew_meter_no());
            user.setMeter_status("1");
            user.setUser_status("1");
            user.setIniting_data(initing_data);
            //如果改变的表号是私有母表的情况下，在数据库母表表中添加新母表信息并更新原先母表关联的子表将其子表的submeter_no字段更新为新母表表号
            if (meter_no.equals(submeter_no)) {
                ParentMeterConf pmc = userDao.qryMotherConfInfo(meter_no);
                if (pmc == null) {
                    return null;
                }
                pmc.setMeter_no(info.getNew_meter_no());
                user.setSubmeter_no(info.getNew_meter_no());
                userDao.regist(user);
                userDao.addMotherMeterConf(pmc);
                if ("1".equals(pmc.getMeter_type())) {
                    List<User> users = userDao.findUserBySubNo(submeter_no);
                    Map<String, Object> map = new HashMap<>();
                    map.put("new_meter_no", info.getNew_meter_no());
                    users.forEach(p -> {
                        if (!p.getMeter_no().equals(p.getSubmeter_no())) {
                            map.put("meter_no", p.getMeter_no());
                            userDao.updateMeterUserSubmeterNo(map);
                        }
                    });
                }
            } else {
                userDao.regist(user);
            }
            userCache.add(user);
            JoyServletContextListener.instanceCallback(userDao).changeMeterNo(params);
            params.put("user_status", "1");//正常
            params.put("meter_status", "4");//已换表
            userDao.updateStatus(params);
            result.setStatus(1);
        } catch (Exception e) {
            SQLException sqle = (SQLException) e.getCause();
            int sqlErrorCode = sqle.getErrorCode();
            if (sqlErrorCode == 1062) {
                result.setStatus(sqlErrorCode);
                result.setData(sqle.getMessage());
            } else {
                result.setStatus(0);
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return result;
    }

    /**
     * 更换费率类型
     *
     * @param user_name
     * @param id_card_no
     * @param fee_type
     * @param original_fee_type
     * @return
     */
    @Override
    public Result changeFeeType(String user_name, String id_card_no, String fee_type, String original_fee_type) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("user_name", user_name);
        params.put("id_card_no", id_card_no);
        params.put("fee_type", fee_type);
        try {
            userDao.changeFeeType(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 申请销户
     *
     * @param info
     * @return
     */
    @Override
    public Result applyRemoveMeter(UserManageInfo info) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("meter_no", info.getMeter_no());
        params.put("user_status", "0");//销户
        params.put("meter_status", "6");//申请销户
        try {
            userDao.addManageInfo(info);
            userDao.updateStatus(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 销户拆表
     *
     * @param info
     * @return
     */
    @Override
    public Result delMeter(UserManageInfo info) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("meter_no", info.getMeter_no());
        params.put("user_status", "0");//销户
        params.put("meter_status", "3");//已拆表
        try {
            userDao.addManageInfo(info);
            userDao.updateStatus(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 账户结算
     *
     * @param info
     * @return
     */
    @Override
    public Result saveAccount(UserManageInfo info) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("meter_no", info.getMeter_no());
        try {
            userDao.addManageInfo(info);
            User user = userDao.findUserByMeterNo(info.getMeter_no());
            double last_balance = user.getLast_balance();
            double closingCost = info.getClosingCost();
            if (last_balance >= 0) {
                last_balance = last_balance - closingCost;
            } else {
                last_balance = last_balance + closingCost;
            }
            params.put("last_balance", last_balance);
            userDao.updateAccount(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 销户
     *
     * @param info
     * @return
     */
    @Override
    public Result delUser(UserManageInfo info) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("meter_no", info.getMeter_no());
        params.put("user_status", "0");//销户
        params.put("meter_status", "5");//已销户
        try {
            userDao.addManageInfo(info);
            userDao.updateStatus(params);
            result.setStatus(1);
            // 如果采用的是JLAA协议,则向SaaS平台注册数据
            User user = userDao.findUserByMeterNo(info.getMeter_no());
            if (user != null) {
                if (user.getProtocol_type().toLowerCase().startsWith("jlaa")) {
                    saaSService.setParams(userDao, null);
                    saaSService.delete2SaaS(user);
                }
            }
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 更改用户信息
     *
     * @param user
     * @param original_user_name
     * @param original_user_address_area
     * @param original_user_address_community
     * @param original_user_address_building
     * @param original_user_address_unit
     * @param original_user_address_room
     * @param original_concentrator_name
     * @param original_supplier_name
     * @param original_contact_info
     * @param original_id_card_no
     * @param original_meter_type
     * @param original_meter_model
     * @param original_protocol_type
     * @param original_valve_protocol
     * @param original_meter_no
     * @param original_submeter_no
     * @param original_valve_no
     * @param original_pay_type
     * @param original_initing_data
     * @return
     */
    @Override
    public Result updateUser(User user,
            String original_user_name,
            String original_user_address_area,
            String original_user_address_community,
            String original_user_address_building,
            String original_user_address_unit,
            String original_user_address_room,
            String original_concentrator_name,
            String original_supplier_name,
            String original_contact_info,
            String original_id_card_no,
            String original_meter_type,
            String original_meter_model,
            String original_protocol_type,
            String original_valve_protocol,
            String original_meter_no,
            String original_submeter_no,
            String original_valve_no,
            String original_pay_type,
            Double original_initing_data) {
        Result result = new Result();
        try {
            if (user.getMeter_no() == null || user.getMeter_no().isEmpty()) {
                return result;
            }
            if (!original_meter_no.equals(user.getMeter_no())) {
                if (userDao.checkMeterNoExist(user.getMeter_no()) != null) {
                    result.setStatus(1062);
                    return result;
                }
            }
            user.setPay_remind(original_meter_no);//催缴提醒字段存放原表号
            String uuid = userCache.getUserByMeterNo(original_meter_no).getUser_address_original_room();
            user.setUser_address_original_room(uuid);
            JoyServletContextListener.instanceCallback(userDao).updateUser(user);
            userDao.modifyUserInfo(user);
            userCache.add(user);
            result.setStatus(1);
            // 如果采用的是JLAA协议,则向SaaS平台注册数据
            if (user.getProtocol_type().toLowerCase().startsWith("jlaa")) {
                user.setRegister_status(1);
                saaSService.setParams(userDao, null);
                saaSService.update2SaaS(user, original_meter_no);
            }
          //修改完用户信息同时将公众号相关的用户进行修改
            WechatApiUtil.sendUpdateUser(user,original_meter_no);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 更改用户信息
     *
     * @param user
     * @return
     */
    @Override
    public Result updateRoom(User user) {
        Result result = new Result();
        try {
            userDao.updateRoom(user);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 删除用户
     *
     * @param user_ids
     * @param user_names
     * @return
     */
    @Override
    public Result removeUser(String user_ids, String user_names) {
        Result result = new Result();
        String[] userIds = user_ids.split(",");
        int[] ids = new int[userIds.length];
        for (int i = 0; i < userIds.length; i++) {
            ids[i] = Integer.parseInt(userIds[i]);
        }
        try {
            String[] meterNos = userDao.findMeterNosByUserIDs(ids);
            userDao.removeReadInfoFailedByMeterNos(meterNos);
            userDao.removeRoomInfoByMeterNos(meterNos);
            userDao.removeUser(ids);
            userCache.remove(meterNos);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 查询用户管理信息
     *
     * @param user_name
     * @return
     */
    @Override
    public Result findUserManageInfo(String user_name) {
        Result result = new Result();
        try {
            List<SysLog> userManageInfo = userDao.findUserManageInfo(user_name);
            result.setStatus(1);
            result.setData(userManageInfo);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 加载供水网点
     *
     * @param province
     * @param city
     * @param district
     * @param startNo
     * @param pageSize
     * @return
     */
    @Override
    public Result loadSuppliers(String province, String city, String district,
            int startNo, int pageSize) {
        Result result = new Result();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("province", province);
            params.put("city", city);
            params.put("district", district);
            params.put("startNo", startNo);
            params.put("pageSize", pageSize);
            List<WaterSupplier> suppliers = userDao.loadSuppliers(params);
            int totalNo = userDao.findSuppliersNo();
            params.clear();
            params.put("suppliers", suppliers);
            params.put("totalNo", totalNo);
            result.setStatus(1);
            result.setData(params);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 保存供水网点
     *
     * @param original_supplier_name
     * @param original_province
     * @param original_city
     * @param original_district
     * @param original_supplier_address
     * @param supplier
     * @return
     */
    @Override
    public Result saveWaterSupplier(String original_supplier_name,
            String original_province,
            String original_city,
            String original_district,
            String original_supplier_address,
            WaterSupplier supplier) {
        Result result = new Result();
        Timestamp add_time = new Timestamp(System.currentTimeMillis());
        supplier.setAdd_time(add_time);
        try {
            userDao.saveWaterSupplier(supplier);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 删除供水网点
     *
     * @param supplier_id
     * @param supplier_name
     * @return
     */
    @Override
    public Result delWaterSupplier(int supplier_id, String supplier_name) {
        Result result = new Result();
        try {
            userDao.delWaterSupplier(supplier_id);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 增加供水网点
     *
     * @param supplier
     * @return
     */
    @Override
    public Result addWaterSupplier(WaterSupplier supplier) {
        Result result = new Result();
        try {
            userDao.addWaterSupplier(supplier);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 加载小区信息
     *
     * @return
     */
    @Override
    public Result loadCommunity() {
        Result result = new Result();
        try {
            List<Community> communities = userDao.loadCommunity();
            result.setStatus(1);
            result.setData(communities);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 添加小区
     *
     * @param community
     * @return
     */
    @Override
    public Result addCommunity(Community community) {
        Result result = new Result();
        try {
            userDao.addCommunity(community);
            result.setStatus(1);
        } catch (Exception e) {
            SQLException sqle = (SQLException) e.getCause();
            int sqlErrorCode = sqle.getErrorCode();
            if (sqlErrorCode == 1062) {
                result.setStatus(sqlErrorCode);
                result.setData(sqle.getMessage());
            } else {
                result.setStatus(0);
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return result;
    }

    /**
     * 查询小区的用户数
     *
     * @param community_name
     * @return
     */
    @Override
    public Result checkCommunity(String community_name) {
        Result result = new Result();
        try {
            int no = userDao.checkCommunity(community_name);
            if (no > 0) {
                result.setStatus(1);
            }
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 修改小区信息
     *
     * @param check
     * @param community_id
     * @param province
     * @param city
     * @param district
     * @param community_name
     * @param new_province
     * @param new_city
     * @param new_district
     * @param new_community_name
     * @param new_community_no
     * @return
     */
    @Override
    public Result modifyCommunity(boolean check,
            String community_id,
            String province,
            String city,
            String district,
            String community_name,
            String new_province,
            String new_city,
            String new_district,
            String new_community_name,
            String new_community_no) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("community_id", community_id);
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        params.put("community_name", community_name);
        params.put("new_province", new_province);
        params.put("new_city", new_city);
        params.put("new_district", new_district);
        params.put("new_community_name", new_community_name);
        params.put("new_community_no", new_community_no);
        try {
            if (check == true) {
                userDao.changeCommunity(params);
            }
            userDao.modifyCommunity(params);
            userDao.modifyBuilding(params);
            userDao.modifyUnit(params);
            userDao.changeConcentrator(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            SQLException sqle = (SQLException) e.getCause();
            int sqlErrorCode = sqle.getErrorCode();
            if (sqlErrorCode == 1062) {
                result.setStatus(sqlErrorCode);
                result.setData(sqle.getMessage());
            } else {
                result.setStatus(0);
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return result;
    }

    /**
     * 删除小区
     *
     * @param province
     * @param city
     * @param district
     * @param community_name
     * @return
     */
    @Override
    public Result delCommunity(String province, String city, String district, String community_name) {
        Result result = new Result();
        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        params.put("community_name", community_name);
        try {
            int no = userDao.checkCommunity(community_name);
            if (no == 0) {
                userDao.delCommunity(params);
                userDao.delBuilding(params);
                userDao.delUnit(params);
                result.setStatus(1);
            } else {
                result.setStatus(2);
            }
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 加载楼栋信息
     *
     * @param province
     * @param city
     * @param district
     * @param community_name
     * @return
     */
    @Override
    public Result loadBuilding(String province, String city, String district, String community_name) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        params.put("community_name", community_name);
        try {
            List<Building> buildings = userDao.loadBuilding(params);
            result.setStatus(1);
            result.setData(buildings);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 修改楼栋信息
     *
     * @param province
     * @param city
     * @param district
     * @param communityName
     * @param buildingName
     * @param newBuildingName
     * @return
     */
    @Override
    public Result modifyBuilding(String province, String city, String district, String communityName, String buildingName, String newBuildingName) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        params.put("community_name", communityName);
        params.put("building_name", buildingName);
        params.put("new_building_name", newBuildingName);
        try {
            userDao.modifyBuilding(params);
            userDao.modifyUnit(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 删除楼栋信息
     *
     * @param province
     * @param city
     * @param district
     * @param community_name
     * @param building_name
     * @return
     */
    @Override
    public Result delBuilding(String province, String city, String district, String community_name, String building_name) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        params.put("community_name", community_name);
        params.put("building_name", building_name);
        try {
            userDao.delBuilding(params);
            userDao.delUnit(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 增加楼栋信息
     *
     * @param building
     * @return
     */
    @Override
    public Result addBuilding(Building building) {
        Result result = new Result();
        try {
            userDao.addBuilding(building);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 加载单元信息
     *
     * @param province
     * @param city
     * @param district
     * @param community_name
     * @param building_name
     * @return
     */
    @Override
    public Result loadUnit(String province, String city, String district, String community_name, String building_name) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        params.put("community_name", community_name);
        params.put("building_name", building_name);
        try {
            List<Unit> units = userDao.loadUnit(params);
            result.setStatus(1);
            result.setData(units);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 修改单元信息
     *
     * @param province
     * @param city
     * @param district
     * @param communityName
     * @param buildingName
     * @param unitName
     * @param newUnitName
     * @return
     */
    @Override
    public Result modifyUnit(String province,
            String city,
            String district,
            String communityName,
            String buildingName,
            String unitName,
            String newUnitName) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        params.put("community_name", communityName);
        params.put("building_name", buildingName);
        params.put("unit_name", unitName);
        params.put("new_unit_name", newUnitName);
        try {
            userDao.modifyUnit(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 删除单元信息
     *
     * @param province
     * @param city
     * @param district
     * @param community_name
     * @param building_name
     * @param unit_name
     * @return
     */
    @Override
    public Result delUnit(String province,
            String city,
            String district,
            String community_name,
            String building_name,
            String unit_name) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        params.put("community_name", community_name);
        params.put("building_name", building_name);
        params.put("unit_name", unit_name);
        try {
            userDao.delUnit(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 增加单元信息
     *
     * @param unit
     * @return
     */
    @Override
    public Result addUnit(Unit unit) {
        Result result = new Result();
        try {
            userDao.addUnit(unit);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 加载集中器信息
     *
     * @param community_name
     * @param building_name
     * @param unit_name
     * @param startNo
     * @param pageSize
     * @return
     */
    @Override
    public Result loadConcentrators(String community_name,
            String building_name,
            String unit_name,
            int startNo, int pageSize) {
        Result result = new Result();
        HashMap<String, Object> params = new HashMap<>();
        params.put("community_name", community_name);
        params.put("building_name", building_name);
        params.put("unit_name", unit_name);
        params.put("startNo", startNo);
        params.put("pageSize", pageSize);
        try {
            List<Concentrator> concentrators = userDao.loadConcentrators(params);
            int totalNo = userDao.findConcentratorNo();
            params.clear();
            params.put("concentrators", concentrators);
            params.put("totalNo", totalNo);
            result.setStatus(1);
            result.setData(params);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 更新集中器在线状态
     *
     * @param mbus_id
     * @param mbus_status
     * @return
     */
    @Override
    public Result updateMbusStatus(String mbus_id, String mbus_status) {
        Result result = new Result();
        HashMap<String, Object> params = new HashMap<>();
        params.put("mbus_id", mbus_id);
        params.put("mbus_status", mbus_status);
        try {
            userDao.updateMbusStatus(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 查询所有的小区信息
     *
     * @param province
     * @param city
     * @param district
     * @return
     */
    @Override
    public Result findAllCommunities(String province, String city, String district) {
        Result result = new Result();
        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        try {
            List<Community> communities = userDao.findAllCommunities(params);
            result.setStatus(1);
            result.setData(communities);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Result addConcentratorValidate(String concentrator_no) {
        Result result = new Result();
        try {
            List<Concentrator> con = userDao.findConcentrators(concentrator_no);
            if (con == null || con.isEmpty()) {// 表示数据库不存在该集中器编号
                result.setStatus(1);
            } else {// 表示数据库已存在该集中器编号
                result.setStatus(0);
            }
        } catch (Exception e) {// 表示数据库访问异常
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setStatus(2);
        }
        return result;
    }

    /**
     * 添加集中器信息
     *
     * @param concentrator
     * @param req
     * @return
     */
    @Override
    public Result addConcentrator(Concentrator concentrator, HttpServletRequest req) {
        Result result = new Result();
        try {
            String concentrator_no = concentrator.getConcentrator_no();
            List<Concentrator> con = userDao.findConcentrators(concentrator_no);
            if (con == null || con.isEmpty()) {
                userDao.addConcentrator(concentrator);
            } else {
                String concentrator_model = con.get(0).getConcentrator_model();
                concentrator.setConcentrator_model(concentrator_model);
                userDao.addConcentrator(concentrator);
            }

            String gateway_id = concentrator.getGateway_id();
            String postUrl = "http://" + concentrator.getConcentrator_ip() + ":" + concentrator.getConcentrator_port();
            postUrl += "/arm/api/registerGateway";
            String url = JoyServletContextListener.GetUrl();
            String account = concentrator.getOperator_account();
            String param = "{DTU: [{"
                    + "id:'" + gateway_id + "',"
                    + "url:'" + url + "',"
                    + "options:{isAutoClear: '0',accountName: '" + account + "'}" + "}]}";
            HttpRequest.sendPost(postUrl, param);
            result.setStatus(1);
        } catch (Exception e) {
            SQLException sqle = (SQLException) e.getCause();
            int sqlErrorCode = sqle.getErrorCode();
            if (sqlErrorCode == 1062) {
                result.setStatus(sqlErrorCode);
                result.setData(sqle.getMessage());
                result.setStatus(2);
            } else {
                result.setStatus(0);
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return result;
    }

    /**
     * 修改集中器信息
     *
     * @param original_concentrator_name
     * @param original_concentrator_no
     * @param original_gateway_id
     * @param original_concentrator_ip
     * @param original_concentrator_port
     * @param original_DTU_sim_no
     * @param original_concentrator_model
     * @param original_community
     * @param original_building
     * @param original_unit
     * @param concentrator
     * @return
     */
    @Override
    public Result modifyConcentrator(String original_concentrator_name,
            String original_concentrator_no, String original_gateway_id, String original_concentrator_ip,
            String original_concentrator_port, String original_DTU_sim_no, String original_concentrator_model,
            String original_community, String original_building,
            String original_unit, Concentrator concentrator) {
        Result result = new Result();
        try {
            String concentrator_no = concentrator.getConcentrator_no();
            List<Concentrator> con = userDao.findConcentrators(concentrator_no);
            if (con == null || con.isEmpty()) {
                userDao.modifyConcentrator(concentrator);
            } else {
                userDao.updateConcentrators(concentrator);
                userDao.modifyConcentrator(concentrator);
            }
            String gateway_id = concentrator.getGateway_id();
            if (original_gateway_id == null ? gateway_id != null : !original_gateway_id.equals(gateway_id)) {
                String postUrl = "http://" + concentrator.getConcentrator_ip() + ":" + concentrator.getConcentrator_port();
                postUrl += "/arm/api/registerGateway";
                String url = JoyServletContextListener.GetUrl();
                String account = concentrator.getOperator_account();
                String param = "{DTU: [{"
                        + "id:'" + gateway_id + "',"
                        + "url:'" + url + "',"
                        + "options:{isAutoClear: '0',accountName: '" + account + "'}" + "}]}";
                HttpRequest.sendPost(postUrl, param);
            }
            result.setStatus(1);
        } catch (Exception e) {
            SQLException sqle = (SQLException) e.getCause();
            int sqlErrorCode = sqle.getErrorCode();
            if (sqlErrorCode == 1062) {
                result.setStatus(sqlErrorCode);
                result.setData(sqle.getMessage());
                result.setStatus(2);
            } else {
                result.setStatus(0);
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return result;
    }

    /**
     * 删除集中器信息
     *
     * @param concentrator_id
     * @param concentrator_name
     * @return
     */
    @Override
    public Result delConcentrator(int concentrator_id, String concentrator_name) {
        Result result = new Result();
        try {
            // 检查集中器是否处于使用状态
            int no = userDao.findConcentratorStatus(concentrator_name);
            if (no == 0) {
                userDao.delConcentrator(concentrator_id);
                result.setStatus(1);
            } else {
                result.setStatus(2);
            }
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 加载费率类型
     *
     * @return
     */
    @Override
    public Result load_feeTypes() {
        Result result = new Result();
        try {
            List<FeeType> feeTypes = userDao.load_feeTypes();
            result.setStatus(1);
            result.setData(feeTypes);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 添加费率类型
     *
     * @param feeType
     * @return
     */
    @Override
    public Result addFeeType(FeeType feeType) {
        Result result = new Result();
        try {
            userDao.addFeeType(feeType);
            feeTypeCache.add(feeType);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 根据id查询费率类型
     *
     * @param feeTypeId
     * @return
     */
    @Override
    public Result findFeeTypeById(int feeTypeId) {
        Result result = new Result();
        try {
            FeeType feeType = userDao.findFeeTypeById(feeTypeId);
            result.setStatus(1);
            result.setData(feeType);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 修改费率类型
     *
     * @param feeType
     * @param original_feeTypeName
     * @param original_meterType
     * @param original_basicUnitPrice
     * @param original_disposingUnitCost
     * @param original_otherCost
     * @param original_extraCost
     * @param original_paymentMethod
     * @param original_isLevelPrice
     * @param original_levelOneStartVolume
     * @param original_levelOneUnitPrice
     * @param original_levelTwoStartVolume
     * @param original_levelTwoUnitPrice
     * @param original_levelThreetartVolume
     * @param original_levelThreeUnitPrice
     * @param original_levelFourStartVolume
     * @param original_levelFourUnitPrice
     * @param original_levelFiveStartVolume
     * @param original_levelFiveUnitPrice
     * @return
     */
    @Override
    public Result modifyFeeType(FeeType feeType,
            String original_feeTypeName,
            String original_meterType,
            Double original_basicUnitPrice,
            Double original_disposingUnitCost,
            Double original_otherCost,
            Double original_extraCost,
            String original_paymentMethod,
            String original_isLevelPrice,
            Double original_levelOneStartVolume,
            Double original_levelOneUnitPrice,
            Double original_levelTwoStartVolume,
            Double original_levelTwoUnitPrice,
            Double original_levelThreetartVolume,
            Double original_levelThreeUnitPrice,
            Double original_levelFourStartVolume,
            Double original_levelFourUnitPrice,
            Double original_levelFiveStartVolume,
            Double original_levelFiveUnitPrice) {
        Result result = new Result();
        try {
            userDao.modifyFeeType(feeType);
            feeTypeCache.add(feeType);
            //修改费率名称时，用户中的费率名称也要修改
            Map<String,Object>params = new HashMap<>();
            params.put("fee_type", feeType.getFeeTypeName());
            params.put("original_fee_type", original_feeTypeName);
            userDao.modifyMtertUserFeeType(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 删除费率类型
     *
     * @param feeTypeId
     * @param feeTypeName
     * @return
     */
    @Override
    public Result delFeeType(int feeTypeId, String feeTypeName) {
        Result result = new Result();
        try {
            userDao.delFeeType(feeTypeId);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 加载住房系数信息
     *
     * @return
     */
    @Override
    public Result load_house_coefficient() {
        Result result = new Result();
        try {
            List<HouseCoefficient> coes = userDao.load_house_coefficient();
            result.setStatus(1);
            result.setData(coes);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 查询房屋系数
     *
     * @param coefficient
     * @return
     */
    @Override
    public Result checkCoefficient(HouseCoefficient coefficient) {
        Result result = new Result();
        try {
            int no1 = userDao.checkCoefficient(coefficient
                    .getCoefficient_name());
            int no2 = userDao.findCoefficient(coefficient.getCoefficient());
            if (no1 == 0 && no2 == 0) {
                result.setStatus(1);
            } else if (no1 == 0 && no2 != 0) {
                result.setStatus(2);
            } else if (no1 != 0 && no2 == 0) {
                result.setStatus(3);
            } else if (no1 != 0 && no2 != 0) {
                result.setStatus(4);
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 添加房屋系数
     *
     * @param coefficient
     * @return
     */
    @Override
    public Result addCoefficient(HouseCoefficient coefficient) {
        Result result = new Result();
        try {
            userDao.addCoefficient(coefficient);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 修改房屋系数
     *
     * @param coefficient_id
     * @param coefficient_name
     * @param coefficient
     * @return
     */
    @Override
    public Result modifyCoefficient(int coefficient_id,
            String coefficient_name, String coefficient) {
        Result result = new Result();
        HashMap<String, Object> params = new HashMap<>();
        params.put("coefficient_id", coefficient_id);
        params.put("coefficient_name", coefficient_name);
        params.put("coefficient", coefficient);
        try {
            userDao.modifyCoefficient(params);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 删除房屋系数
     *
     * @param coefficient_name
     * @return
     */
    @Override
    public Result delCoefficient(String coefficient_name) {
        Result result = new Result();
        try {
            userDao.delCoefficient(coefficient_name);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 打印报告
     *
     * @param request
     * @param response
     * @param operate_id
     * @param user_name
     * @param meter_no
     * @param operate_money
     * @param pay_method
     * @param operate_type
     * @param fee_type
     * @param operate_time
     * @param operator_account
     * @return
     * @throws IOException
     */
    @Override
    public Result printReport(HttpServletRequest request,
            HttpServletResponse response,
            String operate_id,
            String user_name,
            String meter_no,
            String operate_money,
            String pay_method,
            String operate_type,
            String fee_type,
            String operate_time,
            String operator_account) throws IOException {
        Result result = new Result();
        String path = request.getServletContext().getRealPath("/");
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("operate_id", operate_id);
        hm.put("user_name", user_name);
        hm.put("meter_no", meter_no);
        hm.put("operate_money", operate_money);
        hm.put("pay_method", pay_method);
        hm.put("operate_type", operate_type);
        hm.put("fee_type", fee_type);
        hm.put("operate_time", operate_time);
        hm.put("operator_account", operator_account);
        try {
            JasperReport jasperReport = (JasperReport) JRLoader
                    .loadObjectFromLocation(path + "reports/printReport.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, hm, new JREmptyDataSource());
            JasperViewer view = new JasperViewer(jasperPrint, false);
            view.setVisible(true);
            JasperPrintManager.printReport(jasperPrint, true);
            JasperExportManager.exportReportToPdfStream(jasperPrint, response
                    .getOutputStream());
            result.setStatus(1);
        } catch (JRException | IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        response.getOutputStream().flush();
        response.getOutputStream().close();
        response.flushBuffer();
        return result;
    }

    /**
     * 业务统计
     *
     * @param province
     * @param city
     * @param district
     * @param beginDate
     * @param endDate
     * @return
     */
    @Override
    public Result businessQueries(String province, String city, String district, String beginDate, String endDate) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        try {
            int rechargeNo = userDao.findRechargeNo1(params);
            int refundNo = userDao.findRefundNo1(params);
            int changeMeterNo = userDao.findChangeMeterNo1(params);
            int delUserNo = userDao.findDelUserNo1(params);
            map.put("rechargeNo", rechargeNo);
            map.put("refundNo", refundNo);
            map.put("changeMeterNo", changeMeterNo);
            map.put("delUserNo", delUserNo);
            result.setStatus(1);
            result.setData(map);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 充值退费统计
     *
     * @param province
     * @param city
     * @param district
     * @param beginDate
     * @param endDate
     * @return
     */
    @Override
    public Result rechargeRefundAnalyze(String province, String city, String district, String beginDate, String endDate) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        try {
            Double rechargeTotal = userDao.findRechargeTotal2(params);
            Double refundTotal = userDao.findRefundTotal2(params);
            map.put("rechargeTotal", rechargeTotal);
            map.put("refundTotal", refundTotal);
            result.setStatus(1);
            result.setData(map);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 语言切换
     *
     * @param req
     * @param language
     * @return
     */
    @Override
    public Result saveLanguage(HttpServletRequest req, String language) {
        Result result = new Result();
        try {
            VelocityToHtml.generateAllHtml(req, language);
            VelocityToHtml.generateAllJs(req, language);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     *
     * @return
     */
    @Override
    public Result findLanguage() {
        Result result = new Result();
        try {
            String language = userDao.findLanguage();
            result.setStatus(1);
            result.setData(language);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     *
     * @param community_name
     * @param building_name
     * @param unit_name
     * @return
     */
    @Override
    public Result findRoomsByAddress(String community_name,
            String building_name, String unit_name) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("community_name", community_name);
        params.put("building_name", building_name);
        params.put("unit_name", unit_name);
        try {
            String[] rooms = userDao.findRoomsByAddress(params);
            result.setData(rooms);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 根据小区编号、房间号、表类型查询表号
     *
     * @param community_no
     * @param user_address_room
     * @param meter_type
     * @return
     */
    @Override
    public Result findMetersByRoomNo(String community_no,
            String user_address_room,
            String meter_type) {
        Result result = new Result();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("community_no", community_no);
            params.put("user_address_room", user_address_room);
            params.put("meter_type", meter_type);
            List<User> meters = userDao.findMetersByRoomNo(params);
            result.setData(meters);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 根据小区名称、房间号、表类型查询表号
     *
     * @param community_name
     * @param user_address_room
     * @param meter_type
     * @return
     */
    @Override
    public Result findMetersByRoomName(String community_name,
            String user_address_room,
            String meter_type) {
        Result result = new Result();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("community_name", community_name);
            params.put("user_address_room", user_address_room);
            params.put("meter_type", meter_type);
            List<User> meters = userDao.findMetersByRoomName(params);
            result.setData(meters);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 根据小区编号查询用户信息
     *
     * @param community_no
     * @return
     */
    @Override
    public Result findUsersByRoom(String community_no) {
        Result result = new Result();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("community_no", community_no);
            List<User> users = userDao.findUsersByRoom(params);
            result.setData(users);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 根据楼栋名称查询房间号及表号
     *
     * @param buildName
     * @return
     */
    @Override
    public Result findRoomAndMeterNoByBulid(String buildName) {
        Result result = new Result();
        try {
            List<User> listUsers = userDao.findRoomAndMeter_noByBulid(buildName);
            if (listUsers == null || listUsers.isEmpty()) {
                return result;
            }
            List<Room> list = new ArrayList<>();
            listUsers.forEach(p -> list.add(new Room(p.getUser_address_room(), p.getMeter_no(), p.getMeter_type())));
            result.setData(list);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 注册房源信息
     *
     * @param jsonParams
     * @return
     */
    @Override
    public Result registRoomInfo(String jsonParams) {
        Result result = new Result();
        try {
            List<RoomInfo> roomInfo = JSONArray.parseArray(jsonParams, RoomInfo.class);
            userDao.registRoomInfo(roomInfo);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * 根据表号查询用户信息
     *
     * @param meterNo
     * @return
     */
    @Override
    public Result findUserByMeterNo(String meterNo) {
        Result result = new Result();
        try {
            User user = userDao.findUserByMeterNo(meterNo);
            result.setStatus(1);
            result.setData(user);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Map<String, Object> elemeter_basic_info(final String uuid) {
        Map<String, Object> meterInfo = new HashMap<>();
        Map<String, Object> versions = new HashMap<>();
        meterInfo.put("code", "1");
        meterInfo.put("message", "fail");
        meterInfo.put("reqId", String.valueOf(JoyDatetime.getID()));
        try {
            User user = userDao.getMeterByUUID(uuid);
            meterInfo.put("message", "用户不存在");
            if (user != null) {
                String eventOpClo = "1".equals(user.getMeter_status()) ? "2001" : "1004";
                EventRecord event = eventRecordCache.getEventRecord(user.getMeter_no(), eventOpClo);
                ReadInfo readInfo = readInfoCache.findLastReadInfoByMeterNo(user.getMeter_no());
                meterInfo.put("code", "0");
                meterInfo.put("message", "successful");
                meterInfo.put("mac", "");
                meterInfo.put("sn", user.getMeter_no());
                meterInfo.put("uuid", uuid);
                meterInfo.put("elecollector_uuid", "");
                meterInfo.put("bind_time", user.getAdd_time().getTime() / 1000);
                meterInfo.put("onoff_time ", event == null ? System.currentTimeMillis()/1000 : event.getReport_time().getTime() / 1000);
                meterInfo.put("onoff_line ", Integer.valueOf(user.getMeter_status()));
                meterInfo.put("name", "智能电表");
                meterInfo.put("model", "");
                meterInfo.put("model_name", "");
                meterInfo.put("brand", "杭州超仪电表");
                meterInfo.put("enable_state", user.getValve_status());
                meterInfo.put("capacity", "");
                meterInfo.put("curr_amount", readInfo == null ? 0.0f :readInfo.getThis_read());
                meterInfo.put("surplus", "");
                meterInfo.put("operation", 3);
                meterInfo.put("operation_stage", 3);
                meterInfo.put("trans_status ", "");
                meterInfo.put("switch", "1".equals(user.getValve_status()) ? 1 : 2);
                meterInfo.put("switch_stage ", 3);
                versions.put("protocol_version", "1.0");
                versions.put("potocol_type", user.getProtocol_type());
                meterInfo.put("versions", versions);
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return meterInfo;
    }
}
