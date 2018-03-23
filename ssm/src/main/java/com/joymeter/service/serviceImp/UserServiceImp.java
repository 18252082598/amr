package com.joymeter.service.serviceImp;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.joymeter.entity.Result;
import com.joymeter.service.UserService;
import com.joymeter.velocity.VelocityToHtml;
@Service
public class UserServiceImp implements UserService {

    
    /**
     * 语言切换
     */
    @Override
    public Result saveLanguage(HttpServletRequest req, String language) {
        Result result = new Result();
        try {
            VelocityToHtml.generateAllHtml(req, language);
            VelocityToHtml.generateAllJs(req, language);
            result.setStatus(1);
        } catch (Exception e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

}
