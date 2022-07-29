// This file is auto-generated, don't edit it. Thanks.
package com.alipay.easysdk.member.identification.models;

import com.basic.http.NameInMap;
import com.basic.http.HttpModel;
import com.basic.http.Validation;

public class MerchantConfig extends HttpModel {
    @NameInMap("return_url")
    @Validation(required = true)
    public String returnUrl;

    public static MerchantConfig build(java.util.Map<String, ?> map) throws Exception {
        MerchantConfig self = new MerchantConfig();
        return HttpModel.build(map, self);
    }

    public MerchantConfig setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
        return this;
    }
    public String getReturnUrl() {
        return this.returnUrl;
    }

}
