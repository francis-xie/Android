
package com.basic.code.fragment.utils;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.utils.KeyboardUtils;
import com.basic.face.utils.StatusBarUtils;
import com.basic.face.widget.button.switchbutton.SwitchButton;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**

 * @since 2020/12/15 12:29 AM
 */
@Page(name = "KeyBoardUtils", extra = R.drawable.ic_util_keyboard)
public class KeyBoardUtilsFragment extends BaseFragment implements KeyboardUtils.SoftKeyboardToggleListener {

    @BindView(R.id.switch_status)
    SwitchButton switchStatus;
    @BindView(R.id.btn_show_soft)
    Button btnShowSoft;
    @BindView(R.id.btn_hide_soft)
    Button btnHideSoft;
    @BindView(R.id.et_1)
    EditText et1;
    @BindView(R.id.et_2)
    EditText et2;

    private boolean isFullScreen;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_util_keyboard;
    }

    @Override
    protected void initViews() {
        switchStatus.setClickable(false);

    }

    @Override
    protected void initListeners() {
        KeyboardUtils.addKeyboardToggleListener(getActivity(), this);
    }

    @Override
    public void onToggleSoftKeyboard(boolean isVisible) {
        if (switchStatus != null) {
            switchStatus.setChecked(isVisible);
        }
    }

    @SingleClick
    @OnClick({R.id.btn_get_status, R.id.btn_switch_screen, R.id.btn_show_soft, R.id.btn_hide_soft})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_status:
                XToastUtils.toast("键盘显示状态:" + KeyboardUtils.isSoftInputShow(getActivity()));
                break;
            case R.id.btn_switch_screen:
                if (isFullScreen) {
                    StatusBarUtils.cancelFullScreen(getActivity());
                } else {
                    StatusBarUtils.fullScreen(getActivity());
                }
                isFullScreen = !isFullScreen;
                break;
            case R.id.btn_show_soft:
                KeyboardUtils.showSoftInputForce(getActivity());
                break;
            case R.id.btn_hide_soft:
                KeyboardUtils.hideSoftInput(view);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onTouchDownAction(MotionEvent ev) {
        // 不处理隐藏键盘的操作
    }

    private EditText getFocusEditText() {
        if (getActivity() != null) {
            View view = getActivity().getCurrentFocus();
            if (view instanceof EditText) {
                return (EditText) view;
            }
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        KeyboardUtils.removeKeyboardToggleListener(this);
        super.onDestroyView();
    }

}
