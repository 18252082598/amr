package com.joymeter.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import javax.annotation.Resource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.Admin;
import com.joymeter.entity.Building;
import com.joymeter.entity.Community;
import com.joymeter.entity.Concentrator;
import com.joymeter.entity.FeeType;
import com.joymeter.entity.OperateInfo;
import com.joymeter.entity.ReadParameter;
import com.joymeter.entity.Role;
import com.joymeter.entity.SysLog;
import com.joymeter.entity.Unit;
import com.joymeter.entity.User;
import com.joymeter.entity.UserManageInfo;
import com.joymeter.entity.WaterSupplier;

public class Transaction {

    @Resource
    private UserDao userDao;

    public void beginTransaction(JoinPoint join) {

    }

    @SuppressWarnings("unused")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        if (point == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Calendar ca = Calendar.getInstance();
        String operateDate = df.format(ca.getTime());
        Subject currentUser = SecurityUtils.getSubject();
        String loginName;
        loginName = (String) currentUser.getPrincipal();
        String methodName = point.getSignature().getName();
        Object[] method_param;
        Object object;
        method_param = point.getArgs();
        object = point.proceed();
        SysLog sysLog = new SysLog();
        switch (methodName) {
            case "checkLogin":
                String loginAdmin_account = (String) method_param[0];
                sysLog.setOperatingcontent("管理员登录  " + loginAdmin_account);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginAdmin_account);
                System.out.println(sysLog.toString());
                userDao.setOperate(sysLog);
                HashMap<String, Object> params = new HashMap<>();
                params.put("loginAdmin_account", loginAdmin_account);
                params.put("operateDate", operateDate);
                userDao.updateAdminLoadTime(params);
                break;
            case "adminRegist":
                Admin newAdmin = (Admin) method_param[0];
                sysLog.setOperatingcontent("新增管理员  " + newAdmin.getAdmin_account());
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            case "modifyAdmin": {
                Admin admin = (Admin) method_param[0];
                String original_admin_name = (String) method_param[1];
                String original_admin_tel = (String) method_param[2];
                String original_admin_supplier_name = (String) method_param[3];
                String original_admin_password = (String) method_param[4];
                String original_admin_power = (String) method_param[5];
                String original_account_status = (String) method_param[6];
                String admin_name_msg;
                String admin_tel_msg;
                String admin_supplier_name_msg;
                String admin_password_msg;
                String admin_power_msg;
                String account_status_msg;
                if (admin.getAdmin_name().equals(original_admin_name)) {
                    admin_name_msg = "管理员名称：" + original_admin_name + "  ";
                } else {
                    admin_name_msg = "管理员名称：" + original_admin_name + "->" + admin.getAdmin_name() + "  ";
                }
                if (admin.getAdmin_tel().equals(original_admin_tel)) {
                    admin_tel_msg = "  ";
                } else {
                    admin_tel_msg = "管理员联系方式：" + original_admin_tel + "->" + admin.getAdmin_tel() + "  ";
                }
                if (admin.getAdmin_supplier_name().equals(original_admin_supplier_name)) {
                    admin_supplier_name_msg = "  ";
                } else {
                    admin_supplier_name_msg = "管理员所属网点：" + original_admin_supplier_name + "->" + admin.getAdmin_supplier_name() + "  ";
                }
                if (admin.getAdmin_password().equals(original_admin_password)) {
                    admin_password_msg = "  ";
                } else {
                    admin_password_msg = "修改密码  ";
                }
                if (admin.getAdmin_power().equals(original_admin_power)) {
                    admin_power_msg = "  ";
                } else {
                    admin_power_msg = "管理员权限：" + original_admin_power + "->" + admin.getAdmin_power() + "  ";
                }
                if (admin.getAccount_status().equals(original_account_status)) {
                    account_status_msg = "  ";
                } else {
                    account_status_msg = "账户状态：" + original_account_status + "->" + admin.getAccount_status() + "  ";
                }
                sysLog.setOperatingcontent("修改管理员  管理员账号：" + admin.getAdmin_account() + "  " + admin_name_msg + admin_tel_msg + admin_supplier_name_msg + admin_password_msg + admin_power_msg + account_status_msg);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "delAdmin":
                String delAdmin_account = (String) method_param[0];
                sysLog.setOperatingcontent("删除管理员  " + delAdmin_account);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            case "logout":
                //String delAdmin_account= (String) method_param[0];
                sysLog.setOperatingcontent("管理员退出  " + loginName);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            case "regist":
                User newUser = (User) method_param[0];
                sysLog.setOperatingcontent("注册用户  id" + newUser.getUser_id() + "  用户名" + newUser.getUser_name());
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            case "updateUser": {
                User user = (User) method_param[0];
                String original_user_name = (String) method_param[1];
                String original_user_address_area = (String) method_param[2];
                String original_user_address_community = (String) method_param[3];
                String original_user_address_building = (String) method_param[4];
                String original_user_address_unit = (String) method_param[5];
                String original_user_address_room = (String) method_param[6];
                String original_concentrator_name = (String) method_param[7];
                String original_supplier_name = (String) method_param[8];
                String original_contact_info = (String) method_param[9];
                String original_id_card_no = (String) method_param[10];
                String original_meter_type = (String) method_param[11];
                String original_meter_model = (String) method_param[12];
                String original_protocol_type = (String) method_param[13];
                String original_valve_protocol = (String) method_param[14];
                String original_meter_no = (String) method_param[15];
                String original_submeter_no = (String) method_param[16];
                String original_valve_no = (String) method_param[17];
                Double original_initing_data = (Double) method_param[19];
                String user_name_msg;
                String user_address_area_msg;
                String user_address_community_msg;
                String user_address_building_msg;
                String user_address_unit_msg;
                String user_address_room_msg;
                String concentrator_name_msg;
                String supplier_name_msg;
                String contact_info_msg;
                String id_card_no_msg;
                String meter_type_msg;
                String meter_model_msg;
                String protocol_type_msg;
                String valve_protocol_msg = "";
                String meter_no_msg;
                String submeter_no_msg;
                String valve_no_msg;
                String initing_data;
                if (user.getUser_name().equals(original_user_name)) {
                    user_name_msg = "用户名称：" + original_user_name + "  ";
                } else {
                    user_name_msg = "用户名称：" + original_user_name + "->" + user.getUser_name() + "  ";
                }
                if (user.getUser_address_area().equals(original_user_address_area)) {
                    user_address_area_msg = "  ";
                } else {
                    user_address_area_msg = "地址：" + original_user_address_area + "->" + user.getUser_address_area() + "  ";
                }
                if (user.getUser_address_community().equals(original_user_address_community)) {
                    user_address_community_msg = "  ";
                } else {
                    user_address_community_msg = "小区：" + original_user_address_community + "->" + user.getUser_address_community() + "  ";
                }
                if (user.getUser_address_building().equals(original_user_address_building)) {
                    user_address_building_msg = "  ";
                } else {
                    user_address_building_msg = "楼栋：" + original_user_address_building + "->" + user.getUser_address_building() + "  ";
                }
                if (user.getUser_address_unit().equals(original_user_address_unit)) {
                    user_address_unit_msg = "  ";
                } else {
                    user_address_unit_msg = "单元：" + original_user_address_unit + "->" + user.getUser_address_unit() + "  ";
                }
                if (user.getUser_address_room().equals(original_user_address_room)) {
                    user_address_room_msg = "";
                } else {
                    user_address_room_msg = "房号：" + original_user_address_room + "->" + user.getUser_address_room() + "  ";
                }
                if (user.getConcentrator_name().equals(original_concentrator_name)) {
                    concentrator_name_msg = "";
                } else {
                    concentrator_name_msg = "集中器：" + original_concentrator_name + "->" + user.getConcentrator_name() + "  ";
                }
                if (user.getSupplier_name().equals(original_supplier_name)) {
                    supplier_name_msg = "  ";
                } else {
                    supplier_name_msg = "网点：" + original_supplier_name + "->" + user.getSupplier_name() + "  ";
                }
                if (user.getContact_info().equals(original_contact_info)) {
                    contact_info_msg = "";
                } else {
                    contact_info_msg = "联系方式：" + original_contact_info + "->" + user.getContact_info() + "  ";
                }
                if (user.getId_card_no().equals(original_id_card_no)) {
                    id_card_no_msg = "";
                } else {
                    id_card_no_msg = "身份证号：" + original_id_card_no + "->" + user.getId_card_no() + "  ";
                }
                if (user.getMeter_type().equals(original_meter_type)) {
                    meter_type_msg = "";
                } else {
                    meter_type_msg = "仪表类型：" + original_meter_type + "->" + user.getMeter_type() + "  ";
                }
                if (user.getMeter_model().equals(original_meter_model)) {
                    meter_model_msg = "";
                } else {
                    meter_model_msg = "仪表型号：" + original_meter_model + "->" + user.getMeter_model() + "  ";
                }
                if (user.getProtocol_type().equals(original_protocol_type)) {
                    protocol_type_msg = "";
                } else {
                    protocol_type_msg = "协议类型：" + original_protocol_type + "->" + user.getProtocol_type() + "  ";
                }
                if (!user.getValve_protocol().equals(original_valve_protocol)) {
                    valve_protocol_msg = "阀门协议：" + original_valve_protocol + "->" + user.getValve_protocol() + "  ";
                }
                if (user.getMeter_no().equals(original_meter_no)) {
                    meter_no_msg = "";
                } else {
                    meter_no_msg = "表号：" + original_meter_no + "->" + user.getMeter_no() + "  ";
                }
                if (user.getSubmeter_no().equals(original_submeter_no)) {
                    submeter_no_msg = "";
                } else {
                    submeter_no_msg = "子表号：" + original_submeter_no + "->" + user.getSubmeter_no() + "  ";
                }
                if (user.getValve_no().equals(original_valve_no)) {
                    valve_no_msg = "";
                } else {
                    valve_no_msg = "阀门编号：" + original_valve_no + "->" + user.getValve_no() + "  ";
                }
                if (user.getIniting_data().equals(original_initing_data)) {
                    initing_data = "";
                } else {
                    initing_data = "表初始读数：" + original_initing_data + "->" + user.getIniting_data() + "  ";
                }
                sysLog.setOperatingcontent("修改用户  id：" + user.getUser_id() + "  "
                        + user_name_msg
                        + user_address_area_msg
                        + user_address_community_msg
                        + user_address_building_msg
                        + user_address_unit_msg
                        + user_address_room_msg
                        + concentrator_name_msg
                        + supplier_name_msg
                        + contact_info_msg
                        + id_card_no_msg
                        + meter_type_msg
                        + meter_model_msg
                        + protocol_type_msg
                        + meter_no_msg
                        + submeter_no_msg
                        + valve_no_msg
                        + initing_data);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                sysLog.setUserName(user.getUser_name());
                userDao.setOperate(sysLog);
                break;
            }
            case "removeUser":
                String removeUser_id = (String) method_param[0];
                String removeUser_name = (String) method_param[1];
                sysLog.setOperatingcontent("删除用户  id" + removeUser_id + "用户名" + removeUser_name);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                sysLog.setUserName(removeUser_name);
                userDao.setOperate(sysLog);
                break;
            case "upload":
                sysLog.setOperatingcontent("导入用户");
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            case "applyChangeMeter": {
                UserManageInfo info = (UserManageInfo) method_param[0];
                sysLog.setOperatingcontent("用户名：" + info.getUser_name() + "申请换表");
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                sysLog.setUserName(info.getUser_name());
                userDao.setOperate(sysLog);
                break;
            }
            case "removeMeter": {
                UserManageInfo info = (UserManageInfo) method_param[0];
                sysLog.setOperatingcontent("用户名：" + info.getUser_name() + "  换表已拆表");
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                sysLog.setUserName(info.getUser_name());
                userDao.setOperate(sysLog);
                break;
            }
            case "changeMeter": {
                UserManageInfo info = (UserManageInfo) method_param[0];
                String original_meter_no = (String) method_param[1];
                sysLog.setOperatingcontent("换表  " + "  用户名：" + info.getUser_name() + "  " + "原表号：" + info.getMeter_no() + "->" + "新表号：" + info.getNew_meter_no());
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                sysLog.setUserName(info.getUser_name());
                userDao.setOperate(sysLog);
                break;
            }
            case "applyRemoveMeter": {
                UserManageInfo info = (UserManageInfo) method_param[0];
                String original_meter_no = (String) method_param[1];
                sysLog.setOperatingcontent("申请销户  " + "用户名：" + info.getUser_name() + "  " + "表号：" + info.getMeter_no());
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                sysLog.setUserName(info.getUser_name());
                userDao.setOperate(sysLog);
                break;
            }
            case "delMeter": {
                UserManageInfo info = (UserManageInfo) method_param[0];
                sysLog.setOperatingcontent("拆表  " + "用户名：" + info.getUser_name() + "  " + "表号：" + info.getMeter_no());
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                sysLog.setUserName(info.getUser_name());
                userDao.setOperate(sysLog);
                break;
            }
            case "saveAccount": {
                UserManageInfo info = (UserManageInfo) method_param[0];
                sysLog.setOperatingcontent("清算  " + "用户名：" + info.getUser_name() + "  " + "表号：" + info.getMeter_no() + "  " + "清算：" + info.getClosingCost() + "(元)");
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                sysLog.setUserName(info.getUser_name());
                userDao.setOperate(sysLog);
                break;
            }
            case "delUser": {
                UserManageInfo info = (UserManageInfo) method_param[0];
                sysLog.setOperatingcontent("已销户  " + "用户名：" + info.getUser_name() + "  " + "表号：" + info.getMeter_no());
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                sysLog.setUserName(info.getUser_name());
                userDao.setOperate(sysLog);
                break;
            }
            case "changeFeeType": {
                String user_name = (String) method_param[0];
                String fee_type = (String) method_param[2];
                String original_fee_type = (String) method_param[3];
                sysLog.setOperatingcontent("更换费率类型  " + "用户名：" + user_name + "  " + "费率名：" + original_fee_type + "->" + fee_type);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                sysLog.setUserName(user_name);
                userDao.setOperate(sysLog);
                break;
            }
            case "pay": {
                OperateInfo payOperateInfo = (OperateInfo) method_param[0];
                String operate_id = payOperateInfo.getOperate_id();
                String user_name = payOperateInfo.getUser_name();
                String meter_no = payOperateInfo.getMeter_no();
                String pay_method = payOperateInfo.getPay_method();
                Double recharge_money = payOperateInfo.getRecharge_money();
                sysLog.setOperatingcontent("充值: 业务编号" + operate_id + "  用户名" + user_name + "  表号" + meter_no + "  支付方式" + pay_method + "  金额" + recharge_money);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "refund": {
                OperateInfo payOperateInfo = (OperateInfo) method_param[0];
                String operate_id = payOperateInfo.getOperate_id();
                String user_name = payOperateInfo.getUser_name();
                String meter_no = payOperateInfo.getMeter_no();
                Double recharge_money = payOperateInfo.getRecharge_money();
                sysLog.setOperatingcontent("退费: 业务编号" + operate_id + "  用户名" + user_name + "  表号" + meter_no + "  金额" + recharge_money);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            //TODO
            case "打印凭证":
                break;
            //TODO
            case "saveReadInfo":
                break;
            //TODO
            case "一键抄":
                break;
            //TODO
            case "定时抄表":
                break;
            case "exportReadInfoReport":
                sysLog.setOperatingcontent("导出数据:导出抄表记录");
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            case "exportReadInfoFailReport":
                sysLog.setOperatingcontent("导出数据:导出抄表失败记录");
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            case "exportPayRecordReport":
                sysLog.setOperatingcontent("导出数据:充值记录");
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            case "exportReadInfoRemindReport":
                sysLog.setOperatingcontent("导出数据:催缴名单");
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            //TODO
            case "手动录入":
                break;
            case "markup": {
                String userName = (String) method_param[1];
                sysLog.setOperatingcontent(userName + "标记催缴");
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "delMarkup": {
                String userName = (String) method_param[1];
                sysLog.setOperatingcontent(userName + "取消标记催缴");
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "addWaterSupplier": {
                WaterSupplier supplier = (WaterSupplier) method_param[0];
                sysLog.setOperatingcontent("添加网点  " + supplier.getSupplier_name());
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "saveWaterSupplier": {
                String supplier_name_msg;
                String province_msg;
                String city_msg;
                String district_msg;
                String supplier_address_msg;
                String original_supplier_name = (String) method_param[0];
                String original_province = (String) method_param[1];
                String original_city = (String) method_param[2];
                String original_district = (String) method_param[3];
                String original_supplier_address = (String) method_param[4];
                WaterSupplier supplier = (WaterSupplier) method_param[5];
                if (supplier.getSupplier_name().equals(original_supplier_name)) {
                    supplier_name_msg = "网点名称：" + original_supplier_name + "  ";
                } else {
                    supplier_name_msg = "网点名称：" + original_supplier_name + "->" + supplier.getSupplier_name() + "  ";
                }
                if (supplier.getProvince().equals(original_province)) {
                    province_msg = "";
                } else {
                    province_msg = "省份：" + original_province + "->" + supplier.getProvince() + "  ";
                }
                if (supplier.getCity().equals(original_city)) {
                    city_msg = "";
                } else {
                    city_msg = "城市：" + original_city + "->" + supplier.getCity() + "  ";
                }
                if (supplier.getDistrict().equals(original_district)) {
                    district_msg = "";
                } else {
                    district_msg = "地区：" + original_district + "->" + supplier.getDistrict() + "  ";
                }
                if (supplier.getSupplier_address().equals(original_supplier_address)) {
                    supplier_address_msg = "";
                } else {
                    supplier_address_msg = "具体地址：" + original_supplier_address + "->" + supplier.getSupplier_address() + "  ";
                }
                sysLog.setOperatingcontent("修改网点   " + supplier_name_msg + province_msg + city_msg + district_msg + supplier_address_msg);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "delWaterSupplier":
                String supplier_name = (String) method_param[1];
                sysLog.setOperatingcontent("删除网点  " + supplier_name);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            case "saveReadParameter":
                //获取修改后值
                String day;
                String hour;
                String minute;
                String balance_warn;
                String valve_close;
                String isAutoRead;
                String isAutoInform;
                String original_day;
                String original_hour;
                String original_minute;
                //获取修改前的值
                Double original_balance_warn;
                Double original_valve_close;
                String original_isAutoRead;
                String original_isAutoInform;
                ReadParameter rp = (ReadParameter) method_param[0];
                original_day = (String) method_param[1];
                original_hour = (String) method_param[2];
                original_minute = (String) method_param[3];
                original_balance_warn = (Double) method_param[4];
                original_valve_close = (Double) method_param[5];
                original_isAutoRead = (String) method_param[6];
                original_isAutoInform = (String) method_param[7];
                day = original_day + "->" + rp.getDay() + "  ";
                hour = original_hour + "->" + rp.getHour() + "  ";
                minute = original_minute + "->" + rp.getMinute() + "  ";
                balance_warn = "余额报警值: " + original_balance_warn + "->" + rp.getBalance_warn();
                valve_close = "阀门关闭值: " + original_valve_close + "->" + rp.getValve_close();
                isAutoRead = "是否自动抄表: " + original_isAutoRead + "->" + rp.getIsAutoRead();
                isAutoInform = "抄表完毕通知: " + original_isAutoInform + "->" + rp.getIsAutoInform();
                sysLog.setOperatingcontent("修改天: " + day + "修改时: " + hour + "修改分: " + minute + "修改余额报警值:　" + balance_warn
                        + "修改阀门关闭值: " + valve_close + "修改是否自动抄表: " + isAutoRead + "修改抄表完毕是否通知: " + isAutoInform);
                sysLog.setLoginName(loginName);
                sysLog.setOperateDate(operateDate);
                userDao.setOperate(sysLog);
                break;
            case "addConcentrator": {
                Concentrator concentrator = (Concentrator) method_param[0];
                sysLog.setOperatingcontent("添加集中器  集中器编号：" + concentrator.getConcentrator_no() + "  集中器名称：" + concentrator.getConcentrator_name());
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "modifyConcentrator": {
                String concentrator_name_msg;
                String concentrator_no_msg;
                String gateway_id_msg;
                String concentrator_ip_msg;
                String concentrator_port_msg;
                String DTU_sim_no_msg;
                String concentrator_model_msg;
                String community_msg;
                String building_msg;
                String unit_msg;
                String original_concentrator_name = (String) method_param[0];
                String original_concentrator_no = (String) method_param[1];
                String original_gateway_id = (String) method_param[2];
                String original_concentrator_ip = (String) method_param[3];
                String original_concentrator_port = (String) method_param[4];
                String original_DTU_sim_no = (String) method_param[5];
                String original_concentrator_model = (String) method_param[6];
                String original_community = (String) method_param[7];
                String original_building = (String) method_param[8];
                String original_unit = (String) method_param[9];
                Concentrator concentrator = (Concentrator) method_param[10];
                if (concentrator.getConcentrator_name().equals(original_concentrator_name)) {
                    concentrator_name_msg = "集中器名称：" + original_concentrator_name + "  ";
                } else {
                    concentrator_name_msg = "集中器名称：" + original_concentrator_name + "->" + concentrator.getConcentrator_name() + "  ";
                }
                if (concentrator.getConcentrator_no().equals(original_concentrator_no)) {
                    concentrator_no_msg = "集中器编号：" + concentrator.getConcentrator_no() + "  ";
                } else {
                    concentrator_no_msg = "集中器编号：" + original_concentrator_no + "->" + concentrator.getConcentrator_no() + "  ";
                }
                if (concentrator.getGateway_id().equals(original_concentrator_no)) {
                    gateway_id_msg = "网关编号：" + concentrator.getGateway_id() + "  ";
                } else {
                    gateway_id_msg = "网关编号：" + original_gateway_id + "->" + concentrator.getGateway_id() + "  ";
                }
                if (concentrator.getConcentrator_ip().equals(original_concentrator_ip)) {
                    concentrator_ip_msg = "集中器IP：" + concentrator.getConcentrator_ip() + "  ";
                } else {
                    concentrator_ip_msg = "集中器IP：" + original_concentrator_ip + "->" + concentrator.getConcentrator_ip() + "  ";
                }
                if (concentrator.getConcentrator_port().equals(original_concentrator_port)) {
                    concentrator_port_msg = "集中器端口号：" + concentrator.getConcentrator_port() + "  ";
                } else {
                    concentrator_port_msg = "集中器端口号：" + original_concentrator_port + "->" + concentrator.getConcentrator_port() + "  ";
                }
                if (concentrator.getDTU_sim_no().equals(original_DTU_sim_no)) {
                    DTU_sim_no_msg = "";
                } else {
                    DTU_sim_no_msg = "DTU手机sim卡号：" + original_DTU_sim_no + "->" + concentrator.getDTU_sim_no() + "  ";
                }
                if (concentrator.getConcentrator_model().equals(original_concentrator_model)) {
                    concentrator_model_msg = "集中器模式: " + concentrator.getConcentrator_model() + " ";
                } else {
                    concentrator_model_msg = "集中器模式: " + original_concentrator_model + "->" + concentrator.getConcentrator_model() + "";
                }
                if (concentrator.getUser_address_community().equals(original_community)) {
                    community_msg = "";
                } else {
                    community_msg = "小区：" + original_community + "->" + concentrator.getUser_address_community() + "  ";
                }
                if (concentrator.getUser_address_building().equals(original_building)) {
                    building_msg = "";
                } else {
                    building_msg = "楼栋：" + original_building + "->" + concentrator.getUser_address_building() + "  ";
                }
                if (concentrator.getUser_address_unit().equals(original_unit)) {
                    unit_msg = "";
                } else {
                    unit_msg = "单元：" + original_unit + "->" + concentrator.getUser_address_unit() + "  ";
                }
                sysLog.setOperatingcontent("修改集中器   " + concentrator_name_msg + concentrator_no_msg + gateway_id_msg + concentrator_ip_msg + concentrator_port_msg + DTU_sim_no_msg + concentrator_model_msg + community_msg + building_msg + unit_msg);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "delConcentrator":
                String concentrator_name = (String) method_param[1];
                Integer concentrator_id = (Integer) method_param[0];
                sysLog.setOperatingcontent("删除集中器  集中器编号：" + concentrator_id + "  集中器名称：" + concentrator_name);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            case "addFeeType": {
                FeeType feeType = (FeeType) method_param[0];
                sysLog.setOperatingcontent("添加费率  费率名称：" + feeType.getFeeTypeName());
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "modifyFeeType": {
                FeeType feeType = (FeeType) method_param[0];
                String original_feeTypeName = (String) method_param[1];
                String original_meterType = (String) method_param[2];
                Double original_basicUnitPrice = (Double) method_param[3];
                Double original_disposingUnitCost = (Double) method_param[4];
                Double original_otherCost = (Double) method_param[5];
                Double original_extraCost = (Double) method_param[6];
                String original_paymentMethod = (String) method_param[7];
                String original_isLevelPrice = (String) method_param[8];
                Double original_levelOneStartVolume = (Double) method_param[9];
                Double original_levelOneUnitPrice = (Double) method_param[10];
                Double original_levelTwoStartVolume = (Double) method_param[11];
                Double original_levelTwoUnitPrice = (Double) method_param[12];
                Double original_levelThreeStartVolume = (Double) method_param[13];
                Double original_levelThreeUnitPrice = (Double) method_param[14];
                Double original_levelFourStartVolume = (Double) method_param[15];
                Double original_levelFourUnitPrice = (Double) method_param[16];
                Double original_levelFiveStartVolume = (Double) method_param[17];
                Double original_levelFiveUnitPrice = (Double) method_param[18];
                String feeTypeName_msg;
                String meterType_msg;
                String basicUnitPrice_msg;
                String disposingUnitCost_msg;
                String otherCost_msg;
                String extraCost_msg;
                String paymentMethod_msg;
                String isLevelPrice_msg;
                String levelOneStartVolume_msg;
                String levelOneUnitPrice_msg;
                String levelTwoStartVolume_msg;
                String levelTwoUnitPrice_msg;
                String levelThreeStartVolume_msg;
                String levelThreeUnitPrice_msg;
                String levelFourStartVolume_msg;
                String levelFourUnitPrice_msg;
                String levelFiveStartVolume_msg;
                String levelFiveUnitPrice_msg;
                if (feeType.getFeeTypeName().equals(original_feeTypeName)) {
                    feeTypeName_msg = "费率类型：" + feeType.getFeeTypeName() + "  ";
                } else {
                    feeTypeName_msg = "费率类型：" + original_feeTypeName + "->" + feeType.getFeeTypeName() + "  ";
                }
                if (feeType.getMeterType().equals(original_meterType)) {
                    meterType_msg = "";
                } else {
                    meterType_msg = "仪表类型：" + original_meterType + "->" + feeType.getMeterType() + "  ";
                }
                if (feeType.getBasicUnitPrice().equals(original_basicUnitPrice)) {
                    basicUnitPrice_msg = "";
                } else {
                    basicUnitPrice_msg = "基本单价：" + original_basicUnitPrice + "->" + feeType.getBasicUnitPrice() + "  ";
                }
                if (feeType.getDisposingUnitCost().equals(original_disposingUnitCost)) {
                    disposingUnitCost_msg = "";
                } else {
                    disposingUnitCost_msg = "排污费：" + original_disposingUnitCost + "->" + feeType.getDisposingUnitCost() + "  ";
                }
                if (feeType.getOtherCost().equals(original_otherCost)) {
                    otherCost_msg = "";
                } else {
                    otherCost_msg = "其他费用：" + original_otherCost + "->" + feeType.getOtherCost() + "  ";
                }
                if (feeType.getExtraCost().equals(original_extraCost)) {
                    extraCost_msg = "";
                } else {
                    extraCost_msg = "附加费：" + original_extraCost + "->" + feeType.getExtraCost() + "  ";
                }
                if (feeType.getPaymentMethod().equals(original_paymentMethod)) {
                    paymentMethod_msg = "";
                } else {
                    paymentMethod_msg = "结算周期：" + original_paymentMethod + "->" + feeType.getPaymentMethod() + "  ";
                }
                if (feeType.getIsLevelPrice().equals(original_isLevelPrice)) {
                    isLevelPrice_msg = "";
                } else {
                    isLevelPrice_msg = "启用阶梯水价：" + original_isLevelPrice + "->" + feeType.getIsLevelPrice() + "  ";
                }
                if (feeType.getLevelOneStartVolume().equals(original_levelOneStartVolume)) {
                    levelOneStartVolume_msg = "";
                } else {
                    levelOneStartVolume_msg = "一阶起始用量：" + original_levelOneStartVolume + "->" + feeType.getLevelOneStartVolume() + "  ";
                }
                if (feeType.getLevelOneUnitPrice().equals(original_levelOneUnitPrice)) {
                    levelOneUnitPrice_msg = "";
                } else {
                    levelOneUnitPrice_msg = "一阶基本单价：" + original_levelOneUnitPrice + "->" + feeType.getLevelOneUnitPrice() + "  ";
                }
                if (feeType.getLevelTwoStartVolume().equals(original_levelTwoStartVolume)) {
                    levelTwoStartVolume_msg = "";
                } else {
                    levelTwoStartVolume_msg = "二阶起始用量：" + original_levelTwoStartVolume + "->" + feeType.getLevelTwoStartVolume() + "  ";
                }
                if (feeType.getLevelTwoUnitPrice().equals(original_levelTwoUnitPrice)) {
                    levelTwoUnitPrice_msg = "";
                } else {
                    levelTwoUnitPrice_msg = "二阶基本单价：" + original_levelTwoUnitPrice + "->" + feeType.getLevelTwoUnitPrice() + "  ";
                }
                if (feeType.getLevelThreeStartVolume().equals(original_levelThreeStartVolume)) {
                    levelThreeStartVolume_msg = "";
                } else {
                    levelThreeStartVolume_msg = "三阶起始用量：" + original_levelThreeStartVolume + "->" + feeType.getLevelThreeStartVolume() + "  ";
                }
                if (feeType.getLevelThreeUnitPrice().equals(original_levelThreeUnitPrice)) {
                    levelThreeUnitPrice_msg = "";
                } else {
                    levelThreeUnitPrice_msg = "三阶基本单价：" + original_levelThreeUnitPrice + "->" + feeType.getLevelThreeUnitPrice() + "  ";
                }
                if (feeType.getLevelFourStartVolume().equals(original_levelFourStartVolume)) {
                    levelFourStartVolume_msg = "";
                } else {
                    levelFourStartVolume_msg = "四阶起始用量：" + original_levelFourStartVolume + "->" + feeType.getLevelFourStartVolume() + "  ";
                }
                if (feeType.getLevelFourUnitPrice().equals(original_levelFourUnitPrice)) {
                    levelFourUnitPrice_msg = "";
                } else {
                    levelFourUnitPrice_msg = "四阶基本单价：" + original_levelFourUnitPrice + "->" + feeType.getLevelFourUnitPrice() + "  ";
                }
                if (feeType.getLevelFiveStartVolume().equals(original_levelFiveStartVolume)) {
                    levelFiveStartVolume_msg = "";
                } else {
                    levelFiveStartVolume_msg = "五阶起始用量：" + original_levelFiveStartVolume + "->" + feeType.getLevelFiveStartVolume() + "  ";
                }
                if (feeType.getLevelFiveUnitPrice().equals(original_levelFiveUnitPrice)) {
                    levelFiveUnitPrice_msg = "";
                } else {
                    levelFiveUnitPrice_msg = "五阶基本单价：" + original_levelFiveUnitPrice + "->" + feeType.getLevelFiveUnitPrice() + "  ";
                }
                sysLog.setOperatingcontent("修改费率："
                        + feeTypeName_msg
                        + meterType_msg
                        + basicUnitPrice_msg
                        + disposingUnitCost_msg
                        + otherCost_msg
                        + extraCost_msg
                        + paymentMethod_msg
                        + isLevelPrice_msg
                        + levelOneStartVolume_msg
                        + levelOneUnitPrice_msg
                        + levelTwoStartVolume_msg
                        + levelTwoUnitPrice_msg
                        + levelThreeStartVolume_msg
                        + levelThreeUnitPrice_msg
                        + levelFourStartVolume_msg
                        + levelFourUnitPrice_msg
                        + levelFiveStartVolume_msg
                        + levelFiveUnitPrice_msg
                );
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "delFeeType":
                String feeTypeName = (String) method_param[1];
                sysLog.setOperatingcontent("删除费率：" + feeTypeName);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            case "addCommunity":
                Community community = (Community) method_param[0];
                sysLog.setOperatingcontent("添加小区：" + community.getCommunity_name());
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            case "addBuilding":
                Building building = (Building) method_param[0];
                sysLog.setOperatingcontent("添加楼栋：" + building.getCommunity_name() + ">" + building.getBuilding_name());
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            case "addUnit":
                Unit unit = (Unit) method_param[0];
                sysLog.setOperatingcontent("添加单元：" + unit.getCommunity_name() + ">" + unit.getBuilding_name() + ">" + unit.getUnit_name());
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            case "modifyCommunity": {
                String community_name = (String) method_param[4];
                String new_community_name = (String) method_param[8];
                sysLog.setOperatingcontent("修改小区：" + community_name + "->" + new_community_name);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "modifyBuilding": {
                String community_name = (String) method_param[3];
                String building_name = (String) method_param[4];
                String new_building_name = (String) method_param[5];
                sysLog.setOperatingcontent("修改楼栋：" + community_name + ">" + building_name + "->" + new_building_name);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "modifyUnit": {
                String community_name = (String) method_param[3];
                String building_name = (String) method_param[4];
                String unit_name = (String) method_param[5];
                String new_unit_name = (String) method_param[6];
                sysLog.setOperatingcontent("修改单元：" + community_name + ">" + building_name + ">" + unit_name + "->" + new_unit_name);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "delCommunity": {
                String community_name = (String) method_param[3];
                sysLog.setOperatingcontent("删除小区：" + community_name);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "delBuilding": {
                String building_name = (String) method_param[4];
                String community_name = (String) method_param[3];
                sysLog.setOperatingcontent("删除楼栋：" + community_name + ">" + building_name);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "delUnit": {
                String unit_name = (String) method_param[5];
                String building_name = (String) method_param[4];
                String community_name = (String) method_param[3];
                sysLog.setOperatingcontent("删除单元：" + community_name + ">" + building_name + ">" + unit_name);
                sysLog.setOperateDate(operateDate);
                sysLog.setLoginName(loginName);
                userDao.setOperate(sysLog);
                break;
            }
            case "addRole": {
                Role role = (Role) method_param[0];
                sysLog.setOperatingcontent("新增权限：" + role.getRole_name() + ">" + role.getPermission());
                sysLog.setLoginName(loginName);
                sysLog.setOperateDate(operateDate);
                userDao.setOperate(sysLog);
                break;
            }
            case "modifyRole": {
                Role role = (Role) method_param[0];
                String original_role_name = (String) method_param[1];
                String original_permission = (String) method_param[2];
                String role_name_msg;
                String permission_msg;
                if (role.getRole_name().equals(original_role_name)) {
                    role_name_msg = "角色名称：" + role.getRole_name() + "  ";
                } else {
                    role_name_msg = "角色名称：" + original_role_name + "->" + role.getRole_name() + "  ";
                }
                if (role.getPermission().equals(original_permission)) {
                    permission_msg = "  ";
                } else {
                    permission_msg = "权限：" + original_permission + "->" + role.getPermission() + "  ";
                }
                sysLog.setOperatingcontent("修改角色: " + role_name_msg + permission_msg);
                sysLog.setLoginName(loginName);
                sysLog.setOperateDate(operateDate);
                userDao.setOperate(sysLog);
                break;
            }
            case "delRole":
                String role_name = (String) method_param[1];
                sysLog.setLoginName(loginName);
                sysLog.setOperatingcontent("删除角色: " + role_name);
                sysLog.setOperateDate(operateDate);
                break;
            case "modifyTelById": {
                String admin_tel = (String) method_param[1];
                String original_admin_tel = (String) method_param[2];
                sysLog.setLoginName(loginName);
                sysLog.setOperatingcontent("修改管理员: " + loginName + "  " + "联系方式:" + original_admin_tel + "->" + admin_tel);
                sysLog.setOperateDate(operateDate);
                break;
            }
            case "modifyNameById": {
                String admin_name = (String) method_param[1];
                String original_admin_name = (String) method_param[2];
                sysLog.setLoginName(loginName);
                sysLog.setOperatingcontent("修改管理员: " + loginName + "  " + "姓名:" + original_admin_name + "->" + admin_name);
                sysLog.setOperateDate(operateDate);
                break;
            }
            default:
                break;
        }
        return object;
    }

    public void commit() {
    }
}
