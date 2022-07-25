package com.basic.http.interceptor;

import com.basic.http.HttpRequest;
import com.basic.http.utils.AttributeMap;

public interface RequestInterceptor {

    HttpRequest modifyRequest(InterceptorContext context, AttributeMap attributes);

}
