// This file is auto-generated, don't edit it. Thanks.
package com.basic.http.common;

import com.basic.http.*;
import com.basic.http.common.models.*;
import com.basic.http.kernel.util.JsonUtil;

public class Client {

  public com.basic.http.kernel.Client _kernel;

  public Client(com.basic.http.kernel.Client kernel) throws Exception {
    this._kernel = kernel;
  }

  public TradeCreateResponse create(String subject, String outTradeNo, String totalAmount, String buyerId) throws Exception {
    java.util.Map<String, Object> runtime_ = HttpConverter.buildMap(
      new HttpPair("ignoreSSL", _kernel.getConfig("ignoreSSL")),
      new HttpPair("httpProxy", _kernel.getConfig("httpProxy")),
      new HttpPair("connectTimeout", 15000),
      new HttpPair("readTimeout", 15000),
      new HttpPair("retry", HttpConverter.buildMap(
        new HttpPair("maxAttempts", 0)
      ))
    );

    HttpRequest _lastRequest = null;
    long _now = System.currentTimeMillis();
    int _retryTimes = 0;
    while (Http.allowRetry((java.util.Map<String, Object>) runtime_.get("retry"), _retryTimes, _now)) {
      if (_retryTimes > 0) {
        int backoffTime = Http.getBackoffTime(runtime_.get("backoff"), _retryTimes);
        if (backoffTime > 0) {
          Http.sleep(backoffTime);
        }
      }
      _retryTimes = _retryTimes + 1;
      try {
        HttpRequest request_ = new HttpRequest();
        java.util.Map<String, String> systemParams = HttpConverter.buildMap(
          new HttpPair("method", "alipay.trade.create"),
          new HttpPair("app_id", _kernel.getConfig("appId")),
          new HttpPair("timestamp", _kernel.getTimestamp()),
          new HttpPair("format", "json"),
          new HttpPair("version", "1.0"),
          new HttpPair("charset", "UTF-8"),
          new HttpPair("sign_type", _kernel.getConfig("signType")),
          new HttpPair("app_cert_sn", _kernel.getMerchantCertSN()),
          new HttpPair("alipay_root_cert_sn", _kernel.getRootCertSN())
        );
        java.util.Map<String, Object> bizParams = HttpConverter.buildMap(
          new HttpPair("subject", subject),
          new HttpPair("out_trade_no", outTradeNo),
          new HttpPair("total_amount", totalAmount),
          new HttpPair("buyer_id", buyerId)
        );
        java.util.Map<String, String> textParams = new java.util.HashMap<>();
        request_.protocol = _kernel.getConfig("protocol");
        request_.method = "POST";
        request_.pathname = "/gateway.do";
        request_.headers = HttpConverter.buildMap(
          new HttpPair("host", _kernel.getConfig("gatewayHost")),
          new HttpPair("content-type", "application/x-www-form-urlencoded;charset=utf-8")
        );
        request_.query = _kernel.sortMap(HttpConverter.merge(String.class,
          HttpConverter.buildMap(
            new HttpPair("sign", _kernel.sign(systemParams, bizParams, textParams, _kernel.getConfig("merchantPrivateKey")))
          ),
          systemParams,
          textParams
        ));
        request_.body = Http.toReadable(_kernel.toUrlEncodedRequestBody(bizParams));
        _lastRequest = request_;
        HttpResponse response_ = Http.doAction(request_, runtime_);

        java.util.Map<String, Object> respMap = _kernel.readAsJson(response_, "alipay.trade.create");
        if (_kernel.isCertMode()) {
          if (_kernel.verify(respMap, _kernel.extractPublicKey(_kernel.getCertSN(respMap)))) {
            return HttpModel.toModel(_kernel.toRespModel(respMap), new TradeCreateResponse());
          }

        } else {
          if (_kernel.verify(respMap, _kernel.getConfig("alipayPublicKey"))) {
            return HttpModel.toModel(_kernel.toRespModel(respMap), new TradeCreateResponse());
          }

        }

        throw new HttpException(HttpConverter.buildMap(
          new HttpPair("message", "验签失败，请检查支付宝公钥设置是否正确。")
        ));
      } catch (Exception e) {
        if (Http.isRetryable(e)) {
          continue;
        }
        throw new RuntimeException(e);
      }
    }

    throw new HttpUnretryableException(_lastRequest);
  }

