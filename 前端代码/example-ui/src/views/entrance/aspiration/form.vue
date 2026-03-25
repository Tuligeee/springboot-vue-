<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; font-size: 18px; color: #303133;">
          <i class="el-icon-edit-outline" style="color: #409EFF; margin-right: 8px;"></i>
          志愿填报
        </span>
      </div>
    <el-row :gutter="5" class="mb8">
      <div class="block">
        <el-card  class="" header="第一志愿">
          <el-cascader
              placeholder="志愿一"
              :options="options1"
              :value = "selectItem.professionNo1"
              filterable
              clearable
              @change="handleChange1"
          ></el-cascader>
        </el-card>
      </div>
    </el-row>
    <el-row :gutter="5" class="mb8">
      <el-card class="" header="第二志愿">
        <el-cascader
            placeholder="志愿二"
            :options="options2"
            :value = "selectItem.professionNo2"
            filterable
            clearable
            @change="handleChange2"
        ></el-cascader>
      </el-card>
    </el-row>
    <el-row :gutter="5" class="mb8">
      <el-card class="" header="第三志愿">
        <el-cascader
            placeholder="志愿三"
            :options="options3"
            :value = "selectItem.professionNo3"
            filterable
            clearable
            @change="handleChange3"
        ></el-cascader>
      </el-card>
    </el-row>

    <el-row :gutter="20">
      <el-button type="primary" @click="handelConfirm">完成填报</el-button>
    </el-row>

    <el-dialog
        title="志愿填报"
        :visible.sync="dialogVisible"
        width="30%">
      <span>填报完成</span>
    </el-dialog>

      </el-card>
  </div>
</template>
<script>

import {selectItem, addForm} from "@/api/entrance/aspiration";

export default {
  created() {
    this.selectItems();
  },
  data() {
    return {
      dialogVisible: false,
      options1: [],
      options2: [],
      options3: [],
      selectItem: {
        professionNo1: [],
        professionNo2: [],
        professionNo3: [],
      }
    };
  },
  methods: {
    /** 值变化*/
    handleChange1(value) {
      this.selectItem.professionNo1 = value
    },
    handleChange2(value) {
      this.selectItem.professionNo2 = value
    },
    handleChange3(value) {
      this.selectItem.professionNo3 = value
    },
    /** 志愿填报选项 */
    selectItems() {
      const defaultCollegeNo = this.$route.query.collegeNo;
      selectItem().then(
          response => {
            this.options1 = response.data.items;
            this.options2 = response.data.items;
            this.options3 = response.data.items;
            
            // 处理回显，级联选择器需要 [父级ID, 子级ID]
            this.selectItem.professionNo1 = this.findPath(this.options1, response.data.professionNo1);
            this.selectItem.professionNo2 = this.findPath(this.options2, response.data.professionNo2);
            this.selectItem.professionNo3 = this.findPath(this.options3, response.data.professionNo3);

            // 如果从院校中心跳转过来，且第一个志愿为空，则默认选中该学校
            if (defaultCollegeNo && (!this.selectItem.professionNo1 || this.selectItem.professionNo1.length === 0)) {
               this.selectItem.professionNo1 = [defaultCollegeNo];
            }
          }
      );
    },
    /** 根据专业编号查找级联路径 [collegeNo, professionNo] */
    findPath(options, professionNo) {
      if (!professionNo) return [];
      for (const college of options) {
        if (college.children) {
          const profession = college.children.find(p => p.value === professionNo);
          if (profession) {
            return [college.value, profession.value];
          }
        }
      }
      return [];
    },
    /** 确定 */
    handelConfirm() {
      // 提交时只取专业编号 (数组最后一位)
      const data = {
        professionNo1: this.selectItem.professionNo1 ? this.selectItem.professionNo1[this.selectItem.professionNo1.length - 1] : "",
        professionNo2: this.selectItem.professionNo2 ? this.selectItem.professionNo2[this.selectItem.professionNo2.length - 1] : "",
        professionNo3: this.selectItem.professionNo3 ? this.selectItem.professionNo3[this.selectItem.professionNo3.length - 1] : "",
      }
      addForm(data).then(
          response => {
            if(response.data == true){
              this.$message.success("填报完成");
              this.dialogVisible = true;
            }
          }
      );
    }
  }
};
</script>
<style>
</style>
