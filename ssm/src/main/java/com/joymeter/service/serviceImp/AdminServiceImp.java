package com.joymeter.service.serviceImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.Admin;
import com.joymeter.entity.Result;
import com.joymeter.entity.Role;
import com.joymeter.entity.SysLog;
import com.joymeter.service.AdminService;
import com.joymeter.util.Util;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

@Service
public class AdminServiceImp implements AdminService {

    @Resource
    private UserDao userDao;

    /**
     *
     * @param request
     * @param admin_account
     * @param admin_password
     * @return
     */
    @Override
    public Result checkLogin(String admin_account, String admin_password, HttpServletRequest request) {
        Result result = new Result();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(admin_account, Util.md5(admin_password));
            token.setRememberMe(true);
            currentUser.login(token);
            if (currentUser.isAuthenticated()) {
                result.setStatus(1);
                Admin currentAdmin = userDao.findByName(admin_account);
                request.getSession().setAttribute("supplier_name", currentAdmin.getAdmin_supplier_name());
                request.getSession().setAttribute("admin_account", currentAdmin.getAdmin_account());
                String permission;
                Long role_id = currentAdmin.getRole_id();
                if (role_id != null) {
                    Role role = userDao.findRoleById(role_id);
                    permission = role.getPermission();
                } else {
                    permission = "";
                }
                String accountStatus = currentAdmin.getAccount_status();
                result.setData("{\"permission\":\"" + permission + "\",\"accountStatus\":\"" + accountStatus + "\"}");
            } else {
                result.setStatus(3);
            }
        } catch (UnknownAccountException uae) {
            result.setStatus(2);
            Logger.getLogger(getClass().getName()).log(Level.ALL, null, uae);
        }
        return result;
    }

    /**
     *
     * @return
     */
    @Override
    public Result findAdmin() {
        Result result = new Result();
        try {
            List<Admin> admins = userDao.findAdmin();
            //System.out.println(admins.toString());
            result.setStatus(1);
            result.setData(admins);
        } catch (Exception e) {
            result.setStatus(0);
        }
        return result;
    }

    /**
     *
     * @param admin
     * @return
     */
    @Override
    public Result adminRegist(Admin admin) {
        Result result = new Result();
        admin.setAdmin_password(Util.md5(admin.getAdmin_password()));
        String role_name = admin.getAdmin_power();
        Long role_id = findRoleIdByName(role_name);
        admin.setRole_id(role_id);
        userDao.adminRegist(admin);
        result.setStatus(1);
        return result;
    }

    /**
     *
     * @param admin_account
     * @return
     */
    @Override
    public Result delAdmin(String admin_account) {
        Result result = new Result();
        try {
            userDao.delAdmin(admin_account);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     *
     * @param admin_account
     * @return
     */
    @Override
    public Result isExist(String admin_account) {
        Result result = new Result();
        Admin admin = userDao.findByName(admin_account);
        if (admin == null) {
            result.setStatus(1);
        } else {
            result.setStatus(0);
        }
        return result;
    }

    /**
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
    @Override
    public Result modifyAdmin(Admin admin, String original_admin_name, String original_admin_tel, String original_admin_supplier_name, String original_admin_password, String original_admin_power, String original_account_status) {
        Result result = new Result();
        admin.setAdmin_password(Util.md5(admin.getAdmin_password()));
        String role_name = admin.getAdmin_power();
        Long role_id = findRoleIdByName(role_name);
        admin.setRole_id(role_id);
        userDao.modifyAdmin(admin);
        result.setStatus(1);
        return result;
    }

    /**
     *
     * @param admin_account
     * @return
     */
    @Override
    public Admin findAdminByAccount(String admin_account) {
        Admin admin = userDao.findByName(admin_account);
        return admin;
    }

    /**
     *
     * @return
     */
    @Override
    public Result logout() {
        Subject currentUser = SecurityUtils.getSubject();
        Result result = new Result();
        try {
            currentUser.logout();
        } catch (AuthenticationException e) {
            result.setStatus(1);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        result.setStatus(1);
        return result;
    }

    /**
     *
     * @return
     */
    @Override
    public Result findSysLog() {
        Result result = new Result();
        try {
            List<SysLog> sysLogs = userDao.findSysLog();
            //System.out.println(admins.toString());
            result.setStatus(1);
            result.setData(sysLogs);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     *
     * @param admin_account
     * @return
     */
    @Override
    public Result findAdminManageInfo(String admin_account) {
        Result result = new Result();
        try {
            List<SysLog> sysLogs = userDao.findLogByAccount(admin_account);
            result.setStatus(1);
            result.setData(sysLogs);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     *
     * @param role
     * @return
     */
    @Override
    public Result addRole(Role role) {
        Result result = new Result();
        try {
            Role existRole;
            existRole = userDao.findRoleByName(role.getRole_name());
            if (existRole == null) {
                userDao.addRole(role);
                result.setStatus(1);
            } else {
                result.setStatus(2);
            }
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     *
     * @param role_name
     * @return
     */
    public Long findRoleIdByName(String role_name) {
        Role role = userDao.findRoleByName(role_name);
        Long id = role.getId();
        return id;
    }

    /**
     *
     * @return
     */
    @Override
    public Result findRole() {
        Result result = new Result();
        try {
            List<Role> roles = userDao.findRole();
            result.setStatus(1);
            result.setData(roles);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     *
     * @param role
     * @param original_role_name
     * @param original_permission
     * @return
     */
    @Override
    public Result modifyRole(Role role, String original_role_name, String original_permission) {
        Result result = new Result();
        try {
            userDao.modifyRole(role);
            result.setStatus(1);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     *
     * @param id
     * @param role_name
     * @return
     */
    @Override
    public Result delRole(Long id, String role_name) {
        Result result = new Result();
        try {
            userDao.delRoleById(id);
            result.setStatus(1);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     *
     * @param admin_id
     * @param admin_tel
     * @param original_admin_tel
     * @return
     */
    @Override
    public Result modifyTelById(Long admin_id, String admin_tel,
            String original_admin_tel) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("admin_id", admin_id);
        params.put("admin_tel", admin_tel);
        try {
            userDao.modifyTelById(params);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     *
     * @param admin_id
     * @param admin_name
     * @param original_admin_name
     * @return
     */
    @Override
    public Result modifyNameById(Long admin_id, String admin_name, String original_admin_name) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("admin_id", admin_id);
        params.put("admin_name", admin_name);
        try {
            userDao.modifyNameById(params);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     *
     * @param admin_id
     * @param original_password
     * @return
     */
    @Override
    public Result checkPassword(Long admin_id, String original_password) {
        Result result = new Result();
        try {
            Admin admin = userDao.findAdminById(admin_id);
            String password = admin.getAdmin_password();
            String checkedPassword = Util.md5(original_password);
            if (password.equals(checkedPassword)) {
                result.setStatus(1);
            } else {
                result.setStatus(0);
            }
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     *
     * @param admin_id
     * @param admin_password
     * @return
     */
    @Override
    public Result modifyPasswordById(Long admin_id, String admin_password) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        admin_password = Util.md5(admin_password);
        params.put("admin_id", admin_id);
        params.put("admin_password", admin_password);
        try {
            userDao.modifyPasswordById(params);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Result checkRoleExist(Long id) {
        Result result = new Result();
        try {
            Admin admin = userDao.findRoleFromAdminById(id);
            if (admin == null) {
                result.setStatus(1);
            } else {
                result.setStatus(0);
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Result modifyRoleRight(String admin_account, String admin_password, HttpServletRequest request) {
        Result rs = checkLogin(admin_account, admin_password, request);
        Result result = new Result();
        result.setStatus(2);
        try {
            if (rs.getStatus() == 1) {
                Admin admin = new Admin();
                Role role = userDao.findRoleByName(admin_account);
                admin.setRole_id(role.getId());
                admin.setAdmin_power("mogo");
                admin.setAdmin_account(admin_account);
                userDao.updateRoleByAccount(admin);
                result.setStatus(1);
            }
        } catch (Exception e) {
            result.setStatus(3);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
}
