package com.basic.cities.model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 定位状态
 *

 * @since 2018/12/30 下午6:28
 */
public class LocateState {
    public static final int LOCATING    = 123;
    public static final int SUCCESS     = 132;
    public static final int FAILURE     = 321;

    @IntDef({SUCCESS, FAILURE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State{}
}
