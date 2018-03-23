/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.export;

import com.joymeter.entity.OperateInfo;
import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFWriter;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 *
 * @author Administrator
 */
public class ExportRechargeInfo extends ExportReadInfo {

    @Override
    String[] excelHeader_cn() {
        String[] excelHeader = {"业务编号", "用户名称", "用户所在地区", "小区", "楼栋",
            "单元", "房号", "联系电话", "仪表类型", "表号", "费率类型", "操作金额",
            "操作类型(充值 1/退费 0/抄表扣费 2/公摊扣费 3/公摊退费 4)", "账面余额", "是否打印(1-是, 0-否)", "操作员", "支付方式", "充值地点", "仪表型号", "集中器名称", "操作时间"};
        return excelHeader;
    }

    @Override
    String[] excelHeader_en() {
        String[] excelHeader = {"Business No.", "User Name", "Area", "Block", "Building",
            "Unit", "Room No.", "Tel", "Meter Type", "Meter No", "Tariff Type", "Recharge Money",
            "Operate Type(1-Top up, 0-Refund,2-Charge,3-Collateral deductions,4-Public refund)", "Balance", "Is Print(1-Y, 0-N)", "Operate", "Pay Method", "Recharge Loc", "Meter Model", "Concentrator Name", "Operate Time"};
        return excelHeader;
    }

    @Override
    String[] excelHeader_dbf() {
        String[] excelHeader = {"Bus_Num", "User_Name", "Area", "Block", "Building",
            "Unit", "Room_Num", "Tel", "Meter_Type", "Meter_No", "Tar_Type", "Recharge",
            "Oper_Type", "Balance", "Is_Print", "Operate", "Pay_Method", "Location", "MeterModel", "Con_Name", "Oper_Time"};
        return excelHeader;
    }

    @Override
    void changeStyle(int i, XSSFSheet sheet) {
        switch (i) {
            case 20:
                sheet.setColumnWidth(i, 30 * 256);
                break;
            case 0:
            case 2:
            case 7:
            case 10:
            case 17:
            case 19:
                sheet.setColumnWidth(i, 20 * 256);
                break;
            default:
                sheet.setColumnWidth(i, 10 * 256);
                break;
        }
    }

    @Override
    void fillInExcel(Object obj, XSSFRow row) {
        OperateInfo info = (OperateInfo) obj;
        row.createCell(0).setCellValue(info.getOperate_id());
        row.createCell(1).setCellValue(info.getUser_name());
        row.createCell(2).setCellValue(info.getUser_address_area());
        row.createCell(3).setCellValue(info.getUser_address_community());
        row.createCell(4).setCellValue(info.getUser_address_building());
        row.createCell(5).setCellValue(info.getUser_address_unit());
        row.createCell(6).setCellValue(info.getUser_address_room());
        row.createCell(7).setCellValue(info.getContact_info());
        row.createCell(8).setCellValue(info.getMeter_type());
        row.createCell(9).setCellValue(info.getMeter_no());
        row.createCell(10).setCellValue(info.getFee_type());
        row.createCell(11).setCellValue(info.getRecharge_money());
        String operateType = "";
        if("1".equals(info.getOperate_type())){
            operateType = "充值";
        }else if("0".equals(info.getOperate_type())){
             operateType = "退费";
        }else if("2".equals(info.getOperate_type())){
             operateType = "抄表扣费";
        }else if("3".equals(info.getOperate_type())){
             operateType = "公摊扣费";
        }else if("4".equals(info.getOperate_type())){
             operateType = "公摊退费";
        }
       // String operateType = "1".equals(info.getOperate_type()) ? "充值" : "退费";
        row.createCell(12).setCellValue(operateType);
        row.createCell(13).setCellValue(info.getBalance());
        String isPrint = "1".equals(info.getIsPrint()) ? "已打印" : "未打印";
        row.createCell(14).setCellValue(isPrint);
        row.createCell(15).setCellValue(info.getOperator_account());
        row.createCell(16).setCellValue(info.getPay_method());
        row.createCell(17).setCellValue(info.getRecharge_loc());
        row.createCell(18).setCellValue(info.getMeter_model());
        row.createCell(19).setCellValue(info.getConcentrator_name());
        String operate_time = "" + info.getOperate_time();
        row.createCell(20).setCellValue(operate_time);
    }

    /**
     * 导出到DBF
     *
     * @param obj
     * @param writer
     */
    @Override
    void fillInDbf(Object obj, DBFWriter writer, final String[] headers) {
        try {
            OperateInfo info = (OperateInfo) obj;
            Object rowData[] = new Object[headers.length];
            rowData[0] = info.getOperate_id();
            rowData[1] = info.getUser_name();
            rowData[2] = info.getUser_address_area();
            rowData[3] = info.getUser_address_community();
            rowData[4] = info.getUser_address_building();
            rowData[5] = info.getUser_address_unit();
            rowData[6] = info.getUser_address_room();
            rowData[7] = info.getContact_info();
            rowData[8] = info.getMeter_type();
            rowData[9] = info.getMeter_no();
            rowData[10] = info.getFee_type();
            rowData[11] = info.getRecharge_money();
            String operateType = "1".equals(info.getOperate_type()) ? "充值" : "退费";
            rowData[12] = operateType;
            rowData[13] = info.getBalance();
            String isPrint = "1".equals(info.getIsPrint()) ? "已打印" : "未打印";
            rowData[14] = isPrint;
            rowData[15] = info.getOperator_account();
            rowData[16] = info.getPay_method();
            rowData[17] = info.getRecharge_loc();
            rowData[18] = info.getMeter_model();
            rowData[19] = info.getConcentrator_name();
            String operate_time = "" + info.getOperate_time();
            rowData[20] = operate_time;
             writer.setCharset(Charset.forName("GBK"));
            writer.addRecord(rowData);
        } catch (DBFException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
