package com.mock.example.modules.system.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.system.entity.model.SysDept;
import com.mock.example.modules.system.mapper.SysDeptMapper;
import com.mock.example.modules.system.repository.ISysDeptRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Repository
@RequiredArgsConstructor
public class SysDeptRepoImpl
        extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptRepo {

    private final SysDeptMapper mapper;

    @Override
    public List<SysDept> selectDeptList(SysDept dept) {
        return mapper.selectDeptList(dept);
    }

    @Override
    public List<SysDept> selectChildrenDeptById(Long deptId) {
        return mapper.selectChildrenDeptById(deptId);
    }

    @Override
    public List<Integer> selectDeptListByRoleId(Long roleId) {
        return mapper.selectDeptListByRoleId(roleId);
    }

    @Override
    public Boolean checkDeptNameUnique(SysDept dept) {
        List<SysDept> sysDepts = this.list(
                Wrappers.<SysDept>lambdaQuery()
                        .eq(SysDept::getDeptName, dept.getDeptName())
        );

        // 过滤掉自己的deptId,判断是否还有重复
        return CollUtil.isEmpty(
                sysDepts.stream()
                        .filter(sysDept -> !sysDept.getDeptId().equals(dept.getDeptId()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Boolean hasChildByDeptId(Long deptId) {
        int count = this.count(
                Wrappers.<SysDept>lambdaQuery()
                        .eq(SysDept::getParentId,deptId)
                        .eq(SysDept::getDelFlag, "0")
        );

        return BooleanUtils.isTrue(count > 0);
    }

    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        return mapper.selectNormalChildrenDeptById(deptId);
    }

    @Override
    public void updateDeptStatusNormal(Long[] deptIds) {
        this.update(
                Wrappers.<SysDept>lambdaUpdate()
                        .set(SysDept::getStatus,"0")
                        .in(SysDept::getDeptId,deptIds)
        );
    }

    @Override
    public void updateDeptChildren(List<SysDept> depts) {
        mapper.updateDeptChildren(depts);
    }

}
