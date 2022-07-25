// This file is auto-generated, don't edit it. Thanks.
package com.basic.http.util.generic.models;

import com.basic.http.HttpModel;
import com.basic.http.NameInMap;
import com.basic.http.Validation;

public class OpenApiGenericSDKResponse extends HttpModel {
    // 订单信息，字符串形式
    @NameInMap("body")
    @Validation(required = true)
    public String body;

    public static OpenApiGenericSDKResponse build(java.util.Map<String, ?> map) throws Exception {
        OpenApiGenericSDKResponse self = new OpenApiGenericSDKResponse();
        return HttpModel.build(map, self);
    }

    public OpenApiGenericSDKResponse setBody(String body) {
        this.body = body;
        return this;
    }
    public String getBody() {
        return this.body;
    }

}
