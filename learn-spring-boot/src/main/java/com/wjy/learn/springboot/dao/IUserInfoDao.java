package com.wjy.learn.springboot.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.wjy.learn.springboot.bo.UserInfo;

@Repository
public interface IUserInfoDao {

    UserInfo selectById(@Param("userId") Integer userId);

    Integer insert(UserInfo user);

    Integer update(UserInfo user);
}
