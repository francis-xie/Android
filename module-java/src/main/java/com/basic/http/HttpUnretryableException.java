package com.basic.http;

public class HttpUnretryableException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -7006694712718176751L;

    private HttpRequest lastRequest = null;

    public HttpRequest getLastRequest() {
        return lastRequest;
    }

    public HttpUnretryableException(HttpRequest lastRequest, Throwable lastException) {
        super(lastException.getMessage(), lastException);
        this.lastRequest = lastRequest;
    }

    public HttpUnretryableException(HttpRequest lastRequest) {
        this.lastRequest = lastRequest;
    }

    public HttpUnretryableException(Throwable lastException) {
        super(lastException);
    }

    public HttpUnretryableException() {
        super();
    }
}
