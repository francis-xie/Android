
package com.basic.code.fragment.components;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.layout.AlphaViewFragment;
import com.basic.code.fragment.components.layout.ExpandableLayoutFragment;
import com.basic.code.fragment.components.layout.GroupListViewFragment;
import com.basic.code.fragment.components.layout.LinkageScrollLayoutFragment;
import com.basic.code.fragment.components.layout.FACELayoutFragment;

@Page(name = "通用布局", extra = R.drawable.ic_widget_layout)
public class LayoutFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                AlphaViewFragment.class,
                FACELayoutFragment.class,
                GroupListViewFragment.class,
                ExpandableLayoutFragment.class,
                LinkageScrollLayoutFragment.class,
        };
    }
}
