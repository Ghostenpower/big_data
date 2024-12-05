package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.OrderDetailView;
import com.example.demo.entity.Result;
import com.example.demo.service.OrderDetailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderDetailController {
    @Autowired
    OrderDetailService orderDetailService;

    @PostMapping("getDetail")
    public Result getDetail( @RequestBody Order order){
        List<OrderDetailView> list=orderDetailService.getDetail(order);
        return Result.success(list);
    }

}
