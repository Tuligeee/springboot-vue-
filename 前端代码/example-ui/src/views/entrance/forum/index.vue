<template>
  <div class="app-container">
    <div style="margin-bottom: 20px;">
      <el-button type="primary" icon="el-icon-edit" @click="handleAdd">我要发帖</el-button>
    </div>

    <el-card v-for="post in postList" :key="post.id" style="margin-bottom: 10px; cursor: pointer;" shadow="hover">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; font-size: 16px;" @click="handleDetail(post.id)">{{ post.title }}</span>

        <el-button
            style="float: right; padding: 3px 0; color: #F56C6C; margin-left: 10px;"
            type="text"
            icon="el-icon-delete"
            @click.stop="handleDelete(post)"
        >删除</el-button>

        <el-tag size="mini" type="info" style="float: right;">{{ post.createTime }}</el-tag>
      </div>

      <div style="color: #666;" @click="handleDetail(post.id)">
        {{ post.content ? (post.content.length > 100 ? post.content.substring(0, 100) + '...' : post.content) : '' }}
      </div>

      <div style="margin-top: 10px; font-size: 12px; color: #999;">
        <span><i class="el-icon-user"></i> {{ post.nickName || '匿名用户' }}</span>
        <span style="margin-left: 20px;"><i class="el-icon-view"></i> {{ post.viewCount || 0 }}</span>
      </div>
    </el-card>

    <el-dialog title="发布新帖" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入帖子标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="5" placeholder="请输入内容" />
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
// [注意] 引入 delPost
import { listPost, addPost, delPost } from "@/api/entrance/forum";

export default {
  name: "Forum",
  data() {
    return {
      // 帖子表格数据
      postList: [],
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
        content: [{ required: true, message: "内容不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询帖子列表 */
    getList() {
      listPost().then(response => {
        this.postList = response.data;
      });
    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 表单重置 */
    reset() {
      this.form = {
        title: undefined,
        content: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
    },
    /** 跳转详情页 */
    handleDetail(id) {
      this.$router.push({ path: '/forum/detail/' + id });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      // 【修改点】使用 Element 原生的 $confirm
      this.$confirm('是否确认删除这条帖子？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return delPost(id);
      }).then(() => {
        this.getList();
        this.$message({
          message: '删除成功',
          type: 'success'
        });
      }).catch(() => {});
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addPost(this.form).then(response => {
            // 【修改点1】使用 Element 原生的 $message，防止 $modal 报错
            this.$message({
              message: '发布成功',
              type: 'success'
            });
            // 【修改点2】先关闭弹窗
            this.open = false;
            // 【修改点3】再刷新列表
            this.getList();
          }).catch(err => {
            console.error("发帖失败:", err);
          });
        }
      });
    }
  }
};
</script>

<style scoped>
/* 简单的清除浮动样式 */
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
</style>
