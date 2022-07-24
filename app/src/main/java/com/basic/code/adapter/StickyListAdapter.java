
package com.basic.code.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.face.adapter.recyclerview.BaseRecyclerAdapter;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.face.adapter.recyclerview.sticky.FullSpanUtils;
import com.basic.code.R;
import com.basic.code.adapter.entity.NewInfo;
import com.basic.code.adapter.entity.StickyItem;

/**

 * @since 2020/4/25 1:23 AM
 */
public class StickyListAdapter extends BaseRecyclerAdapter<StickyItem> {

    /**
     * 头部标题
     */
    public static final int TYPE_HEAD_STICKY = 1;
    /**
     * 新闻信息
     */
    private static final int TYPE_NEW_INFO = 2;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        FullSpanUtils.onAttachedToRecyclerView(recyclerView, this, TYPE_HEAD_STICKY);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerViewHolder holder) {
        FullSpanUtils.onViewAttachedToWindow(holder, this, TYPE_HEAD_STICKY);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isHeadSticky()) {
            return TYPE_HEAD_STICKY;
        } else {
            return TYPE_NEW_INFO;
        }
    }

    @Override
    public int getItemLayoutId(int viewType) {
        if (viewType == TYPE_HEAD_STICKY) {
            return R.layout.adapter_vlayout_title_item;
        } else {
            return R.layout.adapter_news_facelayout_list_item;
        }
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, StickyItem item) {
        if (item == null) {
            return;
        }

        if (item.isHeadSticky()) {
            holder.text(R.id.tv_title, item.getHeadTitle());
        } else {
            NewInfo info = item.getNewInfo();
            holder.text(R.id.tv_user_name, info.getUserName());
            holder.text(R.id.tv_tag, info.getTag());
            holder.text(R.id.tv_title, info.getTitle());
            holder.text(R.id.tv_summary, info.getSummary());
            holder.text(R.id.tv_praise, info.getPraise() == 0 ? "点赞" : String.valueOf(info.getPraise()));
            holder.text(R.id.tv_comment, info.getComment() == 0 ? "评论" : String.valueOf(info.getComment()));
            holder.text(R.id.tv_read, "阅读量 " + info.getRead());
            holder.image(R.id.iv_image, info.getImageUrl());
        }
    }
}
