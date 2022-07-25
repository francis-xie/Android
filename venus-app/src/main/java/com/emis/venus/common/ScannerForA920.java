package com.emis.venus.common;

import android.os.Handler;
import android.os.Message;
import com.pax.dal.IScanner;
import com.pax.dal.entity.EScannerType;

public class ScannerForA920 {
  private static ScannerForA920 cameraTester;
  private static EScannerType scannerType;
  private IScanner scanner;

  private ScannerForA920(EScannerType type) {
    ScannerForA920.scannerType = type;
    //logTrue(scannerType.name());
    scanner = GetObject.getDal().getScanner(scannerType);
  }

  public static ScannerForA920 getInstance(EScannerType type) {
    if (cameraTester == null || type != scannerType) {
      cameraTester = new ScannerForA920(type);
    }
    return cameraTester;
  }

  public void scan(final Handler handler, int timeout) {
    scanner.open();
    //logTrue("open");
    setTimeOut(timeout);
    scanner.setContinuousTimes(1);
    scanner.setContinuousInterval(1000);
    scanner.start(new IScanner.IScanListener() {

      @Override
      public void onRead(String arg0) {
        //logTrue("read:" + arg0);
        Message message = Message.obtain();
        message.what = 0;
        message.obj = arg0;
        handler.sendMessage(message);
      }

      @Override
      public void onFinish() {
        //logTrue("onFinish");
        close();
      }

      @Override
      public void onCancel() {
        //logTrue("onCancel");
        close();
      }
    });

    ////logTrue("start");
  }

  public void close() {
    scanner.close();
    // logTrue("close");
  }

  public void setTimeOut(int timeout) {
    scanner.setTimeOut(timeout);
    // logTrue("setTimeOut");
  }
}
