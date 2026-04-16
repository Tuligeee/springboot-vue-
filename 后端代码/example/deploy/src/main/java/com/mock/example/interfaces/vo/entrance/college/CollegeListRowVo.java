package com.mock.example.interfaces.vo.entrance.college;

import lombok.Data;

/**
 * 院校列表（前台查询页）：不返回院校代码
 */
@Data
public class CollegeListRowVo {

    private Integer id;
    private String collegeName;
    private String city;
    private Integer ranking;
    private Integer personCount;
    private String educationLevel;
}
