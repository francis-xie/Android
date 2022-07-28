
package com.basic.code.fragment.components.imageview.edit;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.basic.aop.annotation.Permission;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.imageview.crop.CropImageType;
import com.basic.face.widget.imageview.crop.CropImageView;
import com.basic.face.widget.textview.supertextview.SuperButton;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.tools.app.IntentUtils;
import com.basic.tools.app.PathUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.basic.aop.consts.PermissionConsts.STORAGE;
import static com.basic.code.fragment.expands.ScanFragment.REQUEST_IMAGE;

@Page(name = "图片裁剪")
public class ImageCropFragment extends BaseFragment {

    @BindView(R.id.crop_image_view)
    CropImageView mCropImageView;
    @BindView(R.id.btn_rotate)
    SuperButton btnRotate;
    @BindView(R.id.btn_crop)
    SuperButton btnCrop;


    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_crop;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        // 触摸时显示网格
        mCropImageView.setGuidelines(CropImageType.CROPIMAGE_GRID_ON);
        // 自由剪切
//        mCropImageView.setFixedAspectRatio(false);
        // 固定比例剪切
        mCropImageView.setFixedAspectRatio(true);
        mCropImageView.setAspectRatio(40, 30);

        btnRotate.setEnabled(false);
        btnCrop.setEnabled(false);
    }

    @SingleClick
    @OnClick({R.id.btn_select, R.id.btn_crop, R.id.btn_rotate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_select:
                selectImage();
                break;
            case R.id.btn_rotate:
                mCropImageView.rotateImage(90);
                break;
            case R.id.btn_crop:
                mCropImageView.cropImage();
                //使用getCroppedImage获取裁剪的图片

                btnRotate.setEnabled(false);
                btnCrop.setEnabled(false);
                break;
            default:
                break;
        }
    }

    @Permission(STORAGE)
    private void selectImage() {
        startActivityForResult(IntentUtils.getDocumentPickerIntent(IntentUtils.DocumentType.IMAGE), REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择系统图片并解析
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    mCropImageView.setImagePath(PathUtils.getFilePathByUri(uri));
                    btnRotate.setEnabled(true);
                    btnCrop.setEnabled(true);
                }
            }
        }
    }


}
