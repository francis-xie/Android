
package com.basic.face.widget.imageview.preview.enitity;

import android.graphics.Rect;
import android.os.Parcelable;
import androidx.annotation.Nullable;

/**
 * 图片预览接口
 *

 * @since 2018/12/5 上午11:04
 */
public interface IPreviewInfo extends Parcelable {
    /**
     * @return 图片地址
     */
    String getUrl();

    /**
     * @return 记录坐标
     */
    Rect getBounds();

    /**
     * @return 获取视频链接
     */
    @Nullable
    String getVideoUrl();

}
