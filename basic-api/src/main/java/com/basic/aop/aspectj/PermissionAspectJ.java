
package com.basic.aop.aspectj;

import com.basic.aop.AOP;
import com.basic.aop.annotation.Permission;
import com.basic.aop.logger.XLogger;
import com.basic.aop.util.PermissionUtils;
import com.basic.aop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

/**
 * <pre>
 *     desc   : 申请系统权限切片，根据注解值申请所需运行权限

 *     time   : 2018/4/22 下午8:50
 * </pre>
 */
@Aspect
public class PermissionAspectJ {
    @Pointcut("within(@com.basic.aop.annotation.Permission *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.basic.aop.annotation.Permission * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(permission)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, Permission permission) throws Throwable {
        PermissionUtils.permission(permission.value())
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        try {
                            joinPoint.proceed();//获得权限，执行原方法
                        } catch (Throwable e) {
                            e.printStackTrace();
                            XLogger.e(e);
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        XLogger.e("权限申请被拒绝:" + Utils.listToString(permissionsDenied));
                        if (AOP.getOnPermissionDeniedListener() != null) {
                            AOP.getOnPermissionDeniedListener().onDenied(permissionsDenied);
                        }
                    }
                })
                .request();
    }



}


