
package com.basic.code.adapter.dropdownmenu;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.basic.face.adapter.listview.BaseListAdapter;
import com.basic.code.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListDropDownAdapter extends BaseListAdapter<String, ListDropDownAdapter.ViewHolder> {

    public ListDropDownAdapter(Context context, String[] data) {
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
                holder.mText.setBackgroundResource(R.color.check_bg);
            } else {
                holder.mText.setSelected(false);
                holder.mText.setBackgroundResource(R.color.white);
            }
        }
    }

    static class ViewHolder {
        @BindView(R.id.text)
        TextView mText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);

            mText.setGravity(Gravity.CENTER);
        }
    }
}
