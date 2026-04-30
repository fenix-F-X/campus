<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="帖子标题" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入帖子标题" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="分类" prop="category">
        <el-select v-model="queryParams.category" placeholder="帖子分类" clearable>
          <el-option label="失物招领" value="失物招领"/>
          <el-option label="求助" value="求助"/>
          <el-option label="闲置" value="闲置"/>
          <el-option label="其他" value="其他"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd" v-hasPermi="['campus:post:add']">发帖</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="postList" @row-click="handleRowClick" style="cursor: pointer">
      <el-table-column label="帖子ID" prop="postId" width="80"/>
      <el-table-column label="标题" prop="title" :show-overflow-tooltip="true"/>
      <el-table-column label="作者" prop="authorName" width="100"/>
      <el-table-column label="分类" prop="category" width="100"/>
      <el-table-column label="浏览" prop="viewCount" width="80"/>
      <el-table-column label="点赞" prop="likeCount" width="80"/>
      <el-table-column label="发布时间" prop="createTime" width="160"/>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click.stop="handleDelete(scope.row)" v-hasPermi="['campus:post:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 发帖对话框 -->
    <el-dialog title="发布帖子" :visible.sync="open" width="700px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="帖子标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入帖子标题"/>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择分类">
            <el-option label="失物招领" value="失物招领"/>
            <el-option label="求助" value="求助"/>
            <el-option label="闲置" value="闲置"/>
            <el-option label="其他" value="其他"/>
          </el-select>
        </el-form-item>
        <el-form-item label="帖子内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="8"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPost, addPost, delPost } from "@/api/campus/post";

export default {
  name: "CampusPost",
  data() {
    return {
      loading: true,
      postList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        category: null
      },
      open: false,
      form: {},
      rules: {
        title: [{ required: true, message: "帖子标题不能为空", trigger: "blur" }],
        category: [{ required: true, message: "请选择分类", trigger: "change" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listPost(this.queryParams).then(response => {
        this.postList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleAdd() {
      this.reset();
      this.open = true;
    },
    handleRowClick(row) {
      this.$router.push({ name: 'PostDetail', params: { postId: row.postId } });
    },
    handleDelete(row) {
      this.$confirm('确认删除该帖子?', "警告", { type: "warning" }).then(() => {
        return delPost(row.postId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addPost(this.form).then(() => {
            this.$modal.msgSuccess("发布成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        postId: null,
        title: null,
        content: null,
        category: null
      };
      this.resetForm("form");
    }
  }
};
</script>
