package com.smartNotes.exception.file;


import com.smartNotes.exception.BaseException;

public class FileException extends BaseException {

    public FileException() {
        super("文件服务异常");
    }

    public FileException(String message) {
        super(message);
    }
}

