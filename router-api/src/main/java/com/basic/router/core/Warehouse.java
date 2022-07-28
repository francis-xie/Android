
package com.basic.router.core;


import com.basic.router.facade.template.IInterceptor;
import com.basic.router.facade.template.IProvider;
import com.basic.router.facade.template.IRouteGroup;
import com.basic.router.model.RouteInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存放路由信息的仓库
 */
class Warehouse {
    // Cache route and metas
    static Map<String, Class<? extends IRouteGroup>> groupsIndex = new HashMap<>();
    static Map<String, RouteInfo> routes = new HashMap<>();

    // Cache provider
    static Map<Class, IProvider> providers = new HashMap<>();
    static Map<String, RouteInfo> providersIndex = new HashMap<>();

    // Cache interceptor，因为使用的是TreeMap，且Key为Integer,从而实现拦截器的优先级
    static Map<Integer, Class<? extends IInterceptor>> interceptorsIndex = new UniqueKeyTreeMap<>("More than one interceptors use same priority [%s]");
    static List<IInterceptor> interceptors = new ArrayList<>();

    static void clear() {
        routes.clear();
        groupsIndex.clear();
        providers.clear();
        providersIndex.clear();
        interceptors.clear();
        interceptorsIndex.clear();
    }
}
