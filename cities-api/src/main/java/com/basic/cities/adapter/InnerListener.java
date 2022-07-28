package com.basic.cities.adapter;


import com.basic.cities.model.City;

/**
 * 列表的监听
 */
public interface InnerListener {
    /**
     * 选择
     *
     * @param position
     * @param city
     */
    void pick(int position, City city);

    /**
     * 定位
     */
    void locate();
}
