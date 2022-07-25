package com.alipay.easysdk.kms.aliyun.models;

import com.basic.http.NameInMap;
import com.basic.http.HttpModel;
import com.basic.http.Validation;

public class AsymmetricSignResponse extends HttpModel {
    @NameInMap("Value")
    @Validation(required = true)
    public String value;

    @NameInMap("KeyId")
    @Validation(required = true)
    public String keyId;

    @NameInMap("RequestId")
    @Validation(required = true)
    public String requestId;

    @NameInMap("KeyVersionId")
    @Validation(required = true)
    public String keyVersionId;

    public static AsymmetricSignResponse build(java.util.Map<String, ?> map) throws Exception {
        AsymmetricSignResponse self = new AsymmetricSignResponse();
        return HttpModel.build(map, self);
    }
}