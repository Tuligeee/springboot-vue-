USE ry;
SELECT r.role_id, r.role_key, r.role_name
FROM sys_role r 
JOIN sys_user_role ur ON r.role_id = ur.role_id 
JOIN sys_user u ON u.user_id = ur.user_id 
WHERE u.user_name = 'bianjiawang001';
