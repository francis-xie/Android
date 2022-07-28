
package com.basic.code.fragment.expands.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basic.router.annotation.AutoWired;
import com.basic.router.launcher.Router;
import com.basic.face.widget.imageview.crop.CropImageType;
import com.basic.face.widget.imageview.crop.CropImageView;
import com.basic.code.R;
import com.basic.tools.common.StringUtils;
import com.basic.tools.display.ImageUtils;
import com.basic.tools.file.FileUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 图片裁剪页面
 *
 */
public class PictureCropActivity extends AppCompatActivity {

    public static final String KEY_PICTURE_PATH = "key_picture_path";
    public static final String KEY_IS_CAMERA = "key_is_camera";
    public static final int REQUEST_CODE_PICTURE_CROP = 1122;

    public static void open(@NonNull Activity activity, boolean isCamera, String imgPath) {
        Intent intent = new Intent(activity, PictureCropActivity.class);
        intent.putExtra(KEY_IS_CAMERA, isCamera);
        intent.putExtra(KEY_PICTURE_PATH, imgPath);
        activity.startActivityForResult(intent, REQUEST_CODE_PICTURE_CROP);
    }

    @BindView(R.id.crop_image_view)
    CropImageView mCropImageView;
    private Unbinder mUnbinder;

    @AutoWired(name = KEY_PICTURE_PATH)
    String mImgPath;
    /**
     * 是拍摄的图片
     */
    @AutoWired(name = KEY_IS_CAMERA)
    boolean mIsCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_crop);
        mUnbinder = ButterKnife.bind(this);
        Router.getInstance().inject(this);

        if (StringUtils.isEmpty(mImgPath)) {
            finish();
            return;
        }

        Bitmap bit = BitmapFactory.decodeFile(mImgPath);
        mCropImageView.setImageBitmap(bit);
        // 触摸时显示网格
        mCropImageView.setGuidelines(CropImageType.CROPIMAGE_GRID_ON);
        // 自由剪切
        mCropImageView.setFixedAspectRatio(false);
    }


    @OnClick({R.id.iv_close, R.id.iv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                handleBackPressed();
                break;
            case R.id.iv_submit:
                Bitmap bitmap = mCropImageView.getCroppedImage();
                ImageUtils.save(bitmap, mImgPath, Bitmap.CompressFormat.JPEG);

                handlePictureResult(mImgPath);
                break;
            default:
                break;
        }
    }

    private void handlePictureResult(String imgPath) {
        setResult(RESULT_OK, new Intent().putExtra(KEY_PICTURE_PATH, imgPath));
        finish();
    }

    private void handleBackPressed() {
        if (mIsCamera) {
            FileUtils.deleteFile(mImgPath);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        handleBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
