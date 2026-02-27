package com.mock.example.modules.entrance.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.entrance.entity.model.CeAspiration;

import java.util.List;

/**
 * <p>
 * 志愿表单表 仓库类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-04-30
 */
public interface ICeAspirationRepo extends IService<CeAspiration> {

    /**
     * 查询志愿列表
     *
     * @param aspiration 志愿查询对象
     * @return 志愿列表
     */
    List<CeAspiration> selectAspirationList(CeAspiration aspiration);

    /**
     * 通过学号删除
     *
     * @param studentNo 学号
     */
    void deleteByStudentNo(String studentNo);
}
