package com.example.demo.entity;



import lombok.Data;


@Data
public class User {
    private Integer user_id;
    private String username;
    private String password;
    private String email;
}
