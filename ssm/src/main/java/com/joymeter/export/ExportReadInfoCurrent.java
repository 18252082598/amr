/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.export;

import com.joymeter.entity.ReadInfo;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 *
 * @author Administrator
 */
public class ExportReadInfoCurrent extends ExportReadInfo {

    @Override
    String[] excelHeader_cn() {
        String[] excelHeader = {"用户名称", "小区", "楼栋", "单元", "房号",
            "联系电话", "集中器名称", "表出厂编号", "费率类型", "本期读数","公摊用/余量","最终用量", "操作员", "抄表时间"};
        return excelHeader;
    }

    @Override
    String[] excelHeader_en() {
        String[] excelHeader = {"User Name", "Block", "Building", "Unit", "Room No.",
            "Tel", "Concentrator Name", "Meter No", "Tariff Type", "Last Volume","Margin","Final amount", "Operator", "Read Time"};
        return excelHeader;
    }

    /**
     * 导入到Excel
     *
     * @param obj
     * @param row
     */
    @Override
    void fillInExcel(Object obj, XSSFRow row) {
        ReadInfo readInfo = (ReadInfo) obj;
        row.createCell(0).setCellValue(readInfo.getUser_name());
        row.createCell(1).setCellValue(readInfo.getUser_address_community());
        row.createCell(2).setCellValue(readInfo.getUser_address_building());
        row.createCell(3).setCellValue(readInfo.getUser_address_unit());
        row.createCell(4).setCellValue(readInfo.getUser_address_room());
        row.createCell(5).setCellValue(readInfo.getContact_info());
        row.createCell(6).setCellValue(readInfo.getConcentrator_name());
        row.createCell(7).setCellValue(readInfo.getMeter_no());
        row.createCell(8).setCellValue(readInfo.getFee_type());
        String this_read = readInfo.getThis_read() == null ? "0" : "" + dataFormat(readInfo.getThis_read());
        row.createCell(9).setCellValue(this_read);
        row.createCell(10).setCellValue(readInfo.getData8());
         BigDecimal  b1 = new BigDecimal(this_read);
         BigDecimal  b2 = new BigDecimal(readInfo.getData8());
         BigDecimal  b3 = b1.add(b2);
        row.createCell(11).setCellValue(b3.doubleValue());
        row.createCell(12).setCellValue(readInfo.getOperator_account());
        String operate_time = "" + readInfo.getOperate_time();
        row.createCell(13).setCellValue(operate_time);
    }
}
