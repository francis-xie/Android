package com.basic.image.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;

import com.basic.image.PictureFileProvider;
import com.basic.image.config.PictureConfig;

import java.io.File;

/**
 * 相机工具类
 *
 * @author XUE
 * @since 2019/3/28 13:54
 */
public final class CameraUtils {

    private CameraUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 打开相机
     *
     * @param fragment
     * @param outputCameraPath
     */
    public void startOpenCamera(Fragment fragment, String outputCameraPath) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(fragment.getContext().getPackageManager()) != null) {
            File cameraFile = PictureFileUtils.createCameraFile(fragment.getContext(),
                    PictureConfig.TYPE_IMAGE,
                    outputCameraPath, PictureFileUtils.POSTFIX);
            Uri imageUri = parUri(fragment.getContext(), cameraFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            fragment.startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
        }
    }

    /**
     * 打开照相机
     */
    public static void startOpenCamera(Fragment fragment, String path, OnOpenCameraListener listener) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(fragment.getContext().getPackageManager()) != null) {
            File cameraFile = PictureFileUtils.createCameraFile(fragment.getContext(),
                    PictureConfig.TYPE_IMAGE,
                    path, PictureFileUtils.POSTFIX);
            Uri imageUri = parUri(fragment.getContext(), cameraFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (listener != null) {
                listener.onOpenCamera(cameraFile);
            }
            fragment.startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
        }
    }

    /**
     * 打开照相机
     */
    public static void startOpenCamera(Activity activity, String path, OnOpenCameraListener listener) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            File cameraFile = PictureFileUtils.createCameraFile(activity,
                    PictureConfig.TYPE_IMAGE,
                    path, PictureFileUtils.POSTFIX);
            Uri imageUri = parUri(activity, cameraFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (listener != null) {
                listener.onOpenCamera(cameraFile);
            }
            activity.startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
        }
    }

    /**
     * 生成uri
     *
     * @param cameraFile
     * @return
     */
    public static Uri parUri(Context context, File cameraFile) {
        Uri imageUri;
        String authority = context.getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = PictureFileProvider.getUriForFile(context, authority, cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }


    /**
     * 调用系统照相机拍摄的监听
     */
    public interface OnOpenCameraListener {
        /**
         * 开启照相机
         *
         * @param cameraFile
         */
        void onOpenCamera(File cameraFile);
    }

}
