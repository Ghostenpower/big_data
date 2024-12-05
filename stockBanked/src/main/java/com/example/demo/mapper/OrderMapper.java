package com.example.demo.mapper;


import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface OrderMapper {
//    public List<Order> getOrder(User userId);

    //public void createOrder(Order order);


//    @Select("select * from orders where order_id=#{order_id} and user_id=#{user_id} order by order_id DESC")
    List<Order> getOrder(Order order);

    void createOrder(Order order);
    @Select("update orders set total_price=#{total_price} where order_id=#{order_id}")
    void updatePrice(Order order);
}
