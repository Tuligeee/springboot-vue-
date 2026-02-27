package com.mock.example.interfaces.vo.entrance.aspiration;

import lombok.Data;

import java.util.List;

/**
 * 志愿填报选项Vo
 *
 * @author: Mock
 * @date: 2023-04-30 11:04:54
 */
@Data
public class AspirationSelectVo {

    /**
     * 标签名
     */
    private String label;

    /**
     * 值
     */
    private String value;

    /**
     * 子对象
     */
    private List<AspirationSelectVo> children;
}

  