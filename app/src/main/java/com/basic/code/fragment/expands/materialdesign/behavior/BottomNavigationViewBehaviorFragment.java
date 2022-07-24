
package com.basic.code.fragment.expands.materialdesign.behavior;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.basic.page.annotation.Page;
import com.basic.face.adapter.FragmentAdapter;
import com.basic.face.utils.ViewUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;
import com.basic.tools.common.CollectionUtils;

import butterknife.BindView;

/**

 * @since 2019-05-10 00:17
 */
@Page(name = "BottomNavigationView Behavior")
public class BottomNavigationViewBehaviorFragment extends BaseFragment implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    @Override
    protected TitleBar initTitle() {
        toolbar.setNavigationIcon(R.drawable.ic_navigation_back_white);
        toolbar.setTitle("BottomNavigationView");
        toolbar.setNavigationOnClickListener(v -> popToBack());
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bottom_navigationview_behavior;
    }

    int[] menuItemIds = new int[]{R.id.item_dashboard, R.id.item_photo, R.id.item_music, R.id.item_movie};
    String[] titles = new String[]{"资讯", "照片", "音乐", "电影"};

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        for (String title : titles) {
            adapter.addFragment(new SimpleListFragment(), title);
        }
        viewPager.setOffscreenPageLimit(titles.length - 1);
        viewPager.setAdapter(adapter);
        ViewUtils.setViewsFont(bottomNavigation);
    }

    @Override
    protected void initListeners() {
        viewPager.addOnPageChangeListener(this);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        fab.setOnClickListener(v -> XToastUtils.toast("新建"));
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigation.getMenu().getItem(position).setChecked(true);
//        bottomNavigation.setSelectedItemId(menuItemIds[position]);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(titles, menuItem.getTitle());
        if (index != -1) {
            viewPager.setCurrentItem(index, true);
            return true;
        }
        return false;
    }
}
