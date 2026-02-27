package com.mock.example.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * http接口统一返回
 *
 * @author: Mock
 * @date: 2023-02-26 19:39:54
 */
@Data
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private int code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 默认成功返回
     *
     * @return 返回
     */
    public Response ok() {
        this.code = 0;
        this.msg = "返回成功";
        return this;
    }

    /**
     * 成功返回，带消息
     *
     * @param msg 消息内容
     * @return 返回
     */
    public Response okMsg(String msg) {
        this.code = 0;
        this.msg = msg;
        return this;
    }

    /**
     * 默认失败返回
     *
     * @return 返回
     */
    public Response fail() {
        this.code = 1;
        this.msg = "执行失败";
        return this;
    }

    /**
     * 失败返回，带消息
     *
     * @param msg 消息
     * @return 返回
     */
    public Response failMsg(String msg) {
        this.code = 1;
        this.msg = msg;
        return this;
    }

    /**
     * 成功返回，带数据
     *
     * @param data 数据
     * @return 返回
     */
    public Response(T data) {
        ok();
        this.data = data;
    }

    /**
     *
     * 自定义返回
     *
     * @param code 状态码
     * @param msg  消息
     */
    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 自定义返回
     *
     * @param code 状态码
     * @param msg  消息
     * @param data 数据
     */
    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Response() {

    }

}

  