  public TradeQueryResponse query(String outTradeNo) throws Exception {
    java.util.Map<String, Object> runtime_ = HttpConverter.buildMap(
      new HttpPair("ignoreSSL", _kernel.getConfig("ignoreSSL")),
      new HttpPair("httpProxy", _kernel.getConfig("httpProxy")),
      new HttpPair("connectTimeout", 15000),
      new HttpPair("readTimeout", 15000),
      new HttpPair("retry", HttpConverter.buildMap(
        new HttpPair("maxAttempts", 0)
      ))
    );

    HttpRequest _lastRequest = null;
    long _now = System.currentTimeMillis();
    int _retryTimes = 0;
    while (Http.allowRetry((java.util.Map<String, Object>) runtime_.get("retry"), _retryTimes, _now)) {
      if (_retryTimes > 0) {
        int backoffTime = Http.getBackoffTime(runtime_.get("backoff"), _retryTimes);
        if (backoffTime > 0) {
          Http.sleep(backoffTime);
        }
      }
      _retryTimes = _retryTimes + 1;
      try {
        String[] str = new String[]{"TRADE_SETTLE_INFO", "fund_bill_list"
          , "voucher_detail_list", "discount_goods_detail", "mdiscount_amount"};
        HttpRequest request_ = new HttpRequest();
        java.util.Map<String, String> systemParams = HttpConverter.buildMap(
          new HttpPair("method", "alipay.trade.query"),
          new HttpPair("app_id", _kernel.getConfig("appId")),
          new HttpPair("timestamp", _kernel.getTimestamp()),
          new HttpPair("format", "json"),
          new HttpPair("version", "1.0"),
          new HttpPair("charset", "UTF-8"),
          new HttpPair("sign_type", _kernel.getConfig("signType")),
          new HttpPair("app_cert_sn", _kernel.getMerchantCertSN()),
          new HttpPair("alipay_root_cert_sn", _kernel.getRootCertSN())
        );
        java.util.Map<String, Object> bizParams = HttpConverter.buildMap(
          new HttpPair("out_trade_no", outTradeNo)
          , new HttpPair("query_options", str)
        );
        systemParams.put("biz_content", JsonUtil.toJsonString(bizParams));
        bizParams = null;
        java.util.Map<String, String> textParams = new java.util.HashMap<>();
        request_.protocol = _kernel.getConfig("protocol");
        request_.method = "POST";
        request_.pathname = "/gateway.do";
        request_.headers = HttpConverter.buildMap(
          new HttpPair("host", _kernel.getConfig("gatewayHost")),
          new HttpPair("content-type", "application/x-www-form-urlencoded;charset=utf-8")
        );
        request_.query = _kernel.sortMap(HttpConverter.merge(String.class,
          HttpConverter.buildMap(
            new HttpPair("sign", _kernel.sign(systemParams, bizParams, textParams, _kernel.getConfig("merchantPrivateKey")))
          ),
          systemParams,
          textParams
        ));
        //request_.body = Http.toReadable(_kernel.toUrlEncodedRequestBody(bizParams));
        _lastRequest = request_;
        HttpResponse response_ = Http.doAction(request_, runtime_);

        java.util.Map<String, Object> respMap = _kernel.readAsJson(response_, "alipay.trade.query");
        if (_kernel.isCertMode()) {
          if (_kernel.verify(respMap, _kernel.extractPublicKey(_kernel.getCertSN(respMap)))) {
            return HttpModel.toModel(_kernel.toRespModel(respMap), new TradeQueryResponse());
          }
        } else {
          if (_kernel.verify(respMap, _kernel.getConfig("alipayPublicKey"))) {
            return HttpModel.toModel(_kernel.toRespModel(respMap), new TradeQueryResponse());
          }
        }
        throw new HttpException(HttpConverter.buildMap(
          new HttpPair("message", "验签失败，请检查支付宝公钥设置是否正确。")
        ));
      } catch (Exception e) {
        if (Http.isRetryable(e)) {
          continue;
        }
        throw new RuntimeException(e);
      }
    }
    throw new HttpUnretryableException(_lastRequest);
  }

