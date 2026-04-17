package com.mock.example.common.exception.file;


import com.mock.example.common.exception.BaseException;

/**
 * 文件信息异常类
 *
 * @author: Mock
 * @date: 2023-02-26 19:39:54
 */
public class FileException extends BaseException {
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }

}
