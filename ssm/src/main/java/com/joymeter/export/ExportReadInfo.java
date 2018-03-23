/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.export;

import com.joymeter.entity.ReadInfo;
import com.linuxense.javadbf.DBFDataType;
import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Administrator
 */
public class ExportReadInfo {

    //每次设置导出数量  
    public static int PAGE_SIZE = 30000;
    public static int DOWNLOADED = 0;

    String[] excelHeader_cn() {
        String[] excelHeader = {"业务编号", "用户名称", "用户所在地区", "小区", "楼栋", "单元", "房号",
            "联系电话", "集中器名称", "表出厂编号", "费率类型", "本期读数", "上期读数",
            "本期用量", "上期用量", "公摊用/余量","需扣款", "账面余额", "抄表状态(1-成功, 0-失败)", "扣款状态(1-成功, 0-失败)",
            "抄表类型(1-自动, 0-手动)", "操作员", "抄表时间"};
        return excelHeader;
    }

    String[] excelHeader_en() {
        String[] excelHeader = {"Business No.", "User Name", "Area", "Block", "Building", "Unit", "Room No.",
            "Tel", "Concentrator Name", "Meter No", "Tariff Type", "Cur. Volume", "Pre. Volume",
            "Cur. Consuming", "Pre. Consuming","Margin", "Due", "Balance", "Status(1-suc, 0-fai)", "Due STAT(1-suc, 0-fai)",
            "Read Type(1-auto, 0-manually)", "Operator", "Read Time"};
        return excelHeader;
    }

    String[] excelHeader_dbf() {
        String[] excelHeader = {"H1", "H2", "H4", "G1", "G2", "D1", "S1",
            "D2", "S2", "D3", "D4", "G3", "G4", "M1", "M4", "H3", "CBQK", "DH1", "LB1", "YL1", "YL2", "YL3", "YL4",
            "YL5", "YL6", "YL7", "YL8", "YL9", "YL10", "YL11", "YL12", "SS0", "SS1", "CBRQ", "CBTIME", "CBSEQ"};
        return excelHeader;
    }

    /**
     *
     * @param i
     * @param sheet
     */
    void changeStyle(int i, XSSFSheet sheet) {
        switch (i) {
            case 21:
                sheet.setColumnWidth(i, 30 * 256);
                break;
            case 0:
            case 2:
            case 8:
            case 10:
                sheet.setColumnWidth(i, 20 * 256);
                break;
            default:
                sheet.setColumnWidth(i, 10 * 256);
                break;
        }
    }

    /**
     * 导入到Excel
     *
     * @param obj
     * @param row
     */
    void fillInExcel(Object obj, XSSFRow row) {
        ReadInfo readInfo = (ReadInfo) obj;
        row.createCell(0).setCellValue(readInfo.getOperate_id());
        row.createCell(1).setCellValue(readInfo.getUser_name());
        row.createCell(2).setCellValue(readInfo.getUser_address_area());
        row.createCell(3).setCellValue(readInfo.getUser_address_community());
        row.createCell(4).setCellValue(readInfo.getUser_address_building());
        row.createCell(5).setCellValue(readInfo.getUser_address_unit());
        row.createCell(6).setCellValue(readInfo.getUser_address_room());
        row.createCell(7).setCellValue(readInfo.getContact_info());
        row.createCell(8).setCellValue(readInfo.getConcentrator_name());
        row.createCell(9).setCellValue(readInfo.getMeter_no());
        row.createCell(10).setCellValue(readInfo.getFee_type());
        String this_read = readInfo.getThis_read() == null ? "0" : "" + dataFormat(readInfo.getThis_read());
        row.createCell(11).setCellValue(this_read);
        String last_read = readInfo.getLast_read() == null ? "0" : "" + dataFormat(readInfo.getLast_read());
        row.createCell(12).setCellValue(last_read);
        String this_cost = readInfo.getThis_cost() == null ? "0" : "" + dataFormat(readInfo.getThis_cost());
        row.createCell(13).setCellValue(this_cost);
        String last_cost = readInfo.getLast_cost() == null ? "0" : "" + dataFormat(readInfo.getLast_cost());
        row.createCell(14).setCellValue(last_cost);
        String fee_need = readInfo.getFee_need() == null ? "0" : "" + dataFormat(readInfo.getFee_need());
        row.createCell(15).setCellValue(readInfo.getData8());//公摊用/余量
        row.createCell(16).setCellValue(fee_need);
        row.createCell(17).setCellValue(readInfo.getBalance());
        row.createCell(18).setCellValue(readInfo.getException());
        row.createCell(19).setCellValue(readInfo.getFee_status());
        row.createCell(20).setCellValue(readInfo.getRead_type());
        row.createCell(21).setCellValue(readInfo.getOperator_account());
        String operate_time = "" + readInfo.getOperate_time();
        row.createCell(22).setCellValue(operate_time);
    }

