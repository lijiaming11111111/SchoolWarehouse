package com.basketball.exception.stadium;

public class StadiumException extends RuntimeException {
    public StadiumException() {
        super("场馆异常");
    }
    public StadiumException(String message) {
        super(message);
    }
}
