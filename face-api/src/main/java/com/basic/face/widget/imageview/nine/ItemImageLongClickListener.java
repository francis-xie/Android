
package com.basic.face.widget.imageview.nine;

import android.widget.ImageView;

import java.util.List;

/**
 * 九宫图长按
 */
public interface ItemImageLongClickListener<T> {
    /**
     * 长按图片
     *
     * @param imageView
     * @param index     索引
     * @param list      图片列表
     * @return 是否消费了事件
     */
    boolean onItemImageLongClick(ImageView imageView, int index, List<T> list);
}
