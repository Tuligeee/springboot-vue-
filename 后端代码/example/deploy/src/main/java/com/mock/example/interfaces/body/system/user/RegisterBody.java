package com.mock.example.interfaces.body.system.user;

import lombok.Data;

/**
 * 用户注册对象
 *
 * @author: Mock
 * @date: 2023-02-26 19:30:36
 */
@Data
public class RegisterBody {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

}
