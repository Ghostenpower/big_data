package com.example.demo.controller;

import com.example.demo.entity.Result;
import com.example.demo.entity.User;
import com.example.demo.service.CartService;
import com.example.demo.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    CartService cartService;

    @RequestMapping(value="/login",method= RequestMethod.POST )
    public Result login(@RequestBody/*接收json格式*/ User user){
        String username=user.getUsername();
        String password=user.getPassword();
        String token;
        String sql="select * from users where username=? and password=?";//防止sql注入
//        System.out.println(sql);
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql,username,password);//查询数据库
        if(!list.isEmpty()){
            Object user_id=list.get(0).get("user_id");
            Map<String, Object> map=new HashMap<>();
            map.put("username",username);
            map.put("user_id",user_id);

            Integer userId = (Integer) user_id;
            Integer cart_id=cartService.getCartId(userId);
            map.put("cart_id",cart_id);
            token= JWT.CreateJwt(map);//生成token
            Map<String, Object> userMap=new HashMap<>();
            list.get(0).remove("password");//删除密码的map中的密码，防止密码泄露
            map.put("token",token);
            userMap.put("user",map);
            return Result.success(userMap);
        }
        else{
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("result", "false"); // 错误输出
            return Result.error(responseMap);
        }
    }
}