  public TradeRefundResponse refund(String outTradeNo, String refundAmount) throws Exception {
    java.util.Map<String, Object> runtime_ = HttpConverter.buildMap(
      new HttpPair("ignoreSSL", _kernel.getConfig("ignoreSSL")),
      new HttpPair("httpProxy", _kernel.getConfig("httpProxy")),
      new HttpPair("connectTimeout", 15000),
      new HttpPair("readTimeout", 15000),
      new HttpPair("retry", HttpConverter.buildMap(
        new HttpPair("maxAttempts", 0)
      ))
    );

    HttpRequest _lastRequest = null;
    long _now = System.currentTimeMillis();
    int _retryTimes = 0;
    while (Http.allowRetry((java.util.Map<String, Object>) runtime_.get("retry"), _retryTimes, _now)) {
      if (_retryTimes > 0) {
        int backoffTime = Http.getBackoffTime(runtime_.get("backoff"), _retryTimes);
        if (backoffTime > 0) {
          Http.sleep(backoffTime);
        }
      }
      _retryTimes = _retryTimes + 1;
      try {
        HttpRequest request_ = new HttpRequest();
        java.util.Map<String, String> systemParams = HttpConverter.buildMap(
          new HttpPair("method", "alipay.trade.refund"),
          new HttpPair("app_id", _kernel.getConfig("appId")),
          new HttpPair("timestamp", _kernel.getTimestamp()),
          new HttpPair("format", "json"),
          new HttpPair("version", "1.0"),
          new HttpPair("charset", "UTF-8"),
          new HttpPair("sign_type", _kernel.getConfig("signType")),
          new HttpPair("app_cert_sn", _kernel.getMerchantCertSN()),
          new HttpPair("alipay_root_cert_sn", _kernel.getRootCertSN())
        );
        java.util.Map<String, Object> bizParams = HttpConverter.buildMap(
          new HttpPair("out_trade_no", outTradeNo),
          new HttpPair("refund_amount", refundAmount)
        );
        java.util.Map<String, String> textParams = new java.util.HashMap<>();
        request_.protocol = _kernel.getConfig("protocol");
        request_.method = "POST";
        request_.pathname = "/gateway.do";
        request_.headers = HttpConverter.buildMap(
          new HttpPair("host", _kernel.getConfig("gatewayHost")),
          new HttpPair("content-type", "application/x-www-form-urlencoded;charset=utf-8")
        );
        request_.query = _kernel.sortMap(HttpConverter.merge(String.class,
          HttpConverter.buildMap(
            new HttpPair("sign", _kernel.sign(systemParams, bizParams, textParams, _kernel.getConfig("merchantPrivateKey")))
          ),
          systemParams,
          textParams
        ));
        request_.body = Http.toReadable(_kernel.toUrlEncodedRequestBody(bizParams));
        _lastRequest = request_;
        HttpResponse response_ = Http.doAction(request_, runtime_);

        java.util.Map<String, Object> respMap = _kernel.readAsJson(response_, "alipay.trade.refund");
        if (_kernel.isCertMode()) {
          if (_kernel.verify(respMap, _kernel.extractPublicKey(_kernel.getCertSN(respMap)))) {
            return HttpModel.toModel(_kernel.toRespModel(respMap), new TradeRefundResponse());
          }

        } else {
          if (_kernel.verify(respMap, _kernel.getConfig("alipayPublicKey"))) {
            return HttpModel.toModel(_kernel.toRespModel(respMap), new TradeRefundResponse());
          }

        }

        throw new HttpException(HttpConverter.buildMap(
          new HttpPair("message", "验签失败，请检查支付宝公钥设置是否正确。")
        ));
      } catch (Exception e) {
        if (Http.isRetryable(e)) {
          continue;
        }
        throw new RuntimeException(e);
      }
    }

    throw new HttpUnretryableException(_lastRequest);
  }

