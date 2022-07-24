
package com.basic.code.fragment.components.refresh.sample.sortedlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.face.utils.WidgetUtils;
import com.basic.code.R;
import com.basic.code.adapter.entity.NewInfo;
import com.basic.code.fragment.components.refresh.sample.diffutil.DiffUtilCallback;
import com.basic.tools.common.CollectionUtils;
import com.basic.tools.common.logger.Logger;

import java.util.List;

/**
 * 使用SortedList作为数据源的Adapter
 *

 * @since 2020/6/28 12:00 AM
 */
public class SortedListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private SortedList<NewInfo> mDataSource;

    public SortedListAdapter() {

    }

    public SortedListAdapter(SortedList<NewInfo> data) {
        mDataSource = data;
    }

    public SortedListAdapter setDataSource(SortedList<NewInfo> dataSource) {
        mDataSource = dataSource;
        return this;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_news_card_view_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        NewInfo model = getItem(position);
        if (model != null) {
            holder.text(R.id.tv_user_name, model.getUserName());
            holder.text(R.id.tv_tag, model.getTag());
            holder.text(R.id.tv_title, model.getTitle());
            holder.text(R.id.tv_summary, model.getSummary());
            holder.text(R.id.tv_praise, model.getPraise() == 0 ? "点赞" : String.valueOf(model.getPraise()));
            holder.text(R.id.tv_comment, model.getComment() == 0 ? "评论" : String.valueOf(model.getComment()));
            holder.text(R.id.tv_read, "阅读量 " + model.getRead());
            holder.image(R.id.iv_image, model.getImageUrl());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (CollectionUtils.isEmpty(payloads)) {
            Logger.e("正在进行全量刷新:" + position);
            onBindViewHolder(holder, position);
            return;
        }
        // payloads为非空的情况，进行局部刷新

        //取出我们在getChangePayload（）方法返回的bundle
        Bundle payload = WidgetUtils.getChangePayload(payloads);
        if (payload == null) {
            return;
        }

        Logger.e("正在进行增量刷新:" + position);
        NewInfo newInfo = getItem(position);
        for (String key : payload.keySet()) {
            switch (key) {
                case DiffUtilCallback.PAYLOAD_USER_NAME:
                    //这里可以用payload里的数据，不过newInfo也是新的 也可以用
                    holder.text(R.id.tv_user_name, newInfo.getUserName());
                    break;
                case DiffUtilCallback.PAYLOAD_PRAISE:
                    holder.text(R.id.tv_praise, payload.getInt(DiffUtilCallback.PAYLOAD_PRAISE) == 0 ? "点赞" : String.valueOf(payload.getInt(DiffUtilCallback.PAYLOAD_PRAISE)));
                    break;
                case DiffUtilCallback.PAYLOAD_COMMENT:
                    holder.text(R.id.tv_comment, payload.getInt(DiffUtilCallback.PAYLOAD_COMMENT) == 0 ? "评论" : String.valueOf(payload.getInt(DiffUtilCallback.PAYLOAD_COMMENT)));
                    break;
                case DiffUtilCallback.PAYLOAD_READ_NUMBER:
                    holder.text(R.id.tv_read, "阅读量 " + payload.getInt(DiffUtilCallback.PAYLOAD_READ_NUMBER));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataSource != null ? mDataSource.size() : 0;
    }

    /**
     * 获取列表项
     *
     * @param position
     * @return
     */
    public NewInfo getItem(int position) {
        return checkPosition(position) ? mDataSource.get(position) : null;
    }

    private boolean checkPosition(int position) {
        return position >= 0 && position < mDataSource.size();
    }
}
