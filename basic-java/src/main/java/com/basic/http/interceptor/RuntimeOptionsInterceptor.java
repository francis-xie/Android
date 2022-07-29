package com.basic.http.interceptor;

import com.basic.http.utils.AttributeMap;

import java.util.Map;

public interface RuntimeOptionsInterceptor {

    Map<String, Object> modifyRuntimeOptions(InterceptorContext context, AttributeMap attributes);

}
