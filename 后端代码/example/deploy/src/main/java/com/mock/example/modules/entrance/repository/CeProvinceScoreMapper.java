package com.mock.example.modules.entrance.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mock.example.modules.entrance.model.CeProvinceScore;
import org.apache.ibatis.annotations.Mapper;

/**
 * 历年分数线Mapper接口
 * 
 * @author mock
 * @date 2026-03-25
 */
@Mapper
public interface CeProvinceScoreMapper extends BaseMapper<CeProvinceScore> {
}
