
package com.basic.tailor;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.basic.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.basic.tailor.model.CutInfo;

import java.util.ArrayList;
import java.util.List;

/**
 
 * project：PictureSelector
 * package：com.luck.picture.adapter
 * email：893855882@qq.com
 * data：16/12/31
 */

public class PicturePhotoGalleryAdapter extends RecyclerView.Adapter<PicturePhotoGalleryAdapter.ViewHolder> {

    private Context context;
    private List<CutInfo> list = new ArrayList<>();
    private LayoutInflater mInflater;

    public PicturePhotoGalleryAdapter(Context context, List<CutInfo> list) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    public void bindData(List<CutInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = mInflater.inflate(R.layout.tailor_picture_gf_adapter_edit_list,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String path = "";
        CutInfo photoInfo = list.get(position);
        if (photoInfo != null) {
            path = photoInfo.getPath();
        }
        if (photoInfo.isCut()) {
            holder.iv_dot.setVisibility(View.VISIBLE);
            holder.iv_dot.setImageResource(R.drawable.tailor_oval_true);
        } else {
            holder.iv_dot.setVisibility(View.GONE);
        }

        RequestOptions options = new RequestOptions()
                .placeholder(R.color.tailor_color_grey)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(path)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(options)
                .into(holder.mIvPhoto);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvPhoto;
        ImageView iv_dot;

        public ViewHolder(View view) {
            super(view);
            mIvPhoto = (ImageView) view.findViewById(R.id.iv_photo);
            iv_dot = (ImageView) view.findViewById(R.id.iv_dot);
        }
    }

}
