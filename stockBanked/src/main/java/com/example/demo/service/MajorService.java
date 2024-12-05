package com.example.demo.service;

import com.example.demo.entity.Major;

import java.util.List;

public interface MajorService {
    List<Major> getAll();

    //条件查询test
    List<Major> getOne(Major major);
}
