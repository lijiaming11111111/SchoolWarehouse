package com.basketball.exception.user;


import com.basketball.exception.BaseException;

public class UserException extends BaseException {

    public UserException() {
        super("用户异常");
    }

    public UserException(String msg) {
        super(msg);
    }
}
