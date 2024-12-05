package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("stock")
public class StockController {
    @Autowired
    private StockService stockService;

    @PostMapping("get")
    public Result getStock() {
        return Result.success(1);
    }

    @PostMapping("getOneStockInfo")
    public Result getOneStockInfo(@RequestBody StockInfo stockInfo) {
        System.out.println("getOneStockInfo" + stockInfo.getTs_code());
        StockInfo result = stockService.getOneStockInfo(stockInfo);
        System.out.println(result);
        return Result.success(result);
    }

    @PostMapping("getStockInfoByIndustry")
    public Result getStockInfoByIndustry(@RequestBody StockInfo stockInfo) {
        List<StockInfo> result = stockService.getStockInfoByIndustry(stockInfo);
        return Result.success(result);
    }

    @PostMapping("getStockDailyData")
    public Result getStockDailgData(@RequestBody StockInfo stockInfo) {
        System.out.println(stockInfo.getTs_code());
        List<StockDailyData> result = stockService.getStockDailgData(stockInfo);
        return Result.success(result);
    }

    @PostMapping("getNewIndexData")
    public Result getNewIndexData(@RequestBody StockInfo stockInfo) {
        System.out.println(stockInfo.getTs_code());
        List<IndexDaily> result = stockService.getNewIndexData(stockInfo);
        return Result.success(result);
    }

    @PostMapping("getSectorInfo")
    public Result getSectorInfo() {
        List<SectorInfo> result = stockService.getSectorInfo();
        return Result.success(result);
    }

    @PostMapping("getAllIndexData")
    public Result getAllIndexData() {
        List<IndexDaily> result = stockService.getAllIndexData();
        return Result.success(result);
    }

//    @PostMapping("getValuation")
//    public Result getValuation(@RequestBody StockInfo stockInfo) {
//
//        return Result.success(stockService.getValuation(stockInfo));
//    }

    @PostMapping("getAllStockInfo")
    public Result getAllStockInfo() {
        List<StockInfo> result = stockService.getAllStockInfo();
        return Result.success(result);
    }

    @PostMapping("getHighHeatSector")
    public Result getHighHeatSector() {
        List<SectorInfo> result = stockService.getHighHeatSector();
        return Result.success(result);
    }
}
