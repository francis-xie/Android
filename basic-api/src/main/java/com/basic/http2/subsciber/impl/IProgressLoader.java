package com.basic.http2.subsciber.impl;

/**
 * 进度条加载者实现接口
 */
public interface IProgressLoader {
    /**
     * @return 当前网络请求是否在加载
     */
    boolean isLoading();

    /**
     * 设置loading提示信息
     *
     * @param msg
     */
    void updateMessage(String msg);

    /**
     * 显示加载界面
     */
    void showLoading();

    /**
     * 隐藏加载界面
     */
    void dismissLoading();

    /**
     * 设置是否可取消
     *
     * @param flag
     */
    void setCancelable(boolean flag);

    /**
     * 设置取消的回掉监听
     *
     * @param listener
     */
    void setOnProgressCancelListener(OnProgressCancelListener listener);
}
