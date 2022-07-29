package com.basic.http.factory;

import com.basic.http.kernel.Client;
import com.basic.http.kernel.Config;
import com.basic.http.kernel.Context;

/**
 * 客户端工厂，用于快速配置和访问各种场景下的API Client
 * 注：该Factory获取的Client不可储存重复使用，请每次均通过Factory完成调用
 */
public class Factory {

  /**
   * 将一些初始化耗时较多的信息缓存在上下文中
   */
  private static Context context;

  /**
   * 设置客户端参数，只需设置一次，即可反复使用各种场景下的API Client
   *
   * @param options 客户端参数对象
   */
  public static void setOptions(Config options) {
    try {
      context = new Context(options);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /**
   * 支付能力相关
   */
  public static class Payment {
    /**
     * 获取支付通用API Client
     *
     * @return 支付通用API Client
     */
    public static com.basic.http.common.Client Common() throws Exception {
      return new com.basic.http.common.Client(new Client(context));
    }
  }

  /**
   * 辅助工具
   */
  public static class Util {
    /**
     * 获取OpenAPI通用接口，可通过自行拼装参数，调用几乎所有OpenAPI
     *
     * @return OpenAPI通用接口
     */
    public static com.basic.http.util.generic.Client Generic() throws Exception {
      return new com.basic.http.util.generic.Client(new com.basic.http.kernel.Client(context));
    }
  }
}