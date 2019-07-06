package com.jfinal.club.common.exception;

/**
 * 系统错误
 * Created by xukaiwei on 2019/03/24.
 */
public class SystemException extends  RuntimeException{
    private static final long serialVersionUID = 1L;

    public SystemException(String message){
        super(message);
    }

    public SystemException(String message , Throwable e){
        super(message, e);
    }
}
