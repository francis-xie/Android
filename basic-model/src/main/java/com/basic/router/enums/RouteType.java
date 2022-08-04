
package com.basic.router.enums;

/**
 * 路由的类型
 */
public enum RouteType {
    ACTIVITY(0, "android.app.Activity"),
    SERVICE(1, "android.app.Service"),
    PROVIDER(2, "com.basic.router.facade.template.IProvider"),
    CONTENT_PROVIDER(-1, "android.app.ContentProvider"),
    BROADCAST(-1, ""),
    METHOD(-1, ""),
    FRAGMENT(-1, "android.app.Fragment"),
    UNKNOWN(-1, "Unknown route type");

    int id;
    String className;

    public int getId() {
        return id;
    }

    public RouteType setId(int id) {
        this.id = id;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public RouteType setClassName(String className) {
        this.className = className;
        return this;
    }

    RouteType(int id, String className) {
        this.id = id;
        this.className = className;
    }

    /**
     * 根据名称解析路由类型
     * @param name
     * @return
     */
    public static RouteType parse(String name) {
        for (RouteType routeType : RouteType.values()) {
            if (routeType.getClassName().equals(name)) {
                return routeType;
            }
        }
        return UNKNOWN;
    }
}
