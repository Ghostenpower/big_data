package com.example.demo.entity;

import lombok.Data;

@Data
public class Book {
    private Integer book_id;
    private String name;
    private String author;
    private String genre;
    private Double price;
    private Integer quantity;
    private String description;
    private String image;
}
