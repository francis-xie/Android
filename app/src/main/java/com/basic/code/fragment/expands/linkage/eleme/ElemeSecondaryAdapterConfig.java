
package com.basic.code.fragment.expands.linkage.eleme;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig;
import com.basic.code.R;

/**
 * 饿了么列表适配器
 */
public class ElemeSecondaryAdapterConfig implements ILinkageSecondaryAdapterConfig<ElemeGroupedItem.ItemInfo> {

    private static final int SPAN_COUNT = 2;

    private Context mContext;

    private OnSecondaryItemClickListener mItemClickListener;

    public ElemeSecondaryAdapterConfig(OnSecondaryItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public ElemeSecondaryAdapterConfig setOnItemClickListener(OnSecondaryItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        return this;
    }

    @Override
    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public int getGridLayoutId() {
        return R.layout.adapter_eleme_secondary_grid;
    }

    @Override
    public int getLinearLayoutId() {
        return R.layout.adapter_eleme_secondary_linear;
    }


    @Override
    public int getHeaderLayoutId() {
        return R.layout.default_adapter_linkage_secondary_header;
    }

    @Override
    public int getFooterLayoutId() {
        return R.layout.adapter_linkage_empty_footer;
    }

    @Override
    public int getHeaderTextViewId() {
        return R.id.secondary_header;
    }

    @Override
    public int getSpanCountOfGridMode() {
        return SPAN_COUNT;
    }

    @Override
    public void onBindViewHolder(final LinkageSecondaryViewHolder holder,
                                 final BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

        ((TextView) holder.getView(R.id.iv_goods_name)).setText(item.info.getTitle());
        Glide.with(mContext).load(item.info.getImgUrl()).into((ImageView) holder.getView(R.id.iv_goods_img));

        ViewGroup viewGroup = holder.getView(R.id.iv_goods_item);
        viewGroup.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onSecondaryItemClick(holder, viewGroup, item);
            }
        });

        holder.getView(R.id.iv_goods_add).setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onGoodAdd(v, item);
            }
        });
    }

    @Override
    public void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder holder,
                                       BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

        ((TextView) holder.getView(R.id.secondary_header)).setText(item.header);
    }

    @Override
    public void onBindFooterViewHolder(LinkageSecondaryFooterViewHolder holder,
                                       BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

    }


    public interface OnSecondaryItemClickListener {

        void onSecondaryItemClick(LinkageSecondaryViewHolder holder, ViewGroup view, BaseGroupedItem<ElemeGroupedItem.ItemInfo> item);

        void onGoodAdd(View view, BaseGroupedItem<ElemeGroupedItem.ItemInfo> item);

    }
}
