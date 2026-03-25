<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; font-size: 18px; color: #303133;">
          <i class="el-icon-document" style="color: #409EFF; margin-right: 8px;"></i>
          资讯详情
        </span>
      </div>
    <el-card v-loading="loading">
      <div slot="header" class="clearfix">
        <el-page-header @back="goBack" :content="news.title || '资讯详情'"></el-page-header>
      </div>

      <div v-if="news.id">
        <div style="text-align: center; margin-bottom: 30px;">
          <h1 style="font-size: 24px; color: #303133;">{{ news.title }}</h1>
          <div style="color: #909399; font-size: 13px; margin-top: 10px;">
            <span style="margin-right: 20px;"><i class="el-icon-user"></i> 发布者：{{ news.createBy || '管理员' }}</span>
            <span style="margin-right: 20px;"><i class="el-icon-time"></i> 时间：{{ news.createTime }}</span>
            <span style="margin-right: 20px;"><i class="el-icon-view"></i> 阅读：{{ news.viewCount }}</span>
          </div>
          <div style="margin-top: 20px;">
            <el-button 
              :type="isLiked ? 'primary' : 'default'" 
              icon="el-icon-thumb" 
              circle 
              @click="handleLike"
              :disabled="isLiked"
            ></el-button>
            <span style="margin-left: 5px; margin-right: 20px; color: #909399;">{{ news.likeCount || 0 }}</span>

            <el-button 
              :type="isCollected ? 'warning' : 'default'" 
              icon="el-icon-star-off" 
              circle 
              @click="handleCollect"
            ></el-button>
            <span style="margin-left: 5px; color: #909399;">{{ isCollected ? '已收藏' : '收藏' }}</span>
          </div>
        </div>
        <div class="news-content" v-html="news.content"></div>

        <!-- 评论区 -->
        <div style="margin-top: 50px; border-top: 1px solid #EBEEF5; padding-top: 30px;">
          <h3 style="margin-bottom: 20px;">发表评论</h3>
          <el-input
            type="textarea"
            :rows="3"
            placeholder="请输入您的评论内容..."
            v-model="commentForm.content"
          ></el-input>
          <div style="text-align: right; margin-top: 10px;">
            <el-button type="primary" size="small" @click="submitComment">发表评论</el-button>
          </div>

          <h3 style="margin-top: 40px; margin-bottom: 20px;">全部评论 ({{ commentList.length }})</h3>
          <div v-if="commentList.length > 0">
            <div v-for="item in commentList" :key="item.id" style="display: flex; margin-bottom: 20px; border-bottom: 1px solid #F2F6FC; padding-bottom: 15px;">
              <el-avatar :size="40" :src="item.avatar" style="margin-right: 15px;">
                <img src="@/assets/images/profile.jpg"/>
              </el-avatar>
              <div style="flex: 1;">
                <div style="display: flex; justify-content: space-between;">
                  <span style="font-weight: bold; color: #409EFF;">{{ item.nickName || '匿名用户' }}</span>
                  <span style="color: #909399; font-size: 12px;">{{ item.createTime }}</span>
                </div>
                <div style="margin-top: 8px; color: #606266; line-height: 1.5;">{{ item.content }}</div>
                <div v-if="canDelete(item)" style="text-align: right;">
                  <el-button type="text" style="color: #F56C6C; padding: 0;" @click="handleDeleteComment(item.id)">删除</el-button>
                </div>
              </div>
            </div>
          </div>
          <div v-else style="text-align: center; color: #909399; padding: 30px;">
            暂无评论，快来抢沙发吧~
          </div>
        </div>
      </div>

      <div v-else style="text-align: center; color: #909399; padding: 50px;">
        <p>未找到该资讯或已被删除</p>
      </div>
    </el-card>
      </el-card>
  </div>
</template>

<script>
import { getNews, likeNews } from "@/api/entrance/news";
import { checkCollect, toggleCollect } from "@/api/entrance/collection";
import { listComment, addComment, delComment } from "@/api/entrance/comment";

export default {
  name: "NewsDetail",
  data() {
    return {
      id: null,
      loading: true,
      news: {},
      isLiked: false,
      isCollected: false,
      commentList: [],
      commentForm: {
        content: "",
        targetId: null,
        type: "1" // 1为资讯
      }
    };
  },
  created() {
    this.id = this.$route.params.id;
    if (this.id) {
      this.getDetail();
      this.checkCollectionStatus();
      this.getComments();
    }
  },
  methods: {
    getDetail() {
      this.loading = true;
      getNews(this.id).then(response => {
        this.news = response.data;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    checkCollectionStatus() {
      checkCollect({ targetId: this.id, targetType: 1 }).then(res => {
        this.isCollected = res.data;
      });
    },
    getComments() {
      listComment({ targetId: this.id, type: "1" }).then(res => {
        this.commentList = res.data;
      });
    },
    handleLike() {
      likeNews(this.id).then(() => {
        this.msgSuccess("点赞成功");
        this.isLiked = true;
        this.news.likeCount = (this.news.likeCount || 0) + 1;
      });
    },
    handleCollect() {
      toggleCollect({ targetId: this.id, targetType: 1 }).then(res => {
        this.msgSuccess(res.msg);
        this.isCollected = !this.isCollected;
      });
    },
    submitComment() {
      if (!this.commentForm.content.trim()) {
        this.msgError("内容不能为空");
        return;
      }
      this.commentForm.targetId = this.id;
      addComment(this.commentForm).then(() => {
        this.msgSuccess("评论成功");
        this.commentForm.content = "";
        this.getComments();
      });
    },
    canDelete(comment) {
      const currentUserId = this.$store.getters.userId;
      return currentUserId === comment.userId || currentUserId === 1;
    },
    handleDeleteComment(id) {
      this.$confirm('确定删除该评论吗？', '提示', {
        type: 'warning'
      }).then(() => {
        delComment(id).then(() => {
          this.msgSuccess("删除成功");
          this.getComments();
        });
      });
    },
    goBack() {
      this.$router.go(-1);
    }
  }
};
</script>

<style scoped>
.news-content {
  padding: 20px;
  line-height: 1.8;
  color: #303133;
}
.news-content >>> img {
  max-width: 100%;
}
</style>
