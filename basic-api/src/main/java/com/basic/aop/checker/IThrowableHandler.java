
package com.basic.aop.checker;

/**
 * <pre>
 *     desc   : 异常的处理

 *     time   : 2018/5/14 下午11:04
 * </pre>
 */
public interface IThrowableHandler {

    /**
     * 处理异常
     * @param flag 异常的标志
     * @param throwable 捕获到的异常
     * @return
     */
    Object handleThrowable(String flag, Throwable throwable);
}
