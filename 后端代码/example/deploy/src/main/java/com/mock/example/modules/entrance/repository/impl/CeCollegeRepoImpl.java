package com.mock.example.modules.entrance.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mock.example.modules.entrance.entity.model.CeCollege;
import com.mock.example.modules.entrance.mapper.CeCollegeMapper;
import com.mock.example.modules.entrance.repository.ICeCollegeRepo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 院校表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
@Repository
public class CeCollegeRepoImpl
        extends ServiceImpl<CeCollegeMapper, CeCollege> implements ICeCollegeRepo {

    @Override
    public List<CeCollege> selectCollegeList(CeCollege college) {
        LambdaQueryWrapper<CeCollege> wrapper = Wrappers.<CeCollege>lambdaQuery()
                .like(StrUtil.isNotBlank(college.getCollegeNo()),
                        CeCollege::getCollegeNo, college.getCollegeNo())
                .like(StrUtil.isNotBlank(college.getCollegeName()),
                        CeCollege::getCollegeName, college.getCollegeName())
                .like(StrUtil.isNotBlank(college.getCity()),
                        CeCollege::getCity, college.getCity());

        return this.list(wrapper);
    }

    @Override
    public List<CeCollege> selectCollegeListByNos(List<String> collegeNos) {
        if(CollUtil.isEmpty(collegeNos)){
            return Lists.newArrayList();
        }
        return this.list(
                Wrappers.<CeCollege>lambdaQuery()
                        .in(CeCollege::getCollegeNo, collegeNos)
        );
    }

    @Override
    public CeCollege selectCollegeByNo(String collegeNo) {
        return this.getOne(
                Wrappers.<CeCollege>lambdaQuery()
                        .eq(CeCollege::getCollegeNo, collegeNo)
                        .last(" limit 1")
        );
    }

}
