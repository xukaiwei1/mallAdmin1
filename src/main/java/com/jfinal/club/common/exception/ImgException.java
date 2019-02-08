package com.jfinal.club.common.exception;

/**
 * Created by xukaiwei on 2019/2/7.
 */
public class ImgException extends RuntimeException {

    public ImgException() {
        super();
    }

    public ImgException(String message) {
        super(message);
    }


    public ImgException(String message, Throwable cause) {
        super(message, cause);
    }
}