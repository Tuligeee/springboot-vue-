package com.mock.example.modules.entrance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.entrance.model.CeProvinceScore;
import com.mock.example.modules.entrance.repository.CeProvinceScoreMapper;
import com.mock.example.modules.entrance.service.ICeProvinceScoreService;
import org.springframework.stereotype.Service;

/**
 * 历年分数线Service业务层处理
 * 
 * @author mock
 * @date 2026-03-25
 */
@Service
public class CeProvinceScoreServiceImpl extends ServiceImpl<CeProvinceScoreMapper, CeProvinceScore> implements ICeProvinceScoreService {
}
