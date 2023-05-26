package com.wjy.learn.springboot.service;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.wjy.learn.springboot.bo.UserInfo;
import com.wjy.learn.springboot.dao.IUserInfoDao;
import com.wjy.learn.springboot.inf.IUser;

@Service
public class UserImpl implements IUser {

    @Resource
    private IUserInfoDao userInfoDao;

    @Override
    // 缓存关联方法块内的u变量，key为入参id
    @Cacheable(value = "u", key = "#id")
    public UserInfo getUser(Integer id) {
        UserInfo u = userInfoDao.selectById(id);
        return u;
    }

}
