
package com.basic.code.fragment.components.tabbar;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.basic.page.annotation.Page;
import com.basic.face.utils.DensityUtils;
import com.basic.face.widget.tabbar.VerticalTabLayout;
import com.basic.face.widget.tabbar.vertical.TabAdapter;
import com.basic.face.widget.tabbar.vertical.TabView;
import com.basic.face.widget.textview.badge.Badge;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.ToastUtils;
import com.basic.tools.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

@Page(name = "VerticalTabLayout\n垂直的TabLayout")
public class VerticalTabLayoutFragment extends BaseFragment {

    @BindView(R.id.tab_layout0)
    VerticalTabLayout tabLayout0;
    @BindView(R.id.tab_layout1)
    VerticalTabLayout tabLayout1;
    @BindView(R.id.tab_layout2)
    VerticalTabLayout tabLayout2;
    @BindView(R.id.tab_layout)
    VerticalTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vertical_tab_layout;
    }

    @Override
    protected void initViews() {
        mViewPager.setAdapter(new MyPagerAdapter());

        initTab0();
        initTab1();
        initTab2();
        initTab3();

    }

    private void initTab0() {
        tabLayout0.setupWithViewPager(mViewPager);
        tabLayout0.setTabBadge(2, -1);
        tabLayout0.setTabBadge(3, -1);
        tabLayout0.setTabBadge(4, -1);
        tabLayout0.setTabBadge(7, 32);
        tabLayout0.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                ToastUtils.toast("选中 " + tab.getTitle().getContent());
            }

            @Override
            public void onTabUnselected(final TabView tab, int position) {
                Util.getMainHandler().postDelayed(() -> ToastUtils.toast("选择取消 " + tab.getTitle().getContent()), 500);
            }

            @Override
            public void onTabReselected(TabView tab, int position) {
                ToastUtils.toast("重复选择 " + tab.getTitle().getContent());
            }
        });
    }

    private void initTab1() {
        tabLayout1.setupWithViewPager(mViewPager);
    }

    private void initTab2() {
        tabLayout2.setupWithViewPager(mViewPager);
        tabLayout2.setTabBadge(2, -1);
        tabLayout2.setTabBadge(8, -1);
        tabLayout2.getTabAt(3)
                .setBadge(new TabView.TabBadge.Builder().setBadgeGravity(Gravity.START | Gravity.TOP)
                .setBadgeNumber(999)
                .setOnDragStateChangedListener((dragState, badge, targetView) -> {
                    if (dragState == Badge.OnDragStateChangedListener.STATE_SUCCEED) {
                        badge.setBadgeNumber(-1).stroke(0xFFFFFFFF,1,true);
                    }
                }).build());
    }

    private void initTab3() {
        tabLayout.setTabAdapter(new MyTabAdapter());
    }


    private static class MyTabAdapter implements TabAdapter {
        List<MenuBean> menus;

        public MyTabAdapter() {
            menus = new ArrayList<>();
            Collections.addAll(menus, new MenuBean(R.drawable.menu_01_pressed, R.drawable.menu_01_none, "汇总")
                    , new MenuBean(R.drawable.menu_02_pressed, R.drawable.menu_02_none, "图表")
                    , new MenuBean(R.drawable.menu_03_pressed, R.drawable.menu_03_none, "收藏")
                    , new MenuBean(R.drawable.menu_04_pressed, R.drawable.menu_04_none, "竞拍"));
        }

        @Override
        public int getCount() {
            return menus.size();
        }

        @Override
        public TabView.TabBadge getBadge(int position) {
            return new TabView.TabBadge.Builder().setBadgeNumber(999).setBackgroundColor(0xff2faae5)
                    .setOnDragStateChangedListener((dragState, badge, targetView) -> {
                    }).build();
        }

        @Override
        public TabView.TabIcon getIcon(int position) {
            MenuBean menu = menus.get(position);
            return new TabView.TabIcon.Builder()
                    .setIcon(menu.mSelectIcon, menu.mNormalIcon)
                    .setIconGravity(Gravity.START)
                    .setIconMargin(DensityUtils.dp2px(5))
                    .setIconSize(DensityUtils.dp2px(20), DensityUtils.dp2px(20))
                    .build();
        }

        @Override
        public TabView.TabTitle getTitle(int position) {
            MenuBean menu = menus.get(position);
            return new TabView.TabTitle.Builder()
                    .setContent(menu.mTitle)
                    .setTextColor(0xFF36BC9B, 0xFF757575)
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return -1;
        }

    }

    private class MyPagerAdapter extends PagerAdapter implements TabAdapter {
        List<String> titles;

        public MyPagerAdapter() {
            titles = new ArrayList<>();
            Collections.addAll(titles, "Android", "IOS", "Web", "JAVA", "C++",
                    ".NET", "JavaScript", "Swift", "PHP", "Python", "C#", "Groovy", "SQL", "Ruby");
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public TabView.TabBadge getBadge(int position) {
            if (position == 5) {
                return new TabView.TabBadge.Builder().setBadgeNumber(666)
                        .setExactMode(true)
                        .setOnDragStateChangedListener((dragState, badge, targetView) -> {
                        }).build();
            }
            return null;
        }

        @Override
        public TabView.TabIcon getIcon(int position) {
            return null;
        }

        @Override
        public TabView.TabTitle getTitle(int position) {
            return new TabView.TabTitle.Builder()
                    .setContent(titles.get(position))
                    .setTextColor(Color.WHITE, 0xBBFFFFFF)
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView textView = new TextView(getContext());
            textView.setTextAppearance(getContext(), R.style.TextStyle_Content_Match);
            textView.setGravity(Gravity.CENTER);
            textView.setText(String.format("这个是%s页面的内容", titles.get(position)));
            container.addView(textView);
            return textView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    static class MenuBean {
        public int mSelectIcon;
        public int mNormalIcon;
        public String mTitle;

        public MenuBean(int mSelectIcon, int mNormalIcon, String mTitle) {
            this.mSelectIcon = mSelectIcon;
            this.mNormalIcon = mNormalIcon;
            this.mTitle = mTitle;
        }
    }

}
