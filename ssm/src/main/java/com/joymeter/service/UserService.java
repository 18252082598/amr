package com.joymeter.service;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.joymeter.entity.Building;
import com.joymeter.entity.Community;
import com.joymeter.entity.Concentrator;
import com.joymeter.entity.FeeType;
import com.joymeter.entity.HouseCoefficient;
import com.joymeter.entity.ParentMeterConf;
import com.joymeter.entity.Result;
import com.joymeter.entity.Unit;
import com.joymeter.entity.User;
import com.joymeter.entity.UserManageInfo;
import com.joymeter.entity.WaterSupplier;
import java.util.Map;

public interface UserService {

    public Result checkLogin(String account_name, String password);

    public Result findReadSuccessNo();

    public Result findReadFailNo();

    public Result findDeductFailNo();

    public Result toDo();

    public Result findInfo();

    public Result beenDone();

    public Result rechargeReport();

    public Result rechargeReportAtOneWeek();

    public Result rechargeRank();

    public Result regist(User user);
    
    public Result addMotherMeterConf(ParentMeterConf mmc);

    public Result updateStatus(String meter_no, String user_status,
            String meter_status);

    public Result findOperateTime(String meterNo);

    public Result applyChangeMeter(UserManageInfo info);

    public Result removeMeter(UserManageInfo info);

    public Result changeMeter(UserManageInfo info, String original_meter_no);

    public Result changeFeeType(String user_name, String id_card_no,
            String fee_type, String original_fee_type);

    public Result applyRemoveMeter(UserManageInfo info);

    public Result delMeter(UserManageInfo info);

    public Result saveAccount(UserManageInfo info);

    public Result delUser(UserManageInfo info);

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
            Double original_initing_data);

    public Result updateRoom(User user);

    public Result removeUser(String user_ids, String user_names);

    public Result findUserManageInfo(String user_name);

    public Result loadSuppliers(String province, String city, String district, int startNo, int pageNo);

    public Result saveWaterSupplier(String original_supplier_name,
            String original_province,
            String original_city,
            String original_district,
            String original_supplier_address,
            WaterSupplier supplier);

    public Result delWaterSupplier(int supplier_id, String supplier_name);

    public Result addWaterSupplier(WaterSupplier supplier);

    public Result loadCommunity();

    public Result checkCommunity(String community_name);

    public Result modifyCommunity(boolean check, String community_id,
            String province, String city, String district, String community_name,
            String new_province, String new_city, String new_district,
            String new_community_name, String new_community_no);

    public Result addCommunity(Community community);

    public Result delCommunity(String province, String city, String district,
            String community_name);

    public Result loadBuilding(String province, String city, String district,
            String community_name);

    public Result modifyBuilding(String province, String city, String district,
            String communityName, String buildingName, String newBuildingName);

    public Result delBuilding(String province, String city, String district,
            String community_name, String building_name);

    public Result addBuilding(Building building);

    public Result loadUnit(String province, String city, String district,
            String community_name, String building_name);

    public Result modifyUnit(String province, String city, String district,
            String community_name, String building_name, String unit_name,
            String new_unit_name);

    public Result delUnit(String province, String city, String district,
            String community_name, String building_name, String unit_name);

    public Result addUnit(Unit unit);

    public Result loadConcentrators(String community_name, String building_name,
            String unit_name, int startNo, int pageSize);

    public Result updateMbusStatus(String mbus_id, String mbus_status);

    public Result findAllCommunities(String province, String city,
            String district);

    public Result addConcentrator(Concentrator concentrator, HttpServletRequest req);

    public Result modifyConcentrator(String original_concentrator_name,
            String original_concentrator_no, String original_gateway_id, String original_concentrator_ip,
            String original_concentrator_port, String original_DTU_sim_no, String original_concentrator_model,
            String original_community, String original_building,
            String original_unit, Concentrator concentrator);

    public Result delConcentrator(int concentrator_id, String concentrator_name);

    public Result load_feeTypes();

    public Result addFeeType(FeeType feeType);

    public Result findFeeTypeById(int feeTypeId);

    public Result modifyFeeType(FeeType feeType, String original_feeTypeName,
            String original_meterType, Double original_basicUnitPrice,
            Double original_disposingUnitCost, Double original_otherCost,
            Double original_extraCost, String original_paymentMethod,
            String original_isLevelPrice, Double original_levelOneStartVolume,
            Double original_levelOneUnitPrice,
            Double original_levelTwoStartVolume,
            Double original_levelTwoUnitPrice,
            Double original_levelThreetartVolume,
            Double original_levelThreeUnitPrice,
            Double original_levelFourStartVolume,
            Double original_levelFourUnitPrice,
            Double original_levelFiveStartVolume,
            Double original_levelFiveUnitPrice);

    public Result delFeeType(int feeTypeId, String feeTypeName);

    public Result load_house_coefficient();

    public Result checkCoefficient(HouseCoefficient coefficient);

    public Result addCoefficient(HouseCoefficient coefficient);

    public Result modifyCoefficient(int coefficient_id,
            String coefficient_name, String coefficient);

    public Result delCoefficient(String coefficient_name);

    public Result printReport(HttpServletRequest request,
            HttpServletResponse response, String operate_id, String user_name,
            String meter_no, String operate_money, String pay_method,
            String operate_type, String fee_type, String operate_time,
            String operator_account) throws IOException;

    public Result businessQueries(String province, String city, String district, String beginDate, String endDate);
    
    public Result rechargeRefundAnalyze(String province, String city, String district, String beginDate, String endDate);

    public Result saveLanguage(HttpServletRequest req, String language);

    public Result findLanguage();

    public Result findRoomsByAddress(String community_name, String building_name, String unit_name);

    public Result findMetersByRoomNo(String community_no, String user_address_room, String meter_type);

    public Result findMetersByRoomName(String community_name, String user_address_room, String meter_type);

    public Result findUsersByRoom(String community_no);

    public Result addConcentratorValidate(String concentrator_no);

    //通过楼栋名称查询其对应的房间号和表号
    public Result findRoomAndMeterNoByBulid(String buildName);
    
    //注册房源信息
    public Result registRoomInfo(String jsonParams);
    
    public Result findUserByMeterNo(String meterNo);
    //根据电表 uuid 获取电表基本信息 
    public Map<String, Object> elemeter_basic_info(String uuid); 
}
