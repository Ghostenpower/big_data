package com.example.demo.controller;


import com.example.demo.entity.Result;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    /**
     * 获取所有user信息
     * @return
     */
    @GetMapping("/getAll")
    public Result getAll(){
        List<User> users=userService.getAll();
        return Result.success(users);
    }

    @PostMapping("/getOne")
    public List<User> getOne(@RequestBody User user){
        List<User> list=userService.getOne(user);
        return  list;
    }

}
