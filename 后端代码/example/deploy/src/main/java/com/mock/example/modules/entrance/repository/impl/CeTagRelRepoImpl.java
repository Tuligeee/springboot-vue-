package com.mock.example.modules.entrance.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.entrance.entity.enums.TagEnum;
import com.mock.example.modules.entrance.mapper.CeTagRelMapper;
import com.mock.example.modules.entrance.entity.model.CeTagRel;
import com.mock.example.modules.entrance.repository.ICeTagRelRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 标签关联表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
@Repository
public class CeTagRelRepoImpl
        extends ServiceImpl<CeTagRelMapper, CeTagRel> implements ICeTagRelRepo {

    @Override
    public List<CeTagRel> selectTagsByTypeAndRelId(TagEnum tagType, Integer relId) {
        return this.list(
                Wrappers.<CeTagRel>lambdaQuery()
                        .eq(CeTagRel::getRelId, relId)
                        .eq(CeTagRel::getTagType, tagType)
        );
    }

    @Override
    public List<CeTagRel> selectTagsByTypeAndRelIds(TagEnum tagType, List<Integer> relIds) {
        if (CollUtil.isEmpty(relIds)) {
            return Lists.newArrayList();
        }
        return this.list(
                Wrappers.<CeTagRel>lambdaQuery()
                        .in(CeTagRel::getRelId, relIds)
                        .eq(CeTagRel::getTagType, tagType)
        );
    }

    @Override
    public List<CeTagRel> selectTagsByNames(TagEnum tagEnum, List<String> tagNames) {
        if(CollUtil.isEmpty(tagNames)){
            return Lists.newArrayList();
        }
        return this.list(
                Wrappers.<CeTagRel>lambdaQuery()
                        .eq(CeTagRel::getTagType,tagEnum)
                        .in(CeTagRel::getTagName, tagNames)
        );
    }

    @Override
    public void bindTags(TagEnum tagType, Integer relId, List<String> tagNames) {
        if (CollUtil.isEmpty(tagNames)) {
            return;
        }
        List<CeTagRel> ceTagRelList = tagNames.stream()
                .map(
                        tagName -> new CeTagRel()
                                .setRelId(relId)
                                .setTagType(tagType)
                                .setTagName(tagName)
                                .setCreatedUser(SecurityUtil.getUsername())
                ).collect(Collectors.toList());
        this.saveBatch(ceTagRelList);
    }

    @Override
    public void removeTags(TagEnum tagType, Integer relId) {
        this.remove(
                Wrappers.<CeTagRel>lambdaQuery()
                        .eq(CeTagRel::getRelId, relId)
                        .eq(CeTagRel::getTagType, tagType)
        );
    }
}
