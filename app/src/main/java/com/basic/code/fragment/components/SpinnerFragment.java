package com.basic.code.fragment.components;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.spinner.DropDownMenuFragment;
import com.basic.code.fragment.components.spinner.SpinnerStyleFragment;

@Page(name = "下拉框", extra = R.drawable.ic_widget_spinner)
public class SpinnerFragment extends ComponentContainerFragment {

    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                SpinnerStyleFragment.class,
                DropDownMenuFragment.class
        };
    }
}
