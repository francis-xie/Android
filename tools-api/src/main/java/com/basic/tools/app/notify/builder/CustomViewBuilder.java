
package com.basic.tools.app.notify.builder;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

/**
 * <pre>
 *     desc   : 自定义布局的通知

 *     time   : 2018/4/28 上午12:25
 * </pre>
 */
public class CustomViewBuilder extends BaseBuilder {

    /**
     * 自定义view
     */
    private RemoteViews mContentView;

    /**
     * 是否是高度最大的自定义view
     */
    private boolean mIsBigContentView;

    public CustomViewBuilder() {}

    public CustomViewBuilder(String packageName, int layoutId) {
        mContentView = new RemoteViews(packageName, layoutId);
    }

    /**
     * 设置自定义View
     * @param packageName
     * @param layoutId
     * @return
     */
    public CustomViewBuilder setContentView(String packageName, int layoutId) {
        mContentView = new RemoteViews(packageName, layoutId);
        return this;
    }

    /**
     * 设置自定义View
     * @param contentView
     * @return
     */
    public CustomViewBuilder setContentView(RemoteViews contentView) {
        mContentView = contentView;
        return this;
    }

    /**
     * 设置是否是高度最大的自定义view
     * @param isBigContentView
     * @return
     */
    public CustomViewBuilder setIsBigContentView(boolean isBigContentView) {
        mIsBigContentView = isBigContentView;
        return this;
    }

    /**
     * 设置TextView的文字
     * @param viewId
     * @param text
     */
    public CustomViewBuilder setTextViewText(int viewId, CharSequence text) {
        if (mContentView != null) {
            mContentView.setTextViewText(viewId, text);
        }
        return this;
    }

    /**
     * 设置图片资源
     * @param viewId
     * @param srcId
     */
    public CustomViewBuilder setImageViewResource(int viewId, int srcId) {
        if (mContentView != null) {
            mContentView.setImageViewResource(viewId, srcId);
        }
        return this;
    }

    /**
     * 设置图片资源
     * @param viewId
     * @param bitmap
     */
    public CustomViewBuilder setImageViewBitmap(int viewId, Bitmap bitmap) {
        if (mContentView != null) {
            mContentView.setImageViewBitmap(viewId, bitmap);
        }
        return this;
    }

    /**
     * 设置按钮点击事件
     * @param viewId
     * @param pendingIntent
     */
    public CustomViewBuilder setOnClickPendingIntent(int viewId, PendingIntent pendingIntent) {
        if (mContentView != null) {
            mContentView.setOnClickPendingIntent(viewId, pendingIntent);
        }
        return this;
    }

    @Override
    protected void afterBuild() {
        if (mContentView != null) {
            if (mIsBigContentView) {
                getBuilder().setCustomBigContentView(mContentView);
            } else {
                getBuilder().setCustomContentView(mContentView);
            }
        }
    }
}
