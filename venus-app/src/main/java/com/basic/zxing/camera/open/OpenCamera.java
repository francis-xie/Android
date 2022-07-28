package com.basic.zxing.camera.open;

import android.hardware.Camera;

/**
 * 是一个开放的 {@link Camera} 以及它的元数据，比如面向方向和方向。
 */
@SuppressWarnings("deprecation") // camera APIs
public final class OpenCamera {

  private final int index;
  private final Camera camera;
  private final CameraFacing facing;
  private final int orientation;

  public OpenCamera(int index, Camera camera, CameraFacing facing, int orientation) {
    this.index = index;
    this.camera = camera;
    this.facing = facing;
    this.orientation = orientation;
  }

  public Camera getCamera() {
    return camera;
  }

  public CameraFacing getFacing() {
    return facing;
  }

  public int getOrientation() {
    return orientation;
  }

  @Override
  public String toString() {
    return "Camera #" + index + " : " + facing + ',' + orientation;
  }

}
