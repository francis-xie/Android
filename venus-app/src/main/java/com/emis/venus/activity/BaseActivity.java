package com.emis.venus.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.emis.venus.util.LoadingDialogUtil;
import com.emis.venus.util.emisLog;

public class BaseActivity extends AppCompatActivity {

  protected String sResponse_ = "";
  protected String sOrg_Domain;
  protected Context oContext_;
  public Dialog LoadingDialog;

  public Handler mHandler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);

      switch (msg.what) {
        case 1:
          LoadingDialogUtil.closeDialog(LoadingDialog);
          break;
      }
    }
  };

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  protected void hideBottomUIMenu() {
    //隐藏虚拟按键，并且全屏
    if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
      View v = this.getWindow().getDecorView();
      v.setSystemUiVisibility(View.GONE);
    } else if (Build.VERSION.SDK_INT >= 19) {
      //for new api versions.
      View decorView = getWindow().getDecorView();
      int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY /*| View.SYSTEM_UI_FLAG_FULLSCREEN*/;
      decorView.setSystemUiVisibility(uiOptions);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    emisLog.closeLog();
  }

}