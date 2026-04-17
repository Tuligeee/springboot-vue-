package com.mock.example.modules.entrance.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.entrance.mapper.CeStudentMapper;
import com.mock.example.modules.entrance.entity.model.CeStudent;
import com.mock.example.modules.entrance.repository.ICeStudentRepo;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 学生信息表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
@Repository
public class CeStudentRepoImpl
        extends ServiceImpl<CeStudentMapper, CeStudent> implements ICeStudentRepo {

    @Override
    public List<CeStudent> selectStudentList(CeStudent student) {
        LambdaQueryWrapper<CeStudent> wrapper = Wrappers.<CeStudent>lambdaQuery()
                //用户id
                .eq(student.getUserId() != null,
                        CeStudent::getUserId, student.getUserId())
                //入学年份
                .eq(student.getEnrollmentYear() != null,
                        CeStudent::getEnrollmentYear, student.getEnrollmentYear())
                //毕业年份
                .eq(student.getGraduateYear() != null,
                        CeStudent::getGraduateYear, student.getGraduateYear())
                //学生编号
                .like(StrUtil.isNotBlank(student.getStudentNo()),
                        CeStudent::getStudentNo, student.getStudentNo())
                //学生姓名
                .like(StrUtil.isNotBlank(student.getStudentName()),
                        CeStudent::getStudentName, student.getStudentName());

        return this.list(wrapper);
    }

    @Override
    public List<CeStudent> selectByStudentNos(List<String> studentNos) {
        if (CollUtil.isEmpty(studentNos)) {
            return Lists.newArrayList();
        }
        return this.list(
                Wrappers.<CeStudent>lambdaQuery()
                        .in(CeStudent::getStudentNo, studentNos)
        );
    }

    @Override
    public CeStudent selectStudentByName(String studentName) {
        return this.getOne(
                Wrappers.<CeStudent>lambdaQuery()
                        .eq(CeStudent::getStudentName, studentName)
                        .last(" limit 1")

        );
    }

    @Override
    public CeStudent selectStudentByNo(String studentNo) {
        return this.getOne(
                Wrappers.<CeStudent>lambdaQuery()
                        .eq(CeStudent::getStudentNo, studentNo)
                        .last(" limit 1")

        );
    }

    @Override
    public CeStudent selectStudentByUserId(Long userId) {
        return this.getOne(
                Wrappers.<CeStudent>lambdaQuery()
                        .eq(CeStudent::getUserId, userId)
                        .last(" limit 1")

        );
    }
}
