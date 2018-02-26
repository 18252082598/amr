package com.joymeter.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import test.TipTest;

@Controller
@RequestMapping("/test")
public class test {
    @RequestMapping("/get")
    public void getAdminByName() {
        String name = "用户充值:";
        String release = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String feature = "1.含动画渐入与渐出效果\\n2.3秒后启动动画渐出效果";
        new TipTest(name, release, feature);
        
    }
}
