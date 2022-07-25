// This file is auto-generated, don't edit it. Thanks.
package com.basic.http.util.generic.models;

import com.basic.http.HttpModel;
import com.basic.http.NameInMap;
import com.basic.http.Validation;

public class OpenApiGenericResponse extends HttpModel {
    // 响应原始字符串
    @NameInMap("http_body")
    @Validation(required = true)
    public String httpBody;

    @NameInMap("code")
    @Validation(required = true)
    public String code;

    @NameInMap("msg")
    @Validation(required = true)
    public String msg;

    @NameInMap("sub_code")
    @Validation(required = true)
    public String subCode;

    @NameInMap("sub_msg")
    @Validation(required = true)
    public String subMsg;

    public static OpenApiGenericResponse build(java.util.Map<String, ?> map) throws Exception {
        OpenApiGenericResponse self = new OpenApiGenericResponse();
        return HttpModel.build(map, self);
    }

    public OpenApiGenericResponse setHttpBody(String httpBody) {
        this.httpBody = httpBody;
        return this;
    }
    public String getHttpBody() {
        return this.httpBody;
    }

    public OpenApiGenericResponse setCode(String code) {
        this.code = code;
        return this;
    }
    public String getCode() {
        return this.code;
    }

    public OpenApiGenericResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    public String getMsg() {
        return this.msg;
    }

    public OpenApiGenericResponse setSubCode(String subCode) {
        this.subCode = subCode;
        return this;
    }
    public String getSubCode() {
        return this.subCode;
    }

    public OpenApiGenericResponse setSubMsg(String subMsg) {
        this.subMsg = subMsg;
        return this;
    }
    public String getSubMsg() {
        return this.subMsg;
    }

}
