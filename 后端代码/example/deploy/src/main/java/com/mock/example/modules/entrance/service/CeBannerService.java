package com.mock.example.modules.entrance.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.entrance.entity.model.CeBanner;
import com.mock.example.modules.entrance.mapper.CeBannerMapper;
import org.springframework.stereotype.Service;

@Service
public class CeBannerService extends ServiceImpl<CeBannerMapper, CeBanner> {
}
