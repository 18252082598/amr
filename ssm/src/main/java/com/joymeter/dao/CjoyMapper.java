package com.joymeter.dao;

import java.util.List;

import com.joymeter.entity.Cjoy;

public interface CjoyMapper {
    int deleteByPrimaryKey(Integer cjoyid);

    int insert(Cjoy record);

    int insertSelective(Cjoy record);

    Cjoy selectByPrimaryKey(Integer cjoyid);

    int updateByPrimaryKeySelective(Cjoy record);

    int updateByPrimaryKey(Cjoy record);
    
    List<Cjoy> getAllCjoy();
}