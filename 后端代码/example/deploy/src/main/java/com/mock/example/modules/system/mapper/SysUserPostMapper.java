package com.mock.example.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mock.example.modules.system.entity.model.SysUserPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户与岗位关联表 Mapper 接口
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Mapper
public interface SysUserPostMapper extends BaseMapper<SysUserPost> {

}
