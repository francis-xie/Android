
package com.basic.log.annotation;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.basic.log.annotation.LogLevel.DEBUG;
import static com.basic.log.annotation.LogLevel.ERROR;
import static com.basic.log.annotation.LogLevel.INFO;
import static com.basic.log.annotation.LogLevel.JSON;
import static com.basic.log.annotation.LogLevel.VERBOSE;
import static com.basic.log.annotation.LogLevel.WARN;
import static com.basic.log.annotation.LogLevel.WTF;

/**
 * <pre>
 *     desc   : 日志级别
 * </pre>
 */
@StringDef({VERBOSE, DEBUG, JSON, INFO, WARN, ERROR, WTF})
@Retention(RetentionPolicy.SOURCE)
public @interface LogLevel {
    String VERBOSE = "VERBOSE";
    String DEBUG = "DEBUG";
    String JSON = "JSON";
    String INFO = "INFO";
    String WARN = "WARN";
    String ERROR = "ERROR";
    String WTF = "WTF";

}
