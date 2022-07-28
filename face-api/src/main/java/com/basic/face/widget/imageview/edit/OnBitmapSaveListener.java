
package com.basic.face.widget.imageview.edit;

import android.graphics.Bitmap;

/**
 * 图片保存监听
 */
public interface OnBitmapSaveListener {
    /**
     * 图片开始保存
     *
     * @param saveBitmap
     */
    void onBitmapReady(Bitmap saveBitmap);

    /**
     *
     * @param e
     */
    void onFailure(Exception e);
}
