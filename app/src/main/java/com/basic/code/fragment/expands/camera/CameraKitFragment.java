
package com.basic.code.fragment.expands.camera;

import android.graphics.Bitmap;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;
import com.basic.aop.annotation.IOThread;
import com.basic.aop.annotation.MainThread;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.alpha.FACEAlphaImageView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wonderkiln.camerakit.CameraKit.Constants.FLASH_AUTO;
import static com.wonderkiln.camerakit.CameraKit.Constants.FLASH_OFF;
import static com.wonderkiln.camerakit.CameraKit.Constants.FLASH_ON;

/**
 * CameraKit 相机工具
 *
 */
@Page(name = "CameraKit")
public class CameraKitFragment extends BaseFragment {

    @BindView(R.id.camera_view)
    CameraView cameraView;
    @BindView(R.id.iv_photo)
    AppCompatImageView ivPhoto;
    @BindView(R.id.iv_flash_light)
    FACEAlphaImageView ivFlashLight;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camerakit;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/CameraKit/camerakit-android");
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {
        switchFlashIcon(cameraView.getFlash());
        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @IOThread
            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Bitmap bitmap = cameraKitImage.getBitmap();
                updatePhoto(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    @MainThread
    private void updatePhoto(Bitmap bitmap) {
        getMessageLoader().dismiss();
        ivPhoto.setImageBitmap(bitmap);
    }

    @SingleClick
    @OnClick({R.id.iv_flash_light, R.id.iv_face, R.id.iv_take_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_flash_light:
                switchFlashIcon(cameraView.toggleFlash());
                break;
            case R.id.iv_face:
                cameraView.toggleFacing();
                break;
            case R.id.iv_take_photo:
                getMessageLoader("处理中...").show();
                takePhoto();
                break;
            default:
                break;
        }
    }

    @IOThread
    private void takePhoto() {
        cameraView.captureImage();
    }

    private void switchFlashIcon(int flash) {
        switch (flash) {
            case FLASH_OFF:
                ivFlashLight.setImageResource(R.drawable.ic_flash_off);
                break;
            case FLASH_ON:
                ivFlashLight.setImageResource(R.drawable.ic_flash_on);
                break;
            case FLASH_AUTO:
                ivFlashLight.setImageResource(R.drawable.ic_flash_auto);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    public void onPause() {
        cameraView.stop();
        super.onPause();
    }
}
