package com.basic.http.factory;

import com.basic.http.kernel.Client;
import com.basic.http.kernel.Config;
import com.basic.http.kernel.Context;

public class MultipleFactory {

  /**
   * 将一些初始化耗时较多的信息缓存在上下文中
   */
  public Context context;

  /**
   * 设置客户端参数，只需设置一次，即可反复使用各种场景下的API Client
   *
   * @param options 客户端参数对象
   */
  public void setOptions(Config options) {
    try {
      context = new Context(options);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /**
   * 获取支付通用API Client
   *
   * @return 支付通用API Client
   */
  public com.basic.http.common.Client Common() throws Exception {
    return new com.basic.http.common.Client(new Client(context));
  }

  /**
   * 获取OpenAPI通用接口，可通过自行拼装参数，调用几乎所有OpenAPI
   *
   * @return OpenAPI通用接口
   */
  public com.basic.http.util.generic.Client Generic() throws Exception {
    return new com.basic.http.util.generic.Client(new Client(context));
  }
}