-- =================================================================================
-- 高考志愿填报系统 - 冗余菜单清理脚本 (2026-04-20)
-- 目的：彻底移除系统中的测试菜单及冗余模块，确保生产环境纯净
-- =================================================================================

-- 1. 清理角色菜单关联（防止违反外键约束或残留脏数据）
DELETE FROM sys_role_menu 
WHERE menu_id IN (
    SELECT menu_id FROM sys_menu 
    WHERE menu_name IN ('123', '专业分数管理', '学校中心')
    OR menu_id BETWEEN 2075 AND 2078  -- 专业分数管理的子权限
    OR menu_id = 2069                 -- 专业分数管理主ID
);

-- 2. 清理菜单定义
-- 删除子菜单/权限点
DELETE FROM sys_menu WHERE parent_id = 2069;
-- 删除主菜单项
DELETE FROM sys_menu WHERE menu_name IN ('123', '专业分数管理', '学校中心') OR menu_id = 2069;

-- 验证清理结果
SELECT menu_id, menu_name, parent_id, status FROM sys_menu WHERE menu_name IN ('123', '专业分数管理', '学校中心');
