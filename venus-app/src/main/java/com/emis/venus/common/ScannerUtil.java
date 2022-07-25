package com.emis.venus.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.Nullable;
import com.emis.venus.activity.BaseActivity;
import com.pax.dal.entity.EScannerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static com.emis.venus.common.BaseUtil.SCAN_ACT_A920;
import static com.emis.venus.common.BaseUtil.SCAN_ACT_SUNMI;

public class ScannerUtil extends BaseActivity {

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = null;
    switch (Integer.valueOf(emisKeeper.getInstance().getsInfo("mchType", this))) {
      case 0: // sunmi
        /*intent = new Intent("com.summi.scan");
        intent.setPackage("com.sunmi.sunmiqrcodescanner");
        intent.putExtra("CURRENT_PPI", 0X0003);
        intent.putExtra("PLAY_SOUND", true);// 扫描完成声音提示  默认true
        intent.putExtra("PLAY_VIBRATE", false);
        intent.putExtra("IDENTIFY_INVERSE_QR_CODE", true);// 识别反色二维码，默认true
        intent.putExtra("IDENTIFY_MORE_CODE", false);// 识别画面中多个二维码，默认false
        intent.putExtra("IS_SHOW_SETTING", true);// 是否显示右上角设置按钮，默认true
        intent.putExtra("IS_SHOW_ALBUM", true);// 是否显示从相册选择图片按钮，默认true

        startActivityForResult(intent, SCAN_ACT_SUNMI);*/

        break;
      case 1: // A920
        ScannerForA920.getInstance(EScannerType.REAR).scan(handlerForA920, getTimeout());
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    Intent intent = new Intent();
    Bundle bundle = null;
    String P_NO = "";

    switch (requestCode) {
      case SCAN_ACT_SUNMI:
        if (resultCode == RESULT_OK) {
          bundle = data.getExtras();

          ArrayList result = (ArrayList) bundle.getSerializable("data");
          Iterator it = result.iterator();
          while (it.hasNext()) {
            HashMap hashMap = (HashMap) it.next();
            Log.i("sunmi type", (String) hashMap.get("TYPE"));//这个是扫码的类型
            Log.i("sunmi value", (String) hashMap.get("VALUE"));//这个是扫码的结果
            P_NO = (String) hashMap.get("VALUE");
          }
        }

        intent.putExtra("P_NO", P_NO);
        setResult(RESULT_OK, intent);
        finish();
        break;
      case SCAN_ACT_A920:
        bundle = data.getExtras();
        P_NO = bundle.getString("P_NO");

        intent.putExtra("P_NO", P_NO);
        setResult(RESULT_OK, intent);
        finish();
        break;
    }
  }

  private Handler handlerForA920 = new Handler() {//掃瞄商品結果
    public void handleMessage(android.os.Message msg) {
      //EditText txtView = (EditText) findViewById(R.id.edtBarRefund);
      switch (msg.what) {
        case 0:
          String code = msg.obj.toString();
          if ("".equals(code) || code == null) {
            break;
          }
          Intent re = new Intent();
          re.putExtra("P_NO", code);
          onActivityResult(SCAN_ACT_A920, RESULT_OK, re);
          break;
        default:
          break;
      }
    }

    ;
  };

  private int getTimeout() {
    return Integer.parseInt("1000000");
  }
}
