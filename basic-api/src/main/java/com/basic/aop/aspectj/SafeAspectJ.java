
package com.basic.aop.aspectj;

import android.text.TextUtils;

import com.basic.aop.AOP;
import com.basic.aop.annotation.Safe;
import com.basic.aop.logger.XLogger;
import com.basic.aop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * <pre>
 *     desc   : 自动try-catch的注解切片处理

 *     time   : 2018/5/14 下午10:39
 * </pre>
 */
@Aspect
public class SafeAspectJ {

    @Pointcut("within(@com.basic.aop.annotation.Safe *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.basic.aop.annotation.Safe * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(safe)")//在连接点进行方法替换
    public Object aroundJoinPoint(final ProceedingJoinPoint joinPoint, Safe safe) throws Throwable {
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            if (AOP.getIThrowableHandler() != null) {
                String flag = safe.value();
                if (TextUtils.isEmpty(flag)) {
                    flag = Utils.getMethodName(joinPoint);
                }
                result = AOP.getIThrowableHandler().handleThrowable(flag, e);
            } else {
                XLogger.e(e); //默认不做任何处理
            }
        }
        return result;
    }
}
