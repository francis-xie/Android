
package com.basic.code.fragment.components.refresh;

import com.basic.page.annotation.Page;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.refresh.sticky.StickyCustomFragment;
import com.basic.code.fragment.components.refresh.sticky.StickyItemDecorationFragment;
import com.basic.code.fragment.components.refresh.sticky.StickyNestedScrollViewFragment;

@Page(name = "Sticky列表粘顶效果")
public class StickyRecyclerViewFragment extends ComponentContainerFragment {

    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                StickyNestedScrollViewFragment.class,
                StickyItemDecorationFragment.class,
                StickyCustomFragment.class
        };
    }
}
