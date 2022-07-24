
package com.basic.code.fragment.components.tabbar.tablayout;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.basic.page.annotation.Page;
import com.basic.face.adapter.FragmentAdapter;
import com.basic.face.utils.WidgetUtils;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.fragment.components.tabbar.tabsegment.MultiPage;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;

import static com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE;

/**

 * @since 2020/4/21 12:19 AM
 */
@Page(name = "TabLayout简单使用")
public class TabLayoutSimpleFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.tab1)
    TabLayout mTabLayout1;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tablayout_simple;
    }

    @Override
    protected void initViews() {
        // 多数量的Tab,不关联ViewPager
        for (String page : MultiPage.getPageNames()) {
            mTabLayout1.addTab(mTabLayout1.newTab().setText(page));
        }
        mTabLayout1.setTabMode(MODE_SCROLLABLE);
        mTabLayout1.addOnTabSelectedListener(this);

        // 固定数量的Tab,关联ViewPager
        FragmentAdapter<SimpleTabFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        for (String page : ContentPage.getPageNames()) {
            adapter.addFragment(SimpleTabFragment.newInstance(page), page);
        }
        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        WidgetUtils.setTabLayoutTextFont(mTabLayout);
        WidgetUtils.setTabLayoutTextFont(mTabLayout1);

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
