<template>
    <el-row>
        <el-col :span="4">
            <el-card style="max-width: 480px">
                <el-text size="small">
                    代码:{{ row.ts_code }}<br>
                </el-text>
                <el-text size="small">
                    名称:{{ row.name }}<br>
                </el-text>
                <el-text size="small">
                    行业:{{ row.industry }}<br>
                </el-text>
            </el-card>
        </el-col>
        <el-col :span="18" style="width: 100%;min-height: 300px;">
            <div :v-loading="isLoading" ref="chartRef" style="width: 100%; height: 300px;"></div>
        </el-col>
    </el-row>
    <el-divider />
    <el-row>
        <el-col>
            <el-carousel height="400px">
                <el-carousel-item>
                    <el-row>
                        <el-col :span="8">
                            <BarChart v-if="stockInfo" :avgClose="stockInfo.avg_close" :maxClose="stockInfo.max_close"
                                :minClose="stockInfo.min_close" :echartConfig="closeBarConfig"></BarChart>
                        </el-col>
                        <el-col :span="8">
                            <PieChart v-if="stockInfo" :closeAboveAvg="stockInfo.close_above_avg"
                                :closeBelowAvg="stockInfo.close_below_avg" :echart-config="closePieConfig" />
                        </el-col>
                        <el-divider />
                    </el-row>
                </el-carousel-item>
                <el-carousel-item>
                    <el-row>
                        <el-col :span="8">
                            <BarChart v-if="stockInfo" :avgClose="stockInfo.avg_total_mv"
                                :max-close="stockInfo.max_total_mv" :min-close="stockInfo.min_total_mv"
                                :echartConfig="totalMvBarCloseConfig"></BarChart>
                        </el-col>
                        <el-col :span="8">
                            <PieChart v-if="stockInfo" :closeAboveAvg="stockInfo.total_mv_above_avg"
                                :closeBelowAvg="stockInfo.total_mv_below_avg" :echart-config="totalMvPieCloseConfig" />
                        </el-col>
                    </el-row>
                </el-carousel-item>
                <el-carousel-item>
                    <el-row>
                        <el-col :span="8">
                            <BarChart v-if="stockInfo" :avgClose="stockInfo.avg_circ_mv"
                                :max-close="stockInfo.max_circ_mv" :min-close="stockInfo.min_circ_mv"
                                :echartConfig="circMvBarCloseConfig"></BarChart>
                        </el-col>
                        <el-col :span="8">
                            <PieChart v-if="stockInfo" :closeAboveAvg="stockInfo.circ_mv_above_avg"
                                :closeBelowAvg="stockInfo.circ_mv_below_avg" :echart-config="circMvPieCloseConfig" />
                        </el-col>
                    </el-row>
                </el-carousel-item>
            </el-carousel>
        </el-col>
    </el-row>
</template>

<script setup>
import { ref, onBeforeMount, reactive, onMounted, nextTick } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getStockDailyData, getOneStockInfo } from '../axios/api'
import PieChart from '../components/pieChart.vue'
import * as echarts from 'echarts';
import BarChart from '@/components/barChart.vue';
// #region 路由
const route = useRoute()
const router = useRouter()
// #endregion

// #region 日线数据变量
const dailyData = reactive([])

// #endregion

// #region 股票信息以及分析数据
const stockInfo = ref(null)
// #endregion

// #region 第一行表盘配置
const closeBarConfig = {
    title: {
        text: "收盘价统计",
    },
    xAxis: {
        data: ['Avg Close', 'Max Close', 'Min Close'],
    },
    series: {
        name: "收盘价"
    }
}
const closePieConfig = {
    title: {
        text: "股价情况"
    },
    legend: {
        data: ['高于平均值天数', '低于平均值天数']
    },
    series: {
        name: "收盘价情况",
        data: ["高于平均值天数", "低于平均值天数"]
    }
}
// #endregion

// #region 第二行表盘配置
const totalMvBarCloseConfig = {
    title: {
        text: "总市值统计（单位：万元）",
    },
    xAxis: {
        data: ['Avg total_mv', 'Max total_mv', 'Min total_mv'],
    },
    series: {
        name: "市值"
    }
}
const totalMvPieCloseConfig = {
    title: {
        text: "总市值情况（单位：万元）"
    },
    legend: {
        data: ['高于平均总市值天数', '低于平均总市值天数']
    },
    series: {
        name: "总市值情况",
        data: ["高于平均总市值天数", "低于平均总市值天数"]
    }
}
// #endregion

