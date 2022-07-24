package com.basic.code.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basic.face.utils.StatusBarUtils;
import com.basic.face.widget.banner.widget.banner.SimpleImageBanner;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 沉浸式状态栏
 *

 * @since 2019/4/9 上午12:00
 */
public class TranslucentActivity extends AppCompatActivity {

    @BindView(R.id.sib_simple_usage)
    SimpleImageBanner sibSimpleUsage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸式状态栏
        StatusBarUtils.translucent(this);
        setContentView(R.layout.activity_translucent);
        ButterKnife.bind(this);

        sibSimpleUsage.setSource(DemoDataProvider.getBannerList())
                .setOnItemClickListener((view, item, position) -> XToastUtils.toast("headBanner position--->" + position)).startScroll();

    }


    @Override
    protected void onDestroy() {
        sibSimpleUsage.recycle();
        super.onDestroy();
    }
}
