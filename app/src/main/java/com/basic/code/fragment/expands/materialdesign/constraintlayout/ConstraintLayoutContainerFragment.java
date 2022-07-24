
package com.basic.code.fragment.expands.materialdesign.constraintlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.basic.page.annotation.Page;
import com.basic.router.annotation.AutoWired;
import com.basic.router.launcher.Router;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.base.BaseFragment;

import static com.basic.code.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutContainerFragment.KEY_LAYOUT_ID;
import static com.basic.code.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutContainerFragment.KEY_TITLE;

/**
 
 * @since 2020-01-08 10:19
 */
@Page(params = {KEY_TITLE, KEY_LAYOUT_ID})
public class ConstraintLayoutContainerFragment extends BaseFragment {
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_LAYOUT_ID = "key_layout_id";
    @AutoWired(name = KEY_TITLE)
    String title;
    @AutoWired(name = KEY_LAYOUT_ID)
    int layoutId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Router.getInstance().inject(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected TitleBar initTitle() {
        return super.initTitle().setTitle(title);
    }

    @Override
    protected int getLayoutId() {
        return layoutId;
    }

    @Override
    protected void initViews() {

    }
}
