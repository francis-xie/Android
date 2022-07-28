package com.basic.zxing.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.util.Log;

import java.util.concurrent.RejectedExecutionException;

/**
 * 如果设备处于电池供电状态，在一段时间不活动后完成一个活动
 * https://www.runoob.com/android/android-broadcast-receivers.html
 */
public final class InactivityTimer {

  private static final String TAG = InactivityTimer.class.getSimpleName();

  private static final long INACTIVITY_DELAY_MS = 5 * 60 * 1000L;

  private final Activity activity;
  /**
   * 创建广播接收器
   * 广播接收器需要实现为BroadcastReceiver类的子类，并重写onReceive()方法来接收以Intent对象为参数的消息
   */
  private final BroadcastReceiver powerStatusReceiver;
  private boolean registered;
  // https://blog.csdn.net/liuhe688/article/details/6532519
  // 在Android中实现异步任务机制有两种方式，Handler和AsyncTask
  // 三种泛型类型分别代表“启动任务执行的输入参数”、“后台任务执行的进度”、“后台计算结果的类型”
  private AsyncTask<Object, Object, Object> inactivityTask;

  public InactivityTimer(Activity activity) {
    this.activity = activity;
    powerStatusReceiver = new PowerStatusReceiver();
    registered = false;
    onActivity();
  }

  public synchronized void onActivity() {
    cancel();
    inactivityTask = new InactivityAsyncTask();
    try {
      inactivityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); //执行一个异步任务
    } catch (RejectedExecutionException ree) {
      Log.w(TAG, "Couldn't schedule inactivity task; ignoring");
    }
  }

  public synchronized void onPause() {
    cancel();
    if (registered) {
      activity.unregisterReceiver(powerStatusReceiver);
      registered = false;
    } else {
      Log.w(TAG, "PowerStatusReceiver was never registered?");
    }
  }

  public synchronized void onResume() {
    if (registered) {
      Log.w(TAG, "PowerStatusReceiver was already registered?");
    } else {
      activity.registerReceiver(powerStatusReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
      registered = true;
    }
    onActivity();
  }

  private synchronized void cancel() {
    AsyncTask<?, ?, ?> task = inactivityTask;
    if (task != null) {
      task.cancel(true);
      inactivityTask = null;
    }
  }

  public void shutdown() {
    cancel();
  }

  private final class PowerStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      // 广播接收器需要实现为BroadcastReceiver类的子类，并重写onReceive()方法来接收以Intent对象为参数的消息
      if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
        // 0 indicates that we're on battery
        boolean onBatteryNow = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) <= 0;
        if (onBatteryNow) {
          InactivityTimer.this.onActivity();
        } else {
          InactivityTimer.this.cancel();
        }
      }
    }
  }

  private final class InactivityAsyncTask extends AsyncTask<Object, Object, Object> {
    @Override
    protected Object doInBackground(Object... objects) {
      // 用于执行较为费时的操作
      try {
        Thread.sleep(INACTIVITY_DELAY_MS);
        Log.i(TAG, "Finishing activity due to inactivity");
        activity.finish();
      } catch (InterruptedException e) {
        // continue without killing
      }
      return null;
    }
  }

}
