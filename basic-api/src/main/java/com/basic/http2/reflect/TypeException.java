package com.basic.http2.reflect;

/**
 * 类型出错
 */
public class TypeException extends RuntimeException {
    public TypeException() {
    }

    public TypeException(String message) {
        super(message);
    }

    public TypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeException(Throwable cause) {
        super(cause);
    }

}
