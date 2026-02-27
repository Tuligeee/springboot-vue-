package com.mock.example.common.exception;

/**
 * 工具类异常
 *
 * @author: Mock
 * @date: 2023-02-26 19:39:54
 */
public class UtilException extends RuntimeException {

    private static final long serialVersionUID = 8247610319171014183L;

    public UtilException(Throwable e) {
        super(e.getMessage(), e);
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
