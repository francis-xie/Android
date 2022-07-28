
package com.basic.code.fragment.components.tabbar;

import com.basic.page.annotation.Page;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.tabbar.tablayout.TabLayoutCacheFragment;
import com.basic.code.fragment.components.tabbar.tablayout.TabLayoutSimpleFragment;
import com.basic.code.fragment.components.tabbar.tablayout.TabLayoutViewPager2Fragment;

@Page(name = "TabLayout\nMaterial Design 组件")
public class TabLayoutFragment extends ComponentContainerFragment {

    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                TabLayoutSimpleFragment.class,
                TabLayoutCacheFragment.class,
                TabLayoutViewPager2Fragment.class
        };
    }
}
