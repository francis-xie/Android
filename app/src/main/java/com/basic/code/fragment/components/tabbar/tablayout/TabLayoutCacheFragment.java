
package com.basic.code.fragment.components.tabbar.tablayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.utils.ViewUtils;
import com.basic.face.utils.WidgetUtils;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.fragment.components.tabbar.tabsegment.MultiPage;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE;

@Page(name = "TabLayout+FragmentAdapter的缓存问题")
public class TabLayoutCacheFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_switch)
    AppCompatImageView ivSwitch;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private boolean mIsShowNavigationView;

    private FragmentCacheAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tablayout_cache;
    }

    @Override
    protected void initViews() {
        initTabLayout();
    }


    private void initTabLayout() {
        mAdapter = new FragmentCacheAdapter(getChildFragmentManager());
        tabLayout.setTabMode(MODE_SCROLLABLE);
        tabLayout.addOnTabSelectedListener(this);
        viewPager.setAdapter(mAdapter);
        // 设置缓存的数量
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);
    }

    @SingleClick
    @OnClick({R.id.iv_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_switch:
                refreshStatus(mIsShowNavigationView = !mIsShowNavigationView);
                break;
            default:
                break;
        }
    }

    private void refreshStatus(final boolean isShow) {
        ObjectAnimator rotation;
        ObjectAnimator tabAlpha;
        ObjectAnimator textAlpha;
        if (isShow) {
            rotation = ObjectAnimator.ofFloat(ivSwitch, "rotation", 0, -45);
            tabAlpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 0, 1);
            textAlpha = ObjectAnimator.ofFloat(tvTitle, "alpha", 1, 0);
        } else {
            rotation = ObjectAnimator.ofFloat(ivSwitch, "rotation", -45, 0);
            tabAlpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 1, 0);
            textAlpha = ObjectAnimator.ofFloat(tvTitle, "alpha", 0, 1);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotation).with(tabAlpha).with(textAlpha);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                refreshAdapter(isShow);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                switchContainer(isShow);
            }
        });
        animatorSet.setDuration(500).start();
    }

    private void refreshAdapter(boolean isShow) {
        if (isShow) {
            // 动态加载选项卡内容
            for (String page : MultiPage.getPageNames()) {
                mAdapter.addFragment(SimpleTabFragment.newInstance(page), page);
            }
            mAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(0, false);
            WidgetUtils.setTabLayoutTextFont(tabLayout);
        } else {
            mAdapter.clear();
        }
    }


    private void switchContainer(boolean isShow) {
        ViewUtils.setVisibility(tvTitle, isShow ? View.GONE : View.VISIBLE);
        ViewUtils.setVisibility(viewPager, isShow ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        XToastUtils.toast("选中了:" + tab.getText());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
