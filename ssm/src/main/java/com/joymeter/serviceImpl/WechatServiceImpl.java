package com.joymeter.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joymeter.dao.WechatMapper;
import com.joymeter.entity.Wechat;
import com.joymeter.service.WechatService;
@Service
public class WechatServiceImpl implements WechatService {
    @Autowired
    private WechatMapper wechatMapper;

    @Override
    public List<Wechat> getAllWechat() {
        return wechatMapper.getAllWechat();
    }
    
    
    
}
