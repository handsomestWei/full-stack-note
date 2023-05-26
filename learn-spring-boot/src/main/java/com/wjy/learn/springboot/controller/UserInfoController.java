package com.wjy.learn.springboot.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wjy.learn.springboot.bo.UserInfo;
import com.wjy.learn.springboot.inf.IUser;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/user", produces = { "application/json;charset=UTF-8" })
@Slf4j
public class UserInfoController {

    @Resource
    private IUser user;

    @GetMapping(value = "/name")
    public String getUserName(@RequestParam("id") int id) {
        UserInfo u = null;
        try {
            u = user.getUser(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return u != null ? u.getUserName() : "";
    }

}
