package com.example.demo.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockDailyData {
    private String ts_code;            // 股票代码
    private Integer trade_date;            // 交易日期，通常使用int类型（YYYYMMDD）
    private BigDecimal open;          // 开盘价
    private BigDecimal high;          // 最高价
    private BigDecimal low;           // 最低价
    private BigDecimal close;         // 收盘价
    private BigDecimal pre_close;      // 昨日收盘价
    private BigDecimal chg;           // 涨跌额
    private BigDecimal pct_chg;        // 涨跌幅
    private BigDecimal vol;           // 成交量
    private BigDecimal amount;        // 成交金额
    private BigDecimal turnover_rate;  // 换手率
    private BigDecimal pe;            // 市盈率
    private BigDecimal pb;            // 市净率
    private BigDecimal ps;            // 市销率
    private BigDecimal total_mv;       // 总市值
    private BigDecimal circ_mv;        // 流通市值
}
