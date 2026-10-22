package com.retr0.apispringboot.common;

public class BusinessException extends RuntimeException {
    private final ErrorCode errorcode;
    private final Object data;
    public BusinessException(ErrorCode errorcode,String message){
        this(errorcode,message,null);
    }
    public BusinessException(ErrorCode errorcode,String message, Object data) {
        super(message);
        this.errorcode = errorcode;
        this.data = data;
    }
    public static BusinessException notFound(String message) {
        return new BusinessException(ErrorCode.NOT_FOUND, message);
    }

    public static BusinessException conflict(String message) {
        return new BusinessException(ErrorCode.CONFLICT, message);
    }

    public static BusinessException invalidParam(String message) {
        return new BusinessException(ErrorCode.INVALID_PARAM, message);
    }

    public ErrorCode getErrorcode() {
        return errorcode;
    }

    public Object getData() {
        return data;
    }


}
