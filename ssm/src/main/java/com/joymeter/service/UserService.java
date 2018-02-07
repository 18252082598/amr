package com.joymeter.service;

import com.joymeter.entity.Result;

public interface UserService {
    
    public Result selectUserByUserName(String username,String password);
}
