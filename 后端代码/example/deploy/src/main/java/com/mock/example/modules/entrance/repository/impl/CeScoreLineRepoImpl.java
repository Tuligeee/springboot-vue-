package com.mock.example.modules.entrance.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
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
        return this.list(
                Wrappers.<CeScoreLine>lambdaQuery()
                        .in(CeScoreLine::getCollegeNo, collegeNos)
        );
    }

    @Override
    public List<CeScoreLine> selectScoreLineByKey(String collegeNo, String professionNo) {
        return this.list(
                Wrappers.<CeScoreLine>lambdaQuery()
                        .eq(CeScoreLine::getCollegeNo, collegeNo)
                        .eq(CeScoreLine::getProfessionNo, professionNo)
        );
    }

    @Override
    public List<CeScoreLine> selectScoreLineLeScore(Integer year, Integer score) {
        return this.list(
                Wrappers.<CeScoreLine>lambdaQuery()
                        .eq(CeScoreLine::getYear, year)
                        .le(CeScoreLine::getScore,score)
        );
    }

    @Override
    public Integer getLastYear() {
        return this.getOne(
                Wrappers.<CeScoreLine>lambdaQuery()
                        .orderByDesc(CeScoreLine::getYear)
                        .last("limit 1")
        ).getYear();
    }

    @Override
    public void unBindScoreLines(String professionNo, String collegeNo) {
        this.remove(
                Wrappers.<CeScoreLine>lambdaQuery()
                        .eq(CeScoreLine::getCollegeNo, collegeNo)
                        .eq(CeScoreLine::getProfessionNo, professionNo)
        );
    }

}
