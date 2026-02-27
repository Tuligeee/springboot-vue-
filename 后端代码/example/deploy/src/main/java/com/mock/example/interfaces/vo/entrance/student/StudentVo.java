package com.mock.example.interfaces.vo.entrance.student;

import com.mock.example.modules.entrance.entity.enums.SexEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 学生信息vo
 *
 * @author: Mock
 * @date: 2023-04-01 09:31:28
 */
@Data
@Accessors(chain = true)
public class StudentVo {

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 绑定用户名
     */
    private String userName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 入学年份
     */
    private Integer enrollmentYear;

    /**
     * 毕业年份
     */
    private Integer graduateYear;

    /**
     * 性别 MAN-男 WOMEN-女
     */
    private SexEnum sex;

    /**
     * 成绩分数
     */
    private Integer achievement;

    /**
     * 创建时间
     */
    private Date createdTime;

}

  