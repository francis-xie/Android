package com.basic.code.fragment.components.edittext;

import com.basic.page.annotation.Page;
import com.basic.face.widget.edittext.verify.VerifyCodeEditText;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author XUE
 * @since 2019/5/7 13:34
 */
@Page(name = "VerifyCodeEditText\n用于手机验证码或者支付密码的输入框")
public class VerifyCodeEditTextFragment extends BaseFragment implements VerifyCodeEditText.OnInputListener {

    @BindView(R.id.vcet_1)
    VerifyCodeEditText vcet1;
    @BindView(R.id.vcet_2)
    VerifyCodeEditText vcet2;
    @BindView(R.id.vcet_3)
    VerifyCodeEditText vcet3;
    @BindView(R.id.vcet_4)
    VerifyCodeEditText vcet4;
    @BindView(R.id.vcet_5)
    VerifyCodeEditText vcet5;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_verify_code_edittext;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {
        vcet1.setOnInputListener(this);
        vcet2.setOnInputListener(this);
        vcet3.setOnInputListener(this);
        vcet4.setOnInputListener(this);
        vcet5.setOnInputListener(this);
    }

    @Override
    public void onComplete(String input) {
        XToastUtils.toast("onComplete:" + input);
    }

    @Override
    public void onChange(String input) {
        XToastUtils.toast("onChange:" + input);
    }

    @Override
    public void onClear() {
        XToastUtils.toast("onClear");
    }

    @OnClick(R.id.btn_clear)
    public void onViewClicked() {
        vcet1.clearInputValue();
        vcet2.clearInputValue();
        vcet3.clearInputValue();
        vcet4.clearInputValue();
        vcet5.clearInputValue();
    }
}
