
package com.basic.code.fragment.components.imageview;

import com.basic.page.annotation.Page;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.imageview.preview.NineGridImageViewFragment;
import com.basic.code.fragment.components.imageview.preview.PreviewRecycleViewFragment;

@Page(name = "图片预览")
public class PreviewFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                PreviewRecycleViewFragment.class,
                NineGridImageViewFragment.class
        };
    }
}
