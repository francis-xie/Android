
package com.basic.aop.aspectj;

import android.os.Build;
import android.os.Looper;
import android.os.Trace;

import androidx.annotation.NonNull;

import com.basic.aop.annotation.DebugLog;
import com.basic.aop.logger.XLogger;
import com.basic.aop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *     desc   : 埋点记录

 *     time   : 2018/4/24 下午10:01
 * </pre>
 */
@Aspect
public class DebugLogAspectJ {

    @Pointcut("within(@com.basic.aop.annotation.DebugLog *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.basic.aop.annotation.DebugLog * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    } //方法切入点

    @Pointcut("execution(@com.basic.aop.annotation.DebugLog *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {
    } //构造器切入点


    @Around("(method() || constructor()) && @annotation(debugLog)")
    public Object logAndExecute(ProceedingJoinPoint joinPoint, DebugLog debugLog) throws Throwable {
        enterMethod(joinPoint, debugLog);

        long startNanos = System.nanoTime();
        Object result = joinPoint.proceed();
        long stopNanos = System.nanoTime();
        long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);

        exitMethod(joinPoint, debugLog, result, lengthMillis);

        return result;
    }


    /**
     * 方法执行前切入
     *
     * @param joinPoint
     */
    private void enterMethod(ProceedingJoinPoint joinPoint, DebugLog debugLog) {
        if (!XLogger.isDebug()) {
            return;
        }

        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        Class<?> cls = codeSignature.getDeclaringType(); //方法所在类
        String methodName = codeSignature.getName();    //方法名
        String[] parameterNames = codeSignature.getParameterNames(); //方法参数名集合
        Object[] parameterValues = joinPoint.getArgs(); //方法参数集合

        //记录并打印方法的信息
        StringBuilder builder = getMethodLogInfo(methodName, parameterNames, parameterValues);

        XLogger.log(debugLog.priority(), Utils.getClassName(cls), builder.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final String section = builder.toString().substring(2);
            Trace.beginSection(section);
        }
    }

    /**
     * 获取方法的日志信息
     *
     * @param methodName      方法名
     * @param parameterNames  方法参数名集合
     * @param parameterValues 方法参数值集合
     * @return
     */
    @NonNull
    private StringBuilder getMethodLogInfo(String methodName, String[] parameterNames, Object[] parameterValues) {
        StringBuilder builder = new StringBuilder("\u21E2 ");
        builder.append(methodName).append('(');
        for (int i = 0; i < parameterValues.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(parameterNames[i]).append('=');
            builder.append(Utils.toString(parameterValues[i]));
        }
        builder.append(')');

        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
        }
        return builder;
    }


    /**
     * 方法执行完毕，切出
     *
     * @param joinPoint
     * @param result       方法执行后的结果
     * @param lengthMillis 执行方法所需要的时间
     */
    private void exitMethod(ProceedingJoinPoint joinPoint, DebugLog debugLog, Object result, long lengthMillis) {
        if (!XLogger.isDebug()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.endSection();
        }

        Signature signature = joinPoint.getSignature();

        Class<?> cls = signature.getDeclaringType();
        String methodName = signature.getName();

        boolean hasReturnType = Utils.isHasReturnType(signature);

        StringBuilder builder = new StringBuilder("\u21E0 ")
                .append(methodName)
                .append(" [")
                .append(lengthMillis)
                .append("ms]");

        if (hasReturnType) {
            builder.append(" = ");
            builder.append(Utils.toString(result));
        }

        XLogger.log(debugLog.priority(), Utils.getClassName(cls), builder.toString());
    }




}
