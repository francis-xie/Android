
package com.basic.code.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.basic.face.adapter.FragmentAdapter;
import com.basic.face.widget.tabbar.EasyIndicator;
import com.basic.code.R;
import com.basic.code.base.BaseAppCompatActivity;
import com.basic.code.fragment.components.tabbar.tablayout.ContentPage;
import com.basic.code.fragment.components.tabbar.TestPageFragment;

import java.util.ArrayList;
import java.util.List;

public class EasyIndicatorActivity extends BaseAppCompatActivity {
    EasyIndicator mEasyIndicator;
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_easy_indicator);

        mEasyIndicator = findViewById(R.id.easy_indicator);
        mViewPager = findViewById(R.id.view_pager);

        List<TestPageFragment> list = new ArrayList<>();
        String[] pages = ContentPage.getPageNames();
        for (String page : pages) {
            list.add(TestPageFragment.newInstance(page));
        }
        mEasyIndicator.setTabTitles(ContentPage.getPageNames());
        mEasyIndicator.setViewPager(mViewPager, new FragmentAdapter<>(getSupportFragmentManager(), list));
        mViewPager.setOffscreenPageLimit(ContentPage.size() - 1);

    }
}
