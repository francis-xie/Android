
package com.basic.code.fragment.expands.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.page.base.PageFragment;
import com.basic.page.enums.CoreAnim;
import com.basic.scan.Scan;
import com.basic.scan.camera.CameraManager;
import com.basic.scan.ui.CaptureActivity;
import com.basic.scan.ui.CaptureFragment;
import com.basic.scan.util.QRCodeAnalyzeUtils;
import com.basic.code.R;
import com.basic.code.utils.XToastUtils;

import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * <pre>
 *     desc   : 自定义二维码扫描界面

 *     time   : 2018/5/6 下午11:48
 * </pre>
 */
@Page(name = "自定义二维码扫描", anim = CoreAnim.none)
public class CustomCaptureFragment extends PageFragment {

    private boolean mIsOpen;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_custom_capture;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        // 为二维码扫描界面设置定制化界面
        CaptureFragment captureFragment = Scan.getCaptureFragment(R.layout.layout_custom_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        captureFragment.setCameraInitCallBack(e -> {
            if (e != null) {
                CaptureActivity.showNoPermissionTip(getActivity(), (dialog, which) -> popToBack());
            } else {
                mIsOpen = Scan.isFlashLightOpen();
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    /**
     * 二维码解析回调函数
     */
    QRCodeAnalyzeUtils.AnalyzeCallback analyzeCallback = new QRCodeAnalyzeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(Scan.RESULT_TYPE, Scan.RESULT_SUCCESS);
            bundle.putString(Scan.RESULT_DATA, result);
            resultIntent.putExtras(bundle);
            setFragmentResult(RESULT_OK, resultIntent);
            popToBack();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(Scan.RESULT_TYPE, Scan.RESULT_FAILED);
            bundle.putString(Scan.RESULT_DATA, "");
            resultIntent.putExtras(bundle);
            setFragmentResult(RESULT_OK, resultIntent);
            popToBack();
        }
    };

    /**
     * 初始化监听
     */
    @Override
    protected void initListeners() {

    }

    @OnClick(R.id.ll_flash_light)
    @SingleClick
    void onClickFlashLight(View v) {
        mIsOpen = !mIsOpen;
        try {
            Scan.switchFlashLight(mIsOpen);
        } catch (RuntimeException e) {
            e.printStackTrace();
            XToastUtils.error("设备不支持闪光灯!");
        }
    }

    @Override
    public void onDestroyView() {
        //恢复设置
        CameraManager.FRAME_MARGIN_TOP = -1;
        super.onDestroyView();
    }
}
