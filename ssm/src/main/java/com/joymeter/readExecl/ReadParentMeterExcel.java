package com.joymeter.readExecl;

import com.joymeter.entity.ParentMeterConf;
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
import com.joymeter.util.FileUtil;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadParentMeterExcel {

    /**
     *
     * @param request
     * @param storeName
     * @return
     * @throws IOException
     */
    public List<ParentMeterConf> readXls(HttpServletRequest request, String storeName) throws IOException {
        String ctxPath = request.getSession().getServletContext().getRealPath("/") + FileUtil.UPLOADDIR;
        String downLoadPath = ctxPath + storeName;
        InputStream is = new FileInputStream(downLoadPath);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        ParentMeterConf motherMeterConf = null;
        List<ParentMeterConf> list = new ArrayList<>();
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            int rownumber = xssfSheet.getLastRowNum();
            for (int rowNum = 2; rowNum <= rownumber; rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null && xssfRow.getCell(0) != null) {
                    ParentMeterConf thisRowUser = readExcel(xssfRow, motherMeterConf); 
                    String admin_account = (String) request.getSession().getAttribute("admin_account");
                    thisRowUser.setOperator_account(admin_account);
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
    private ParentMeterConf readExcel(XSSFRow xssfRow, ParentMeterConf motherMeterConf) {
        motherMeterConf = new ParentMeterConf();
        XSSFCell meter_no = xssfRow.getCell(0);
        XSSFCell meter_type = xssfRow.getCell(1);
        XSSFCell allot_type = xssfRow.getCell(2);
        motherMeterConf.setMeter_no(getValue(meter_no));
        motherMeterConf.setMeter_type(getValue(meter_type));
        motherMeterConf.setAllot_type(getValue(allot_type));    
        motherMeterConf.setAdd_time(new Timestamp(System.currentTimeMillis()).toString());
      
        return motherMeterConf;
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
                    return String.valueOf(xssfCell.getStringCellValue());
                }
                if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
                    if (xssfCell.getCellFormula() == null) {
                        return String.valueOf("");
                    }
                    return String.valueOf(xssfCell.getCellFormula());
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ReadParentMeterExcel.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
