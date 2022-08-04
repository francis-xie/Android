package com.basic.cities.adapter;

import com.basic.cities.model.City;

/**
 * 城市选择监听
 */
public interface OnPickListener {
    /**
     * 选择
     *
     * @param position
     * @param city
     */
    void onPick(int position, City city);

    /**
     * 定位
     *
     * @param listener
     */
    void onLocate(OnLocationListener listener);

    /**
     * 取消
     */
    void onCancel();
}
