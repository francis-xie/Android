package com.basic.http.interceptor;


import com.basic.http.HttpRequest;
import com.basic.http.HttpResponse;
import com.basic.http.utils.AttributeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InterceptorChain implements AutoCloseable {
    private List<RuntimeOptionsInterceptor> runtimeOptionsInterceptors = new ArrayList<>();
    private List<RequestInterceptor> requestInterceptors = new ArrayList<>();
    private List<ResponseInterceptor> responseInterceptors = new ArrayList<>();

    private InterceptorChain() {
    }

    public static InterceptorChain create() {
        return new InterceptorChain();
    }

    public void addRuntimeOptionsInterceptor(RuntimeOptionsInterceptor interceptor) {
        this.runtimeOptionsInterceptors.add(interceptor);
    }

    public void addRequestInterceptor(RequestInterceptor interceptor) {
        this.requestInterceptors.add(interceptor);
    }

    public void addResponseInterceptor(ResponseInterceptor interceptor) {
        this.responseInterceptors.add(interceptor);
    }

    @Override
    public void close() {
        runtimeOptionsInterceptors.clear();
        requestInterceptors.clear();
        responseInterceptors.clear();
    }

    public InterceptorContext modifyRuntimeOptions(InterceptorContext context, AttributeMap attributes) {
        InterceptorContext result = context;
        for (RuntimeOptionsInterceptor interceptor : runtimeOptionsInterceptors) {
            Map<String, Object> interceptorResult = interceptor.modifyRuntimeOptions(result, attributes);
            result.setRuntimeOptions(interceptorResult);
        }
        return result;
    }

    public InterceptorContext modifyRequest(InterceptorContext context, AttributeMap attributes) {
        InterceptorContext result = context;
        for (RequestInterceptor interceptor : requestInterceptors) {
            HttpRequest interceptorResult = interceptor.modifyRequest(result, attributes);
            result.setHttpRequest(interceptorResult);
        }
        return result;
    }

    public InterceptorContext modifyResponse(InterceptorContext context, AttributeMap attributes) {
        InterceptorContext result = context;
        for (ResponseInterceptor interceptor : responseInterceptors) {
            HttpResponse interceptorResult = interceptor.modifyResponse(result, attributes);
            result.setHttpResponse(interceptorResult);
        }
        return result;
    }
}