  public TradeCloseResponse close(String outTradeNo) throws Exception {
    java.util.Map<String, Object> runtime_ = HttpConverter.buildMap(
      new HttpPair("ignoreSSL", _kernel.getConfig("ignoreSSL")),
      new HttpPair("httpProxy", _kernel.getConfig("httpProxy")),
      new HttpPair("connectTimeout", 15000),
      new HttpPair("readTimeout", 15000),
      new HttpPair("retry", HttpConverter.buildMap(
        new HttpPair("maxAttempts", 0)
      ))
    );

    HttpRequest _lastRequest = null;
    long _now = System.currentTimeMillis();
    int _retryTimes = 0;
    while (Http.allowRetry((java.util.Map<String, Object>) runtime_.get("retry"), _retryTimes, _now)) {
      if (_retryTimes > 0) {
        int backoffTime = Http.getBackoffTime(runtime_.get("backoff"), _retryTimes);
        if (backoffTime > 0) {
          Http.sleep(backoffTime);
        }
      }
      _retryTimes = _retryTimes + 1;
      try {
        HttpRequest request_ = new HttpRequest();
        java.util.Map<String, String> systemParams = HttpConverter.buildMap(
          new HttpPair("method", "alipay.trade.close"),
          new HttpPair("app_id", _kernel.getConfig("appId")),
          new HttpPair("timestamp", _kernel.getTimestamp()),
          new HttpPair("format", "json"),
          new HttpPair("version", "1.0"),
          new HttpPair("charset", "UTF-8"),
          new HttpPair("sign_type", _kernel.getConfig("signType")),
          new HttpPair("app_cert_sn", _kernel.getMerchantCertSN()),
          new HttpPair("alipay_root_cert_sn", _kernel.getRootCertSN())
        );
        java.util.Map<String, Object> bizParams = HttpConverter.buildMap(
          new HttpPair("out_trade_no", outTradeNo)
        );
        java.util.Map<String, String> textParams = new java.util.HashMap<>();
        request_.protocol = _kernel.getConfig("protocol");
        request_.method = "POST";
        request_.pathname = "/gateway.do";
        request_.headers = HttpConverter.buildMap(
          new HttpPair("host", _kernel.getConfig("gatewayHost")),
          new HttpPair("content-type", "application/x-www-form-urlencoded;charset=utf-8")
        );
        request_.query = _kernel.sortMap(HttpConverter.merge(String.class,
          HttpConverter.buildMap(
            new HttpPair("sign", _kernel.sign(systemParams, bizParams, textParams, _kernel.getConfig("merchantPrivateKey")))
          ),
          systemParams,
          textParams
        ));
        request_.body = Http.toReadable(_kernel.toUrlEncodedRequestBody(bizParams));
        _lastRequest = request_;
        HttpResponse response_ = Http.doAction(request_, runtime_);

        java.util.Map<String, Object> respMap = _kernel.readAsJson(response_, "alipay.trade.close");
        if (_kernel.isCertMode()) {
          if (_kernel.verify(respMap, _kernel.extractPublicKey(_kernel.getCertSN(respMap)))) {
            return HttpModel.toModel(_kernel.toRespModel(respMap), new TradeCloseResponse());
          }

        } else {
          if (_kernel.verify(respMap, _kernel.getConfig("alipayPublicKey"))) {
            return HttpModel.toModel(_kernel.toRespModel(respMap), new TradeCloseResponse());
          }

        }

        throw new HttpException(HttpConverter.buildMap(
          new HttpPair("message", "验签失败，请检查支付宝公钥设置是否正确。")
        ));
      } catch (Exception e) {
        if (Http.isRetryable(e)) {
          continue;
        }
        throw new RuntimeException(e);
      }
    }

    throw new HttpUnretryableException(_lastRequest);
  }

