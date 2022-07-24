
package com.basic.code.fragment.components.tabbar;

import com.basic.page.annotation.Page;
import com.basic.face.utils.ResUtils;
import com.basic.face.widget.tabbar.MultiTabControlView;
import com.basic.face.widget.tabbar.TabControlView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;

/**
 *
 *

 * @since 2019/1/3 上午12:41
 */
@Page(name = "TabControlView\n选项卡控制滑块")
public class TabControlViewFragment extends BaseFragment {
    @BindView(R.id.tcv_select)
    TabControlView mTabControlView;
    @BindView(R.id.tcv_multi_select)
    MultiTabControlView mMultiTabControlView;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tabcontrolview;
    }

    @Override
    protected void initViews() {
        initTabControlView();
        initMultiTabControlView();

    }

    private void initTabControlView() {
        try {
            mTabControlView.setItems(ResUtils.getStringArray(R.array.course_param_option), ResUtils.getStringArray(R.array.course_param_value));
            mTabControlView.setDefaultSelection(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTabControlView.setOnTabSelectionChangedListener((title, value) -> XToastUtils.toast("选中了：" + title + ", 选中的值为：" + value));
    }

    private void initMultiTabControlView() {
        try {
            mMultiTabControlView.setItems(ResUtils.getStringArray(R.array.course_param_option), ResUtils.getStringArray(R.array.course_param_value));
            mMultiTabControlView.setDefaultSelection(1, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMultiTabControlView.setOnMultiTabSelectionChangedListener((title, value, isChecked) -> XToastUtils.toast((isChecked ? "选中了：" : "取消了：") + title + ", 值为：" + value));
    }

}
