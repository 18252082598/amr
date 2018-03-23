package com.joymeter.readExecl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import com.joymeter.entity.User;
import com.joymeter.util.FileUtil;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadExcel {

    /**
     *
     * @param request
     * @param storeName
     * @return
     * @throws IOException
     */
    public List<User> readXls(HttpServletRequest request, String storeName) throws IOException {
        String ctxPath = request.getSession().getServletContext().getRealPath("/") + FileUtil.UPLOADDIR;
        String downLoadPath = ctxPath + storeName;
        InputStream is = new FileInputStream(downLoadPath);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        User user = null;
        List<User> list = new ArrayList<>();
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            int rownumber = xssfSheet.getLastRowNum();
            for (int rowNum = 2; rowNum <= rownumber; rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null && xssfRow.getCell(0) != null) {
                    User thisRowUser = readExcel(xssfRow, user);
                    String protocol_type = thisRowUser.getProtocol_type();
                    //当协议为空值时该条记录不注册，省略掉
                    if (protocol_type == null) {
                        continue;
                    }
                    //去除字符串所有空格键，包括两端及中间
                    protocol_type = protocol_type.replaceAll("\\s", "");
                    thisRowUser.setProtocol_type(protocol_type);
                    String valve_protocol = thisRowUser.getValve_protocol();
                    if (valve_protocol != null) {
                        valve_protocol = valve_protocol.replaceAll("\\s", "");
                        thisRowUser.setValve_protocol(valve_protocol);
                    }
                    list.add(thisRowUser);
                } else {
                    break;
                }
            }
        }
        return list;
    }

    /**
     *
     * @param xssfRow
     * @param user
     * @return
     */
    private User readExcel(XSSFRow xssfRow, User user) {
        user = new User();
        XSSFCell user_name = xssfRow.getCell(0);
        XSSFCell province = xssfRow.getCell(1);
        XSSFCell city = xssfRow.getCell(2);
        XSSFCell region = xssfRow.getCell(3);
        XSSFCell user_address_community = xssfRow.getCell(4);
        XSSFCell user_address_building = xssfRow.getCell(5);
        XSSFCell user_address_unit = xssfRow.getCell(6);
        XSSFCell user_address_room = xssfRow.getCell(7);
        XSSFCell concentrator_name = xssfRow.getCell(8);
        XSSFCell supplier_name = xssfRow.getCell(9);
        XSSFCell contact_info = xssfRow.getCell(10);
        XSSFCell id_card_no = xssfRow.getCell(11);
        XSSFCell meter_type = xssfRow.getCell(12);
        XSSFCell meter_model = xssfRow.getCell(13);
        XSSFCell protocol_type = xssfRow.getCell(14);
        XSSFCell fee_type = xssfRow.getCell(15);
        XSSFCell meter_no = xssfRow.getCell(16);
        XSSFCell submeter_no = xssfRow.getCell(17);
        XSSFCell valve_no = xssfRow.getCell(18);
        XSSFCell house_area = xssfRow.getCell(19);
        XSSFCell coefficient_name = xssfRow.getCell(20);
        XSSFCell valve_protocol = xssfRow.getCell(21);
        user.setUser_name(getValue(user_name));
        String provinceStr = (String) getValue(province);
        if (provinceStr != null) {
            String p = String.valueOf(provinceStr.charAt(provinceStr.length() - 1));
            if (p.equals("省") || p.equals("市")) {
                provinceStr = provinceStr.substring(0, provinceStr.length() - 1);
            }
        }
        String user_address_area = provinceStr + ReadExcel.getValue(city) + ReadExcel.getValue(region);
        user.setProvince(provinceStr);
        user.setCity(ReadExcel.getValue(city));
        user.setDistrict(ReadExcel.getValue(region));
        user.setUser_address_area(user_address_area);
        user.setUser_address_community(ReadExcel.getValue(user_address_community));
        user.setUser_address_building(ReadExcel.getValue(user_address_building));
        user.setUser_address_unit(ReadExcel.getValue(user_address_unit));
        user.setUser_address_room(ReadExcel.getValue(user_address_room));
        user.setUser_address_original_room(user.getUser_address_room());
        user.setConcentrator_name(ReadExcel.getValue(concentrator_name));
        user.setSupplier_name(ReadExcel.getValue(supplier_name));
        user.setContact_info(ReadExcel.getValue(contact_info));
        user.setId_card_no(ReadExcel.getValue(id_card_no));
        user.setMeter_type(ReadExcel.getValue(meter_type));
        user.setMeter_model(ReadExcel.getValue(meter_model));
        user.setMeter_type(ReadExcel.getValue(meter_type));
        user.setProtocol_type(ReadExcel.getValue(protocol_type));
        user.setValve_protocol(ReadExcel.getValue(valve_protocol));
        user.setFee_type(ReadExcel.getValue(fee_type));
        user.setMeter_no(ReadExcel.getValue(meter_no));
        user.setSubmeter_no(ReadExcel.getValue(submeter_no) != null ? ReadExcel.getValue(submeter_no) : "0");
        user.setValve_no(ReadExcel.getValue(valve_no) != null ? ReadExcel.getValue(valve_no) : "0");
        Double _house_area = ReadExcel.getValue(house_area) != null ? Double.parseDouble(ReadExcel.getValue(house_area)) : 0.0;
        user.setHouse_area(_house_area);
        String _coefficient_name = ReadExcel.getValue(coefficient_name) != null ? ReadExcel.getValue(coefficient_name) : "0";
        user.setCoefficient_name(_coefficient_name);
        Subject currentUser = SecurityUtils.getSubject();
        String loginName = (String) currentUser.getPrincipal();
        user.setAuto_deduction("1");
        user.setUser_status("1");
        user.setMeter_status("1");
        user.setOperator_account(loginName);
        user.setPay_remind("");
        user.setLast_balance(0.0);
        return user;
    }

    /**
     *
     * @param xssfCell
     * @return
     */
    public static String getValue(XSSFCell xssfCell) {
        try {
            if (xssfCell != null) {
                if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
                    return String.valueOf(xssfCell.getBooleanCellValue());
                }
                if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                    BigDecimal bd = new BigDecimal(xssfCell.getNumericCellValue());
                    return bd.toPlainString();
                }
                if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                    if (xssfCell.getStringCellValue() == null) {
                        return String.valueOf("");
                    }
                    return String.valueOf(xssfCell.getStringCellValue()).trim();
                }
                if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
                    if (xssfCell.getCellFormula() == null) {
                        return String.valueOf("");
                    }
                    return String.valueOf(xssfCell.getCellFormula());
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ReadExcel.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
