<template>
  <div class="login-container">
    <div class="login-box">
      <!-- 左侧品牌展示区 - 重新找回文字仪式感 -->
      <div class="login-left">
        <div class="brand-wrapper">
          <div class="brand-content">
            <h2 class="welcome-text">WELCOME</h2>
            <h1 class="system-name">高考志愿填报系统</h1>
            <div class="divider"></div>
            <p class="slogan">智选志愿 · 成就梦想</p>
          </div>
          <div class="brand-decoration">
            <span class="dot d1"></span>
            <span class="dot d2"></span>
            <span class="dot d3"></span>
          </div>
        </div>
      </div>

      <!-- 右侧登录表单区 -->
      <div class="login-right">
        <div class="login-form-wrapper">
          <div class="form-header">
            <div class="form-title">用户登录</div>
            <p class="form-sub">User Login</p>
          </div>
          <el-form ref="loginForm" :model="loginForm" :rules="loginRules">
            <el-form-item prop="username">
              <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
                <i slot="prefix" class="el-icon-user el-input__icon"></i>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                auto-complete="off"
                placeholder="密码"
                show-password
                @keyup.enter.native="handleLogin"
              >
                <i slot="prefix" class="el-icon-lock el-input__icon"></i>
              </el-input>
            </el-form-item>
            
            <div class="login-options">
              <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
              <router-link v-if="register" class="register-link" :to="'/register'">立即注册</router-link>
            </div>

            <el-form-item style="width:100%; margin-top: 10px;">
              <el-button
                :loading="loading"
                size="medium"
                type="primary"
                class="login-btn"
                @click.native.prevent="handleLogin"
              >
                <span v-if="!loading">立 即 登 录</span>
                <span v-else>安 全 登 录 中...</span>
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>

    <!-- 底部备案信息或修饰 -->
    <div class="login-footer">
      <span>Copyright © 2026 高考志愿填报系统 All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        username: "admin",
        password: "admin123",
        rememberMe: false
      },
      loginRules: {
        username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
        password: [{ required: true, trigger: "blur", message: "请输入您的密码" }]
      },
      loading: false,
      register: true,
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    this.getCookie();
  },
  methods: {
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(()=>{});
          }).catch(() => {
            this.loading = false;
          });
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.login-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: #f0f4f8; /* 浅淡灰蓝，解决“太蓝”的问题 */
  position: relative;
  overflow: hidden;
}

.login-box {
  display: flex;
  width: 960px;
  height: 560px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 15px 45px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  z-index: 10;
  animation: slideIn 0.8s ease-out;
}

@keyframes slideIn {
  from { opacity: 0; transform: translateY(30px); }
  to { opacity: 1; transform: translateY(0); }
}

.login-left {
  flex: 4;
  background: #0974e7;
  position: relative;
  display: flex;
  align-items: center;
  padding: 60px;
  color: #fff;

  .brand-wrapper {
    position: relative;
    z-index: 2;
  }

  .welcome-text {
    font-size: 14px;
    letter-spacing: 5px;
    color: rgba(255, 255, 255, 0.8);
    margin-bottom: 10px;
    font-weight: bold;
  }

  .system-name {
    font-size: 32px;
    font-weight: bold;
    margin-bottom: 20px;
    line-height: 1.2;
    text-shadow: 0 2px 10px rgba(0,0,0,0.2);
  }

  .divider {
    width: 45px;
    height: 5px;
    background: #fff;
    margin-bottom: 25px;
    border-radius: 2px;
  }

  .slogan {
    font-size: 16px;
    color: #fff;
    letter-spacing: 3px;
    opacity: 0.9;
  }

  .dot {
    position: absolute;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.1);
  }
  .d1 { width: 140px; height: 140px; top: -80px; left: -60px; }
  .d2 { width: 200px; height: 200px; bottom: -100px; right: -50px; }
}

.login-right {
  flex: 6;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px;
  background: #fff;

  .login-form-wrapper {
    width: 100%;
    max-width: 360px;
  }

  .form-header {
    margin-bottom: 40px;
    .form-title {
      font-size: 26px;
      font-weight: bold;
      color: #0974e7;
      margin-bottom: 8px;
    }
    .form-sub {
      font-size: 13px;
      color: #bbb;
      letter-spacing: 2px;
    }
  }
}

.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  
  .register-link {
    font-size: 14px;
    color: #0974e7;
    text-decoration: none;
    font-weight: bold;
    &:hover { color: #20a0ff; }
  }
}

.login-btn {
  width: 100%;
  height: 50px;
  font-size: 17px;
  font-weight: bold;
  letter-spacing: 6px;
  border-radius: 8px;
  background: #0974e7;
  border: none;
  transition: all 0.3s;
  
  &:hover {
    background: #1890ff;
    transform: translateY(-2px);
    box-shadow: 0 10px 25px rgba(9, 116, 231, 0.4);
  }
}

.login-footer {
  position: absolute;
  bottom: 30px;
  color: #fff;
  font-size: 12px;
  z-index: 10;
  opacity: 0.6;
}

.el-input__icon {
  font-size: 18px;
  color: #0974e7;
}

::v-deep .el-input__inner {
  height: 50px;
  line-height: 50px;
  border-radius: 8px;
  background: #f8fbff;
  border: 1px solid #c9e2ff;
  &:focus {
    background: #fff;
    border-color: #0974e7;
    box-shadow: 0 0 0 2px rgba(9, 116, 231, 0.1);
  }
}
</style>
