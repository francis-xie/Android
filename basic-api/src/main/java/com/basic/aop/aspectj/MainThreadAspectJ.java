
package com.basic.aop.aspectj;

import android.os.Looper;

import com.basic.aop.logger.XLogger;
import com.basic.aop.util.AppExecutors;
import com.basic.aop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * <pre>
 *     desc   : 主线程切片, 保证注解的方法发生在主线程中

 *     time   : 2018/4/23 上午12:33
 * </pre>
 */
@Aspect
public class MainThreadAspectJ {

    @Pointcut("within(@com.basic.aop.annotation.MainThread *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.basic.aop.annotation.MainThread * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  //方法切入点

    @Around("method()")//在连接点进行方法替换
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            joinPoint.proceed();
        } else {
            XLogger.d(Utils.getMethodDescribeInfo(joinPoint) + " \u21E2 [当前线程]:" + Thread.currentThread().getName() + "，正在切换到主线程！");
            AppExecutors.get().mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        joinPoint.proceed();
                    } catch (Throwable e) {
                        e.printStackTrace();
                        XLogger.e(e);
                    }
                }
            });
        }
    }
}
