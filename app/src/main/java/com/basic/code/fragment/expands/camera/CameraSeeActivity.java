
package com.basic.code.fragment.expands.camera;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.basic.camera.AspectRatio;
import com.basic.camera.CameraSee;
import com.basic.aop.annotation.IOThread;
import com.basic.aop.annotation.Safe;
import com.basic.aop.annotation.SingleClick;
import com.basic.code.R;
import com.basic.code.utils.Utils;
import com.basic.code.utils.ToastUtils;
import com.basic.tools.common.StringUtils;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.basic.code.fragment.expands.camera.CameraActivity.REQUEST_CODE_OPEN_CAMERA;
import static com.basic.code.fragment.expands.camera.PictureCropActivity.REQUEST_CODE_PICTURE_CROP;

/**
 * 功能齐全的相机拍摄页面
 *
 */
public class CameraSeeActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        AspectRatioFragment.Listener {
    private static final String TAG = "CameraSeeActivity";

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private static final String FRAGMENT_DIALOG = "dialog";

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

    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.camera)
    CameraSee mCameraSee;

    private int mCurrentFlash;

    private Unbinder mUnbinder;

    public static void open(@NonNull Activity activity) {
        activity.startActivityForResult(new Intent(activity, CameraSeeActivity.class), REQUEST_CODE_OPEN_CAMERA);
    }

    public static void open(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, CameraSeeActivity.class), requestCode);
    }

    public static void open(@NonNull Fragment fragment) {
        fragment.startActivityForResult(new Intent(fragment.getContext(), CameraSeeActivity.class), REQUEST_CODE_OPEN_CAMERA);
    }

    public static void open(@NonNull Fragment fragment, int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getContext(), CameraSeeActivity.class), requestCode);
    }

    @SingleClick
    @OnClick({R.id.iv_camera_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_camera_button:
                if (mCameraSee != null) {
                    mCameraSee.takePicture();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
        mUnbinder = ButterKnife.bind(this);

        if (mCameraSee != null) {
            mCameraSee.addCallback(mCallback);
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.toast(R.string.camera_permission_not_granted);
                }
                // No need to start camera here; it is handled by onResume
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aspect_ratio:
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (mCameraSee != null && fragmentManager.findFragmentByTag(FRAGMENT_DIALOG) == null) {
                    final Set<AspectRatio> ratios = mCameraSee.getSupportedAspectRatios();
                    final AspectRatio currentRatio = mCameraSee.getAspectRatio();
                    AspectRatioFragment.newInstance(ratios, currentRatio).show(fragmentManager, FRAGMENT_DIALOG);
                }
                return true;
            case R.id.switch_flash:
                if (mCameraSee != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
                    mCameraSee.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                return true;
            case R.id.switch_camera:
                if (Camera.getNumberOfCameras() > 1) {
                    if (mCameraSee != null) {
                        int facing = mCameraSee.getFacing();
                        mCameraSee.setFacing(facing == CameraSee.FACING_FRONT ? CameraSee.FACING_BACK : CameraSee.FACING_FRONT);
                    }
                } else {
                    ToastUtils.error("当前设备不支持切换摄像头！");
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAspectRatioSelected(@NonNull AspectRatio ratio) {
        if (mCameraSee != null) {
            ToastUtils.toast(ratio.toString());
            mCameraSee.setAspectRatio(ratio);
        }
    }

    /**
     * 拍照的回调
     */
    private CameraSee.Callback mCallback = new CameraSee.Callback() {
        @Override
        public void onCameraOpened(CameraSee cameraSee) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraSee cameraSee) {
            Log.d(TAG, "onCameraClosed");
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
            ToastUtils.error("图片保存失败！");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICTURE_CROP) {
            setResult(RESULT_OK, data);
            finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ConfirmationDialogFragment
                    .newInstance(R.string.camera_permission_confirmation,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_PERMISSION,
                            R.string.camera_permission_not_granted)
                    .show(getSupportFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    protected void onPause() {
        stopCamera();
        super.onPause();
    }

    @Safe
    private void startCamera() {
        if (mCameraSee != null) {
            mCameraSee.start();
        }
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

    public static class ConfirmationDialogFragment extends DialogFragment {
        private static final String ARG_MESSAGE = "message";
        private static final String ARG_PERMISSIONS = "permissions";
        private static final String ARG_REQUEST_CODE = "request_code";
        private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";

        public static ConfirmationDialogFragment newInstance(@StringRes int message,
                                                             String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
            ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_MESSAGE, message);
            args.putStringArray(ARG_PERMISSIONS, permissions);
            args.putInt(ARG_REQUEST_CODE, requestCode);
            args.putInt(ARG_NOT_GRANTED_MESSAGE, notGrantedMessage);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Bundle args = getArguments();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(args.getInt(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok,
                            (dialog, which) -> {
                                String[] permissions = args.getStringArray(ARG_PERMISSIONS);
                                if (permissions == null) {
                                    throw new IllegalArgumentException();
                                }
                                ActivityCompat.requestPermissions(getActivity(),
                                        permissions, args.getInt(ARG_REQUEST_CODE));
                            })
                    .setNegativeButton(android.R.string.cancel,
                            (dialog, which) -> ToastUtils.toast(args.getInt(ARG_NOT_GRANTED_MESSAGE)))
                    .create();
        }

    }

}
