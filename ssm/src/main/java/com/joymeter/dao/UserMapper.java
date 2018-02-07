package com.joymeter.dao;

import com.joymeter.entity.User;

public interface UserMapper {
    
    User selectUserByUserName(String username);
    
    int deleteByPrimaryKey(Integer userid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}
