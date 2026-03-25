import os

base_dir = r"C:\Users\Tuilg\OneDrive\Desktop\springboot+vue gaokao\前端代码\example-ui\src\views"

updates = {
    "system/user/index.vue": ("用户管理", "el-icon-user"),
    "system/role/index.vue": ("角色管理", "el-icon-s-custom"),
    "system/menu/index.vue": ("菜单管理", "el-icon-menu"),
    "system/dept/index.vue": ("部门管理", "el-icon-s-operation"),
    "system/post/index.vue": ("岗位管理", "el-icon-s-flag"),
    "system/dict/index.vue": ("字典管理", "el-icon-collection"),
    "system/dict/data.vue": ("字典数据", "el-icon-document-copy"),
    "system/config/index.vue": ("参数设置", "el-icon-setting"),
    "system/notice/index.vue": ("通知公告", "el-icon-message"),
    "monitor/online/index.vue": ("在线用户", "el-icon-user-solid"),
    "monitor/job/index.vue": ("定时任务", "el-icon-time"),
    "monitor/job/log.vue": ("任务日志", "el-icon-document"),
    "tool/gen/index.vue": ("代码生成", "el-icon-magic-stick")
}

for rel_path, (title, icon) in updates.items():
    full_path = os.path.join(base_dir, os.path.normpath(rel_path))
    if not os.path.exists(full_path):
        continue
    
    with open(full_path, "r", encoding="utf-8") as f:
        content = f.read()
    
    if '<el-card shadow="hover"' in content and 'class="page-card"' in content:
        continue

    header = f'''<el-card shadow="hover" class="page-card" style="margin-bottom: 20px;">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; font-size: 18px; color: #303133;">
          <i class="{icon}" style="color: #409EFF; margin-right: 8px;"></i>
          {title}
        </span>
      </div>'''

    if '<div class="app-container">' in content:
        content = content.replace('<div class="app-container">', '<div class="app-container">\n    ' + header, 1)
        
        parts = content.rsplit('</template>', 1)
        if len(parts) == 2:
            inner_content = parts[0]
            last_div_idx = inner_content.rfind('</div>')
            if last_div_idx != -1:
                new_inner = inner_content[:last_div_idx] + '    </el-card>\n  </div>\n'
                content = new_inner + '</template>' + parts[1]
                
        with open(full_path, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Updated {rel_path}")
