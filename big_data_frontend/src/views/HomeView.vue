<template>
  <main>
    <el-row justify="center" style="margin-bottom: 10px;">
      <el-col>
        <el-descriptions :title="indexInfo.name" border>
          <el-descriptions-item label="股票代码">{{ indexInfo.ts_code }}</el-descriptions-item>
          <el-descriptions-item label="指数全名">{{ indexInfo.fullname }}</el-descriptions-item>
          <el-descriptions-item label="市场">
            <el-tag size="small">SZSE</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="发布者">{{ indexInfo.publisher }}</el-descriptions-item>
          <el-descriptions-item label="指数类型">{{ indexInfo.index_type }}</el-descriptions-item>
          <el-descriptions-item label="类别">综合指数</el-descriptions-item>
          <el-descriptions-item label="基准日期">{{ indexInfo.base_date }}</el-descriptions-item>
          <el-descriptions-item label="基准点">{{ indexInfo.base_point }}</el-descriptions-item>
          <el-descriptions-item label="上市日期">{{ indexInfo.list_date }}</el-descriptions-item>
          <el-descriptions-item label="权重规则">{{ indexInfo.weight_rule }}</el-descriptions-item>
          <el-descriptions-item label="描述">
            {{ indexInfo.desc }}
          </el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>
    <!-- 1第一行 -->
    <el-row justify="center" :gutter="10">
      <!-- 1第一列 -->
      <el-col :span="8" style="align-self: center;">
        <!-- 2第一行 -->
        <el-row :gutter="10" justify="center" style="margin-bottom: 10px;">
          <!-- 2第一列 -->
          <el-col :span="8">
            <el-card style="max-width: 400px">
              <el-row>
                <el-col align="end"><el-text :type="analyzeData.pct_chg > 0 ? 'danger' : 'success'"> </el-text>
                  <el-icon>
                    <Grid />
                  </el-icon>
                </el-col>
              </el-row>
              <el-row><el-col align="center"><el-text size="large">{{ analyzeData.trade_date
                    }}</el-text></el-col></el-row>
              <el-row><el-col align="center"><el-text type="info">日期</el-text></el-col></el-row>
            </el-card>
          </el-col>
          <!-- 2第二列 -->
          <el-col :span="8">
            <el-card style="max-width: 400px">
              <el-row><el-col align="end"><el-text :type="analyzeData.pct_chg > 0 ? 'danger' : 'success'">{{
                analyzeData.pct_chg }}%</el-text>
                  <el-icon :color="analyzeData.pct_chg > 0 ? '#f56c6c' : '#6ac23a'">
                    <Top v-if="analyzeData.pct_chg > 0" />
                    <Bottom v-if="analyzeData.pct_chg <= 0" />
                  </el-icon>
                </el-col>
              </el-row>
              <el-row><el-col align="center"><el-text size="large">{{ analyzeData.close }}</el-text></el-col></el-row>
              <el-row><el-col align="center"><el-text type="info">价格</el-text></el-col></el-row>
            </el-card>
          </el-col>
        </el-row>
        <!-- 2第二行 -->
        <el-row :gutter="10" justify="center" style="margin-bottom: 10px;">
          <!-- 2第一列 -->
          <el-col :span="8">
            <el-card style="max-width: 400px">
              <el-row><el-col align="end"><el-text :type="analyzeData.pct_vol > 0 ? 'danger' : 'success'">{{
                analyzeData.pct_vol }}%</el-text>
                  <el-icon :color="analyzeData.pct_vol > 0 ? '#f56c6c' : '#6ac23a'">
                    <Top v-if="analyzeData.pct_vol > 0" />
                    <Bottom v-if="analyzeData.pct_vol <= 0" />
                  </el-icon>
                </el-col>
              </el-row>
              <el-row><el-col align="center"><el-text size="large">{{ analyzeData.vol }}</el-text></el-col></el-row>
              <el-row><el-col align="center"><el-text type="info">成交量</el-text></el-col></el-row>
            </el-card>
          </el-col>
          <!-- 2第二列 -->
          <el-col :span="8">
            <el-card style="max-width: 400px">
              <el-row><el-col align="end"><el-text :type="analyzeData.pct_amount > 0 ? 'danger' : 'success'">{{
                analyzeData.pct_amount }}%</el-text>
                  <el-icon :color="analyzeData.pct_amount > 0 ? '#f56c6c' : '#6ac23a'">
                    <Top v-if="analyzeData.pct_amount > 0" />
                    <Bottom v-if="analyzeData.pct_amount <= 0" />
                  </el-icon>
                </el-col>
              </el-row>
              <el-row><el-col align="center"><el-text size="large">{{ analyzeData.amount }}</el-text></el-col></el-row>
              <el-row><el-col align="center"><el-text type="info">成交金额</el-text></el-col></el-row>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
      <!-- 1第二列 -->
      <el-col :span="14">
        <div :v-loading="isLoading" ref="indexAllDailyChart" style="width: 100%; height: 400px;"></div>
      </el-col>
    </el-row>
    <!-- 1第二行 -->
    <el-row :gutter="10">
      <!-- 1第一列 -->
      <el-col :span="24">
        <div v-loading="isLoading" ref="hotSections" style="width: 100%; height: 400px;"></div>
      </el-col>
    </el-row>
    <el-row>
      <el-col>
        <div v-loading="isLoading" ref="quantityRatio" style="width: 100%; height: 700px;"></div>
      </el-col>
    </el-row>
    <el-row>
      <el-col>
        <div v-loading="isLoading" ref="wordCloud" style="width: 100%; height: 600px;"></div>
      </el-col>
    </el-row>
  </main>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { getNewIndexData, getSectorInfo, getAllIndexData, getAllStockInfo } from '../axios/api'
