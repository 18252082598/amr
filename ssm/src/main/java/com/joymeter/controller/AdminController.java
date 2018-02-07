package com.joymeter.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joymeter.entity.Wechat;
import com.joymeter.service.AdminService;
import com.joymeter.service.WechatService;

@RequestMapping("/admin")
@Controller
public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private WechatService wechatService;
    @RequestMapping("/get")
    public String getAdminByName(@RequestParam("username") String username, Map<String,Object> map) {
        List<Wechat> list = wechatService.getAllWechat();
        map.put("wechats", list);
        return "wechat_web/jsp/main";
        
    }
}
