package com.alipay.easysdk.kms.aliyun.models;

import com.basic.http.NameInMap;
import com.basic.http.HttpModel;
import com.basic.http.Validation;

public class AsymmetricSignRequest extends HttpModel {
    @NameInMap("KeyId")
    @Validation(required = true)
    public String keyId;

    @NameInMap("KeyVersionId")
    @Validation(required = true)
    public String keyVersionId;

    @NameInMap("Algorithm")
    @Validation(required = true)
    public String algorithm;

    @NameInMap("Digest")
    @Validation(required = true)
    public String digest;

    public static AsymmetricSignRequest build(java.util.Map<String, ?> map) throws Exception {
        AsymmetricSignRequest self = new AsymmetricSignRequest();
        return HttpModel.build(map, self);
    }
}