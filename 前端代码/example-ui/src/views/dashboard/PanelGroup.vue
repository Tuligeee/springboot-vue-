<template>
  <el-row :gutter="16" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper" style="background: rgba(99,161,255,0.1);">
          <svg-icon icon-class="peoples" class-name="card-panel-icon" style="color: #6BA1FF;" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            {{ labels && labels[0] ? labels[0] : '用户总数' }}
          </div>
          <count-to :start-val="0" :end-val="stats.val1 || 0" :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper" style="background: rgba(115,209,142,0.1);">
          <svg-icon icon-class="form" class-name="card-panel-icon" style="color: #73D18E;" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            {{ labels && labels[1] ? labels[1] : '入驻高校' }}
          </div>
          <count-to :start-val="0" :end-val="stats.val2 || 0" :duration="3000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper" style="background: rgba(252,197,100,0.12);">
          <svg-icon icon-class="dict" class-name="card-panel-icon" style="color: #F2BF6C;" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            {{ labels && labels[2] ? labels[2] : '收录专业' }}
          </div>
          <count-to :start-val="0" :end-val="stats.val3 || 0" :duration="3200" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper" style="background: rgba(162,131,222,0.1);">
          <svg-icon icon-class="chart" class-name="card-panel-icon" style="color: #A283DE;" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            {{ labels && labels[3] ? labels[3] : '志愿单数' }}
          </div>
          <count-to :start-val="0" :end-val="stats.val4 || 0" :duration="3600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'

export default {
  components: {
    CountTo
  },
  props: {
    stats: {
      type: Object,
      default: () => ({
        val1: 0,
        val2: 0,
        val3: 0,
        val4: 0
      })
    },
    labels: {
      type: Array,
      default: () => ['用户总数', '入驻高校', '收录专业', '志愿单数']
    }
  },
  methods: {
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-bottom: 20px;

  .card-panel-col {
    margin-bottom: 16px;
  }

  .card-panel {
    display: flex;
    align-items: center;
    height: 100px;
    padding: 20px 24px;
    background: #FFFFFF;
    border-radius: 12px;
    border: 1px solid #F0F0F0;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
    transition: box-shadow 0.25s ease, transform 0.25s ease;

    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
      transform: translateY(-2px);
    }

    .card-panel-icon-wrapper {
      width: 56px;
      height: 56px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 12px;
      margin-right: 20px;
      flex-shrink: 0;
    }

    .card-panel-icon {
      font-size: 28px;
    }

    .card-panel-description {
      display: flex;
      flex-direction: column;

      .card-panel-text {
        font-size: 14px;
        color: #8C8C8C;
        margin-bottom: 8px;
        font-weight: 400;
      }

      .card-panel-num {
        font-size: 28px;
        font-weight: 600;
        color: #1F2733;
        letter-spacing: -0.5px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
    }
  }
}
</style>
