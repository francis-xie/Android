package com.basic.log.strategy.format;

import com.basic.log.annotation.LogLevel;
import com.basic.log.strategy.log.ILogStrategy;
import com.basic.log.strategy.log.LogcatLogStrategy;
import com.basic.log.utils.Utils;

/**
 * <pre>
 *     desc   : 漂亮的打印格式策略
 * </pre>
 */
public class PrettyFormatStrategy implements IFormatStrategy {

    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;

    /**
     * Drawing toolbox
     */
    private static final char TOP_LEFT_CORNER = '┌';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char MIDDLE_CORNER = '├';
    private static final char HORIZONTAL_LINE = '│';
    private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    private final int methodCount;
    private final int methodOffset;
    private final boolean showThreadInfo;
    private final ILogStrategy logStrategy;
    private final String tag;

    private PrettyFormatStrategy(Builder builder) {
        methodCount = builder.methodCount;
        methodOffset = builder.methodOffset;
        showThreadInfo = builder.showThreadInfo;
        logStrategy = builder.logStrategy;
        tag = builder.tag;
    }

    /**
     * 格式化日志内容
     *
     * @param level
     * @param onceOnlyTag
     * @param message
     */
    @Override
    public void format(String level, String onceOnlyTag, String message) {
        String tag = formatTag(onceOnlyTag);

        logTopBorder(level, tag);
        logHeaderContent(level, tag, methodCount);

        //get bytes of message with system's default charset (which is UTF-8 for Android)
        byte[] bytes = message.getBytes();
        int length = bytes.length;
        if (length <= CHUNK_SIZE) {
            if (methodCount > 0) {
                logDivider(level, tag);
            }
            logContent(level, tag, message);
            logBottomBorder(level, tag);
            return;
        }
        if (methodCount > 0) {
            logDivider(level, tag);
        }
        for (int i = 0; i < length; i += CHUNK_SIZE) {
            int count = Math.min(length - i, CHUNK_SIZE);
            //create a new String with system's default charset (which is UTF-8 for Android)
            logContent(level, tag, new String(bytes, i, count));
        }
        logBottomBorder(level, tag);
    }

    private void logTopBorder(@LogLevel String level, String tag) {
        logChunk(level, tag, TOP_BORDER);
    }

    private void logHeaderContent(@LogLevel String level, String tag, int methodCount) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (showThreadInfo) {
            logChunk(level, tag, HORIZONTAL_LINE + " Thread: " + Thread.currentThread().getName());
            logDivider(level, tag);
        }
        String spaceFlag = "";

        int stackOffset = Utils.getStackOffset(trace) + methodOffset;

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (methodCount + stackOffset > trace.length) {
            methodCount = trace.length - stackOffset - 1;
        }

        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(HORIZONTAL_LINE)
                    .append(' ')
                    .append(spaceFlag)
                    .append(getSimpleClassName(trace[stackIndex].getClassName()))
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")");
            spaceFlag += "   ";
            logChunk(level, tag, builder.toString());
        }
    }

    private void logBottomBorder(@LogLevel String level, String tag) {
        logChunk(level, tag, BOTTOM_BORDER);
    }

    private void logDivider(@LogLevel String level, String tag) {
        logChunk(level, tag, MIDDLE_BORDER);
    }

    private void logContent(@LogLevel String level, String tag, String chunk) {
        String[] lines = chunk.split(System.getProperty("line.separator"));
        for (String line : lines) {
            logChunk(level, tag, HORIZONTAL_LINE + " " + line);
        }
    }

    private void logChunk(@LogLevel String level, String tag, String chunk) {
        logStrategy.log(level, tag, chunk);
    }

    private String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    private String formatTag(String tag) {
        if (!Utils.isEmpty(tag) && !Utils.equals(this.tag, tag)) {
            return this.tag + "-" + tag;
        }
        return this.tag;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        /**
         * 显示的方法数
         */
        int methodCount = 2;
        /**
         * 方法的偏移
         */
        int methodOffset = 0;
        /**
         * 是否显示线程信息
         */
        boolean showThreadInfo = true;
        /**
         * 日志打印策略
         */
        ILogStrategy logStrategy;
        /**
         * 标签
         */
        String tag = "pretty";

        private Builder() {
        }

        public Builder methodCount(int val) {
            methodCount = val;
            return this;
        }

        public Builder methodOffset(int val) {
            methodOffset = val;
            return this;
        }

        public Builder showThreadInfo(boolean val) {
            showThreadInfo = val;
            return this;
        }

        public Builder logStrategy(ILogStrategy val) {
            logStrategy = val;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public PrettyFormatStrategy build() {
            if (logStrategy == null) {
                logStrategy = new LogcatLogStrategy();
            }
            return new PrettyFormatStrategy(this);
        }
    }

}
