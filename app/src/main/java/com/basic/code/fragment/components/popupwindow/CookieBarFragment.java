
package com.basic.code.fragment.components.popupwindow;

import android.view.Gravity;
import android.view.View;

import com.basic.page.annotation.Page;
import com.basic.face.widget.popupwindow.bar.CookieBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import butterknife.OnClick;

@Page(name = "CookieBar\n顶部和底部信息显示条")
public class CookieBarFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cookiebar;
    }

    @Override
    protected void initViews() {

    }

    @OnClick({R.id.btn_top, R.id.btn_bottom, R.id.btn_top_with_icon, R.id.btn_custom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_top:
                CookieBar.builder(getActivity())
                        .setTitle(R.string.cookie_title)
                        .setMessage(R.string.cookie_message)
                        .show();
                break;
            case R.id.btn_bottom:
                CookieBar.builder(getActivity())
                        .setTitle(R.string.cookie_title)
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage(R.string.cookie_message)
                        .setLayoutGravity(Gravity.BOTTOM)
                        .setAction(R.string.cookie_action, view13 -> XToastUtils.toast("点击消失！"))
                        .show();
                break;
            case R.id.btn_top_with_icon:
                CookieBar.builder(getActivity())
                        .setMessage(R.string.cookie_message)
                        .setDuration(-1)
                        .setActionWithIcon(R.drawable.ic_action_close, view12 -> XToastUtils.toast("点击消失！"))
                        .show();
                break;
            case R.id.btn_custom:
                CookieBar.builder(getActivity())
                        .setTitle(R.string.cookie_title)
                        .setMessage(R.string.cookie_message)
                        .setDuration(3000)
                        .setBackgroundColor(R.color.colorPrimary)
                        .setActionColor(android.R.color.white)
                        .setTitleColor(android.R.color.white)
                        .setAction(R.string.cookie_action, view1 -> XToastUtils.toast("点击消失！"))
                        .show();
                break;
            default:
                break;
        }
    }
}
