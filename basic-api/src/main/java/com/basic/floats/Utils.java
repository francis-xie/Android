
package com.basic.floats;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * 工具类
 */
final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 得到设备屏幕的高度
     */
    static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备屏幕的宽度
     */
    static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

    /**
     * 计算状态栏高度高度 getStatusBarHeight
     *
     * @return
     */
    static int getStatusBarHeight() {
        return getInternalDimensionSize(Resources.getSystem(),
                STATUS_BAR_HEIGHT_RES_NAME);
    }

    static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 旋转图片
     *
     * @param angle  旋转角度
     * @param bitmap 要旋转的图片
     * @return 旋转后的图片
     */
    static Bitmap rotate(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable Drawable
     * @return Bitmap
     */
    static Bitmap drawable2Bitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

}
