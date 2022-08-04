package com.basic.log.utils;

import androidx.annotation.NonNull;
import com.basic.log.annotation.LogLevel;
import com.basic.log.annotation.LogSegment;

import java.io.File;

/**
 * 打印相关
 */
public final class PrinterUtils {

    private PrinterUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 日志打印输出到控制台.
     *
     * @param level   级别
     * @param tag     标签
     * @param message 信息
     */
    public static void printConsole(@LogLevel String level, @NonNull String tag,
                                    @NonNull String message) {
        Utils.log(level, tag, message);
    }


    /**
     * 日志打印输出到文件.【输出普通日志使用】
     *
     * @param logDirPath 日志文件根目录路径
     * @param logPrefix  日志文件前缀
     * @param logSegment 日志时间片
     * @param zoneOffset 时区
     * @param message    信息
     */
    public static void printFile(@NonNull String logDirPath, String logPrefix,
                                 @LogSegment int logSegment, @TimeUtils.ZoneOffset long zoneOffset,
                                 @NonNull String message) {
        String fileName = Utils.getLogFileName(logPrefix, logSegment, zoneOffset);
        Utils.write(logDirPath, fileName, message);
    }


    /**
     * 日志打印输出到文件.【输出崩溃日志使用】
     *
     * @param logDirPath 日志文件根目录路径
     * @param logPrefix  日志文件前缀
     * @param zoneOffset 时区
     * @param fmt        时间格式
     * @param message    信息
     */
    public static File printFile(@NonNull String logDirPath, String logPrefix,
                                 @TimeUtils.ZoneOffset long zoneOffset, @NonNull String fmt,
                                 @NonNull String message) {
        String fileName = Utils.getLogFileName(logPrefix, zoneOffset, fmt);
        Utils.write(logDirPath, fileName, message);
        return new File(logDirPath, fileName);
    }

}