package com.basic.code.fragment.other;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.basic.code.R;
import com.basic.code.core.BaseFragment;
import com.basic.code.databinding.FragmentSettingsBinding;
import com.basic.code.utils.TokenUtils;
import com.basic.code.utils.ToastUtils;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.dialog.DialogLoader;
import com.basic.face.widget.textview.supertextview.SuperTextView;
import com.basic.tools.Util;

@Page(name = "设置")
public class SettingsFragment extends BaseFragment<FragmentSettingsBinding> implements SuperTextView.OnSuperTextViewClickListener {

    @NonNull
    @Override
    protected FragmentSettingsBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentSettingsBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initViews() {
        binding.menuCommon.setOnSuperTextViewClickListener(this);
        binding.menuPrivacy.setOnSuperTextViewClickListener(this);
        binding.menuPush.setOnSuperTextViewClickListener(this);
        binding.menuHelper.setOnSuperTextViewClickListener(this);
        binding.menuChangeAccount.setOnSuperTextViewClickListener(this);
        binding.menuLogout.setOnSuperTextViewClickListener(this);
    }

    @SingleClick
    @Override
    public void onClick(SuperTextView superTextView) {
        int id = superTextView.getId();
        if (id  == R.id.menu_common || id  == R.id.menu_privacy || id == R.id.menu_push || id  == R.id.menu_helper) {
            ToastUtils.toast(superTextView.getLeftString());
        } else if (id == R.id.menu_change_account) {
            ToastUtils.toast(superTextView.getCenterString());
        } else if (id == R.id.menu_logout) {
            DialogLoader.getInstance().showConfirmDialog(
                    getContext(),
                    getString(R.string.lab_logout_confirm),
                    getString(R.string.lab_yes),
                    (dialog, which) -> {
                        dialog.dismiss();
                        Util.getActivityLifecycleHelper().exit();
                        TokenUtils.handleLogoutSuccess();
                    },
                    getString(R.string.lab_no),
                    (dialog, which) -> dialog.dismiss()
            );
        }
    }

}
