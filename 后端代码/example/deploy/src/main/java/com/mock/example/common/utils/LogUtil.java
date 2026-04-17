package com.mock.example.common.utils;

/**
 * 获取i18n资源文件
 *
 * @author: Mock
 * @date: 2022-10-17 23:07:40
 */
public class LogUtil {
    public static String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
