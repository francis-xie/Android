package com.basic.face.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.StyleRes;

import com.basic.R;
import com.basic.face.widget.progress.loading.IMessageLoader;
import com.basic.face.widget.progress.loading.LoadingCancelListener;
import com.basic.face.widget.progress.loading.MiniLoadingView;

/**
 * 迷你loading加载
 */
public class MiniLoadingDialog extends BaseDialog implements IMessageLoader {

    private MiniLoadingView mLoadingView;
    private TextView mTvTipMessage;

    private LoadingCancelListener mLoadingCancelListener;

    public MiniLoadingDialog(Context context) {
        super(context, R.style.FACEDialog_Custom_MiniLoading, R.layout.face_dialog_loading_mini);
        initView(getString(R.string.face_tip_loading_message));
    }

    public MiniLoadingDialog(Context context, String tipMessage) {
        super(context, R.style.FACEDialog_Custom_MiniLoading, R.layout.face_dialog_loading_mini);
        initView(tipMessage);
    }

    public MiniLoadingDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId, R.layout.face_dialog_loading_mini);
        initView(getString(R.string.face_tip_loading_message));
    }

    public MiniLoadingDialog(Context context, @StyleRes int themeResId, String tipMessage) {
        super(context, themeResId, R.layout.face_dialog_loading_mini);
        initView(tipMessage);
    }

    private void initView(String tipMessage) {
        mLoadingView = findViewById(R.id.mini_loading_view);
        mTvTipMessage = findViewById(R.id.tv_tip_message);

        updateMessage(tipMessage);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

    }



    /**
     * 更新提示信息
     *
     * @param tipMessage 提示信息
     */
    @Override
    public void updateMessage(String tipMessage) {
        if (mTvTipMessage != null) {
            if (!TextUtils.isEmpty(tipMessage)) {
                mTvTipMessage.setText(tipMessage);
                mTvTipMessage.setVisibility(View.VISIBLE);
            } else {
                mTvTipMessage.setText("");
                mTvTipMessage.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 更新提示信息
     *
     * @param tipMessageId 提示信息的id
     */
    @Override
    public void updateMessage(int tipMessageId) {
        updateMessage(getString(tipMessageId));
    }

    @Override
    protected void performShow() {
        super.performShow();
        if (mLoadingView != null) {
            mLoadingView.start();
        }
    }

    /**
     * 隐藏加载
     */
    @Override
    public void dismiss() {
        if (mLoadingView != null) {
            mLoadingView.stop();
        }
        super.dismiss();
    }

    /**
     * 资源释放
     */
    @Override
    public void recycle() {

    }

    /**
     * 是否在加载
     *
     * @return
     */
    @Override
    public boolean isLoading() {
        return isShowing();
    }

    /**
     * 设置是否可取消
     *
     * @param flag
     */
    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        if (flag) {
            setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if (mLoadingCancelListener != null) {
                        mLoadingCancelListener.onCancelLoading();
                    }
                }
            });
        }
    }

    /**
     * 设置取消的回掉监听
     *
     * @param listener
     */
    @Override
    public void setLoadingCancelListener(LoadingCancelListener listener) {
        mLoadingCancelListener = listener;
    }
}
