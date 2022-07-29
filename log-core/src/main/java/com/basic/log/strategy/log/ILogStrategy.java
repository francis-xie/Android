package com.basic.log.strategy.log;

import com.basic.log.annotation.LogLevel;

/**
 * <pre>
 *     desc   : 日志打印策略实现接口
 * </pre>
 */
public interface ILogStrategy {

    /**
     * 打印日志
     * @param level
     * @param tag
     * @param message
     */
    void log(@LogLevel String level, String tag, String message);
}
