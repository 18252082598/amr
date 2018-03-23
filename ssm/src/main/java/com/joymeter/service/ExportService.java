/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.service;

import com.joymeter.entity.Result;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
public interface ExportService {

    public void exportRechargeInfo(HttpServletRequest request, HttpServletResponse response, String user_name, String community,
            String building, String unit, String room, String operator_name, String meter_no, String language, int totalNo, String fileType);

    public void exportReadInfo(HttpServletRequest request, HttpServletResponse response, String user_name, String community,
            String building, String unit, String room, String operator_name, String meter_no, String meter_type, String startTime, String endTime,
            String language, int totalNo, String fileType, String method, String title, String fee_type);

    public void exportRemind(HttpServletRequest request, HttpServletResponse response, String user_name, String community,
            String building, String unit, String operator_name, String meter_no, String language, int totalNo, String fileType);

    public void exportRoomInfo(HttpServletRequest request, HttpServletResponse response);

    public Result checkStatus();
}
