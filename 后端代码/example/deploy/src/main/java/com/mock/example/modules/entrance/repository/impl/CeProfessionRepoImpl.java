package com.mock.example.modules.entrance.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.entrance.entity.model.CeProfession;
import com.mock.example.modules.entrance.mapper.CeProfessionMapper;
import com.mock.example.modules.entrance.repository.ICeProfessionRepo;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 专业表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
@Repository
public class CeProfessionRepoImpl
        extends ServiceImpl<CeProfessionMapper, CeProfession> implements ICeProfessionRepo {

    @Override
    public CeProfession selectByProfessionNo(String professionNo) {
        return this.getOne(
                Wrappers.<CeProfession>lambdaQuery()
                        .eq(CeProfession::getProfessionNo, professionNo)
        );
    }

    @Override
    public List<CeProfession> selectProfessionList(CeProfession ceProfession) {
        LambdaQueryWrapper<CeProfession> wrapper = Wrappers.<CeProfession>lambdaQuery()
                .like(StrUtil.isNotBlank(ceProfession.getProfessionNo()),
                        CeProfession::getProfessionNo, ceProfession.getProfessionNo())
                .like(StrUtil.isNotBlank(ceProfession.getProfessionName()),
                        CeProfession::getProfessionName, ceProfession.getProfessionName())
                .like(StrUtil.isNotBlank(ceProfession.getCollegeNo()),
                        CeProfession::getCollegeNo, ceProfession.getCollegeNo());

        return this.list(wrapper);
    }

    @Override
    public List<CeProfession> selectProfessionByNos(List<String> professionNos) {
        if (CollUtil.isEmpty(professionNos)) {
            return Lists.newArrayList();
        }
        return this.list(
                Wrappers.<CeProfession>lambdaQuery()
                        .in(CeProfession::getProfessionNo, professionNos)
        );
    }

    @Override
    public List<CeProfession> selectProfessionByIds(List<Integer> professionIds) {
        if (CollUtil.isEmpty(professionIds)) {
            return Lists.newArrayList();
        }
        return this.list(
                Wrappers.<CeProfession>lambdaQuery()
                        .in(CeProfession::getId, professionIds)
        );
    }
}
