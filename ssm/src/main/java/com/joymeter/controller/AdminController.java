package com.joymeter.controller;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.joymeter.entity.Admin;
import com.joymeter.entity.Result;
import com.joymeter.entity.Role;
import com.joymeter.service.AdminService;
import javax.servlet.http.HttpServletRequest;

/**
 * 管理员操作
 *
 * @author Joymeter
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 管理员注册
     *
     * @param admin
     * @return
     */
    @ResponseBody
    @RequestMapping("/adminRegist.do")
    public Result adminRegiest(Admin admin) {
        return adminService.adminRegist(admin);
    }

    /**
     * 查询管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/findAdmin.do")
    public Result loadAdmins() {
        return adminService.findAdmin();
    }

    /**
     * 删除管理员
     *
     * @param admin_account
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteAdmin.do")
    public Result delAdmin(@RequestParam("admin_account") String admin_account) {
        return adminService.delAdmin(admin_account);
    }

    /**
     * 检查账号是否存在
     *
     * @param admin_account
     * @return
     */
    @ResponseBody
    @RequestMapping("/isExist.do")
    public Result isExist(@RequestParam("admin_account") String admin_account) {
        return adminService.isExist(admin_account);
    }

    /**
     * 根据账号查找管理员
     *
     * @param admin_account
     * @return
     */
    @ResponseBody
    @RequestMapping("/findAdminByAccount.do")
    public Result findAdminByAccount(
            @RequestParam("admin_account") String admin_account) {
        Admin admin = adminService.findAdminByAccount(admin_account);
        Result result = new Result();
        result.setData(admin);
        return result;
    }

    /**
     * 管理员信息修改
     *
     * @param admin
     * @param original_admin_name
     * @param original_admin_tel
     * @param original_admin_supplier_name
     * @param original_admin_password
     * @param original_admin_power
     * @param original_account_status
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyAdmin.do")
    public Result modifyAdmin(
            Admin admin,
            @RequestParam("original_admin_name") String original_admin_name,
            @RequestParam("original_admin_tel") String original_admin_tel,
            @RequestParam("original_admin_supplier_name") String original_admin_supplier_name,
            @RequestParam("original_admin_password") String original_admin_password,
            @RequestParam("original_admin_power") String original_admin_power,
            @RequestParam("original_account_status") String original_account_status) {
        return adminService.modifyAdmin(admin, original_admin_name,
                original_admin_tel, original_admin_supplier_name,
                original_admin_password, original_admin_power,
                original_account_status);
    }

    /**
     * 查找系统日志
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/findSysLog.do")
    public Result loadSysLogs() {
        return adminService.findSysLog();
    }

    /**
     * 根据ID修改管理员电话
     *
     * @param admin_id
     * @param admin_tel
     * @param original_admin_tel
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyTelById.do")
    public Result modifyTelById(@RequestParam("admin_id") Long admin_id,
            @RequestParam("admin_tel") String admin_tel,
            @RequestParam("original_admin_tel") String original_admin_tel) {
        return adminService.modifyTelById(admin_id, admin_tel, original_admin_tel);
    }

    /**
     * 根据ID修改管理员姓名
     *
     * @param admin_id
     * @param admin_name
     * @param original_admin_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyNameById.do")
    public Result modifyNameById(@RequestParam("admin_id") Long admin_id,
            @RequestParam("admin_name") String admin_name,
            @RequestParam("original_admin_name") String original_admin_name) {
        return adminService.modifyNameById(admin_id, admin_name, original_admin_name);
    }

    /**
     * 根据ID修改密码
     *
     * @param admin_id
     * @param admin_password
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyPasswordById.do")
    public Result modifyPasswordById(@RequestParam("admin_id") Long admin_id,
            @RequestParam("admin_password") String admin_password) {
        return adminService.modifyPasswordById(admin_id, admin_password);
    }

    /**
     * 查询管理员操作信息
     *
     * @param admin_account
     * @return
     */
    @ResponseBody
    @RequestMapping("/findAdminManageInfo.do")
    public Result findAdminManageInfo(@RequestParam("admin_account") String admin_account) {
        return adminService.findAdminManageInfo(admin_account);
    }

    /**
     * 查询管理员角色
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/findRole.do")
    public Result findRole() {
        return adminService.findRole();
    }

    /**
     * 添加管理员角色
     *
     * @param role
     * @return
     */
    @ResponseBody
    @RequestMapping("/addRole.do")
    public Result registRole(Role role) {
        return adminService.addRole(role);
    }

    /**
     * 修改管理员角色
     *
     * @param role
     * @param original_role_name
     * @param original_permission
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyRole.do")
    public Result modifyRole(Role role,
            @RequestParam("original_role_name") String original_role_name,
            @RequestParam("original_permission") String original_permission) {
        return adminService.modifyRole(role, original_role_name, original_permission);
    }

    /**
     * 删除管理员角色
     *
     * @param id
     * @param role_name
     * @return
     */
    @ResponseBody
    @RequestMapping("/delRole.do")
    public Result delRole(@RequestParam("id") Long id,
            @RequestParam("role_name") String role_name) {
        return adminService.delRole(id, role_name);
    }

    /**
     * 检查角色是否存在
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkRoleExist.do")
    public Result checkRoleExist(@RequestParam("id") Long id) {
        return adminService.checkRoleExist(id);
    }

    /**
     * 检查管理员密码
     *
     * @param admin_id
     * @param original_password
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkPassword.do")
    public Result checkPassword(@RequestParam("admin_id") Long admin_id,
            @RequestParam("original_password") String original_password) {
        return adminService.checkPassword(admin_id, original_password);
    }
    
    @ResponseBody
    @RequestMapping("/authorization.do")
    public Result authorization(
            @RequestParam("admin_account") String admin_account,
            @RequestParam("admin_password") String admin_password,
            HttpServletRequest request){
        return  adminService.modifyRoleRight(admin_account, admin_password, request);
    }
}
