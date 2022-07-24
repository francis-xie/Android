
package com.basic.code.adapter.base.delegate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;

/**
 * 单独布局的DelegateAdapter
 *

 * @since 2020/3/20 1:04 AM
 */
public abstract class SingleDelegateAdapter extends DelegateAdapter.Adapter<RecyclerViewHolder> {

    private int mLayoutId;

    private LayoutHelper mLayoutHelper;

    /**
     * 构造函数
     *
     * @param layoutId 布局ID
     */
    public SingleDelegateAdapter(int layoutId) {
        this(layoutId, new SingleLayoutHelper());
    }

    /**
     * 构造函数
     *
     * @param layoutId     布局ID
     * @param layoutHelper 布局助手
     */
    public SingleDelegateAdapter(int layoutId, LayoutHelper layoutHelper) {
        mLayoutId = layoutId;
        mLayoutHelper = layoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    /**
     * 加载布局获取控件
     *
     * @param parent   父布局
     * @param layoutId 布局ID
     * @return
     */
    protected View inflateView(ViewGroup parent, @LayoutRes int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(inflateView(parent, mLayoutId));
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
