// This file is auto-generated, don't edit it. Thanks.
package com.basic.http.common.models;

import com.basic.http.HttpModel;
import com.basic.http.NameInMap;
import com.basic.http.Validation;

public class TradeCancelResponse extends HttpModel {
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

    @NameInMap("trade_no")
    @Validation(required = true)
    public String tradeNo;

    @NameInMap("out_trade_no")
    @Validation(required = true)
    public String outTradeNo;

    @NameInMap("retry_flag")
    @Validation(required = true)
    public String retryFlag;

    @NameInMap("action")
    @Validation(required = true)
    public String action;

    @NameInMap("gmt_refund_pay")
    @Validation(required = true)
    public String gmtRefundPay;

    @NameInMap("refund_settlement_id")
    @Validation(required = true)
    public String refundSettlementId;

    public static TradeCancelResponse build(java.util.Map<String, ?> map) throws Exception {
        TradeCancelResponse self = new TradeCancelResponse();
        return HttpModel.build(map, self);
    }

    public TradeCancelResponse setHttpBody(String httpBody) {
        this.httpBody = httpBody;
        return this;
    }
    public String getHttpBody() {
        return this.httpBody;
    }

    public TradeCancelResponse setCode(String code) {
        this.code = code;
        return this;
    }
    public String getCode() {
        return this.code;
    }

    public TradeCancelResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    public String getMsg() {
        return this.msg;
    }

    public TradeCancelResponse setSubCode(String subCode) {
        this.subCode = subCode;
        return this;
    }
    public String getSubCode() {
        return this.subCode;
    }

    public TradeCancelResponse setSubMsg(String subMsg) {
        this.subMsg = subMsg;
        return this;
    }
    public String getSubMsg() {
        return this.subMsg;
    }

    public TradeCancelResponse setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
        return this;
    }
    public String getTradeNo() {
        return this.tradeNo;
    }

    public TradeCancelResponse setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }
    public String getOutTradeNo() {
        return this.outTradeNo;
    }

    public TradeCancelResponse setRetryFlag(String retryFlag) {
        this.retryFlag = retryFlag;
        return this;
    }
    public String getRetryFlag() {
        return this.retryFlag;
    }

    public TradeCancelResponse setAction(String action) {
        this.action = action;
        return this;
    }
    public String getAction() {
        return this.action;
    }

    public TradeCancelResponse setGmtRefundPay(String gmtRefundPay) {
        this.gmtRefundPay = gmtRefundPay;
        return this;
    }
    public String getGmtRefundPay() {
        return this.gmtRefundPay;
    }

    public TradeCancelResponse setRefundSettlementId(String refundSettlementId) {
        this.refundSettlementId = refundSettlementId;
        return this;
    }
    public String getRefundSettlementId() {
        return this.refundSettlementId;
    }

}
