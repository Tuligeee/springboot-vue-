<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; font-size: 18px; color: #303133;">
          <i class="el-icon-edit-outline" style="color: #409EFF; margin-right: 8px;"></i>
          在线模拟志愿填报 (湖北院校专业组模式)
        </span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="$router.back()">返回上一页</el-button>
      </div>

      <div class="apply-content" v-loading="loading">
        <!-- 志愿单切换 -->
        <div class="sheet-selector">
          <span class="label">选择志愿单：</span>
          <el-radio-group v-model="currentSheetNo" @change="handleSheetChange" size="medium">
            <el-radio-button v-for="i in 5" :key="i" :label="i">志愿单 {{i}}</el-radio-button>
          </el-radio-group>
        </div>

        <div class="action-bar">
          <el-button type="primary" icon="el-icon-plus" round @click="addCollege">添加目标院校</el-button>
          <span class="hint">* 您可以为每所院校添加多个意向专业</span>
        </div>

        <el-form class="dynamic-form">
          <div v-for="(group, gIndex) in collegeGroups" :key="gIndex" class="college-group-card">
            <div class="group-header">
              <div class="header-left">
                <span class="index-badge">院校 {{ gIndex + 1 }}</span>
                <el-select 
                  v-model="group.collegeNo" 
                  filterable 
                  placeholder="请搜索并选择院校"
                  style="width: 300px"
                  @change="handleCollegeChange(group)"
                >
                  <el-option
                    v-for="item in collegeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <el-button type="text" icon="el-icon-delete" class="text-danger" @click="removeCollege(gIndex)">移除院校</el-button>
            </div>

            <div class="professions-container">
              <div v-for="(pNo, pIndex) in group.professionNos" :key="pIndex" class="profession-row">
                <span class="p-label">专业 {{ pIndex + 1 }}:</span>
                <el-select 
                  v-model="group.professionNos[pIndex]" 
                  filterable 
                  placeholder="选择该校下属专业"
                  style="flex: 1"
                >
                  <el-option
                    v-for="prof in getProfessionOptions(group.collegeNo)"
                    :key="prof.value"
                    :label="prof.label"
                    :value="prof.value">
                  </el-option>
                </el-select>
                <el-button type="text" icon="el-icon-close" @click="removeProfession(gIndex, pIndex)"></el-button>
              </div>
              <el-button type="dashed" icon="el-icon-plus" size="small" @click="addProfession(gIndex)" class="add-p-btn">增加该校专业</el-button>
            </div>
          </div>

          <el-empty v-if="collegeGroups.length === 0" description="请先点击上方“添加目标院校”开始填报"></el-empty>

          <div class="form-actions" v-if="collegeGroups.length > 0">
            <el-button type="primary" size="large" icon="el-icon-check" @click="submitApply" class="submit-btn">
              确认提交方案 [ 志愿单 {{ currentSheetNo }} ]
            </el-button>
          </div>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
import { selectItem, addForm } from "@/api/entrance/aspiration";

