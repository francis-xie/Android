package com.google.android.cameraview;

import com.google.android.cameraview.strategy.ICameraStrategy;
import com.google.android.cameraview.strategy.impl.DefaultCameraStrategy;

/**
 * @since 2019/4/8 11:51
 */
class _CameraView {

    /**
     * 默认的相机策略
     */
    private static ICameraStrategy sICameraStrategy = new DefaultCameraStrategy();

    private static com.google.android.cameraview.AspectRatio sDefaultAspectRatio = com.google.android.cameraview.AspectRatio.of(16, 9);

    public static void setICameraStrategy(ICameraStrategy sICameraStrategy) {
        _CameraView.sICameraStrategy = sICameraStrategy;
    }

    public static ICameraStrategy getICameraStrategy() {
        return sICameraStrategy;
    }

    public static void setDefaultAspectRatio(com.google.android.cameraview.AspectRatio aspectRatio) {
        _CameraView.sDefaultAspectRatio = aspectRatio;
    }

    public static com.google.android.cameraview.AspectRatio getDefaultAspectRatio() {
        return sDefaultAspectRatio;
    }
}
