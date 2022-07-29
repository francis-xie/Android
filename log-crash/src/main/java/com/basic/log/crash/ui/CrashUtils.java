package com.basic.log.crash.ui;

import android.content.Context;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 崩溃工具类
 */
public final class CrashUtils {

    private CrashUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    static CrashInfo parseCrash(Context context, Throwable ex) {
        CrashInfo model = new CrashInfo();
        try {
            model.setException(ex);
            model.setPackageName(context.getPackageName());
            model.setTime(System.currentTimeMillis());
            if (ex.getCause() != null) {
                ex = ex.getCause();
            }
            model.setExceptionMsg(ex.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            pw.flush();
            String exceptionType = ex.getClass().getName();

            if (ex.getStackTrace() != null && ex.getStackTrace().length > 0) {
                StackTraceElement element = ex.getStackTrace()[0];
                model.setLineNumber(element.getLineNumber());
                model.setClassName(element.getClassName());
                model.setFileName(element.getFileName());
                model.setMethodName(element.getMethodName());
                model.setExceptionType(exceptionType);
            }

            model.setFullException(sw.toString());
        } catch (Exception e) {
            return model;
        }
        return model;
    }

}
