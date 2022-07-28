package com.basic.code.fragment.components.guideview;

import android.content.Intent;

import com.basic.page.annotation.Page;
import com.basic.code.activity.SplashActivity;
import com.basic.code.base.BaseSimpleListFragment;

import java.util.List;

import static com.basic.code.activity.SplashActivity.KEY_ENABLE_ALPHA_ANIM;
import static com.basic.code.activity.SplashActivity.KEY_IS_DISPLAY;

/**
 * 启动页演示
 */
@Page(name = "启动页")
public class SplashFragment extends BaseSimpleListFragment {
    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("渐近式启动页");
        lists.add("非渐近式启动页");
        return lists;
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        Intent i = new Intent();
        i.putExtra(KEY_IS_DISPLAY, true);
        i.setClass(getContext(), SplashActivity.class);
        switch (position) {
            case 0:
                i.putExtra(KEY_ENABLE_ALPHA_ANIM, true);
                break;
            case 1:
                i.putExtra(KEY_ENABLE_ALPHA_ANIM, false);
                break;
            default:
                break;
        }
        startActivity(i);
    }
}
