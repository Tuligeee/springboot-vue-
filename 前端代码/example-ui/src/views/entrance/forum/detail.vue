<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; font-size: 18px; color: #303133;">
          <i class="el-icon-chat-line-square" style="color: #409EFF; margin-right: 8px;"></i>
          帖子详情
        </span>
      </div>
    <el-button size="mini" icon="el-icon-back" @click="$router.go(-1)" style="margin-bottom:10px;">返回列表</el-button>

    <el-card>
      <div slot="header" class="clearfix">
        <span style="font-size: 18px; font-weight: bold;">{{ post.title }}</span>
        <div style="float: right; font-size: 12px; color: #888;">
          <i class="el-icon-user"></i> {{ post.nickName }} &nbsp;|&nbsp;
          <i class="el-icon-time"></i> {{ post.createTime }} &nbsp;|&nbsp;
          <i class="el-icon-view"></i> {{ post.viewCount }}
          
          <el-button v-if="currentUserId === post.userId || checkRole(['admin'])" type="text" size="mini" icon="el-icon-edit" style="margin-left: 15px;" @click="openEditDialog">修改</el-button>
          <el-button v-if="currentUserId === post.userId || checkRole(['admin'])" type="text" size="mini" icon="el-icon-delete" style="color: #F56C6C; margin-left: 5px;" @click="handleDeletePost">删除</el-button>
        </div>
      </div>
      <div style="padding: 20px 0; font-size: 16px; line-height: 1.8; min-height: 100px;">
        {{ post.content }}
      </div>
    </el-card>

    <div style="margin-top: 20px;">
      <div style="margin-bottom: 15px; font-weight: bold; border-left: 4px solid #409EFF; padding-left: 10px;">
        全部评论 ({{ commentList.length }})
      </div>

      <el-card v-for="item in commentList" :key="item.id" style="margin-bottom: 10px;" shadow="never">
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <div>
            <span style="font-weight: bold; color: #606266; margin-right: 10px;">{{ item.nickName || '匿名' }}</span>
            <span style="color: #999; font-size: 12px;">{{ item.createTime }}</span>
          </div>

          <el-button
              v-if="currentUserId === item.userId || currentUserId === post.userId || checkRole(['admin'])"
              type="text"
              style="color: #F56C6C; padding: 0;"
              icon="el-icon-delete"
              @click="handleDeleteComment(item.id)"
          >删除</el-button>
        </div>
        <div style="margin-top: 10px; color: #333;">
          {{ item.content }}
        </div>
      </el-card>

      <el-empty v-if="commentList.length === 0" description="暂无评论，快来抢沙发吧！"></el-empty>
    </div>

    <div style="margin-top: 30px; background: #f8f8f8; padding: 20px; border-radius: 4px;">
      <h4>发表评论</h4>
      <el-input
          type="textarea"
          :rows="4"
          placeholder="写下你的看法，理性发言..."
          v-model="commentContent"
          maxlength="500"
          show-word-limit
      ></el-input>
      <div style="text-align: right; margin-top: 10px;">
        <el-button type="primary" @click="submitComment" icon="el-icon-s-promotion">发送评论</el-button>
      </div>
    </div>

    <el-dialog title="修改帖子" :visible.sync="editDialogVisible" width="500px" append-to-body>
      <el-form ref="editForm" :model="editForm" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="editForm.title" placeholder="请输入帖子标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="editForm.content" type="textarea" :rows="5" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEdit">确 定</el-button>
        <el-button @click="editDialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>

  </el-card>
  </div>
</template>

<script>
import { getInfo } from "@/api/login";
import { checkRole } from "@/utils/permission";
import { getPost, listComment, addComment, delComment, delPost, updatePost } from "@/api/entrance/forum";

export default {
  name: "ForumDetail",
  data() {
    return {
      currentUserId: null,
      postId: null,
      post: {},
      commentList: [],
      commentContent: "",
      editDialogVisible: false,
      editForm: { title: "", content: "" }
    };
  },
  created() {
    this.postId = this.$route.params.postId;
    if (this.postId) {
      this.getDetail();
      this.getComments();
    }
    getInfo().then(res => {
      const user = res.user || (res.data && res.data.user) || {};
      this.currentUserId = user.userId;
    });
  },
  methods: {
    checkRole,
    /** 获取帖子详情 */
    getDetail() {
      getPost(this.postId).then(res => {
        this.post = res.data;
      });
    },
    /** 获取评论列表 */
    getComments() {
      listComment(this.postId).then(res => {
        this.commentList = res.data;
      });
    },
    /** [新增] 删除评论 */
    handleDeleteComment(id) {
      this.$confirm('确认删除这条评论吗？', '提示', {
        type: 'warning'
      }).then(() => {
        return delComment(id);
      }).then(() => {
        this.$message.success("删除成功");
        this.getComments(); // 刷新列表
      }).catch(err => {
        // 取消或报错不做处理，request.js 通常会统一处理错误提示
      });
    },
    /** [修复] 提交评论 */
    submitComment() {
      if (!this.commentContent.trim()) {
        this.$message.warning("评论内容不能为空");
        return;
      }

      const data = {
        postId: this.postId,
        content: this.commentContent
      };

      addComment(data).then(() => {
        // [修复重点] 使用 this.$message 而不是 this.$modal
        this.$message.success("评论成功");

        // 清空输入框
        this.commentContent = "";

        // [关键] 立即刷新列表
        this.getComments();
      }).catch(err => {
        console.error("评论失败", err);
      });
    },
    /** 打开编辑弹窗 */
    openEditDialog() {
      this.editForm = {
        id: this.post.id,
        title: this.post.title,
        content: this.post.content
      };
      this.editDialogVisible = true;
    },
    /** 提交编辑 */
    submitEdit() {
      if (!this.editForm.title || !this.editForm.content) {
        this.$message.warning("标题和内容不能为空");
        return;
      }
      updatePost(this.editForm).then(() => {
        this.$message.success("修改成功");
        this.editDialogVisible = false;
        this.getDetail(); // 刷新详情
      });
    },
    /** 删除帖子 */
    handleDeletePost() {
      this.$confirm('确认删除这篇帖子吗？', '提示', {
        type: 'warning'
      }).then(() => {
        return delPost(this.postId);
      }).then(() => {
        this.$message.success("删除成功");
        // 返回上一页或列表页
        if (this.$route.path.startsWith('/forum-view')) {
           this.$router.push('/forum-view/list');
        } else {
           this.$router.push('/entrance/forum/list');
        }
      });
    }
  }
};
</script>

<style scoped>
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
</style>
