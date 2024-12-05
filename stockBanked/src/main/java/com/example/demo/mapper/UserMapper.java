package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from users")
    List<User> getAll();

    List<User> getOne(User user);
    @Insert("insert into users(user_id,username,password,email) values (#{user_id},#{username},#{password},#{email})")
    void createUser(User user);

    List<User> getUser(User user);
}
