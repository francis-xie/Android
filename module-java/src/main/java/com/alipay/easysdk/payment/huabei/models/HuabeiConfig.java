// This file is auto-generated, don't edit it. Thanks.
package com.alipay.easysdk.payment.huabei.models;

import com.basic.http.NameInMap;
import com.basic.http.HttpModel;
import com.basic.http.Validation;

public class HuabeiConfig extends HttpModel {
    @NameInMap("hb_fq_num")
    @Validation(required = true)
    public String hbFqNum;

    @NameInMap("hb_fq_seller_percent")
    @Validation(required = true)
    public String hbFqSellerPercent;

    public static HuabeiConfig build(java.util.Map<String, ?> map) throws Exception {
        HuabeiConfig self = new HuabeiConfig();
        return HttpModel.build(map, self);
    }

    public HuabeiConfig setHbFqNum(String hbFqNum) {
        this.hbFqNum = hbFqNum;
        return this;
    }
    public String getHbFqNum() {
        return this.hbFqNum;
    }

    public HuabeiConfig setHbFqSellerPercent(String hbFqSellerPercent) {
        this.hbFqSellerPercent = hbFqSellerPercent;
        return this;
    }
    public String getHbFqSellerPercent() {
        return this.hbFqSellerPercent;
    }

}
