package com.mock.example.interfaces.vo.system.user;

import com.mock.example.interfaces.vo.system.post.PostVo;
import com.mock.example.interfaces.vo.system.role.RoleVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户详情
 *
 * @author: Mock
 * @date: 2025-01-04 20:41:37
 */
@Data
@Accessors(chain = true)
public class UserDetailVo {

    /**
     * 用户信息
     */
    private UserVo user;

    /**
     * 角色列表
     */
    private List<Integer> roleIds;

    /**
     * 岗位列表
     */
    private List<Integer> postIds;

    /**
     * 角色列表
     */
    private List<RoleVo> roles;

    /**
     * 岗位列表
     */
    private List<PostVo> posts;
}

  