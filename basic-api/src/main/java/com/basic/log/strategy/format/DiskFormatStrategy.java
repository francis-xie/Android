package com.basic.log.strategy.format;

import com.basic.log.annotation.LogLevel;
import com.basic.log.strategy.log.DiskLogStrategy;
import com.basic.log.strategy.log.ILogStrategy;
import com.basic.log.utils.TimeUtils;
import com.basic.log.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.basic.log.utils.Utils.CLASS_METHOD_LINE_FORMAT;
import static com.basic.log.utils.Utils.LINE_BREAK;
import static com.basic.log.utils.Utils.SINGLE_DIVIDER;

/**
 * <pre>
 *     desc   : 磁盘日志格式化策略
 * </pre>
 */
public class DiskFormatStrategy implements IFormatStrategy {
    /**
     * 日志需要显示的方法数
     */
    private final int mMethodCount;
    /**
     * 在方法堆栈中的偏移
     */
    private final int mMethodOffset;
    /**
     * 是否显示线程信息
     */
    private final boolean mShowThreadInfo;
    /**
     * 时区偏移时间.
     */
    @TimeUtils.ZoneOffset
    private long mZoneOffset;
    /**
     * 时间格式.
     */
    private final ThreadLocal<DateFormat> mLogDateFormat = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(TimeUtils.LOG_LINE_TIME);
        }
    };
    /**
     * 日志记录策略
     */
    private final ILogStrategy mLogStrategy;


    public DiskFormatStrategy(Builder builder) {
        mMethodCount = builder.methodCount;
        mMethodOffset = builder.methodOffset;
        mShowThreadInfo = builder.showThreadInfo;
        mZoneOffset = builder.zoneOffset;
        setTimeFormat(new SimpleDateFormat(builder.timeFormat));
        mLogStrategy = builder.logStrategy;
    }

    public DiskFormatStrategy setTimeFormat(DateFormat format) {
        if (format != null) {
            mLogDateFormat.set(format);
        }
        return this;
    }

    /**
     * 格式化日志内容
     *
     * @param level
     * @param tag
     * @param message
     */
    @Override
    public void format(@LogLevel String level, String tag, String message) {
        if (LogLevel.ERROR.equals(level)) {
            mLogStrategy.log(level, tag, getErrorLogInfo(message).toString());
        } else {
            mLogStrategy.log(level, tag, getLogInfo(level, message, mMethodCount).toString());
        }
    }

    /**
     * 获取出错信息
     * @param message
     * @return
     */
    private StringBuffer getErrorLogInfo(String message) {
        StringBuffer logSb = new StringBuffer();
        logSb.append(mLogDateFormat.get().format(TimeUtils.getCurDate(mZoneOffset))).append(mShowThreadInfo ? "  Thread: " + Thread.currentThread().getName() : "").append(LINE_BREAK);

        logSb.append(message).append(LINE_BREAK)
                .append(LINE_BREAK).append(LINE_BREAK)
                .append(SINGLE_DIVIDER).append(LINE_BREAK);

        return logSb;
    }

    /**
     * 获取打印日志信息
     *
     * @param level
     * @param info
     * @param methodCount
     * @return
     */
    private StringBuffer getLogInfo(@LogLevel String level, String info, int methodCount) {
        StringBuffer logSb = new StringBuffer();
        logSb.append(mLogDateFormat.get().format(TimeUtils.getCurDate(mZoneOffset))).append(mShowThreadInfo ? "  Thread: " + Thread.currentThread().getName() : "").append(LINE_BREAK)
                .append("stack: ");

        logSb = getStackInfo(logSb, methodCount);

        logSb.append("【").append(level).append(" info: ").append(info).append("】").append(LINE_BREAK)
                .append(LINE_BREAK).append(LINE_BREAK)
                .append(SINGLE_DIVIDER).append(LINE_BREAK);

        return logSb;

    }

    /**
     * 获取堆栈信息
     * @param logSb
     * @param methodCount
     * @return
     */
    private StringBuffer getStackInfo(StringBuffer logSb, int methodCount) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackOffset = Utils.getStackOffset(trace) + mMethodOffset;

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (methodCount + stackOffset > trace.length) {
            methodCount = trace.length - stackOffset - 1;
        }
        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            logSb.append(String.format(CLASS_METHOD_LINE_FORMAT, trace[stackIndex].getClassName(), trace[stackIndex].getMethodName(), trace[stackIndex].getLineNumber(), trace[stackIndex].getFileName()))
                    .append(LINE_BREAK);
        }
        return logSb;
    }


    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        /**
         * 日志需要显示的方法数
         */
        int methodCount;
        /**
         * 在方法堆栈中的偏移
         */
        int methodOffset;
        /**
         * 是否显示线程信息
         */
        boolean showThreadInfo;
        /**
         * 时区偏移时间.
         */
        @TimeUtils.ZoneOffset
        private long zoneOffset;
        /**
         * 时间格式.
         */
        private String timeFormat;
        /**
         * 日志记录策略
         */
        ILogStrategy logStrategy;

        private Builder() {
            methodCount = 1;
            methodOffset = 0;
            showThreadInfo = true;
            zoneOffset = TimeUtils.ZoneOffset.P0800;
            timeFormat = TimeUtils.LOG_LINE_TIME;
        }

        public int getMethodCount() {
            return methodCount;
        }

        public Builder setMethodCount(int methodCount) {
            this.methodCount = methodCount;
            return this;
        }

        public int getMethodOffset() {
            return methodOffset;
        }

        public Builder setMethodOffset(int methodOffset) {
            this.methodOffset = methodOffset;
            return this;
        }

        public boolean isShowThreadInfo() {
            return showThreadInfo;
        }

        public Builder setShowThreadInfo(boolean showThreadInfo) {
            this.showThreadInfo = showThreadInfo;
            return this;
        }

        public long getZoneOffset() {
            return zoneOffset;
        }

        public Builder setZoneOffset(long zoneOffset) {
            this.zoneOffset = zoneOffset;
            return this;
        }

        public String getTimeFormat() {
            return timeFormat;
        }

        public Builder setTimeFormat(String timeFormat) {
            this.timeFormat = timeFormat;
            return this;
        }

        public ILogStrategy getLogStrategy() {
            return logStrategy;
        }

        public Builder setLogStrategy(ILogStrategy logStrategy) {
            this.logStrategy = logStrategy;
            return this;
        }

        public DiskFormatStrategy build() {
            if (logStrategy == null) {
                logStrategy = DiskLogStrategy.newBuilder().build();
            }
            return new DiskFormatStrategy(this);
        }
    }
}
