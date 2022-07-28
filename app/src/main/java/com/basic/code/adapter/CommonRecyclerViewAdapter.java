
package com.basic.code.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.basic.code.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的 RecyclerView Adapter。
 */
public class CommonRecyclerViewAdapter extends RecyclerView.Adapter<CommonRecyclerViewAdapter.ViewHolder> {

    private List<Data> mItems;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public CommonRecyclerViewAdapter() {
        mItems = new ArrayList<>();
    }

    public static List<Data> generateDatas(int count) {
        List<Data> mDatas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            mDatas.add(new Data(String.valueOf(i)));
        }
        return mDatas;
    }

    public void addItem(int position) {
        if (position > mItems.size()) {
            return;
        }

        mItems.add(position, new Data(String.valueOf(position)));
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        if (position >= mItems.size()) {
            return;
        }
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View root = inflater.inflate(R.layout.adapter_item_common_recycler_view, viewGroup, false);
        return new ViewHolder(root, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Data data = mItems.get(i);
        viewHolder.setText(data.text);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItemCount(int count) {
        mItems.clear();
        mItems.addAll(generateDatas(count));

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(RecyclerView.ViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public static class Data {
        public String text;

        public Data(String text) {
            this.text = text;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;
        private CommonRecyclerViewAdapter mAdapter;

        public ViewHolder(View itemView, CommonRecyclerViewAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);

            mAdapter = adapter;

            mTextView = itemView.findViewById(R.id.textView);
        }

        public void setText(String text) {
            mTextView.setText(text);
        }


        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }
}
