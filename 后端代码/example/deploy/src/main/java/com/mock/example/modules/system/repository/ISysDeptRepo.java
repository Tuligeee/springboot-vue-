package com.mock.example.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.system.entity.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 部门表 仓库服务类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
public interface ISysDeptRepo extends IService<SysDept> {

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    List<SysDept> selectDeptList(SysDept dept);

    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    List<SysDept> selectChildrenDeptById(Long deptId);

    /**
     * 通过角色id查询部门列表
     *
     * @param roleId 角色id
     * @return 部门列表
     */
    List<Integer> selectDeptListByRoleId(Long roleId);

    /**
     * 查询部门名称是否重复
     *
     * @param sysDept 部门信息
     * @return true 无重复
     */
    Boolean checkDeptNameUnique(SysDept sysDept);

    /**
     * 是否存在部门子节点
     *
     * @param deptId 部门id
     * @return 结果
     */
    Boolean hasChildByDeptId(Long deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    int selectNormalChildrenDeptById(Long deptId);

    /**
     * 更新部门状态为正常
     *
     * @param deptIds 部门id
     */
    void updateDeptStatusNormal(Long[] deptIds);

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    void updateDeptChildren(@Param("depts") List<SysDept> depts);
}
