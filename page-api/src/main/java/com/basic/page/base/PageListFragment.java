package com.basic.page.base;

import android.widget.AdapterView;
import android.widget.ListView;

import com.basic.page.R;

/**
 * 带ListView的基础fragment
 *

 * @since 2018/5/24 下午3:36
 */
public abstract class PageListFragment extends PageFragment implements AdapterView.OnItemClickListener {

    protected ListView mListView;

    @Override
    protected int getLayoutId() {
        return R.layout.page_fragment_listview;
    }

    @Override
    protected void initViews() {
        mListView = findViewById(R.id.lv_simple);
        mListView.setOnItemClickListener(this);
        initData();
    }

    /**
     * 初始化集合数据
     */
    protected abstract void initData();

    @Override
    protected void initListeners() {

    }

    protected ListView getListView() {
        return mListView;
    }

    @Override
    public void onDestroyView() {
        if (mListView != null) {
            mListView.setOnItemClickListener(null);
        }
        super.onDestroyView();
    }
}
