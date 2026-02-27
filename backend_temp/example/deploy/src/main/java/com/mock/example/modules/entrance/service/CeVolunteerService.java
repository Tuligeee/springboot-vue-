package com.mock.example.modules.entrance.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.common.exception.BizException;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.entrance.entity.model.CeVolunteer;
import com.mock.example.modules.entrance.mapper.CeVolunteerMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CeVolunteerService extends ServiceImpl<CeVolunteerMapper, CeVolunteer> {

    // 获取当前用户的志愿列表
    public List<CeVolunteer> selectMyList(Long userId) {
        QueryWrapper<CeVolunteer> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        query.orderByDesc("create_time");
        return this.list(query);
    }

    // 添加志愿（包含去重检查）
    public boolean addVolunteer(CeVolunteer volunteer) {
        Long userId = SecurityUtil.getUserId();

        // 1. 构造检查条件
        QueryWrapper<CeVolunteer> check = new QueryWrapper<>();
        check.eq("user_id", userId);
        check.eq("college_id", volunteer.getCollegeId());

        // 【核心修复】必须把“专业ID”也加入判断条件！
        // 只有当 学校ID 和 专业ID 同时一致时，才算是重复
        if (volunteer.getProfessionId() != null) {
            check.eq("profession_id", volunteer.getProfessionId());
        } else {
            // 如果是纯收藏学校（没选专业），则判断库里有没有该学校且专业为空的记录
            check.isNull("profession_id");
        }

        if (this.count(check) > 0) {
            // 提示文案稍微改一下，更准确
            throw new BizException("您已填报过该专业，请勿重复添加！");
        }

        // 2. 补全信息并保存
        volunteer.setUserId(userId);
        volunteer.setCreateTime(new Date());

        return this.save(volunteer);
    }
}
