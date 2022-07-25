package com.basic.http;

import com.basic.http.common.models.TradeQueryResponse;
import com.basic.http.factory.Factory;
import com.basic.http.factory.Factory.Payment;
import com.basic.http.kernel.Config;
import com.basic.http.kernel.util.ResponseChecker;

/**
 * https://repo1.maven.org/maven2/com/alipay/sdk/alipay-easysdk/2.2.2/
 * https://repo1.maven.org/maven2/com/alipay/sdk/easysdk-kernel/1.0.11/
 * https://repo1.maven.org/maven2/com/aliyun/tea/1.2.0/
 * https://github.com/alipay/alipay-easysdk/tree/master/java
 * 当面付：https://opensupport.alipay.com/support/helpcenter/99?ant_source=antsupport
 * 多商户收款：https://opensupport.alipay.com/support/helpcenter/99/201602479724?ant_source=antsupport
 * 设置/修改门店收款账号：https://opendocs.alipay.com/open/205/103873/
 * 收款账号设置页面URL： https://e.alipay.com/shop/isvPayeeAccount.htm
 * 门店管理：https://b.alipay.com/page/store-management/shop
 * 门店管理：https://uemprod.alipay.com/baseinfo/merchantShopInfo.htm#/shop?_k=alzx7m
 * 开放平台控制台：https://open.alipay.com/develop/manage
 * 授权管理：https://b.alipay.com/page/sp-account-center/authorize.htm
 * 收款账号绑定：https://b.alipay.com/page/account-manage/spBindShop.htm
 * 代运营中心：https://p.alipay.com/page/operation/workspace
 * 商户分账：https://opensupport.alipay.com/support/helpcenter/116?ant_source=antsupport
 * 商户分账签约：https://mrchportalweb.alipay.com/dynlink/productSign/sign.htm?productCode=I1140300001000001870
 * 商户分账接入：https://opensupport.alipay.com/access/integrationList/template/46/332/333/335?ant_source=antsupport
 */
public class Main {
  public static void main(String[] args) throws Exception {
    // 1. 设置参数（全局只需设置一次）
    Factory.setOptions(getOptions());
    try {
      // 2. 发起API调用（以创建当面付付款码支付 > 支付 > 统一收单线下交易查询接口为例）
      TradeQueryResponse response = Payment.Common().query("041041120220716220449500");
      System.out.println(response.getHttpBody());
      response = Factory.Payment.Common().query("041041120220716220449500");
      System.out.println(response.getHttpBody());
      // 3. 处理响应或异常
      if (ResponseChecker.success(response)) {
        System.out.println("调用成功");
      } else {
        System.err.println("调用失败，原因：" + response.msg + "，" + response.subMsg);
      }
    } catch (Exception e) {
      System.err.println("调用遭遇异常，原因：" + e.getMessage());
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private static Config getOptions() {
    Config config = new Config();
    config.protocol = "https";
    config.gatewayHost = "openapi.alipay.com";
    config.signType = "RSA";

    config.appId = "2015050600061058";
    // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
    config.merchantPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOyptMDIlV6yKpM23pxqb5u8tLW1/Tt1WfGYkup0gLH/v2cjTu5CwM2daciOCnvkZbtg0s4RcB0Oqkgs9iUY/1/3KRX6WnoVvameSWMAFACpgBcm2G4ovton7Fx/6MDCUm2oKIVYE8Oh5wXJv8bDeyzY1kLK3BYd8uwlbqsyqmuTAgMBAAECgYEAnScoNyg7QBGxgeZatqVbDaCmo0+BI/2OR4cYcAYC+7AnapagsEi1fRQ9jYb84T5Oa7ok4d/kgfceDiWFIidSVCFu8aK1xBAIjxJ82Zx7zAKamjIKmKx9FpsRPDZiVBvcd/F886GDr+dyRcLbByi/9SPA3WXa3S08SroDRX+swrECQQD8G9mTDFOsOCrO+6dm22Ub2XSAUiGLixpgUi3z1NOo74vg8a2YuMQTo35TfeGFiO2Dh9sJhv/w3IGWhT4Q/+PJAkEA8FDTSRcn79svV80lnFqljRsrKuA9AeBexUqYk0xgpFJD227WZErswqh3La+3LvN9P4R7bAjlffgRlvcHjBEqewJBAJzmbENzEYQGNMY6olWGR2fMrw/Jjaz99n1px8WPd+cUpnZpksOQgh9CyjUGC9wCeuc4Ht7QEOuDIHdgF1+XEdkCQCvTkbTStEXaA1hP4XwSS/7f5Y98NaQR9zaj1A+I7lcJabarEA5aW7NkZde/I/k8Dqt2zct2E+RVSV/uzUGOH+ECQHKkzmZcGBEb2A9QJAEsHRPQIBob6lDRIEkG9PphAHa7Xyecvps/Ehgx+SwycKKJKLwQWnRv/UaC0is+NOHvYzU=";
    //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
    config.alipayPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

    //可设置异步通知接收服务地址（可选）
    //config.notifyUrl = "<-- 请填写您的支付类接口异步通知接收服务地址，例如：https://www.test.com/callback -->";
    //可设置AES密钥，调用AES加解密相关接口时需要（可选）
    //config.encryptKey = "<-- 请填写您的AES密钥，例如：aa4BtZ4tspm2wnXLb1ThQA== -->";

    return config;
  }
}