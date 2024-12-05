package com.example.demo.controller;

import com.example.demo.entity.Result;
import com.example.demo.service.FlaskService;
import com.example.demo.service.impl.FlaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("flask")
public class FlaskCntroller {

    @Autowired
    FlaskService flaskService;

    @PostMapping("test")
    public String test(){
        return flaskService.sendMessageToFlask("getStockInfo");
    }
}
