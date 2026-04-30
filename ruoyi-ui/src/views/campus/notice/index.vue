<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="公告标题" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入公告标题" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="公告类型" clearable>
          <el-option label="通知" value="1"/>
          <el-option label="活动" value="2"/>
          <el-option label="紧急" value="3"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd" v-hasPermi="['campus:notice:add']">新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="noticeList">
      <el-table-column label="公告ID" prop="noticeId" width="80"/>
      <el-table-column label="标题" prop="title" :show-overflow-tooltip="true"/>
      <el-table-column label="类型" prop="type" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === '1'">通知</el-tag>
          <el-tag v-else-if="scope.row.type === '2'" type="success">活动</el-tag>
          <el-tag v-else type="danger">紧急</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发布人" prop="publisherName" width="100"/>
      <el-table-column label="浏览量" prop="viewCount" width="80"/>
      <el-table-column label="置顶" prop="isTop" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isTop === '1'" type="warning">是</el-tag>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" prop="createTime" width="160"/>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleEdit(scope.row)" v-hasPermi="['campus:notice:edit']">修改</el-button>
          <el-button size="mini" type="text" @click="handleDelete(scope.row)" v-hasPermi="['campus:notice:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="公告标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入公告标题"/>
        </el-form-item>
        <el-form-item label="公告类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio label="1">通知</el-radio>
            <el-radio label="2">活动</el-radio>
            <el-radio label="3">紧急</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否置顶" prop="isTop">
          <el-radio-group v-model="form.isTop">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="公告内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="6"/>
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
import { listNotice, addNotice, updateNotice, delNotice } from "@/api/campus/notice";

export default {
  name: "CampusNotice",
  data() {
    return {
      loading: true,
      noticeList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        type: null
      },
      open: false,
      title: "",
      form: {},
      rules: {
        title: [{ required: true, message: "公告标题不能为空", trigger: "blur" }],
        type: [{ required: true, message: "请选择公告类型", trigger: "change" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listNotice(this.queryParams).then(response => {
        this.noticeList = response.rows;
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
      this.title = "发布公告";
    },
    handleEdit(row) {
      this.reset();
      this.form = { ...row };
      this.open = true;
      this.title = "修改公告";
    },
    handleDelete(row) {
      this.$confirm('确认删除该公告?', "警告", { type: "warning" }).then(() => {
        return delNotice(row.noticeId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.noticeId) {
            updateNotice(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addNotice(this.form).then(() => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        noticeId: null,
        title: null,
        content: null,
        type: "1",
        isTop: "0"
      };
      this.resetForm("form");
    }
  }
};
</script>
