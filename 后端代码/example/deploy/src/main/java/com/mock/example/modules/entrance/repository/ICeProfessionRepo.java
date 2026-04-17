package com.mock.example.modules.entrance.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.entrance.entity.model.CeProfession;

import java.util.List;

/**
 * <p>
 * 专业表 仓库类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
public interface ICeProfessionRepo extends IService<CeProfession> {

    /**
     * 通过专业编号好像专业
     *
     * @param professioNo 专业编码
     * @return 专业
     */
    CeProfession selectByProfessionNo(String professionNo);

    /**
     * 查询专业列表
     *
     * @param ceProfession 专业请求参数
     * @return 专业列表
     */
    List<CeProfession> selectProfessionList(CeProfession ceProfession);

    /**
     * 查询专业列表
     *
     * @param professionNos 专业代码列表
     * @return 专业列表
     */
    List<CeProfession> selectProfessionByNos(List<String> professionNos);

    /**
     * 查询专业列表
     *
     * @param professionIds 专业id列表
     * @return 专业列表
     */
    List<CeProfession> selectProfessionByIds(List<Integer> professionIds);
}
