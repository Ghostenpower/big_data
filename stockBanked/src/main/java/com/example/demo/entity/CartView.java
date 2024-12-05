package com.example.demo.entity;

import lombok.Data;

@Data
public class CartView {
    private Integer cart_id;
    private Integer book_id;
    private Boolean is_choose;
    private Double total_price;
    private Integer quantity;
    private String name;
    private Double price;
    private String image;
}
