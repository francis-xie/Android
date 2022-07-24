package com.basic.refresh.layout.adapter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 基础 RecyclerAdapter
 *

 * @since 2018/12/6 下午3:04
 */
public abstract class BaseRecyclerAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> implements ListAdapter {

    /**
     * 数据
     */
    private final List<T> mList;
    /**
     * 当前点击的条目
     */
    private int mLastPosition = -1;
    private boolean mOpenAnimationEnable = true;

    public BaseRecyclerAdapter() {
        setHasStableIds(false);
        mList = new ArrayList<>();
    }

    public BaseRecyclerAdapter(Collection<T> collection) {
        setHasStableIds(false);
        mList = new ArrayList<>(collection);
    }

    private void addAnimate(V holder, int position) {
        if (mOpenAnimationEnable && mLastPosition < position) {
            holder.itemView.setAlpha(0);
            holder.itemView.animate().alpha(1).start();
            mLastPosition = position;
        }
    }

    /**
     * 加载布局获取控件
     *
     * @param parent
     * @param layoutId
     * @return
     */
    protected View getInflate(ViewGroup parent, @LayoutRes int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull V holder, int position) {
        if (getItem(position) != null) {
            onBindViewHolder(holder, getItem(position), position);
        }
    }

    /**
     * 绑定布局控件
     *
     * @param holder
     * @param model
     * @param position
     */
    protected abstract void onBindViewHolder(V holder, T model, int position);

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull V holder) {
        super.onViewAttachedToWindow(holder);
        addAnimate(holder, holder.getLayoutPosition());
    }

    public void setOpenAnimationEnable(boolean enabled) {
        mOpenAnimationEnable = enabled;
    }

    public BaseRecyclerAdapter refresh(Collection<T> collection) {
        if (collection != null) {
            mList.clear();
            mList.addAll(collection);
            notifyChanged();
            mLastPosition = -1;
        }
        return this;
    }

    public BaseRecyclerAdapter loadMore(Collection<T> collection) {
        if (collection != null) {
            mList.addAll(collection);
            notifyChanged();
        }
        return this;
    }

    public BaseRecyclerAdapter load(T item) {
        if (item != null) {
            mList.add(item);
            notifyChanged();
        }
        return this;
    }

    /**
     * 通知变化
     */
    private void notifyChanged() {
        notifyDataSetChanged();
        notifyListDataSetChanged();
    }

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */
    public void notifyListDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    /**
     * Notifies the attached observers that the underlying data is no longer valid
     * or available. Once invoked adapter is no longer valid and should
     * not report further data set changes.
     */
    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V holder;
        if (convertView != null) {
            holder = (V) convertView.getTag();
        } else {
            holder = onCreateViewHolder(parent, getItemViewType(position));
            convertView = holder.itemView;
            convertView.setTag(holder);
        }
        onBindViewHolder(holder, position);
        addAnimate(holder, position);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public T getItem(int position) {
        return position < getCount() ? mList.get(position) : null;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    public void replace(int position, T item) {
        if (position > -1 && position < mList.size() && item != null) {
            mList.set(position, item);
            notifyDataSetChanged();
        }
    }

    public void replaceNotNotify(int position, T item) {
        if (position > -1 && position < mList.size() && item != null) {
            mList.set(position, item);
        }
    }

    /**
     * 获取集合数据
     *
     * @return
     */
    public List<T> getListData() {
        return mList;
    }

    public int getLastPosition() {
        return mLastPosition;
    }

    /**
     * 清除数据
     */
    public void clear() {
        if (!isEmpty()) {
            mList.clear();
        }
    }
}
