
package com.basic.code.fragment.components.imageview.pictureselector;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.image.PictureSelector;
import com.basic.image.config.PictureConfig;
import com.basic.image.entity.LocalMedia;
import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "PictureSelector\n图片选择")
public class PictureSelectorFragment extends BaseFragment implements ImageSelectGridAdapter.OnAddPicClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ImageSelectGridAdapter mAdapter;

    private List<LocalMedia> mSelectList = new ArrayList<>();

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/LuckSiege/PictureSelector");
            }
        });
        return titleBar;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo_picker;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter = new ImageSelectGridAdapter(getActivity(), this));
        mAdapter.setSelectList(mSelectList);
        mAdapter.setSelectMax(8);
        mAdapter.setOnItemClickListener((position, v) -> PictureSelector.create(PictureSelectorFragment.this).themeStyle(R.style.FACEPictureStyle).openExternalPreview(position, mSelectList));
    }

    @OnClick({R.id.button, R.id.button_no_camera, R.id.button_one_photo, R.id.button_photo_gif})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                Utils.getPictureSelector(this)
                        .selectionMedia(mSelectList)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.button_no_camera:
                Utils.getPictureSelector(this)
                        .selectionMedia(mSelectList)
                        .previewImage(false)
                        .isCamera(false)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.button_one_photo:
                Utils.getPictureSelector(this)
                        .selectionMedia(mSelectList)
                        .maxSelectNum(1)
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.button_photo_gif:
                Utils.getPictureSelector(this)
                        .selectionMedia(mSelectList)
                        .isGif(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    mSelectList = PictureSelector.obtainMultipleResult(data);
                    mAdapter.setSelectList(mSelectList);
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onAddPicClick() {
        Utils.getPictureSelector(this)
                .selectionMedia(mSelectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
}
