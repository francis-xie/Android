
package com.basic.code.adapter.dropdownmenu;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.basic.face.adapter.listview.BaseListAdapter;
import com.basic.code.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 星座适配器
 *

 * @since 2019-11-30 15:55
 */
public class ConstellationAdapter extends BaseListAdapter<String, ConstellationAdapter.ViewHolder> {

    public ConstellationAdapter(Context context, String[] data) {
        super(context, data);
    }

    @Override
    protected ViewHolder newViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_drop_down_constellation;
    }

    @Override
    protected void convert(ViewHolder holder, String item, int position) {
        holder.mText.setText(item);
        if (mSelectPosition != -1) {
            if (mSelectPosition == position) {
                holder.mText.setSelected(true);
            } else {
                holder.mText.setSelected(false);
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
