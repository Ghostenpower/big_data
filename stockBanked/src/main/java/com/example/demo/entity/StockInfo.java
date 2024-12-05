package com.example.demo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StockInfo {
    private String ts_code;
    private String name;
    private Date trade_date;
    private BigDecimal vol;
    private BigDecimal avg_vol_5;
    private BigDecimal volume_ratio;
    private String industry;
    private BigDecimal pe;
    private BigDecimal pb;
    private BigDecimal ps;
    private BigDecimal score;
    private BigDecimal avg_close;
    private BigDecimal max_close;
    private BigDecimal min_close;
    private Integer close_above_avg;
    private Integer close_below_avg;
    private BigDecimal avg_total_mv;
    private BigDecimal max_total_mv;
    private BigDecimal min_total_mv;
    private Integer total_mv_above_avg;
    private Integer total_mv_below_avg;
    private BigDecimal avg_circ_mv;
    private BigDecimal max_circ_mv;
    private BigDecimal min_circ_mv;
    private Integer circ_mv_above_avg;
    private Integer circ_mv_below_avg;
}
