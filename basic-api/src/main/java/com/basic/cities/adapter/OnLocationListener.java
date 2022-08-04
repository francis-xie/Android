package com.basic.cities.adapter;

import com.basic.cities.model.LocatedCity;

/**
 * 定位监听
 */
public interface OnLocationListener {

    /**
     * 定位发送变化
     *
     * @param location
     * @param state
     */
    void onLocationChanged(LocatedCity location, int state);
}

