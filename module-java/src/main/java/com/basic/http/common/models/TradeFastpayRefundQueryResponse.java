// This file is auto-generated, don't edit it. Thanks.
package com.basic.http.common.models;

import com.basic.http.HttpModel;
import com.basic.http.NameInMap;
import com.basic.http.Validation;

public class TradeFastpayRefundQueryResponse extends HttpModel {
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

    @NameInMap("error_code")
    @Validation(required = true)
    public String errorCode;

    @NameInMap("gmt_refund_pay")
    @Validation(required = true)
    public String gmtRefundPay;

    @NameInMap("industry_sepc_detail")
    @Validation(required = true)
    public String industrySepcDetail;

    @NameInMap("out_request_no")
    @Validation(required = true)
    public String outRequestNo;

    @NameInMap("out_trade_no")
    @Validation(required = true)
    public String outTradeNo;

    @NameInMap("present_refund_buyer_amount")
    @Validation(required = true)
    public String presentRefundBuyerAmount;

    @NameInMap("present_refund_discount_amount")
    @Validation(required = true)
    public String presentRefundDiscountAmount;

    @NameInMap("present_refund_mdiscount_amount")
    @Validation(required = true)
    public String presentRefundMdiscountAmount;

    @NameInMap("refund_amount")
    @Validation(required = true)
    public String refundAmount;

    @NameInMap("refund_charge_amount")
    @Validation(required = true)
    public String refundChargeAmount;

    @NameInMap("refund_detail_item_list")
    @Validation(required = true)
    public java.util.List<TradeFundBill> refundDetailItemList;

    @NameInMap("refund_reason")
    @Validation(required = true)
    public String refundReason;

    @NameInMap("refund_royaltys")
    @Validation(required = true)
    public java.util.List<RefundRoyaltyResult> refundRoyaltys;

    @NameInMap("refund_settlement_id")
    @Validation(required = true)
    public String refundSettlementId;

    @NameInMap("refund_status")
    @Validation(required = true)
    public String refundStatus;

    @NameInMap("send_back_fee")
    @Validation(required = true)
    public String sendBackFee;

    @NameInMap("total_amount")
    @Validation(required = true)
    public String totalAmount;

    @NameInMap("trade_no")
    @Validation(required = true)
    public String tradeNo;

    public static TradeFastpayRefundQueryResponse build(java.util.Map<String, ?> map) throws Exception {
        TradeFastpayRefundQueryResponse self = new TradeFastpayRefundQueryResponse();
        return HttpModel.build(map, self);
    }

    public TradeFastpayRefundQueryResponse setHttpBody(String httpBody) {
        this.httpBody = httpBody;
        return this;
    }
    public String getHttpBody() {
        return this.httpBody;
    }

    public TradeFastpayRefundQueryResponse setCode(String code) {
        this.code = code;
        return this;
    }
    public String getCode() {
        return this.code;
    }

    public TradeFastpayRefundQueryResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    public String getMsg() {
        return this.msg;
    }

    public TradeFastpayRefundQueryResponse setSubCode(String subCode) {
        this.subCode = subCode;
        return this;
    }
    public String getSubCode() {
        return this.subCode;
    }

    public TradeFastpayRefundQueryResponse setSubMsg(String subMsg) {
        this.subMsg = subMsg;
        return this;
    }
    public String getSubMsg() {
        return this.subMsg;
    }

    public TradeFastpayRefundQueryResponse setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }
    public String getErrorCode() {
        return this.errorCode;
    }

    public TradeFastpayRefundQueryResponse setGmtRefundPay(String gmtRefundPay) {
        this.gmtRefundPay = gmtRefundPay;
        return this;
    }
    public String getGmtRefundPay() {
        return this.gmtRefundPay;
    }

    public TradeFastpayRefundQueryResponse setIndustrySepcDetail(String industrySepcDetail) {
        this.industrySepcDetail = industrySepcDetail;
        return this;
    }
    public String getIndustrySepcDetail() {
        return this.industrySepcDetail;
    }

    public TradeFastpayRefundQueryResponse setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
        return this;
    }
    public String getOutRequestNo() {
        return this.outRequestNo;
    }

    public TradeFastpayRefundQueryResponse setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }
    public String getOutTradeNo() {
        return this.outTradeNo;
    }

    public TradeFastpayRefundQueryResponse setPresentRefundBuyerAmount(String presentRefundBuyerAmount) {
        this.presentRefundBuyerAmount = presentRefundBuyerAmount;
        return this;
    }
    public String getPresentRefundBuyerAmount() {
        return this.presentRefundBuyerAmount;
    }

    public TradeFastpayRefundQueryResponse setPresentRefundDiscountAmount(String presentRefundDiscountAmount) {
        this.presentRefundDiscountAmount = presentRefundDiscountAmount;
        return this;
    }
    public String getPresentRefundDiscountAmount() {
        return this.presentRefundDiscountAmount;
    }

    public TradeFastpayRefundQueryResponse setPresentRefundMdiscountAmount(String presentRefundMdiscountAmount) {
        this.presentRefundMdiscountAmount = presentRefundMdiscountAmount;
        return this;
    }
    public String getPresentRefundMdiscountAmount() {
        return this.presentRefundMdiscountAmount;
    }

    public TradeFastpayRefundQueryResponse setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
        return this;
    }
    public String getRefundAmount() {
        return this.refundAmount;
    }

    public TradeFastpayRefundQueryResponse setRefundChargeAmount(String refundChargeAmount) {
        this.refundChargeAmount = refundChargeAmount;
        return this;
    }
    public String getRefundChargeAmount() {
        return this.refundChargeAmount;
    }

    public TradeFastpayRefundQueryResponse setRefundDetailItemList(java.util.List<TradeFundBill> refundDetailItemList) {
        this.refundDetailItemList = refundDetailItemList;
        return this;
    }
    public java.util.List<TradeFundBill> getRefundDetailItemList() {
        return this.refundDetailItemList;
    }

    public TradeFastpayRefundQueryResponse setRefundReason(String refundReason) {
        this.refundReason = refundReason;
        return this;
    }
    public String getRefundReason() {
        return this.refundReason;
    }

    public TradeFastpayRefundQueryResponse setRefundRoyaltys(java.util.List<RefundRoyaltyResult> refundRoyaltys) {
        this.refundRoyaltys = refundRoyaltys;
        return this;
    }
    public java.util.List<RefundRoyaltyResult> getRefundRoyaltys() {
        return this.refundRoyaltys;
    }

    public TradeFastpayRefundQueryResponse setRefundSettlementId(String refundSettlementId) {
        this.refundSettlementId = refundSettlementId;
        return this;
    }
    public String getRefundSettlementId() {
        return this.refundSettlementId;
    }

    public TradeFastpayRefundQueryResponse setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
        return this;
    }
    public String getRefundStatus() {
        return this.refundStatus;
    }

    public TradeFastpayRefundQueryResponse setSendBackFee(String sendBackFee) {
        this.sendBackFee = sendBackFee;
        return this;
    }
    public String getSendBackFee() {
        return this.sendBackFee;
    }

    public TradeFastpayRefundQueryResponse setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }
    public String getTotalAmount() {
        return this.totalAmount;
    }

    public TradeFastpayRefundQueryResponse setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
        return this;
    }
    public String getTradeNo() {
        return this.tradeNo;
    }

}
