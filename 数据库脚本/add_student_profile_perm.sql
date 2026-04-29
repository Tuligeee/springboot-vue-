-- 1. 为学生角色（ID: 2）分配档案维护权限（逻辑权限）
-- 虽然门户页是硬编码链接，但后端接口可以通过权限标识进行更细粒度的控制

INSERT INTO `sys_menu` (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('高考档案维护', 1, 10, 'studentProfile', 'entrance/student/profile', 1, 0, 'C', '1', '0', 'entrance:student:profile', 'medal', 'admin', NOW());

SET @profile_menu_id = LAST_INSERT_ID();

-- 将该权限分配给普通学生角色 (Role ID: 2)
INSERT INTO `sys_role_menu` (role_id, menu_id) VALUES (2, @profile_menu_id);

-- 同时确保学生拥有志愿单管理的权限标识（如果之前没加的话）
-- 假设学生需要访问 /college_entrance/student/myProfile 等接口
