package com.basic.code.activity;

import android.app.Activity;

import com.basic.face.widget.activity.BaseGuideActivity;
import com.basic.code.DemoDataProvider;

import java.util.List;

/**
 *  启动引导页
 */
public class UserGuideActivity extends BaseGuideActivity {
    @Override
    protected List<Object> getGuideResourceList() {
        return DemoDataProvider.getUserGuides();
    }

    @Override
    protected Class<? extends Activity> getSkipClass() {
        return LoginActivity.class;
    }

}
