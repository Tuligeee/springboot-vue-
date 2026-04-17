package com.mock.example.modules.entrance.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.entrance.entity.model.CeNews;
import com.mock.example.modules.entrance.mapper.CeNewsMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CeNewsService extends ServiceImpl<CeNewsMapper, CeNews> {

    // 查询列表（支持按标题搜索）
    public List<CeNews> selectNewsList(CeNews news) {
        QueryWrapper<CeNews> query = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(news.getTitle())) {
            query.like("title", news.getTitle());
        }
        if (StringUtils.isNotEmpty(news.getType())) {
            query.eq("type", news.getType());
        }
        query.orderByDesc("create_time");
        return this.list(query);
    }

    // 新增资讯
    public boolean addNews(CeNews news) {
        news.setCreateTime(new Date());
        news.setCreateBy(SecurityUtil.getUsername());
        news.setViewCount(0);
        return this.save(news);
    }

    // 查看详情（阅读数+1）
    public CeNews getNewsById(Long id) {
        CeNews news = this.getById(id);
        if (news != null) {
            news.setViewCount(news.getViewCount() + 1);
            this.updateById(news);
        }
        return news;
    }
}
