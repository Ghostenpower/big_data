package com.example.demo.controller;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Book;
import com.example.demo.entity.Result;
import com.example.demo.entity.User;
import com.example.demo.service.AdminService;
import com.example.demo.service.BookService;
import com.example.demo.service.UserService;
import com.example.demo.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody Admin admin){
        List<Admin> list= adminService.getAdmin(admin);
        if (list.isEmpty()){
            return Result.error("账号或密码错误");
        }else{
            Map<String, Object> map=new HashMap<>();
            map.put("username",list.get(0).getUsername());
            map.put("admin_id",list.get(0).getAdmin_id());
            map.put("email",list.get(0).getEmail());
            String token= JWT.CreateJwt(map);
            map.put("token",token);
            return Result.success(map);
        }
    }

    @PostMapping("/updateBookNum")
    public Result updateBookNum(Book book){
        bookService.updateBookNum(book);
        return Result.success("success");
    }

    @PostMapping("/updateBookPrice")
    public Result updateBookPrice(Book book){
        bookService.updateBookPrice(book);
        return Result.success("success");
    }

    @PostMapping("/getUsers")
    public Result getUsers(User user){
        List<User> list=userService.getUser(user);
        return Result.success(list);
    }
}
