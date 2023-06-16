package com.coderwang.exception;

/**
 * @Title: ConnectException
 * @Author coderWang
 * @Date 2023/6/16 21:20
 * @description:
 */
public class ConnectException extends RuntimeException{

    public ConnectException() {
    }

    public ConnectException(String message) {
        super(message);
    }

    public ConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectException(Throwable cause) {
        super(cause);
    }

    public ConnectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
