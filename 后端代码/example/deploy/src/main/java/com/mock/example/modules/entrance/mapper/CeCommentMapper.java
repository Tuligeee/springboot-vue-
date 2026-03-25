package com.mock.example.modules.entrance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mock.example.modules.entrance.entity.model.CeComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CeCommentMapper extends BaseMapper<CeComment> {
    
    @Select("SELECT c.*, u.nick_name as nickName, u.avatar FROM ce_comment c " +
            "LEFT JOIN sys_user u ON c.user_id = u.user_id " +
            "WHERE c.target_id = #{targetId} AND c.type = #{type} " +
            "ORDER BY c.create_time DESC")
    List<CeComment> selectCommentsWithUser(@Param("targetId") Long targetId, @Param("type") String type);
}
