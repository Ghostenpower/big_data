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
    closeAboveAvg: {
      type: Number,
      required: true
    },
    closeBelowAvg: {
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
    if (!chartRef.value) return;  // 确保容器存在
  
    myChart = echarts.init(chartRef.value);
  
    const option = {
      title: {
        text: props.echartConfig.title.text,
        left: 'center',
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)',
      },
      legend: {
        // orient: 'vertical',
        left: 'center',
        data: props.echartConfig.legend.data,
        top: '20%'
      },
      series: [
        {
          name: props.echartConfig.series.name,
          type: 'pie',
          radius: '50%',
          center: ['50%', '60%'],
          data: [
            { value: props.closeAboveAvg, name: props.echartConfig.series.data[0] },
            { value: props.closeBelowAvg, name: props.echartConfig.series.data[1] },
          ],
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
  
    // 监听窗口尺寸变化并调整图表大小
    window.addEventListener('resize', handleResize);
  };
  
  // 处理图表调整大小
  const handleResize = () => {
    if (myChart) {
      myChart.resize();
    }
  };
  
  // 监听 props 更新，更新图表配置
  watch(
    () => [props.closeAboveAvg, props.closeBelowAvg],
    () => {
      if (myChart) {
        const option = myChart.getOption();
        option.series[0].data = [
          { value: props.closeAboveAvg, name: '高于平均值天数' },
          { value: props.closeBelowAvg, name: '低于平均值天数' },
        ];
        myChart.setOption(option);
        myChart.resize();  // 在更新数据时调整图表大小
      }
    },
    { immediate: true } // 初次渲染时调用
  );
  
  // 在组件挂载时初始化图表
  onMounted(() => {
    nextTick(() => {
      initChart();
    });
  });
  
  // 在组件卸载时销毁图表实例
  onBeforeUnmount(() => {
    if (myChart) {
      myChart.dispose();
      window.removeEventListener('resize', handleResize);  // 清理事件监听器
    }
  });
  </script>
  