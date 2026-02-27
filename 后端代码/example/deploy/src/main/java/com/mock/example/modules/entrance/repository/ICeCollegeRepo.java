package com.mock.example.modules.entrance.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.entrance.entity.model.CeCollege;

import java.util.List;

/**
 * <p>
 * 院校表 仓库类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
public interface ICeCollegeRepo extends IService<CeCollege> {

    /**
     * 查询院校列表
     *
     * @param college 院系参数
     * @return 院校列表
     */
    List<CeCollege> selectCollegeList(CeCollege college);

    /**
     * 通过院校代码查询院校
     *
     * @param collegeNos 院校代码列表
     * @return 院校列表
     */
    List<CeCollege> selectCollegeListByNos(List<String> collegeNos);

    /**
     * 通过院校编号查询院校
     *
     * @param collegeNo 院校编号
     * @return 院校
     */
    CeCollege selectCollegeByNo(String collegeNo);

}
