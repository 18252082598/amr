package com.joymeter.dao;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.joymeter.entity.Admin;
import com.joymeter.entity.Building;
import com.joymeter.entity.Community;
import com.joymeter.entity.Concentrator;
import com.joymeter.entity.EventRecord;
import com.joymeter.entity.FeeType;
import com.joymeter.entity.HouseCoefficient;
import com.joymeter.entity.ParentMeterConf;
import com.joymeter.entity.OperateInfo;
import com.joymeter.entity.PubReductionFee;
import com.joymeter.entity.ReadInfo;
import com.joymeter.entity.ReadParameter;
import com.joymeter.entity.Role;
import com.joymeter.entity.RoomInfo;
import com.joymeter.entity.SysLog;
import com.joymeter.entity.Unit;
import com.joymeter.entity.User;
import com.joymeter.entity.UserManageInfo;
import com.joymeter.entity.WaterSupplier;

public interface UserDao {

    public int validate_community(String community_name);

    public int validate_building(String building_name);

    public int validate_unit(String unit_name);

    public Admin findByName(String account_name);

    public List<User> findAllUser();

    public int findMeterChangeNo();

    public int findUserDelNo();

    public int findOverdueNo();

    public List<ReadInfo> findReadInfoByMonth();

    public int findReadSuccessNo();

    public int findReadFailNo();

    public int findDeductFailNo();

    public int findCommunityNumber();

    public int findBuildingNumber();

    public int findUnitNumber();

    public int findMbusNumber();

    public int findMeterNumber();

    public int findRechargeNo(String month);

    public int findRefundNo(String month);

    public int findChangeMeterNo(String month);

    public int findDelUserNo(String month);

    public int findRechargeNo1(Map<String, Object> params);

    public int findRefundNo1(Map<String, Object> params);

    public int findChangeMeterNo1(Map<String, Object> params);

    public int findDelUserNo1(Map<String, Object> params);

    public Double findRechargeTotal2(Map<String, Object> params);

    public Double findRefundTotal2(Map<String, Object> params);

    public Double findTotalRecharge();

    public Double findTotalRefund();

    public Double findRechargeAmount(int dayNo);

    public Double findCommunityRecharge(String community_name);

    public List<Admin> loadAllOperators();

    public List<User> findUser(Map<String, Object> params);

    public List<ParentMeterConf> findMotherMeterConf(Map<String, Object> params);

    public int findTotalUser(Map<String, Object> params);

    public int findTotalMotherMeterConf(Map<String, Object> params);

    public void pay(OperateInfo operateInfo);

    public void update(OperateInfo operateInfo);

    public List<OperateInfo> loadOperateInfo(Map<String, Object> params);

    public List<OperateInfo> loadOperateInfoByMeterNo(Map<String, Object> params);

    public List<OperateInfo> loadOperateInfoByMeterNoAndOperate_type(Map<String, Object> params);

    public int findTotalOperateInfoNo(String user_name);

    public int findTotalOperateInfoNoByMeterNo(String meter_no);

    public int findTotalOperateInfoByMeterNoAndOperate_type(Map<String, Object> params);

    public void refund(OperateInfo operateInfo);

    public List<OperateInfo> findOperateInfo(HashMap<String, Object> params);

    public int findAllOperateInfoNo(HashMap<String, Object> params);

    public int findTotalUsersByCommunityName(String community_name);

    public List<User> findUsersByCommunity(String community_name);

    public String findValveNoByMeterNo(String meter_no);

    public Concentrator findConcentrator(String concentrator_name);

    public List<Concentrator> findConcentrators(String concentrator_no);

    public void updateConcentrators(Concentrator concentrator);

    public String findConcentratorNameByMeterNo(String meter_no);

    public List<User> findUserByRoomId(Map<String, Object> params);

    //根据表号,查询出相关的费率信息
    public User findUserByMeterNo(String meter_no);

    public User findUserByValveNo(String valve_no);

    //根据费率名称查询出费率
    public FeeType findFeeTypeByName(String feeTypeName);

    public ReadInfo findLastReadInfoByMeterNo(String meter_no);

    public void saveReadInfo(ReadInfo readInfo);

    public void saveDoubtfulReadInfo(List<ReadInfo> doubtfulReadInfo);

    public void updateValveStatus(Map<String, Object> params);

    public List<ReadInfo> findValveStatus(Timestamp sendTime);

    public List<User> findUsersToRead(Map<String, Object> params);

    public int findTotalUsersNoToRead(Map<String, Object> params);

    public List<ReadInfo> findReadInfo(Map<String, Object> params);

    public List<ReadInfo> findReadInfoCurrent(Map<String, Object> params);

    public List<ReadInfo> findReadInfoFail(Map<String, Object> params);

    public List<ReadInfo> findReadInfoDoubtful(Map<String, Object> params);

    public int findReadInfoTotal(Map<String, Object> params);

    public int findTotalReadInfoCurrent(Map<String, Object> params);

    public int findTotalReadInfoNoFail(Map<String, Object> params);

    public int findTotalReadInfoNoDoubtful(Map<String, Object> params);

