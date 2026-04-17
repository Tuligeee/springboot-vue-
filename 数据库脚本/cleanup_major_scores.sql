-- 隐藏专业分数线相关菜单
-- 将 visible 字段设为 '1' (隐藏)

-- 隐藏 '专业分数线' (menu_id=2069)
UPDATE sys_menu SET visible = '1' WHERE menu_id = 2069;

-- 同时推荐隐藏其子权限按钮（可选，但推荐）
UPDATE sys_menu SET visible = '1' WHERE parent_id = 2069;
