<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资讯标题" prop="title">
        <el-input
            v-model="queryParams.title"
            placeholder="请输入资讯标题"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="资讯类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable size="small">
          <el-option label="高考政策" value="1" />
          <el-option label="报考指南" value="2" />
          <el-option label="院校动态" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="newsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="封面" align="center" width="100">
        <template slot-scope="scope">
          <el-image
              v-if="scope.row.coverImg"
              style="width: 50px; height: 50px"
              :src="scope.row.coverImg"
              :preview-src-list="[scope.row.coverImg]">
          </el-image>
          <span v-else>无</span>
        </template>
      </el-table-column>

      <el-table-column label="标题" align="center" prop="title" :show-overflow-tooltip="true" />

      <el-table-column label="类型" align="center" prop="type" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type == '1'" type="danger">高考政策</el-tag>
          <el-tag v-else-if="scope.row.type == '2'" type="primary">报考指南</el-tag>
          <el-tag v-else-if="scope.row.type == '3'" type="success">院校动态</el-tag>
          <span v-else>其他</span>
        </template>
      </el-table-column>

      <el-table-column label="阅读量" align="center" prop="viewCount" width="80" />
      <el-table-column label="发布时间" align="center" prop="createTime" width="160" />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              class="text-danger"
              @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="16">
            <el-form-item label="资讯标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入资讯标题" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="资讯类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择">
                <el-option label="高考政策" value="1" />
                <el-option label="报考指南" value="2" />
                <el-option label="院校动态" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="封面图片" prop="coverImg">
          <image-upload v-model="form.coverImg" :limit="1"/>
        </el-form-item>

        <el-form-item label="摘要" prop="summary">
          <el-input v-model="form.summary" type="textarea" placeholder="请输入文章摘要（展示在列表页）" />
        </el-form-item>

        <el-form-item label="正文内容" prop="content">
          <editor v-model="form.content" :min-height="300"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listNews, getNews, delNews, addNews, updateNews } from "@/api/entrance/news";
import Editor from '@/components/Editor';
import ImageUpload from '@/components/ImageUpload';

export default {
  name: "News",
  components: { Editor, ImageUpload },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      newsList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        type: null
      },
      form: {},
      rules: {
        title: [
          { required: true, message: "资讯标题不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "请选择资讯类型", trigger: "change" }
        ],
        content: [
          { required: true, message: "正文内容不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listNews(this.queryParams).then(response => {
        this.newsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: null,
        title: null,
        coverImg: null,
        summary: null,
        content: null,
        type: "1",
        status: "0"
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "发布新资讯";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getNews(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改资讯";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateNews(this.form).then(response => {
              // [修复] 使用原生 $message
              this.$message.success("修改成功");
              // [修复] 手动关闭弹窗
              this.open = false;
              // [修复] 刷新列表
              this.getList();
            });
          } else {
            addNews(this.form).then(response => {
              // [修复] 使用原生 $message
              this.$message.success("发布成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      // [优化] 如果是单条删除，显示文章标题；如果是批量删除，显示ID或条数
      let alertText = '';
      if (row && row.title) {
        alertText = '是否确认删除资讯文章："' + row.title + '"？';
      } else {
        alertText = '是否确认删除所选的 ' + ids.length + ' 条资讯文章？';
      }
      this.$confirm(alertText, '系统提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delNews(ids);
      }).then(() => {
        this.getList();
        this.$message.success("删除成功");
      }).catch(() => {});
    },
  }
};
</script>
