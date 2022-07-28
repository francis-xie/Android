
package com.basic.code.fragment.expands;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.expands.alibaba.TangramAndroidFragment;
import com.basic.code.fragment.expands.alibaba.UltraViewPagerFragment;
import com.basic.code.fragment.expands.alibaba.VLayoutFragment;

@Page(name = "alibaba UIKit", extra = R.drawable.ic_expand_alibaba)
public class AlibabaUIFragment extends ComponentContainerFragment {
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                VLayoutFragment.class,
                TangramAndroidFragment.class,
                UltraViewPagerFragment.class
        };
    }
}
