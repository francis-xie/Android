
package com.basic.code.fragment.expands;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.imageview.ImageLoader;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.fragment.expands.camera.CameraActivity;
import com.basic.code.fragment.expands.camera.CameraKitFragment;
import com.basic.code.fragment.expands.camera.CameraSeeActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.basic.code.fragment.expands.camera.CameraActivity.REQUEST_CODE_OPEN_CAMERA;
import static com.basic.code.fragment.expands.camera.PictureCropActivity.KEY_PICTURE_PATH;

/**
 
 * @since 2019-10-16 10:25
 */
@Page(name = "照相机", extra = R.drawable.ic_expand_camera)
public class CameraFragment extends BaseFragment {

    @BindView(R.id.iv_content)
    AppCompatImageView ivContent;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camera;
    }

    @Override
    protected void initViews() {

    }

    @SingleClick
    @OnClick({R.id.btn_camera, R.id.btn_camera_complex, R.id.btn_camera_kit})
    public void onViewClicked(View view) {
        switch(view.getId()) {
            case R.id.btn_camera:
                CameraActivity.open(this);
                break;
            case R.id.btn_camera_complex:
                CameraSeeActivity.open(this);
                break;
            case R.id.btn_camera_kit:
                openNewPage(CameraKitFragment.class);
                break;
            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_OPEN_CAMERA && resultCode == RESULT_OK) {
            if (data != null) {
                String imgPath = data.getStringExtra(KEY_PICTURE_PATH);
                ImageLoader.get().loadImage(ivContent, imgPath);
            }
        }
    }
}
