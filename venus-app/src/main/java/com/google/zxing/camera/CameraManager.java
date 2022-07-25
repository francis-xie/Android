package com.google.zxing.camera;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.camera.open.OpenCamera;
import com.google.zxing.camera.open.OpenCameraInterface;

import java.io.IOException;

/**
 * 该对象封装了Camera服务对象，并期望是唯一与之通信的对象。
 * implementation封装了获取预览大小的图像所需的步骤，用于预览和解码。
 */
@SuppressWarnings("deprecation") // camera APIs
public final class CameraManager {

  private static final String TAG = CameraManager.class.getSimpleName();

  private static final int MIN_FRAME_WIDTH = 240;
  private static final int MIN_FRAME_HEIGHT = 240;
  private static final int MAX_FRAME_WIDTH = 1200; // = 5/8 * 1920
  private static final int MAX_FRAME_HEIGHT = 675; // = 5/8 * 1080

  private final Context context;
  private final CameraConfigurationManager configManager;
  private OpenCamera camera;
  private AutoFocusManager autoFocusManager;
  private Rect framingRect;
  private Rect framingRectInPreview;
  private boolean initialized;
  private boolean previewing;
  private int requestedCameraId = OpenCameraInterface.NO_REQUESTED_CAMERA;
  private int requestedFramingRectWidth;
  private int requestedFramingRectHeight;
  /**
   * 预览帧在这里交付，我们将其传递给注册的处理程序。一定要清除处理程序，使其只接收一条消息。
   */
  private final PreviewCallback previewCallback;

  public CameraManager(Context context) {
    this.context = context;
    this.configManager = new CameraConfigurationManager(context);
    previewCallback = new PreviewCallback(configManager);
  }

