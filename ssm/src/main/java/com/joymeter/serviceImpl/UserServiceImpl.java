package com.joymeter.serviceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.joymeter.dao.UserMapper;
import com.joymeter.entity.Result;
import com.joymeter.entity.User;
import com.joymeter.service.UserService;
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    
    @Override
    public Result selectUserByUserName(String username,String password) {
        Result result =  new Result();
        User user = userMapper.selectUserByUserName(username);
        if(user==null) {
            result.setStatus(0);
            result.setData("用户不存在!");
            return result;
        }else if(user.getPwd()!=password) {
            result.setStatus(2);
            result.setData("密码输入不正确,请重新输入!");
        }else {
            result.setStatus(1);
            result.setData("success");
            return result;
        }
        return null;
    }

}
