package com.joymeter.export;

import com.joymeter.entity.RoomInfo;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 将查询到的房源信息导入到模板Excel中
 *
 * @author wuwei
 */
public class ExportRoomInfo extends ExportReadInfo {

    /**
     * 导出到Excel
     *
     * @param request
     * @param list
     * @param language
     * @param file
     * @param fileType 导出的文件类型 'EXCEL', 'DBF'
     * @return 返回文件名
     * @throws IOException
     */
    public String export(HttpServletRequest request, List<?> list, String language, final String file, final String fileType) throws IOException {
        //获取标准模板
        String ctxPath = request.getServletContext().getRealPath("/file");
        String modelName = "Chinese".equals(language) ? "导入用户标准格式.xlsx" : "ImportModel.xlsx";
        String modelFilePath = ctxPath + "/" + modelName;
        String fileName = getFileName(fileType, file);
        try (FileInputStream fis = new FileInputStream(modelFilePath);
                FileOutputStream fos = new FileOutputStream(fileName);
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fis)) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    XSSFRow row = xssfSheet.createRow(i + 2);
                    Object obj = list.get(i);
                    fillInExcel(obj, row);
                }
            }
            xssfWorkbook.write(fos);
        }
        return fileName;
    }

    @Override
    void fillInExcel(Object obj, XSSFRow row) {
        RoomInfo roomInfo = (RoomInfo) obj;
        row.createCell(1).setCellValue(roomInfo.getProvinceName());
        row.createCell(2).setCellValue(roomInfo.getCityName());
        row.createCell(3).setCellValue(roomInfo.getCityAreaName());
        row.createCell(4).setCellValue(roomInfo.getVillageName());
        row.createCell(5).setCellValue(roomInfo.getBuildingNumber());
        row.createCell(6).setCellValue(roomInfo.getUnitNumber());
        row.createCell(7).setCellValue(roomInfo.getHouseNumber());
        row.createCell(11).setCellValue(roomInfo.getOnlineSyncCode());
        row.createCell(16).setCellValue(roomInfo.getMeterNo());
    }
}
