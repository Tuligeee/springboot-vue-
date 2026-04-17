-- 1. 修正菜单权限标识，确保与后端 @PreAuthorize 一致
UPDATE sys_menu SET perms = 'entrance:scoreLine:list' WHERE menu_id = 2069;
UPDATE sys_menu SET perms = 'entrance:scoreLine:query' WHERE menu_id = 2075;
UPDATE sys_menu SET perms = 'entrance:scoreLine:add' WHERE menu_id = 2076;
UPDATE sys_menu SET perms = 'entrance:scoreLine:edit' WHERE menu_id = 2077;
UPDATE sys_menu SET perms = 'entrance:scoreLine:remove' WHERE menu_id = 2078;

-- 2. 强制清除旧的关联，防止主键冲突
DELETE FROM sys_role_menu WHERE menu_id IN (2069, 2075, 2076, 2077, 2078);

-- 3. 为管理员 (1) 和 学校管理员 (101) 完整授权
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2069), (1, 2075), (1, 2076), (1, 2077), (1, 2078);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (101, 2069), (101, 2075), (101, 2076), (101, 2077), (101, 2078);

-- 4. 为学生用户 (100) 授权查看权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 2069), (100, 2075);

-- 5. 确保菜单状态是正常且显示的
UPDATE sys_menu SET visible = '0', status = '0' WHERE menu_id IN (2069, 2075, 2076, 2077, 2078);
