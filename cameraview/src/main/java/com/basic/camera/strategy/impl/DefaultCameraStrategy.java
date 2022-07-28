package com.basic.camera.strategy.impl;

import com.basic.camera.Size;
import com.basic.camera.strategy.ICameraStrategy;

import java.util.SortedSet;

/**
 * 默认相机策略
 * @since 2019/4/8 11:36
 */
public class DefaultCameraStrategy implements ICameraStrategy {
    /**
     * 找到最合适的尺寸
     *
     * @param sizes
     * @return
     */
    @Override
    public Size chooseOptimalPictureSize(SortedSet<Size> sizes) {
        return sizes.last();
    }
}
