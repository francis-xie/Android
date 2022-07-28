package com.basic.code.fragment.components.guideview;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.basic.page.annotation.Page;
import com.basic.face.widget.guidview.FocusShape;
import com.basic.face.widget.guidview.GuideCaseView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.OnClick;

/**
 * 引导样式
 */
@Page(name = "GuideCaseView\n引导样式")
public class GuideCaseViewStyleFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guidecase_view_style;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {

    }

    @OnClick(R.id.btn_nofocus)
    public void noFocus() {
        new GuideCaseView.Builder(getActivity())
                .picture(R.drawable.img_guidecaseview_gain_speed_gesture)
                .build()
                .show();
    }

    @OnClick(R.id.btn_rounded_rect)
    public void roundedRectFocus(View view) {
        new GuideCaseView.Builder(getActivity())
                .focusOn(view)
                .title("这是圆角矩形聚焦框")
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .roundRectRadius(90)
                .build()
                .show();
    }


    @OnClick(R.id.btn_large_circle)
    public void focusWithLargerCircle(View view) {
        new GuideCaseView.Builder(getActivity())
                .focusOn(view)
                .focusCircleRadiusFactor(1.5)
                .title("一个巨大的圆形聚焦")
                .focusBorderColor(Color.GREEN)
                .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                .build()
                .show();
    }

    @OnClick(R.id.btn_rect_position)
    public void focusRoundRectPosition(View view) {
        new GuideCaseView.Builder(getActivity())
                .title("坐标聚焦")
                .focusRectAtPosition(600, 80, 800, 140)
                .roundRectRadius(60)
                .build()
                .show();
    }

    @OnClick(R.id.btn_background_color)
    public void focusWithBackgroundColor(View view) {
        new GuideCaseView.Builder(getActivity())
                .focusOn(view)
                .backgroundColor(Color.parseColor("#AAff0000"))
                .title("背景颜色和文字样式都可以自定义")
                .titleStyle(R.style.MyTitleStyle, Gravity.TOP | Gravity.CENTER)
                .build()
                .show();
    }

    @OnClick(R.id.btn_no_anim)
    public void noFocusAnimation(View view) {
        new GuideCaseView.Builder(getActivity())
                .focusOn(view)
                .disableFocusAnimation()
                .build()
                .show();
    }

    @OnClick(R.id.btn_custom_anim)
    public void focusWithCustomAnimation(View view) {
        Animation enterAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.gcv_alpha_in);
        Animation exitAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.gcv_alpha_out);

        final GuideCaseView guideCaseView = new GuideCaseView.Builder(getActivity())
                .focusOn(view)
                .title("自定义进入和退出动画")
                .enterAnimation(enterAnimation)
                .exitAnimation(exitAnimation)
                .build();
        guideCaseView.show();
        exitAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                guideCaseView.removeView();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private GuideCaseView guideCaseView;

    @OnClick(R.id.btn_custom_view)
    public void focusWithCustomView(View view) {
        guideCaseView = new GuideCaseView.Builder(getActivity())
                .focusOn(view)
                .customView(R.layout.layout_custom_guide_case_view, view12 -> view12.findViewById(R.id.btn_action_close).setOnClickListener(view1 -> guideCaseView.hide()))
                .closeOnTouch(false)
                .build();
        guideCaseView.show();
    }
}
