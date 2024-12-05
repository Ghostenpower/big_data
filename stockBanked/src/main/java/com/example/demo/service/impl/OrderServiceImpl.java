package com.example.demo.service.impl;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Override
    public void createOrder(Order order) {
        orderMapper.createOrder(order);
    }

    @Override
    public List<Order> getOrder(Order order){
        List<Order> list = orderMapper.getOrder(order);
        return list;
    }

    @Override
    public void updatePrice(Order order) {
        orderMapper.updatePrice(order);
    }
}
