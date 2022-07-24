package com.basic.code.fragment.components;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.edittext.CustomEditTextFragment;
import com.basic.code.fragment.components.edittext.EditTextStyleFragment;
import com.basic.code.fragment.components.edittext.MaterialEditTextFragment;
import com.basic.code.fragment.components.edittext.VerifyCodeEditTextFragment;

/**
 * 输入框组件
 *

 * @since 2018/11/26 下午5:48
 */
@Page(name = "输入框", extra = R.drawable.ic_widget_edittext)
public class EditTextFragment extends ComponentContainerFragment {

    @Override
    public Class[] getPagesClasses() {
        return new Class[]{
                EditTextStyleFragment.class,
                CustomEditTextFragment.class,
                MaterialEditTextFragment.class,
                VerifyCodeEditTextFragment.class
        };
    }
}
