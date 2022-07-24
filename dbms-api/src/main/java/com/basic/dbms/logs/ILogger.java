
package com.basic.dbms.logs;

/**
 * <pre>
 *     desc   : 简易的日志记录接口

 *     time   : 2018/4/30 下午12:18
 * </pre>
 */
public interface ILogger {

    /**
     * 打印信息
     *
     * @param priority 优先级
     * @param tag      标签
     * @param message  信息
     * @param t        出错信息
     */
    void log(int priority, String tag, String message, Throwable t);

}
