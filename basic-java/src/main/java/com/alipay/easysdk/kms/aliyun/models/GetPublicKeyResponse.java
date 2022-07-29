package com.alipay.easysdk.kms.aliyun.models;

import com.basic.http.NameInMap;
import com.basic.http.HttpModel;
import com.basic.http.Validation;

public class GetPublicKeyResponse extends HttpModel {
    @NameInMap("PublicKey")
    @Validation(required = true)
    public String publicKey;

    @NameInMap("KeyId")
    @Validation(required = true)
    public String keyId;

    @NameInMap("RequestId")
    @Validation(required = true)
    public String requestId;

    @NameInMap("KeyVersionId")
    @Validation(required = true)
    public String keyVersionId;

    public static GetPublicKeyResponse build(java.util.Map<String, ?> map) throws Exception {
        GetPublicKeyResponse self = new GetPublicKeyResponse();
        return HttpModel.build(map, self);
    }
}
