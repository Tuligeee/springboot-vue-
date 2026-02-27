package com.mock.example.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.system.entity.model.SysPost;

import java.util.List;

/**
 * <p>
 * 岗位信息表 仓库服务类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
public interface ISysPostRepo extends IService<SysPost> {

    /**
     * 获取用户岗位列表
     *
     * @param post 岗位
     * @return 岗位列表
     */
    List<SysPost> selectPostList(SysPost post);

    /**
     * 查询用户岗位ID列表
     *
     * @param userId 用户id
     * @return 岗位id列表
     */
    List<Integer> selectPostListByUserId(Long userId);

    /**
     * 通过用户名查询岗位组
     *
     * @param userName 用户名
     * @return 用户岗位
     */
    String selectUserPostGroup(String userName);

    /**
     * 名称是唯一
     *
     * @param sysPost 岗位信息
     * @return true 是唯一
     */
    Boolean checkPostNameUnique(SysPost sysPost);

    /**
     * 岗位编码是唯一
     *
     * @param sysPost 岗位信息
     * @return true 是唯一
     */
    Boolean checkPostCodeUnique(SysPost sysPost);

    /**
     * 删除岗位
     *
     * @param postIds 岗位id列表
     */
    void deletePostByIds(Long[] postIds);
}
