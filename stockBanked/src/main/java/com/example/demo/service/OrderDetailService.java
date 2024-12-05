package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.OrderDetailView;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderDetailService {
    public List<OrderDetailView> getDetail(Order order);

    void  insertOrderDetail(OrderDetail orderDetail);


}
