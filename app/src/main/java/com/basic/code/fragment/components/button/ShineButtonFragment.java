
package com.basic.code.fragment.components.button;

import android.view.View;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.button.shinebutton.ShineButton;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;
import com.basic.code.widget.ShineButtonDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**

 * @since 2020-01-06 17:24
 */
@Page(name = "ShineButton\n增强效果的按钮，自带闪烁的特效")
public class ShineButtonFragment extends BaseFragment implements ShineButton.OnCheckedChangeListener {
    @BindView(R.id.shine_button_1)
    ShineButton shineButton1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shine_button;
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initListeners() {
        shineButton1.setOnCheckStateChangeListener(this);
    }

    @SingleClick
    @OnClick(R.id.btn_dialog)
    public void onViewClicked(View view) {
        new ShineButtonDialog(getContext()).show();
    }

    @Override
    public void onCheckedChanged(ShineButton shineButton, boolean isChecked) {
        XToastUtils.toast("checked:" + isChecked);
    }
}
