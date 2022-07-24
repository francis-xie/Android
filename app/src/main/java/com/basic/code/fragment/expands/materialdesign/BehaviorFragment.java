package com.basic.code.fragment.expands.materialdesign;

import com.basic.page.annotation.Page;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.expands.materialdesign.behavior.BottomNavigationViewBehaviorFragment;
import com.basic.code.fragment.expands.materialdesign.behavior.ComplexDetailsPageFragment;
import com.basic.code.fragment.expands.materialdesign.behavior.ComplexNestedScrollingFragment;
import com.basic.code.fragment.expands.materialdesign.behavior.NestedScrollingFragment;
import com.basic.code.fragment.expands.materialdesign.behavior.RecyclerViewBehaviorFragment;
import com.basic.code.fragment.expands.materialdesign.behavior.TabLayoutBehaviorFragment;
import com.basic.code.fragment.expands.materialdesign.behavior.ToolbarBehaviorFragment;

/**
 * @author XUE
 * @since 2019/5/9 9:11
 */
@Page(name = "Behavior\n手势行为")
public class BehaviorFragment extends ComponentContainerFragment {
    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                RecyclerViewBehaviorFragment.class,
                TabLayoutBehaviorFragment.class,
                BottomNavigationViewBehaviorFragment.class,
                ToolbarBehaviorFragment.class,
                ComplexDetailsPageFragment.class,
                NestedScrollingFragment.class,
                ComplexNestedScrollingFragment.class
        };
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        if (position >= 3) {
            openNewPage(getSimpleDataItem(position));
        } else {
            openPage(getSimpleDataItem(position));
        }
    }
}
