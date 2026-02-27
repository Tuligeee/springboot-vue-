package com.mock.example.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.system.entity.model.SysUserRole;

/**
 * <p>
 * 用户和角色关联表 仓库服务类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
public interface ISysUserRoleRepo extends IService<SysUserRole> {

    /**
     * 通过用户id删除角色
     *
     * @param userId 用户id
     */
    void deleteRoleByUserId(Long userId);

    /**
     * 批量取消授权用户角色
     *
     * @param roleId 角色id
     * @param userIds 需删除的用户id
     */
    void deleteUserRoleInfos(Long roleId, Long[] userIds);

    /**
     * 通过角色id查询记录数
     *
     * @param roleId 角色id
     * @return 记录数
     */
    Integer countByRoleId(Long roleId);
}
