package com.basic.code.fragment.news;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.basic.code.core.BaseFragment;
import com.basic.code.databinding.FragmentGridItemBinding;
import com.basic.page.annotation.Page;
import com.basic.router.annotation.AutoWired;
import com.basic.router.launcher.Router;

@Page
public class GridItemFragment extends BaseFragment<FragmentGridItemBinding> {

    public static final String KEY_TITLE_NAME = "title_name";

    /**
     * 自动注入参数，不能是private
     */
    @AutoWired(name = KEY_TITLE_NAME)
    String title;

    @NonNull
    @Override
    protected FragmentGridItemBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentGridItemBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initArgs() {
        // 自动注入参数必须在initArgs里进行注入
        Router.getInstance().inject(this);
    }

    @Override
    protected String getPageTitle() {
        return title;
    }



    @Override
    protected void initViews() {

    }


}
