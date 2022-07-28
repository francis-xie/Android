package com.basic.code.fragment.components;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.statelayout.MultipleStatusViewFragment;
import com.basic.code.fragment.components.statelayout.StatefulLayoutFragment;
import com.basic.code.fragment.components.statelayout.StatusLoaderFragment;
import com.basic.code.fragment.components.statelayout.StatusViewFragment;

/**
 * 状态切换
 */
@Page(name = "状态切换", extra = R.drawable.ic_widget_statelayout)
public class StateLayoutFragment extends ComponentContainerFragment {

    @Override
    public Class[] getPagesClasses() {
        return new Class[] {
                StatefulLayoutFragment.class,
                MultipleStatusViewFragment.class,
                StatusViewFragment.class,
                StatusLoaderFragment.class
        };
    }
}
