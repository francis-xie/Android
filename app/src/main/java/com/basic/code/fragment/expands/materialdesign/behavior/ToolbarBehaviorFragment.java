package com.basic.code.fragment.expands.materialdesign.behavior;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.basic.page.annotation.Page;
import com.basic.face.utils.StatusBarUtils;
import com.basic.face.utils.ViewUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;
import com.basic.tools.display.Colors;

import butterknife.BindView;

@Page(name = "CoordinatorLayout + AppBarLayout\n详情页常用组合")
public class ToolbarBehaviorFragment extends BaseFragment {
    @BindView(R.id.appbar_layout_toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapse_layout)
    CollapsingToolbarLayout collapseLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.fab_scrolling)
    FloatingActionButton fabScrolling;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_behavior_toolbar;
    }

    @Override
    protected void initArgs() {
        super.initArgs();
        StatusBarUtils.translucent(getActivity(), Colors.TRANSPARENT);
        StatusBarUtils.setStatusBarLightMode(getActivity());

    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        toolbar.inflateMenu(R.menu.menu_search);
        toolbar.setNavigationOnClickListener(v -> popToBack());
        ViewUtils.setToolbarLayoutTextFont(collapseLayout);
        fabScrolling.setOnClickListener(v -> XToastUtils.toast("分享"));

        appbarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                StatusBarUtils.setStatusBarDarkMode(getActivity());
            } else {
                StatusBarUtils.setStatusBarLightMode(getActivity());
            }
        });
    }

}
