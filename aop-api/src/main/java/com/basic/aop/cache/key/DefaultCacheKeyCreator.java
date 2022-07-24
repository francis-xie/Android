
package com.basic.aop.cache.key;

import com.basic.aop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;

/**
 * <p>key规则 ： 方法名(参数1名=参数1值|参数2名=参数2值|...)</p>
 *

 * @since 2019/4/7 下午3:58
 */
public class DefaultCacheKeyCreator implements ICacheKeyCreator {
    /**
     * 根据签名，自动生成缓存的Key
     *
     * @param joinPoint
     * @return
     */
    @Override
    public String getCacheKey(ProceedingJoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        String methodName = codeSignature.getName();
        String[] parameterNames = codeSignature.getParameterNames(); //方法参数名集合
        Object[] parameterValues = joinPoint.getArgs(); //方法参数集合

        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(methodName).append('(');
        for (int i = 0; i < parameterValues.length; i++) {
            if (i > 0) {
                keyBuilder.append("|");
            }
            keyBuilder.append(parameterNames[i]).append('=');
            keyBuilder.append(Utils.toString(parameterValues[i]));
        }
        keyBuilder.append(')');
        return keyBuilder.toString();
    }
}
