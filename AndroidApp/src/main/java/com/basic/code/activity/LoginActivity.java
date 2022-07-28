package com.basic.code.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.basic.code.core.BaseActivity;
import com.basic.code.fragment.other.LoginFragment;
import com.basic.face.utils.KeyboardUtils;
import com.basic.face.utils.StatusBarUtils;
import com.basic.tools.display.Colors;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openPage(LoginFragment.class, getIntent().getExtras());
    }

    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }

    @Override
    protected void initStatusBarStyle() {
        StatusBarUtils.initStatusBarStyle(this, false, Colors.WHITE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
    }
}
