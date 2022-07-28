package com.basic.code.utils;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.basic.face.FACE;
import com.basic.face.widget.toast.XToast;

/**
 * xtoast 工具类
 */
public final class ToastUtils {


    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    static {
        XToast.Config.get()
                .setAlpha(200)
                .allowQueue(false);
    }

    //======普通土司=======//

    @MainThread
    public static void toast(@NonNull CharSequence message) {
        XToast.normal(FACE.getContext(), message).show();
    }

    @MainThread
    public static void toast(@StringRes int message) {
        XToast.normal(FACE.getContext(), message).show();
    }

    @MainThread
    public static void toast(@NonNull CharSequence message, int duration) {
        XToast.normal(FACE.getContext(), message, duration).show();
    }

    @MainThread
    public static void toast(@StringRes int message, int duration) {
        XToast.normal(FACE.getContext(), message, duration).show();
    }

    //======错误【红色】=======//

    @MainThread
    public static void error(@NonNull Throwable throwable) {
        XToast.error(FACE.getContext(), throwable.getMessage()).show();
    }

    @MainThread
    public static void error(@NonNull CharSequence message) {
        XToast.error(FACE.getContext(), message).show();
    }

    @MainThread
    public static void error(@StringRes int message) {
        XToast.error(FACE.getContext(), message).show();
    }

    @MainThread
    public static void error(@NonNull CharSequence message, int duration) {
        XToast.error(FACE.getContext(), message, duration).show();
    }

    @MainThread
    public static void error(@StringRes int message, int duration) {
        XToast.error(FACE.getContext(), message, duration).show();
    }

    //======成功【绿色】=======//

    @MainThread
    public static void success(@NonNull CharSequence message) {
        XToast.success(FACE.getContext(), message).show();
    }

    @MainThread
    public static void success(@StringRes int message) {
        XToast.success(FACE.getContext(), message).show();
    }

    @MainThread
    public static void success(@NonNull CharSequence message, int duration) {
        XToast.success(FACE.getContext(), message, duration).show();
    }

    @MainThread
    public static void success(@StringRes int message, int duration) {
        XToast.success(FACE.getContext(), message, duration).show();
    }

    //======信息【蓝色】=======//

    @MainThread
    public static void info(@NonNull CharSequence message) {
        XToast.info(FACE.getContext(), message).show();
    }

    @MainThread
    public static void info(@StringRes int message) {
        XToast.info(FACE.getContext(), message).show();
    }

    @MainThread
    public static void info(@NonNull CharSequence message, int duration) {
        XToast.info(FACE.getContext(), message, duration).show();
    }

    @MainThread
    public static void info(@StringRes int message, int duration) {
        XToast.info(FACE.getContext(), message, duration).show();
    }

    //=======警告【黄色】======//

    @MainThread
    public static void warning(@NonNull CharSequence message) {
        XToast.warning(FACE.getContext(), message).show();
    }

    @MainThread
    public static void warning(@StringRes int message) {
        XToast.warning(FACE.getContext(), message).show();
    }

    @MainThread
    public static void warning(@NonNull CharSequence message, int duration) {
        XToast.warning(FACE.getContext(), message, duration).show();
    }

    @MainThread
    public static void warning(@StringRes int message, int duration) {
        XToast.warning(FACE.getContext(), message, duration).show();
    }

}
