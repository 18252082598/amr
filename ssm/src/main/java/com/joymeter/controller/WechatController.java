package com.joymeter.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.joymeter.entity.Cjoy;
import com.joymeter.entity.Wechat;
import com.joymeter.service.CjoyService;
import com.joymeter.service.WechatService;

@RequestMapping("/wechat")
@Controller
public class WechatController {
    
    @Resource
    private WechatService wechatService;
    @Resource
    private CjoyService cjoyService;
    
    @RequestMapping("/queryAll")
    public String getAllWechat(HttpServletRequest req,HttpServletResponse res,Map<String,Object> map) {
        List<Wechat> wechatList = wechatService.getAllWechat();
        List<Cjoy> cjoyList  = cjoyService.getAllCjoy();
        map.put("wechats", wechatList);
        map.put("cjoys", cjoyList);
        return "wechat_web/jsp/hy_list";      
    }
}
