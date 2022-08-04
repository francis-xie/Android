
package com.basic.tools.app.notify.builder;

import android.graphics.Bitmap;

import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationCompat;

import com.basic.tools.display.ImageUtils;

/**
 * <pre>
 *     desc   : 附带图片的通知

 *     time   : 2018/4/28 上午12:25
 * </pre>
 */
public class BigPicBuilder extends BaseBuilder {

    private Bitmap mBitmap;
    /**
     * 图片的资源id
     */
    @DrawableRes
    private int mBigPicResId;


    public BigPicBuilder setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        return this;
    }


    public BigPicBuilder setPicRes(@DrawableRes int bigPicResId) {
        mBigPicResId = bigPicResId;
        return this;
    }

    @Override
    public void beforeBuild() {
        NotificationCompat.BigPictureStyle picStyle = new NotificationCompat.BigPictureStyle();
        if (mBitmap == null || mBitmap.isRecycled()) {
            mBitmap = ImageUtils.getBitmap(mBigPicResId);
        }
        picStyle.bigPicture(mBitmap);
        picStyle.setBigContentTitle(mContentTitle);
        picStyle.setSummaryText(mSummaryText);
        setStyle(picStyle);
    }

}
