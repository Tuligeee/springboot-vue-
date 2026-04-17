package com.mock.example.modules.entrance.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.entrance.entity.model.CeScoreLine;
import com.mock.example.modules.entrance.mapper.CeScoreLineMapper;
import com.mock.example.modules.entrance.repository.ICeScoreLineRepo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 分数线表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
@Repository
public class CeScoreLineRepoImpl
        extends ServiceImpl<CeScoreLineMapper, CeScoreLine> implements ICeScoreLineRepo {

    @Override
    public List<CeScoreLine> selectScoreLineByCollegeNos(List<String> collegeNos) {
        if (CollUtil.isEmpty(collegeNos)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<CeScoreLine> query = Wrappers.<CeScoreLine>lambdaQuery()
                .in(CeScoreLine::getCollegeNo, collegeNos);

        // Security Isolation
        if (SecurityUtil.getLoginUser() != null && SecurityUtil.getLoginUser().getUser() != null) {
            boolean isSchoolAdmin = SecurityUtil.getLoginUser().getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                Long myCollegeId = SecurityUtil.getLoginUser().getUser().getCollegeId();
                if (myCollegeId != null) {
                    query.inSql(CeScoreLine::getCollegeNo,
                            "SELECT college_no FROM ce_college WHERE id = " + myCollegeId);
                } else {
                    query.eq(CeScoreLine::getCollegeNo, "NO_COLLEGE_ASSIGNED");
                }
            }
        }
        return this.list(query);
    }

    @Override
    public List<CeScoreLine> selectScoreLineByKey(String collegeNo, String professionNo) {
        LambdaQueryWrapper<CeScoreLine> query = Wrappers.<CeScoreLine>lambdaQuery()
                .eq(CeScoreLine::getCollegeNo, collegeNo)
                .eq(CeScoreLine::getProfessionNo, professionNo);

        // Security Isolation
        if (SecurityUtil.getLoginUser() != null && SecurityUtil.getLoginUser().getUser() != null) {
            boolean isSchoolAdmin = SecurityUtil.getLoginUser().getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                Long myCollegeId = SecurityUtil.getLoginUser().getUser().getCollegeId();
                if (myCollegeId != null) {
                    query.inSql(CeScoreLine::getCollegeNo,
                            "SELECT college_no FROM ce_college WHERE id = " + myCollegeId);
                } else {
                    query.eq(CeScoreLine::getCollegeNo, "NO_COLLEGE_ASSIGNED");
                }
            }
        }
        return this.list(query);
    }

    @Override
    public List<CeScoreLine> selectScoreLineLeScore(Integer year, Integer score) {
        LambdaQueryWrapper<CeScoreLine> query = Wrappers.<CeScoreLine>lambdaQuery()
                .eq(CeScoreLine::getYear, year)
                .le(CeScoreLine::getScore, score);

        // Security Isolation
        if (SecurityUtil.getLoginUser() != null && SecurityUtil.getLoginUser().getUser() != null) {
            boolean isSchoolAdmin = SecurityUtil.getLoginUser().getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                Long myCollegeId = SecurityUtil.getLoginUser().getUser().getCollegeId();
                if (myCollegeId != null) {
                    query.inSql(CeScoreLine::getCollegeNo,
                            "SELECT college_no FROM ce_college WHERE id = " + myCollegeId);
                } else {
                    query.eq(CeScoreLine::getCollegeNo, "NO_COLLEGE_ASSIGNED");
                }
            }
        }
        return this.list(query);
    }

    @Override
    public Integer getLastYear() {
        return this.getOne(
                Wrappers.<CeScoreLine>lambdaQuery()
                        .orderByDesc(CeScoreLine::getYear)
                        .last("limit 1"))
                .getYear();
    }

    @Override
    public void unBindScoreLines(String professionNo, String collegeNo) {
        this.remove(
                Wrappers.<CeScoreLine>lambdaQuery()
                        .eq(CeScoreLine::getCollegeNo, collegeNo)
                        .eq(CeScoreLine::getProfessionNo, professionNo));
    }

}
