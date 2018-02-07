package com.joymeter.controller;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.joymeter.entity.Result;
import com.joymeter.service.UserService;

@RequestMapping("/user")
@Controller
public class UserController {
    
    @Resource
    private UserService userService;
    
    @RequestMapping("/login")
    public String login(@RequestParam("username") String username,@RequestParam("password") String password,Result result) {
        return "wechat_web/jsp/main";
    }
}
