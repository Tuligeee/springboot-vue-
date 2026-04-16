USE ry;
SELECT * FROM sys_user WHERE user_name = 'bianjiawang001';
SELECT ur.*, r.role_key, m.menu_id, m.perms 
FROM sys_user u 
LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id 
LEFT JOIN sys_role r ON r.role_id = ur.role_id 
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id 
LEFT JOIN sys_menu m ON rm.menu_id = m.menu_id 
WHERE u.user_name = 'bianjiawang001' AND m.perms LIKE '%aspiration%';
