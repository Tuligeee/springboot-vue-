package com.mock.example.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mock.example.modules.system.entity.model.SysNotice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysNoticeMapper extends BaseMapper<SysNotice> {
}
