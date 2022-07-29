package com.basic.http.kernel;

import com.basic.http.HttpModel;
import com.basic.http.kernel.util.Signer;
import com.google.common.base.Strings;

import java.util.Map;

public class Context {
  /**
   * 客户端配置参数
   */
  private final Map<String, Object> config;

  /**
   * 证书模式运行时环境
   */
  private CertEnvironment certEnvironment;

  /**
   * SHA256WithRSA签名器
   */
  private Signer signer;

  public Context(Config options) throws Exception {
    config = HttpModel.buildMap(options);
//        Preconditions.checkArgument(Constants.RSA2.equals(getConfig(Constants.SIGN_TYPE_CONFIG_KEY)),
//                "SDK只允许使用RSA2签名方式，RSA签名方式由于安全性相比RSA2弱已不再推荐。");

    if (!Strings.isNullOrEmpty(getConfig(Constants.ALIPAY_CERT_PATH_CONFIG_KEY))) {
      certEnvironment = new CertEnvironment(
        getConfig(Constants.MERCHANT_CERT_PATH_CONFIG_KEY),
        getConfig(Constants.ALIPAY_CERT_PATH_CONFIG_KEY),
        getConfig(Constants.ALIPAY_ROOT_CERT_PATH_CONFIG_KEY));
    }
    signer = new Signer();
  }

  public String getConfig(String key) {
    if (String.valueOf(config.get(key)) == "null") {
      return null;
    } else {
      return String.valueOf(config.get(key));
    }
  }

  public CertEnvironment getCertEnvironment() {
    return certEnvironment;
  }

  public Signer getSigner() {
    return signer;
  }

  public void setSigner(Signer signer) {
    this.signer = signer;
  }
}