
package com.basic.code.fragment.components.refresh.refreshs;

import com.basic.page.annotation.Page;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.refresh.refreshs.style.RefreshAllStyleFragment;
import com.basic.code.fragment.components.refresh.refreshs.style.RefreshClassicsStyleFragment;
import com.basic.code.fragment.components.refresh.refreshs.style.RefreshCustomStyleFragment;
import com.basic.code.fragment.components.refresh.refreshs.style.RefreshMaterialStyleFragment;

@Page(name = "下拉刷新样式\n包含10多种下拉样式")
public class RefreshStyleFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                RefreshClassicsStyleFragment.class,
                RefreshMaterialStyleFragment.class,
                RefreshAllStyleFragment.class,
                RefreshCustomStyleFragment.class
        };
    }
}
