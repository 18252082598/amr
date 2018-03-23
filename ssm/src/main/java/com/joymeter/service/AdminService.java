package com.joymeter.service;

import com.joymeter.entity.Admin;
import com.joymeter.entity.Result;
import com.joymeter.entity.Role;
import javax.servlet.http.HttpServletRequest;

public interface AdminService {

    Result adminRegist(Admin admin);

    Result findAdmin();

    Result delAdmin(String admin_account);

    Result isExist(String admin_account);

    Result modifyAdmin(Admin admin, String original_admin_name, String original_admin_tel, String original_admin_supplier_name, String original_admin_password, String original_admin_power, String original_account_status);

    Admin findAdminByAccount(String admin_account);

    Result logout();

    Result checkLogin(String admin_account, String admin_password, HttpServletRequest request);

    Result findSysLog();

    Result findAdminManageInfo(String admin_account);

    Result addRole(Role role);

    Result findRole();

    Result modifyRole(Role role, String original_role_name, String original_permission);

    Result delRole(Long id, String role_name);

    Result modifyTelById(Long admin_id, String admin_tel, String original_admin_tel);

    Result modifyNameById(Long admin_id, String admin_name, String original_admin_name);

    Result checkPassword(Long admin_id, String original_password);

    Result modifyPasswordById(Long admin_id, String admin_password);

    Result checkRoleExist(Long id);
    
    Result modifyRoleRight(String logon_id, String logon_passWord,HttpServletRequest request);
}
