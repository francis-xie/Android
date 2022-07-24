
package com.basic.code.fragment.components;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.banner.RecyclerViewBannerFragment;
import com.basic.code.fragment.components.banner.ViewPagerBannerFragment;

/**

 * @since 2019-05-30 00:38
 */
@Page(name = "轮播条", extra = R.drawable.ic_widget_banner)
public class BannerFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                ViewPagerBannerFragment.class,
                RecyclerViewBannerFragment.class
        };
    }
}
