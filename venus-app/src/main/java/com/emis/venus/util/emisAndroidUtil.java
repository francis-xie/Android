package com.emis.venus.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import okhttp3.MediaType;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class emisAndroidUtil {
  public static boolean _DEBUG_ = true;
  public static String sAppVersion_;  // Current App version
  public static String sNewAppVersion_;  // New App version
  public static String WWW_EMIS_URL = null;  // Init in MainActivity.java
  public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  public static final MediaType WWW_FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

  public static String getDate(String sDate) {
    if (sDate.indexOf("/") > 0) {
      String[] _aDates = sDate.split("/");
      //int _iYear = Integer.parseInt(_aDates[0])-1911;
      int _iMonth = Integer.parseInt(_aDates[1]);
      String _sMonth = (_iMonth < 10) ? "0" + _iMonth : "" + _iMonth;
      int _iDay = Integer.parseInt(_aDates[2]);
      String _sDay = (_iDay < 10) ? "0" + _iDay : "" + _iDay;
      sDate = _aDates[0] + "/" + _sMonth + "/" + _sDay;  // 西元日期, app_call.jsp裡會轉成ROC日期
    } else if (sDate.indexOf("-") > 0) {
      String[] _aDates = sDate.split("-");
      //int _iYear = Integer.parseInt(_aDates[0])-1911;
      int _iMonth = Integer.parseInt(_aDates[1]);
      String _sMonth = (_iMonth < 10) ? "0" + _iMonth : "" + _iMonth;
      int _iDay = Integer.parseInt(_aDates[2]);
      String _sDay = (_iDay < 10) ? "0" + _iDay : "" + _iDay;
      sDate = _aDates[0] + "/" + _sMonth + "/" + _sDay;  // 西元日期, app_call.jsp裡會轉成ROC日期
    } else if (sDate.length() == 8) {
      sDate = sDate.substring(0, 4) + "/" + sDate.substring(4, 6) + "/" + sDate.substring(6);  // 西元日期, app_call.jsp裡會轉成ROC日期
    }
    return sDate;
  }

  public static String setDateFormat(int year, int monthOfYear, int dayOfMonth) {
    int _iMM = monthOfYear + 1;
    String _sMM = (_iMM <= 9) ? "0" + _iMM : "" + _iMM;
    String _sDD = (dayOfMonth <= 9) ? "0" + dayOfMonth : "" + dayOfMonth;
    return String.valueOf(year) + "/" + _sMM + "/" + _sDD;
  }

  public static String getAppVersion(Activity activity) {
    String _sVersion = "1.0";
    try {
      PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
      _sVersion = pInfo.versionName;
      sAppVersion_ = _sVersion;
      //Toast.makeText(activity.getApplicationContext(), _sVersion, Toast.LENGTH_LONG).show();
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    return _sVersion;
  }

  public static String getAppName(Context oContext) {
    return oContext.getApplicationInfo().loadLabel(oContext.getPackageManager()).toString();
  }

  //遍曆設置字體
  public static void changeViewSize(ViewGroup viewGroup, int screenWidth, int screenHeight) {//傳入Activity頂層Layout,屏幕寬,屏幕高
    int adjustFontSize = adjustFontSize(screenWidth, screenHeight);
    for (int i = 0; i < viewGroup.getChildCount(); i++) {
      View v = viewGroup.getChildAt(i);
      if (v instanceof ViewGroup) {
        changeViewSize((ViewGroup) v, screenWidth, screenHeight);
      } else if (v instanceof Button) {//按鈕加大這個一定要放在TextView上面，因为Button也繼承了TextView
        ((Button) v).setTextSize(adjustFontSize + 2);
      } else if (v instanceof TextView) {
/*
        if(v.getId()== R.id.txtCaption){//頂部標題
          ( (TextView)v ).setTextSize(adjustFontSize+4);
        }else{
*/
        ((TextView) v).setTextSize(adjustFontSize);
//        }
      }
    }
  }


  //獲取字體大小
  public static int adjustFontSize(int screenWidth, int screenHeight) {
    screenWidth = screenWidth > screenHeight ? screenWidth : screenHeight;
    /**
     * 1. 在視圖的 onsizechanged裏獲取視圖寬度，一般情況下默認寬度是320，所以計算一個縮放比率
     rate = (float) w/320   w是實際寬度
     2.然後在設置字體尺寸時 paint.setTextSize((int)(8*rate));   8是在分辨率寬为320 下需要設置的字體大小
     實際字體大小 = 默認字體大小 x  rate
     */
    int rate = (int) (5 * (float) screenWidth / 320); //我自己測試這個倍數比較适合，當然你可以測試後再修改
    return rate < 15 ? 15 : rate; //字體太小也不好看的
  }

  public static void confirm(final Activity activity, String[] aMsgs) {
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        switch (which) {
          case DialogInterface.BUTTON_POSITIVE:
            // Yes button clicked
            Toast.makeText(activity, "Yes Clicked",
              Toast.LENGTH_LONG).show();
            break;

          case DialogInterface.BUTTON_NEGATIVE:
            // No button clicked
            // do nothing
            Toast.makeText(activity, "No Clicked",
              Toast.LENGTH_LONG).show();
            break;
        }
      }
    };

    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setMessage(aMsgs[0])
      .setPositiveButton(aMsgs[1], dialogClickListener)
      .setNegativeButton(aMsgs[2], dialogClickListener).show();
  }

  public static void info(final Activity activity, String[] aMsgs, final AlertDialogCallback<String> callback) {
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        switch (which) {
          case DialogInterface.BUTTON_POSITIVE:
            // Yes button clicked
            callback.doAlertDialogCallback("OK");
            break;

          case DialogInterface.BUTTON_NEGATIVE:
            // No button clicked
            // do nothing
            break;
        }
      }
    };

    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setMessage(aMsgs[0])
      .setPositiveButton(aMsgs[1], dialogClickListener).show();
  }

  public static void confirm(final Activity activity, String[] aMsgs, final AlertDialogCallback<String> callback) {
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        switch (which) {
          case DialogInterface.BUTTON_POSITIVE:
            // Yes button clicked
            callback.doAlertDialogCallback("OK");
            break;
          case DialogInterface.BUTTON_NEGATIVE:
            // No button clicked
            // do nothing
            break;
        }
      }
    };

    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setTitle(aMsgs[0])
      .setMessage(aMsgs[1])
      .setPositiveButton(aMsgs[2], dialogClickListener)
      .setNegativeButton(aMsgs[3], dialogClickListener)
      .show();
  }

  public final static int SCREEN_SIZE = 0;
  public final static int SCREEN_WIDTH = 1;
  public final static int SCREEN_HEIGHT = 2;
  public static int iScreenWidth_ = 768;
  public static int iScreenHeight_ = 1024;

  public static Configuration oConf_;
  public static Display oDisplay_ = null;
  public static String sButtonShape_;

  public static void setConfiguration(Configuration conf, Activity activity) {
    oConf_ = conf;
    oDisplay_ = activity.getWindowManager().getDefaultDisplay();
  }

  public static int getScreenSize() {
    return oConf_.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
  }

  public static int getScreenWidth() {
    return oDisplay_.getWidth();
    //return oConf_.screenWidthDp;
  }

  public static int getScreenHeight() {
    return oDisplay_.getHeight();
    //return oConf_.screenHeightDp;
  }

  public static float getDeviceWidth(Activity activity) {
    DisplayMetrics metrics = new DisplayMetrics();
    activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    int widthPixels = metrics.widthPixels;
    int heightPixels = metrics.heightPixels;
    float scaleFactor = metrics.density;
    float widthDp = widthPixels / scaleFactor;
    return widthDp;
  }

  public static String getNewVersion() {
    return sNewAppVersion_;
  }

  public static String getNewVersion(String sResult) {
    String _sUpdateInfo = "";
    int _iIndex = sResult.indexOf("APP_VER=");
    if (_iIndex > 0) {
      int _iIndex2 = sResult.indexOf(" ", _iIndex + 2);
      String _sVersion = sResult.substring(_iIndex + 8, _iIndex2);
      if (_sVersion.compareTo(sAppVersion_) > 0) {
        sNewAppVersion_ = _sVersion;
        _sUpdateInfo = "有新版本[" + _sVersion + "]可更新。\n";
      } else {
        sNewAppVersion_ = "";
      }
    }
    return sNewAppVersion_;
  }

  public static void hideKeyboard(Activity activity) {
    activity.getWindow().setSoftInputMode(
      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    View view = activity.getCurrentFocus();
  }

  public static void showKeyboard(Activity activity) {
    activity.getWindow().setSoftInputMode(
      WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    View view = activity.getCurrentFocus();
  }

  public static void hideKeyboard(View view) {
    //View view = activity.getCurrentFocus();
    InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
  }

  public static void showKeyboard(View view) {
    // View view = activity.getCurrentFocus();
    InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    inputManager.showSoftInput(view, 0);
  }

  private void noPasswordDialog(Context oContext, DialogInterface.OnClickListener listener, String sMsg) {
    AlertDialog.Builder builder = new AlertDialog.Builder(oContext);
    builder.setMessage(Html.fromHtml(sMsg))
      .setPositiveButton("關閉", listener).show();
  }

  public static int getDrawable(Context context, String name) {
    return context.getResources().getIdentifier(name,
      "drawable", context.getPackageName());
  }

  public static void makeButtonShape(Activity activity, int iResourceID, String sButtonShape) {
    LinearLayout linear = (LinearLayout) activity.findViewById(iResourceID);
    for (int i = 0; i < linear.getChildCount(); i++) {
      View child = linear.getChildAt(i);
      if (child instanceof Button && !(child instanceof CheckBox)) {  // CheckBox也屬於Button
        Button btn = (Button) child;
        int _iResourceID = emisAndroidUtil.getDrawable(activity, sButtonShape);
        btn.setBackground(activity.getResources().getDrawable(_iResourceID));
        btn.setTextColor(Color.WHITE);
        btn.setShadowLayer(5, 0, 0, Color.parseColor("#A8A8A8"));
      }
    }
  }

  public static void makeButtonTextSize(Activity activity, int iResourceID, float fSize) {
    LinearLayout linear = (LinearLayout) activity.findViewById(iResourceID);
    for (int i = 0; i < linear.getChildCount(); i++) {
      View child = linear.getChildAt(i);
      if (child instanceof Button && !(child instanceof CheckBox)) {  // CheckBox也屬於Button
        Button btn = (Button) child;
        btn.setTextSize(fSize);
        //Log.i("Util", "size=" + fSize);
      }
    }
  }

  public static void makeButtonShape(Activity activity, int iButtonID) {
    Button btnOK = (Button) activity.findViewById(iButtonID);
    if (emisAndroidUtil.sButtonShape_.endsWith("_red")) {
      btnOK.setTextColor(Color.WHITE);
      btnOK.setBackgroundColor(Color.parseColor("#B0212B"));
    } else if (emisAndroidUtil.sButtonShape_.endsWith("_blue")) {
      btnOK.setTextColor(Color.WHITE);
      btnOK.setBackgroundColor(Color.parseColor("#315B7A"));
    } else if (emisAndroidUtil.sButtonShape_.endsWith("_black")) {
      btnOK.setTextColor(Color.WHITE);
      btnOK.setBackgroundColor(Color.parseColor("#5A6F7D"));
    }
  }

  public static void doSettings(Context oContext, Class oClass) {
    Intent intent = new Intent();
    intent.setClass(oContext, oClass);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    oContext.startActivity(intent);
  }

  public static String getDeviceUsername(Context oContext) {
    AccountManager manager = AccountManager.get(oContext);
    Account[] accounts = manager.getAccountsByType("com.google");
    List<String> possibleEmails = new LinkedList<String>();

    for (Account account : accounts) {
      // account.name as an email address only for certain account.type values.
      possibleEmails.add(account.name);
    }

    if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
      String email = possibleEmails.get(0);
      String[] parts = email.split("@");

      if (parts.length > 1)
        return parts[0];
    }
    return null;
  }

  public static String getDeviceUUID() {
    return UUID.randomUUID().toString();
  }

  public static void showMessage(Context oContext, String sCaption, String sMsg, String[] aButtons) {
    AlertDialog.Builder builder = new AlertDialog.Builder(oContext);

    // 2. Chain together various setter methods to set the dialog characteristics
    builder.setMessage(sMsg).setTitle(sCaption); // 对话框内容、对话框标题
    /*
        builder.setPositiveButton("Launch missile", null);
        builder.setNeutralButton("Remind me later", null);
        builder.setNegativeButton("Cancel", null);
     */
    if (aButtons == null) {
      builder.setPositiveButton("知道了", null); //给对话框添加"Yes"按钮
    } else {
      if (aButtons.length == 1) {
        builder.setPositiveButton(aButtons[0], null); //给对话框添加"Yes"按钮
      } else if (aButtons.length == 2) {
        builder.setNegativeButton(aButtons[0], null); //对话框添加"No"按钮
        builder.setPositiveButton(aButtons[1], null); //给对话框添加"Yes"按钮
      } else if (aButtons.length >= 3) {
        builder.setNegativeButton(aButtons[0], null); //对话框添加"No"按钮
        builder.setNeutralButton(aButtons[1], null); //普通按钮
        builder.setPositiveButton(aButtons[2], null); //给对话框添加"Yes"按钮
      }
    }
    // 3. Get the AlertDialog from create()
    AlertDialog dialog = builder.create(); //创建对话框
    dialog.setIcon(android.R.drawable.ic_dialog_alert); //对话框图标
    dialog.show(); //显示对话框
  }

  public static boolean isEmpty(String sValue, TextView txtView) {
    if (sValue.equals("") || (sValue.startsWith("(") && sValue.endsWith(")"))) {
      txtView.setTextColor(0xFFFF0000);
      return true;
    }
    return false;
  }


  public static void noPasswordDialog(final Context context) {
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        switch (which) {
          case DialogInterface.BUTTON_POSITIVE:
            Activity activity = (Activity) context;
            activity.finish();
            break;
          case DialogInterface.BUTTON_NEGATIVE:
            break;
        }
      }
    };
    String _sMsg = "本作業需要設定密碼。\n" + "請先執行設定作業。";

    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setMessage(Html.fromHtml(_sMsg))
      .setPositiveButton("關閉", dialogClickListener).show();
  }

}
