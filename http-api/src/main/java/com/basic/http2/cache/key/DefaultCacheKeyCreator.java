package com.basic.http2.cache.key;

import androidx.annotation.NonNull;

import com.basic.http2.annotation.NetMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * <p>key规则 ： url-方法名(参数1名=参数1值|参数2名=参数2值|...)</p>
 */
public class DefaultCacheKeyCreator implements ICacheKeyCreator {

    @Override
    public String getCacheKey(@NonNull Method method, @NonNull Object[] args, NetMethod apiMethod) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(apiMethod.url()).append("-").append(method.getName()).append('(');
        Type[] parameters = method.getGenericParameterTypes();
        if (apiMethod.paramType() == NetMethod.JSON_OBJECT) {
            keyBuilder.append("JSON_OBJECT").append('=')
                    .append(Strings.toString(args[0]));
        } else {
            if (apiMethod.cacheKeyIndex() == NetMethod.ALL_PARAMS_INDEX) {
                for (int i = 0; i < parameters.length; i++) {
                    if (i > 0) {
                        keyBuilder.append("|");
                    }
                    keyBuilder.append(apiMethod.parameterNames()[i]).append('=')
                            .append(Strings.toString(args[i]));
                }
            } else {
                int index = apiMethod.cacheKeyIndex();
                if (index >= 0 && index < parameters.length) {
                    keyBuilder.append(apiMethod.parameterNames()[index]).append('=')
                            .append(Strings.toString(args[index]));
                }
            }
        }
        keyBuilder.append(')');
        return keyBuilder.toString();
    }
}
