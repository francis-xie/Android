
package com.basic.code.adapter.dropdownmenu;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.basic.face.adapter.listview.BaseListAdapter;
import com.basic.face.utils.ResUtils;
import com.basic.code.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CityDropDownAdapter extends BaseListAdapter<String, CityDropDownAdapter.ViewHolder> {

    public CityDropDownAdapter(Context context) {
        super(context);
    }

    public CityDropDownAdapter(Context context, String[] data) {
        super(context, data);
    }

    @Override
    protected ViewHolder newViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_drop_down_list_item;
    }

    @Override
    protected void convert(ViewHolder holder, String item, int position) {
        holder.mText.setText(item);
        if (mSelectPosition != -1) {
            if (mSelectPosition == position) {
                holder.mText.setSelected(true);
                holder.mText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ResUtils.getVectorDrawable(holder.mText.getContext(), R.drawable.ic_checked_right), null);
            } else {
                holder.mText.setSelected(false);
                holder.mText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            }
        }
    }

    static class ViewHolder {
        @BindView(R.id.text)
        TextView mText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
