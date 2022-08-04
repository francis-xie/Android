
package com.basic.scan.decoding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.basic.R;
import com.basic.scan.camera.CameraManager;
import com.basic.scan.camera.PlanarYUVLuminanceSource;
import com.basic.scan.logs.QCLog;
import com.basic.scan.ui.ICaptureView;

import java.util.Hashtable;

/**
 * 解码处理（解码的核心）
 */
final class DecodeHandler extends Handler {

    private static final String TAG = DecodeHandler.class.getSimpleName();

    private final ICaptureView mCaptureView;
    private final MultiFormatReader mMultiFormatReader;

    DecodeHandler(ICaptureView captureView, Hashtable<DecodeHintType, Object> hints) {
        mMultiFormatReader = new MultiFormatReader();
        mMultiFormatReader.setHints(hints);
        mCaptureView = captureView;
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.decode) {
            decode((byte[]) message.obj, message.arg1, message.arg2);
        } else if (message.what == R.id.quit) {
            Looper.myLooper().quit();
        }
    }

    /**
     * 主要的解码方法
     *
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decode(byte[] data, int width, int height) {
        long start = System.currentTimeMillis();
        Result rawResult = null;

        //modify here
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotatedData[x * height + height - y - 1] = data[x + y * width];
            }
        }
        // Here we are swapping, that's the difference to #11
        int tmp = width;
        width = height;
        height = tmp;

        PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(rotatedData, width, height);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            //解码
            rawResult = mMultiFormatReader.decodeWithState(bitmap);
        } catch (ReaderException re) {
            // continue
        } finally {
            mMultiFormatReader.reset();
        }

        if (rawResult != null) {
            long end = System.currentTimeMillis();
            QCLog.dTag(TAG, "Found barcode (" + (end - start) + " ms):\n" + rawResult.toString());
            Message message = Message.obtain(mCaptureView.getCaptureHandler(), R.id.decode_succeeded, rawResult);
            Bundle bundle = new Bundle();
            bundle.putParcelable(DecodeThread.BARCODE_BITMAP, source.renderCroppedGreyscaleBitmap());
            message.setData(bundle);
            message.sendToTarget();
        } else {
            Message message = Message.obtain(mCaptureView.getCaptureHandler(), R.id.decode_failed);
            message.sendToTarget();
        }
    }

}
