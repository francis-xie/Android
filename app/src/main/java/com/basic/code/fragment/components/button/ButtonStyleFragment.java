package com.basic.code.fragment.components.button;

import com.basic.page.annotation.Page;
import com.basic.face.utils.CountDownButtonHelper;
import com.basic.face.widget.button.ButtonView;
import com.basic.face.widget.button.CountDownButton;
import com.basic.face.widget.button.roundbutton.RoundButton;
import com.basic.face.widget.textview.supertextview.SuperButton;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**

 * @date 2017/10/17 下午4:26
 */
@Page(name = "统一的按钮样式")
public class ButtonStyleFragment extends BaseFragment {

    @BindView(R.id.bt_countdown1)
    SuperButton mBtCountDown1;
    private CountDownButtonHelper mCountDownHelper1;
    @BindView(R.id.bt_countdown2)
    ButtonView mBtCountDown2;
    private CountDownButtonHelper mCountDownHelper2;
    @BindView(R.id.bt_countdown3)
    RoundButton mBtCountDown3;
    private CountDownButtonHelper mCountDownHelper3;
    @BindView(R.id.bt_countdown4)
    CountDownButton mBtCountDown4;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_button_style;
    }

    @Override
    protected void initViews() {
        mCountDownHelper1 = new CountDownButtonHelper(mBtCountDown1, 10);
        mCountDownHelper2 = new CountDownButtonHelper(mBtCountDown2, 20);
        mCountDownHelper3 = new CountDownButtonHelper(mBtCountDown3, 30);
        mCountDownHelper3.setOnCountDownListener(new CountDownButtonHelper.OnCountDownListener() {
            @Override
            public void onCountDown(int time) {
                mBtCountDown3.setText("还剩：" + time + "秒");
            }

            @Override
            public void onFinished() {
                mBtCountDown3.setText("点击重试");
                XToastUtils.toast("时间到！");
            }
        });
    }

    @Override
    protected void initListeners() {

    }

    @OnClick(R.id.bt_countdown1)
    public void startCountDown1() {
        mCountDownHelper1.start();
    }

    @OnClick(R.id.bt_countdown2)
    public void startCountDown2() {
        mCountDownHelper2.start();
    }

    @OnClick(R.id.bt_countdown3)
    public void startCountDown3() {
        mCountDownHelper3.start();
    }

    @Override
    public void onDestroyView() {
        mCountDownHelper1.cancel();
        mCountDownHelper2.cancel();
        mCountDownHelper3.cancel();
        mBtCountDown4.cancelCountDown();
        super.onDestroyView();
    }
}
