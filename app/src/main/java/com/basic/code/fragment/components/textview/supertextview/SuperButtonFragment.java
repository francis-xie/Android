package com.basic.code.fragment.components.textview.supertextview;

import android.graphics.Color;

import com.basic.page.annotation.Page;
import com.basic.face.widget.textview.supertextview.SuperButton;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

/**
 * 超级按钮  实现shape所有的属性
 */
@Page(name = "SuperButton")
public class SuperButtonFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_super_button;
    }

    @Override
    protected void initViews() {
        SuperButton superButton = new SuperButton(getContext());
        /**
         * 所有属性均可用代码动态实现
         * 以下只是展示部分方法 可根据需求选择不同的方法
         */
        superButton.setShapeType(SuperButton.RECTANGLE)
                .setShapeCornersRadius(20)
                .setShapeSolidColor(getResources().getColor(R.color.colorAccent))
                .setShapeStrokeColor(getResources().getColor(R.color.colorPrimary))
                .setShapeStrokeWidth(1)
                .setShapeStrokeDashWidth(2)
                .setShapeStrokeDashGap(5)
                .setTextGravity(SuperButton.TEXT_GRAVITY_RIGHT)
                .setShapeUseSelector(true)
                .setShapeSelectorPressedColor(Color.GRAY)
                .setShapeSelectorNormalColor(Color.RED)
                .setShapeSelectorDisableColor(getResources().getColor(R.color.colorPrimary))
                .setUseShape();
        // TODO: 动态设置切记需要在最后调用 setUseShape 才能对设置的参数生效
    }

    @Override
    protected void initListeners() {

    }
}
