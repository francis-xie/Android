
package com.basic.face.adapter.recyclerview.sticky;

/**
 * 粘顶布局滚动变化监听
 */
public interface OnStickyChangedListener {
    /**
     * 滚动中
     *
     * @param offset 滚动偏移
     */
    void onScrolling(int offset);

    /**
     * 不可见
     */
    void onInVisible();

    /**
     * 当高度不够滑动切换时
     */
    void onNotEnoughHighScroll();
}