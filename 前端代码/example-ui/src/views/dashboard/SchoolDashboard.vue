<template>
  <div class="dashboard-editor-container">
    <panel-group :stats="{val1: overview.schoolStudentCount, val2: overview.schoolProfessionCount, val3: overview.schoolScoreCount, val4: overview.schoolAspirationCount}" :labels="['关注我校人数', '本校专业数', '历年分数记录', '我校意向热度']" />

    <!-- 快捷操作栏 -->
    <el-row :gutter="16" style="margin-bottom: 16px;">
      <el-col :span="24">
        <el-card shadow="hover" class="quick-actions-card">
          <div class="quick-actions">
            <span style="font-weight: bold; font-size: 16px; color: #303133; margin-right: 20px;">
              <i class="el-icon-s-operation" style="color: #409EFF;"></i> 快捷操作
            </span>
            <el-button type="primary" plain icon="el-icon-school" @click="$router.push('/school/profile')">维护本校资料</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

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
  name: 'SchoolDashboard',
  components: {
    PanelGroup,
    PieChart,
    BarChart
  },
  data() {
    return {
      overview: {
        schoolStudentCount: 0,
        schoolProfessionCount: 0,
        schoolScoreCount: 0,
        schoolAspirationCount: 0
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
