
package com.basic.router.exception;

/**
 * 主流程的处理异常
 *
 
 * @since 2018/5/17 下午11:11
 */
public class HandlerException extends RuntimeException {
    /**
     * 构造方法
     * @param detailMessage 主流程处理相关的错误信息
     */
    public HandlerException(String detailMessage) {
        super(detailMessage);
    }
}
