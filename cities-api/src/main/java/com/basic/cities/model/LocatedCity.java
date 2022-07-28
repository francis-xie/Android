package com.basic.cities.model;

/**
 * 定位城市
 */
public class LocatedCity extends City {

    public LocatedCity(String name, String province, String code) {
        super(name, province, "定位城市", code);
    }
}
