INSERT INTO `sys_menu` VALUES (6020, '学生信息管理', 6000, 2, 'student', 'entrance/student/index', '', 1, 0, 'C', '0', '0', 'entrance:student:list', 'user', 'admin', sysdate(), '', null, '学生信息维护');
INSERT INTO `sys_menu` VALUES (6021, '学生查询', 6020, 1, '#', '', '', 1, 0, 'F', '0', '0', 'entrance:student:query', '#', 'admin', sysdate(), '', null, '');
INSERT INTO `sys_menu` VALUES (6022, '学生新增', 6020, 2, '#', '', '', 1, 0, 'F', '0', '0', 'entrance:student:add', '#', 'admin', sysdate(), '', null, '');
INSERT INTO `sys_menu` VALUES (6023, '学生修改', 6020, 3, '#', '', '', 1, 0, 'F', '0', '0', 'entrance:student:edit', '#', 'admin', sysdate(), '', null, '');
INSERT INTO `sys_menu` VALUES (6024, '学生删除', 6020, 4, '#', '', '', 1, 0, 'F', '0', '0', 'entrance:student:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO `sys_role_menu` (role_id, menu_id) VALUES (1, 6020), (1, 6021), (1, 6022), (1, 6023), (1, 6024), (2, 6020), (2, 6021), (2, 6022), (2, 6023), (2, 6024);
