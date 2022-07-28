
package com.basic.face.widget.layout.linkage;

/**
 * LinkageScrollLayout {@link LinkageScrollLayout}的所有子布局必须要实现的接口
 */
public interface ILinkageScroll {
    /**
     * 设置内联事件的回调接口
     *
     * @param event ChildLinkageEvent that the top/bottom view holds
     */
    void setChildLinkageEvent(ChildLinkageEvent event);

    /**
     * 获取子布局向 LinkageScrollLayout {@link LinkageScrollLayout} 提供的滚动处理接口
     */
    LinkageScrollHandler provideScrollHandler();
}
