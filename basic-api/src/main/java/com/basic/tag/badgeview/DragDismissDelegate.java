
package com.basic.tag.badgeview;

/**
 
 * Date: 16-11-15
 * E-mail:peng8350@gmail.com
 * 拖动消失的代理
 */
public interface DragDismissDelegate {

    /**
     * 拖动大于BGABadgeViewHelper.mMoveHiddenThreshold后抬起手指徽章消失的回调方法
     *
     * @param badgeable
     */
    void onDismiss(Badgeable badgeable);
}