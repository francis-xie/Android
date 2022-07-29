package com.basic.http.interceptor;

import com.basic.http.HttpResponse;
import com.basic.http.utils.AttributeMap;

public interface ResponseInterceptor {

    HttpResponse modifyResponse(InterceptorContext context, AttributeMap attributes);

}
