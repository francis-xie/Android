
package com.basic.code.fragment.components.refresh.sample.sortedlist;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedListAdapterCallback;

import com.basic.code.adapter.entity.NewInfo;
import com.basic.tools.common.StringUtils;

import static com.basic.code.fragment.components.refresh.sample.diffutil.DiffUtilCallback.PAYLOAD_COMMENT;
import static com.basic.code.fragment.components.refresh.sample.diffutil.DiffUtilCallback.PAYLOAD_PRAISE;
import static com.basic.code.fragment.components.refresh.sample.diffutil.DiffUtilCallback.PAYLOAD_READ_NUMBER;
import static com.basic.code.fragment.components.refresh.sample.diffutil.DiffUtilCallback.PAYLOAD_USER_NAME;

/**
 * 配合SortedList使用
 */
public class SortedListCallback extends SortedListAdapterCallback<NewInfo> {

    public SortedListCallback(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    /**
     * 排序方法
     *
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(NewInfo o1, NewInfo o2) {
        return Long.compare(o1.getID(), o2.getID());
    }

    /**
     * 用来判断 两个对象是否是相同的Item。
     *
     * @param item1
     * @param item2
     * @return
     */
    @Override
    public boolean areItemsTheSame(NewInfo item1, NewInfo item2) {
        return item1.getID() == item2.getID();
    }

    /**
     * 1.这个方法仅仅在areItemsTheSame()返回true时，才调用。
     * <p>
     * 2.DiffUtil用这个方法来检查两个item是否含有相同的数据
     * <p>
     * 3.DiffUtil 用这个方法替代equals方法去检查是否相等。所以你可以根据你的UI去改变它的返回值
     *
     * @param oldItem
     * @param newItem
     * @return
     */
    @Override
    public boolean areContentsTheSame(NewInfo oldItem, NewInfo newItem) {
        //如果有内容不同，就返回false
        if (!StringUtils.equals(oldItem.getUserName(), newItem.getUserName())) {
            return false;
        }
        if (oldItem.getPraise() != newItem.getPraise()) {
            return false;
        }
        if (oldItem.getComment() != newItem.getComment()) {
            return false;
        }
        if (oldItem.getRead() != newItem.getRead()) {
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public Object getChangePayload(NewInfo item1, NewInfo item2) {
        //这里就不用比较核心字段了,一定相等
        Bundle payload = new Bundle();
        if (!StringUtils.equals(item1.getUserName(), item2.getUserName())) {
            payload.putString(PAYLOAD_USER_NAME, item2.getUserName());
        }
        if (item1.getPraise() != item2.getPraise()) {
            payload.putInt(PAYLOAD_PRAISE, item2.getPraise());
        }
        if (item1.getComment() != item2.getComment()) {
            payload.putInt(PAYLOAD_COMMENT, item2.getComment());
        }
        if (item1.getRead() != item2.getRead()) {
            payload.putInt(PAYLOAD_READ_NUMBER, item2.getRead());
        }
        if (payload.size() == 0) {
            return null;
        }
        return payload;
    }

}
