
package com.basic.face.widget.imageview.nine;

import android.widget.ImageView;

import java.util.List;

/**
 * 九宫图条目点击
 */
public interface ItemImageClickListener<T> {
    /**
     * 九宫格条目点击
     *
     * @param imageView
     * @param index     索引
     * @param list      图片列表
     */
    void onItemImageClick(ImageView imageView, int index, List<T> list);
}
