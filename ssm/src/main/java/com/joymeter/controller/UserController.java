package com.joymeter.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joymeter.entity.Result;
import com.joymeter.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Resource
    private UserService userService;
    
    @ResponseBody
    @RequestMapping("/saveLanguage.do")
    public Result saveLanguage(HttpServletRequest req,
            @RequestParam("language")String language) {
        return userService.saveLanguage(req,language);
    }
}
