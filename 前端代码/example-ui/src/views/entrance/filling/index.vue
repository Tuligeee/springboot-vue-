<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; font-size: 18px; color: #303133;">
          <i class="el-icon-document-copy" style="color: #409EFF; margin-right: 8px;"></i>
          填报中心 - 我的模拟志愿表 (共5个方案)
        </span>
      </div>

      <div v-loading="loading" class="sheets-list">
        <el-row :gutter="20">
          <el-col :span="24" v-for="sheet in sheetList" :key="sheet.sheetNo" style="margin-bottom: 25px;">
            <el-card shadow="never" :class="['sheet-item-card', sheet.hasData ? 'has-data' : 'no-data']">
              <div class="sheet-header">
                <div class="header-left">
                  <span class="sheet-no">方案单 {{ sheet.sheetNo }}</span>
                  <el-tag v-if="sheet.hasData" size="mini" type="success" effect="dark" style="margin-left: 10px;">已填报</el-tag>
                </div>
                <div class="sheet-actions">
                  <el-button 
                    size="mini" 
                    :type="sheet.hasData ? 'primary' : 'success'" 
                    icon="el-icon-edit"
                    @click="$router.push({path: '/filling-view/apply', query: {sheetNo: sheet.sheetNo}})"
                  >
                    {{ sheet.hasData ? '修改方案' : '立即填报' }}
                  </el-button>
                  <el-button 
                    v-if="sheet.hasData"
                    size="mini" 
                    type="warning" 
                    plain
                    icon="el-icon-download"
                    @click="handleExport(sheet.sheetNo)"
                    style="margin-left: 10px;"
                  >导出志愿单</el-button>
                  <el-button 
                    v-if="sheet.hasData"
                    size="mini" 
                    type="danger" 
                    plain
                    icon="el-icon-delete"
                    @click="handleRemoveSheet(sheet.sheetNo)"
                    style="margin-left: 10px;"
                  >清空方案</el-button>
                </div>
              </div>
              
              <div class="sheet-body">
                <template v-if="sheet.hasData">
                  <div class="college-group-summary">
                    <div v-for="(group, idx) in sheet.details" :key="idx" class="summary-item">
                      <div class="college-name-row">
                        <i class="el-icon-school"></i> {{ group.collegeName }}
                      </div>
                      <div class="professions-list">
                        <el-tag 
                          v-for="(pName, pIdx) in group.professions" 
                          :key="pIdx" 
                          size="mini" 
                          type="info" 
                          style="margin-right: 8px; margin-bottom: 5px;">
                          {{ pName }}
                        </el-tag>
                      </div>
                    </div>
                  </div>
                </template>
                <div v-else class="empty-text">
                  <i class="el-icon-warning-outline"></i> 暂未录入任何院校专业组合，点击右侧按钮开启模拟填报
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import { listSheets, delSheet } from "@/api/entrance/aspiration";
import request from '@/utils/request';

export default {
  name: "MyVolunteer",
  data() {
    return {
      loading: true,
      sheetList: []
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 导出志愿单 */
    handleExport(sheetNo) {
      this.loading = true;
      request({
        url: '/college_entrance/aspiration/export/' + sheetNo,
        method: 'get'
      }).then(res => {
        if (res.msg) {
          this.executeDownload(res.msg);
        } else {
          this.$message.error("导出失败：未返回文件名");
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    /** 执行真实下载 */
    executeDownload(fileName) {
      window.location.href = process.env.VUE_APP_BASE_API + "/common/download?fileName=" + encodeURIComponent(fileName) + "&delete=true";
    },
    handleRemoveSheet(sheetNo) {
      this.$confirm(`确定要彻底清空“方案 ${sheetNo}”的所有内容吗？`, '警告', {
        type: 'warning'
      }).then(() => {
        this.loading = true;
        return delSheet(sheetNo);
      }).then(() => {
        this.$message.success(`方案 ${sheetNo} 已成功重置`);
        this.getList();
      }).catch(() => {
        this.loading = false;
      });
    },
    getList() {
      this.loading = true;
      listSheets().then(res => {
        if (res.code === 0) {
          this.sheetList = res.data;
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    }
  }
};
</script>

<style scoped>
.sheet-item-card { border-radius: 12px; transition: all 0.3s; }
.sheet-item-card.has-data { background: #fcfdfe; border-left: 6px solid #0974e7; }
.sheet-item-card.no-data { background: #fafafa; border-left: 6px solid #e6ebf1; opacity: 0.7; }
.sheet-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; padding-bottom: 12px; border-bottom: 1px solid #eff2f7; }
.sheet-no { font-weight: bold; font-size: 17px; color: #1f2d3d; }
.college-group-summary { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 15px; }
.summary-item { background: #fff; border: 1px solid #f0f2f5; border-radius: 8px; padding: 12px; }
.college-name-row { font-weight: bold; color: #333; margin-bottom: 10px; font-size: 14px; }
.professions-list { display: flex; flex-wrap: wrap; }
.empty-text { color: #adb5bd; font-size: 14px; padding: 20px 0; font-style: italic; }
</style>
