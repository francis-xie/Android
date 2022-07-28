package com.basic.code.fragment.components;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.button.ButtonStyleFragment;
import com.basic.code.fragment.components.button.ButtonViewFragment;
import com.basic.code.fragment.components.button.GoodViewFragment;
import com.basic.code.fragment.components.button.RippleViewFragment;
import com.basic.code.fragment.components.button.RoundButtonFragment;
import com.basic.code.fragment.components.button.ShadowButtonFragment;
import com.basic.code.fragment.components.button.ShadowViewFragment;
import com.basic.code.fragment.components.button.ShineButtonFragment;
import com.basic.code.fragment.components.button.SmoothCheckBoxFragment;
import com.basic.code.fragment.components.button.SwitchButtonFragment;
import com.basic.code.fragment.components.button.SwitchIconViewFragment;

/**
 * 按钮
 */
@Page(name = "按钮", extra = R.drawable.ic_widget_button)
public class ButtonFragment extends ComponentContainerFragment {

    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                ButtonStyleFragment.class,
                ShadowButtonFragment.class,
                ShadowViewFragment.class,
                RoundButtonFragment.class,
                ButtonViewFragment.class,
                SwitchButtonFragment.class,
                RippleViewFragment.class,
                SwitchIconViewFragment.class,
                SmoothCheckBoxFragment.class,
                GoodViewFragment.class,
                ShineButtonFragment.class
        };
    }
}
