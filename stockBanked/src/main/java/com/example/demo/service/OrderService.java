package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface OrderService {
    //public List<Order> getOrder(User userId);

    public void createOrder(Order order);
    public List<Order> getOrder(Order order);

    void updatePrice(Order order);
}
