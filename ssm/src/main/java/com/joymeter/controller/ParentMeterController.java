package com.joymeter.controller;

import com.joymeter.entity.ParentMeterConf;
import com.joymeter.entity.Result;
import com.joymeter.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.joymeter.service.ParentConfService;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author yinhf
 */
@Controller
@RequestMapping("/motherMeterConf")
public class ParentMeterController {

    @Resource
    private ParentConfService parentConfService;

    /**
     *
     * @param mmc
     * @param org_meter_no
     * @param org_meter_type
     * @param org_allot_type
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyMotherMeterConfirm.do")
    public Result modifyMotherMeterConfirm(
            ParentMeterConf mmc,
            @RequestParam("org_meter_no") String org_meter_no,
            @RequestParam("org_meter_type") String org_meter_type,
            @RequestParam("org_allot_type") String org_allot_type
    ) {
        return parentConfService.updateMotherMeterConf(mmc, org_meter_no, org_meter_type, org_allot_type);
    }

    @ResponseBody
    @RequestMapping("/removeParentMeterConf.do")
    public Result removeParentMeterConf(
            @RequestParam("meter_Nums") String meter_Nums
    ) {
        return parentConfService.removeParentMeterConf(meter_Nums);
    }

    @ResponseBody
    @RequestMapping("/qryShareInfo.do")
    public Result qryShareInfo(@RequestParam("user_name") String user_name,
            @RequestParam("community") String community,
            @RequestParam("building") String building,
            @RequestParam("unit") String unit,
            @RequestParam("room") String room,
            @RequestParam("meter_no") String meter_no,
            @RequestParam("startNo") int startNo,
            @RequestParam("pageSize") int pageSize,
            HttpServletRequest request) {
        return parentConfService.qryShareBilling(user_name, community,
                building, unit, room, meter_no, startNo, pageSize);
    }

    @ResponseBody
    @RequestMapping("/upFile.do")
    public Result upload(HttpServletRequest request) throws Exception {

        return parentConfService.upload(request);
    }

    @ResponseBody
    @RequestMapping("/downLoadConfExcel.do")
    public Result downLoadConfExcel(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return parentConfService.downLoadConfExcel(request, response);
    }
}
