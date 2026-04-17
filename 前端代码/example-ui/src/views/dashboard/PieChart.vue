<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons')
import resize from './mixins/resize'

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
      type: Array,
      default: () => []
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
      if (!data || data.length === 0) return

      var total = data.reduce(function(sum, item) { return sum + (Number(item.value) || 0) }, 0)

      this.chart.setOption({
        title: {
          text: '热门专业填报分布',
          left: 20,
          top: 15,
          textStyle: { color: '#333333', fontSize: 15, fontWeight: '600' }
        },
        color: ['#6BA1FF', '#73D18E', '#F2BF6C', '#F09898', '#A283DE', '#7CD5D5'],
        tooltip: {
          trigger: 'item',
          backgroundColor: '#fff',
          borderColor: '#F0F0F0',
          borderWidth: 1,
          padding: [12, 16],
          textStyle: { color: '#333333', fontSize: 13 },
          extraCssText: 'box-shadow: 0 2px 8px rgba(0,0,0,0.1); border-radius: 8px;',
          formatter: function(params) {
            return '<div style="font-weight:600;margin-bottom:4px;">' + params.name + '</div>' +
              '<div style="color:#8C8C8C;">填报数：<span style="color:#333;font-weight:500;">' + params.value + '</span></div>' +
              '<div style="color:#8C8C8C;">占比：<span style="color:#1890FF;font-weight:500;">' + params.percent + '%</span></div>'
          }
        },
        legend: {
          orient: 'vertical',
          right: '6%',
          top: 'middle',
          icon: 'circle',
          itemWidth: 8,
          itemHeight: 8,
          itemGap: 20,
          formatter: function(name) {
            var item = data.find(function(d) { return d.name === name })
            var val = item ? item.value : 0
            var pct = total > 0 ? ((val / total) * 100).toFixed(1) : '0.0'
            return '{name|' + name + '}{pct|' + pct + '%}'
          },
          textStyle: {
            rich: {
              name: {
                color: '#333333',
                fontSize: 13,
                fontWeight: '400',
                width: 120,
                padding: [0, 0, 0, 4]
              },
              pct: {
                color: '#8C8C8C',
                fontSize: 13,
                fontWeight: '400',
                align: 'right'
              }
            }
          }
        },
        series: [
          {
            name: '填报分布',
            type: 'pie',
            radius: ['44%', '66%'],
            center: ['35%', '55%'],
            label: {
              show: false
            },
            labelLine: {
              show: false
            },
            emphasis: {
              scale: true,
              scaleSize: 6,
              itemStyle: {
                shadowBlur: 12,
                shadowColor: 'rgba(0, 0, 0, 0.12)'
              }
            },
            data: data,
            animationEasing: 'cubicInOut',
            animationDuration: 1200
          }
        ]
      })
    }
  }
}
</script>
