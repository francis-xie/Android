package com.basic.log.strategy.log;


import com.basic.log.Log;
import com.basic.log.annotation.LogLevel;
import com.basic.log.annotation.LogSegment;
import com.basic.log.utils.FileUtils;
import com.basic.log.utils.PrinterUtils;
import com.basic.log.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *     desc   : 磁盘打印日志策略
 * </pre>
 */
public class DiskLogStrategy implements ILogStrategy {
    /**
     * 日志保存的目录.
     */
    private String mLogDir;
    /**
     * logDir设置的是否是绝对路径【默认是false：相对路径】
     */
    private boolean mAbsolutePath;
    /**
     * 日志文件的前缀.
     */
    private String mLogPrefix;
    /**
     * 切片间隔，单位小时.
     */
    @LogSegment
    private int mLogSegment;
    /**
     * 时区偏移时间.
     */
    @TimeUtils.ZoneOffset
    private long mZoneOffset;
    /**
     * 写入文件的日志级别.
     */
    private List<String> mLogLevels;

    public DiskLogStrategy(Builder builder) {
        mLogDir = builder.logDir;
        mAbsolutePath = builder.absolutePath;
        mLogPrefix = builder.logPrefix;
        mLogSegment = builder.logSegment;
        mZoneOffset = builder.zoneOffset;
        mLogLevels = builder.logLevels;
    }

    /**
     * 打印日志
     *
     * @param level
     * @param tag
     * @param message
     */
    @Override
    public void log(@LogLevel String level, String tag, String message) {
        if (mLogLevels.contains(level)) {
            synchronized (DiskLogStrategy.class) {
                PrinterUtils.printFile(getLogDirPath(), mLogPrefix, mLogSegment, mZoneOffset, message);
            }
        }
    }

    /**
     * 获取日志文件根目录的路径
     * @return
     */
    public String getLogDirPath() {
        return mAbsolutePath ? mLogDir : FileUtils.getDiskCacheDir(Log.getContext(), mLogDir);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        /**
         * 日志保存的目录.
         */
        String logDir;
        /**
         * logDir设置的是否是绝对路径【默认是相对路径】
         */
        boolean absolutePath;

        /**
         * 日志文件的前缀.
         */
        String logPrefix;
        /**
         * 切片间隔，单位小时.
         */
        @LogSegment
        int logSegment;
        /**
         * 时区偏移时间.
         */
        @TimeUtils.ZoneOffset
        long zoneOffset;

        /**
         * 写入文件的日志级别.
         */
        List<String> logLevels;

        private Builder() {
            logDir = "log";
            absolutePath = false;
            logPrefix = "";
            logSegment = LogSegment.TWENTY_FOUR_HOURS;
            zoneOffset = TimeUtils.ZoneOffset.P0800;
            logLevels = new ArrayList<>();
            logLevels.add(LogLevel.ERROR);
            logLevels.add(LogLevel.WTF);
        }

        public Builder setLogDir(String logDir) {
            this.logDir = logDir;
            return this;
        }

        public Builder setAbsolutePath(boolean absolutePath) {
            this.absolutePath = absolutePath;
            return this;
        }

        public Builder setLogPrefix(String logPrefix) {
            this.logPrefix = logPrefix;
            return this;
        }

        public Builder setLogSegment(int logSegment) {
            this.logSegment = logSegment;
            return this;
        }

        public Builder setZoneOffset(long zoneOffset) {
            this.zoneOffset = zoneOffset;
            return this;
        }

        public Builder setLogLevels(List<String> logLevels) {
            this.logLevels = logLevels;
            return this;
        }

        public Builder setLogLevels(@LogLevel String... logLevels) {
            setLogLevels(Arrays.asList(logLevels));
            return this;
        }

        public DiskLogStrategy build() {
            return new DiskLogStrategy(this);
        }


        public String getLogDir() {
            return logDir;
        }

        public boolean isAbsolutePath() {
            return absolutePath;
        }

        public String getLogPrefix() {
            return logPrefix;
        }

        public int getLogSegment() {
            return logSegment;
        }

        public long getZoneOffset() {
            return zoneOffset;
        }

        public List<String> getLogLevels() {
            return logLevels;
        }

    }
}
