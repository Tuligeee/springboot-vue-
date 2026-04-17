package com.mock.example.modules.entrance.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
@TableName("ce_volunteer")
public class CeVolunteer {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Long collegeId;
    private String collegeName;

    // 如果还没做专业选择，这两个字段先留空即可
    private Long professionId;
    private String professionName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