    /**
     * 导出到DBF
     *
     * @param obj
     * @param writer
     */
    void fillInDbf(Object obj, DBFWriter writer, final String[] headers) {
        try {
            ReadInfo readInfo = (ReadInfo) obj;
            String this_read = readInfo.getThis_read() == null ? "0" : "" + dataFormat(readInfo.getThis_read());
            String this_cost = readInfo.getThis_cost() == null ? "0" : "" + dataFormat(readInfo.getThis_cost());
            String last_read = readInfo.getLast_read() == null ? "0" : "" + dataFormat(readInfo.getLast_read());
            String last_cost = readInfo.getLast_cost() == null ? "0" : "" + dataFormat(readInfo.getLast_cost());
            Object[] rowData = new Object[headers.length];
            rowData[0] = "1"; // H1
            rowData[1] = readInfo.getUser_name(); // H2
            rowData[2] = "0"; // H4
            rowData[3] = "0"; // G1
            rowData[4] = "0"; // G2
            rowData[5] = last_read; // D1
            rowData[6] = last_cost; // S1
            rowData[7] = this_read; // D2
            rowData[8] = this_cost; // S2
            rowData[9] = "0"; // D3
            rowData[10] = "0"; // D4
            rowData[11] = "0"; // G3
            rowData[12] = "0"; // G4
            rowData[13] = readInfo.getMeter_no(); // M1
            rowData[14] = readInfo.getUser_address(); // M4
            rowData[15] = readInfo.getMeter_no();//H3
            rowData[16] = "0";//CBQK
            rowData[17] = "0";//DH1
            rowData[18] = "0";//LB1
            rowData[19] = "0";//YL1
            rowData[20] = "0";//YL2
            rowData[21] = "0";//YL3
            rowData[22] = "0";//YL4
            rowData[23] = "0";//YL5
            rowData[24] = "0";//YL6
            rowData[25] = "0";//YL7
            rowData[26] = "0";//YL8
            rowData[27] = "0";//YL9
            rowData[28] = "0";//YL10
            rowData[29] = "0";//YL11
            rowData[30] = "0";//YL12
            rowData[31] = "0";//SS0
            rowData[32] = "0";//SS1
            rowData[33] = readInfo.getOperate_day();//CBRQ
            rowData[34] = readInfo.getOperate_dayTime();//CBTIME
            rowData[35] = "0";//CBSEQ
            writer.setCharset(Charset.forName("GBK"));
            writer.addRecord(rowData);
        } catch (DBFException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 创建DBF字段
     *
     * @param headers
     * @return
     */
    public DBFWriter createDbfFields(final String[] headers) {
        try {
            DBFField[] fields = new DBFField[headers.length];
            for (int i = 0; i < headers.length; i++) {
                fields[i] = new DBFField();
                fields[i].setName(headers[i]);
                fields[i].setType(DBFDataType.CHARACTER);
                fields[i].setFieldLength(50);
            }
            DBFWriter writer = new DBFWriter();
            writer.setFields(fields);
            return writer;
        } catch (DBFException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * 默认导出为Excel
     *
     * @param list
     * @param language
     * @param file
     * @return
     * @throws java.io.IOException
     */
    public String export(List<?> list, String language, final String file) throws IOException {
        return export(list, language, file, "EXCEL");
    }

    /**
     * 导出到Excel
     *
     * @param list
     * @param language
     * @param file
     * @param fileType 导出的文件类型 'EXCEL', 'DBF'
     * @return 返回文件名
     * @throws IOException
     */
    public String export(List<?> list, String language, final String file, final String fileType) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Read List Export");
        XSSFRow row = sheet.createRow((int) 0);
        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style.setAlignment(XSSFCellStyle.VERTICAL_CENTER);
        String[] headers = getHeaders(language);
        if (headers == null) {
            return "";
        }
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
            changeStyle(i, sheet);
        }

        DBFWriter dbfWriter = createDbfFields(excelHeader_dbf());
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                Object obj = list.get(i);
                switch (fileType) {
                    case "EXCEL":
                        fillInExcel(obj, row);
                        break;
                    case "DBF":
                        fillInDbf(obj, dbfWriter, excelHeader_dbf());
                        break;
                }
            }          
        }       
        final String fileName = getFileName(fileType, file);
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            switch (fileType) {
                case "EXCEL":
                    wb.write(fos);
                    break;
                case "DBF":
                    dbfWriter.write(fos);
                    break;
            }
        }
        return fileName;
    }

    /**
     * 根据语言获取头字段
     *
     * @param language
     * @return
     */
    private String[] getHeaders(String language) {
        switch (language) {
            case "Chinese":
                return excelHeader_cn();
            case "English":
                return excelHeader_en();
            default:
                return null;
        }
    }

    /**
     * 根据文件类型补全扩展名
     *
     * @param fileType 文件类型
     * @param file 不带扩展名的文件名
     * @return 带扩展名的文件名
     */
    public String getFileName(final String fileType, String file) {
        switch (fileType) {
            case "EXCEL":
                file += ".xlsx";
                break;
            case "DBF":
                file += ".dbf";
                break;
        }
        return file;
    }

    /**
     * 数据转换
     *
     * @param data
     * @return
     */
    String dataFormat(double data) {
        String str;
        if (data == 0) {
            str = "0";
        } else {
            str = Double.toString(data);
        }
        return str;
    }
}
