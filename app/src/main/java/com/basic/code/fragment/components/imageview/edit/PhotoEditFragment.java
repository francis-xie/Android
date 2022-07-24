
package com.basic.code.fragment.components.imageview.edit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;

import com.basic.aop.annotation.Permission;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.utils.DensityUtils;
import com.basic.face.utils.ResUtils;
import com.basic.face.widget.imageview.edit.OnPhotoEditorListener;
import com.basic.face.widget.imageview.edit.PhotoEditor;
import com.basic.face.widget.imageview.edit.PhotoEditorView;
import com.basic.face.widget.imageview.edit.PhotoFilter;
import com.basic.face.widget.imageview.edit.TextStyleBuilder;
import com.basic.face.widget.imageview.edit.ViewType;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;
import com.basic.code.utils.XToastUtils;
import com.basic.tools.app.IntentUtils;
import com.basic.tools.app.PathUtils;
import com.basic.tools.display.ImageUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.basic.aop.consts.PermissionConsts.STORAGE;
import static com.basic.code.fragment.expands.ScanFragment.REQUEST_IMAGE;

/**

 * @since 2019-10-28 10:56
 */
@Page(name = "图片编辑\n画笔、橡皮檫、文字、滤镜、保存")
public class PhotoEditFragment extends BaseFragment implements OnPhotoEditorListener {

    @BindView(R.id.photo_editor_view)
    PhotoEditorView photoEditorView;

    private PhotoEditor mPhotoEditor;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo_edit;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mPhotoEditor = new PhotoEditor.Builder(getContext(), photoEditorView)
                .setPinchTextScalable(true)
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);
    }

    @SingleClick
    @OnClick({R.id.btn_select, R.id.btn_brush, R.id.btn_text, R.id.btn_rubber, R.id.iv_undo, R.id.iv_redo, R.id.btn_filter, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_select:
                selectImage();
                break;
            case R.id.iv_undo:
                mPhotoEditor.undo();
                break;
            case R.id.iv_redo:
                mPhotoEditor.redo();
                break;
            case R.id.btn_brush:
                mPhotoEditor.setBrushColor(ResUtils.getColor(R.color.face_config_color_white))
                        .setBrushSize(DensityUtils.dp2px(5))
                        .setBrushDrawingMode(true);
                break;
            case R.id.btn_text:
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                styleBuilder.withTextColor(ResUtils.getColor(R.color.face_config_color_white));
                mPhotoEditor.addText("FACE", styleBuilder);
                break;
            case R.id.btn_rubber:
                mPhotoEditor.brushEraser();
                break;
            case R.id.btn_filter:
                mPhotoEditor.setFilterEffect(PhotoFilter.GRAY_SCALE);
                break;
            case R.id.btn_save:
                saveImage();
                break;
            default:
                break;
        }
    }

    /**
     * 保存图片
     */
    @SuppressLint("MissingPermission")
    @Permission(STORAGE)
    private void saveImage() {
        getMessageLoader("保存中...").show();
        mPhotoEditor.saveAsFile(Utils.getImageSavePath(), new PhotoEditor.OnSaveListener() {
            @Override
            public void onSuccess(@NonNull String imagePath) {
                getMessageLoader().dismiss();
                if (photoEditorView != null) {
                    photoEditorView.getSource().setImageBitmap(ImageUtils.getBitmap(imagePath));
                }
            }
            @Override
            public void onFailure(@NonNull Exception exception) {
                getMessageLoader().dismiss();
                XToastUtils.error(exception);
            }
        });
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
                    mPhotoEditor.clearAllViews();
                    @SuppressLint("MissingPermission")
                    Bitmap bitmap = ImageUtils.getBitmap(PathUtils.getFilePathByUri(uri));
                    photoEditorView.getSource().setImageBitmap(bitmap);
                }
            }
        }
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {

    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {

    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {

    }
}
