
package com.basic.tools.display;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;

/**
 * 环形无进度条弹窗工具类
 *

 * @since 2018/9/30 下午4:02
 */
public class CProgressDialogUtils {

    private static ProgressDialog sCircleProgressDialog;

    private CProgressDialogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 显示环形加载框
     *
     * @param activity
     */
    public static void showProgressDialog(Activity activity) {
        showProgressDialog(activity, "加载中", false, null);
    }

    /**
     * 显示环形加载框
     *
     * @param activity
     * @param listener 取消的监听
     */
    public static void showProgressDialog(Activity activity, DialogInterface.OnCancelListener listener) {
        showProgressDialog(activity, "加载中", true, listener);
    }

    /**
     * 显示环形加载框
     *
     * @param activity
     * @param msg
     */
    public static void showProgressDialog(Activity activity, String msg) {
        showProgressDialog(activity, msg, false, null);
    }

    public static void showProgressDialog(Activity activity, String msg, DialogInterface.OnCancelListener listener) {
        showProgressDialog(activity, msg, true, listener);
    }

    /**
     * 显示环形加载框
     *
     * @param activity
     * @param msg
     * @param cancelable 是否可取消
     * @param listener   取消的监听
     */
    public static void showProgressDialog(final Activity activity, String msg, boolean cancelable, DialogInterface.OnCancelListener listener) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (sCircleProgressDialog == null) {
            sCircleProgressDialog = new ProgressDialog(activity);
            sCircleProgressDialog.setMessage(msg);
            sCircleProgressDialog.setOwnerActivity(activity);
            sCircleProgressDialog.setOnCancelListener(listener);
            sCircleProgressDialog.setCancelable(cancelable);
        } else {
            if (activity.equals(sCircleProgressDialog.getOwnerActivity())) {
                sCircleProgressDialog.setMessage(msg);
                sCircleProgressDialog.setCancelable(cancelable);
                sCircleProgressDialog.setOnCancelListener(listener);
            } else {
                //不相等,所以取消任何ProgressDialog
                cancelProgressDialog();
                sCircleProgressDialog = new ProgressDialog(activity);
                sCircleProgressDialog.setMessage(msg);
                sCircleProgressDialog.setCancelable(cancelable);
                sCircleProgressDialog.setOwnerActivity(activity);
                sCircleProgressDialog.setOnCancelListener(listener);
            }
        }

        if (!sCircleProgressDialog.isShowing()) {
            sCircleProgressDialog.show();
        }
    }

    /**
     * 取消弹窗
     *
     * @param activity
     */
    public static void cancelProgressDialog(Activity activity) {
        if (sCircleProgressDialog != null && sCircleProgressDialog.isShowing()) {
            if (sCircleProgressDialog.getOwnerActivity() == activity) {
                sCircleProgressDialog.cancel();
                sCircleProgressDialog = null;
            }
        }
    }

    /**
     * 取消弹窗
     */
    public static void cancelProgressDialog() {
        if (sCircleProgressDialog != null && sCircleProgressDialog.isShowing()) {
            sCircleProgressDialog.cancel();
            sCircleProgressDialog = null;
        }
    }

}
