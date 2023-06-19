package com.coderwang.exception;

/**
 * @author 29059
 */
public class ReadConfigException extends RuntimeException{

    public ReadConfigException() {
    }

    public ReadConfigException(String message) {
        super(message);
    }

    public ReadConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadConfigException(Throwable cause) {
        super(cause);
    }

    public ReadConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
