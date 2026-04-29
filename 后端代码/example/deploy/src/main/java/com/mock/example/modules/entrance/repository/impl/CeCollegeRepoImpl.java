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
import java.util.stream.Collectors;

import com.mock.example.common.utils.SecurityUtil;

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
                .like(StrUtil.isNotBlank(college.getCollegeName()),
                        CeCollege::getCollegeName, college.getCollegeName())
                .like(StrUtil.isNotBlank(college.getCity()),
                        CeCollege::getCity, college.getCity())
                .eq(StrUtil.isNotBlank(college.getEducationLevel()),
                        CeCollege::getEducationLevel, college.getEducationLevel());

        // 数据隔离
        if (SecurityUtil.isRestrictedSchoolAdmin()) {
            Long myCollegeId = SecurityUtil.getLoginUser().getUser().getCollegeId();
            if (myCollegeId != null) {
                wrapper.eq(CeCollege::getId, myCollegeId);
            } else {
                wrapper.eq(CeCollege::getId, -1); // 无权查看
            }
        }

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

    @Override
    public List<String> selectUniqueEducationLevels() {
        return this.baseMapper.selectObjs(
                Wrappers.<CeCollege>query().select("distinct education_level")
                        .isNotNull("education_level")
                        .ne("education_level", "")
        ).stream().filter(o -> o != null).map(Object::toString).sorted().collect(Collectors.toList());
    }

}
