
package com.basic.code.fragment.components;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.tabbar.EasyIndicatorFragment;
import com.basic.code.fragment.components.tabbar.JPTabBarFragment;
import com.basic.code.fragment.components.tabbar.TabControlViewFragment;
import com.basic.code.fragment.components.tabbar.TabLayoutFragment;
import com.basic.code.fragment.components.tabbar.TabSegmentFragment;
import com.basic.code.fragment.components.tabbar.VerticalTabLayoutFragment;

/**
 
 * @since 2018/12/26 下午2:01
 */
@Page(name = "选项卡", extra = R.drawable.ic_widget_tabbar)
public class TabBarFragment extends ComponentContainerFragment {

    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                EasyIndicatorFragment.class,
                TabSegmentFragment.class,
                TabLayoutFragment.class,
                VerticalTabLayoutFragment.class,
                TabControlViewFragment.class,
                JPTabBarFragment.class
        };
    }
}
