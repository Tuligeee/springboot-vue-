package com.mock.example.common.exception;

/**
 * 业务异常
 *
 * @author: Mock
 * @date: 2023-02-26 19:39:54
 */
public final class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public BizException() {
    }

    public BizException(String message) {
        this.message = message;
    }

    public BizException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public BizException setMessage(String message) {
        this.message = message;
        return this;
    }

    public BizException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }
}