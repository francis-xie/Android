// This file is auto-generated, don't edit it. Thanks.
package com.alipay.easysdk.payment.app.models;

import com.basic.http.NameInMap;
import com.basic.http.HttpModel;
import com.basic.http.Validation;

public class AlipayTradeAppPayResponse extends HttpModel {
    // 订单信息，字符串形式
    @NameInMap("body")
    @Validation(required = true)
    public String body;

    public static AlipayTradeAppPayResponse build(java.util.Map<String, ?> map) throws Exception {
        AlipayTradeAppPayResponse self = new AlipayTradeAppPayResponse();
        return HttpModel.build(map, self);
    }

    public AlipayTradeAppPayResponse setBody(String body) {
        this.body = body;
        return this;
    }
    public String getBody() {
        return this.body;
    }

}
