
package com.basic.code.fragment.components.statelayout;

import com.basic.page.annotation.Page;
import com.basic.face.widget.statelayout.StatusLoader;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.statelayout.status.adapter.DefaultStatusAdapter;
import com.basic.code.fragment.components.statelayout.status.StatusLoaderMultipleFragment;
import com.basic.code.fragment.components.statelayout.status.StatusLoaderSingleFragment;

@Page(name = "StatusLoader\n使用状态适配器动态设置")
public class StatusLoaderFragment extends ComponentContainerFragment {

    @Override
    protected void initArgs() {
        StatusLoader.initDefault(new DefaultStatusAdapter());
    }

    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                StatusLoaderMultipleFragment.class,
                StatusLoaderSingleFragment.class
        };
    }
}
