package com.basic.http2.subsciber;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.basic.http2.subsciber.impl.IProgressLoader;
import com.basic.http2.subsciber.impl.OnProgressCancelListener;

/**
 * 默认进度加载
 */
public class ProgressDialogLoader implements IProgressLoader {
    /**
     * 进度loading弹窗
     */
    private ProgressDialog mDialog;
    /**
     * 进度框取消监听
     */
    private OnProgressCancelListener mOnProgressCancelListener;

    public ProgressDialogLoader(Context context) {
        this(context, "请稍候...");
    }

    public ProgressDialogLoader(Context context, String msg) {
        mDialog = new ProgressDialog(context);
        updateMessage(msg);
    }

    @Override
    public boolean isLoading() {
        return mDialog != null && mDialog.isShowing();
    }

    @Override
    public void updateMessage(String msg) {
        if (mDialog != null) {
            mDialog.setMessage(msg);
        }
    }

    @Override
    public void showLoading() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    @Override
    public void dismissLoading() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void setCancelable(boolean flag) {
        mDialog.setCancelable(flag);
        if (flag) {
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if (mOnProgressCancelListener != null) {
                        mOnProgressCancelListener.onCancelProgress();
                    }
                }
            });
        }
    }

    @Override
    public void setOnProgressCancelListener(OnProgressCancelListener listener) {
        mOnProgressCancelListener = listener;
    }
}
