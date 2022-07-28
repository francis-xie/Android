
package com.basic.tag.badgeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 
 * Date: 16-11-15
 * E-mail:peng8350@gmail.com
 */
public class BadgeRelativeLayout extends RelativeLayout implements Badgeable {
    private BadgeViewHelper mBadgeViewHeler;

    public BadgeRelativeLayout(Context context) {
        this(context, null);
    }

    public BadgeRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBadgeViewHeler = new BadgeViewHelper(this, context, attrs, BadgeViewHelper.BadgeGravity.RightCenter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mBadgeViewHeler.onTouchEvent(event);
    }

    @Override
    public boolean callSuperOnTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mBadgeViewHeler.drawBadge(canvas);
    }

    @Override
    public void showCirclePointBadge() {
        mBadgeViewHeler.showCirclePointBadge();
    }

    @Override
    public void showTextBadge(String badgeText) {
        mBadgeViewHeler.showTextBadge(badgeText);
    }

    @Override
    public void hiddenBadge() {
        mBadgeViewHeler.hiddenBadge();
    }

    @Override
    public void showDrawableBadge(Bitmap bitmap) {
        mBadgeViewHeler.showDrawable(bitmap);
    }

    @Override
    public void setDragDismissDelegage(DragDismissDelegate delegate) {
        mBadgeViewHeler.setDragDismissDelegage(delegate);
    }

    @Override
    public boolean isShowBadge() {
        return mBadgeViewHeler.isShowBadge();
    }

    @Override
    public BadgeViewHelper getBadgeViewHelper() {
        return mBadgeViewHeler;
    }
}