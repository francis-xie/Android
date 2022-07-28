package com.basic.code.fragment.components.refresh;

import android.view.View;

import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.refresh.broccoli.AnimationPlaceholderFragment;
import com.basic.code.fragment.components.refresh.broccoli.CommonPlaceholderFragment;
import com.basic.code.utils.Utils;

@Page(name = "Broccoli\n预加载占位控件")
public class BroccoliFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                CommonPlaceholderFragment.class,
                AnimationPlaceholderFragment.class
        };
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/samlss/Broccoli");
            }
        });
        return titleBar;
    }
}
