package com.joymeter.service;

import javax.servlet.http.HttpServletRequest;

import com.joymeter.entity.Result;

public interface UserService {

    public Result saveLanguage(HttpServletRequest req, String language);
    

}
