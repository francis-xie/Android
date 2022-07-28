
package com.basic.code.fragment.utils.view;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basic.page.annotation.Page;
import com.basic.face.utils.ViewUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.dialog.bottomsheet.BottomSheet;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;

import static com.basic.face.utils.ViewUtils.Direction.BOTTOM_TO_TOP;
import static com.basic.face.utils.ViewUtils.Direction.LEFT_TO_RIGHT;
import static com.basic.face.utils.ViewUtils.Direction.TOP_TO_BOTTOM;

@Page(name = "控件动画")
public class ViewAnimationFragment extends BaseFragment {

    @BindView(R.id.popup)
    TextView mPopupView;

    @BindView(R.id.container)
    ViewGroup mContainer;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_view_animation;
    }

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

    @Override
    protected void initViews() {

    }


    private void showBottomSheetList() {
        new BottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("Fade 动画")
                .addItem("Slide（上进上出） 动画")
                .addItem("Slide（左进右出） 动画")
                .addItem("背景闪烁（黄色）")
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    switch (position) {
                        case 0:
                            if (mPopupView.getVisibility() == View.GONE) {
                                mPopupView.setText("以 Fade 动画显示本浮层");
                                ViewUtils.fadeIn(mPopupView, 500, null);
                            } else {
                                mPopupView.setText("以 Fade 动画隐藏本浮层");
                                ViewUtils.fadeOut(mPopupView, 500, null);
                            }
                            break;
                        case 1:
                            if (mPopupView.getVisibility() == View.GONE) {
                                mPopupView.setText("以 Slide（上进上出）动画显示本浮层");
                                ViewUtils.slideIn(mPopupView, 500, null, TOP_TO_BOTTOM);
                            } else {
                                mPopupView.setText("以 Slide（上进上出）动画隐藏本浮层");
                                ViewUtils.slideOut(mPopupView, 500, null, BOTTOM_TO_TOP);
                            }
                            break;
                        case 2:
                            if (mPopupView.getVisibility() == View.GONE) {
                                mPopupView.setText("以 Slide（左进右出）动画显示本浮层");
                                ViewUtils.slideIn(mPopupView, 500, null, LEFT_TO_RIGHT);
                            } else {
                                mPopupView.setText("以 Slide（左进右出）动画隐藏本浮层");
                                ViewUtils.slideOut(mPopupView, 500, null, LEFT_TO_RIGHT);
                            }
                            break;
                        case 3:
                            ViewUtils.playBackgroundBlinkAnimation(mContainer, ContextCompat.getColor(getContext(), R.color.face_config_color_pure_yellow));
                            break;
                        default:
                            break;
                    }
                })
                .build()
                .show();
    }
}
