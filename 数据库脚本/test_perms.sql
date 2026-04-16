USE ry;
SELECT r.role_id, rm.menu_id, m.menu_name, m.perms 
FROM sys_role r 
JOIN sys_user_role ur ON r.role_id = ur.role_id 
JOIN sys_user u ON u.user_id = ur.user_id 
JOIN sys_role_menu rm ON r.role_id = rm.role_id 
JOIN sys_menu m ON rm.menu_id = m.menu_id 
WHERE u.user_name = 'bianjiawang001' AND m.perms LIKE '%aspiration%';