import * as echarts from 'echarts';
import 'echarts-wordcloud';
//深证A指基础信息
const indexInfo = reactive({
  ts_code: "399107.SZ",       // 指数代码
  name: "深证A指",            // 指数名称
  fullname: "深证A股指数",    // 指数全名
  market: "SZSE",             // 市场
  publisher: "深圳证券交易所",// 发布者
  index_type: "深证综合指数",// 指数类型
  category: "综合指数",      // 类别
  base_date: "19910403",      // 基准日期
  base_point: 100,            // 基准点
  list_date: "19921004",      // 上市日期
  weight_rule: "其他",        // 权重规则
  desc: "深证A股指数反映了在深圳证券交易所主板、创业板上市的全部A股股票的价格综合变动情况以及市场总体走势，为市场提供了全面而客观的业绩衡量基准和投资参考工具。"                    // 描述
})

const indexParams = reactive({
  ts_code: '399107.SZ',
  name: '',
  industry: ''
})

const isLoading = ref(false)

const indexData = reactive([])
//分析后的最新深证A指数据
const analyzeData = computed(() => {
  if (indexData.length > 0) {
    return {
      "trade_date": indexData[0].trade_date.toString().replace(/(\d{4})(\d{2})(\d{2})/, '\$1-\$2-\$3'),
      "close": indexData[0].close,
      "pre_close": indexData[0].pre_close,
      "chg": indexData[0].chg,
      "pct_chg": indexData[0].pct_chg,
      "vol": indexData[0].vol,
      "pre_vol": indexData[1].vol,
      "pct_vol": ((indexData[0].vol - indexData[1].vol) / indexData[1].vol * 100).toFixed(2),
      "amount": indexData[0].amount,
      "pre_amount": indexData[1].amount,
      "pct_amount": ((indexData[0].amount - indexData[1].amount) / indexData[1].amount * 100).toFixed(2)
    }
  } else {
    return {
      "trade_date": 0,
      "close": 0,
      "pre_close": 0,
      "chg": 0,
      "pct_chg": 0,
      "vol": 0,
      "pre_vol": 0,
      "pct_vol": 0,
      "amount": 0,
      "pre_amount": 0,
      "pct_amount": 0
    }
  }

})

// #region 板块图表设置
const hotSections = ref(null)
const hotSectionsData = reactive([])
const industries = computed(() => hotSectionsData.map(item => item.industry));
const counts = computed(() => hotSectionsData.map(item => item.count));
const countsOver2 = computed(() => hotSectionsData.map(item => item.count_over_2));
const hotSectionsChartInit = () => {
  // 初始化 ECharts 实例
  const hotSectionsChart = echarts.init(hotSections.value);

  // 配置 ECharts
  const hotsectionschartoption = {
    title: {
      text: '板块热度排名(截至2016年6月8日)'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['板块股票总量', '板块中量比大于2的股票数量']
    },
    xAxis: {
      type: 'category',
      data: industries.value
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '板块股票总量',
        type: 'bar',
        stack: '总量',
        data: counts.value,
        emphasis: {
          focus: 'series'
        },
        itemStyle: {
          color: '#87CEEB' // skyblue
        }
      },
      {
        name: '板块中量比大于2的股票数量',
        type: 'bar',
        stack: '总量',
        data: countsOver2.value,
        emphasis: {
          focus: 'series'
        },
        itemStyle: {
          color: '#FFA500' // orange
        }
      }
    ]
  };

  // 使用配置项填充图表
  hotSectionsChart.setOption(hotsectionschartoption);

  // 监听窗口变化，重新设置图表大小
  window.addEventListener('resize', () => {
    hotSectionsChart.resize();
  });
}
// #endregion

