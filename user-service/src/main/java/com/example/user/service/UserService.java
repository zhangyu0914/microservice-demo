package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.user.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {

    Map<String, String>  login(String username, String password);

    Map<String, String> ldapLogin(String username, String password);

    Map<String, String> githubLogin(String code);
}
