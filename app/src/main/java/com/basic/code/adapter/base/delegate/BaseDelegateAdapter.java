
package com.basic.code.adapter.base.delegate;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.basic.face.adapter.recyclerview.RecyclerViewHolder;

import java.util.Collection;

/**
 * 通用的DelegateAdapter适配器
 *

 * @since 2020/3/20 12:44 AM
 */
public abstract class BaseDelegateAdapter<T> extends XDelegateAdapter<T, RecyclerViewHolder> {

    public BaseDelegateAdapter() {
        super();
    }

    public BaseDelegateAdapter(Collection<T> list) {
        super(list);
    }

    public BaseDelegateAdapter(T[] data) {
        super(data);
    }

    /**
     * 适配的布局
     *
     * @param viewType
     * @return
     */
    protected abstract int getItemLayoutId(int viewType);

    @NonNull
    @Override
    protected RecyclerViewHolder getViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(inflateView(parent, getItemLayoutId(viewType)));
    }
}