// #region 第三行表盘配置
const circMvBarCloseConfig = {
    title: {
        text: "流通市值统计（单位：万元）",
    },
    xAxis: {
        data: ['Avg total_mv', 'Max total_mv', 'Min total_mv'],
    },
    series: {
        name: "流通市值"
    }
}
const circMvPieCloseConfig = {
    title: {
        text: "流通市值情况（单位：万元）"
    },
    legend: {
        data: ['高于平均流通市值天数', '低于平均流通市值天数']
    },
    series: {
        name: "流通市值情况",
        data: ["高于平均流通市值天数", "低于平均流通市值天数"]
    }
}
// #endregion

// #region 初始化stockInfo,用于获取股票日线数据，初始值为000001.SZ
const row = ref(route.query.ts_code && route.query.name && route.query.industry ? {
    ts_code: route.query.ts_code,
    name: route.query.name,
    industry: route.query.industry
} : {
    ts_code: '000001.SZ',
    name: '平安银行',
    industry: '银行'
});
// #endregion

// #region 图表专用是否加载标志
const isLoading = ref(false)
// #endregion

// #region 创建一个ref来引用图表容器，配置图表初始化函数
const chartRef = ref(null);
let myChart = null;
//图表初始化函数
const initChart = () => {
    nextTick(() => {
        myChart = echarts.init(chartRef.value);

        // 处理新的数据格式，只保留所需字段
        const processedData = dailyData.map(item => {
            const tradeDate = item.trade_date.toString(); // trade_date 转为字符串
            const date = new Date(tradeDate.slice(0, 4), tradeDate.slice(4, 6) - 1, tradeDate.slice(6, 8));
            return [
                date.getTime(), // 时间戳
                item.open,  // 开盘价
                item.close,  // 收盘价
                item.low,  // 最高价
                item.high   // 最低价
            ];
        });

        const option = {
            title: {
                text: '股票 K线图',
                left: 'center'
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: { backgroundColor: '#6a7985' }
                },
                formatter: function (params) {

                    const param = params[0];
                    const date = new Date(param.value[0]);
                    return `
                        日期: ${date.toLocaleDateString()}<br>
                        开盘价: ${param.value[1]}<br>
                        最高价: ${param.value[4]}<br>
                        最低价: ${param.value[3]}<br>
                        收盘价: ${param.value[2]}
                    `;
                }
            },
            grid: {
                left: '10%',
                right: '10%',
                bottom: '20%',
                top: '10%'
            },
            xAxis: {
                type: 'time',   // 时间轴
                boundaryGap: false,
                axisLine: { onZero: false },
                axisPointer: {
                    label: {
                        formatter: function (params) {
                            return new Date(params.value).toLocaleDateString();
                        }
                    }
                }
            },
            yAxis: {
                scale: true,
                axisLine: { lineStyle: { color: '#999' } }
            },
            series: [
                {
                    name: 'K线图',
                    type: 'candlestick',
                    data: processedData,
                    itemStyle: {
                        color: '#ec0000', // 下跌的颜色
                        borderColor: '#8A0000',
                        borderWidth: 1
                    },
                    emphasis: {
                        itemStyle: {
                            color: '#4fff00', // 上涨的颜色
                            borderColor: '#00ff00',
                            borderWidth: 2
                        }
                    }
                }
            ],
            dataZoom: [
                {
                    type: 'inside', // 内部滑动条，鼠标滚动控制
                    xAxisIndex: [0], // 对应 x 轴
                    start: 90, // 动态起始位置
                    end: 100  // 动态结束位置
                },
                {
                    type: 'slider', // 滑动条形式
                    xAxisIndex: [0], // 对应 x 轴
                    start: 90, // 动态起始位置
                    end: 100,  // 动态结束位置
                    top: '87%',
                }
            ]
        };

        myChart.setOption(option);

        // 窗口大小变化时调整图表尺寸
        window.addEventListener('resize', () => {
            myChart.resize();
        });
    });
};
// #endregion

// #region 将 onBeforeMount 中的函数声明为 async，初始化
onBeforeMount(async () => {
    try {
        isLoading.value = true
        const response = await getStockDailyData(row.value); // 假设 row.value 是请求的参数
        if (response.data != []) {
            dailyData.length = 0; // 清空原数组
            dailyData.push(...response.data); // 展开数组并插入新的元素
        }
        const response1 = await getOneStockInfo(row.value); // 假设 row.value 是请求的参数
        if (response1.data != []) {
            stockInfo.value = response1.data;
        }
        initChart()
    } catch (error) {
        console.error('获取股票数据失败', error);  // 错误处理
    } finally {
        isLoading.value = false
    }
});
// #endregion

</script>

<style scoped>
.demonstration {
    color: var(--el-text-color-secondary);
}

.el-carousel__item:nth-child(2n) {
    background-color: #cedef3;
}

.el-carousel__item:nth-child(2n + 1) {
    background-color: #becdd6;
}
</style>