
package com.basic.code.adapter.entity;

/**

 * @since 2020/4/25 1:25 AM
 */
public class StickyItem {

    /**
     * 是否顶部粘连
     */
    private boolean mIsHeadSticky;
    /**
     * 顶部标题
     */
    private String mHeadTitle;

    /**
     * 新闻信息
     */
    private NewInfo mNewInfo;


    public StickyItem(String headTitle) {
        mHeadTitle = headTitle;
        mIsHeadSticky = true;
    }

    public StickyItem(NewInfo newInfo) {
        mNewInfo = newInfo;
        mIsHeadSticky = false;
    }

    public boolean isHeadSticky() {
        return mIsHeadSticky;
    }

    public String getHeadTitle() {
        return mHeadTitle;
    }

    public StickyItem setHeadTitle(String headTitle) {
        mHeadTitle = headTitle;
        return this;
    }

    public NewInfo getNewInfo() {
        return mNewInfo;
    }

    public StickyItem setNewInfo(NewInfo newInfo) {
        mNewInfo = newInfo;
        return this;
    }
}
