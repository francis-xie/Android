
package com.basic.code.fragment.components.layout;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

@Page(name = "AlphaView\n点击改变背景透明度的控件")
public class AlphaViewFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_alpha_view;
    }

    @Override
    protected void initViews() {

    }
}
