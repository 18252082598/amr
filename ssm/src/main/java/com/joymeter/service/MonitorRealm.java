package com.joymeter.service;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import com.joymeter.entity.Admin;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonitorRealm extends AuthorizingRealm {

    /**
     * @Autowired UserService userService;
     *
     * @Autowired RoleService roleService;
     *
     * @Autowired LoginLogService loginLogService;
     */
    @Resource
    private AdminService adminService;

    public MonitorRealm() {
        super();
    }

    /**
     * 
     * @param principals
     * @return 
     */
    @Override
    public AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        Set<String> roleNames = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        roleNames.add("admin");
        permissions.add("user.do?myjsp");
        permissions.add("login.do?main");
        permissions.add("login.do?logout");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissions);
        return info;
    }

    /**
     * 
     * @param authcToken
     * @return
     * @throws AuthenticationException 
     */
    @SuppressWarnings("unused")
    @Override
    public AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        AuthenticationInfo authenticationInfo = null;
        try {
            Admin admin = adminService.findAdminByAccount(token.getUsername());
            if (admin != null) {
                String password = new String(token.getPassword());
                if (password.equals(admin.getAdmin_password())) {
                    authenticationInfo = new SimpleAuthenticationInfo(admin.getAdmin_account(), admin.getAdmin_password(), getName());
                }
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return authenticationInfo;
    }

    /**
     *
     * @param principal
     */
    public void clearCachedAuthorizationInfo(String principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(
                principal, getName());
        clearCachedAuthorizationInfo(principals);
    }
}
