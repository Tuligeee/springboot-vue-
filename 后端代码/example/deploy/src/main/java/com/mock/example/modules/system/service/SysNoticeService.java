package com.mock.example.modules.system.service;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.system.entity.model.SysNotice;
import com.mock.example.modules.system.mapper.SysNoticeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysNoticeService extends ServiceImpl<SysNoticeMapper, SysNotice> {

    public List<SysNotice> selectNoticeList(SysNotice notice) {
        QueryWrapper<SysNotice> query = new QueryWrapper<>();
        if (notice.getNoticeTitle() != null && !notice.getNoticeTitle().trim().isEmpty()) {
            query.like("notice_title", notice.getNoticeTitle().trim());
        }
        if (notice.getCreateBy() != null && !notice.getCreateBy().trim().isEmpty()) {
            query.like("create_by", notice.getCreateBy().trim());
        }
        if (notice.getNoticeType() != null && !notice.getNoticeType().trim().isEmpty()) {
            query.eq("notice_type", notice.getNoticeType().trim());
        }
        if (notice.getStatus() != null && !notice.getStatus().trim().isEmpty()) {
            query.eq("status", notice.getStatus().trim());
        }
        query.orderByDesc("create_time");
        return this.list(query);
    }

    public List<SysNotice> selectPublicNoticeList(String title) {
        QueryWrapper<SysNotice> query = new QueryWrapper<>();
        query.eq("status", "0");
        if (title != null && !title.trim().isEmpty()) {
            query.like("notice_title", title.trim());
        }
        query.orderByDesc("create_time");
        return this.list(query);
    }

    public boolean saveNotice(SysNotice notice, String username) {
        notice.setCreateBy(username);
        notice.setCreateTime(DateUtil.date());
        return this.save(notice);
    }

    public boolean updateNotice(SysNotice notice, String username) {
        notice.setUpdateBy(username);
        notice.setUpdateTime(DateUtil.date());
        return this.updateById(notice);
    }
}
