package com.alipay.easysdk.kms.aliyun.models;

import com.basic.http.NameInMap;
import com.basic.http.HttpModel;
import com.basic.http.Validation;

public class GetPublicKeyRequest extends HttpModel {
    @NameInMap("KeyId")
    @Validation(required = true)
    public String keyId;

    @NameInMap("KeyVersionId")
    @Validation(required = true)
    public String keyVersionId;

    public static GetPublicKeyRequest build(java.util.Map<String, ?> map) throws Exception {
        GetPublicKeyRequest self = new GetPublicKeyRequest();
        return HttpModel.build(map, self);
    }
}
