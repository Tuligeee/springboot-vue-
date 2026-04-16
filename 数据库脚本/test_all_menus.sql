USE ry;
SELECT * FROM sys_menu WHERE menu_name LIKE '%志愿%' OR path LIKE '%aspiration%';
SELECT * FROM sys_menu WHERE menu_id IN (2061, 2063, 2064, 2080);
