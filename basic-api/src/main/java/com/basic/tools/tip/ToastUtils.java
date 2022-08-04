package com.basic.tools.tip;

import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.R;
import com.basic.tools.Util;
import com.basic.tools.resource.ResUtils;

/**
 * <pre>
 *     desc   : 管理toast的类，整个app用该类来显示toast

 *     time   : 2018/4/27 下午8:38
 * </pre>
 */
public final class ToastUtils {

    private static Toast sToast = null;

    /**
     * 显示toast在主线程中
     *
     * @param text     提示信息
     * @param duration 提示长度
     */
    public static void toast(final String text, final int duration) {
        if (isMainLooper()) {
            showToast(text, duration);
        } else {
            Util.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast(text, duration);
                }
            });
        }
    }

    /**
     * 是否是主线程
     *
     * @return
     */
    private static boolean isMainLooper() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 显示toast在主线程中
     *
     * @param text
     */
    public static void toast(String text) {
        toast(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示toast在主线程中
     *
     * @param resId
     */
    public static void toast(int resId) {
        toast(ResUtils.getString(resId));
    }

    /**
     * 显示单一的toast
     *
     * @param text     提示信息
     * @param duration 提示长度
     */
    private static void showToast(String text, int duration) {
        if (sToast == null) {
            sToast = makeText(Util.getContext(), text, duration);
        } else {
            ((TextView) sToast.getView().findViewById(R.id.tv_info)).setText(text);
        }
        sToast.show();
    }


    /**
     * 构建Toast
     *
     * @param context
     * @param msg
     * @param duration
     * @return
     */
    private static Toast makeText(Context context, String msg, int duration) {
        View view = LayoutInflater.from(context).inflate(R.layout.util_layout_toast, null);
        Toast toast = new Toast(context);
        toast.setView(view);
        TextView tv = view.findViewById(R.id.tv_info);
        if (tv != null) {
            tv.setText(msg);
            if (tv.getBackground() != null) {
                tv.getBackground().setAlpha(100);
            }
        }
        toast.setDuration(duration);
        return toast;
    }

    /**
     * 取消toast显示
     */
    public static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
        }
    }
}
