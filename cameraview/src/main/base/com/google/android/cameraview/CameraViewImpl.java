
package com.google.android.cameraview;

import android.view.View;

import com.google.android.cameraview.strategy.ICameraStrategy;
import com.google.android.cameraview.strategy.impl.DefaultCameraStrategy;

import java.util.Set;

abstract class CameraViewImpl {

    protected final Callback mCallback;

    protected final PreviewImpl mPreview;

    protected ICameraStrategy mICameraStrategy;

    CameraViewImpl(Callback callback, PreviewImpl preview) {
        mCallback = callback;
        mPreview = preview;
        mICameraStrategy = _CameraView.getICameraStrategy();
    }

    View getView() {
        return mPreview.getView();
    }

    public CameraViewImpl setICameraStrategy(ICameraStrategy iCameraStrategy) {
        mICameraStrategy = iCameraStrategy;
        return this;
    }

    /**
     * @return {@code true} if the implementation was able to start the camera session.
     */
    abstract boolean start();

    abstract void stop();

    abstract boolean isCameraOpened();

    abstract void setFacing(int facing);

    abstract int getFacing();

    abstract Set<AspectRatio> getSupportedAspectRatios();

    /**
     * @return {@code true} if the aspect ratio was changed.
     */
    abstract boolean setAspectRatio(AspectRatio ratio);

    abstract AspectRatio getAspectRatio();

    abstract void setAutoFocus(boolean autoFocus);

    abstract boolean getAutoFocus();

    abstract void setFlash(int flash);

    abstract int getFlash();

    abstract void takePicture();

    abstract void setDisplayOrientation(int displayOrientation);

    interface Callback {

        void onCameraOpened();

        void onCameraClosed();

        void onPictureTaken(byte[] data);

    }

}
