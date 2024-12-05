package com.example.demo.entity;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class IndexDaily {
    private String ts_code;  // 股票代码
    private int trade_date;  // 交易日期 (yyyyMMdd)
    private BigDecimal close;   // 收盘价
    private BigDecimal open;    // 开盘价
    private BigDecimal high;    // 最高价
    private BigDecimal low;     // 最低价
    private BigDecimal pre_close;  // 昨日收盘价
    private BigDecimal chg;      // 涨跌额
    private BigDecimal pct_chg;   // 涨跌幅
    private BigDecimal vol;      // 成交量
    private BigDecimal amount;   // 成交金额
}
