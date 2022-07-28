
package com.basic.code.fragment.components.layout;

import com.basic.page.annotation.Page;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.layout.expandable.ExpandableHorizontalFragment;
import com.basic.code.fragment.components.layout.expandable.ExpandableRecycleViewFragment;
import com.basic.code.fragment.components.layout.expandable.ExpandableSimpleFragment;

@Page(name = "可伸缩布局\n可水平、垂直伸缩")
public class ExpandableLayoutFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                ExpandableSimpleFragment.class,
                ExpandableHorizontalFragment.class,
                ExpandableRecycleViewFragment.class
        };
    }
}
