package com.mock.example.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.system.entity.model.SysUser;

import java.util.List;

/**
 * <p>
 * 用户信息表 仓库服务类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
public interface ISysUserRepo extends IService<SysUser> {

    /**
     * 通过用户id查询用户
     *
     * @param userId 用户id
     * @return 用户
     */
    SysUser selectByUserId(Long userId);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户
     */
    SysUser selectUserByUserName(String userName);

    /**
     * 通过用户id列表查询
     *
     * @param userIds 用户id列表
     * @return 用户列表
     */
    List<SysUser> selectUserByUserIds(List<Long> userIds);

    /**
     * 通过条件查询用户列表
     *
     * @param user 用户条件
     * @return 用户列表
     */
    List<SysUser> selectUserList(SysUser user);

    /**
     * 根据条件分页查询未已配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectAllocatedList(SysUser user);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    Boolean checkUserNameUnique(String userName);

    /**
     * 检查部门下是否存在用户
     *
     * @param deptId 部门id
     * @return true 存在用户
     */
    Boolean checkDeptExistUser(Long deptId);
    /**
     * 修改用户状态
     *
     * @param userId 用户id
     * @param status 用户状态
     */
    void updateUserStatus(Long userId, String status);

    /**
     * 修改用户密码
     *
     * @param userId   用户id
     * @param password 用户密码（已经加密过的）
     */
    void updatePassword(Long userId, String password);

    /**
     * 更新用户头像
     *
     * @param userId 用户id
     * @param avatar 用户头像
     */
    void updateAvatar(Long userId, String avatar);
}
