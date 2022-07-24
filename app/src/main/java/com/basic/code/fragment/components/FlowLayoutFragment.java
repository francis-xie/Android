package com.basic.code.fragment.components;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.flowlayout.FlexboxLayoutFragment;
import com.basic.code.fragment.components.flowlayout.FlowTagLayoutFragment;
import com.basic.code.fragment.components.flowlayout.NormalFlowLayoutFragment;

/**

 * @date 2017/11/20 下午4:08
 */
@Page(name = "流布局", extra = R.drawable.ic_widget_flowlayout)
public class FlowLayoutFragment extends ComponentContainerFragment {

    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                FlowTagLayoutFragment.class,
                NormalFlowLayoutFragment.class,
                FlexboxLayoutFragment.class
        };
    }
}
