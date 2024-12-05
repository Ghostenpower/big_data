package com.example.demo.mapper;

import com.example.demo.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface AdminMapper {
    @Select("select * from admins where username=#{username} and password=#{password}")
    List<Admin> getAdmin(Admin admin);
}
