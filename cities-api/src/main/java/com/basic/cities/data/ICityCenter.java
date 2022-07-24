package com.basic.cities.data;

import com.basic.cities.model.City;

import java.util.List;

/**
 * 城市中心信息存储
 *

 * @since 2018/12/30 下午9:47
 */
public interface ICityCenter {

    /**
     * 获取所有城市信息
     *
     * @return 所有城市信息的集合
     */
    List<City> getAllCities();


    /**
     * 搜索城市
     *
     * @param keyword
     * @return 搜索结果
     */
    List<City> searchCity(final String keyword);
}
