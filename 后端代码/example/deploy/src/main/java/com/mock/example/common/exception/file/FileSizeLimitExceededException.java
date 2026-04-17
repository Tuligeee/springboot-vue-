package com.mock.example.common.exception.file;

/**
 * 文件名大小限制异常类
 *
 * @author: Mock
 * @date: 2023-02-26 19:39:54
 */
public class FileSizeLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("upload.exceed.maxSize", new Object[]{defaultMaxSize});
    }
}
