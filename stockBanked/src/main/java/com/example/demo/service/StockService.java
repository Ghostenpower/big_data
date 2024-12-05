package com.example.demo.service;

import com.example.demo.entity.IndexDaily;
import com.example.demo.entity.SectorInfo;
import com.example.demo.entity.StockDailyData;
import com.example.demo.entity.StockInfo;

import java.util.List;

public interface StockService {
    StockInfo getOneStockInfo(StockInfo stockInfo);

    List<StockInfo> getStockInfoByIndustry(StockInfo stockInfo);

    Integer getStockDailgDataCount(StockDailyData stockDailyData);

    List<StockDailyData> getStockDailgData(StockInfo stockInfo);

    List<IndexDaily> getNewIndexData(StockInfo stockInfo);

    List<SectorInfo> getSectorInfo();

    List<IndexDaily> getAllIndexData();

    List<StockInfo> getAllStockInfo();

    List<SectorInfo> getHighHeatSector();
}
