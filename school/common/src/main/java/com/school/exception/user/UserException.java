package com.school.exception.user;


import com.school.exception.BaseException;

public class UserException extends BaseException {

    public UserException() {
        super("用户异常");
    }

    public UserException(String msg) {
        super(msg);
    }
}
