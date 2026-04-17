package com.mock.example.common.exception.user;


import com.mock.example.common.exception.BaseException;

/**
 * 用户信息异常
 *
 * @author: Mock
 * @date: 2023-02-26 19:39:54
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
