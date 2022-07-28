package com.basic.code.fragment.other;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.basic.code.R;
import com.basic.code.activity.MainActivity;
import com.basic.code.core.BaseFragment;
import com.basic.code.databinding.FragmentLoginBinding;
import com.basic.code.utils.RandomUtils;
import com.basic.code.utils.SettingUtils;
import com.basic.code.utils.TokenUtils;
import com.basic.code.utils.Utils;
import com.basic.code.utils.ToastUtils;
import com.basic.code.utils.sdkinit.UMengInit;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.page.enums.CoreAnim;
import com.basic.face.utils.CountDownButtonHelper;
import com.basic.face.utils.ResUtils;
import com.basic.face.utils.ThemeUtils;
import com.basic.face.utils.ViewUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.tools.app.ActivityUtils;


/**
 * 登录页面
 */
@Page(anim = CoreAnim.none)
public class LoginFragment extends BaseFragment<FragmentLoginBinding> implements View.OnClickListener {

    private View mJumpView;

    private CountDownButtonHelper mCountDownHelper;

    @NonNull
    @Override
    protected FragmentLoginBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentLoginBinding.inflate(inflater, container, false);
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle()
                .setImmersive(true);
        titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setTitle("");
        titleBar.setLeftImageDrawable(ResUtils.getVectorDrawable(getContext(), R.drawable.ic_login_close));
        titleBar.setActionTextColor(ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
        mJumpView = titleBar.addAction(new TitleBar.TextAction(R.string.title_jump_login) {
            @Override
            public void performAction(View view) {
                onLoginSuccess();
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {
        mCountDownHelper = new CountDownButtonHelper(binding.btnGetVerifyCode, 60);
        //隐私政策弹窗
        if (!SettingUtils.isAgreePrivacy()) {
            Utils.showPrivacyDialog(getContext(), (dialog, which) -> {
                dialog.dismiss();
                handleSubmitPrivacy();
            });
        }
        boolean isAgreePrivacy = SettingUtils.isAgreePrivacy();
        binding.cbProtocol.setChecked(isAgreePrivacy);
        refreshButton(isAgreePrivacy);
        binding.cbProtocol.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingUtils.setIsAgreePrivacy(isChecked);
            refreshButton(isChecked);
        });
    }

    @Override
    protected void initListeners() {
        binding.btnGetVerifyCode.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.tvOtherLogin.setOnClickListener(this);
        binding.tvForgetPassword.setOnClickListener(this);
        binding.tvUserProtocol.setOnClickListener(this);
        binding.tvPrivacyProtocol.setOnClickListener(this);
    }

    private void refreshButton(boolean isChecked) {
        ViewUtils.setEnabled(binding.btnLogin, isChecked);
        ViewUtils.setEnabled(mJumpView, isChecked);
    }

    private void handleSubmitPrivacy() {
        SettingUtils.setIsAgreePrivacy(true);
        UMengInit.init();
        // 应用市场不让默认勾选
//        ViewUtils.setChecked(cbProtocol, true);
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_get_verify_code) {
            if (binding.etPhoneNumber.validate()) {
                getVerifyCode(binding.etPhoneNumber.getEditValue());
            }
        } else if (id == R.id.btn_login) {
            if (binding.etPhoneNumber.validate()) {
                if (binding.etVerifyCode.validate()) {
                    loginByVerifyCode(binding.etPhoneNumber.getEditValue(), binding.etVerifyCode.getEditValue());
                }
            }
        } else if (id == R.id.tv_other_login) {
            ToastUtils.info("其他登录方式");
        } else if (id == R.id.tv_forget_password) {
            ToastUtils.info("忘记密码");
        } else if (id == R.id.tv_user_protocol) {
            Utils.gotoProtocol(this, false, true);
        } else if (id == R.id.tv_privacy_protocol) {
            Utils.gotoProtocol(this, true, true);
        }

    }

    /**
     * 获取验证码
     */
    private void getVerifyCode(String phoneNumber) {
        // TODO: 2020/8/29 这里只是界面演示而已
        ToastUtils.warning("只是演示，验证码请随便输");
        mCountDownHelper.start();
    }

    /**
     * 根据验证码登录
     *
     * @param phoneNumber 手机号
     * @param verifyCode  验证码
     */
    private void loginByVerifyCode(String phoneNumber, String verifyCode) {
        // TODO: 2020/8/29 这里只是界面演示而已
        onLoginSuccess();
    }

    /**
     * 登录成功的处理
     */
    private void onLoginSuccess() {
        String token = RandomUtils.getRandomNumbersAndLetters(16);
        if (TokenUtils.handleLoginSuccess(token)) {
            popToBack();
            ActivityUtils.startActivity(MainActivity.class);
        }
    }

    @Override
    public void onDestroyView() {
        if (mCountDownHelper != null) {
            mCountDownHelper.recycle();
        }
        super.onDestroyView();
    }



}

