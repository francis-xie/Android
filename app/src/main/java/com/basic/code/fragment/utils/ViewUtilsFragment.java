
package com.basic.code.fragment.utils;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.utils.view.ObjectAnimationFragment;
import com.basic.code.fragment.utils.view.ViewAnimationFragment;
import com.basic.code.fragment.utils.view.ViewCustomAnimationFragment;
import com.basic.code.fragment.utils.view.ViewPaddingFragment;

@Page(name = "ViewUtils", extra = R.drawable.ic_util_view)
public class ViewUtilsFragment extends ComponentContainerFragment {
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                ViewAnimationFragment.class,
                ViewPaddingFragment.class,
                ObjectAnimationFragment.class,
                ViewCustomAnimationFragment.class
        };
    }
}
