
package com.basic.code.fragment.expands.materialdesign;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.adapter.FragmentAdapter;
import com.basic.face.utils.ViewUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.fragment.expands.materialdesign.behavior.SimpleListFragment;
import com.basic.code.utils.XToastUtils;
import com.basic.tools.common.CollectionUtils;

import butterknife.BindView;

@Page(name = "DrawerLayout + NavigationView\n常见主页布局")
public class DrawerLayoutFragment extends BaseFragment implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    String[] titles = new String[]{"资讯", "照片", "音乐", "电影"};

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_drawerlayout;
    }

    @Override
    protected TitleBar initTitle() {
        toolbar.setTitle("资讯");
        toolbar.inflateMenu(R.menu.menu_setting);
        toolbar.setOnMenuItemClickListener(item -> {
            XToastUtils.toast("点击了:" + item.getTitle());
            return true;
        });
        return null;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        navView.setItemIconTintList(null);
        View headerView = navView.getHeaderView(0);
        LinearLayout navHeader = headerView.findViewById(R.id.nav_header);
        navHeader.setOnClickListener(this);

        //主页
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        for (String title : titles) {
            adapter.addFragment(new SimpleListFragment(), title);
        }
        viewPager.setOffscreenPageLimit(titles.length - 1);
        viewPager.setAdapter(adapter);

        ViewUtils.setViewsFont(bottomNavigation);
        ViewUtils.clearViewLongClick(bottomNavigation, R.id.item_dashboard, R.id.item_photo, R.id.item_music, R.id.item_movie);
        ViewUtils.clearAllViewLongClick(toolbar);
    }


    @Override
    protected void initListeners() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(menuItem -> {
            XToastUtils.toast("点击了:" + menuItem.getTitle());
            return true;
        });
        fab.setOnClickListener(this);

        //主页
        viewPager.addOnPageChangeListener(this);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header:
                XToastUtils.toast("点击头部！");
                break;
            case R.id.fab:
                XToastUtils.toast("新建");
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        MenuItem item = bottomNavigation.getMenu().getItem(position);
        toolbar.setTitle(item.getTitle());
        item.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(titles, menuItem.getTitle());
        if (index != -1) {
            toolbar.setTitle(menuItem.getTitle());
            viewPager.setCurrentItem(index, true);
            return true;
        }
        return false;
    }
}