    public List<ReadInfo> findReadInfoMonthly(Map<String, Object> params);

    //抄表管理：按照年份查询每月能耗综合
    public Double findTotalPowerMonthly(Map<String, Object> params);

    public int findTotalReadInfoCurrentMonthly(Map<String, Object> params);

    public List<ReadInfo> findReadInfoCurrentMonthly(Map<String, Object> params);

    public List<ReadInfo> findReadInfoMonthlyFail(Map<String, Object> params);

    public List<ReadInfo> findReadInfoMonthlyDoubtful(Map<String, Object> params);

    public int findTotalReadInfoNoMonthly(HashMap<String, Object> params);

    public int findTotalReadInfoNoMonthlyFail(HashMap<String, Object> params);

    public int findTotalReadInfoNoMonthlyDoubtful(HashMap<String, Object> params);

    public void saveReadInfoFailed(ReadInfo readInfo);

    public List<ReadInfo> findDoubtfulReadInfo(Map<String, Object> params);

    public int findTotalNoOfReadInfoFail();

    public int findTotalNoOfDoubtfulReadInfo();

    public void delReadInfoFailed(String meter_no);

    public void delReadInfoFail();

    public List<ReadInfo> queryReadInfoFailed(String meter_no);

    public List<ReadInfo> findReadResult(Timestamp sendTime);

    public List<ReadInfo> findReadResultByOperator(Map<String, Object> params);

    public void updateBalance(Map<String, Object> params);

    public void deletePreReadParameter(String parameter_id);

    public void saveReadParameter(ReadParameter readParameter);

    public List<ReadParameter> findReadParameter();

    public List<User> findUsersRemind(Map<String, Object> params);

    public int findTotalUsersNoRemind(HashMap<String, Object> params);

    public double findBalanceByMeterNo(String meter_no);

    public void markup(String meter_no);

    public void delMarkup(String meter_no);

    public void regist(User user);

    public List<UserManageInfo> findOperateTime(String meter_no);

    public List<UserManageInfo> queryOperateTime(String meter_no);

    public void addManageInfo(UserManageInfo info);

    public void updateStatus(Map<String, Object> params);

    public void updateAccount(Map<String, Object> params);

    public void changeFeeType(Map<String, Object> params);

    public void modifyUserInfo(User user);

    public void updateMotherMeterConf(ParentMeterConf mmc);

    public List<User> findUserBySubNo(String submeter_no);

    public void updateMotherMeter(Map<String, Object> params);

    public void updateUserBySubNo(Map<String, Object> params);

    public void addMotherMeterConf(ParentMeterConf mmc);

    public void updateRoom(User user);

    public void removeUser(int[] user_ids);

    public String[] findMeterNosByUserIDs(int[] user_ids);

    public void removeReadInfoFailedByMeterNos(String[] meterNos);

    public void removeRoomInfoByMeterNos(String[] meterNos);

    public List<SysLog> findUserManageInfo(String user_name);

    public List<WaterSupplier> loadSuppliers(Map<String, Object> params);

    public int findSuppliersNo();

    public void saveWaterSupplier(WaterSupplier supplier);

    public void delWaterSupplier(int supplier_id);

    public void addWaterSupplier(WaterSupplier supplier);

    public List<Community> loadCommunity();

    public void addCommunity(Community community);

    public void modifyCommunity(Map<String, Object> params);

    public void changeCommunity(Map<String, Object> params);

    public void changeConcentrator(Map<String, Object> params);

    public int checkCommunity(String community_name);

    public void delCommunity(Map<String, Object> params);

    public List<Building> loadBuilding(Map<String, Object> params);

    public void modifyBuilding(Map<String, Object> params);

    public void delBuilding(Map<String, Object> params);

    public void addBuilding(Building building);

    public List<Unit> loadUnit(Map<String, Object> params);

    public void modifyUnit(Map<String, Object> params);

    public void delUnit(Map<String, Object> params);

    public void addUnit(Unit unit);

    public List<Concentrator> findAllConcentrators();

    public List<Concentrator> loadConcentrators(HashMap<String, Object> params);

    public int findConcentratorNo();

    public void updateMbusStatus(HashMap<String, Object> params);

    public List<Community> findAllCommunities(HashMap<String, Object> params);

    public Community findCommunityByName(final String name);

    public void modifyConcentrator(Concentrator concentrator);

    public void delConcentrator(int concentrator_id);

    public int findConcentratorStatus(String concentrator_name);

    public void addConcentrator(Concentrator concentrator);

    public List<FeeType> load_feeTypes();

    public void updateFeeTypeStatus(Map<String, Object> params);

    public void addFeeType(FeeType feeType);

    public FeeType findFeeTypeById(int feeTypeId);

    public void modifyFeeType(FeeType feeType);

    public void delFeeType(int feeTypeId);

    public List<HouseCoefficient> load_house_coefficient();

    public int checkCoefficient(String coefficient_name);

    public int findCoefficient(double coefficient);

    public void addCoefficient(HouseCoefficient coefficient);

    public void modifyCoefficient(HashMap<String, Object> params);