// #region 指数全部日线数据
const indexAllDailyData = reactive([])
const indexAllDailyChart = ref(null);
// 提取 x 轴和 y 轴数据（使用 computed）
const dates = computed(() => indexAllDailyData.map(item => item.trade_date));  // 获取日期
const closePrices = computed(() => indexAllDailyData.map(item => item.close));  // 获取收盘价
const openPrices = computed(() => indexAllDailyData.map(item => item.open));    // 获取开盘价
const highPrices = computed(() => indexAllDailyData.map(item => item.high));    // 获取最高价
const lowPrices = computed(() => indexAllDailyData.map(item => item.low));      // 获取最低价
const indexAllDailyChartInit = () => {
  const renderindexAllDailyChartInstance = echarts.init(indexAllDailyChart.value);

  const option = {
    title: {
      text: '深证A股指数日线数据',
      left: 'left'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['Close', 'Open', 'High', 'Low']
    },
    xAxis: {
      type: 'category',
      data: dates.value,  // 从 computed 获取日期
      axisLabel: {
        rotate: 20  // 旋转日期标签以避免重叠
      }
    },
    yAxis: {
      type: 'value',
      scale: true,
    },
    series: [
      {
        name: 'Close',
        type: 'line',
        data: closePrices.value,  // 从 computed 获取收盘价
        smooth: true
      },
      {
        name: 'Open',
        type: 'line',
        data: openPrices.value,  // 从 computed 获取开盘价
        smooth: true
      },
      {
        name: 'High',
        type: 'line',
        data: highPrices.value,  // 从 computed 获取最高价
        smooth: true
      },
      {
        name: 'Low',
        type: 'line',
        data: lowPrices.value,  // 从 computed 获取最低价
        smooth: true
      }
    ],
    // 添加 dataZoom 配置
    dataZoom: [
      {
        type: 'inside',  // 内部缩放，鼠标滚轮可以缩放图表
        start: 90,        // 初始显示范围的起始百分比（0为最左端）
        end: 100         // 初始显示范围的结束百分比（100为最右端）
      },
      {
        type: 'slider',  // 外部滑动条缩放
        show: true,      // 显示滑动条
        start: 90,        // 初始显示范围的起始百分比（0为最左端）
        end: 100,       // 初始显示范围的结束百分比（100为最右端）
      }
    ]
  };

  renderindexAllDailyChartInstance.setOption(option);
};
// #endregion

/*股票基础数据以及spark分析后的数据*/
const stockInfos = reactive([])
/*end*/

// #region 成交量以及量比判断热门股票数据, vol为当日成交量，量比=当日成交量/五日平均成交量
const vol = computed(() => stockInfos.map(stock => stock.vol));  // 当日成交量
const avg_vol_5 = computed(() => stockInfos.map(stock => stock.avg_vol_5));  // 五日平均成交量
const volume_ratio = computed(() => stockInfos.map(stock => stock.volume_ratio));  // 量比
const name = computed(() => stockInfos.map(stock => stock.name));  // 股票名称
const quantityRatio = ref(null);//图表

// 初始化气泡图
const quantityRatioChartInit = () => {
  const chart = echarts.init(quantityRatio.value); // 获取图表容器的实例

  const option = {
    title: {
      text: "股票量比图"
    },
    tooltip: {
      trigger: 'item',
      formatter: function (params) {
        const dataIndex = params.dataIndex;
        return `
          <strong>股票名称: ${name.value[dataIndex]}</strong><br/>
          五日平均成交量: ${avg_vol_5.value[dataIndex]}<br/>
          当日成交量: ${vol.value[dataIndex]}<br/>
          量比: ${params.value[2]}<br/>
        `;
      }
    },
    xAxis: {
      type: 'value',
      name: '五日平均成交量',
      axisLabel: {
        formatter: '{value}'
      },
      splitLine: {
        show: false
      },
    },
    yAxis: {
      type: 'value',
      name: '当日成交量',
      axisLabel: {
        formatter: '{value}'
      },
    },
    series: [
      {
        type: 'scatter',
        symbolSize: function (data) {
          const volumeRatio = data[2];  // 量比
          return 10 + Math.min(volumeRatio, 10) * 10; // 根据量比调整气泡大小
        },
        data: stockInfos.map((stock, i) => {
          const xValue = avg_vol_5.value[i];  // 五日平均成交量
          const yValue = vol.value[i];        // 当日成交量
          const volumeRatio = volume_ratio.value[i];  // 量比作为气泡大小

          // 根据量比计算颜色
          const color = getColorByVolumeRatio(volumeRatio);

          return {
            value: [xValue, yValue, volumeRatio],
            itemStyle: {
              color: color,  // 根据量比动态改变颜色
            }
          };
        }),
        name: '热门股票成交量数据',
        emphasis: {
          itemStyle: {
            borderColor: 'red',
            borderWidth: 2,
          }
        }
      }
    ],
    dataZoom: [
      {
        type: 'inside',  // 内部缩放，允许鼠标滚动缩放
        start: 0,  // 初始缩放比例
        end: 20,
      },
      {
        type: 'slider',  // 滑动条缩放，允许拖动缩放
        show: true,
        xAxisIndex: [0],
        start: 0,
        end: 20,
      }
    ],
  };

  // 设置图表选项
  chart.setOption(option);
};

