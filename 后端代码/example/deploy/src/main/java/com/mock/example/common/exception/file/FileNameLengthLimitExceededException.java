package com.mock.example.common.exception.file;

/**
 * 文件名称超长限制异常类
 *
 * @author: Mock
 * @date: 2023-02-26 19:39:54
 */
public class FileNameLengthLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("upload.filename.exceed.length", new Object[]{defaultFileNameLength});
    }
}
