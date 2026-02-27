package com.mock.example.modules.entrance.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.entrance.entity.enums.TagEnum;
import com.mock.example.modules.entrance.entity.model.CeTagRel;

import java.util.List;

/**
 * <p>
 * 标签关联表 仓库类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
public interface ICeTagRelRepo extends IService<CeTagRel> {

    /**
     * 通过标签类型和标签关联id查询
     *
     * @param tagType 标签类型
     * @param relId   标签关联id
     * @return 标签关联列表
     */
    List<CeTagRel> selectTagsByTypeAndRelId(TagEnum tagType, Integer relId);

    /**
     * 通过标签类型和标签关联id列表查询
     *
     * @param tagType 标签类型
     * @param relIds  标签关联id列表
     * @return 标签关联列表
     */
    List<CeTagRel> selectTagsByTypeAndRelIds(TagEnum tagType, List<Integer> relIds);

    /**
     * 通过标签名筛选
     *
     * @param tagEnum 标签类型
     * @param tagNames 标签名称
     * @return 标签关联列表
     */
    List<CeTagRel> selectTagsByNames(TagEnum tagEnum, List<String> tagNames);

    /**
     * 绑定标签
     *
     * @param tagType  标签类型
     * @param relId    标签关联id
     * @param tagNames 标签名称列表
     */
    void bindTags(TagEnum tagType, Integer relId, List<String> tagNames);

    /**
     * 移除标签
     *
     * @param tagType 标签类型
     * @param relId   标签关联id
     */
    void removeTags(TagEnum tagType, Integer relId);

}