// 根据量比值返回不同的颜色
const getColorByVolumeRatio = (volumeRatio) => {
  if (volumeRatio < 1) {
    return '#73c0de';  // 量比较小时，蓝色
  } else if (volumeRatio >= 1 && volumeRatio < 3) {
    return '#ffcc00';  // 量比适中时，黄色
  } else if (volumeRatio >= 3 && volumeRatio < 5) {
    return '#ff7f50';  // 量比较高时，橙色
  } else {
    return '#ff4500';  // 量比非常高时，红色
  }
};

// #endregion 成交量和五日平均成交量

// #region 词云图初始化
const wordCloud = ref(null);

// 使用计算属性来动态计算词云数据
const wordCloudData = computed(() => {
  return hotSectionsData
    .filter(item => item.high_heat_count > 0)  // 过滤掉热度为 0 的项
    .map(item => ({
      name: item.industry,
      value: item.high_heat_count
    }));
});

// 初始化词云图
const wordCloudChartInit = () => {
  const chart = echarts.init(wordCloud.value);

  const option = {
    title: {
      text: '行业热度词云图',
      left: 'left',
    },
    tooltip: {
      show: true,
      formatter: function (params) {
        return `${params.name}: ${params.value}`;
      }
    },
    visualMap: {
      show: false,  // 隐藏 visualMap 控件
      min: 0,  // 设置 min 值，依据你的数据范围调整
      max: 20,  // 设置 max 值，依据你的数据范围调整
      inRange: {
        color: ['#1E90FF', '#FFD700', '#FF6347']  // 从低到高值的颜色映射
      },
      calculable: true,  // 开启可计算的范围
    },
    series: [
      {
        type: 'wordCloud',
        sizeRange: [20, 100],  // 设置字体大小范围
        rotationRange: [-90, 90],  // 设置旋转角度范围
        rotationStep: 45,
        gridSize: 10,  // 网格大小
        shape: 'circle',  // 形状为圆形
        width: '100%',  // 宽度100%
        height: '100%',  // 高度100%
        drawOutOfBound: false,  // 不允许绘制出界的部分
        data: wordCloudData.value,  // 使用计算属性传递的数据
        emphasis: {
          itemStyle: {
            shadowBlur: 10,  // 设置阴影
            shadowColor: '#333',  // 阴影颜色
            shadowOffsetX: 5,  // 阴影X偏移
            shadowOffsetY: 5,  // 阴影Y偏移
          }
        },
        itemStyle: {
          color: function (params) {
            // 这里通过 visualMap 来控制颜色，color 回调可以省略
          }
        }
      }
    ],
    dataZoom: [
      {
        type: 'inside',
        start: 0,
        end: 100,
      },
      {
        type: 'slider',
        show: true,
        xAxisIndex: [0],
        start: 0,
        end: 100,
      }
    ],
  };

  // 设置图表选项
  chart.setOption(option);
};
// #endregion

onMounted(async () => {
  try {
    isLoading.value = true

    // #region 获取指数数据
    const response = await getNewIndexData(indexParams)
    if (response.data) {
      indexData.length = 0;
      indexData.push(...response.data);
    }
    // #endregion

    // #region 获取板块数据
    const response1 = await getSectorInfo();
    if (response1.data) {
      hotSectionsData.length = 0;
      hotSectionsData.push(...response1.data);
    }
    // 使用配置项填充图表
    hotSectionsChartInit()
    // #endregion

    // #region 获取指数全部数据
    const response2 = await getAllIndexData();
    if (response2.data) {
      indexAllDailyData.length = 0;
      indexAllDailyData.push(...response2.data);
    }
    indexAllDailyChartInit();
    // #endregion

    // #region 获取stockInfo数据
    const response3 = await getAllStockInfo();
    if (response3.data) {
      stockInfos.length = 0;
      stockInfos.push(...response3.data);
    }
    // #endregion
    quantityRatioChartInit();
    wordCloudChartInit();
  } catch (error) {
    console.error('获取指数数据失败', error);  // 错误处理
  } finally {
    isLoading.value = false
  }
})
</script>
