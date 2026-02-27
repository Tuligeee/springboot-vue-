<template>
  <div class="app-container">
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
        professionNo1: "",
        professionNo2: "",
        professionNo3: "",
      }
    };
  },
  methods: {
    /** 值变化*/
    handleChange1(value) {
      this.selectItem.professionNo1 = value[1]
    },
    handleChange2(value) {
      this.selectItem.professionNo2 = value[1]
    },
    handleChange3(value) {
      this.selectItem.professionNo3 = value[1]
    },
    /** 志愿填报选项 */
    selectItems() {
      selectItem().then(
          response => {
            this.options1 = response.data.items;
            this.selectItem.professionNo1 = response.data.professionNo1;
            this.options2 = response.data.items;
            this.selectItem.professionNo2 = response.data.professionNo2;
            this.options3 = response.data.items;
            this.selectItem.professionNo3 = response.data.professionNo3;
          }
      );
    },
    /** 确定 */
    handelConfirm() {
      addForm(this.selectItem).then(
          response => {
            if(response.data == true){
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
