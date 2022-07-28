package com.basic.page.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.basic.page.R;

/**
 * 带ListView的基础fragment
 */
public abstract class PageListFragment extends PageFragment implements AdapterView.OnItemClickListener {

    protected ListView mListView;

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.page_fragment_listview, container, false);
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
