package com.example.demo.entity;

import com.mysql.cj.xdevapi.JsonArray;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Order {
    private Integer order_id;
    private Integer user_id;
    private Double total_price;
    private Timestamp order_date;
    private String status;
}
