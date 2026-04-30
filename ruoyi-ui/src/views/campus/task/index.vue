<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="任务标题" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入任务标题" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="任务状态" clearable>
          <el-option label="待接取" value="0"/>
          <el-option label="进行中" value="1"/>
          <el-option label="待审核" value="2"/>
          <el-option label="已完成" value="3"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd" v-hasPermi="['campus:task:add']">新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="taskList">
      <el-table-column label="任务ID" prop="taskId" width="80"/>
      <el-table-column label="任务标题" prop="title" :show-overflow-tooltip="true"/>
      <el-table-column label="发布人" prop="publisherName" width="100"/>
      <el-table-column label="积分" prop="rewardPoints" width="80"/>
      <el-table-column label="状态" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="info">待接取</el-tag>
          <el-tag v-else-if="scope.row.status === '1'" type="warning">进行中</el-tag>
          <el-tag v-else-if="scope.row.status === '2'">待审核</el-tag>
          <el-tag v-else-if="scope.row.status === '3'" type="success">已完成</el-tag>
          <el-tag v-else type="danger">已拒绝</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="截止时间" prop="deadline" width="160"/>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleTake(scope.row)" v-if="scope.row.status === '0'">接取</el-button>
          <el-button size="mini" type="text" @click="handleEdit(scope.row)" v-hasPermi="['campus:task:edit']">修改</el-button>
          <el-button size="mini" type="text" @click="handleDelete(scope.row)" v-hasPermi="['campus:task:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="任务标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入任务标题"/>
        </el-form-item>
        <el-form-item label="任务描述" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="4"/>
        </el-form-item>
        <el-form-item label="积分奖励" prop="rewardPoints">
          <el-input-number v-model="form.rewardPoints" :min="0"/>
        </el-form-item>
        <el-form-item label="截止时间" prop="deadline">
          <el-date-picker v-model="form.deadline" type="datetime" placeholder="选择日期时间"/>
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
import { listTask, addTask, updateTask, delTask, takeTask } from "@/api/campus/task";

export default {
  name: "CampusTask",
  data() {
    return {
      loading: true,
      taskList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        status: null
      },
      open: false,
      title: "",
      form: {},
      rules: {
        title: [{ required: true, message: "任务标题不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listTask(this.queryParams).then(response => {
        this.taskList = response.rows;
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
      this.title = "发布任务";
    },
    handleEdit(row) {
      this.reset();
      this.form = { ...row };
      this.open = true;
      this.title = "修改任务";
    },
    handleTake(row) {
      this.$confirm('确认接取该任务?', "提示", { type: "warning" }).then(() => {
        return takeTask(row.taskId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("接取成功");
      });
    },
    handleDelete(row) {
      this.$confirm('确认删除该任务?', "警告", { type: "warning" }).then(() => {
        return delTask(row.taskId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.taskId) {
            updateTask(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTask(this.form).then(() => {
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
        taskId: null,
        title: null,
        content: null,
        rewardPoints: 0,
        deadline: null
      };
      this.resetForm("form");
    }
  }
};
</script>
