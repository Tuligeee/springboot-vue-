package com.mock.example.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mock.example.modules.system.entity.model.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
     SysUser selectUserByUserName(String userName);

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
}
