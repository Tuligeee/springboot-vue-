package com.mock.example.interfaces.body.entrance.aspiration;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 志愿填报请求对象
 *
 * @author: Mock
 * @date: 2023-04-30 08:55:42
 */
@Data
public class AspirationBody {

    /**
     * Id
     */
    private Integer Id;

    /**
     * 学生学号
     */
    private String studentNo;

    /**
     * 填报年份
     */
    private Integer entranceYear;

    /**
     * 学生姓名
     */
    @TableField(exist = false)
    private String studentName;

}

  