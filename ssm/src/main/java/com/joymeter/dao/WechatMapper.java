package com.joymeter.dao;

import java.util.List;

import com.joymeter.entity.Wechat;

public interface WechatMapper {
    
    int deleteByPrimaryKey(Integer wechatid);

    int insert(Wechat record);

    int insertSelective(Wechat record);

    Wechat selectByPrimaryKey(Integer wechatid);

    int updateByPrimaryKeySelective(Wechat record);

    int updateByPrimaryKey(Wechat record);

 //   List<Wechat> selectByStatus(Integer status);

 //   List<Wechat> selectByUserId(Integer id);

 //   Wechat getWechatByappId(String appId);

 //   int updateByAppId(Wechat wechat);

    Wechat getWechatByCjoyId(Integer cjoyId);
    
    List<Wechat> getAllWechat();
    
}
