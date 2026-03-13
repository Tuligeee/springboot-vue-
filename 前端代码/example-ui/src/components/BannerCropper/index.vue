<template>
  <div class="banner-cropper">
    <div v-if="value" class="preview-container" @click="editCropper">
      <img :src="value" class="banner-img" title="点击重新裁剪" />
      <div class="mask"><i class="el-icon-edit"></i> 重新裁剪</div>
    </div>
    <div v-else class="upload-placeholder" @click="editCropper">
      <i class="el-icon-plus"></i>
      <div class="text">上传并裁剪 (2.5:1)</div>
    </div>

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body @opened="modalOpened" @close="closeDialog">
      <el-row>
        <el-col :md="24" :style="{height: '400px'}">
          <vue-cropper
            ref="cropper"
            :img="options.img"
            :info="true"
            :autoCrop="options.autoCrop"
            :autoCropWidth="options.autoCropWidth"
            :autoCropHeight="options.autoCropHeight"
            :fixed="options.fixed"
            :fixedNumber="options.fixedNumber"
            :fixedBox="options.fixedBox"
            :centerBox="true"
            :high="true"
            :full="false"
            :canScale="true"
            :mode="'cover'"
            v-if="visible"
          />
        </el-col>
      </el-row>
      <div class="cropper-buttons">
        <el-upload action="#" :http-request="requestUpload" :show-file-list="false" :before-upload="beforeUpload">
          <el-button size="small" type="primary">选择图片</el-button>
        </el-upload>
        <div class="tool-group">
          <el-button icon="el-icon-plus" size="small" circle @click="changeScale(1)"></el-button>
          <el-button icon="el-icon-minus" size="small" circle @click="changeScale(-1)"></el-button>
          <el-button icon="el-icon-refresh-left" size="small" circle @click="rotateLeft()"></el-button>
          <el-button icon="el-icon-refresh-right" size="small" circle @click="rotateRight()"></el-button>
        </div>
        <el-button type="success" size="small" @click="uploadImg">确认并上传</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { VueCropper } from "vue-cropper";
import axios from 'axios';
import { getToken } from "@/utils/auth";

export default {
  components: { VueCropper },
  props: {
    value: { type: String, default: "" }
  },
  computed: {
    // 动态计算预览图缩放比例，使其完美适配 240x80 的框
    previewStyle() {
      if (!this.previews.w) return {};
      const scale = 240 / this.previews.w;
      return {
        transform: `scale(${scale})`,
        transformOrigin: '0 0'
      };
    }
  },
  data() {
    return {
      open: false,
      visible: false,
      title: "裁剪轮播图 (现代大屏 2.5:1)",
      options: {
        img: this.value,
        autoCrop: true,
        autoCropWidth: 0, 
        autoCropHeight: 0,
        fixed: true,
        fixedNumber: [2.5, 1],
        fixedBox: true // 锁定裁剪框大小，只允许用户通过缩放/位移图片来填满左右
      },
      previews: {}
    };
  },
  methods: {
    editCropper() { this.open = true; },
    modalOpened() { this.visible = true; },
    requestUpload() {},
    rotateLeft() { this.$refs.cropper.rotateLeft(); },
    rotateRight() { this.$refs.cropper.rotateRight(); },
    changeScale(num) { this.$refs.cropper.changeScale(num || 1); },
    beforeUpload(file) {
      if (file.type.indexOf("image/") == -1) {
        this.$message.error("请选择图片文件");
        return false;
      }
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => { this.options.img = reader.result; };
    },
    uploadImg() {
      this.$refs.cropper.getCropBlob(data => {
        let formData = new FormData();
        // 关键：某些后端需要特定的参数名，这里统一使用 "file"
        formData.append("file", data, "banner.png");
        
        const uploadUrl = process.env.VUE_APP_BASE_API + "/common/upload";
        const loading = this.$loading({ lock: true, text: "正在上传并处理图片...", background: "rgba(0, 0, 0, 0.7)" });

        axios.post(uploadUrl, formData, {
          headers: { 
            'Content-Type': 'multipart/form-data',
            'Authorization': "Bearer " + getToken()
          }
        }).then(res => {
          loading.close();
          const data = res.data;
          // 兼容性逻辑：判断 code 为 200 或 0 都视为成功
          if (data.code === 200 || data.code === 0) {
            // 兼容性逻辑：提取图片 URL (优先从 data 层取，没有则取根层)
            let imgUrl = "";
            if (data.data && data.data.url) {
                imgUrl = data.data.url;
            } else {
                imgUrl = data.url || data.fileName;
            }

            if (imgUrl) {
              this.$emit("input", imgUrl); 
              this.$message.success("裁剪并上传成功");
              this.open = false;
            } else {
              this.$message.error("上传成功但未获取到图片地址");
            }
          } else {
            this.$message.error(data.msg || "上传失败");
          }
        }).catch(err => {
          loading.close();
          this.$message.error("网络请求失败，请检查后端服务");
        });
      });
    },
    realTime(data) { this.previews = data; },
    closeDialog() { this.visible = false; }
  }
};
</script>

<style scoped lang="scss">
.banner-cropper {
  .preview-container {
    position: relative;
    width: 350px;
    height: 140px;
    border-radius: 4px;
    overflow: hidden;
    cursor: pointer;
    border: 1px solid #dcdfe6;
    
    .banner-img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    
    .mask {
      position: absolute;
      top: 0; left: 0; width: 100%; height: 100%;
      background: rgba(0,0,0,0.5);
      color: #fff;
      display: flex;
      justify-content: center;
      align-items: center;
      opacity: 0;
      transition: opacity 0.3s;
    }
    &:hover .mask { opacity: 1; }
  }
  
  .upload-placeholder {
    width: 350px;
    height: 140px;
    background-color: #fbfdff;
    border: 1px dashed #c0ccda;
    border-radius: 6px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    color: #8c939d;
    &:hover { border-color: #409eff; color: #409eff; }
    i { font-size: 28px; margin-bottom: 8px; }
  }
}

.banner-upload-preview {
  width: 240px;
  height: 80px;
  border: 1px solid #ccc;
  overflow: hidden;
  background: #f8f8f8;
  display: flex;
  justify-content: center;
  align-items: center;
}
.preview-text { text-align: center; margin-top: 20px; color: #999; }
.cropper-buttons {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  .tool-group { display: flex; gap: 10px; }
}
</style>
