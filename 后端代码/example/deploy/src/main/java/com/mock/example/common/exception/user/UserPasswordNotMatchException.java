package com.mock.example.common.exception.user;

/**
 * 用户密码不正确或不符合规范异常类
 *
 * @author: Mock
 * @date: 2023-02-26 19:39:54
 */
public class UserPasswordNotMatchException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException() {
        super("用户密码不正确", null);
    }
}
