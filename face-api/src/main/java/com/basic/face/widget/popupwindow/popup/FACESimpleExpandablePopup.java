
package com.basic.face.widget.popupwindow.popup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ExpandableListView;

import com.basic.face.adapter.simple.ExpandableItem;
import com.basic.face.adapter.simple.FACESimpleExpandableListAdapter;

import java.util.List;

/**
 * 简单的可伸缩弹窗
 *

 * @since 2019-11-11 16:31
 */
public class FACESimpleExpandablePopup<T extends FACESimpleExpandablePopup> extends FACEExpandableListPopup {

    public FACESimpleExpandablePopup(Context context, List<ExpandableItem> data) {
        this(context, new FACESimpleExpandableListAdapter(data));
    }

    public FACESimpleExpandablePopup(Context context, ExpandableItem... data) {
        this(context, new FACESimpleExpandableListAdapter(data));
    }

    public FACESimpleExpandablePopup(Context context, FACESimpleExpandableListAdapter adapter) {
        super(context, adapter);
    }

    /**
     * 创建弹窗
     *
     * @param onExpandableItemClickListener
     * @return
     */
    public T create(int width, final OnExpandableItemClickListener onExpandableItemClickListener) {
        create(width);
        setOnExpandableItemClickListener(onExpandableItemClickListener);
        return (T) this;
    }

    /**
     * 创建弹窗
     *
     * @param width     弹窗的宽度
     * @param maxHeight 弹窗最大的高度
     * @return
     */
    @Override
    public T create(int width, int maxHeight) {
        super.create(width, maxHeight);
        return (T) this;
    }

    /**
     * 创建弹窗
     *
     * @param width
     * @param maxHeight
     * @param onExpandableItemClickListener
     * @return
     */
    public T create(int width, int maxHeight, final OnExpandableItemClickListener onExpandableItemClickListener) {
        create(width, maxHeight);
        setOnExpandableItemClickListener(onExpandableItemClickListener);
        return (T) this;
    }

    /**
     * 设置条目点击监听
     *
     * @param onExpandableItemClickListener
     * @return
     */
    public FACEExpandableListPopup setOnExpandableItemClickListener(final OnExpandableItemClickListener onExpandableItemClickListener) {
        setOnExpandableItemClickListener(true, onExpandableItemClickListener);
        return this;
    }

    /**
     * 设置条目点击监听
     *
     * @param autoDismiss
     * @param onExpandableItemClickListener
     * @return
     */
    public T setOnExpandableItemClickListener(final boolean autoDismiss, final OnExpandableItemClickListener onExpandableItemClickListener) {
        if (mExpandableListView != null) {
            mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    if (onExpandableItemClickListener != null) {
                        onExpandableItemClickListener.onExpandableItemClick(getAdapter(), getAdapter().getGroup(groupPosition), groupPosition, childPosition);
                    }
                    if (autoDismiss) {
                        dismiss();
                    }
                    return false;
                }
            });
        }
        return (T) this;
    }

    @Override
    public FACESimpleExpandableListAdapter getAdapter() {
        return (FACESimpleExpandableListAdapter) mAdapter;
    }

    @Override
    public T create(int width) {
        super.create(width);
        return (T) this;
    }

    @Override
    public T setDividerHeight(int dividerHeight) {
        super.setDividerHeight(dividerHeight);
        return (T) this;
    }

    @Override
    public T setHasDivider(boolean hasDivider) {
        super.setHasDivider(hasDivider);
        return (T) this;
    }

    @Override
    public T setGroupDivider(Drawable divider) {
        super.setGroupDivider(divider);
        return (T) this;
    }

    @Override
    public T setChildDivider(Drawable divider) {
        super.setChildDivider(divider);
        return (T) this;
    }

    @Override
    public T setEnableOnlyExpandOne(boolean enable) {
        super.setEnableOnlyExpandOne(enable);
        return (T) this;
    }

    /**
     * 可伸缩列表条目点击监听
     */
    public interface OnExpandableItemClickListener {
        /**
         * 条目点击
         *
         * @param adapter
         * @param group
         * @param groupPosition 父节点索引
         * @param childPosition 子节点索引
         */
        void onExpandableItemClick(FACESimpleExpandableListAdapter adapter, ExpandableItem group, int groupPosition, int childPosition);
    }


}