    public void delCoefficient(String coefficient_name);

    public List<Admin> findAdmin();

    public Admin findAdminById(Long admin_id);

    public void adminRegist(Admin admin);

    public void delAdmin(String admin_account);

    public void modifyAdmin(Admin admin);

    public void setOperate(SysLog sysLog);

    public List<SysLog> findSysLog();

    public void updateAdminLoadTime(HashMap<String, Object> params);

    public Role findRoleById(Long role_id);

    public Role findRoleByName(String role_name);

    public List<SysLog> findLogByAccount(String admin_account);

    public void addRole(Role role);

    public List<Role> findRole();

    public void modifyRole(Role role);

    public void delRoleById(Long id);

    public void modifyTelById(Map<String, Object> params);

    public void modifyNameById(Map<String, Object> params);

    public void modifyPasswordById(Map<String, Object> params);

    public Admin findRoleFromAdminById(Long id);

    public void clearLanguage();

    public void saveLanguage(String language);

    public String findLanguage();

    public void insertUsers(User user);

    public User checkMeterNoExist(String meter_no);

    public int findNumByMonth(String month);

    public int findRechargeNoByDistrict(Map<String, Object> params);

    public int findRefundNoByDistrict(Map<String, Object> params);

    public int findChangeMeterNoByDistrict(Map<String, Object> params);

    public int findDelUserNoByDistrict(Map<String, Object> params);

    public String[] findRoomsByAddress(Map<String, Object> params);

    public List<User> findMetersByRoomNo(Map<String, Object> params);

    public List<User> findMetersByRoomName(Map<String, Object> params);

    public List<User> findUsersByRoom(Map<String, Object> params);

    public User findUserByRoom(Map<String, Object> params);

    public List<User> findUserByIdCardNo(String id_card_no);

    public String findCommunityNoByName(String community_name);

    //通过楼栋名称查询其对应的房间号及表号
    public List<User> findRoomAndMeter_noByBulid(String buildName);

    //注册房源信息
    public void registRoomInfo(List<RoomInfo> roomInfo);

    //查询房源信息总数
    public int findTotalRoomInfoNo();

    //查询房源信息
    public List<RoomInfo> findRoomInfo(HashMap<String, Object> params);

    //更新房源信息
    public int updateRoomInfo(User user);

    public RoomInfo findRoomInfoByOnlineSyncCode(String onlineSyncCode);

    public Concentrator findConcentratorByMeterNo(String meter_no);

    public List<OperateInfo> findRechargeInfo(Map<String, Object> params);

    public void updateOnline_synv_code(User user);

    public void updateCallBackStatus(RoomInfo info);

    public List<User> findUnCallBackUsers();

    public void updateRegisterStatus(String meter_no);

    public List<User> findUnregisterUsers();

    public User updateSetToken(Map<String, Object> params);

    public void updateCreditTimes(Map<String, Object> params);
    //查询母表下面是否有正常的子表

    public int countCommMeter(String meterNo);

    public void removeParentConf(String meterNo);

    //根据表号查询出母表配置信息
    public ParentMeterConf qryMotherConfInfo(String meterNo);
    // 根据网关信息查询出公有母表下正常使用的子表

    public List<User> qrySubMeter(Map<String, Object> params);

    //将公摊扣费的数据存入表中
    public void setDeductionFee(PubReductionFee params);

    //查询公摊扣费数据
    public List<PubReductionFee> getDeductionFee(Map<String, Object> params);

    //统计公摊扣费记录条数qryAllShareNo
    public int qryAllShareNo(Map<String, Object> params);

    public void updateMeterHouseArea(String meterNo);

    //将公摊扣费剩余的用量存入表中
    public void setRemainAmount(Map<String, Object> params);

    public ReadInfo qryMaxOfThisRead(String meterNo);

    public Double qryMaxOfaverageCount(String meterNo);

    public List<ReadInfo> qryLatestReadInfos(Map<String, Object> params);

    public List<ReadInfo> qrysubLatestReadInfos(Map<String, Object> params);

    public void updateUserMeterStatus(Map<String, Object> params);

    public void addEvent(EventRecord eventRecord);

    public List<User> getMeterByUserName(String user_name);

    public User getMeterByUUID(String uuid);

    public EventRecord getEventInfo(Map<String, Object> params);

    public String findMeterByUUID(String uuid);

    public String findMeterNoByOnline_synv_code(String online_synv_code);

    public void updateWechatBalance(Map<String, Object> map);

    public Double findReadInfoSumData8(final String meter_no);

    public void updateMeterUserSubmeterNo(Map<String, Object> map);

    public List<EventRecord> getEleExceptions(Map<String, Object> map);

    public List<EventRecord> getEleOperations(Map<String, Object> map);

    //根据充值编号查询信息
    public OperateInfo findOperateInfoByOperateId(String operate_id);
    //去重查询出所有的操作类型
    public List<String> loadOperationMethod();
    
    public void updateRoleByAccount(Admin admin);
    
    public void modifyMtertUserFeeType(Map<String,Object> params);

}
