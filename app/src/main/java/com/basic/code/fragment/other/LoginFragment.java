
package com.basic.code.fragment.other;

import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.page.enums.CoreAnim;
import com.basic.face.utils.CountDownButtonHelper;
import com.basic.face.utils.DrawableUtils;
import com.basic.face.utils.ResUtils;
import com.basic.face.utils.ThemeUtils;
import com.basic.face.utils.ViewUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.button.roundbutton.RoundButton;
import com.basic.face.widget.edittext.materialedittext.MaterialEditText;
import com.basic.face.widget.textview.supertextview.SuperButton;
import com.basic.code.R;
import com.basic.code.activity.MainActivity;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.PrivacyUtils;
import com.basic.code.utils.SettingSPUtils;
import com.basic.code.utils.TokenUtils;
import com.basic.code.utils.ToastUtils;
import com.basic.code.utils.sdkinit.UMengInit;
import com.basic.tools.app.ActivityUtils;
import com.basic.tools.common.RandomUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.basic.code.fragment.other.ServiceProtocolFragment.KEY_PROTOCOL_TITLE;


/**
 * 登录页面
 */
@Page(anim = CoreAnim.none)
public class LoginFragment extends BaseFragment {

    @BindView(R.id.et_phone_number)
    MaterialEditText etPhoneNumber;
    @BindView(R.id.et_verify_code)
    MaterialEditText etVerifyCode;
    @BindView(R.id.btn_get_verify_code)
    RoundButton btnGetVerifyCode;

    @BindView(R.id.cb_protocol)
    CheckBox cbProtocol;
    @BindView(R.id.btn_login)
    SuperButton btnLogin;

    private CountDownButtonHelper mCountDownHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle()
                .setImmersive(true);
        titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setTitle("");
        titleBar.setLeftImageDrawable(DrawableUtils.setTint(ResUtils.getVectorDrawable(getContext(), R.drawable.ic_login_close), ThemeUtils.getMainThemeColor(getContext())));
        return titleBar;
    }

    @Override
    protected void initViews() {
        mCountDownHelper = new CountDownButtonHelper(btnGetVerifyCode, 60);
        //隐私政策弹窗
        SettingSPUtils spUtils = SettingSPUtils.getInstance();
        if (!spUtils.isAgreePrivacy()) {
            PrivacyUtils.showPrivacyDialog(getContext(), (dialog, which) -> {
                dialog.dismiss();
                spUtils.setIsAgreePrivacy(true);
                UMengInit.init();
                // 应用市场不让默认勾选
//                ViewUtils.setChecked(cbProtocol, true);
            });
        }
        cbProtocol.setOnCheckedChangeListener((buttonView, isChecked) -> {
            spUtils.setIsAgreePrivacy(isChecked);
            ViewUtils.setEnabled(btnLogin, isChecked);
        });
        ViewUtils.setEnabled(btnLogin, spUtils.isAgreePrivacy());
        ViewUtils.setChecked(cbProtocol, spUtils.isAgreePrivacy());
    }

    @SingleClick
    @OnClick({R.id.btn_get_verify_code, R.id.btn_login, R.id.tv_other_login, R.id.tv_forget_password, R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_verify_code:
                if (etPhoneNumber.validate()) {
                    getVerifyCode(etPhoneNumber.getEditValue());
                }
                break;
            case R.id.btn_login:
                if (etPhoneNumber.validate()) {
                    if (etVerifyCode.validate()) {
                        loginByVerifyCode(etPhoneNumber.getEditValue(), etVerifyCode.getEditValue());
                    }
                }
                break;
            case R.id.tv_other_login:
                ToastUtils.info("其他登录方式");
                break;
            case R.id.tv_forget_password:
                ToastUtils.info("忘记密码");
                break;
            case R.id.tv_user_protocol:
                openPage(ServiceProtocolFragment.class, KEY_PROTOCOL_TITLE, ResUtils.getString(R.string.title_user_protocol));
                break;
            case R.id.tv_privacy_protocol:
                openPage(ServiceProtocolFragment.class, KEY_PROTOCOL_TITLE, ResUtils.getString(R.string.title_privacy_protocol));
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode(String phoneNumber) {
        // TODO: 2019-11-18 这里只是界面演示而已
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
        // TODO: 2019-11-18 这里只是界面演示而已
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
