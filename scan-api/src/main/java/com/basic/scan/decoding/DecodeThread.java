
package com.basic.scan.decoding;

import android.os.Handler;
import android.os.Looper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;
import com.basic.scan.ui.ICaptureView;

import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

/**
 * 解码线程
 *

 * @since 2019/1/17 上午12:03
 */
final class DecodeThread extends Thread {

    public static final String BARCODE_BITMAP = "barcode_bitmap";
    private final ICaptureView mCaptureView;
    private final Hashtable<DecodeHintType, Object> mHints;
    private Handler mHandler;
    private final CountDownLatch mHandlerInitLatch;

    DecodeThread(ICaptureView captureView,
                 Vector<BarcodeFormat> decodeFormats,
                 String characterSet,
                 ResultPointCallback resultPointCallback) {

        mCaptureView = captureView;
        mHandlerInitLatch = new CountDownLatch(1);

        mHints = new Hashtable<>(3);

        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<>();
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }

        mHints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        if (characterSet != null) {
            mHints.put(DecodeHintType.CHARACTER_SET, characterSet);
        }

        mHints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
    }

    Handler getHandler() {
        try {
            mHandlerInitLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            // continue?
        }
        return mHandler;
    }

    @Override
    public void run() {
        Looper.prepare();
        mHandler = new DecodeHandler(mCaptureView, mHints);
        mHandlerInitLatch.countDown();
        Looper.loop();
    }

}
