
package com.basic.code.fragment.components.button;

import android.view.View;

import com.basic.page.annotation.Page;
import com.basic.face.widget.button.SwitchIconView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "SwitchIconView\n可切换图标的按钮")
public class SwitchIconViewFragment extends BaseFragment {

    @BindView(R.id.switchIconView1)
    SwitchIconView switchIconView1;
    @BindView(R.id.switchIconView2)
    SwitchIconView switchIconView2;
    @BindView(R.id.switchIconView3)
    SwitchIconView switchIconView3;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_switch_icon_view;
    }

    @Override
    protected void initViews() {

    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button1:
                switchIconView1.switchState();
                break;
            case R.id.button2:
                switchIconView2.switchState();
                break;
            case R.id.button3:
                switchIconView3.switchState();
                break;
            default:
                break;
        }
    }
}
