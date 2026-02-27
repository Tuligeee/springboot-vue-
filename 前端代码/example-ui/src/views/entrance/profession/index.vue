<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="院校代码" prop="collegeNo">
        <el-input
            v-model="queryParams.collegeNo"
            placeholder="请输入院校代码"
            clearable
            size="small"
            style="width: 150px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="专业代码" prop="professionNo">
        <el-input
            v-model="queryParams.professionNo"
            placeholder="请输入专业代码"
            clearable
            size="small"
            style="width: 150px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="专业名称" prop="professionName">
        <el-input
            v-model="queryParams.professionName"
            placeholder="请输入专业名称"
            clearable
            size="small"
            style="width: 200px"
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
            v-hasPermi="['entrance:profession:add']"
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
            v-hasPermi="['entrance:profession:edit']"
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
            v-hasPermi="['entrance:profession:remove']"
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
            v-hasPermi="['entrance:profession:tag']"
        >设置标签
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="professionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" prop="id" width="40"/>
      <el-table-column label="专业代码" prop="professionNo" :show-overflow-tooltip="true" width="80"/>
      <el-table-column label="专业名称" prop="professionName" :show-overflow-tooltip="true" width="100"/>
      <el-table-column label="院校代码" prop="collegeNo" :show-overflow-tooltip="true" width="80"/>
      <el-table-column label="院校名称" prop="collegeName" :show-overflow-tooltip="true" width="120"/>
      <el-table-column label="修业年限" prop="studyYear" :show-overflow-tooltip="true" width="75"/>
      <el-table-column label="分数线" prop="scoreLineText" :show-overflow-tooltip="true" width="120"/>
      <el-table-column label="专业详情" prop="detailInfo" :show-overflow-tooltip="true" width="120"/>
      <el-table-column label="专业标签" prop="tagNameText" :show-overflow-tooltip="true" width="120"/>
      <el-table-column label="创建时间" align="center" prop="createdTime" width="180">
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
              v-hasPermi="['entrance:profession:edit']"
          >修改
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['entrance:profession:remove']"
          >删除
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-s-tools"
              @click="handleTagSet(scope.row)"
              v-hasPermi="['entrance:profession:tag']"
          >设置标签
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="院校代码" prop="collegeNo">
          <el-input v-model="form.collegeNo" placeholder="请输入院校代码"/>
        </el-form-item>
        <el-form-item label="专业代码" prop="professionNo">
          <el-input v-model="form.professionNo" placeholder="请输入专业代码"/>
        </el-form-item>
        <el-form-item label="专业名称" prop="professionName">
          <el-input v-model="form.professionName" placeholder="请输入专业名称"/>
        </el-form-item>
        <el-form-item label="修业年限" prop="studyYear">
          <el-input v-model="form.studyYear" placeholder="请输入修业年限"/>
        </el-form-item>
        <el-form-item label="专业详情" prop="detailInfo">
          <el-input type="textarea" v-model="form.detailInfo" placeholder="请输入专业详情"/>
        </el-form-item>

        <el-form-item
            v-for="(scoreLine, index) in form.scoreLines"
            :label="'分数线' + (index + 1)"
            :key="scoreLine.key"
            :prop="'scoreLines.' + index + '.score'"
            :rules="{required: true, message: '分数不能为空', trigger: 'blur' }"
        >
          <el-row :gutter="20">
            <el-col :span="8">
              <el-input size="mini" placeholder="请输入年度" v-model="scoreLine.year"></el-input>
            </el-col>
            <el-col :span="8">
              <el-input size="mini" placeholder="请输入分数" v-model="scoreLine.score"></el-input>
            </el-col>
            <el-col :span="4">
              <el-button size="mini" icon="el-icon-delete" @click.prevent="removeScoreLine(scoreLine)">删除</el-button>
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item>
          <el-button size="mini" icon="el-icon-circle-plus-outline" @click="addScoreLine">新增</el-button>
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

import {delProfession, listProfession, addProfession, updateProfession, getProfession} from "@/api/entrance/profession";
import {bindTags, getTags} from "@/api/entrance/tag";

export default {
  name: "Profession",
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
      // 专业表格数据
      professionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        professionNo: undefined,
        professionName: undefined,
        collegeNo: undefined
      },
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单校验
      rules: {
        collegeNo: [
          {required: true, message: "院校代码不能为空", trigger: "blur"}
        ],
        professionNo: [
          {required: true, message: "专业代码不能为空", trigger: "blur"}
        ],
        professionName: [
          {required: true, message: "专业名称不能为空", trigger: "blur"}
        ]
      },
      //标签属性
      tagOpen: false,
      tagTitle: '',
      inputVisible: false,
      tagNames: [],
      tagName: '',
      tagType: 'PROFESSION',
      bindTagId: undefined
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询专业列表 */
    getList() {
      this.loading = true;
      listProfession(this.queryParams).then(
          response => {
            this.professionList = response.rows;
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
        collegeNo: undefined,
        professionNo: undefined,
        professionName: undefined,
        studyYear: undefined,
        detailInfo: undefined,
        scoreLines: [{
          year: '',
          score: ''
        }]
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
      this.title = "添加院校";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const professionId = row.id || this.ids
      getProfession(professionId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改院校";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateProfession(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProfession(this.form).then(response => {
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
      const professionIds = row.id || this.ids;
      this.$confirm('是否确认删除专业ID为"' + professionIds + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delProfession(professionIds);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 添加分数线*/
    addScoreLine() {
      this.form.scoreLines.push({
        year: '',
        score: '',
        key: Date.now()
      });
    },
    /** 删除分数线*/
    removeScoreLine(item) {
      var index = this.form.scoreLines.indexOf(item)
      if (index !== -1) {
        this.form.scoreLines.splice(index, 1)
      }
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
/** 表格tips宽度样式*/
.el-tooltip__popper {
  max-width: 800px;
}

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




