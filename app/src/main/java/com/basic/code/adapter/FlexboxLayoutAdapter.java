
package com.basic.code.adapter;

import android.util.SparseBooleanArray;

import androidx.annotation.NonNull;

import com.basic.face.adapter.recyclerview.BaseRecyclerAdapter;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.code.R;

import java.util.ArrayList;
import java.util.List;

/**

 * @since 2019-11-23 01:32
 */
public class FlexboxLayoutAdapter extends BaseRecyclerAdapter<String> {

    private boolean mIsMultiSelectMode;
    private boolean mCancelable;

    /**
     * 多选的状态记录
     */
    private SparseBooleanArray mMultiSelectStatus = new SparseBooleanArray();

    public FlexboxLayoutAdapter(String[] data) {
        super(data);
    }

    public FlexboxLayoutAdapter setIsMultiSelectMode(boolean isMultiSelectMode) {
        mIsMultiSelectMode = isMultiSelectMode;
        return this;
    }

    public FlexboxLayoutAdapter setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return this;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.adapter_flexbox_layout_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, String item) {
        holder.text(R.id.tv_tag, item);
        if (mIsMultiSelectMode) {
            holder.select(R.id.tv_tag, mMultiSelectStatus.get(position));
        } else {
            holder.select(R.id.tv_tag, getSelectPosition() == position);
        }
    }

    /**
     * 选择
     *
     * @param position 选中索引
     * @return
     */
    public boolean select(int position) {
        return mIsMultiSelectMode ? multiSelect(position) : singleSelect(position);
    }

    /**
     * 多选
     *
     * @param positions
     */
    public void multiSelect(int... positions) {
        if (!mIsMultiSelectMode) {
            return;
        }
        for (int position : positions) {
            multiSelect(position);
        }
    }

    /**
     * 多选
     *
     * @param position
     */
    private boolean multiSelect(int position) {
        if (!mIsMultiSelectMode) {
            return false;
        }
        mMultiSelectStatus.append(position, !mMultiSelectStatus.get(position));
        notifyItemChanged(position);
        return true;
    }

    /**
     * 单选
     *
     * @param position
     */
    private boolean singleSelect(int position) {
        return singleSelect(position, mCancelable);
    }

    /**
     * 单选
     *
     * @param position
     * @param cancelable
     */
    private boolean singleSelect(int position, boolean cancelable) {
        if (position == getSelectPosition()) {
            if (cancelable) {
                setSelectPosition(-1);
                return true;
            }
        } else {
            setSelectPosition(position);
            return true;
        }
        return false;
    }


    /**
     * @return 获取选中的内容
     */
    public String getSelectContent() {
        String value = getSelectItem();
        if (value == null) {
            return "";
        }
        return value;
    }


    /**
     * @return 获取多选的内容
     */
    public List<String> getMultiContent() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            if (mMultiSelectStatus.get(i)) {
                list.add(getItem(i));
            }
        }
        return list;
    }


    /**
     * 清除选中
     */
    public void clearSelection() {
        if (mIsMultiSelectMode) {
            clearMultiSelection();
        } else {
            clearSingleSelection();
        }
    }

    private void clearMultiSelection() {
        mMultiSelectStatus.clear();
        notifyDataSetChanged();
    }

    private void clearSingleSelection() {
        setSelectPosition(-1);
    }
}