  /**
   * 打开相机驱动，初始化硬件参数。
   *
   * @param holder 相机将绘制预览帧的表面对象。
   * @throws IOException 摄像头驱动打开失败。
   */
  public synchronized void openDriver(SurfaceHolder holder) throws IOException {
    OpenCamera theCamera = camera;
    if (theCamera == null) {
      theCamera = OpenCameraInterface.open(requestedCameraId);
      if (theCamera == null) {
        throw new IOException("Camera.open() failed to return object from driver");
      }
      camera = theCamera;
    }

    if (!initialized) {
      initialized = true;
      configManager.initFromCameraParameters(theCamera);
      if (requestedFramingRectWidth > 0 && requestedFramingRectHeight > 0) {
        setManualFramingRect(requestedFramingRectWidth, requestedFramingRectHeight);
        requestedFramingRectWidth = 0;
        requestedFramingRectHeight = 0;
      }
    }

    Camera cameraObject = theCamera.getCamera();
    Camera.Parameters parameters = cameraObject.getParameters();
    String parametersFlattened = parameters == null ? null : parameters.flatten(); // Save these, temporarily
    try {
      configManager.setDesiredCameraParameters(theCamera, false);
    } catch (RuntimeException re) {
      // Driver failed
      Log.w(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
      Log.i(TAG, "Resetting to saved camera params: " + parametersFlattened);
      // Reset:
      if (parametersFlattened != null) {
        parameters = cameraObject.getParameters();
        parameters.unflatten(parametersFlattened);
        try {
          cameraObject.setParameters(parameters);
          configManager.setDesiredCameraParameters(theCamera, true);
        } catch (RuntimeException re2) {
          // Well, darn. Give up
          Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
        }
      }
    }
    cameraObject.setPreviewDisplay(holder);

  }

  public synchronized boolean isOpen() {
    return camera != null;
  }

  /**
   * 关闭相机驱动程序，如果仍在使用。
   */
  public synchronized void closeDriver() {
    if (camera != null) {
      camera.getCamera().release();
      camera = null;
      // 确保每次我们关闭相机时清除这些，以便任何扫描矩形
      // 意图请求被遗忘。
      framingRect = null;
      framingRectInPreview = null;
    }
  }

  /**
   * 要求相机硬件开始绘制预览帧到屏幕。
   */
  public synchronized void startPreview() {
    OpenCamera theCamera = camera;
    if (theCamera != null && !previewing) {
      theCamera.getCamera().startPreview();
      previewing = true;
      autoFocusManager = new AutoFocusManager(context, theCamera.getCamera());
    }
  }

  /**
   * 告诉相机停止绘制预览帧。
   */
  public synchronized void stopPreview() {
    if (autoFocusManager != null) {
      autoFocusManager.stop();
      autoFocusManager = null;
    }
    if (camera != null && previewing) {
      camera.getCamera().stopPreview();
      previewCallback.setHandler(null, 0);
      previewing = false;
    }
  }

  /**
   * 方便的方法 {@link CaptureActivity}
   *
   * @param newSetting if {@code true}, 如果灯目前处于关闭状态，应将其打开。反之亦然。
   */
  public synchronized void setTorch(boolean newSetting) {
    OpenCamera theCamera = camera;
    if (theCamera != null && newSetting != configManager.getTorchState(theCamera.getCamera())) {
      boolean wasAutoFocusManager = autoFocusManager != null;
      if (wasAutoFocusManager) {
        autoFocusManager.stop();
        autoFocusManager = null;
      }
      configManager.setTorch(theCamera.getCamera(), newSetting);
      if (wasAutoFocusManager) {
        autoFocusManager = new AutoFocusManager(context, theCamera.getCamera());
        autoFocusManager.start();
      }
    }
  }

  /**
   * 一个单独的预览帧将被返回给提供的处理程序。数据将以字节[]的形式到达
   * 在消息。Obj字段，宽度和高度编码为消息。__arg1 message.arg2,
   * 分别。
   *
   * @param handler 将消息发送给的处理程序。
   * @param message 要发送的消息的哪个字段。
   */
  public synchronized void requestPreviewFrame(Handler handler, int message) {
    OpenCamera theCamera = camera;
    if (theCamera != null && previewing) {
      previewCallback.setHandler(handler, message);
      theCamera.getCamera().setOneShotPreviewCallback(previewCallback);
    }
  }

  /**
   * 计算UI应该绘制的框架矩形，以显示用户在哪里放置
   * 条形码。这个目标有助于对齐，以及迫使用户持有设备
   * 要足够远，以确保图像对焦。
   *
   * @return 以窗口坐标在屏幕上绘制的矩形。
   */
  public synchronized Rect getFramingRect() {
    if (framingRect == null) {
      if (camera == null) {
        return null;
      }
      Point screenResolution = configManager.getScreenResolution();
      if (screenResolution == null) {
        // Called early, before init even finished
        return null;
      }

      int width = findDesiredDimensionInRange(screenResolution.x, MIN_FRAME_WIDTH, MAX_FRAME_WIDTH);
      int height = findDesiredDimensionInRange(screenResolution.y, MIN_FRAME_HEIGHT, MAX_FRAME_HEIGHT);

      int leftOffset = (screenResolution.x - width) / 2;
      int topOffset = (screenResolution.y - height) / 2;
      framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
    }
    return framingRect;
  }

  private static int findDesiredDimensionInRange(int resolution, int hardMin, int hardMax) {
    int dim = 5 * resolution / 8; // Target 5/8 of each dimension
    if (dim < hardMin) {
      return hardMin;
    }
    return Math.min(dim, hardMax);
  }

  /**
   * 就像 {@link #getFramingRect} 但是坐标是基于预览框架的，
   * 而不是UI /屏幕。
   *
   * @return {@link Rect} 用预览大小表示条码扫描区域
   */
  public synchronized Rect getFramingRectInPreview() {
    if (framingRectInPreview == null) {
      Rect framingRect = getFramingRect();
      if (framingRect == null) {
        return null;
      }
      Rect rect = new Rect(framingRect);
      Point cameraResolution = configManager.getCameraResolution();
      Point screenResolution = configManager.getScreenResolution();
      if (cameraResolution == null || screenResolution == null) {
        // Called early, before init even finished
        return null;
      }
      rect.left = rect.left * cameraResolution.x / screenResolution.x;
      rect.right = rect.right * cameraResolution.x / screenResolution.x;
      rect.top = rect.top * cameraResolution.y / screenResolution.y;
      rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
      framingRectInPreview = rect;
    }
    return framingRectInPreview;
  }


  /**
   * 允许第三方应用程序指定相机ID，而不是确定
   * 它会自动根据可用的相机和它们的方向。
   *
   * @param cameraId 摄像头要使用的摄像头ID。负值表示“无偏好”。
   */
  public synchronized void setManualCameraId(int cameraId) {
    requestedCameraId = cameraId;
  }

  /**
   * 允许第三方应用程序指定扫描矩形的尺寸，而不是确定
   * 他们自动基于屏幕分辨率。
   *
   * @param width  要扫描的像素宽度。
   * @param height 以像素为单位的扫描高度。
   */
  public synchronized void setManualFramingRect(int width, int height) {
    if (initialized) {
      Point screenResolution = configManager.getScreenResolution();
      if (width > screenResolution.x) {
        width = screenResolution.x;
      }
      if (height > screenResolution.y) {
        height = screenResolution.y;
      }
      int leftOffset = (screenResolution.x - width) / 2;
      int topOffset = (screenResolution.y - height) / 2;
      framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
      Log.d(TAG, "Calculated manual framing rect: " + framingRect);
      framingRectInPreview = null;
    } else {
      requestedFramingRectWidth = width;
      requestedFramingRectHeight = height;
    }
  }

  /**
   * 工厂方法，根据格式构建适当的LuminanceSource对象
   * 预览缓冲区，如Camera.Parameters所述。
   *
   * @param data   一个预览帧。
   * @param width  图像的宽度。
   * @param height 图像的高度。
   * @return PlanarYUVLuminanceSource实例。
   */
  public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
    Rect rect = getFramingRectInPreview();
    if (rect == null) {
      return null;
    }
    // 继续假设它是YUV，而不是死亡。
    return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
      rect.width(), rect.height(), false);
  }

}
