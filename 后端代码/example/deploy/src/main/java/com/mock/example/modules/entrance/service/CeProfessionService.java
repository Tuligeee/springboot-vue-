package com.mock.example.modules.entrance.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.entrance.entity.model.CeProfession;
import com.mock.example.modules.entrance.mapper.CeProfessionMapper;
import org.springframework.stereotype.Service;

/**
 * 专业 Service 业务层处理
 * 关键点：必须继承 ServiceImpl<Mapper, Entity> 才能使用 list(query)
 */
@Service
public class CeProfessionService extends ServiceImpl<CeProfessionMapper, CeProfession> {

}
