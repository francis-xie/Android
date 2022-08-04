package com.basic.code.fragment.components.image;

import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.View;

import com.basic.page.annotation.Page;
import com.basic.face.utils.DensityUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.dialog.bottomsheet.BottomSheet;
import com.basic.face.widget.imageview.RadiusImageView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;

@Page(name = "RadiusImageView\n圆角图片")
public class RadiusImageViewFragment extends BaseFragment {

    @BindView(R.id.radiusImageView)
    RadiusImageView mRadiusImageView;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.icon_topbar_overflow) {
            @Override
            public void performAction(View view) {
                showBottomSheetList();
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
        return R.layout.fragment_radius_imageview;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {

    }

    private void reset() {
        mRadiusImageView.setBorderColor(ContextCompat.getColor(getContext(), R.color.radiusImageView_border_color));
        mRadiusImageView.setBorderWidth(DensityUtils.dp2px(getContext(), 2));
        mRadiusImageView.setCornerRadius(DensityUtils.dp2px(getContext(), 10));
        mRadiusImageView.setSelectedMaskColor(ContextCompat.getColor(getContext(), R.color.radiusImageView_selected_mask_color));
        mRadiusImageView.setSelectedBorderColor(ContextCompat.getColor(getContext(), R.color.radiusImageView_selected_border_color));
        mRadiusImageView.setSelectedBorderWidth(DensityUtils.dp2px(getContext(), 3));
        mRadiusImageView.setTouchSelectModeEnabled(true);
        mRadiusImageView.setCircle(false);
        mRadiusImageView.setOval(false);
    }

    private void showBottomSheetList() {
        new BottomSheet.BottomListSheetBuilder(getActivity())
                .addItem(getResources().getString(R.string.circularImageView_modify_1))
                .addItem(getResources().getString(R.string.circularImageView_modify_2))
                .addItem(getResources().getString(R.string.circularImageView_modify_3))
                .addItem(getResources().getString(R.string.circularImageView_modify_4))
                .addItem(getResources().getString(R.string.circularImageView_modify_5))
                .addItem(getResources().getString(R.string.circularImageView_modify_6))
                .addItem(getResources().getString(R.string.circularImageView_modify_7))
                .addItem(getResources().getString(R.string.circularImageView_modify_8))
                .addItem(getResources().getString(R.string.circularImageView_modify_9))
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    reset();
                    switch (position) {
                        case 0:
                            mRadiusImageView.setBorderColor(Color.BLACK);
                            mRadiusImageView.setBorderWidth(DensityUtils.dp2px(getContext(), 4));
                            break;
                        case 1:
                            mRadiusImageView.setSelectedBorderWidth(DensityUtils.dp2px(getContext(), 6));
                            mRadiusImageView.setSelectedBorderColor(Color.GREEN);
                            break;
                        case 2:
                            mRadiusImageView.setSelectedMaskColor(ContextCompat.getColor(getContext(), R.color.radiusImageView_selected_mask_color));
                            break;
                        case 3:
                            if (mRadiusImageView.isSelected()) {
                                mRadiusImageView.setSelected(false);
                            } else {
                                mRadiusImageView.setSelected(true);
                            }
                            break;
                        case 4:
                            mRadiusImageView.setCornerRadius(DensityUtils.dp2px(getContext(), 20));
                            break;
                        case 5:
                            mRadiusImageView.setCircle(true);
                            break;
                        case 6:
                            mRadiusImageView.setOval(true);
                            break;
                        case 7:
                            mRadiusImageView.setTouchSelectModeEnabled(false);
                            break;
                        default:
                            break;
                    }
                })
                .build()
                .show();
    }
}
