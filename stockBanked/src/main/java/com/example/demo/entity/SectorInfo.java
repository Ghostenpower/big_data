package com.example.demo.entity;

import lombok.Data;

@Data
public class SectorInfo {
    private String industry;
    private Integer count;
    private Integer count_over_2;
    private Integer high_heat_count;
}
