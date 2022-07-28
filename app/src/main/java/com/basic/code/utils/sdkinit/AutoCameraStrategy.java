
package com.basic.code.utils.sdkinit;

import com.basic.camera.Size;
import com.basic.camera.strategy.ICameraStrategy;

import java.util.SortedSet;

/**
 * 自适应
 */
public class AutoCameraStrategy implements ICameraStrategy {

    private long mTargetPicturePixels;

    public AutoCameraStrategy(long targetPicturePixels) {
        mTargetPicturePixels = targetPicturePixels;
    }

    /**
     * 找到最合适的尺寸(拍摄的照片）
     *
     * @param sizes
     * @return
     */
    @Override
    public Size chooseOptimalPictureSize(SortedSet<Size> sizes) {
        //从小到大排序
        for (Size size : sizes) {
            //尺寸不要超过指定的PicturePixels.
            if (((long) size.getWidth()) * ((long) size.getHeight()) >= mTargetPicturePixels) {
                return size;
            }
        }
        //找不到就选择最大的尺寸
        return sizes.last();
    }

}
