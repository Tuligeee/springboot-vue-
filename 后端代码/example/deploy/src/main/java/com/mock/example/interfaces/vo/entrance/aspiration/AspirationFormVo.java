package com.mock.example.interfaces.vo.entrance.aspiration;

import lombok.Data;

import java.util.List;

/**
 * 志愿填报Vo
 *
 * @author: Mock
 * @date: 2023-04-30 11:04:54
 */
@Data
public class AspirationFormVo {

    /**
     * 第一志愿专业
     */
    private String professionNo1;

    /**
     * 第二志愿专业
     */
    private String professionNo2;

    /**
     * 第三志愿专业
     */
    private String professionNo3;

    /**
     * 筛选条件
     */
    private List<AspirationSelectVo> items;
}

  