  public TradeCancelResponse cancel(String outTradeNo) throws Exception {
    java.util.Map<String, Object> runtime_ = HttpConverter.buildMap(
      new HttpPair("ignoreSSL", _kernel.getConfig("ignoreSSL")),
      new HttpPair("httpProxy", _kernel.getConfig("httpProxy")),
      new HttpPair("connectTimeout", 15000),
      new HttpPair("readTimeout", 15000),
      new HttpPair("retry", HttpConverter.buildMap(
        new HttpPair("maxAttempts", 0)
      ))
    );

    HttpRequest _lastRequest = null;
    long _now = System.currentTimeMillis();
    int _retryTimes = 0;
    while (Http.allowRetry((java.util.Map<String, Object>) runtime_.get("retry"), _retryTimes, _now)) {
      if (_retryTimes > 0) {
        int backoffTime = Http.getBackoffTime(runtime_.get("backoff"), _retryTimes);
        if (backoffTime > 0) {
          Http.sleep(backoffTime);
        }
      }
      _retryTimes = _retryTimes + 1;
      try {
        HttpRequest request_ = new HttpRequest();
        java.util.Map<String, String> systemParams = HttpConverter.buildMap(
          new HttpPair("method", "alipay.trade.cancel"),
          new HttpPair("app_id", _kernel.getConfig("appId")),
          new HttpPair("timestamp", _kernel.getTimestamp()),
          new HttpPair("format", "json"),
          new HttpPair("version", "1.0"),
          new HttpPair("charset", "UTF-8"),
          new HttpPair("sign_type", _kernel.getConfig("signType")),
          new HttpPair("app_cert_sn", _kernel.getMerchantCertSN()),
          new HttpPair("alipay_root_cert_sn", _kernel.getRootCertSN())
        );
        java.util.Map<String, Object> bizParams = HttpConverter.buildMap(
          new HttpPair("out_trade_no", outTradeNo)
        );
        java.util.Map<String, String> textParams = new java.util.HashMap<>();
        request_.protocol = _kernel.getConfig("protocol");
        request_.method = "POST";
        request_.pathname = "/gateway.do";
        request_.headers = HttpConverter.buildMap(
          new HttpPair("host", _kernel.getConfig("gatewayHost")),
          new HttpPair("content-type", "application/x-www-form-urlencoded;charset=utf-8")
        );
        request_.query = _kernel.sortMap(HttpConverter.merge(String.class,
          HttpConverter.buildMap(
            new HttpPair("sign", _kernel.sign(systemParams, bizParams, textParams, _kernel.getConfig("merchantPrivateKey")))
          ),
          systemParams,
          textParams
        ));
        request_.body = Http.toReadable(_kernel.toUrlEncodedRequestBody(bizParams));
        _lastRequest = request_;
        HttpResponse response_ = Http.doAction(request_, runtime_);

        java.util.Map<String, Object> respMap = _kernel.readAsJson(response_, "alipay.trade.cancel");
        if (_kernel.isCertMode()) {
          if (_kernel.verify(respMap, _kernel.extractPublicKey(_kernel.getCertSN(respMap)))) {
            return HttpModel.toModel(_kernel.toRespModel(respMap), new TradeCancelResponse());
          }

        } else {
          if (_kernel.verify(respMap, _kernel.getConfig("alipayPublicKey"))) {
            return HttpModel.toModel(_kernel.toRespModel(respMap), new TradeCancelResponse());
          }

        }

        throw new HttpException(HttpConverter.buildMap(
          new HttpPair("message", "验签失败，请检查支付宝公钥设置是否正确。")
        ));
      } catch (Exception e) {
        if (Http.isRetryable(e)) {
          continue;
        }
        throw new RuntimeException(e);
      }
    }

    throw new HttpUnretryableException(_lastRequest);
  }

  public TradeFastpayRefundQueryResponse queryRefund(String outTradeNo, String outRequestNo) throws Exception {
    java.util.Map<String, Object> runtime_ = HttpConverter.buildMap(
      new HttpPair("ignoreSSL", _kernel.getConfig("ignoreSSL")),
      new HttpPair("httpProxy", _kernel.getConfig("httpProxy")),
      new HttpPair("connectTimeout", 15000),
      new HttpPair("readTimeout", 15000),
      new HttpPair("retry", HttpConverter.buildMap(
        new HttpPair("maxAttempts", 0)
      ))
    );

    HttpRequest _lastRequest = null;
    long _now = System.currentTimeMillis();
    int _retryTimes = 0;
    while (Http.allowRetry((java.util.Map<String, Object>) runtime_.get("retry"), _retryTimes, _now)) {
      if (_retryTimes > 0) {
        int backoffTime = Http.getBackoffTime(runtime_.get("backoff"), _retryTimes);
        if (backoffTime > 0) {
          Http.sleep(backoffTime);
        }
      }
      _retryTimes = _retryTimes + 1;
      try {
        HttpRequest request_ = new HttpRequest();
        java.util.Map<String, String> systemParams = HttpConverter.buildMap(
          new HttpPair("method", "alipay.trade.fastpay.refund.query"),
          new HttpPair("app_id", _kernel.getConfig("appId")),
          new HttpPair("timestamp", _kernel.getTimestamp()),
          new HttpPair("format", "json"),
          new HttpPair("version", "1.0"),
          new HttpPair("charset", "UTF-8"),
          new HttpPair("sign_type", _kernel.getConfig("signType")),
          new HttpPair("app_cert_sn", _kernel.getMerchantCertSN()),
          new HttpPair("alipay_root_cert_sn", _kernel.getRootCertSN())
        );
        java.util.Map<String, Object> bizParams = HttpConverter.buildMap(
          new HttpPair("out_trade_no", outTradeNo),
          new HttpPair("out_request_no", outRequestNo)
        );
        java.util.Map<String, String> textParams = new java.util.HashMap<>();
        request_.protocol = _kernel.getConfig("protocol");
        request_.method = "POST";
        request_.pathname = "/gateway.do";
        request_.headers = HttpConverter.buildMap(
          new HttpPair("host", _kernel.getConfig("gatewayHost")),
          new HttpPair("content-type", "application/x-www-form-urlencoded;charset=utf-8")
        );
        request_.query = _kernel.sortMap(HttpConverter.merge(String.class,
          HttpConverter.buildMap(
            new HttpPair("sign", _kernel.sign(systemParams, bizParams, textParams, _kernel.getConfig("merchantPrivateKey")))
          ),
          systemParams,
          textParams
        ));
        request_.body = Http.toReadable(_kernel.toUrlEncodedRequestBody(bizParams));
        _lastRequest = request_;
        HttpResponse response_ = Http.doAction(request_, runtime_);

        java.util.Map<String, Object> respMap = _kernel.readAsJson(response_, "alipay.trade.fastpay.refund.query");
        if (_kernel.isCertMode()) {
          if (_kernel.verify(respMap, _kernel.extractPublicKey(_kernel.getCertSN(respMap)))) {
            return HttpModel.toModel(_kernel.toRespModel(respMap), new TradeFastpayRefundQueryResponse());
          }

        } else {
          if (_kernel.verify(respMap, _kernel.getConfig("alipayPublicKey"))) {
            return HttpModel.toModel(_kernel.toRespModel(respMap), new TradeFastpayRefundQueryResponse());
          }

        }

        throw new HttpException(HttpConverter.buildMap(
          new HttpPair("message", "验签失败，请检查支付宝公钥设置是否正确。")
        ));
      } catch (Exception e) {
        if (Http.isRetryable(e)) {
          continue;
        }
        throw new RuntimeException(e);
      }
    }

    throw new HttpUnretryableException(_lastRequest);
  }

