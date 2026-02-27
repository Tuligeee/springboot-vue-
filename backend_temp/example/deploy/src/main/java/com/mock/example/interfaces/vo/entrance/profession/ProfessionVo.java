package com.mock.example.interfaces.vo.entrance.profession;

import lombok.Data;

import java.util.Date;

/**
 * 专业请求对象body
 *
 * @author: Mock
 * @date: 2023-04-01 20:57:31
 */
@Data
public class ProfessionVo {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 专业代码
     */
    private String professionNo;

    /**
     * 专业名称
     */
    private String professionName;

    /**
     * 所属院校代码
     */
    private String collegeNo;

    /**
     * 所属专业名称
     */
    private String collegeName;

    /**
     * 修业年限
     */
    private Integer studyYear;

    /**
     * 详情
     */
    private String detailInfo;

    /**
     * 分数线 (组装后文案)
     */
    private String scoreLineText;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 创建人
     */
    private String createdUser;

}

  