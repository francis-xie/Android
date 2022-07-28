package com.basic.code.utils.update;

import com.basic.code.utils.ToastUtils;
import com.basic.renew.entity.UpdateError;
import com.basic.renew.listener.OnUpdateFailureListener;

/**
 * 自定义版本更新提示
 */
public class CustomUpdateFailureListener implements OnUpdateFailureListener {

    /**
     * 是否需要错误提示
     */
    private boolean mNeedErrorTip;

    public CustomUpdateFailureListener() {
        this(true);
    }

    public CustomUpdateFailureListener(boolean needErrorTip) {
        mNeedErrorTip = needErrorTip;
    }

    /**
     * 更新失败
     *
     * @param error 错误
     */
    @Override
    public void onFailure(UpdateError error) {
        if (mNeedErrorTip) {
            ToastUtils.error(error);
        }
        if (error.getCode() == UpdateError.ERROR.DOWNLOAD_FAILED) {
            UpdateTipDialog.show("应用下载失败，是否考虑切换" + UpdateTipDialog.DOWNLOAD_TYPE_NAME + "下载？");
        }
    }
}
