package com.smartNotes.exception.user;


import com.smartNotes.exception.BaseException;

public class UserException extends BaseException {

    public UserException() {
        super("用户异常");
    }

    public UserException(String msg) {
        super(msg);
    }
}