  public DataDataserviceBillDownloadurlQueryResponse downloadBill(String billType, String billDate) throws Exception {
    java.util.Map<String, Object> runtime_ = HttpConverter.buildMap(
      new HttpPair("ignoreSSL", _kernel.getConfig("ignoreSSL")),
      new HttpPair("httpProxy", _kernel.getConfig("httpProxy")),
      new HttpPair("connectTimeout", 15000),
      new HttpPair("readTimeout", 15000),
      new HttpPair("retry", HttpConverter.buildMap(
        new HttpPair("maxAttempts", 0)
      ))
    );

    HttpRequest _lastRequest = null;
    long _now = System.currentTimeMillis();
    int _retryTimes = 0;
    while (Http.allowRetry((java.util.Map<String, Object>) runtime_.get("retry"), _retryTimes, _now)) {
      if (_retryTimes > 0) {
        int backoffTime = Http.getBackoffTime(runtime_.get("backoff"), _retryTimes);
        if (backoffTime > 0) {
          Http.sleep(backoffTime);
        }
      }
      _retryTimes = _retryTimes + 1;
      try {
        HttpRequest request_ = new HttpRequest();
        java.util.Map<String, String> systemParams = HttpConverter.buildMap(
          new HttpPair("method", "alipay.data.dataservice.bill.downloadurl.query"),
          new HttpPair("app_id", _kernel.getConfig("appId")),
          new HttpPair("timestamp", _kernel.getTimestamp()),
          new HttpPair("format", "json"),
          new HttpPair("version", "1.0"),
          new HttpPair("charset", "UTF-8"),
          new HttpPair("sign_type", _kernel.getConfig("signType")),
          new HttpPair("app_cert_sn", _kernel.getMerchantCertSN()),
          new HttpPair("alipay_root_cert_sn", _kernel.getRootCertSN())
        );
        java.util.Map<String, Object> bizParams = HttpConverter.buildMap(
          new HttpPair("bill_type", billType),
          new HttpPair("bill_date", billDate)
        );
        java.util.Map<String, String> textParams = new java.util.HashMap<>();
        request_.protocol = _kernel.getConfig("protocol");
        request_.method = "POST";
        request_.pathname = "/gateway.do";
        request_.headers = HttpConverter.buildMap(
          new HttpPair("host", _kernel.getConfig("gatewayHost")),
          new HttpPair("content-type", "application/x-www-form-urlencoded;charset=utf-8")
        );
        request_.query = _kernel.sortMap(HttpConverter.merge(String.class,
          HttpConverter.buildMap(
            new HttpPair("sign", _kernel.sign(systemParams, bizParams, textParams, _kernel.getConfig("merchantPrivateKey")))
          ),
          systemParams,
          textParams
        ));
        request_.body = Http.toReadable(_kernel.toUrlEncodedRequestBody(bizParams));
        _lastRequest = request_;
        HttpResponse response_ = Http.doAction(request_, runtime_);

        java.util.Map<String, Object> respMap = _kernel.readAsJson(response_, "alipay.data.dataservice.bill.downloadurl.query");
        if (_kernel.isCertMode()) {
          if (_kernel.verify(respMap, _kernel.extractPublicKey(_kernel.getCertSN(respMap)))) {
            return HttpModel.toModel(_kernel.toRespModel(respMap), new DataDataserviceBillDownloadurlQueryResponse());
          }

        } else {
          if (_kernel.verify(respMap, _kernel.getConfig("alipayPublicKey"))) {
            return HttpModel.toModel(_kernel.toRespModel(respMap), new DataDataserviceBillDownloadurlQueryResponse());
          }

        }

        throw new HttpException(HttpConverter.buildMap(
          new HttpPair("message", "验签失败，请检查支付宝公钥设置是否正确。")
        ));
      } catch (Exception e) {
        if (Http.isRetryable(e)) {
          continue;
        }
        throw new RuntimeException(e);
      }
    }

    throw new HttpUnretryableException(_lastRequest);
  }

