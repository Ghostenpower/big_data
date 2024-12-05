package com.example.demo.entity;

import lombok.Data;

import java.util.List;
@Data
public class Pager<T> {
    private int page=1;//分页起始页
    private int size;//每页记录数
    private List<T> data;//返回的记录集合
    private long total;//总记录条数
}
