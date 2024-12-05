<template>
  <div>
    <!-- 图表容器 -->
    <div ref="chartRef" style="width: 100%; height: 400px;"></div>
  </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount, ref, watch, nextTick } from 'vue';
import * as echarts from 'echarts';

// 定义传入的 props 数据
const props = defineProps({
  avgClose: {
    type: Number,
    required: true
  },
  maxClose: {
    type: Number,
    required: true
  },
  minClose: {
    type: Number,
    required: true
  },
  echartConfig: {
    type: Object,
    required: true
  }
});

const chartRef = ref(null);
let myChart = null;

// 初始化图表的函数
const initChart = () => {
  if (!chartRef.value) return; // 确保 chartRef 存在

  myChart = echarts.init(chartRef.value);

  const option = {
    title: {
      text: props.echartConfig.title.text,
      left: 'center',
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
    },
    grid:{
      left: '25%',
    },
    xAxis: {
      type: 'category',
      data: props.echartConfig.xAxis.data, // X 轴显示的数据项
      axisLabel: {
        rotate: 30, // 可以适当旋转标签以避免重叠
      },
    },
    yAxis: {
      type: 'value',
    },
    series: [
      {
        name: props.echartConfig.series.name,
        type: 'bar',
        data: [props.avgClose, props.maxClose, props.minClose], // Y 轴数据对应的三个值
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          },
        },
      },
    ],
  };

  // 使用配置项填充图表
  myChart.setOption(option);

  window.addEventListener('resize', handleResize);
};

const handleResize = () => {
  if (myChart) {
    myChart.resize();
  }
};

// 监听 props 更新，更新图表配置
watch(
  () => [props.avgClose, props.maxClose, props.minClose],
  () => {
    if (myChart) {
      const option = myChart.getOption();
      option.series[0].data = [props.avgClose, props.maxClose, props.minClose];
      myChart.setOption(option);
    }
  },
  { immediate: true } // 初次渲染时调用
);

// 在组件挂载时初始化图表
onMounted(() => {
  nextTick(() => {
    initChart(); // 等待 DOM 完成渲染后再初始化图表
  });
});

// 在组件卸载时销毁图表实例
onBeforeUnmount(() => {
  if (myChart) {
    myChart.dispose();
  }
});
</script>
