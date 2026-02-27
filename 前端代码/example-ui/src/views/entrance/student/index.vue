<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="学生编号" prop="studentNo">
        <el-input
            v-model="queryParams.studentNo"
            placeholder="请输入学生编号"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学生姓名" prop="studentName">
        <el-input
            v-model="queryParams.studentName"
            placeholder="请输入学生姓名"
            clearable
            size="small"
            style="width: 150px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入学年份" prop="enrollmentYear">
        <el-input
            v-model="queryParams.enrollmentYear"
            placeholder="请输入入学年份"
            clearable
            size="small"
            style="width: 150px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="毕业年限" prop="graduateYear">
        <el-input
            v-model="queryParams.graduateYear"
            placeholder="请输入毕业年份"
            clearable
            size="small"
            style="width: 150px"
            @keyup.enter.native="handleQuery"
        />
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
            v-hasPermi="['entrance:student:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['entrance:student:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['entrance:student:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="info"
            plain
            icon="el-icon-s-tools"
            size="mini"
            :disabled="single"
            @click="handleTagSet  "
            v-hasPermi="['entrance:student:tag']"
        >设置标签
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="studentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" prop="id" width="60"/>
      <el-table-column label="登录用户" prop="userName" :show-overflow-tooltip="true" width="100"/>
      <el-table-column label="学生编号" prop="studentNo" :show-overflow-tooltip="true" width="120"/>
      <el-table-column label="学生姓名" prop="studentName" :show-overflow-tooltip="true" width="80"/>
      <el-table-column label="性别" :show-overflow-tooltip="true" width="50">
        <template slot-scope="scope">
          {{ scope.row.sex == "MAN" ? "男" : scope.row.sex == "WOMEN" ? "女" : "" }}
        </template>
      </el-table-column>
      <el-table-column label="入学年份" prop="enrollmentYear" :show-overflow-tooltip="true" width="80"/>
      <el-table-column label="毕业年份" prop="graduateYear" :show-overflow-tooltip="true" width="80"/>
      <el-table-column label="高考成绩" prop="achievement" :show-overflow-tooltip="true" width="80"/>
      <el-table-column label="学生标签" prop="tagNameText" :show-overflow-tooltip="true" width="150"/>
      <el-table-column label="创建时间" align="center" prop="createdTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope" v-if="scope.row.id !== 0">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['entrance:student:edit']"
          >修改
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['entrance:student:remove']"
          >删除
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-s-tools"
              @click="handleTagSet(scope.row)"
              v-hasPermi="['entrance:student:tag']"
          >设置标签
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改学生配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="学生编号" prop="studentNo">
          <el-input v-model="form.studentNo" placeholder="请输入学生编号"/>
        </el-form-item>
        <el-form-item label="学生名称" prop="studentName">
          <el-input v-model="form.studentName" placeholder="请输入学生名称"/>
        </el-form-item>
        <el-form-item label="用户账号" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户账号,用来绑定登录用户"/>
        </el-form-item>
        <el-form-item label="入学年份" prop="enrollmentYear">
          <el-input v-model="form.enrollmentYear" placeholder="请输入入学年份"/>
        </el-form-item>
        <el-form-item label="毕业年份" prop="graduateYear">
          <el-input v-model="form.graduateYear" placeholder="请输入毕业年份"/>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="form.sex" placeholder="请选择下拉选择" clearable :style="{width: '100%'}">
            <el-option v-for="(item, index) in sexSelectOptions" :key="index" :label="item.label"
                       :value="item.value" :disabled="item.disabled"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="高考成绩" prop="achievement">
          <el-input v-model="form.achievement" placeholder="请输入高考成绩"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 标签  -->
    <el-dialog :title="tagTitle" :visible.sync="tagOpen" width="500px" append-to-body>
      <el-tag
          :key="tag"
          v-for="tag in this.tagNames"
          closable
          :disable-transitions="false"
          @close="handleTagClose(tag)">
        {{ tag }}
      </el-tag>
      <el-input
          class="input-new-tag"
          v-if="inputVisible"
          v-model="tagName"
          ref="saveTagInput"
          size="small"
          @keyup.enter.native="handleTagInputConfirm"
          @blur="handleTagInputConfirm"
      >
      </el-input>
      <el-button v-else class="button-new-tag" size="small" @click="showTagInput">点击添加</el-button>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="bindTags">确 定</el-button>
      </div>
    </el-dialog>

    <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
    />

  </div>
</template>

<script>

import {listStudent, addStudent, updateStudent, getStudent, delStudent} from "@/api/entrance/student";
import {getTags, bindTags} from "@/api/entrance/tag";

export default {
  name: "Student",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 学生表格数据
      studentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        studentNo: undefined,
        studentName: undefined,
        enrollmentYear: undefined,
        graduateYear: undefined
      },
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单校验
      rules: {
        studentNo: [
          {required: true, message: "学生编号不能为空", trigger: "blur"}
        ],
        studentName: [
          {required: true, message: "学生姓名不能为空", trigger: "blur"}
        ]
      },
      //性别下拉框选项
      sexSelectOptions: [{
        value: 'MAN',
        label: '男'
      }, {
        value: 'WOMEN',
        label: '女'
      }],
      //标签属性
      tagOpen: false,
      tagTitle: '',
      inputVisible: false,
      tagNames: [],
      tagName: '',
      tagType: 'STUDENT',
      bindTagId: undefined
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询学生列表 */
    getList() {
      this.loading = true;
      listStudent(this.queryParams).then(
          response => {
            this.studentList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
      );
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        studentNo: undefined,
        studentName: undefined,
        userName: undefined,
        enrollmentYear: undefined,
        graduateYear: undefined,
        sex: undefined,
        achievement: undefined
      };
      this.resetForm("form");
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加学生";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const studentId = row.id || this.ids
      getStudent(studentId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改学生";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateStudent(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStudent(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const studentIds = row.id || this.ids;
      this.$confirm('是否确认删除学生ID为"' + studentIds + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delStudent(studentIds);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 设置标签*/
    handleTagSet(row) {
      const relId = row.id || this.ids
      getTags(this.tagType, relId).then(response => {
        this.tagOpen = true;
        this.tagTitle = "添加标签（用于志愿填报测评）";
        this.tagNames = response.data.tagNames;
        this.bindTagId = response.data.relId;
      });
    },
    /** 删除标签*/
    handleTagClose(tag) {
      this.tagNames.splice(this.tagNames.indexOf(tag), 1);
    },
    /** 展示标签输入框*/
    showTagInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    /** 标签输入确认*/
    handleTagInputConfirm() {
      let tagName = this.tagName;
      if (tagName) {
        this.tagNames.push(tagName);
      }
      this.inputVisible = false;
      this.tagName = '';
    },
    /** 绑定标签*/
    bindTags: function () {
      let tagData = {
        relId: this.bindTagId,
        tagType: this.tagType,
        tagNames: this.tagNames
      }
      bindTags(tagData).then(response => {
        this.msgSuccess("设置成功");
        //重置标签数据
        this.tagOpen = false;
        this.tagNames = [];
        this.tagName = '';
        this.bindTagId = undefined;
        this.getList();
      });
    }

  }
};
</script>

<style>
.el-tag + .el-tag {
  margin-left: 10px;
}

.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0px;
  padding-bottom: 0;
}

.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
