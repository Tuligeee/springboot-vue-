-- 1. 删除旧表（如果存在）
DROP TABLE IF EXISTS `ce_province_score`;

-- 2. 创建全新的历年分数线表
CREATE TABLE `ce_province_score` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `year` int(11) NOT NULL COMMENT '年份',
  `province` varchar(50) NOT NULL COMMENT '省份',
  `category` varchar(20) NOT NULL COMMENT '科类 (文科/理科/综合)',
  `batch` varchar(50) NOT NULL COMMENT '批次 (本科一批/本科二批/高职专科)',
  `score` int(11) NOT NULL COMMENT '分数线',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='历年分数线表';

-- 3. 插入演示数据
INSERT INTO `ce_province_score` (`year`, `province`, `category`, `batch`, `score`, `create_time`) VALUES 
(2023, '湖北', '理科', '本科一批', 525, NOW()),
(2023, '湖北', '文科', '本科一批', 542, NOW()),
(2022, '湖北', '理科', '本科一批', 504, NOW()),
(2022, '湖北', '文科', '本科一批', 527, NOW());

-- 4. 清理旧菜单 (2067 是之前的 ID)
DELETE FROM sys_role_menu WHERE menu_id = 2067;
DELETE FROM sys_menu WHERE menu_id = 2067 OR menu_name = '历年分数线' OR menu_name = '档线信息管理';

-- 5. 插入全新菜单 (使用新的 ID 3000 避免冲突)
-- 目录/菜单
INSERT INTO `sys_menu` VALUES (3000, '历年分数线', 2054, 3, 'provinceScore', 'entrance/provinceScore/index', NULL, 1, 0, 'C', '0', '0', 'entrance:provinceScore:list', 'chart', 'admin', NOW(), '', NULL, '历年分数线查询');

-- 按钮权限
INSERT INTO `sys_menu` VALUES (3001, '分数线查询', 3000, 1, '#', '', '', 1, 0, 'F', '0', '0', 'entrance:provinceScore:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (3002, '分数线新增', 3000, 2, '#', '', '', 1, 0, 'F', '0', '0', 'entrance:provinceScore:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (3003, '分数线修改', 3000, 3, '#', '', '', 1, 0, 'F', '0', '0', 'entrance:provinceScore:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (3004, '分数线删除', 3000, 4, '#', '', '', 1, 0, 'F', '0', '0', 'entrance:provinceScore:remove', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (3005, '分数线导出', 3000, 5, '#', '', '', 1, 0, 'F', '0', '0', 'entrance:provinceScore:export', '#', 'admin', NOW(), '', NULL, '');

-- 6. 角色授权 (管理员 1, 学生 100, 学校管理员 101)
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3000), (1, 3001), (1, 3002), (1, 3003), (1, 3004), (1, 3005);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (100, 3000), (100, 3001);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (101, 3000), (101, 3001), (101, 3002), (101, 3003);
