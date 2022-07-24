package com.basic.cities.model;

/**
 * 热门城市
 *

 * @since 2018/12/30 下午6:27
 */
public class HotCity extends City {

    public HotCity(String name, String province, String code) {
        super(name, province, "热门城市", code);
    }
}
