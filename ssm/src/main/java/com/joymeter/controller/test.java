package com.joymeter.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import test.TipTest;

@Controller
@RequestMapping("/test")
public class test {
    
    @RequestMapping("/get")
    public void getAdminByName(HttpServletRequest req,
            @RequestParam("name")String name,
            @RequestParam("release")String release,
            @RequestParam("feature")String feature) {
//        String name = "用户充值:";
//        String release = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        String feature = "1.含动画渐入与渐出效果\\n2.3秒后启动动画渐出效果";
        new TipTest(name, release, feature);
        
    }
}
