package com.basic.log.strategy.format;

import com.basic.log.annotation.LogLevel;

/**
 * <pre>
 *     desc   : 日志格式策略实现接口
 * </pre>
 */
public interface IFormatStrategy {

    /**
     * 格式化日志内容
     * @param level
     * @param tag
     * @param message
     */
    void format(@LogLevel String level, String tag, String message);
}
