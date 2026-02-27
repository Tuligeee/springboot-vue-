package com.mock.example.interfaces.body.entrance.student;

import com.mock.example.modules.entrance.entity.enums.SexEnum;
import lombok.Data;

/**
 * 学生请求体
 *
 * @author: Mock
 * @date: 2025-01-31 20:07:41
 */
@Data
public class StudentBody {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

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
}

  