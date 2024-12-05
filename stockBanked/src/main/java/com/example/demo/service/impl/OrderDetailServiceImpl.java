package com.example.demo.service.impl;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.OrderDetailView;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Override
    public List<OrderDetailView> getDetail(Order order) {
        List<OrderDetailView> list=orderDetailMapper.getDetail(order);
        return list;
    }
    @Override
    public void  insertOrderDetail(OrderDetail orderDetail){
        orderDetailMapper.insertOrderDetail(orderDetail);
    }
    }

