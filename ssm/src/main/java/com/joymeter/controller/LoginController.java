package com.joymeter.controller;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.joymeter.entity.Result;
import com.joymeter.service.AdminService;
import javax.servlet.http.HttpServletRequest;

/**
 * 账号登录
 *
 * @author Joymeter
 */
@Controller
@RequestMapping("/operator")
public class LoginController {

    @Resource
    private AdminService adminService;

    /**
     * 登录
     *
     * @param request
     * @param admin_account
     * @param admin_password
     * @return
     */
    @ResponseBody
    @RequestMapping("/login.do")
    public Result login(
            @RequestParam("admin_account") String admin_account,
            @RequestParam("admin_password") String admin_password,
            HttpServletRequest request) {
        return adminService.checkLogin(admin_account, admin_password, request);
    }

    /**
     * 退出
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/logout.do")
    public Result logout() {
        return adminService.logout();
    }
}
