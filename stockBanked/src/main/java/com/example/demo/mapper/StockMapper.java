package com.example.demo.mapper;

import com.example.demo.entity.IndexDaily;
import com.example.demo.entity.SectorInfo;
import com.example.demo.entity.StockDailyData;
import com.example.demo.entity.StockInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockMapper {
//    @Select("SELECT * FROM stock_info WHERE ts_code = #{ts_code}")
    StockInfo getOneStockInfo(StockInfo stockInfo);

    List<StockInfo> getStockInfoByIndustry(StockInfo stockInfo);
    @Select("SELECT COUNT(*) FROM stock_daily_data WHERE ts_code=#{ts_code}")
    Integer getStockDailgDataCount(StockDailyData stockDailyData);

    @Select("SELECT * FROM stock_daily_data WHERE ts_code=#{ts_code}")
    List<StockDailyData> getStockDailgData(StockInfo stockInfo);

    List<IndexDaily> getNewIndexData(StockInfo stockInfo);

    List<IndexDaily> getAllIndexData();

    @Select("SELECT * FROM sector_stock_count_and_vol_ratio_over_2 order by count_over_2 desc ,count desc ")
    List<SectorInfo> getSectorInfo();

    List<StockInfo> getAllStockInfo();

    List<SectorInfo> getHighHeatSector();
}