  public Boolean verifyNotify(java.util.Map<String, String> parameters) throws Exception {
    if (_kernel.isCertMode()) {
      return _kernel.verifyParams(parameters, _kernel.extractPublicKey(""));
    } else {
      return _kernel.verifyParams(parameters, _kernel.getConfig("alipayPublicKey"));
    }

  }

  /**
   * ISV代商户代用，指定appAuthToken
   *
   * @param appAuthToken 代调用token
   * @return 本客户端，便于链式调用
   */
  public Client agent(String appAuthToken) {
    _kernel.injectTextParam("app_auth_token", appAuthToken);
    return this;
  }

  /**
   * 用户授权调用，指定authToken
   *
   * @param authToken 用户授权token
   * @return 本客户端，便于链式调用
   */
  public Client auth(String authToken) {
    _kernel.injectTextParam("auth_token", authToken);
    return this;
  }

  /**
   * 设置异步通知回调地址，此处设置将在本调用中覆盖Config中的全局配置
   *
   * @param url 异步通知回调地址，例如：https://www.test.com/callback
   * @return 本客户端，便于链式调用
   */
  public Client asyncNotify(String url) {
    _kernel.injectTextParam("notify_url", url);
    return this;
  }

  /**
   * 将本次调用强制路由到后端系统的测试地址上，常用于线下环境内外联调，沙箱与线上环境设置无效
   *
   * @param testUrl 后端系统测试地址
   * @return 本客户端，便于链式调用
   */
  public Client route(String testUrl) {
    _kernel.injectTextParam("ws_service_url", testUrl);
    return this;
  }

  /**
   * 设置API入参中没有的其他可选业务请求参数(biz_content下的字段)
   *
   * @param key   业务请求参数名称（biz_content下的字段名，比如timeout_express）
   * @param value 业务请求参数的值，一个可以序列化成JSON的对象
   *              如果该字段是一个字符串类型（String、Price、Date在SDK中都是字符串），请使用String储存
   *              如果该字段是一个数值型类型（比如：Number），请使用Long储存
   *              如果该字段是一个复杂类型，请使用嵌套的Map指定各下级字段的值
   *              如果该字段是一个数组，请使用List储存各个值
   *              对于更复杂的情况，也支持Map和List的各种组合嵌套，比如参数是值是个List，List中的每种类型是一个复杂对象
   * @return 本客户端，便于链式调用
   */
  public Client optional(String key, Object value) {
    _kernel.injectBizParam(key, value);
    return this;
  }

  /**
   * 批量设置API入参中没有的其他可选业务请求参数(biz_content下的字段)
   * optional方法的批量版本
   *
   * @param optionalArgs 可选参数集合，每个参数由key和value组成，key和value的格式请参见optional方法的注释
   * @return 本客户端，便于链式调用
   */
  public Client batchOptional(java.util.Map<String, Object> optionalArgs) {
    for (java.util.Map.Entry<String, Object> pair : optionalArgs.entrySet()) {
      _kernel.injectBizParam(pair.getKey(), pair.getValue());
    }
    return this;
  }
}