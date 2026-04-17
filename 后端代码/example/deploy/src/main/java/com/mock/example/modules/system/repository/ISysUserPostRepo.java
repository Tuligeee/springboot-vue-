package com.mock.example.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.system.entity.model.SysUserPost;

/**
 * <p>
 * 用户与岗位关联表 仓库服务类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
public interface ISysUserPostRepo extends IService<SysUserPost> {

    /**
     * 通过岗位查询查询
     *
     * @param postId 岗位id
     * @return 数量
     */
    int countPostByPostId(Long postId);

    /**
     * 通过用户id删除岗位
     *
     * @param userId 用户id
     */
    void deletePostByUserId(Long userId);

}
