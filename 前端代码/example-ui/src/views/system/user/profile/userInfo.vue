<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="用户昵称" prop="nickName">
      <el-input v-model="user.nickName" maxlength="30" />
    </el-form-item>
    <el-form-item label="手机号码" prop="mobile">
      <el-input v-model="user.mobile" maxlength="11" />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="user.email" maxlength="50" />
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="user.sex">
        <el-radio label="0">男</el-radio>
        <el-radio label="1">女</el-radio>
      </el-radio-group>
    </el-form-item>

    <div style="font-weight: bold; margin: 15px 0 10px 0; color:#303133; border-left: 4px solid #409EFF; padding-left: 8px;">高考考籍 (必填)</div>
    
    <el-form-item label="毕业年份" required>
      <el-input-number v-model="studentForm.graduateYear" placeholder="请输入毕业年份" :min="2020" :max="2030" style="width: 200px" />
    </el-form-item>
    <el-form-item label="高考成绩" required>
      <el-input-number v-model="studentForm.achievement" placeholder="请输入高考成绩" :min="0" :max="750" style="width: 200px" />
    </el-form-item>
    <el-form-item label="首选科目" required>
      <el-radio-group v-model="studentForm.subjectFirst">
        <el-radio label="物理">物理</el-radio>
        <el-radio label="历史">历史</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="再选科目" required>
      <el-checkbox-group v-model="subjectSecondArray" :max="2">
        <el-checkbox label="思想政治">思想政治</el-checkbox>
        <el-checkbox label="地理">地理</el-checkbox>
        <el-checkbox label="化学">化学</el-checkbox>
        <el-checkbox label="生物">生物</el-checkbox>
      </el-checkbox-group>
      <div style="font-size:12px;color:#F56C6C;line-height:14px;margin-top:5px;">高考新规要求，请严格、必须选择两门再选科目方可保存！</div>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" size="mini" @click="submit" :loading="loading">保存信息与档案</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserProfile } from "@/api/system/user";
import { getMyProfile, updateMyProfile } from "@/api/entrance/student";

export default {
  props: {
    user: {
      type: Object
    }
  },
  data() {
    return {
      // 表单校验
      rules: {
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
        email: [
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        mobile: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      },
      studentForm: {
        graduateYear: new Date().getFullYear(),
        achievement: 500,
        subjectFirst: "",
        subjectSecond: ""
      },
      subjectSecondArray: [],
      loading: false
    };
  },
  watch: {
    subjectSecondArray(val) {
      if (val) {
        this.studentForm.subjectSecond = val.join(',');
      } else {
        this.studentForm.subjectSecond = '';
      }
    }
  },
  created() {
    this.loadStudentProfile();
  },
  methods: {
    loadStudentProfile() {
      getMyProfile().then(res => {
        if (res.data && res.data.id) {
          this.studentForm = res.data;
          if (this.studentForm.subjectSecond) {
            this.subjectSecondArray = this.studentForm.subjectSecond.split(',');
          }
        }
      });
    },
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (!this.studentForm.subjectFirst) {
            this.$message.error("必填错误：请选择首选科目（物理或历史）");
            return;
          }
          if (this.subjectSecondArray.length !== 2) {
            this.$message.error("必填错误：请严格选择满两门再选科目");
            return;
          }
          
          this.loading = true;
          // 同时保存基本信息与高考考籍
          Promise.all([
            updateUserProfile(this.user),
            updateMyProfile(this.studentForm)
          ]).then(() => {
            this.msgSuccess("用户的基本信息与高考考籍已全部修改成功！");
            this.loading = false;
          }).catch(() => {
            this.loading = false;
          });
        }
      });
    },
    close() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/index" });
    }
  }
};
</script>
