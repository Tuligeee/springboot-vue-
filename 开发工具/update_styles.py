import os

base_dir = r"C:\Users\Tuilg\OneDrive\Desktop\springboot+vue gaokao\前端代码\example-ui\src\views\entrance"

updates = {
    "college/index.vue": ("院校查询中心", "el-icon-school"),
    "college/detail.vue": ("院校详情", "el-icon-reading"),
    "profession/index.vue": ("专业查询中心", "el-icon-collection"),
    "provinceLine/index.vue": ("历年分数线", "el-icon-data-line"),
    "news/list.vue": ("高考资讯", "el-icon-news"),
    "news/detail.vue": ("资讯详情", "el-icon-document"),
    "forum/index.vue": ("交流论坛", "el-icon-chat-dot-round"),
    "forum/detail.vue": ("帖子详情", "el-icon-chat-line-square"),
    "student/index.vue": ("学生信息管理", "el-icon-user"),
    "aspiration/index.vue": ("志愿管理", "el-icon-s-order"),
    "banner/index.vue": ("轮播图管理", "el-icon-picture-outline"),
    "myCollection/index.vue": ("我的收藏", "el-icon-star-off")
}

for rel_path, (title, icon) in updates.items():
    full_path = os.path.join(base_dir, os.path.normpath(rel_path))
    if not os.path.exists(full_path):
        print(f"Skipping {rel_path}, not found.")
        continue
    
    with open(full_path, "r", encoding="utf-8") as f:
        content = f.read()
    
    if '<el-card shadow="hover"' in content and 'class="page-card"' in content:
        print(f"Skipping {rel_path}, already updated.")
        continue

    header = f'''<el-card shadow="hover" class="page-card">
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
