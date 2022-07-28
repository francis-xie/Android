package com.basic.code.fragment.profile;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.basic.code.R;
import com.basic.code.core.BaseFragment;
import com.basic.code.databinding.FragmentProfileBinding;
import com.basic.code.fragment.other.AboutFragment;
import com.basic.code.fragment.other.SettingsFragment;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.page.enums.CoreAnim;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.textview.supertextview.SuperTextView;

@Page(anim = CoreAnim.none)
public class ProfileFragment extends BaseFragment<FragmentProfileBinding> implements SuperTextView.OnSuperTextViewClickListener {

    @NonNull
    @Override
    protected FragmentProfileBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentProfileBinding.inflate(inflater, container, false);
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

    @Override
    protected void initListeners() {
        binding.menuSettings.setOnSuperTextViewClickListener(this);
        binding.menuAbout.setOnSuperTextViewClickListener(this);

    }

    @SingleClick
    @Override
    public void onClick(SuperTextView view) {
        int id = view.getId();
        if (id == R.id.menu_settings) {
            openNewPage(SettingsFragment.class);
        } else if (id == R.id.menu_about) {
            openNewPage(AboutFragment.class);
        }
    }
}
