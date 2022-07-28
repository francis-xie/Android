
package com.basic.code.fragment.expands.materialdesign.behavior;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.basic.page.annotation.Page;
import com.basic.face.adapter.FragmentAdapter;
import com.basic.face.utils.StatusBarUtils;
import com.basic.face.utils.ViewUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;
import com.basic.tools.display.Colors;

import butterknife.BindView;

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

@Page(name = "复杂详情页联动")
public class ComplexDetailsPageFragment extends BaseFragment {

    @BindView(R.id.appbar_layout_toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapse_layout)
    CollapsingToolbarLayout collapseLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.fab_scrolling)
    FloatingActionButton fabScrolling;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_complex_details_page;
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

    String[] titles = new String[]{"资讯", "娱乐", "教育"};

    @Override
    protected void initViews() {
        toolbar.setNavigationOnClickListener(v -> popToBack());

        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        tabLayout.setTabMode(MODE_FIXED);
        for (String title : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
            adapter.addFragment(new SimpleListFragment(), title);
        }
        viewPager.setOffscreenPageLimit(titles.length - 1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        ViewUtils.setViewsFont(tabLayout);
        ViewUtils.setToolbarLayoutTextFont(collapseLayout);
    }

    @Override
    protected void initListeners() {
        fabScrolling.setOnClickListener(v -> XToastUtils.toast("新建"));
        appbarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                StatusBarUtils.setStatusBarDarkMode(getActivity());
                fabScrolling.hide();
            } else {
                StatusBarUtils.setStatusBarLightMode(getActivity());
                fabScrolling.show();
            }
        });
    }
}
