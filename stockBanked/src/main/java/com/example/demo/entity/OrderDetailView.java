package com.example.demo.entity;

import lombok.Data;

@Data
public class OrderDetailView {
    private Integer detail_id;
    private Integer book_id;
    private Integer order_id;
    private String name;
    private Double price;
    private Integer quantity;
    private Double total_price;
}
