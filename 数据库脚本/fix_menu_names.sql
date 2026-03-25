-- =================================================================================
-- 高考志愿填报系统 - 菜单名称视觉适配脚本 (学生端优化)
-- 目的：移除所有具有“管理”色彩的菜单词汇，改为符合学生视角的“查询/浏览/填报”
-- =================================================================================

-- 1. 核心业务分类优化 (一级菜单)
-- 将“学校中心”保持不变，但将“填报管理”改为“模拟填报”
UPDATE sys_menu SET menu_name = '志愿填报' WHERE menu_name = '填报管理' OR menu_id = 2061;

-- 2. 招生相关菜单优化 (二级菜单)
-- 如果看到“招生信息管理”，统一改为“院校招生查询”
UPDATE sys_menu SET menu_name = '院校查询' WHERE menu_name LIKE '%招生信息管理%' OR menu_id = 2055;

-- 3. 专业相关菜单优化 (二级菜单)
-- 如果看到“本校专业管理”，统一改为“专业列表查询”
UPDATE sys_menu SET menu_name = '专业查询' WHERE menu_name LIKE '%本校专业管理%' OR menu_id = 2056;

-- 4. 资讯与档线管理优化
-- 资讯管理 -> 政策资讯
UPDATE sys_menu SET menu_name = '政策资讯' WHERE menu_name LIKE '%高考资讯管理%' OR menu_id = 2065;
-- 档线管理 -> 历年分数线
UPDATE sys_menu SET menu_name = '历年分数线' WHERE menu_name LIKE '%档线信息管理%' OR menu_id = 2067;

-- 5. 其他细节优化
UPDATE sys_menu SET menu_name = '志愿单管理' WHERE menu_name = '志愿管理' OR menu_id = 2063;
UPDATE sys_menu SET menu_name = '在线志愿填报' WHERE menu_name = '志愿填报' AND parent_id = 2061;

-- 验证结果预览
SELECT menu_id, menu_name, parent_id FROM sys_menu WHERE menu_id > 2050;
