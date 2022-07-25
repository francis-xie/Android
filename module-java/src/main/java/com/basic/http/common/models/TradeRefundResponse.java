// This file is auto-generated, don't edit it. Thanks.
package com.basic.http.common.models;

import com.basic.http.HttpModel;
import com.basic.http.NameInMap;
import com.basic.http.Validation;

public class TradeRefundResponse extends HttpModel {
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

    @NameInMap("buyer_logon_id")
    @Validation(required = true)
    public String buyerLogonId;

    @NameInMap("fund_change")
    @Validation(required = true)
    public String fundChange;

    @NameInMap("refund_fee")
    @Validation(required = true)
    public String refundFee;

    @NameInMap("refund_currency")
    @Validation(required = true)
    public String refundCurrency;

    @NameInMap("gmt_refund_pay")
    @Validation(required = true)
    public String gmtRefundPay;

    @NameInMap("refund_detail_item_list")
    @Validation(required = true)
    public java.util.List<TradeFundBill> refundDetailItemList;

    @NameInMap("store_name")
    @Validation(required = true)
    public String storeName;

    @NameInMap("buyer_user_id")
    @Validation(required = true)
    public String buyerUserId;

    @NameInMap("refund_preset_paytool_list")
    @Validation(required = true)
    public java.util.List<PresetPayToolInfo> refundPresetPaytoolList;

    @NameInMap("refund_settlement_id")
    @Validation(required = true)
    public String refundSettlementId;

    @NameInMap("present_refund_buyer_amount")
    @Validation(required = true)
    public String presentRefundBuyerAmount;

    @NameInMap("present_refund_discount_amount")
    @Validation(required = true)
    public String presentRefundDiscountAmount;

    @NameInMap("present_refund_mdiscount_amount")
    @Validation(required = true)
    public String presentRefundMdiscountAmount;

    public static TradeRefundResponse build(java.util.Map<String, ?> map) throws Exception {
        TradeRefundResponse self = new TradeRefundResponse();
        return HttpModel.build(map, self);
    }

    public TradeRefundResponse setHttpBody(String httpBody) {
        this.httpBody = httpBody;
        return this;
    }
    public String getHttpBody() {
        return this.httpBody;
    }

    public TradeRefundResponse setCode(String code) {
        this.code = code;
        return this;
    }
    public String getCode() {
        return this.code;
    }

    public TradeRefundResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    public String getMsg() {
        return this.msg;
    }

    public TradeRefundResponse setSubCode(String subCode) {
        this.subCode = subCode;
        return this;
    }
    public String getSubCode() {
        return this.subCode;
    }

    public TradeRefundResponse setSubMsg(String subMsg) {
        this.subMsg = subMsg;
        return this;
    }
    public String getSubMsg() {
        return this.subMsg;
    }

    public TradeRefundResponse setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
        return this;
    }
    public String getTradeNo() {
        return this.tradeNo;
    }

    public TradeRefundResponse setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }
    public String getOutTradeNo() {
        return this.outTradeNo;
    }

    public TradeRefundResponse setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
        return this;
    }
    public String getBuyerLogonId() {
        return this.buyerLogonId;
    }

    public TradeRefundResponse setFundChange(String fundChange) {
        this.fundChange = fundChange;
        return this;
    }
    public String getFundChange() {
        return this.fundChange;
    }

    public TradeRefundResponse setRefundFee(String refundFee) {
        this.refundFee = refundFee;
        return this;
    }
    public String getRefundFee() {
        return this.refundFee;
    }

    public TradeRefundResponse setRefundCurrency(String refundCurrency) {
        this.refundCurrency = refundCurrency;
        return this;
    }
    public String getRefundCurrency() {
        return this.refundCurrency;
    }

    public TradeRefundResponse setGmtRefundPay(String gmtRefundPay) {
        this.gmtRefundPay = gmtRefundPay;
        return this;
    }
    public String getGmtRefundPay() {
        return this.gmtRefundPay;
    }

    public TradeRefundResponse setRefundDetailItemList(java.util.List<TradeFundBill> refundDetailItemList) {
        this.refundDetailItemList = refundDetailItemList;
        return this;
    }
    public java.util.List<TradeFundBill> getRefundDetailItemList() {
        return this.refundDetailItemList;
    }

    public TradeRefundResponse setStoreName(String storeName) {
        this.storeName = storeName;
        return this;
    }
    public String getStoreName() {
        return this.storeName;
    }

    public TradeRefundResponse setBuyerUserId(String buyerUserId) {
        this.buyerUserId = buyerUserId;
        return this;
    }
    public String getBuyerUserId() {
        return this.buyerUserId;
    }

    public TradeRefundResponse setRefundPresetPaytoolList(java.util.List<PresetPayToolInfo> refundPresetPaytoolList) {
        this.refundPresetPaytoolList = refundPresetPaytoolList;
        return this;
    }
    public java.util.List<PresetPayToolInfo> getRefundPresetPaytoolList() {
        return this.refundPresetPaytoolList;
    }

    public TradeRefundResponse setRefundSettlementId(String refundSettlementId) {
        this.refundSettlementId = refundSettlementId;
        return this;
    }
    public String getRefundSettlementId() {
        return this.refundSettlementId;
    }

    public TradeRefundResponse setPresentRefundBuyerAmount(String presentRefundBuyerAmount) {
        this.presentRefundBuyerAmount = presentRefundBuyerAmount;
        return this;
    }
    public String getPresentRefundBuyerAmount() {
        return this.presentRefundBuyerAmount;
    }

    public TradeRefundResponse setPresentRefundDiscountAmount(String presentRefundDiscountAmount) {
        this.presentRefundDiscountAmount = presentRefundDiscountAmount;
        return this;
    }
    public String getPresentRefundDiscountAmount() {
        return this.presentRefundDiscountAmount;
    }

    public TradeRefundResponse setPresentRefundMdiscountAmount(String presentRefundMdiscountAmount) {
        this.presentRefundMdiscountAmount = presentRefundMdiscountAmount;
        return this;
    }
    public String getPresentRefundMdiscountAmount() {
        return this.presentRefundMdiscountAmount;
    }

}
