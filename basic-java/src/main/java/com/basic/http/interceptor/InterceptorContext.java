package com.basic.http.interceptor;

import com.basic.http.HttpRequest;
import com.basic.http.HttpResponse;

import java.util.Map;

public class InterceptorContext {
    private HttpRequest httpRequest;
    private Map<String, Object> runtimeOptions;
    private HttpResponse httpResponse;

    private InterceptorContext() {
    }

    public static InterceptorContext create() {
        return new InterceptorContext();
    }

    public HttpRequest httpRequest() {
        return httpRequest;
    }

    public Map<String, Object> runtimeOptions() {
        return runtimeOptions;
    }

    public HttpResponse httpResponse() {
        return httpResponse;
    }

    public InterceptorContext setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
        return this;
    }

    public InterceptorContext setRuntimeOptions(Map<String, Object> runtimeOptions) {
        this.runtimeOptions = runtimeOptions;
        return this;
    }

    public InterceptorContext addRuntimeOptions(String key, Object value) {
        this.runtimeOptions.put(key, value);
        return this;
    }

    public InterceptorContext setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
        return this;
    }

    public InterceptorContext copy() {
        return create().setHttpRequest(httpRequest)
                .setRuntimeOptions(runtimeOptions)
                .setHttpResponse(httpResponse);
    }
}
