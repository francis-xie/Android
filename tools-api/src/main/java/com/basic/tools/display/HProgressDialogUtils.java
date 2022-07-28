
package com.basic.tools.display;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

/**
 * 水平进度条弹窗工具类
 */
public class HProgressDialogUtils {

    private static ProgressDialog sHorizontalProgressDialog;

    private HProgressDialogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 显示水平进度条弹窗
     *
     * @param context
     * @param msg
     * @param isShowSize 是否显示进度大小
     */
    public static void showHorizontalProgressDialog(Context context, String msg, boolean isShowSize) {
        cancel();

        if (sHorizontalProgressDialog == null) {
            sHorizontalProgressDialog = new ProgressDialog(context);
            sHorizontalProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            sHorizontalProgressDialog.setCancelable(false);
            if (isShowSize) {
                sHorizontalProgressDialog.setProgressNumberFormat("%2dMB/%1dMB");
            }

        }
        if (!TextUtils.isEmpty(msg)) {
            sHorizontalProgressDialog.setMessage(msg);
        }
        sHorizontalProgressDialog.show();

    }

    public static void setMax(long total) {
        if (sHorizontalProgressDialog != null) {
            sHorizontalProgressDialog.setMax(((int) total) / (1024 * 1024));
        }
    }

    /**
     * 消失
     */
    public static void cancel() {
        if (sHorizontalProgressDialog != null) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }

    /**
     * 设置加载进度
     *
     * @param current 进度
     */
    public static void setProgress(int current) {
        if (sHorizontalProgressDialog == null) {
            return;
        }
        sHorizontalProgressDialog.setProgress(current);
        if (sHorizontalProgressDialog.getProgress() >= sHorizontalProgressDialog.getMax()) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }

    /**
     * 设置当前文件大小
     *
     * @param current 大小【单位:B】
     */
    public static void setProgress(long current) {
        if (sHorizontalProgressDialog == null) {
            return;
        }
        sHorizontalProgressDialog.setProgress(((int) current) / (1024 * 1024)); //因为单位是MB
        if (sHorizontalProgressDialog.getProgress() >= sHorizontalProgressDialog.getMax()) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }

    /**
     * 加载中
     *
     * @param total   总大小
     * @param current 当前的大小
     */
    public static void onLoading(long total, long current) {
        if (sHorizontalProgressDialog == null) {
            return;
        }
        if (current == 0) {
            sHorizontalProgressDialog.setMax(((int) total) / (1024 * 1024));
        }
        sHorizontalProgressDialog.setProgress(((int) current) / (1024 * 1024));
        if (sHorizontalProgressDialog.getProgress() >= sHorizontalProgressDialog.getMax()) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }
}
