package com.example.demo.service.impl;

import com.example.demo.entity.IndexDaily;
import com.example.demo.entity.SectorInfo;
import com.example.demo.entity.StockDailyData;
import com.example.demo.entity.StockInfo;
import com.example.demo.mapper.StockMapper;
import com.example.demo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {
    private final StockMapper stockMapper;

    public StockServiceImpl(StockMapper stockMapper) {
        this.stockMapper = stockMapper;
    }

    public StockInfo getOneStockInfo(StockInfo stockInfo) {
        return stockMapper.getOneStockInfo(stockInfo);
    }

    @Override
    public List<StockInfo> getStockInfoByIndustry(StockInfo stockInfo) {
        return stockMapper.getStockInfoByIndustry(stockInfo);
    }

    @Override
    public Integer getStockDailgDataCount(StockDailyData stockDailyData) {
        return stockMapper.getStockDailgDataCount(stockDailyData);
    }

    @Override
    public List<StockDailyData> getStockDailgData(StockInfo stockInfo) {
        return stockMapper.getStockDailgData(stockInfo);
    }

    @Override
    public List<IndexDaily> getNewIndexData(StockInfo stockInfo) {
        return stockMapper.getNewIndexData(stockInfo);
    }

    @Override
    public List<SectorInfo> getSectorInfo() {
        return stockMapper.getSectorInfo();
    }

    @Override
    public List<IndexDaily> getAllIndexData() {
        return stockMapper.getAllIndexData();
    }

    @Override
    public List<StockInfo> getAllStockInfo() {
        return stockMapper.getAllStockInfo();
    }

    @Override
    public List<SectorInfo> getHighHeatSector() {
        return stockMapper.getHighHeatSector();
    }

}
