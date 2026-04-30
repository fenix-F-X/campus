<template>
  <div class="app-container">
    <el-card v-loading="loading">
      <div slot="header" class="clearfix">
        <el-button style="float: left" icon="el-icon-back" @click="goBack">返回</el-button>
        <span style="margin-left: 20px; font-size: 20px; font-weight: bold">{{ post.title }}</span>
      </div>

      <div class="post-meta">
        <span>作者：{{ post.authorName }}</span>
        <el-divider direction="vertical"/>
        <span>分类：{{ post.category }}</span>
        <el-divider direction="vertical"/>
        <span>浏览：{{ post.viewCount }}</span>
        <el-divider direction="vertical"/>
        <span>{{ post.createTime }}</span>
      </div>

      <el-divider/>

      <div class="post-content" v-html="post.content"></div>

      <div class="post-actions">
        <el-button type="primary" icon="el-icon-thumb" @click="handleLike">
          点赞 ({{ post.likeCount }})
        </el-button>
      </div>
    </el-card>

    <!-- 评论区 -->
    <el-card style="margin-top: 20px">
      <div slot="header">评论 ({{ post.comments ? post.comments.length : 0 }})</div>

      <!-- 发表评论 -->
      <el-input
        type="textarea"
        :rows="3"
        placeholder="写下你的评论..."
        v-model="commentContent"
        maxlength="500"
        show-word-limit
      />
      <el-button type="primary" style="margin-top: 10px" @click="handleAddComment">发表评论</el-button>

      <el-divider/>

      <!-- 评论列表 -->
      <div v-for="comment in post.comments" :key="comment.commentId" class="comment-item">
        <div class="comment-header">
          <span class="comment-author">{{ comment.authorName }}</span>
          <span class="comment-time">{{ comment.createTime }}</span>
        </div>
        <div class="comment-content">{{ comment.content }}</div>

        <!-- 子评论 -->
        <div v-if="comment.children && comment.children.length > 0" class="comment-children">
          <div v-for="child in comment.children" :key="child.commentId" class="comment-child">
            <span class="comment-author">{{ child.authorName }}</span>：
            <span>{{ child.content }}</span>
            <span class="comment-time">{{ child.createTime }}</span>
          </div>
        </div>

        <div class="comment-actions">
          <el-button type="text" size="small" @click="handleReply(comment)">回复</el-button>
          <el-button type="text" size="small" @click="handleDelComment(comment.commentId)">删除</el-button>
        </div>
      </div>
    </el-card>

    <!-- 回复对话框 -->
    <el-dialog title="回复评论" :visible.sync="replyDialogVisible" width="500px">
      <el-input
        type="textarea"
        :rows="4"
        placeholder="写下你的回复..."
        v-model="replyContent"
        maxlength="500"
        show-word-limit
      />
      <div slot="footer">
        <el-button @click="replyDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitReply">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPost, likePost, addComment, delComment } from "@/api/campus/post";

export default {
  name: "PostDetail",
  data() {
    return {
      loading: true,
      post: {
        comments: []
      },
      commentContent: "",
      replyDialogVisible: false,
      replyContent: "",
      replyParentId: null
    };
  },
  created() {
    this.loadPost();
  },
  methods: {
    loadPost() {
      const postId = this.$route.params.postId;
      this.loading = true;
      getPost(postId).then(response => {
        this.post = response.data;
        this.loading = false;
      });
    },
    goBack() {
      this.$router.back();
    },
    handleLike() {
      likePost(this.post.postId).then(() => {
        this.post.likeCount++;
        this.$message.success("点赞成功");
      });
    },
    handleAddComment() {
      if (!this.commentContent.trim()) {
        this.$message.warning("评论内容不能为空");
        return;
      }
      addComment({
        postId: this.post.postId,
        content: this.commentContent,
        parentId: 0
      }).then(() => {
        this.$message.success("评论成功");
        this.commentContent = "";
        this.loadPost();
      });
    },
    handleReply(comment) {
      this.replyParentId = comment.commentId;
      this.replyDialogVisible = true;
    },
    submitReply() {
      if (!this.replyContent.trim()) {
        this.$message.warning("回复内容不能为空");
        return;
      }
      addComment({
        postId: this.post.postId,
        content: this.replyContent,
        parentId: this.replyParentId
      }).then(() => {
        this.$message.success("回复成功");
        this.replyContent = "";
        this.replyDialogVisible = false;
        this.loadPost();
      });
    },
    handleDelComment(commentId) {
      this.$confirm("确认删除该评论?", "警告", { type: "warning" }).then(() => {
        return delComment(commentId);
      }).then(() => {
        this.$message.success("删除成功");
        this.loadPost();
      });
    }
  }
};
</script>

<style scoped>
.post-meta {
  color: #909399;
  font-size: 14px;
}
.post-content {
  margin: 20px 0;
  line-height: 1.8;
  font-size: 15px;
}
.post-actions {
  margin-top: 20px;
}
.comment-item {
  padding: 15px 0;
  border-bottom: 1px solid #ebeef5;
}
.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}
.comment-author {
  font-weight: bold;
  color: #409eff;
}
.comment-time {
  color: #909399;
  font-size: 12px;
}
.comment-content {
  margin: 8px 0;
  line-height: 1.6;
}
.comment-children {
  margin-left: 40px;
  margin-top: 10px;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
}
.comment-child {
  padding: 5px 0;
  font-size: 14px;
}
.comment-actions {
  margin-top: 8px;
}
</style>
