package com.basic.code.fragment.components.button;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

/**
 * 可设置阴影效果的控件
 *

 * @since 2019/3/31 下午6:43
 */
@Page(name = "ShadowView\n可设置阴影效果的控件")
public class ShadowViewFragment extends BaseFragment {
    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shadow_view;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {

    }
}
