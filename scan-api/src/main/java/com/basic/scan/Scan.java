
package com.basic.scan;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.google.zxing.Result;
import com.basic.scan.camera.AutoFocusCallback;
import com.basic.scan.camera.CameraManager;
import com.basic.scan.logs.QCLog;
import com.basic.scan.ui.CaptureActivity;
import com.basic.scan.ui.CaptureFragment;
import com.basic.scan.util.QRCodeAnalyzeUtils;
import com.basic.scan.util.QRCodeProduceUtils;
import com.basic.scan.util.QRCodeProduceUtils.Builder;

import java.util.List;

import static com.basic.scan.util.QRCodeProduceUtils.QRCODE_BITMAP_MAX_SIZE;

/**
 * 二维码 Scan API
 */
public class Scan {

    /**
     * 扫描返回的结果
     */
    public static final String RESULT_TYPE = "result_type";
    /**
     * 扫描到的数据
     */
    public static final String RESULT_DATA = "result_data";
    /**
     * 扫描成功
     */
    public static final int RESULT_SUCCESS = 1;
    /**
     * 扫描失败
     */
    public static final int RESULT_FAILED = 2;

    /**
     * CaptureFragment加载的容器Id
     */
    public static final String KEY_LAYOUT_ID = "key_layout_id";

    /**
     * 是否是重复扫码
     */
    public static final String KEY_IS_REPEATED = "key_is_repeated";

    /**
     * 扫码的间隙
     */
    public static final String KEY_SCAN_INTERVAL = "key_scan_interval";

    /**
     * 默认二维码扫描的activity启动
     */
    public static final String ACTION_DEFAULT_CAPTURE = "com.basic.scan.ui.captureactivity";

    /**
     * 自动聚焦的间期
     */
    public static long sAutoFocusInterval = AutoFocusCallback.AUTO_FOCUS_INTERVAL_MS;

    /**
     * 设置是否打开调试
     *
     * @param isDebug
     */
    public static void debug(boolean isDebug) {
        QCLog.debug(isDebug);
    }

    /**
     * 设置调试模式
     *
     * @param tag
     */
    public static void debug(String tag) {
        QCLog.debug(tag);
    }

    /**
     * 设置自动聚焦的间期【默认是1500ms】
     *
     * @param sAutoFocusInterval
     */
    public static void setAutoFocusInterval(long sAutoFocusInterval) {
        Scan.sAutoFocusInterval = sAutoFocusInterval;
    }

    public static long getAutoFocusInterval() {
        return sAutoFocusInterval;
    }

    //==================================调用默认二维码扫描=================================//

    /**
     * 开始二维码扫描
     *
     * @param fragment
     * @param requestCode 请求码
     */
    public static void startScan(Fragment fragment, int requestCode) {
        CaptureActivity.start(fragment, requestCode, R.style.ScanTheme);
    }

    /**
     * 开始二维码扫描
     *
     * @param fragment
     * @param requestCode 请求码
     * @param theme       主题
     */
    public static void startScan(Fragment fragment, int requestCode, int theme) {
        CaptureActivity.start(fragment, requestCode, theme);
    }

    /**
     * 开始二维码扫描
     *
     * @param activity
     * @param requestCode 请求码
     */
    public static void startScan(Activity activity, int requestCode) {
        CaptureActivity.start(activity, requestCode, R.style.ScanTheme);
    }

    /**
     * 开始二维码扫描
     *
     * @param activity
     * @param requestCode 请求码
     * @param theme       主题
     */
    public static void startScan(Activity activity, int requestCode, int theme) {
        CaptureActivity.start(activity, requestCode, theme);
    }

    //==================================CaptureFragment=================================//

    /**
     * 获取CaptureFragment设置layout参数
     *
     * @param layoutId
     */
    public static CaptureFragment getCaptureFragment(int layoutId) {
        return CaptureFragment.newInstance(layoutId);
    }


    /**
     * 获取CaptureFragment设置扫码参数
     *
     * @param layoutId     布局id
     * @param isRepeated   是否重复扫码
     * @param scanInterval 扫码间隙
     * @return
     */
    public static CaptureFragment getCaptureFragment(int layoutId, boolean isRepeated, long scanInterval) {
        return CaptureFragment.newInstance(layoutId, isRepeated, scanInterval);
    }

    /**
     * 为CaptureFragment设置layout参数
     *
     * @param captureFragment
     * @param layoutId
     */
    public static void setFragmentArgs(CaptureFragment captureFragment, int layoutId) {
        setFragmentArgs(captureFragment, layoutId, false, 0);
    }

