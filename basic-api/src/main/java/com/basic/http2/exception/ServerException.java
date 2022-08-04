package com.basic.http2.exception;

/**
 * 服务器处理异常
 */
public class ServerException extends RuntimeException {
    private int errCode;
    private String message;

    public ServerException(int errCode, String msg) {
        super(msg);
        this.errCode = errCode;
        this.message = msg;
    }

    public int getErrCode() {
        return errCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}