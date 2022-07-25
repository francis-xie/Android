// This file is auto-generated, don't edit it. Thanks.
package com.alipay.easysdk.payment.wap;

import com.alipay.easysdk.payment.wap.models.*;
import com.basic.http.HttpConverter;
import com.basic.http.HttpModel;
import com.basic.http.HttpPair;

public class Client {

    public com.alipay.easysdk.kernel.Client _kernel;
    public Client(com.alipay.easysdk.kernel.Client kernel) throws Exception {
        this._kernel = kernel;
    }


    public AlipayTradeWapPayResponse pay(String subject, String outTradeNo, String totalAmount, String quitUrl, String returnUrl) throws Exception {
        java.util.Map<String, String> systemParams = HttpConverter.buildMap(
            new HttpPair("method", "alipay.trade.wap.pay"),
            new HttpPair("app_id", _kernel.getConfig("appId")),
            new HttpPair("timestamp", _kernel.getTimestamp()),
            new HttpPair("format", "json"),
            new HttpPair("version", "1.0"),
            new HttpPair("alipay_sdk", _kernel.getSdkVersion()),
            new HttpPair("charset", "UTF-8"),
            new HttpPair("sign_type", _kernel.getConfig("signType")),
            new HttpPair("app_cert_sn", _kernel.getMerchantCertSN()),
            new HttpPair("alipay_root_cert_sn", _kernel.getAlipayRootCertSN())
        );
        java.util.Map<String, Object> bizParams = HttpConverter.buildMap(
            new HttpPair("subject", subject),
            new HttpPair("out_trade_no", outTradeNo),
            new HttpPair("total_amount", totalAmount),
            new HttpPair("quit_url", quitUrl),
            new HttpPair("product_code", "QUICK_WAP_WAY")
        );
        java.util.Map<String, String> textParams = HttpConverter.buildMap(
            new HttpPair("return_url", returnUrl)
        );
        String sign = _kernel.sign(systemParams, bizParams, textParams, _kernel.getConfig("merchantPrivateKey"));
        java.util.Map<String, String> response = HttpConverter.buildMap(
            new HttpPair("body", _kernel.generatePage("POST", systemParams, bizParams, textParams, sign))
        );
        return HttpModel.toModel(response, new AlipayTradeWapPayResponse());
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