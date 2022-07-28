package com.basic.cities;

import android.content.Context;

import androidx.annotation.StyleRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.basic.cities.adapter.OnPickListener;
import com.basic.cities.data.DBCityCenter;
import com.basic.cities.data.ICityCenter;
import com.basic.cities.model.HotCity;
import com.basic.cities.model.LocatedCity;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 城市选择器
 */
public class CitySelector {

    private static final String TAG = "CitySelector";

    private WeakReference<FragmentManager> mFragmentManager;

    private boolean mEnableAnim;
    private int mAnimStyle;
    private LocatedCity mLocation;
    private List<HotCity> mHotCities;
    private OnPickListener mOnPickListener;

    private CitySelector() {
    }

    private CitySelector(Fragment fragment) {
        mFragmentManager = new WeakReference<>(fragment.getChildFragmentManager());
    }

    private CitySelector(FragmentActivity activity) {
        mFragmentManager = new WeakReference<>(activity.getSupportFragmentManager());
    }

    public static CitySelector from(Fragment fragment) {
        return new CitySelector(fragment);
    }

    public static CitySelector from(FragmentActivity activity) {
        return new CitySelector(activity);
    }

    /**
     * 设置动画效果
     *
     * @param animStyle
     * @return
     */
    public CitySelector setAnimationStyle(@StyleRes int animStyle) {
        mAnimStyle = animStyle;
        return this;
    }

    /**
     * 设置当前已经定位的城市
     *
     * @param location
     * @return
     */
    public CitySelector setLocatedCity(LocatedCity location) {
        mLocation = location;
        return this;
    }

    public CitySelector setHotCities(List<HotCity> cities) {
        mHotCities = cities;
        return this;
    }

    /**
     * 启用动画效果，默认为false
     *
     * @param enable
     * @return
     */
    public CitySelector enableAnimation(boolean enable) {
        mEnableAnim = enable;
        return this;
    }

    /**
     * 设置选择结果的监听器
     *
     * @param listener
     * @return
     */
    public CitySelector setOnPickListener(OnPickListener listener) {
        mOnPickListener = listener;
        return this;
    }

    public void show() {
        FragmentTransaction ft = mFragmentManager.get().beginTransaction();
        final Fragment prev = mFragmentManager.get().findFragmentByTag(TAG);
        if (prev != null) {
            ft.remove(prev).commit();
            ft = mFragmentManager.get().beginTransaction();
        }
        ft.addToBackStack(null);
        final CitySelectorDialogFragment citySelectorFragment =
                CitySelectorDialogFragment.newInstance(mEnableAnim);
        citySelectorFragment.setLocatedCity(mLocation);
        citySelectorFragment.setHotCities(mHotCities);
        citySelectorFragment.setAnimationStyle(mAnimStyle);
        citySelectorFragment.setOnPickListener(mOnPickListener);
        citySelectorFragment.show(ft, TAG);
    }


    private static ICityCenter sICityCenter;

    /**
     * 设置城市信息中心的实现类
     *
     * @param sICityCenter
     */
    public static void setICityCenter(ICityCenter sICityCenter) {
        CitySelector.sICityCenter = sICityCenter;
    }

    /**
     * 获取城市信息中心的实现类
     *
     * @param context
     * @return
     */
    public static ICityCenter getICityCenter(Context context) {
        if (sICityCenter == null) {
            sICityCenter = new DBCityCenter(context);
        }
        return sICityCenter;
    }

}
