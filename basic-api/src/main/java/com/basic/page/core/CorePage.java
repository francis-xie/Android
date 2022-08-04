package com.basic.page.core;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * assets/page.json 页面属性类
 */
@Keep
public class CorePage implements Serializable {
    private final static long serialVersionUID = 3736359137726536495L;

    public final static String KEY_PAGE_NAME = "name";
    public final static String KEY_PAGE_CLAZZ = "classPath";
    public final static String KEY_PAGE_PARAMS = "params";
    /**
     * 页面名
     */
    @SerializedName(KEY_PAGE_NAME)
    private String mName;
    /**
     * 页面class
     */
    @SerializedName(KEY_PAGE_CLAZZ)
    private String mClazz;
    /**
     * 页面参数
     */
    @SerializedName(KEY_PAGE_PARAMS)
    private String mParams;

    public CorePage() {
    }

    /**
     * 传入参数，json object结构
     *
     * @param name
     * @param clazz
     * @param params
     */
    public CorePage(String name, String clazz, String params) {
        mName = name;
        mClazz = clazz;
        mParams = params;
    }

    public String getClazz() {
        return mClazz;
    }

    public CorePage setClazz(String clazz) {
        mClazz = clazz;
        return this;
    }

    public String getName() {
        return mName;
    }

    public CorePage setName(String name) {
        mName = name;
        return this;
    }

    public String getParams() {
        return mParams;
    }

    public CorePage setParams(String params) {
        mParams = params;
        return this;
    }

    @Override
    public String toString() {
        return "Page{" + "mName='" + mName + '\'' + ", mClazz='" + mClazz + '\'' + ", mParams='" + mParams + '\'' + '}';
    }
}
