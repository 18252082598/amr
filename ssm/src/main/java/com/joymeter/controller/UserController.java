package com.joymeter.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.joymeter.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/findReadSuccessNo.do")
    public Result findReadSuccessNo() {
        return userService.findReadSuccessNo();
    }

    @ResponseBody
    @RequestMapping("/findReadFailNo.do")
    public Result findReadFailNo() {
        return userService.findReadFailNo();
    }

    @ResponseBody
    @RequestMapping("/findDeductFailNo.do")
    public Result findDeductFailNo() {
        return userService.findDeductFailNo();
    }

    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/urgeToDo.do")
    public Result toDo() {
        return userService.toDo();
    }

    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/findInfo.do")
    public Result findInfo() {
        return userService.findInfo();
    }

    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/beenDone.do")
    public Result beenDone() {
        return userService.beenDone();
    }

    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/rechargeReport.do")
    public Result rechargeReport() {
        return userService.rechargeReport();
    }

    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/rechargeReportAtOneWeek.do")
    public Result rechargeReportAtOneWeek() {
        return userService.rechargeReportAtOneWeek();
    }

    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/rechargeRanking.do")
    public Result rechargeRank() {
        return userService.rechargeRank();
    }

    /**
     *
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("/regist.do")
    public Result regist(User user) {
        return userService.regist(user);
    }
    
     /**
     *
     * @param mmc
     * @return
     */
    @ResponseBody
    @RequestMapping("/addMotherMeterConf.do")
    public Result addMotherMeterConf(ParentMeterConf mmc) {
        return userService.addMotherMeterConf(mmc);
    }

    /**
     *
     * @param meter_no
     * @param user_status
     * @param meter_status
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateStatus.do")
    public Result updateStatus(@RequestParam("meter_no") String meter_no,
            @RequestParam("user_status") String user_status,
            @RequestParam("meter_status") String meter_status) {
        return userService.updateStatus(meter_no, user_status, meter_status);
    }

    /**
     *
     * @param meter_no
     * @return
     */
    @ResponseBody
    @RequestMapping("/findOperateTime.do")
    public Result findOperateTime(@RequestParam("meter_no") String meter_no) {
        return userService.findOperateTime(meter_no);
    }

    /**
     *
     * @param info
     * @return
     */
    @ResponseBody
    @RequestMapping("/applyChangeMeter.do")
    public Result applyChangeMeter(UserManageInfo info) {
        return userService.applyChangeMeter(info);
    }

    /**
     *
     * @param info
     * @return
     */
    @ResponseBody
    @RequestMapping("/removeMeter.do")
    public Result removeMeter(UserManageInfo info) {
        return userService.removeMeter(info);
    }

    /**
     *
     * @param info
     * @param original_meter_no
     * @return
     */
    @ResponseBody
    @RequestMapping("/changeMeter.do")
    public Result changeMeter(UserManageInfo info,
            @RequestParam("original_meter_no") String original_meter_no) {
        return userService.changeMeter(info, original_meter_no);
    }

    /**
     *
     * @param user_name
     * @param id_card_no
     * @param fee_type
     * @param original_fee_type
     * @return
     */
    @ResponseBody
    @RequestMapping("/changeFeeType.do")
    public Result changeFeeType(@RequestParam("user_name") String user_name,
            @RequestParam("id_card_no") String id_card_no,
            @RequestParam("fee_type") String fee_type,
            @RequestParam("original_fee_type") String original_fee_type) {
        return userService.changeFeeType(user_name, id_card_no, fee_type, original_fee_type);
    }

    /**
     *
     * @param info
     * @return
     */
    @ResponseBody
    @RequestMapping("/applyRemoveMeter.do")
    public Result applyRemoveMeter(UserManageInfo info) {
        return userService.applyRemoveMeter(info);
    }

    /**
     *
     * @param info
     * @return
     */
    @ResponseBody
    @RequestMapping("/delMeter.do")
    public Result delMeter(UserManageInfo info) {
        return userService.delMeter(info);
    }

    /**
     *
     * @param info
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveAccount.do")
    public Result saveAccount(UserManageInfo info) {
        return userService.saveAccount(info);
    }

    /**
     *
     * @param info
     * @return
     */
    @ResponseBody
    @RequestMapping("/delUser.do")
    public Result delUser(UserManageInfo info) {
        return userService.delUser(info);
    }

    /**
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
    @ResponseBody
    @RequestMapping("/modifyUserConfirm.do")
    public Result modifyUserInfo(
            User user,
            @RequestParam("original_user_name") String original_user_name,
            @RequestParam("original_user_address_area") String original_user_address_area,
            @RequestParam("original_user_address_community") String original_user_address_community,
            @RequestParam("original_user_address_building") String original_user_address_building,
            @RequestParam("original_user_address_unit") String original_user_address_unit,
            @RequestParam("original_user_address_room") String original_user_address_room,
            @RequestParam("original_concentrator_name") String original_concentrator_name,
            @RequestParam("original_supplier_name") String original_supplier_name,
            @RequestParam("original_contact_info") String original_contact_info,
            @RequestParam("original_id_card_no") String original_id_card_no,
            @RequestParam("original_meter_type") String original_meter_type,
            @RequestParam("original_meter_model") String original_meter_model,
            @RequestParam("original_protocol_type") String original_protocol_type,
            @RequestParam("original_valve_protocol") String original_valve_protocol,
            @RequestParam("original_meter_no") String original_meter_no,
            @RequestParam("original_submeter_no") String original_submeter_no,
            @RequestParam("original_valve_no") String original_valve_no,
            @RequestParam("original_pay_type") String original_pay_type,
            @RequestParam("original_initing_data") Double original_initing_data) {
        return userService.updateUser(user, original_user_name, original_user_address_area,
                original_user_address_community, original_user_address_building,
                original_user_address_unit, original_user_address_room,
                original_concentrator_name, original_supplier_name,
                original_contact_info, original_id_card_no,
                original_meter_type, original_meter_model,
                original_protocol_type, original_valve_protocol,
                original_meter_no, original_submeter_no, original_valve_no,
                original_pay_type, original_initing_data);
    }
    
   
   


    /**
     *
     * @param user_ids
     * @param user_names
     * @return
     */
    @ResponseBody
    @RequestMapping("/removeUser.do")
    public Result deleteUser(@RequestParam("user_ids") String user_ids,
            @RequestParam("user_names") String user_names) {
        return userService.removeUser(user_ids, user_names);
    }

    /**
     *
     * @param user_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/findUserManageInfo.do")
    public Result findUserManageInfo(@RequestParam("user_name") String user_name) {
        return userService.findUserManageInfo(user_name);
    }

    /**
     *
     * @param province
     * @param city
     * @param district
     * @param startNo
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadSuppliers.do")
    public Result loadSuppliers(
            @RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("startNo") int startNo,
            @RequestParam("pageSize") int pageSize) {
        return userService.loadSuppliers(province, city, district, startNo, pageSize);
    }

    /**
     *
     * @param supplier
     * @return
     */
    @ResponseBody
    @RequestMapping("/addSupplier.do")
    public Result addWaterSupplier(WaterSupplier supplier) {
        return userService.addWaterSupplier(supplier);
    }

    /**
     *
     * @param original_supplier_name
     * @param original_province
     * @param original_city
     * @param original_district
     * @param original_supplier_address
     * @param supplier
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveSupplier.do")
    public Result saveWaterSupplier(
            @RequestParam("original_supplier_name") String original_supplier_name,
            @RequestParam("original_province") String original_province,
            @RequestParam("original_city") String original_city,
            @RequestParam("original_district") String original_district,
            @RequestParam("original_supplier_address") String original_supplier_address,
            WaterSupplier supplier) {
        return userService.saveWaterSupplier(original_supplier_name,
                original_province, original_city, original_district,
                original_supplier_address, supplier);
    }

    /**
     *
     * @param supplier_id
     * @param supplier_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteSupplier.do")
    public Result delWaterSupplier(
            @RequestParam("supplier_id") int supplier_id,
            @RequestParam("supplier_name") String supplier_name) {
        return userService.delWaterSupplier(supplier_id, supplier_name);
    }

    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadCommunity.do")
    public Result loadCommunity() {
        return userService.loadCommunity();
    }

    /**
     *
     * @param community
     * @return
     */
    @ResponseBody
    @RequestMapping("/addCommunity.do")
    public Result addCommunity(Community community) {
        return userService.addCommunity(community);
    }

    /**
     *
     * @param community_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkCommunity.do")
    public Result checkCommunity(
            @RequestParam("community_name") String community_name) {
        return userService.checkCommunity(community_name);
    }

    /**
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
    @ResponseBody
    @RequestMapping("/modifyCommunity.do")
    public Result modifyCommunity(@RequestParam("check") boolean check,
            @RequestParam("community_id") String community_id,
            @RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("community_name") String community_name,
            @RequestParam("new_province") String new_province,
            @RequestParam("new_city") String new_city,
            @RequestParam("new_district") String new_district,
            @RequestParam("new_community_name") String new_community_name,
            @RequestParam("new_community_no") String new_community_no) {
        return userService.modifyCommunity(check, community_id, province,
                city, district, community_name, new_province, new_city,
                new_district, new_community_name, new_community_no);
    }

    /**
     *
     * @param province
     * @param city
     * @param district
     * @param community_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/delCommunity.do")
    public Result delCommunity(@RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("community_name") String community_name) {
        return userService.delCommunity(province, city, district, community_name);
    }

    /**
     *
     * @param province
     * @param city
     * @param district
     * @param community_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadBuilding.do")
    public Result loadBuilding(@RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("community_name") String community_name) {
        return userService.loadBuilding(province, city, district, community_name);
    }

    /**
     *
     * @param province
     * @param city
     * @param district
     * @param community_name
     * @param building_name
     * @param new_building_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyBuilding.do")
    public Result modifyBuilding(@RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("community_name") String community_name,
            @RequestParam("building_name") String building_name,
            @RequestParam("new_building_name") String new_building_name) {
        return userService.modifyBuilding(province, city, district,
                community_name, building_name, new_building_name);
    }

    /**
     *
     * @param province
     * @param city
     * @param district
     * @param community_name
     * @param building_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/delBuilding.do")
    public Result delBuilding(@RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("community_name") String community_name,
            @RequestParam("building_name") String building_name) {
        return userService.delBuilding(province, city, district, community_name, building_name);
    }

    /**
     *
     * @param building
     * @return
     */
    @ResponseBody
    @RequestMapping("/addBuilding.do")
    public Result addBuilding(Building building) {
        return userService.addBuilding(building);
    }

    /**
     *
     * @param province
     * @param city
     * @param district
     * @param community_name
     * @param building_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadUnit.do")
    public Result loadUnit(@RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("community_name") String community_name,
            @RequestParam("building_name") String building_name) {
        return userService.loadUnit(province, city, district,
                community_name, building_name);
    }

    /**
     *
     * @param province
     * @param city
     * @param district
     * @param community_name
     * @param building_name
     * @param unit_name
     * @param new_unit_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyUnit.do")
    public Result modifyUnit(@RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("community_name") String community_name,
            @RequestParam("building_name") String building_name,
            @RequestParam("unit_name") String unit_name,
            @RequestParam("new_unit_name") String new_unit_name) {
        return userService.modifyUnit(province, city, district,
                community_name, building_name, unit_name, new_unit_name);
    }

    /**
     *
     * @param province
     * @param city
     * @param district
     * @param community_name
     * @param building_name
     * @param unit_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/delUnit.do")
    public Result delUnit(@RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("community_name") String community_name,
            @RequestParam("building_name") String building_name,
            @RequestParam("unit_name") String unit_name) {
        return userService.delUnit(province, city, district,
                community_name, building_name, unit_name);
    }

    /**
     *
     * @param unit
     * @return
     */
    @ResponseBody
    @RequestMapping("/addUnit.do")
    public Result addUnit(Unit unit) {
        return userService.addUnit(unit);
    }

    /**
     *
     * @param community_name
     * @param building_name
     * @param unit_name
     * @param startNo
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadConcentrators.do")
    public Result loadConcentrators(
            @RequestParam("community_name") String community_name,
            @RequestParam("building_name") String building_name,
            @RequestParam("unit_name") String unit_name,
            @RequestParam("startNo") int startNo,
            @RequestParam("pageSize") int pageSize) {
        return userService.loadConcentrators("", "", "", startNo, pageSize);
        //return userService.loadConcentrators(community_name, building_name, unit_name, startNo, pageSize);
    }

    /**
     *
     * @param mbus_id
     * @param mbus_status
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateMbusStatus.do")
    public Result updateMbusStatus(@RequestParam("mbus_id") String mbus_id,
            @RequestParam("mbus_status") String mbus_status) {
        return userService.updateMbusStatus(mbus_id, mbus_status);
    }

    /**
     *
     * @param concentrator
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping("/addConcentrator.do")
    public Result addConcentrator(Concentrator concentrator, HttpServletRequest req) {
        return userService.addConcentrator(concentrator, req);
    }

    /**
     *
     * @param province
     * @param city
     * @param district
     * @return
     */
    @ResponseBody
    @RequestMapping("/findAllCommunities.do")
    public Result findAllCommunities(@RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district) {
        return userService.findAllCommunities(province, city, district);
    }

    /**
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
    @ResponseBody
    @RequestMapping("/modifyConcentrator.do")
    public Result modifyConcentrator(
            @RequestParam(value = "original_concentrator_name", required = false) String original_concentrator_name,
            @RequestParam(value = "original_concentrator_no", required = false) String original_concentrator_no,
            @RequestParam(value = "original_gateway_id", required = false) String original_gateway_id,
            @RequestParam(value = "original_concentrator_ip", required = false) String original_concentrator_ip,
            @RequestParam(value = "original_concentrator_port", required = false) String original_concentrator_port,
            @RequestParam(value = "original_DTU_sim_no", required = false) String original_DTU_sim_no,
            @RequestParam(value = "original_concentrator_model", required = false) String original_concentrator_model,
            @RequestParam(value = "original_community", required = false) String original_community,
            @RequestParam(value = "original_building", required = false) String original_building,
            @RequestParam(value = "original_unit", required = false) String original_unit,
            Concentrator concentrator) {
        return userService.modifyConcentrator(
                original_concentrator_name, original_concentrator_no, original_gateway_id,
                original_concentrator_ip, original_concentrator_port,
                original_DTU_sim_no, original_concentrator_model, original_community, original_building,
                original_unit, concentrator);
    }
    
    @ResponseBody
    @RequestMapping("/addConcentratorValidate.do")
    public Result addConcentratorValidate(String concentrator_no){
        return  userService.addConcentratorValidate(concentrator_no);
    }
    /**
     *
     * @param concentrator_id
     * @param concentrator_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteConcentrator.do")
    public Result delConcentrator(
            @RequestParam("concentrator_id") int concentrator_id,
            @RequestParam("concentrator_name") String concentrator_name) {
        return userService.delConcentrator(concentrator_id, concentrator_name);
    }

    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadFeeTypes.do")
    public Result load_feeTypes() {
        return userService.load_feeTypes();
    }

    /**
     *
     * @param feeType
     * @return
     */
    @ResponseBody
    @RequestMapping("/addFeeType.do")
    public Result addFeeType(FeeType feeType) {
        return userService.addFeeType(feeType);
    }

    /**
     *
     * @param feeTypeId
     * @return
     */
    @ResponseBody
    @RequestMapping("/findFeeTypeById.do")
    public Result findFeeTypeById(@RequestParam("feeTypeId") int feeTypeId) {
        return userService.findFeeTypeById(feeTypeId);
    }

    /**
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
     * @param original_levelThreeStartVolume
     * @param original_levelThreeUnitPrice
     * @param original_levelFourStartVolume
     * @param original_levelFourUnitPrice
     * @param original_levelFiveStartVolume
     * @param original_levelFiveUnitPrice
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyFeeType.do")
    public Result modifyFeeType(
            FeeType feeType,
            @RequestParam("original_feeTypeName") String original_feeTypeName,
            @RequestParam("original_meterType") String original_meterType,
            @RequestParam("original_basicUnitPrice") Double original_basicUnitPrice,
            @RequestParam("original_disposingUnitCost") Double original_disposingUnitCost,
            @RequestParam("original_otherCost") Double original_otherCost,
            @RequestParam("original_extraCost") Double original_extraCost,
            @RequestParam("original_paymentMethod") String original_paymentMethod,
            @RequestParam("original_isLevelPrice") String original_isLevelPrice,
            @RequestParam("original_levelOneStartVolume") Double original_levelOneStartVolume,
            @RequestParam("original_levelOneUnitPrice") Double original_levelOneUnitPrice,
            @RequestParam("original_levelTwoStartVolume") Double original_levelTwoStartVolume,
            @RequestParam("original_levelTwoUnitPrice") Double original_levelTwoUnitPrice,
            @RequestParam("original_levelThreeStartVolume") Double original_levelThreeStartVolume,
            @RequestParam("original_levelThreeUnitPrice") Double original_levelThreeUnitPrice,
            @RequestParam("original_levelFourStartVolume") Double original_levelFourStartVolume,
            @RequestParam("original_levelFourUnitPrice") Double original_levelFourUnitPrice,
            @RequestParam("original_levelFiveStartVolume") Double original_levelFiveStartVolume,
            @RequestParam("original_levelFiveUnitPrice") Double original_levelFiveUnitPrice) {
        return userService.modifyFeeType(feeType,
                original_feeTypeName, original_meterType,
                original_basicUnitPrice, original_disposingUnitCost,
                original_otherCost, original_extraCost, original_paymentMethod,
                original_isLevelPrice, original_levelOneStartVolume,
                original_levelOneUnitPrice, original_levelTwoStartVolume,
                original_levelTwoUnitPrice, original_levelThreeStartVolume,
                original_levelThreeUnitPrice, original_levelFourStartVolume,
                original_levelFourUnitPrice, original_levelFiveStartVolume,
                original_levelFiveUnitPrice);
    }

    /**
     *
     * @param feeTypeId
     * @param feeTypeName
     * @return
     */
    @ResponseBody
    @RequestMapping("/delFeeType.do")
    public Result delFeeType(@RequestParam("feeTypeId") int feeTypeId,
            @RequestParam("feeTypeName") String feeTypeName) {
        return userService.delFeeType(feeTypeId, feeTypeName);
    }

    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/load_house_coefficient.do")
    public Result load_house_coefficient() {
        return userService.load_house_coefficient();
    }

    /**
     *
     * @param coefficient
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkCoefficient.do")
    public Result checkCoefficient(HouseCoefficient coefficient) {
        return userService.checkCoefficient(coefficient);
    }

    /**
     *
     * @param coefficient
     * @return
     */
    @ResponseBody
    @RequestMapping("/addCoefficient.do")
    public Result addCoefficient(HouseCoefficient coefficient) {
        return userService.addCoefficient(coefficient);
    }

    /**
     *
     * @param coefficient_id
     * @param coefficient_name
     * @param coefficient
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyCoefficient.do")
    public Result modifyCoefficient(
            @RequestParam("coefficient_id") int coefficient_id,
            @RequestParam("coefficient_name") String coefficient_name,
            @RequestParam("coefficient") String coefficient) {
        return userService.modifyCoefficient(coefficient_id, coefficient_name, coefficient);
    }

    /**
     *
     * @param coefficient_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteCoefficient.do")
    public Result delCoefficient(
            @RequestParam("coefficient_name") String coefficient_name) {
        return userService.delCoefficient(coefficient_name);
    }
    
     /**
     *
     * @param province
     * @param city
     * @param district
     * @param beginDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping("/rechargeRefundAnalyze.do")
    public Result rechargeRefundAnalyze(
            @RequestParam("province")String province,
            @RequestParam("city")String city,
            @RequestParam("district")String district,
            @RequestParam("beginDate")String beginDate,
            @RequestParam("endDate")String endDate
    ){
        return userService.rechargeRefundAnalyze(province, city, district, beginDate, endDate);
    }
    
    /**
     *
     * @param province
     * @param city
     * @param district
     * @param beginDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping("/BusinessQueries.do")
    public Result BusinessQueries(
            @RequestParam("province")String province,
            @RequestParam("city")String city,
            @RequestParam("district")String district,
            @RequestParam("beginDate")String beginDate,
            @RequestParam("endDate")String endDate
    ){
        return userService.businessQueries(province, city, district, beginDate, endDate);
    }
    
    /**
     *
     * @param req
     * @param language
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveLanguage.do")
    public Result saveLanguage(HttpServletRequest req,
            @RequestParam("language") String language) {
        return userService.saveLanguage(req, language);
    }

    /**
     *
     * @param community_name
     * @param building_name
     * @param unit_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/findRoomsByAddress.do")
    public Result findRoomsByAddress(
            @RequestParam("community_name") String community_name,
            @RequestParam("building_name") String building_name,
            @RequestParam("unit_name") String unit_name) {
        return userService.findRoomsByAddress(community_name, building_name, unit_name);
    }
}
