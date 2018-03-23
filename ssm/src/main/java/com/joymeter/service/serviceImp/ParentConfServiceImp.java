package com.joymeter.service.serviceImp;

import com.joymeter.cache.MeterUserCache;
import com.joymeter.cache.ParentMeterCache;
import com.joymeter.cache.ReadInfoCache;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.OperateInfo;
import com.joymeter.entity.ParentMeterConf;
import com.joymeter.entity.PubReductionFee;
import com.joymeter.entity.ReadInfo;
import com.joymeter.entity.Result;
import com.joymeter.entity.User;
import com.joymeter.readExecl.ReadParentMeterExcel;
import com.joymeter.util.FileUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.joymeter.service.ParentConfService;
import com.joymeter.service.RechargeService;
import com.joymeter.util.DownloadUtil;
import com.joymeter.util.JoyDatetime;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author yinhf
 */
@Service
public class ParentConfServiceImp implements ParentConfService {

    @Resource
    private UserDao userDao;
    @Resource
    private RechargeService rechargeService;
    @Resource
    private ParentMeterCache parentMeterCache;
    @Resource
    private ReadInfoCache readInfoCache;
    @Resource
    private MeterUserCache meterUserCache;

    /**
     * 更改母表配置信息
     *
     * @param mmc
     * @param org_meter_no
     * @param org_meter_type
     * @param org_allot_type
     * @return
     */
    @Override
    public Result updateMotherMeterConf(ParentMeterConf mmc, String org_meter_no, String org_meter_type, String org_allot_type) {
        Result result = new Result();
        try {
            if (!mmc.getMeter_no().equals(org_meter_no)) {
                List<User> users = userDao.findUserBySubNo(mmc.getMeter_no());
                Map<String, Object> map = new HashMap<>();
                map.put("meter_no", mmc.getMeter_no());
                map.put("org_meter_no", org_meter_no);
                users.forEach(p -> {
                    if (p.getMeter_no().equals(p.getSubmeter_no())) {
                        userDao.updateMotherMeter(map);
                        parentMeterCache.add(mmc);
                    } else {
                        userDao.updateUserBySubNo(map);
                    }
                });
            }
            parentMeterCache.add(mmc);
            userDao.updateMotherMeterConf(mmc);
            userDao.updateMeterHouseArea(mmc.getMeter_no());
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

    @Override
    public Result removeParentMeterConf(String meter_Nums) {
        Result result = new Result();
        result.setStatus(0);
        if (meter_Nums.equals("")) {
            result.setData("表号为空");
            return result;
        }
        try {
            userDao.removeParentConf(meter_Nums);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Result qryShareBilling(String user_name, String community,
            String building, String unit, String room, String meter_no, int startNo, int pageSize) {
        Result result = new Result();
        result.setStatus(0);
        Map<String, Object> map = new HashMap<>();
        map.put("pef_userName", user_name);
        map.put("pef_community", community);
        map.put("pef_buildNo", building);
        map.put("pef_unitNo", unit);
        map.put("pef_roomNo", room);
        map.put("pef_meterNo", meter_no);
        map.put("startNo", startNo);
        map.put("pageSize", pageSize);
        try {
            List<PubReductionFee> pubReductionFees = userDao.getDeductionFee(map);
            int total = userDao.qryAllShareNo(map);
            map.clear();
            map.put("pubReductionFees", pubReductionFees);
            map.put("totalNo", total);
            result.setStatus(1);
            result.setData(map);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            result.setStatus(0);
        }

        return result;
    }

    @Override
    public Result upload(HttpServletRequest request) throws Exception {
        Result result = new Result();
        try {
            int i = 0;
            int success = 0;
            int exist = 0;
            OutputStream outputStream;
            String fileName;
            ParentMeterConf parentMeterConf;
            Map<String, Integer> num = new HashMap<>();
            List<Object> li = new LinkedList<>();//存放已经存在的表号
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = mRequest.getFileMap();
            String uploadDir = request.getSession().getServletContext().getRealPath("/") + FileUtil.UPLOADDIR;
            File file = new File(uploadDir);
            if (!file.exists()) {
                file.mkdir();
            }
            for (Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator(); it.hasNext(); i++) {
                Map.Entry<String, MultipartFile> entry = it.next();
                MultipartFile mFile = entry.getValue();
                fileName = mFile.getOriginalFilename();
                String storeName = FileUtil.rename(fileName);
                String noExcelName = uploadDir + storeName;
                String excelName = FileUtil.excelName(noExcelName);
                outputStream = new FileOutputStream(excelName);
                FileCopyUtils.copy(mFile.getInputStream(), outputStream);
                ReadParentMeterExcel xlsMain = new ReadParentMeterExcel();
                List<ParentMeterConf> list = xlsMain.readXls(request, storeName);
                int size = list.size();
                for (int j = 0; j < size; j++) {
                    parentMeterConf = list.get(j);
                    if (parentMeterConf.getMeter_no() == null) {
                        continue;
                    }
                    if (parentMeterConf.getMeter_type().equals("公有母表")) {
                        parentMeterConf.setMeter_type("0");
                    } else if (parentMeterConf.getMeter_type().equals("私有母表")) {
                        parentMeterConf.setMeter_type("1");
                    }
                    switch (parentMeterConf.getAllot_type()) {
                        case "均摊":
                            parentMeterConf.setAllot_type("0");
                            break;
                        case "按比例":
                            parentMeterConf.setAllot_type("1");
                            break;
                        case "按人头":
                            parentMeterConf.setAllot_type("2");
                            break;
                        default:
                            parentMeterConf.setAllot_type("3");
                            break;
                    }
                    //检查表号是否已经存在
                    ParentMeterConf existMeterConf = userDao.qryMotherConfInfo(parentMeterConf.getMeter_no());
                    if (existMeterConf == null) {
                        userDao.addMotherMeterConf(parentMeterConf);
                        success++;

                    } else {
                        exist++;
                        li.add(parentMeterConf.getMeter_no());
                    }
                }
            }
            num.put("success", success);
            num.put("exist", exist);
            li.add(num);
            result.setStatus(1);
            result.setData(li);
        } catch (IOException e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Result downLoadConfExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Result result = new Result();
        String ctxPath = request.getSession().getServletContext().getRealPath("/file");
        String language = request.getParameter("lang1");
        String ModelName = "Chinese".equals(language) ? "配置母表信息导入表.xlsx" : "Configuration information import table.xlsx";
        String downLoadPath = ctxPath + "/" + ModelName;
        InputStream inStream = new FileInputStream(downLoadPath);
        OutputStream os = response.getOutputStream();
        response.reset();
        response.setContentType("bin");
        response.setHeader("Content-disposition", "attachment; filename="
                + new String(ModelName.getBytes("utf-8"), "ISO8859-1"));
        DownloadUtil.downloadLocal(inStream, os);
        return result;
    }
    //公摊计费

    @Override
    public Result shareBilling(User user, ReadInfo readInfo, OperateInfo parentOperateInfo) {
        ParentMeterConf parentConInfo = parentMeterCache.queryParentMeterConf(user.getMeter_no());
        if (parentConInfo != null && readInfo.getThis_cost() > 0) {
            //母表类型 0:公有母表 1：私有母表  费用承担方式 0:均摊 1：按比例 2：按人头 3:鼓风机
            Map<String, Object> map = new HashMap<>();
            map.put("concentrator_name", user.getConcentrator_name());
            map.put("meter_no", user.getMeter_no());
            map.put("type", parentConInfo.getMeter_type());
            List<User> nums = userDao.qrySubMeter(map);
            double averageCount = 0.0, averageShare = 0.0;
            int size = nums.size();
            String allot_type = parentConInfo.getAllot_type();
            String operator_account = readInfo.getOperator_account();
            if (size > 0) {
                try {
                    BigDecimal b, b1;
                    double sum = nums.stream().mapToDouble(u -> u.getHouse_area()).sum();//人数总和或者比例的总和
                    PubReductionFee fee = new PubReductionFee();
                    OperateInfo operateInfo = new OperateInfo();
                    //每位用户需要承担的用量
                    b = new BigDecimal(readInfo.getThis_cost() / size);
                    averageCount = b.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                    //每位用户需要承担的费用
                    b1 = new BigDecimal(readInfo.getFee_need() / size);
                    averageShare = b1.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                    //扣取每一个表号的费用
                    double refundOrRechange = 0.0;
                    for (int i = 0; i < size; i++) {
                        User sub = nums.get(i);
                        switch (allot_type) {
                            case "1"://比例
                                b = new BigDecimal(readInfo.getThis_cost() * nums.get(i).getHouse_area() / 100d);
                                averageCount = b.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                                b1 = new BigDecimal(readInfo.getFee_need() * nums.get(i).getHouse_area() / 100d);
                                averageShare = b1.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                                break;
                            case "2"://人头
                                b = new BigDecimal(readInfo.getThis_cost() * (sub.getHouse_area() / sum));
                                averageCount = b.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                                b1 = new BigDecimal(readInfo.getFee_need() * (sub.getHouse_area() / sum));
                                averageShare = b1.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                                break;
                        }
                        refundOrRechange = refundOrRechange + averageShare;
                        operateInfo.setBalance(sub.getLast_balance());//用户余额
                        operateInfo.setCity(sub.getCity());
                        operateInfo.setConcentrator_name(sub.getConcentrator_name());
                        operateInfo.setContact_info(sub.getContact_info());
                        operateInfo.setDistrict(sub.getDistrict());
                        operateInfo.setFee_type(sub.getFee_type());
                        operateInfo.setIsPrint("0");
                        operateInfo.setMeter_model(sub.getMeter_model());
                        operateInfo.setMeter_no(sub.getMeter_no());
                        operateInfo.setMeter_type(sub.getMeter_type());
                        operateInfo.setOperate_id("GT" + String.valueOf(JoyDatetime.getTokenID()));
                        operateInfo.setOperate_time(new Timestamp(System.currentTimeMillis()));
                        operateInfo.setOperate_type("3");//公摊扣费
                        operateInfo.setOperator_account(sub.getOperator_account());
                        operateInfo.setPay_method("现金");
                        operateInfo.setProvince(sub.getProvince());
                        operateInfo.setRecharge_loc(sub.getSupplier_name());
                        operateInfo.setRecharge_money(averageShare);
                        operateInfo.setUser_address_area(sub.getUser_address_area());
                        operateInfo.setUser_address_community(sub.getUser_address_community());
                        operateInfo.setUser_address_building(sub.getUser_address_building());
                        operateInfo.setUser_address_unit(sub.getUser_address_unit());
                        operateInfo.setUser_address_room(sub.getUser_address_room());
                        operateInfo.setUser_name(sub.getUser_name());

                        String pef_bussinessNum = "GT" + String.valueOf(JoyDatetime.getTokenID());
                        fee.setPef_bussinessNum(pef_bussinessNum);
                        fee.setPef_meterNo(sub.getMeter_no());
                        fee.setPef_userName(sub.getUser_name());
                        fee.setPef_belongPar(sub.getSubmeter_no());
                        fee.setPef_parType(parentConInfo.getMeter_type());
                        fee.setPef_allotType(parentConInfo.getAllot_type());
                        fee.setPef_shareRadio(sub.getHouse_area());//分摊比例
                        fee.setPef_shareSize(averageCount);//分摊量
                        fee.setPef_shareFee(0.0);//应扣费
                        fee.setPef_refund(0.00d);//母表公摊退费
                        fee.setPef_applyTime(new Timestamp(System.currentTimeMillis()).toString());
                        fee.setPef_Total(readInfo.getThis_cost());//母表用量，母表记录的总量
                        fee.setPef_community(sub.getUser_address_community());//用户小区
                        fee.setPef_buildNo(sub.getUser_address_building());//用户所在栋
                        fee.setPef_unitNo(sub.getUser_address_unit());//用户所在单元
                        fee.setPef_roomNo(sub.getUser_address_room());//用户所在房间
                        fee.setPef_rateType(sub.getFee_type());
                        BigDecimal b3 = new BigDecimal(sub.getLast_balance() - averageShare);
                        double balance = b3.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                        fee.setPef_accountBalance(sub.getLast_balance());//子表的账户余额                      
                        if ("0".equals(readInfo.getIsAutoClear())) {
                            rechargeService.payOrRefund(operateInfo, "autoPay");//子表扣费
                            fee.setPef_shareFee(averageShare);//应扣费
                            fee.setPef_accountBalance(balance);
                        }
                        this.saveSubMeterReadInfo(sub, operator_account, averageCount, averageShare, readInfo.getIsAutoClear());
                        userDao.setDeductionFee(fee);//将子表应扣量信息存入表中 
                    }
                    switch (parentConInfo.getAllot_type()) {
                        case "1":
                            BigDecimal b4 = new BigDecimal(readInfo.getThis_cost() * (1 - sum / 100d));
                            double data8 = b4.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();//公摊剩余量
                            readInfo.setData8(data8);
                            this.updateData8(user, parentOperateInfo, data8);
                            break;
                    }

                    switch (parentConInfo.getAllot_type()) {
                        case "1":
                            refundOrRechange = readInfo.getFee_need() * (sum / 100d);
                            break;
                    }
                    String opeParent_id = "TF" + String.valueOf(JoyDatetime.getTokenID());
                    parentOperateInfo.setOperate_id(opeParent_id);
                    parentOperateInfo.setRecharge_money(refundOrRechange);//公摊退费
                    parentOperateInfo.setOperate_type("4");//公摊退费
                    parentOperateInfo.setOperate_time(new Timestamp(System.currentTimeMillis()));
                    if ("0".equals(readInfo.getIsAutoClear())) {
                        rechargeService.payOrRefund(parentOperateInfo, "pay");//母表退费
                    }
                    double last_balance = user.getLast_balance();
                    //记录母表退费信息
                    PubReductionFee refund = new PubReductionFee();
                    String pef_bussinessNum = "TF" + String.valueOf(JoyDatetime.getTokenID());
                    refund.setPef_bussinessNum(pef_bussinessNum);
                    refund.setPef_meterNo(user.getMeter_no());
                    refund.setPef_userName(user.getUser_name());
                    refund.setPef_parType(parentConInfo.getMeter_type());
                    refund.setPef_allotType(parentConInfo.getAllot_type());
                    refund.setPef_applyTime(new Timestamp(System.currentTimeMillis()).toString());
                    refund.setPef_refund(refundOrRechange);
                    refund.setPef_Total(readInfo.getThis_cost());
                    refund.setPef_accountBalance(last_balance);
                    refund.setPef_community(user.getUser_address_community());
                    refund.setPef_buildNo(user.getUser_address_building());
                    refund.setPef_unitNo(user.getUser_address_unit());
                    refund.setPef_roomNo(user.getUser_address_room());
                    refund.setPef_rateType(user.getFee_type());
                    if ("0".equals(readInfo.getIsAutoClear())) {
                        userDao.setDeductionFee(refund);
                    }
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        return null;
    }

    //更新data8的值
    public void updateData8(User user, OperateInfo parentOperateInfo, double data8) {
        BigDecimal b6 = new BigDecimal(data8);
        double maxReminCount = b6.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (maxReminCount != 0) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("operate_id", parentOperateInfo.getOperate_id());
            map2.put("data8", maxReminCount);
            userDao.setRemainAmount(map2);
        }
    }

    //记录子表信息
    public void saveSubMeterReadInfo(User sub, String operator_account, double averageCount,
            double averageShare, String isAutoClear) {
        ReadInfo subThisReadInfo = new ReadInfo();
        Date date = new Date(System.currentTimeMillis());
        BigDecimal b3 = new BigDecimal(averageCount);
        double maxAverageCount = b3.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
        String id = "CB" + String.valueOf(JoyDatetime.getTokenID());
        subThisReadInfo.setOperate_id(id);
        subThisReadInfo.setCommunity_no(sub.getUser_address_community());
        subThisReadInfo.setUser_name(sub.getUser_name());
        subThisReadInfo.setUser_address_area(sub.getUser_address_area());
        subThisReadInfo.setUser_address_building(sub.getUser_address_building());
        subThisReadInfo.setUser_address_community(sub.getUser_address_community());
        subThisReadInfo.setUser_address_room(sub.getUser_address_room());
        subThisReadInfo.setUser_address_unit(sub.getUser_address_unit());
        subThisReadInfo.setUser_address(sub.getUser_address_area()
                + sub.getUser_address_community()
                + sub.getUser_address_building()
                + sub.getUser_address_room());
        subThisReadInfo.setContact_info(sub.getContact_info());
        subThisReadInfo.setConcentrator_name(sub.getConcentrator_name());
        subThisReadInfo.setMeter_no(sub.getMeter_no());
        subThisReadInfo.setFee_type(sub.getFee_type());
        subThisReadInfo.setFee_status("2");//未扣
        subThisReadInfo.setException("1");//无
        subThisReadInfo.setRead_type("0");//自动
        subThisReadInfo.setOperator_account(operator_account);
        subThisReadInfo.setOperate_time(new Timestamp(System.currentTimeMillis()));
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dayTime = new SimpleDateFormat("HH:mm:ss");
        subThisReadInfo.setOperate_day(day.format(date));
        subThisReadInfo.setOperate_dayTime(dayTime.format(date));
        subThisReadInfo.setMeter_type(sub.getMeter_type());
        subThisReadInfo.setIsAutoClear("11");//11:子表均摊抄表记录
        subThisReadInfo.setData2(0.0);
        subThisReadInfo.setData3(0.0);
        subThisReadInfo.setData4(0.0);
        subThisReadInfo.setData5(0.0);
        subThisReadInfo.setData6(0.0);
        subThisReadInfo.setData7(0.0);
        subThisReadInfo.setData8(maxAverageCount);//公摊用量
        subThisReadInfo.setData9("0.0");
        subThisReadInfo.setThis_cost(averageCount);
        subThisReadInfo.setFee_need(averageShare);//fee_need 需要扣费    
        BigDecimal b2 = new BigDecimal(sub.getLast_balance() - averageShare);
        double subMeterBalance = b2.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
        subThisReadInfo.setBalance(sub.getLast_balance());//账面余额,未扣均摊费
        if ("0".equals(isAutoClear)) {
            subThisReadInfo.setBalance(subMeterBalance);//账面余额
            subThisReadInfo.setFee_status("1");//已扣
        }
        ReadInfo submetReadInfo = userDao.qryMaxOfThisRead(sub.getMeter_no());
        subThisReadInfo.setLast_read(0.0d);
        subThisReadInfo.setThis_read(0.0d);
        subThisReadInfo.setLast_cost(0.0d);
        if (submetReadInfo != null) {
            subThisReadInfo.setLast_read(submetReadInfo.getLast_read());
            subThisReadInfo.setThis_read(submetReadInfo.getThis_read());
            subThisReadInfo.setLast_cost(submetReadInfo.getThis_cost());
        }
        userDao.saveReadInfo(subThisReadInfo);
    }

    @Override
    public Result shareBilling(User user, ReadInfo readInfo,
            OperateInfo parentOperateInfo, String sendTime) {
        ParentMeterConf parentConInfo = parentMeterCache.queryParentMeterConf(user.getMeter_no());
        //母表，或者子表属于某只母表时,进入公摊计算
        if (parentConInfo != null || !"0".equals(user.getSubmeter_no())) {
            List<User> nums = null;
            Map<String, Object> map = new HashMap<>();
            map.put("concentrator_name", user.getConcentrator_name());
            map.put("meter_no", user.getSubmeter_no());
            if (parentConInfo != null) {
                map.put("type", parentConInfo.getMeter_type());
                nums = meterUserCache.getSubUsers(map);
            }
            Collection<ReadInfo> exceInfos = null;
            Map<String, Object> map2 = new HashMap<>();
            map2.put("sendTime", sendTime);
            map2.put("meter_no", user.getMeter_no());
            double parThis_cost = 0.0;
            double parFee_need = 0.0;
            String operator_account = readInfo.getOperator_account();
            //母表均摊:母表不为空、母表的子表个数不为0、母表本期用量不为0
            if ((parentConInfo != null && nums != null && nums.size() > 0) && 0.0 != readInfo.getThis_cost()) {
                List<ReadInfo> latestReadInfos = readInfoCache.qryLatestReadInfos(map2);
                if (latestReadInfos.size() > 0 && nums.size() == latestReadInfos.size()) {
                    //检查是否有异常读数
                    exceInfos = latestReadInfos.stream()
                            .filter(r -> "0".equals(r.getException()))
                            .collect(Collectors.toList());
                    parThis_cost = readInfo.getThis_cost();//获取母表的用电量
                    parFee_need = readInfo.getFee_need();//获取母表的费用
                    if (exceInfos.isEmpty()) {
                        this.spilt(parThis_cost, parFee_need, latestReadInfos, nums, operator_account,
                                readInfo.getIsAutoClear(), user, parentConInfo, parentOperateInfo);
                    }
                }
            } else if (parentConInfo == null) { //子表
                //求出剩余子表和母表的读数 
                List<ReadInfo> latestReadInfos = readInfoCache.qrysubLatestReadInfos(map2);
                map.put("type", "1");
                nums = meterUserCache.getSubUsers(map);
                if (latestReadInfos.size() > 0 && nums.size() == latestReadInfos.size() - 1) {
                    exceInfos = latestReadInfos.stream()
                            .filter(r -> "0".equals(r.getException()))
                            .collect(Collectors.toList());
                    if (exceInfos.isEmpty()) {
                        try {
                            List<ReadInfo> latestParReadInfos = latestReadInfos.stream()
                                    .filter(r -> r.getMeter_no().equals(user.getSubmeter_no()))
                                    .collect(Collectors.toList());
                            if (latestParReadInfos.get(0).getThis_cost() == 0.0) {
                                return null;
                            }
                            //查询所有子表的抄表记录
                            latestReadInfos = latestReadInfos.stream()
                                    .filter(r -> !r.getMeter_no().equals(user.getSubmeter_no()))
                                    .collect(Collectors.toList());
                            this.spilt(parThis_cost, parFee_need, latestReadInfos, nums, operator_account,
                                    readInfo.getIsAutoClear(), user, parentConInfo, parentOperateInfo);
                        } catch (Exception e) {
                            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                        }
                    }
                }
            }
        }
        return null;
    }

    public Result spilt(double parThis_cost, double parFee_need, Collection<ReadInfo> latestReadInfos,
            List<User> nums, String operator_account, String isAutoClear, User user,
            ParentMeterConf parentConInfo, OperateInfo parentOperateInfo) {
        try {
            int size = nums.size();
            double averageCount = 0.0, averageShare = 0.0;
            BigDecimal b, b1;
            PubReductionFee fee = new PubReductionFee();
            OperateInfo operateInfo = new OperateInfo();
            List<ReadInfo> readInfo;
            //股风机用电总电量
            double sumTisCost = parThis_cost / latestReadInfos.stream().mapToDouble(t -> t.getThis_cost()).sum();
            double sumFee_need = parFee_need / latestReadInfos.stream().mapToDouble(t -> t.getThis_cost()).sum();
            double refundOrRechange = 0.0;
            //扣取每一个表号的费用
            for (int i = 0; i < size; i++) {
                User sub = nums.get(i);
                readInfo = latestReadInfos.stream()
                        .filter(r -> r.getMeter_no().equals(sub.getMeter_no()))
                        .collect(Collectors.toList());
                double this_cost = readInfo.get(0).getThis_cost();
                //每位用户需要承担的用量
                b = new BigDecimal(this_cost * sumTisCost);
                averageCount = b.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                //每位用户需要承担的费用
                b1 = new BigDecimal(this_cost * sumFee_need);
                averageShare = b1.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                this.saveSubMeterReadInfo(sub, operator_account, averageCount, averageShare, isAutoClear);
                operateInfo.setBalance(sub.getLast_balance());//用户余额
                operateInfo.setCity(sub.getCity());
                operateInfo.setConcentrator_name(sub.getConcentrator_name());
                operateInfo.setContact_info(sub.getContact_info());
                operateInfo.setDistrict(sub.getDistrict());
                operateInfo.setFee_type(sub.getFee_type());
                operateInfo.setIsPrint("0");
                operateInfo.setMeter_model(sub.getMeter_model());
                operateInfo.setMeter_no(sub.getMeter_no());
                operateInfo.setMeter_type(sub.getMeter_type());
                String operate_id = "GT" + String.valueOf(JoyDatetime.getTokenID());
                operateInfo.setOperate_id(operate_id);
                operateInfo.setOperate_time(new Timestamp(System.currentTimeMillis()));
                operateInfo.setOperate_type("3");//公摊扣费
                operateInfo.setOperator_account(sub.getOperator_account());
                operateInfo.setPay_method("现金");
                operateInfo.setProvince(sub.getProvince());
                operateInfo.setRecharge_loc(sub.getSupplier_name());
                operateInfo.setRecharge_money(averageShare);
                operateInfo.setUser_address_area(sub.getUser_address_area());
                operateInfo.setUser_address_community(sub.getUser_address_community());
                operateInfo.setUser_address_building(sub.getUser_address_building());
                operateInfo.setUser_address_unit(sub.getUser_address_unit());
                operateInfo.setUser_address_room(sub.getUser_address_room());
                operateInfo.setUser_name(sub.getUser_name());

                String pef_bussinessNum = "GT" + String.valueOf(JoyDatetime.getTokenID());
                fee.setPef_bussinessNum(pef_bussinessNum);
                fee.setPef_meterNo(sub.getMeter_no());
                fee.setPef_userName(sub.getUser_name());
                fee.setPef_belongPar(sub.getSubmeter_no());
                fee.setPef_parType("1");
                fee.setPef_allotType("3");
                fee.setPef_shareRadio(sub.getHouse_area());//分摊比例
                fee.setPef_shareSize(averageCount);//分摊量
                fee.setPef_shareFee(0.0);//应扣费
                fee.setPef_refund(0.00d);//母表公摊退费
                fee.setPef_applyTime(new Timestamp(System.currentTimeMillis()).toString());
                fee.setPef_Total(parThis_cost);//母表用量，母表记录的总量
                fee.setPef_community(sub.getUser_address_community());
                fee.setPef_buildNo(sub.getUser_address_building());
                fee.setPef_unitNo(sub.getUser_address_unit());
                fee.setPef_roomNo(sub.getUser_address_room());
                fee.setPef_rateType(sub.getFee_type());
                BigDecimal b3 = new BigDecimal(sub.getLast_balance() - averageShare);
                double balance = b3.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                fee.setPef_accountBalance(sub.getLast_balance());//子表的账户余额                      
                if ("0".equals(isAutoClear)) {
                    rechargeService.payOrRefund(operateInfo, "autoPay");//子表扣费
                    fee.setPef_shareFee(averageShare);//应扣费
                    fee.setPef_accountBalance(balance);
                }
                userDao.setDeductionFee(fee);
                refundOrRechange = refundOrRechange + averageShare;
            }
            Double maxData8 = 0.0d;
            BigDecimal b4 = new BigDecimal(0.0);
            double data8 = b4.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
            this.updateData8(user, parentOperateInfo, data8);
            String opeParent_id = "TF" + String.valueOf(JoyDatetime.getTokenID());
            parentOperateInfo.setOperate_id(opeParent_id);
            parentOperateInfo.setRecharge_money(refundOrRechange);//公摊退费
            parentOperateInfo.setOperate_type("4");//公摊退费
            parentOperateInfo.setOperate_time(new Timestamp(System.currentTimeMillis()));
            if ("0".equals(isAutoClear)) {
                rechargeService.payOrRefund(parentOperateInfo, "pay");//母表退费
            }
            double last_balance = user.getLast_balance();
            //记录母表退费信息
            PubReductionFee refund = new PubReductionFee();
            String pef_bussinessNum = "TF" + String.valueOf(JoyDatetime.getTokenID());
            refund.setPef_bussinessNum(pef_bussinessNum);
            refund.setPef_meterNo(user.getMeter_no());
            refund.setPef_userName(user.getUser_name());
            refund.setPef_parType("1");
            refund.setPef_allotType("3");
            refund.setPef_applyTime(new Timestamp(System.currentTimeMillis()).toString());
            refund.setPef_refund(refundOrRechange);
            refund.setPef_Total(parThis_cost);
            refund.setPef_accountBalance(last_balance);
            refund.setPef_community(user.getUser_address_community());
            refund.setPef_buildNo(user.getUser_address_building());
            refund.setPef_unitNo(user.getUser_address_unit());
            refund.setPef_roomNo(user.getUser_address_room());
            refund.setPef_rateType(user.getFee_type());
            if ("0".equals(isAutoClear)) {
                userDao.setDeductionFee(refund);
            }

        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
