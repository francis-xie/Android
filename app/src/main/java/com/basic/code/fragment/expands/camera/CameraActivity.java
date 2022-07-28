
package com.basic.code.fragment.expands.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.basic.camera.CameraSee;
import com.basic.image.PictureSelector;
import com.basic.image.config.PictureConfig;
import com.basic.image.entity.LocalMedia;
import com.basic.aop.annotation.IOThread;
import com.basic.aop.annotation.Permission;
import com.basic.aop.annotation.Safe;
import com.basic.face.widget.alpha.FACEAlphaImageView;
import com.basic.code.R;
import com.basic.code.utils.RotateSensorHelper;
import com.basic.code.utils.Utils;
import com.basic.code.utils.XToastUtils;
import com.basic.tools.common.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.wonderkiln.camerakit.CameraView;

import static com.basic.aop.consts.PermissionConsts.CAMERA;
import static com.basic.aop.consts.PermissionConsts.STORAGE;
import static com.basic.code.fragment.expands.camera.PictureCropActivity.REQUEST_CODE_PICTURE_CROP;

/**
 * 简单的相机拍照界面
 *

 * @since 2019-09-29 13:58
 */
public class CameraActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_OPEN_CAMERA = 1245;

    private static final int[] FLASH_OPTIONS = {
      CameraSee.FLASH_AUTO,
      CameraSee.FLASH_OFF,
      CameraSee.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };
    private int mCurrentFlash;


    @BindView(R.id.iv_flash_light)
    FACEAlphaImageView ivFlashLight;
    @BindView(R.id.iv_camera_close)
    FACEAlphaImageView ivCameraClose;
    @BindView(R.id.iv_take_photo)
    FACEAlphaImageView ivTakePhoto;
    @BindView(R.id.iv_picture_select)
    FACEAlphaImageView ivPictureSelect;
    @BindView(R.id.camera_view)
    CameraSee mCameraSee;

    private Unbinder mUnbinder;

    private RotateSensorHelper mSensorHelper;


    @Permission({CAMERA, STORAGE})
    public static void open(@NonNull Activity activity) {
        activity.startActivityForResult(new Intent(activity, CameraActivity.class), REQUEST_CODE_OPEN_CAMERA);
    }

    @Permission({CAMERA, STORAGE})
    public static void open(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, CameraActivity.class), requestCode);
    }

    @Permission({CAMERA, STORAGE})
    public static void open(@NonNull Fragment fragment) {
        fragment.startActivityForResult(new Intent(fragment.getContext(), CameraActivity.class), REQUEST_CODE_OPEN_CAMERA);
    }

    @Permission({CAMERA, STORAGE})
    public static void open(@NonNull Fragment fragment, int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getContext(), CameraActivity.class), requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mUnbinder = ButterKnife.bind(this);

        if (mCameraSee != null) {
            mCameraSee.addCallback(mCallback);
        }

        List<View> views = new ArrayList<>();
        views.add(ivFlashLight);
        views.add(ivCameraClose);
        views.add(ivTakePhoto);
        views.add(ivPictureSelect);
        mSensorHelper = new RotateSensorHelper(this, views);
    }

    @OnClick({R.id.iv_camera_close, R.id.iv_flash_light, R.id.iv_take_photo, R.id.iv_picture_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_camera_close:
                finish();
                break;
            case R.id.iv_flash_light:
                if (mCameraSee != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    ivFlashLight.setImageResource(FLASH_ICONS[mCurrentFlash]);
                    mCameraSee.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                break;
            case R.id.iv_take_photo:
                if (mCameraSee != null) {
                    mCameraSee.takePicture();
                }
                break;
            case R.id.iv_picture_select:
                Utils.getPictureSelector(this)
                        .maxSelectNum(1)
                        .isCamera(false)
                        .compress(false)
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            default:
                break;
        }
    }

    /**
     * 拍照的回调
     */
    private CameraSee.Callback mCallback = new CameraSee.Callback() {
        @Override
        public void onCameraOpened(CameraSee cameraSee) {
        }

        @Override
        public void onCameraClosed(CameraSee cameraSee) {
        }

        @Override
        public void onPictureTaken(final CameraSee cameraSee, final byte[] data) {
            handlePictureTaken(data);
        }
    };

    @IOThread
    private void handlePictureTaken(byte[] data) {
        String picPath = Utils.handleOnPictureTaken(data);
        if (!StringUtils.isEmpty(picPath)) {
            PictureCropActivity.open(this, true, picPath);
        } else {
            XToastUtils.error("图片保存失败！");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    List<LocalMedia> result = PictureSelector.obtainMultipleResult(data);
                    PictureCropActivity.open(this, false, result.get(0).getPath());
                    break;
                case REQUEST_CODE_PICTURE_CROP:
                    setResult(RESULT_OK, data);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        startCamera();
    }

    @Safe
    private void startCamera() {
        if (mCameraSee != null) {
            mCameraSee.start();
        }
    }


    @Override
    protected void onPause() {
        stopCamera();
        super.onPause();
    }


    @Safe
    private void stopCamera() {
        if (mCameraSee != null) {
            mCameraSee.stop();
        }
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (isFinishing()) {
            onRelease();
        }
        super.onStop();
    }

    /**
     * 资源释放
     */
    protected void onRelease() {
        mSensorHelper.recycle();
    }
}
