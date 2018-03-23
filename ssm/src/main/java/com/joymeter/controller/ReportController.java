package com.joymeter.controller;

import com.joymeter.entity.Result;
import com.joymeter.service.ExportService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Resource
    private ExportService exportService;

    @ResponseBody
    @RequestMapping("/checkStatus.do")
    public Result exportReadInfo() {
        return exportService.checkStatus();
    }

    @ResponseBody
    @RequestMapping("/exportReadInfo.do")
    public void exportReadInfo(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("user_name") String user_name,
            @RequestParam("community") String community,
            @RequestParam("building") String building,
            @RequestParam("unit") String unit,
            @RequestParam("room") String room,
            @RequestParam("operator_name") String operator_name,
            @RequestParam("meter_no") String meter_no,
            @RequestParam("meter_type") String meter_type,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("language") String language,
            @RequestParam("totalNo") int totalNo,
            @RequestParam("fileType") String fileType,
            @RequestParam("method") String method,
            @RequestParam("title") String title,
            @RequestParam("fee_type") String fee_type
    ) throws IOException {
        exportService.exportReadInfo(request, response, user_name, community,
                building, unit, room, operator_name, meter_no, meter_type, startTime, endTime,
                language, totalNo, fileType, method, title, fee_type);
    }

    @ResponseBody
    @RequestMapping("/exportRemind.do")
    public void exportRemind(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("user_name") String user_name,
            @RequestParam("community") String community,
            @RequestParam("building") String building,
            @RequestParam("unit") String unit,
            @RequestParam("operator_name") String operator_name,
            @RequestParam("meter_no") String meter_no,
            @RequestParam("language") String language,
            @RequestParam("totalNo") int totalNo,
            @RequestParam("fileType") String fileType) throws IOException {
        exportService.exportRemind(request, response, user_name, community, building, unit, operator_name, meter_no, language, totalNo, fileType);
    }

    @ResponseBody
    @RequestMapping("/exportRechargeInfo.do")
    public void exportRechargeInfo(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("user_name") String user_name,
            @RequestParam("community") String community,
            @RequestParam("building") String building,
            @RequestParam("unit") String unit,
            @RequestParam("room") String room,
            @RequestParam("operator_name") String operator_name,
            @RequestParam("meter_no") String meter_no,
            @RequestParam("language") String language,
            @RequestParam("totalNo") int totalNo,
            @RequestParam("fileType") String fileType) throws IOException {
        exportService.exportRechargeInfo(request, response, user_name, community, building, unit, room, operator_name, meter_no, language, totalNo, fileType);
    }

    @ResponseBody
    @RequestMapping("/exportRoomInfo.do")
    public void exportRoomInfo(HttpServletRequest request, HttpServletResponse response) {
        exportService.exportRoomInfo(request, response);
    }
}
