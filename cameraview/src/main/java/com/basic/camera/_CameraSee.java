package com.basic.camera;

import com.basic.camera.strategy.ICameraStrategy;
import com.basic.camera.strategy.impl.DefaultCameraStrategy;

/**
 * @since 2019/4/8 11:51
 */
class _CameraSee {

    /**
     * 默认的相机策略
     */
    private static ICameraStrategy sICameraStrategy = new DefaultCameraStrategy();

    private static AspectRatio sDefaultAspectRatio = AspectRatio.of(16, 9);

    public static void setICameraStrategy(ICameraStrategy sICameraStrategy) {
        _CameraSee.sICameraStrategy = sICameraStrategy;
    }

    public static ICameraStrategy getICameraStrategy() {
        return sICameraStrategy;
    }

    public static void setDefaultAspectRatio(AspectRatio aspectRatio) {
        _CameraSee.sDefaultAspectRatio = aspectRatio;
    }

    public static AspectRatio getDefaultAspectRatio() {
        return sDefaultAspectRatio;
    }
}
