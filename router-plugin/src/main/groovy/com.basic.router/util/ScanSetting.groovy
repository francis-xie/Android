
package com.basic.router.util

/**
 * 扫描注册配置
 */
class ScanSetting {
    static final String PLUGIN_NAME = "com.basic.router"
    /**
     * 路由表的注册代码将生成插入到该类LogisticsCenter（路由中心）中
     */
    static final String GENERATE_TO_CLASS_NAME = 'com/basic/router/core/LogisticsCenter'
    /**
     * 路由表的注册代码将生成插入的类文件名
     */
    static final String GENERATE_TO_CLASS_FILE_NAME = GENERATE_TO_CLASS_NAME + '.class'
    /**
     * 注册代码将动态生成到loadRouterMap方法中
     */
    static final String GENERATE_TO_METHOD_NAME = 'loadRouterMap'
    /**
     * annotationProcessor自动生成路由代码的包名
     */
    static final String ROUTER_CLASS_PACKAGE_NAME = 'com/basic/router/routes/'
    /**
     * 存放所有接口的包名
     */
    private static final INTERFACE_PACKAGE_NAME = 'com/basic/router/facade/template/'

    /**
     * register method name in class: {@link #GENERATE_TO_CLASS_NAME}
     */
    static final String REGISTER_METHOD_NAME = 'register'
    /**
     * 需要扫描的接口名
     */
    String interfaceName = ''

    /**
     * 包含LogisticsCenter类的jar包文件 {@link #GENERATE_TO_CLASS_NAME}
     */
    File fileContainsInitClass
    /**
     * 扫描结果 {@link #interfaceName}
     * @return 返回类名的集合
     */
    ArrayList<String> classList = new ArrayList<>()

    /**
     * 自动扫描注册的配置构造器
     * @param interfaceName 需要扫描的接口名
     */
    ScanSetting(String interfaceName){
        this.interfaceName = INTERFACE_PACKAGE_NAME + interfaceName
    }

}