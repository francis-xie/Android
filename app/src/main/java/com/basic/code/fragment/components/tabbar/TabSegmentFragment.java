
package com.basic.code.fragment.components.tabbar;

import com.basic.page.annotation.Page;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.tabbar.tabsegment.TabSegmentFixModeFragment;
import com.basic.code.fragment.components.tabbar.tabsegment.TabSegmentScrollableModeFragment;

@Page(name = "TabSegment\n扩展性极强的选项卡")
public class TabSegmentFragment extends ComponentContainerFragment {

    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                TabSegmentFixModeFragment.class,
                TabSegmentScrollableModeFragment.class
        };
    }
}
