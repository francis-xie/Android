
package com.basic.util.exception;

/**
 * 错误信息处理者
 *

 * @since 2018/6/10 下午9:30
 */
public interface IExceptionHandler {

    /**
     * 处理过滤错误信息
     * @param e
     * @return
     */
    RxException handleException(Throwable e);
}
