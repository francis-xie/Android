
package com.basic.code.fragment.expands.linkage.custom;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.basic.code.R;

/**
 * 自定义主菜单适配器
 */
public class CustomLinkagePrimaryAdapterConfig implements ILinkagePrimaryAdapterConfig {

    private static final int MARQUEE_REPEAT_LOOP_MODE = -1;
    private static final int MARQUEE_REPEAT_NONE_MODE = 0;
    private Context mContext;
    private OnPrimaryItemClickListener mItemClickListener;

    public CustomLinkagePrimaryAdapterConfig(OnPrimaryItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public CustomLinkagePrimaryAdapterConfig setOnItemClickListener(OnPrimaryItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        return this;
    }

    @Override
    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.default_adapter_linkage_primary;
    }

    @Override
    public int getGroupTitleViewId() {
        return R.id.tv_group;
    }

    @Override
    public int getRootViewId() {
        return R.id.layout_group;
    }

    @Override
    public void onBindViewHolder(LinkagePrimaryViewHolder holder, boolean selected, String title) {
        TextView tvTitle = ((TextView) holder.mGroupTitle);
        tvTitle.setText(title);

        tvTitle.setBackgroundColor(mContext.getResources().getColor(selected ? R.color.colorAccent : R.color.colorWhite));
        tvTitle.setTextColor(ContextCompat.getColor(mContext, selected ? R.color.colorWhite : R.color.colorGray));
        tvTitle.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
        tvTitle.setFocusable(selected);
        tvTitle.setFocusableInTouchMode(selected);
        tvTitle.setMarqueeRepeatLimit(selected ? MARQUEE_REPEAT_LOOP_MODE : MARQUEE_REPEAT_NONE_MODE);
    }

    @Override
    public void onItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
        if (mItemClickListener != null) {
            mItemClickListener.onPrimaryItemClick(holder, view, title);
        }
    }

    public interface OnPrimaryItemClickListener {
        /**
         * we suggest you get position by holder.getAdapterPosition
         *
         * @param holder primaryHolder
         * @param view   view
         * @param title  groupTitle
         */
        void onPrimaryItemClick(LinkagePrimaryViewHolder holder, View view, String title);
    }
}
