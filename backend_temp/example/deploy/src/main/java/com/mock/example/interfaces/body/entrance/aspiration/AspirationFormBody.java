package com.mock.example.interfaces.body.entrance.aspiration;

import lombok.Data;

/**
 * 志愿填报表单
 *
 * @author: Mock
 * @date: 2023-04-30 08:55:42
 */
@Data
public class AspirationFormBody {

    /**
     * 第一志愿
     */
    private String professionNo1;

    /**
     * 第二志愿
     */
    private String professionNo2;

    /**
     * 第三志愿
     */
    private String professionNo3;

}

  