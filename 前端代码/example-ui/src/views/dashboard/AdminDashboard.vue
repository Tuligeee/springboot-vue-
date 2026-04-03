<template>
  <div class="dashboard-editor-container">
    <panel-group :stats="{val1: overview.userCount, val2: overview.collegeCount, val3: overview.professionCount, val4: overview.aspirationCount}" />

    <el-row :gutter="16">
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-card">
          <pie-chart :chart-data="chartData.pieData" />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-card">
          <bar-chart :chart-data="chartData.barData" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import PanelGroup from './PanelGroup'
import PieChart from './PieChart'
import BarChart from './BarChart'
import { getStatisticsOverview, getStatisticsChart } from '@/api/entrance/statistics'

export default {
  name: 'AdminDashboard',
  components: {
    PanelGroup,
    PieChart,
    BarChart
  },
  data() {
    return {
      overview: {
        userCount: 0,
        collegeCount: 0,
        professionCount: 0,
        aspirationCount: 0
      },
      chartData: {
        pieData: [],
        barData: {
          xData: [],
          yData1: [],
          yData2: []
        }
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      getStatisticsOverview().then(res => {
        if (res.data) {
          this.overview = res.data;
        }
      });
      getStatisticsChart().then(res => {
        if (res.data) {
          this.chartData = res.data;
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 20px;
  background-color: #F0F2F6;
  min-height: calc(100vh - 84px);

  .chart-card {
    background: #FFFFFF;
    padding: 16px;
    margin-bottom: 16px;
    border-radius: 12px;
    border: 1px solid #F0F0F0;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
    transition: box-shadow 0.25s ease, transform 0.25s ease;

    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
      transform: translateY(-2px);
    }
  }
}
</style>
