package com.basic.http;

import java.util.Map;

public class HttpRetryableException extends HttpException {

    private static final long serialVersionUID = 3883312421128465122L;

    public HttpRetryableException(Throwable cause) {
        super("", cause);
        message = cause.getMessage();
    }

    public HttpRetryableException(Map<String, ?> map) {
        super(map);
    }

    public HttpRetryableException() {
    }
}