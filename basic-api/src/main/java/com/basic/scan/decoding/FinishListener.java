
package com.basic.scan.decoding;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * 简单的界面结束的监听
 */
public final class FinishListener
        implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, Runnable {

    private final Activity activityToFinish;

    public FinishListener(Activity activityToFinish) {
        this.activityToFinish = activityToFinish;
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        run();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        run();
    }

    @Override
    public void run() {
        activityToFinish.finish();
    }

}
