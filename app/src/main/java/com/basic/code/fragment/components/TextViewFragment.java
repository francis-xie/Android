package com.basic.code.fragment.components;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.textview.AutoFitTextViewFragment;
import com.basic.code.fragment.components.textview.AutoHyphenationTextViewFragment;
import com.basic.code.fragment.components.textview.BadgeViewFragment;
import com.basic.code.fragment.components.textview.ExpandableTextViewFragment;
import com.basic.code.fragment.components.textview.LabelViewFragment;
import com.basic.code.fragment.components.textview.LoggerTextViewFragment;
import com.basic.code.fragment.components.textview.SuperTextViewFragment;

/**
 * TextView
 */
@Page(name = "文字", extra = R.drawable.ic_widget_textview)
public class TextViewFragment extends ComponentContainerFragment {

    @Override
    public Class[] getPagesClasses() {
        return new Class[]{
                SuperTextViewFragment.class,
                ExpandableTextViewFragment.class,
                LabelViewFragment.class,
                BadgeViewFragment.class,
                AutoFitTextViewFragment.class,
                AutoHyphenationTextViewFragment.class,
                LoggerTextViewFragment.class
        };
    }

}
