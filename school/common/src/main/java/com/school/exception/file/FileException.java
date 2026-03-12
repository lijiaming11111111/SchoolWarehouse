package com.school.exception.file;


import com.school.exception.BaseException;

public class FileException extends BaseException {

    public FileException() {
        super("文件服务异常");
    }

    public FileException(String message) {
        super(message);
    }
}

