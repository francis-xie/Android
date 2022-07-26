package com.basic.zxing.util;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * 在一些情况下，简单的监听器用于退出应用程序。
 */
public final class FinishListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {

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

  private void run() {
    activityToFinish.finish();
  }

}
