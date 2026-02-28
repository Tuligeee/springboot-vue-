package com.mock.example.modules.entrance.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("ce_collection")
public class CeCollection {
    @TableId(type = IdType.AUTO)
    private Long collectionId;

    private Long userId;
    private Long targetId;
    private Integer targetType;
    private Date createTime;
}
