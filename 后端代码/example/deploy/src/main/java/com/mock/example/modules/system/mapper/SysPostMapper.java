package com.mock.example.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mock.example.modules.system.entity.model.SysPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 岗位信息表 Mapper 接口
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Mapper
public interface SysPostMapper extends BaseMapper<SysPost> {

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
     * 查询用户岗位列表
     *
     * @param userName 用户名
     * @return 用户岗位列表
     */
    List<SysPost> selectPostsByUserName(String userName);
}
