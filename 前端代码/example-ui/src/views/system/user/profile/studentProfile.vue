<template>
  <el-form ref="form" :model="form" :rules="rules" label-width="100px" style="margin-top: 20px;">
    <el-form-item label="毕业年份" prop="graduateYear">
      <el-input-number v-model="form.graduateYear" placeholder="请输入毕业年份" :min="2020" :max="2030" style="width: 200px" />
    </el-form-item>
    <el-form-item label="高考成绩" prop="achievement">
      <el-input-number v-model="form.achievement" placeholder="请输入高考成绩" :min="0" :max="750" style="width: 200px" />
    </el-form-item>
    <el-form-item label="首选科目" prop="subjectFirst">
      <el-radio-group v-model="form.subjectFirst">
        <el-radio label="物理">物理</el-radio>
        <el-radio label="历史">历史</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="再选科目" prop="subjectSecond">
      <el-checkbox-group v-model="subjectSecondArray" :max="2">
        <el-checkbox label="思想政治">思想政治</el-checkbox>
        <el-checkbox label="地理">地理</el-checkbox>
        <el-checkbox label="化学">化学</el-checkbox>
        <el-checkbox label="生物">生物</el-checkbox>
      </el-checkbox-group>
      <div style="font-size:12px;color:#999;line-height:14px;margin-top:5px;">请严格选择两门再选科目</div>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit" :loading="loading">保存档案</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { getMyProfile, updateMyProfile } from "@/api/entrance/student";

export default {
  data() {
    return {
      form: {
        graduateYear: new Date().getFullYear(),
        achievement: 500,
        subjectFirst: "",
        subjectSecond: ""
      },
      subjectSecondArray: [],
      loading: false,
      rules: {
        achievement: [
          { required: true, message: "请输入高考成绩", trigger: "blur" }
        ],
        subjectFirst: [
          { required: true, message: "请选择首选科目", trigger: "change" }
        ]
      }
    };
  },
  watch: {
    subjectSecondArray(val) {
      if (val) {
        this.form.subjectSecond = val.join(',');
      } else {
        this.form.subjectSecond = '';
      }
    }
  },
  created() {
    this.loadProfile();
  },
  methods: {
    loadProfile() {
      getMyProfile().then(res => {
        if (res.data && res.data.id) {
          this.form = res.data;
          if (this.form.subjectSecond) {
            this.subjectSecondArray = this.form.subjectSecond.split(',');
          }
        }
      });
    },
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.subjectSecondArray.length !== 2) {
            this.$message.error("请严格选择满两门再选科目");
            return;
          }
          this.loading = true;
          updateMyProfile(this.form).then(response => {
            this.$message.success("高考档案修改成功");
            this.loading = false;
          }).catch(() => {
            this.loading = false;
          });
        }
      });
    }
  }
};
</script>
