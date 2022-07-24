
package com.basic.code.fragment.components;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.refresh.BroccoliFragment;
import com.basic.code.fragment.components.refresh.RefreshHeadViewFragment;
import com.basic.code.fragment.components.refresh.SmartRefreshLayoutFragment;
import com.basic.code.fragment.components.refresh.StickyRecyclerViewFragment;
import com.basic.code.fragment.components.refresh.SwipeRecyclerViewFragment;
import com.basic.code.fragment.components.refresh.SwipeRefreshLayoutFragment;
import com.basic.code.fragment.components.refresh.sample.SampleListFragment;

/**

 * @since 2018/12/6 下午6:09
 */
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
                SmartRefreshLayoutFragment.class,
                SwipeRecyclerViewFragment.class,
                SwipeRefreshLayoutFragment.class,
                RefreshHeadViewFragment.class,
                BroccoliFragment.class,
                StickyRecyclerViewFragment.class,
                SampleListFragment.class
        };
    }
}
