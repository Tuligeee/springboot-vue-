package com.mock.example.modules.entrance.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.entrance.entity.model.CeStudent;

import java.util.List;

/**
 * <p>
 * 学生信息表 仓库类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
public interface ICeStudentRepo extends IService<CeStudent> {

    /**
     * 查询学生列表
     *
     * @param student 学生对象
     * @return 学生列表
     */
    List<CeStudent> selectStudentList(CeStudent student);

    /**
     * 通过学生编号列表查询
     *
     * @param studentNos 学生编号列表
     * @return 学生列表
     */
    List<CeStudent> selectByStudentNos(List<String> studentNos);

    /**
     * 通过学生姓名查询
     *
     * @param studentName 学生姓名
     * @return 学生信息
     */
    CeStudent selectStudentByName(String studentName);

    /**
     * 通过学生编号查询
     *
     * @param studentNo 学生编号
     * @return 学生信息
     */
    CeStudent selectStudentByNo(String studentNo);

    /**
     * 通过用户id查询
     *
     * @param userId 用户账号id
     * @return 学生信息
     */
    CeStudent selectStudentByUserId(Long userId);
}
