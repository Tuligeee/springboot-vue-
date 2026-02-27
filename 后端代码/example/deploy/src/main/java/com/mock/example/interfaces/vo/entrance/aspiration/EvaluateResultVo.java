package com.mock.example.interfaces.vo.entrance.aspiration;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 评估结果
 *
 * @author: Mock
 * @date: 2023-04-05 11:00:59
 */
@Data
@Accessors(chain = true)
public class EvaluateResultVo {

    /**
     * 评估结果
     */
    private List<String> results;

}

  