export default {
  name: "AspirationApply",
  data() {
    return {
      loading: false,
      currentSheetNo: 1,
      collegeOptions: [], // [{label, value, children:[]}]
      collegeGroups: [] // [{collegeNo: '', professionNos: ['']}]
    };
  },
  created() {
    this.currentSheetNo = parseInt(this.$route.query.sheetNo || 1);
    this.initData();
  },
  methods: {
    initData() {
      this.loading = true;
      selectItem(this.currentSheetNo).then(res => {
        if (res.code === 0) {
          this.collegeOptions = res.data.items;
          // 处理回显
          if (res.data.groups && res.data.groups.length > 0) {
            this.collegeGroups = res.data.groups;
          } else {
            // 如果没数据，给一个默认院校框
            this.addCollege();
          }
        }
        this.loading = false;
      }).catch(() => { this.loading = false; });
    },
    handleSheetChange() {
      this.$router.push({ query: { ...this.$route.query, sheetNo: this.currentSheetNo } });
      this.initData();
    },
    addCollege() {
      this.collegeGroups.push({ collegeNo: '', professionNos: [''] });
    },
    removeCollege(index) {
      this.collegeGroups.splice(index, 1);
    },
    handleCollegeChange(group) {
      // 切换学校时，重置其下属专业
      group.professionNos = [''];
    },
    addProfession(gIndex) {
      this.collegeGroups[gIndex].professionNos.push('');
    },
    removeProfession(gIndex, pIndex) {
      if (this.collegeGroups[gIndex].professionNos.length > 1) {
        this.collegeGroups[gIndex].professionNos.splice(pIndex, 1);
      } else {
        this.$message.warning("每所院校至少保留一个专业");
      }
    },
    getProfessionOptions(collegeNo) {
      const college = this.collegeOptions.find(c => c.value === collegeNo);
      return college ? college.children : [];
    },
    submitApply() {
      // --- 资深程序员优化：深度查重校验逻辑 ---
      const collegeSet = new Set();
      for (let i = 0; i < this.collegeGroups.length; i++) {
        const g = this.collegeGroups[i];
        
        // 1. 必填校验
        if (!g.collegeNo) { this.$message.error(`院校 ${i + 1} 尚未选择`); return; }
        
        // 2. 院校重复校验
        if (collegeSet.has(g.collegeNo)) {
          this.$message.error("同一份志愿单中不能填报重复的院校，请移除多余项");
          return;
        }
        collegeSet.add(g.collegeNo);

        // 3. 专业重复校验
        const profSet = new Set();
        for (let j = 0; j < g.professionNos.length; j++) {
          const pNo = g.professionNos[j];
          if (!pNo) { this.$message.error(`院校 ${i + 1} 的专业 ${j + 1} 尚未选择`); return; }
          
          if (profSet.has(pNo)) {
            this.$message.error(`在同一所院校下不能填报重复的专业，请核对“院校 ${i+1}”中的选择`);
            return;
          }
          profSet.add(pNo);
        }
      }
      // --- 校验结束 ---

      this.$confirm(`确认提交 [ 志愿单 ${this.currentSheetNo} ] 吗？`, '确认').then(() => {
        this.loading = true;
        addForm({
          sheetNo: this.currentSheetNo,
          collegeGroups: this.collegeGroups
        }).then(res => {
          if (res.code === 0) {
            this.$message.success("志愿方案保存成功！");
            this.$router.go(-1);
          }
          this.loading = false;
        }).catch(() => { this.loading = false; });
      });
    }
  }
};
</script>

<style scoped>
.apply-content { max-width: 900px; margin: 0 auto; }
.sheet-selector { margin-bottom: 25px; text-align: center; }
.action-bar { margin-bottom: 20px; display: flex; align-items: center; justify-content: space-between; padding: 0 10px; }
.hint { font-size: 13px; color: #909399; }
.college-group-card { background: #fff; border: 1px solid #e6ebf1; border-radius: 12px; padding: 20px; margin-bottom: 30px; box-shadow: 0 4px 12px rgba(0,0,0,0.03); }
.group-header { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px dashed #eff2f7; padding-bottom: 15px; margin-bottom: 15px; }
.header-left { display: flex; align-items: center; gap: 15px; }
.index-badge { background: #0974e7; color: white; padding: 2px 12px; border-radius: 4px; font-size: 12px; font-weight: bold; }
.professions-container { padding-left: 20px; }
.profession-row { display: flex; align-items: center; gap: 15px; margin-bottom: 12px; }
.p-label { color: #606266; font-size: 14px; width: 60px; }
.add-p-btn { width: 100%; border-style: dashed; color: #409EFF; }
.form-actions { margin-top: 40px; text-align: center; }
.submit-btn { padding: 15px 80px; font-size: 18px; border-radius: 30px; }
</style>
