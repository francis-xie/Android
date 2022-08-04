package com.basic.log.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.basic.log.annotation.LogSegment.FOUR_HOURS;
import static com.basic.log.annotation.LogSegment.ONE_HOUR;
import static com.basic.log.annotation.LogSegment.SIX_HOURS;
import static com.basic.log.annotation.LogSegment.THREE_HOURS;
import static com.basic.log.annotation.LogSegment.TWELVE_HOURS;
import static com.basic.log.annotation.LogSegment.TWENTY_FOUR_HOURS;
import static com.basic.log.annotation.LogSegment.TWO_HOURS;

/**
 * <pre>
 *     desc   : 日志时间切片，用于文件日志记录
 * </pre>
 */
@IntDef({ONE_HOUR, TWO_HOURS, THREE_HOURS, FOUR_HOURS, SIX_HOURS, TWELVE_HOURS, TWENTY_FOUR_HOURS})
@Retention(RetentionPolicy.SOURCE)
public @interface LogSegment {
    int ONE_HOUR = 1;
    int TWO_HOURS = 2;
    int THREE_HOURS = 3;
    int FOUR_HOURS = 4;
    int SIX_HOURS = 6;
    int TWELVE_HOURS = 12;
    int TWENTY_FOUR_HOURS = 24;
}
