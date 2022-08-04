
package com.basic.aop.aspectj;

import android.view.View;

import com.basic.aop.annotation.SingleClick;
import com.basic.aop.logger.XLogger;
import com.basic.aop.util.ClickUtils;
import com.basic.aop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * <pre>
 *     desc   : 防止View被连续点击

 *     time   : 2018/4/22 下午6:38
 * </pre>
 */
@Aspect
public class SingleClickAspectJ {

    @Pointcut("within(@com.basic.aop.annotation.SingleClick *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.basic.aop.annotation.SingleClick * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(singleClick)")//在连接点进行方法替换
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, SingleClick singleClick) throws Throwable {
        View view = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof View) {
                view = (View) arg;
                break;
            }
        }
        if (view != null) {
            if (!ClickUtils.isFastDoubleClick(view, singleClick.value())) {
                joinPoint.proceed();//不是快速点击，执行原方法
            } else {
                XLogger.d(Utils.getMethodDescribeInfo(joinPoint) + ":发生快速点击，View id:" + view.getId());
            }
        }
    }




}
