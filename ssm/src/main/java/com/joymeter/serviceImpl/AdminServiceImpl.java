package com.joymeter.serviceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.joymeter.dao.AdminMapper;
import com.joymeter.entity.Admin;
import com.joymeter.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private AdminMapper adminMapper;
    
    @Override
    public Admin getAdminByName(String admin_name) {
        return adminMapper.getAdminByName(admin_name);
    }

}
