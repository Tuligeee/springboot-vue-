package com.mock.example.interfaces.vo.entrance.college;

import lombok.Data;

/**
 * 院校vo
 *
 * @author: Mock
 * @date: 2025-01-31 20:07:41
 */
@Data
public class CollegeVo {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 院校代码
     */
    private String collegeNo;

    /**
     * 院校名称
     */
    private String collegeName;

    /**
     * 城市
     */
    private String city;

    /**
     * 排名
     */
    private Integer ranking;

    /**
     * 人数
     */
    private Integer personCount;

    /**
     * 详细信息
     */
    private String detailInfo;

}

  