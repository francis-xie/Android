package com.basic.code.fragment.trending;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.basic.code.core.BaseFragment;
import com.basic.code.databinding.FragmentTrendingBinding;
import com.basic.page.annotation.Page;
import com.basic.page.enums.CoreAnim;
import com.basic.face.widget.actionbar.TitleBar;

@Page(anim = CoreAnim.none)
public class TrendingFragment extends BaseFragment<FragmentTrendingBinding> {

    @NonNull
    @Override
    protected FragmentTrendingBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentTrendingBinding.inflate(inflater, container, false);
    }

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {

    }
}
