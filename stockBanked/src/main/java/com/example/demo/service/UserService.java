package com.example.demo.service;

import com.example.demo.entity.User;

import java.util.List;

public interface UserService {
    //获取全部信息
    List<User> getAll();

    List<User> getOne(User user);

    void createUser(User user);

    List<User> getUser(User user);
}
