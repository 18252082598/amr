package com.joymeter.service;

import com.joymeter.entity.Result;
import javax.servlet.http.HttpServletRequest;

public interface FileService {

    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    Result upload(HttpServletRequest request) throws Exception;
}
