package com.basic.code.fragment.components.textview.supertextview;

import android.view.Gravity;

import com.basic.page.annotation.Page;
import com.basic.face.widget.layout.ExpandableLayout;
import com.basic.face.widget.textview.badge.Badge;
import com.basic.face.widget.textview.badge.BadgeView;
import com.basic.face.widget.textview.supertextview.SuperTextView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.ToastUtils;

import butterknife.BindView;

@Page(name = "SuperTextView点击事件")
public class SuperClickFragment extends BaseFragment {
    @BindView(R.id.super_tv)
    SuperTextView superTextView;
    @BindView(R.id.super_cb_tv)
    SuperTextView superTextView_cb;
    @BindView(R.id.super_switch_tv)
    SuperTextView superTextView_switch;
    @BindView(R.id.super_message_tv)
    SuperTextView stvMessage;
    @BindView(R.id.stv_expandable)
    SuperTextView stvExpandable;
    @BindView(R.id.expandable_layout)
    ExpandableLayout mExpandableLayout;
    @BindView(R.id.stv_name)
    SuperTextView stvName;
    @BindView(R.id.stv_phone)
    SuperTextView stvPhone;

    private Badge mBadge;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_super_click;
    }

    @Override
    protected void initViews() {
        //设置空字符串用于占位
        stvMessage.setRightString("      ");
        mBadge = new BadgeView(getContext()).bindTarget(stvMessage.getRightTextView())
                .setBadgeGravity(Gravity.END | Gravity.CENTER)
                .setBadgePadding(3, true)
                .setBadgeTextSize(9, true)
                .setBadgeNumber(3);

    }

    @Override
    protected void initListeners() {
        /**
         * 根据实际需求对需要的View设置点击事件
         */
        superTextView.setOnSuperTextViewClickListener(superTextView -> ToastUtils.toast("整个item的点击事件")).setLeftTopTvClickListener(textView -> ToastUtils.toast(superTextView.getLeftTopString())).setLeftTvClickListener(textView -> ToastUtils.toast(superTextView.getLeftString())).setLeftBottomTvClickListener(textView -> ToastUtils.toast(superTextView.getLeftBottomString())).setCenterTopTvClickListener(textView -> ToastUtils.toast(superTextView.getCenterTopString())).setCenterTvClickListener(textView -> ToastUtils.toast(superTextView.getCenterString())).setCenterBottomTvClickListener(textView -> ToastUtils.toast(superTextView.getCenterBottomString())).setRightTopTvClickListener(textView -> ToastUtils.toast(superTextView.getRightTopString())).setRightTvClickListener(textView -> ToastUtils.toast(superTextView.getRightString())).setRightBottomTvClickListener(textView -> ToastUtils.toast(superTextView.getRightBottomString())).setLeftImageViewClickListener(imageView -> {
        }).setRightImageViewClickListener(imageView -> {
        });


        superTextView_cb.setOnSuperTextViewClickListener(superTextView -> superTextView.setCheckBoxChecked(!superTextView.getCheckBoxIsChecked())).setCheckBoxCheckedChangeListener((buttonView, isChecked) -> ToastUtils.toast("isChecked : " + isChecked));

        superTextView_switch.setOnSuperTextViewClickListener(superTextView -> superTextView.setSwitchIsChecked(!superTextView.getSwitchIsChecked(), false)).setSwitchCheckedChangeListener((buttonView, isChecked) -> ToastUtils.toast("isChecked : " + isChecked));

        stvMessage.setOnSuperTextViewClickListener(superTextView -> mBadge.hide(true));

        stvName.setCenterEditTextFocusChangeListener((v, hasFocus) -> ToastUtils.toast("聚焦变化：" + hasFocus));

        stvPhone.setCenterEditTextClickListener(v -> ToastUtils.toast("点击监听"));


        mExpandableLayout.setOnExpansionChangedListener((expansion, state) -> {
            if (stvExpandable != null && stvExpandable.getRightIconIV() != null) {
                stvExpandable.getRightIconIV().setRotation(expansion * 90);
            }
        });
        stvExpandable.setOnSuperTextViewClickListener(superTextView -> {
            if (mExpandableLayout != null) {
                mExpandableLayout.toggle();
            }
        });


    }

}
