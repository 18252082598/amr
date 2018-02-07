package com.joymeter.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.joymeter.dao.CjoyMapper;
import com.joymeter.entity.Cjoy;
import com.joymeter.service.CjoyService;

@Service
public class CjoyServiceImpl implements CjoyService{
    @Resource
    private CjoyMapper cjoyMapper;

    @Override
    public List<Cjoy> getAllCjoy() {
        return cjoyMapper.getAllCjoy();
    }
    
    
}
