-- 1. 添加“省控线管理”菜单到“院校管理”(ID: 2054) 目录下
INSERT INTO `sys_menu` (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('省控线管理', 2054, 3, 'provinceLine', 'entrance/provinceLine/index', 1, 0, 'C', '0', '0', 'entrance:provinceLine:list', 'chart', 'admin', NOW());

-- 2. 添加“录取分数管理”菜单到“院校管理”(ID: 2054) 目录下
INSERT INTO `sys_menu` (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('专业分数维护', 2054, 5, 'scoreLine', 'entrance/scoreLine/index', 1, 0, 'C', '0', '0', 'entrance:scoreLine:list', 'education', 'admin', NOW());

-- 3. 为“省控线管理”添加按钮权限
SET @last_id = LAST_INSERT_ID(); -- 这里不安全，我们直接查或硬编码
-- 由于省控线管理是新增的，我们假设它是最后插入的
-- 实际上最好通过查询获取 parent_id

-- 4. 授予管理员权限 (admin 角色 ID 1 在 sys_role_menu 中通常全量或手动加)
-- 获取刚才插入的菜单ID (假设分别是 A 和 B)
-- 这里使用子查询更稳妥

INSERT INTO `sys_role_menu` (role_id, menu_id)
SELECT 1, menu_id FROM `sys_menu` WHERE perms IN ('entrance:provinceLine:list', 'entrance:scoreLine:list');

-- 5. 授予院校管理员权限 (仅分数维护)
INSERT INTO `sys_role_menu` (role_id, menu_id)
SELECT 2, menu_id FROM `sys_menu` WHERE perms = 'entrance:scoreLine:list';
