package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public List<User> getAll() {
        return userMapper.getAll();
    }

    @Override
    public List<User> getOne(User user) {
        List<User> list=userMapper.getOne(user);
        return  list;
    }

    @Override
    public void createUser(User user) {
        userMapper.createUser(user);
    }

    @Override
    public List<User> getUser(User user) {
        List<User> list=userMapper.getUser(user);
        return list;
    }
}
