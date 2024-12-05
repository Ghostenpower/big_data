package com.example.demo.entity;

import lombok.Data;

@Data
public class Admin {
    private int admin_id;
    private String username;
    private String password;
    private String email;
}
