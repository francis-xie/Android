
package com.basic.tag.badgeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

/**
 
 * Date: 16-11-15
 * E-mail:peng8350@gmail.com
 */
public interface Badgeable {
    /**
     * 显示圆点徽章
     */
    void showCirclePointBadge();

    /**
     * 显示文字徽章
     *
     * @param badgeText
     */
    void showTextBadge(String badgeText);

    /**
     * 隐藏徽章
     */
    void hiddenBadge();

    /**
     * 显示图像徽章
     *
     * @param bitmap
     */
    void showDrawableBadge(Bitmap bitmap);

    /**
     * 调用父类的onTouchEvent方法
     *
     * @param event
     * @return
     */
    boolean callSuperOnTouchEvent(MotionEvent event);

    /**
     * 拖动大于BGABadgeViewHelper.mMoveHiddenThreshold后抬起手指徽章消失的代理
     *
     * @param delegate
     */
    void setDragDismissDelegage(DragDismissDelegate delegate);

    /**
     * 是否显示徽章
     *
     * @return
     */
    boolean isShowBadge();

    BadgeViewHelper getBadgeViewHelper();

    int getWidth();

    int getHeight();

    void postInvalidate();

    ViewParent getParent();

    int getId();

    boolean getGlobalVisibleRect(Rect r);

    Context getContext();

    View getRootView();
}