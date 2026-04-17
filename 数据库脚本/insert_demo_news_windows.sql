-- 插入演示资讯数据 (SVG图片版)
USE ry;
DELETE FROM ce_news;
INSERT INTO ce_news (title, type, cover_img, summary, content, view_count, create_time, del_flag) VALUES 
('2026年湖北省普通高校招生考试报名工作通知', '1', '/img/news/policy.svg', '湖北省2026年高考报名将于10月中旬正式开启...', '正文内容', 1250, NOW(), '0'),
('专家解读：如何根据位次科学选择院校专业组', '2', '/img/news/guide.svg', '在新高考改革下，位次比分数更具有参考价值...', '正文内容', 2840, NOW(), '0'),
('武汉大学2026年拟新增多个人工智能交叉学科专业', '3', '/img/news/whu.svg', '为响应国家战略，武汉大学计划在明年新增三个跨学科专业...', '正文内容', 4560, NOW(), '0');
