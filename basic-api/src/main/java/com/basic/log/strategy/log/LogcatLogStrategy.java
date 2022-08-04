package com.basic.log.strategy.log;

import com.basic.log.utils.PrinterUtils;

/**
 * <pre>
 *     desc   : Logcat日志策略
 * </pre>
 */
public class LogcatLogStrategy implements ILogStrategy {
    /**
     * 打印日志
     *
     * @param level
     * @param tag
     * @param message
     */
    @Override
    public void log(String level, String tag, String message) {
        PrinterUtils.printConsole(level, tag, message);
    }
}
