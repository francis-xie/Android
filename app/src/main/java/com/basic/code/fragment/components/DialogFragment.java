
package com.basic.code.fragment.components;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.dialog.BottomSheetFragment;
import com.basic.code.fragment.components.dialog.DialogStrategyFragment;
import com.basic.code.fragment.components.dialog.MaterialDialogFragment;

/**

 * @since 2018/11/14 下午11:00
 */
@Page(name = "对话框", extra = R.drawable.ic_widget_dialog)
public class DialogFragment extends ComponentContainerFragment {
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                DialogStrategyFragment.class,
                MaterialDialogFragment.class,
                BottomSheetFragment.class
        };
    }

}
