package com.mock.example.modules.entrance.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.entrance.entity.model.CeAspirationDetail;

import java.util.List;

/**
 * <p>
 * 志愿明细 仓库类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-04-30
 */
public interface ICeAspirationDetailRepo extends IService<CeAspirationDetail> {

    /**
     * 通过学号删除
     *
     * @param studentNo 学号
     */
    void deleteByStudentNo(String studentNo);

    /**
     * 查询志愿填报详情列表
     *
     * @param studentNo 学号
     * @return 填报详情
     */
    List<CeAspirationDetail> selectAspirationDetailList(String studentNo);
}
