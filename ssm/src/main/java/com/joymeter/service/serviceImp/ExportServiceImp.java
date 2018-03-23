/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.service.serviceImp;

import com.joymeter.cache.ReadInfoCache;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.Result;
import com.joymeter.export.ExportReadInfo;
import static com.joymeter.export.ExportReadInfo.DOWNLOADED;
import com.joymeter.export.ExportReadInfoCurrent;
import com.joymeter.export.ExportRechargeInfo;
import com.joymeter.export.ExportRemind;
import com.joymeter.export.ExportRoomInfo;
import com.joymeter.export.ZipUtil;
import static com.joymeter.export.ZipUtil.getFileName;
import org.springframework.stereotype.Service;
import com.joymeter.service.ExportService;
import com.joymeter.util.Util;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
@Service
public class ExportServiceImp implements ExportService {

    @Resource
    private UserDao userDao;
    @Resource
    private ReadInfoCache readInfoCache;

    /**
     *
     * @return
     */
    @Override
    public Result checkStatus() {
        Result result = new Result();
        int DOWNLOADED = ExportReadInfo.DOWNLOADED;
        ExportReadInfo.DOWNLOADED = 0;
        result.setStatus(DOWNLOADED);
        return result;
    }

    /**
     * 设置响应头
     *
     * @param response
     * @param title
     */
    public void setResponseHeader(HttpServletResponse response, String title) {
        try {
            response.reset();// 清空输出流  
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String((title + Util.getDf().format(new java.util.Date())).getBytes("GB2312"), "8859_1") + ".zip");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 根据文件格式,导出相应的文件
     *
     * @param params
     * @param totalNo
     * @param request
     * @param response
     * @param language
     * @param title
     * @param method
     * @param fileType 文件类型(EXCEL, DBF)
     */
    public void excelGenerate(HashMap<String, Object> params, int totalNo, HttpServletRequest request, HttpServletResponse response, String language, String title, String method, String fileType) {
        int pageSize = ExportReadInfo.PAGE_SIZE;
        int other = totalNo % pageSize;
        int page = getPage(totalNo, pageSize);
        List<String> fileNames = new ArrayList();// 用于存放生成的文件名称         
        setResponseHeader(response, title);
        String supplier_name = (String) request.getSession().getAttribute("supplier_name");
        params.put("supplier_name", supplier_name);
        try {
            for (int i = 0; i < page; i++) {
                params.put("startNo", i * pageSize);
                if (i == page - 1 && other != 0) {
                    params.put("pageSize", other);
                } else {
                    params.put("pageSize", pageSize);
                }
                String file = request.getServletContext().getRealPath("/file") + "\\" + getFileName(title) + "_" + i;
                List<?> list;
                switch (method) {
                    case "findReadInfo": {
                        list = userDao.findReadInfo(params);
                        ExportReadInfo exporter = new ExportReadInfo();
                        String fileName = exporter.export(list, language, file, fileType);
                        fileNames.add(fileName);
                        break;
                    }
                    case "findReadInfoCurrent": {
                        list = userDao.findReadInfoCurrent(params);
                        ExportReadInfo exporter = new ExportReadInfoCurrent();
                        String fileName = exporter.export(list, language, file, fileType);
                        fileNames.add(fileName);
                        break;
                    }
                    case "findReadInfoFail": {
                        list = userDao.findReadInfoFail(params);
                        ExportReadInfo exporter = new ExportReadInfo();
                        String fileName = exporter.export(list, language, file, fileType);
                        fileNames.add(fileName);
                        break;
                    }
                    case "findReadInfoDoubtful": {
                        list = userDao.findReadInfoDoubtful(params);
                        ExportReadInfo exporter = new ExportReadInfo();
                        String fileName = exporter.export(list, language, file, fileType);
                        fileNames.add(fileName);
                        break;
                    }
                    case "findUsersRemind": {
                        list = userDao.findUsersRemind(params);
                        ExportRemind exporter = new ExportRemind();
                        String fileName = exporter.export(list, language, file, fileType);
                        fileNames.add(fileName);
                        break;
                    }
                    case "findOperateInfo": {
                        list = userDao.findOperateInfo(params);
                        ExportRechargeInfo exporter = new ExportRechargeInfo();
                        String fileName = exporter.export(list, language, file, fileType);
                        fileNames.add(fileName);
                        break;
                    }
                    case "findRoomInfo": {
                        list = userDao.findRoomInfo(params);
                        ExportRoomInfo exporter = new ExportRoomInfo();
                        String fileName = exporter.export(request, list, language, file, fileType);
                        fileNames.add(fileName);
                        break;
                    }
                }
            }
            try (OutputStream out = response.getOutputStream()) {
                ZipUtil.fileToZip(request, fileNames, out, title);
            }
            DOWNLOADED = 1;
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 当前页号
     *
     * @param totalNo
     * @param pageSize
     * @return
     */
    private int getPage(int totalNo, int pageSize) {
        int page;
        if (totalNo % pageSize == 0) {
            page = totalNo / pageSize;
        } else {
            page = totalNo / pageSize + 1;
        }
        return page;
    }

    /**
     * @param room
     * @modify 2019-01-26 mengshuai(增加费率类型时导出也需要增加过滤条件)
     *
     * @param request
     * @param response
     * @param userName
     * @param community
     * @param building
     * @param unit
     * @param operatorName
     * @param meterNo
     * @param meterType
     * @param startTime
     * @param endTime
     * @param language
     * @param totalNo
     * @param fileType
     * @param method
     * @param title
     * @param fee_type
     */
    @Override
    public void exportReadInfo(HttpServletRequest request, HttpServletResponse response, String userName, String community,
            String building, String unit, String room, String operatorName, String meterNo,
            String meterType, String startTime, String endTime, String language,
            int totalNo, String fileType, String method, String title,String fee_type) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user_name", userName);
        params.put("community", community);
        params.put("building", building);
        params.put("unit", unit);
        params.put("room", room);
        params.put("operator_name", operatorName);
        params.put("meter_no", meterNo);
        params.put("fee_type", fee_type);
        params.put("endTime", endTime);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        excelGenerate(params, totalNo, request, response, language, title, method, fileType);
    }

    /**
     *
     * @param request
     * @param response
     * @param user_name
     * @param community
     * @param building
     * @param unit
     * @param operator_name
     * @param meter_no
     * @param language
     * @param totalNo
     * @param fileType
     */
    @Override
    public void exportRemind(HttpServletRequest request, HttpServletResponse response, String user_name, String community,
            String building, String unit, String operator_name, String meter_no, String language, int totalNo, String fileType) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user_name", user_name);
        params.put("community", community);
        params.put("building", building);
        params.put("unit", unit);
        params.put("operator_name", operator_name);
        params.put("meter_no", meter_no);
        String title = "remind";
        String method = "findUsersRemind";
        excelGenerate(params, totalNo, request, response, language, title, method, fileType);
    }

    /**
     *
     * @param request
     * @param response
     * @param user_name
     * @param community
     * @param building
     * @param unit
     * @param room
     * @param operator_name
     * @param meter_no
     * @param language
     * @param totalNo
     * @param fileType
     */
    @Override
    public void exportRechargeInfo(HttpServletRequest request, HttpServletResponse response, String user_name, String community,
            String building, String unit, String room, String operator_name, String meter_no, String language, int totalNo, String fileType) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user_name", user_name);
        params.put("community", community);
        params.put("building", building);
        params.put("unit", unit);
        params.put("room", room);
        params.put("operator_name", operator_name);
        params.put("meter_no", meter_no);
        String title = "rechargeinfo";
        String method = "findOperateInfo";
        excelGenerate(params, totalNo, request, response, language, title, method, fileType);
    }

    /**
     * 导出房源信息
     *
     * @param request
     * @param response
     */
    @Override
    public void exportRoomInfo(HttpServletRequest request, HttpServletResponse response) {
        int totalNo = userDao.findTotalRoomInfoNo();
        HashMap<String, Object> map = new HashMap<>();
        String language = "Chinese";
        String title = "roomInfo";
        String method = "findRoomInfo";
        String fileType = "EXCEL";
        excelGenerate(map, totalNo, request, response, language, title, method, fileType);
    }
}
