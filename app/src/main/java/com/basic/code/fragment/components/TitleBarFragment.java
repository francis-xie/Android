
package com.basic.code.fragment.components;

import android.view.View;

import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;

/**
 * 导航栏使用
 */
@Page(name = "导航栏", extra = R.drawable.ic_widget_titlebar)
public class TitleBarFragment extends BaseFragment {
    @BindView(R.id.titlebar)
    TitleBar mTitleBar;

    @BindView(R.id.titlebar1)
    TitleBar mTitleBar1;
    @BindView(R.id.titlebar2)
    TitleBar mTitleBar2;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_titlebar;
    }

    @Override
    protected void initViews() {
        mTitleBar.setLeftClickListener(view -> XToastUtils.toast("点击返回")).setCenterClickListener(v -> XToastUtils.toast("点击标题")).addAction(new TitleBar.ImageAction(R.drawable.ic_add_white_24dp) {
            @Override
            public void performAction(View view) {
                XToastUtils.toast("点击更多！");
            }
        });

        mTitleBar1.setLeftClickListener(v -> XToastUtils.toast("点击返回"))
                .addAction(new TitleBar.TextAction("更多") {
                    @Override
                    public void performAction(View view) {
                        XToastUtils.toast("点击更多！");
                    }
                });

        //禁用左侧的图标及文字
        mTitleBar2.disableLeftView()
                .addAction(new TitleBar.ImageAction(R.drawable.ic_navigation_more) {
                    @Override
                    public void performAction(View view) {
                        XToastUtils.toast("点击菜单！");
                    }
                });
    }

}
