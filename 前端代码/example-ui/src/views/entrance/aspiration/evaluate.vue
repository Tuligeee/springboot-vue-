<template>
  <div class="app-container">
    <el-row :gutter="20" class="mb8">
      <el-col :span="1.5">
        <el-input v-model="evaluateParam.studentNo" style="width: 240px" placeholder="请输入学生学号"/>
        <el-checkbox-button v-model="evaluateParam.byScore">按分数</el-checkbox-button>
        <el-checkbox-button v-model="evaluateParam.byTag">按标签</el-checkbox-button>
      </el-col>
    </el-row>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="info" size="mini" icon="el-icon-search" @click="handlerEvaluate">点击测评</el-button>
      </el-col>
    </el-row>
    <el-row :gutter="25">
      <el-col :span="1.5">
        <el-card v-show="this.evaluateResultShow" v-loading=evaluateLoading class="box-card">
          <div slot="header" class="clearfix">
            <span>测评结果</span>
          </div>
          <div v-for="result in evaluateResult" :key="result" class="text item">
            {{ result }}
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import {evaluate} from "@/api/entrance/aspiration";

export default {
  name: "Evaluate",
  data() {
    return {
      // 测评参数
      evaluateParam: {
        studentNo: '',
        byScore: true,
        byTag: false
      },
      evaluateLoading: false,
      // 测评结果
      evaluateResult: [],
      evaluateResultShow:false
    };
  },
  methods: {
    /** 专业测评 */
    handlerEvaluate() {
      this.evaluateResultShow = true;
      this.evaluateLoading = true;
      evaluate(this.evaluateParam).then(
          response => {
            this.evaluateResult = response.data.results;
            this.evaluateLoading = false;
          }
      );
    }
  }
};
</script>

<style>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}

.box-card {
  width: 1000px;
  height: 500px;
}
</style>
