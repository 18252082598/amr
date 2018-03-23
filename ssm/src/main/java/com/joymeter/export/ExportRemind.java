/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.export;

import com.joymeter.entity.User;
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
public class ExportRemind extends ExportReadInfo {

    @Override
    String[] excelHeader_cn() {
        String[] excelHeader = {"催缴状态", "用户名称", "用户所在地区", "小区", "楼栋",
            "单元", "房号", "联系电话", "身份证号", "网点", "户型系数", "集中器名称", "表号", "账面余额", "操作员", "抄表时间"};
        return excelHeader;
    }

    @Override
    String[] excelHeader_en() {
        String[] excelHeader = {"Status", "User Name", "Area", "Block", "Building",
            "Unit", "Room No.", "Tel", "ID Card No.", "Supplier Name", "Coefficient Name", "Concentrator Name", "Meter No.", "Balance", "Operator", "Last Read Time"};
        return excelHeader;
    }

    @Override
    String[] excelHeader_dbf() {
        String[] excelHeader = {"Status", "User_Name", "Area", "Block", "Building",
            "Unit", "Room_Num", "Tel", "Id_Card", "Supplier", "Coeff", "Con_Name", "Meter_Num", "Balance", "Operator", "Last_Time"};
        return excelHeader;
    }

    @Override
    void changeStyle(int i, XSSFSheet sheet) {
        switch (i) {
            case 15:
                sheet.setColumnWidth(i, 30 * 256);
                break;
            case 2:
            case 7:
            case 8:
            case 9:
            case 11:
                sheet.setColumnWidth(i, 20 * 256);
                break;
            default:
                sheet.setColumnWidth(i, 10 * 256);
                break;
        }
    }

    @Override
    void fillInExcel(Object obj, XSSFRow row) {
        User reminder = (User) obj;
        String payRemind = reminder.getPay_remind();
        String isRemind = "1".equals(payRemind) ? "已催缴" : "未催缴";
        row.createCell(0).setCellValue(isRemind);
        row.createCell(1).setCellValue(reminder.getUser_name());
        row.createCell(2).setCellValue(reminder.getUser_address_area());
        row.createCell(3).setCellValue(reminder.getUser_address_community());
        row.createCell(4).setCellValue(reminder.getUser_address_building());
        row.createCell(5).setCellValue(reminder.getUser_address_unit());
        row.createCell(6).setCellValue(reminder.getUser_address_room());
        row.createCell(7).setCellValue(reminder.getContact_info());
        row.createCell(8).setCellValue(reminder.getId_card_no());
        row.createCell(9).setCellValue(reminder.getSupplier_name());
        row.createCell(10).setCellValue(reminder.getCoefficient_name());
        row.createCell(11).setCellValue(reminder.getConcentrator_name());
        row.createCell(12).setCellValue(reminder.getMeter_no());
        row.createCell(13).setCellValue(reminder.getLast_balance());
        row.createCell(14).setCellValue(reminder.getOperator_account());
        String operate_time = "" + reminder.getLast_read_time();
        row.createCell(15).setCellValue(operate_time);
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
            User reminder = (User) obj;
            Object rowData[] = new Object[headers.length];
            String payRemind = reminder.getPay_remind();
            String isRemind = "1".equals(payRemind) ? "已催缴" : "未催缴";
            rowData[0] = isRemind;
            rowData[1] = reminder.getUser_name();
            rowData[2] = reminder.getUser_address_area();
            rowData[3] = reminder.getUser_address_community();
            rowData[4] = reminder.getUser_address_building();
            rowData[5] = reminder.getUser_address_unit();
            rowData[6] = reminder.getUser_address_room();
            rowData[7] = reminder.getContact_info();
            rowData[8] = reminder.getId_card_no();
            rowData[9] = reminder.getSupplier_name();
            rowData[10] = reminder.getCoefficient_name();
            rowData[11] = reminder.getConcentrator_name();
            rowData[12] = reminder.getMeter_no();
            rowData[13] = reminder.getLast_balance();
            rowData[14] = reminder.getOperator_account();
            String operate_time = "" + reminder.getLast_read_time();
            rowData[15] = operate_time;
             writer.setCharset(Charset.forName("GBK"));
            writer.addRecord(rowData);
        } catch (DBFException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
