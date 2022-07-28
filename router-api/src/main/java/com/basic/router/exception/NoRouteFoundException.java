
package com.basic.router.exception;

/**
 * 找不到路由的错误
 */
public class NoRouteFoundException extends RuntimeException {
    /**
     * 构造方法
     * @param detailMessage 找不到路由相关的错误信息
     */
    public NoRouteFoundException(String detailMessage) {
        super(detailMessage);
    }
}
