<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons')
import resize from './mixins/resize'

const animationDuration = 1200

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '400px'
    },
    chartData: {
      type: Object,
      default: () => ({
        xData: [],
        yData1: [],
        yData2: []
      })
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el)
      this.setOptions(this.chartData)
    },
    setOptions(data) {
      if (!data) return
      
      this.chart.setOption({
        title: {
          text: '平台每日动态趋势监控',
          left: 20,
          top: 15,
          textStyle: { color: '#333333', fontSize: 15, fontWeight: '600' }
        },
        color: ['#6BA1FF', '#73D18E'],
        tooltip: {
          trigger: 'axis',
          backgroundColor: '#fff',
          borderColor: '#F0F0F0',
          borderWidth: 1,
          padding: [12, 16],
          textStyle: { color: '#333333', fontSize: 13 },
          extraCssText: 'box-shadow: 0 2px 8px rgba(0,0,0,0.1); border-radius: 8px;',
          axisPointer: {
            type: 'shadow',
            shadowStyle: {
              color: 'rgba(24,144,255,0.04)'
            }
          }
        },
        legend: {
          right: 20,
          top: 15,
          icon: 'circle',
          itemWidth: 8,
          itemHeight: 8,
          itemGap: 24,
          textStyle: { color: '#8C8C8C', fontSize: 12 },
          data: ['阅读量(近7天)', '收藏量(近7天)']
        },
        grid: {
          top: 65,
          left: 20,
          right: 20,
          bottom: 20,
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: data.xData || ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
          axisLine: { lineStyle: { color: '#E8E8E8' } },
          axisTick: { show: false },
          axisLabel: { color: '#8C8C8C', fontSize: 12 }
        }],
        yAxis: [{
          type: 'value',
          axisLine: { show: false },
          axisTick: { show: false },
          axisLabel: { color: '#8C8C8C', fontSize: 12 },
          splitLine: { lineStyle: { color: '#F5F5F5', type: 'dashed' } }
        }],
        series: [{
          name: '阅读量(近7天)',
          type: 'bar',
          barWidth: '35%',
          barGap: '10%',
          itemStyle: {
            borderRadius: [4, 4, 0, 0]
          },
          data: data.yData1 || [],
          animationDuration
        }, {
          name: '收藏量(近7天)',
          type: 'bar',
          barWidth: '35%',
          itemStyle: {
            borderRadius: [4, 4, 0, 0]
          },
          data: data.yData2 || [],
          animationDuration
        }]
      })
    }
  }
}
</script>
