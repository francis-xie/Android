package com.basic.code.fragment.components;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.popupwindow.CookieBarFragment;
import com.basic.code.fragment.components.popupwindow.EasyPopFragment;
import com.basic.code.fragment.components.popupwindow.PopupWindowStyleFragment;
import com.basic.code.fragment.components.popupwindow.SnackbarFragment;
import com.basic.code.fragment.components.popupwindow.ViewTipFragment;
import com.basic.code.fragment.components.popupwindow.XToastFragment;
import com.basic.code.fragment.components.popupwindow.FACEPopupFragment;

/**
 
 * @date 2017/10/29 下午7:44
 */
@Page(name = "弹出窗", extra = R.drawable.ic_widget_popupwindow)
public class PopupWindowFragment extends ComponentContainerFragment {

    @Override
    public Class[] getPagesClasses() {
        return new Class[]{
                PopupWindowStyleFragment.class,
                ViewTipFragment.class,
                EasyPopFragment.class,
                FACEPopupFragment.class,
                SnackbarFragment.class,
                CookieBarFragment.class,
                XToastFragment.class
        };
    }
}
