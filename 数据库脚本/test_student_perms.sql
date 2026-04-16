USE ry;
SELECT m.menu_id, m.menu_name, m.perms 
FROM sys_menu m 
JOIN sys_role_menu rm ON m.menu_id = rm.menu_id 
WHERE rm.role_id = 100 AND m.perms LIKE '%aspiration%';

SELECT m.menu_id, m.menu_name, m.perms 
FROM sys_menu m 
JOIN sys_role_menu rm ON m.menu_id = rm.menu_id 
WHERE rm.role_id = 2 AND m.perms LIKE '%aspiration%';
