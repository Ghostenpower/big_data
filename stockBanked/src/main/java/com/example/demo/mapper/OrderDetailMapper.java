package com.example.demo.mapper;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.OrderDetailView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface OrderDetailMapper {
//    @Select("select * from order_details where order_id=#{order_id}")
    public List<OrderDetailView> getDetail(Order order);

    void insertOrderDetail(OrderDetail orderDetail);
}
