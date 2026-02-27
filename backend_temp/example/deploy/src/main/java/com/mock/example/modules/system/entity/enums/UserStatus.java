package com.mock.example.modules.system.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统用户状态
 *
 * @author: Mock
 * @date: 2023-02-26 20:32:08
 */
@Getter
@AllArgsConstructor
public enum UserStatus {

    OK("0", "正常"),

    DISABLE("1", "停用"),

    DELETED("2", "删除");

    /**
     * 状态
     */
    private final String code;

    /**
     * 信息
     */
    private final String info;

}
