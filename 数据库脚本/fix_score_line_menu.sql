-- 1. 统一菜单名称为“历年分数线”
UPDATE sys_menu SET menu_name = '历年分数线', remark = '包含省控线及历年投档线数据' 
WHERE menu_id = 2067 OR menu_name = '档线信息管理' OR menu_name = '档线管理';

-- 2. 如果菜单 2067 不存在，则重新插入一条
INSERT INTO `sys_menu` VALUES (2067, '历年分数线', 0, 1, 'provinceLine', 'entrance/provinceLine/index', NULL, 1, 0, 'C', '0', '0', 'entrance:provinceLine:list', 'cascader', 'admin', SYSDATE(), '', NULL, '历年分数线查询管理');

-- 3. 确保超级管理员 (1)、学生 (100) 和 学校管理员 (101) 拥有此菜单权限
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (1, 2067);
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (100, 2067);
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (101, 2067);

-- 4. 补充相关的增删改查权限按钮 (F 类型)，确保角色可以操作
-- 添加
INSERT IGNORE INTO `sys_menu` VALUES (2071, '档线添加', 2067, 1, '#', '', '', 1, 0, 'F', '0', '0', 'entrance:provinceLine:add', '#', 'admin', SYSDATE(), '', NULL, '');
-- 修改
INSERT IGNORE INTO `sys_menu` VALUES (2072, '档线修改', 2067, 2, '#', '', '', 1, 0, 'F', '0', '0', 'entrance:provinceLine:edit', '#', 'admin', SYSDATE(), '', NULL, '');
-- 删除
INSERT IGNORE INTO `sys_menu` VALUES (2073, '档线删除', 2067, 3, '#', '', '', 1, 0, 'F', '0', '0', 'entrance:provinceLine:remove', '#', 'admin', SYSDATE(), '', NULL, '');
-- 导出
INSERT IGNORE INTO `sys_menu` VALUES (2074, '档线导出', 2067, 4, '#', '', '', 1, 0, 'F', '0', '0', 'entrance:provinceLine:export', '#', 'admin', SYSDATE(), '', NULL, '');

-- 分配按钮权限给管理员
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) SELECT 1, menu_id FROM sys_menu WHERE parent_id = 2067;
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) SELECT 101, menu_id FROM sys_menu WHERE parent_id = 2067;
