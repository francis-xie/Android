package com.basic.code.fragment.components.textview;

import com.basic.page.annotation.Page;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.textview.supertextview.SuperButtonFragment;
import com.basic.code.fragment.components.textview.supertextview.SuperClickFragment;
import com.basic.code.fragment.components.textview.supertextview.SuperNetPictureLoadingFragment;
import com.basic.code.fragment.components.textview.supertextview.SuperTextCommonUseFragment;

/**
 * SuperTextView演示
 *

 * @since 2018/11/29 上午12:29
 */
@Page(name = "可拓展的TextView")
public class SuperTextViewFragment extends ComponentContainerFragment {
    @Override
    public Class[] getPagesClasses() {
        return new Class[]{
                SuperClickFragment.class,
                SuperButtonFragment.class,
                SuperTextCommonUseFragment.class,
                SuperNetPictureLoadingFragment.class
        };
    }

}
