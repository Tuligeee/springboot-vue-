package com.mock.example.modules.entrance.repository.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.entrance.entity.model.CeAspiration;
import com.mock.example.modules.entrance.mapper.CeAspirationMapper;
import com.mock.example.modules.entrance.repository.ICeAspirationRepo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 志愿表单表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-04-30
 */
@Repository
public class CeAspirationRepoImpl
        extends ServiceImpl<CeAspirationMapper, CeAspiration> implements ICeAspirationRepo {

    @Override
    public List<CeAspiration> selectAspirationList(CeAspiration aspiration) {
        return this.list(Wrappers.<CeAspiration>lambdaQuery()
                .like(
                        StrUtil.isNotBlank(aspiration.getStudentNo()),
                        CeAspiration::getStudentNo, aspiration.getStudentNo()
                )
                .eq(
                        aspiration.getEntranceYear() != null,
                        CeAspiration::getEntranceYear, aspiration.getEntranceYear()
                )
        );
    }

    @Override
    public void deleteByStudentNo(String studentNo) {
        this.remove(
                Wrappers.<CeAspiration>lambdaQuery()
                        .eq(CeAspiration::getStudentNo, studentNo)
        );
    }
}
