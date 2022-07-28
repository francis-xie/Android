
package com.basic.code.adapter;

import android.content.Context;
import android.view.View;

import com.basic.face.adapter.listview.BaseListAdapter;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.face.utils.ResUtils;
import com.basic.code.R;

import java.util.Collection;

public class SimpleListViewAdapter extends BaseListAdapter<String, RecyclerViewHolder> {

    public SimpleListViewAdapter(Context context) {
        super(context);
    }

    public SimpleListViewAdapter(Context context, Collection<String> data) {
        super(context, data);
    }

    public SimpleListViewAdapter(Context context, String[] data) {
        super(context, data);
    }

    @Override
    protected RecyclerViewHolder newViewHolder(View convertView) {
        return new RecyclerViewHolder(convertView);
    }

    @Override
    protected int getLayoutId() {
        return android.R.layout.simple_list_item_2;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, String item, int position) {
        holder.text(android.R.id.text1, ResUtils.getResources().getString(R.string.item_example_number_title, position));
        holder.text(android.R.id.text2, ResUtils.getResources().getString(R.string.item_example_number_abstract, position));
        holder.textColorId(android.R.id.text2, R.color.face_config_color_light_blue_gray);
    }

}
