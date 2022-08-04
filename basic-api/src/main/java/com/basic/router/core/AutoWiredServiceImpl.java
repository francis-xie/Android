
package com.basic.router.core;

import android.content.Context;
import android.util.LruCache;

import com.basic.router.annotation.Router;
import com.basic.router.facade.service.AutoWiredService;
import com.basic.router.facade.template.ISyringe;

import java.util.ArrayList;
import java.util.List;

import static com.basic.router.utils.Consts.ROUTE_SERVICE_AUTOWIRED;
import static com.basic.router.utils.Consts.SUFFIX_AUTOWIRED;

/**
 * 全局自动注入属性服务
 */
@Router(path = ROUTE_SERVICE_AUTOWIRED)
public class AutoWiredServiceImpl implements AutoWiredService {
    /**
     * 存放自动注入属性的注射器缓存
     */
    private LruCache<String, ISyringe> mClassCache;
    /**
     * 存放不需要自动注入属性的类类名
     */
    private List<String> mBlackList;

    @Override
    public void init(Context context) {
        mClassCache = new LruCache<>(66);
        mBlackList = new ArrayList<>();
    }

    @Override
    public void autoWire(Object instance) {
        String className = instance.getClass().getName();
        try {
            if (!mBlackList.contains(className)) {
                ISyringe autoWiredSyringe = mClassCache.get(className);
                if (autoWiredSyringe == null) {  // No cache.
                    //根据生成规则反射生成APT自动生成的自动依赖注入注射器，如果没有对应的类可生成，证明该类无需自动注入属性
                    autoWiredSyringe = (ISyringe) Class.forName(instance.getClass().getName() + SUFFIX_AUTOWIRED).getConstructor().newInstance();
                }
                autoWiredSyringe.inject(instance);
                mClassCache.put(className, autoWiredSyringe);
            }
        } catch (Exception ex) {
            mBlackList.add(className);    // 反射生成自动依赖注入注射器失败，证明该类无需自动注入属性
        }
    }
}
