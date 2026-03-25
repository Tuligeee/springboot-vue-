-- 插入或更新首页轮播图数据
-- 假设表名为 ce_banner，如果不同请根据实际修改
DELETE FROM ce_banner;
INSERT INTO ce_banner (title, img_url, link_url, sort, status, created_time) VALUES 
('圆梦名校', '/img/banner/banner1.svg', '/college/college', 1, '0', NOW()),
('科学填报', '/img/banner/banner2.svg', '/aspiration/my-volunteer', 2, '0', NOW());
