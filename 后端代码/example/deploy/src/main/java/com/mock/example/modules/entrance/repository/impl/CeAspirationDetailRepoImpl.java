package com.mock.example.modules.entrance.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.entrance.entity.model.CeAspirationDetail;
import com.mock.example.modules.entrance.mapper.CeAspirationDetailMapper;
import com.mock.example.modules.entrance.repository.ICeAspirationDetailRepo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 志愿明细 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-04-30
 */
@Repository
public class CeAspirationDetailRepoImpl
        extends ServiceImpl<CeAspirationDetailMapper, CeAspirationDetail> implements ICeAspirationDetailRepo {

    @Override
    public void deleteByStudentNo(String studentNo) {
        this.remove(
                Wrappers.<CeAspirationDetail>lambdaQuery()
                        .eq(CeAspirationDetail::getStudentNo, studentNo)
        );
    }

    @Override
    public List<CeAspirationDetail> selectAspirationDetailList(String studentNo) {
        return this.list(
                Wrappers.<CeAspirationDetail>lambdaQuery()
                        .eq(CeAspirationDetail::getStudentNo, studentNo)
        );
    }
}
