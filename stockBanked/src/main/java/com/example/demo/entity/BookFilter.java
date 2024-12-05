package com.example.demo.entity;

import lombok.Data;

import java.util.List;

@Data
public class BookFilter {
    private Integer book_id;

    private Boolean isisLikeName;
    private String name;

    private Boolean isisLikeAuthor;
    private String author;

    private Boolean isLikeGenre;
    private String genre;

    private Double price;
    private Integer quantity;
    private String descripition;

    private Integer currentPage;
    private Integer pageSize;
}
