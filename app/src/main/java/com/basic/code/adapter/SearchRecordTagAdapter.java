
package com.basic.code.adapter;

import androidx.annotation.NonNull;

import com.basic.face.adapter.recyclerview.BaseRecyclerAdapter;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.code.R;
import com.basic.code.base.db.entity.SearchRecord;

public class SearchRecordTagAdapter extends BaseRecyclerAdapter<SearchRecord> {

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.adapter_search_record_tag_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, SearchRecord item) {
        if (item != null) {
            holder.text(R.id.tv_tag, item.getContent());
        }
    }
}
