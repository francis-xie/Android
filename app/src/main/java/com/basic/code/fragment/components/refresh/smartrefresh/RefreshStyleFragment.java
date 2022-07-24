
package com.basic.code.fragment.components.refresh.smartrefresh;

import com.basic.page.annotation.Page;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.refresh.smartrefresh.style.RefreshAllStyleFragment;
import com.basic.code.fragment.components.refresh.smartrefresh.style.RefreshClassicsStyleFragment;
import com.basic.code.fragment.components.refresh.smartrefresh.style.RefreshCustomStyleFragment;
import com.basic.code.fragment.components.refresh.smartrefresh.style.RefreshMaterialStyleFragment;

/**

 * @since 2018/12/7 上午12:44
 */
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
