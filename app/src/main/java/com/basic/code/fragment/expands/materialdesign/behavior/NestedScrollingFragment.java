
package com.basic.code.fragment.expands.materialdesign.behavior;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.basic.page.annotation.Page;
import com.basic.face.adapter.FragmentAdapter;
import com.basic.face.utils.ResUtils;
import com.basic.face.utils.StatusBarUtils;
import com.basic.face.utils.ThemeUtils;
import com.basic.face.utils.ViewUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.widget.StickyNavigationLayout;

import butterknife.BindView;

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

@Page(name = "NestedScrolling机制实现嵌套滑动")
public class NestedScrollingFragment extends BaseFragment {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.sticky_navigation_layout)
    StickyNavigationLayout stickyNavigationLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nested_scrolling;
    }

    @Override
    protected void initArgs() {
        StatusBarUtils.initStatusBarStyle(getActivity(), true);
    }

    @Override
    protected TitleBar initTitle() {
        titlebar.setLeftClickListener(v -> popToBack());
        titlebar.setImmersive(true);
        titlebar.setTitle("NestedScrolling机制实现嵌套滑动");
        initTitleBar(0);
        return null;
    }

    private String[] titles = new String[]{"资讯", "娱乐", "教育"};

    @Override
    protected void initViews() {
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
        stickyNavigationLayout.setOnScrollChangeListener(this::initTitleBar);
    }

    private void initTitleBar(float moveRatio) {
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        int backColor = (int) argbEvaluator.evaluate(moveRatio, Color.WHITE, Color.BLACK);
        Drawable wrapDrawable = DrawableCompat.wrap(ResUtils.getDrawable(getContext(), R.drawable.face_ic_navigation_back_white));
        DrawableCompat.setTint(wrapDrawable, backColor);
        int bgColor = (int) argbEvaluator.evaluate(moveRatio, Color.TRANSPARENT, ThemeUtils.resolveColor(getContext(), R.attr.face_actionbar_color));
        titlebar.setLeftImageDrawable(wrapDrawable);
        titlebar.setBackgroundColor(bgColor);
        titlebar.getCenterText().setAlpha(moveRatio);
    }

}
