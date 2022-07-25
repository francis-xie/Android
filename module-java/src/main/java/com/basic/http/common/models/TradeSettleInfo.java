// This file is auto-generated, don't edit it. Thanks.
package com.basic.http.common.models;

import com.basic.http.HttpModel;
import com.basic.http.NameInMap;
import com.basic.http.Validation;

public class TradeSettleInfo extends HttpModel {
    @NameInMap("trade_settle_detail_list")
    @Validation(required = true)
    public java.util.List<TradeSettleDetail> tradeSettleDetailList;

    public static TradeSettleInfo build(java.util.Map<String, ?> map) throws Exception {
        TradeSettleInfo self = new TradeSettleInfo();
        return HttpModel.build(map, self);
    }

    public TradeSettleInfo setTradeSettleDetailList(java.util.List<TradeSettleDetail> tradeSettleDetailList) {
        this.tradeSettleDetailList = tradeSettleDetailList;
        return this;
    }
    public java.util.List<TradeSettleDetail> getTradeSettleDetailList() {
        return this.tradeSettleDetailList;
    }

}