    /**
     * 为CaptureFragment设置layout参数
     *
     * @param captureFragment
     * @param layoutId
     */
    public static void setFragmentArgs(CaptureFragment captureFragment, int layoutId, boolean isRepeated, long scanInterval) {
        if (captureFragment == null || layoutId == -1) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LAYOUT_ID, layoutId);
        bundle.putBoolean(KEY_IS_REPEATED, isRepeated);
        bundle.putLong(KEY_SCAN_INTERVAL, scanInterval);
        captureFragment.setArguments(bundle);
    }

    //================================二维码解析===================================//

    /**
     * 解析二维码（接口回调返回结果）
     *
     * @param QRCodePicPath   二维码图片的路径
     * @param analyzeCallback 解析的回调
     */
    public static void analyzeQRCode(String QRCodePicPath, QRCodeAnalyzeUtils.AnalyzeCallback analyzeCallback) {
        QRCodeAnalyzeUtils.analyze(QRCodePicPath, analyzeCallback);
    }

    /**
     * 解析二维码（接口回调返回结果）
     *
     * @param QRCodeBitmap    二维码图片
     * @param analyzeCallback 解析的回调
     */
    public static void analyzeQRCode(Bitmap QRCodeBitmap, QRCodeAnalyzeUtils.AnalyzeCallback analyzeCallback) {
        QRCodeAnalyzeUtils.analyze(QRCodeBitmap, analyzeCallback);
    }

    /**
     * 解析二维码（简单返回结果，扫描失败返回空）
     *
     * @param QRCodePicPath 二维码图片的路径
     */
    public static String analyzeQRCode(String QRCodePicPath) {
        return QRCodeAnalyzeUtils.analyze(QRCodePicPath);
    }

    /**
     * 解析二维码（简单返回结果）
     *
     * @param QRCodeBitmap 二维码图片
     */
    public static Result analyzeQRCode(Bitmap QRCodeBitmap) {
        return QRCodeAnalyzeUtils.analyze(QRCodeBitmap);
    }

    /**
     * 获取解析二维码的结果
     *
     * @param QRCodePicPath 二维码图片的路径
     */
    public static Result getAnalyzeQRCodeResult(String QRCodePicPath) {
        return QRCodeAnalyzeUtils.analyzeQRCode(QRCodePicPath);
    }

    //================================二维码生成===================================//

    /**
     * 生成含图标的二维码图片
     *
     * @param contents 二维码写入的数据
     * @param width    二维码的宽
     * @param height   二维码的高
     * @param logo     二维码中央的logo
     * @return 含图标的二维码
     */
    public static Bitmap createQRCodeWithLogo(String contents, int width, int height, Bitmap logo) {
        return QRCodeProduceUtils.create(contents, width, height, logo);
    }

    /**
     * 生成含图标的二维码图片
     *
     * @param contents 二维码写入的数据
     * @param logo     二维码中央的logo
     * @return 含图标的二维码
     */
    public static Bitmap createQRCodeWithLogo(String contents, Bitmap logo) {
        return QRCodeProduceUtils.create(contents, QRCODE_BITMAP_MAX_SIZE, QRCODE_BITMAP_MAX_SIZE, logo);
    }

    /**
     * 获取二维码生成构建者
     *
     * @param contents
     * @return
     */
    public static Builder newQRCodeBuilder(String contents) {
        return QRCodeProduceUtils.newBuilder(contents);
    }

    //================================FlashLight===================================//

    /**
     * 开关闪光灯
     *
     * @param isEnable 是否开启闪光灯
     */
    public static void switchFlashLight(boolean isEnable) throws RuntimeException {
        if (isEnable) {
            enableFlashLight();
        } else {
            disableFlashLight();
        }
    }

    /**
     * 关闭闪光灯
     */
    public static void disableFlashLight() {
        Camera camera = CameraManager.get().getCamera();
        if (camera != null) {
            Camera.Parameters parameter = camera.getParameters();
            if (parameter != null) {
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameter);
            }
        }
    }

    /**
     * 开启闪光灯
     */
    public static void enableFlashLight() {
        Camera camera = CameraManager.get().getCamera();
        if (camera != null) {
            Camera.Parameters parameter = camera.getParameters();
            if (parameter != null) {
                List<String> supportedFlashModes = parameter.getSupportedFlashModes();
                if (supportedFlashModes != null) {
                    if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    } else if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_ON)) {
                        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    }
                    camera.setParameters(parameter);
                }
            }
        }
    }

    /**
     * @return 闪光灯是否打开
     */
    public static boolean isFlashLightOpen() {
        String mode = getFlashMode();
        if (mode != null && mode.length() > 0) {
            return mode.equals(Camera.Parameters.FLASH_MODE_TORCH)
                    || mode.equals(Camera.Parameters.FLASH_MODE_ON);
        }
        return false;
    }

    /**
     * 获取闪光灯的模式
     *
     * @return
     */
    private static String getFlashMode() {
        Camera camera = CameraManager.get().getCamera();
        if (camera != null) {
            Camera.Parameters parameter = camera.getParameters();
            return parameter.getFlashMode();
        }
        return null;
    }
}
