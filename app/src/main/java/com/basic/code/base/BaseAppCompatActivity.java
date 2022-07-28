
package com.basic.code.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basic.code.utils.Utils;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

/**
 * 基础AppCompatActivity
 */
public class BaseAppCompatActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        //注入字体
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.initTheme(this);
        super.onCreate(savedInstanceState);
    }

}
