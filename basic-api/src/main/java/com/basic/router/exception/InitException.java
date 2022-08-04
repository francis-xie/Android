
package com.basic.router.exception;

/**
 * 初始化相关异常
 */
public class InitException extends RuntimeException {
    /**
     * 构造方法
     * @param detailMessage 初始化相关的错误信息
     */
    public InitException(String detailMessage) {
        super(detailMessage);
    }
}
