package com.basic.code.fragment.components.edittext;

import com.basic.page.annotation.Page;
import com.basic.face.widget.button.roundbutton.RoundButton;
import com.basic.face.widget.edittext.materialedittext.MaterialEditText;
import com.basic.face.widget.edittext.materialedittext.validation.RegexpValidator;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "统一的输入框样式")
public class EditTextStyleFragment extends BaseFragment {

    @BindView(R.id.et_basic)
    MaterialEditText mEtBasic;
    @BindView(R.id.bt_disenable)
    RoundButton mBtDisenable;
    @BindView(R.id.et_check)
    MaterialEditText mEtCheck;
    @BindView(R.id.et_auto_check)
    MaterialEditText mEtAutoCheck;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edittext_style;
    }

    @Override
    protected void initViews() {
        initValidationEt();
    }

    @Override
    protected void initListeners() {

    }

    private void initValidationEt() {
        mEtCheck.addValidator(new RegexpValidator("只能输入数字!", "\\d+"));
        mEtAutoCheck.addValidator(new RegexpValidator("只能输入数字!", "\\d+"));
        mEtAutoCheck.addValidator(new RegexpValidator(getString(R.string.tip_number_only_error_message), getString(R.string.regexp_number_only)));
    }

    @OnClick(R.id.bt_disenable)
    public void disEnable() {
        mEtBasic.setEnabled(!mEtBasic.isEnabled());
        mBtDisenable.setText(mEtBasic.isEnabled() ? "不允许输入" : "允许输入");
    }


    @OnClick(R.id.bt_check_vaild)
    public void checkValid() {
        mEtCheck.validate();
    }
}
