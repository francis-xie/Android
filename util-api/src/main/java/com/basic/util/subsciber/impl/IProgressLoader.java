
package com.basic.util.subsciber.impl;

/**
 * 进度条加载者实现接口
 *

 * @since 2018/6/10 下午9:27
 */
public interface IProgressLoader {
    /**
     * 当前是否在加载
     * @return
     */
    boolean isLoading();

    /**
     * 更新加载提示信息
     * @param tipMessage
     * @return
     */
    void updateMessage(String tipMessage);

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
     * @param cancelable
     */
    void setCancelable(boolean cancelable);

    /**
     * 设置取消的回掉监听
     * @param listener
     */
    void setOnProgressCancelListener(OnProgressCancelListener listener);
}
