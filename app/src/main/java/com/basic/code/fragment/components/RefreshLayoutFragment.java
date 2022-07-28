
package com.basic.code.fragment.components;

import com.basic.code.fragment.components.refresh.*;
import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.refresh.RefreshLayoutsFragment;
import com.basic.code.fragment.components.refresh.sample.SampleListFragment;

@Page(name = "列表刷新", extra = R.drawable.ic_widget_refresh)
public class RefreshLayoutFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                RefreshLayoutsFragment.class,
                SwipeRecyclerViewFragment.class,
                SwipeRefreshLayoutFragment.class,
                RefreshHeadViewFragment.class,
                BroccoliFragment.class,
                StickyRecyclerViewFragment.class,
                SampleListFragment.class
        };
    }
}
