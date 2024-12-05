package com.example.demo.entity;

import lombok.Data;

@Data
public class OrderDetail {
    private Integer detail_id;
    private Integer order_id;
    private Integer book_id;
    private Integer quantity;
    private Double total_price;
}
