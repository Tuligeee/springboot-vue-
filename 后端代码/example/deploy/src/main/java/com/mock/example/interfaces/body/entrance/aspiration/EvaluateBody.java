package com.mock.example.interfaces.body.entrance.aspiration;

import lombok.Data;

/**
 * 测评请求对象
 *
 * @author: Mock
 * @date: 2023-04-05 10:46:32
 */
@Data
public class EvaluateBody {

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 通过分数评估
     */
    private Boolean byScore;

    /**
     * 通过标签评估
     */
    private Boolean byTag;

}

  