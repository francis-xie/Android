package com.emis.venus.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.emis.venus.R;
import com.emis.venus.db.emisDb;
import com.emis.venus.db.emisProp;
import com.emis.venus.db.emisSQLiteWrapper;
import com.emis.venus.entity.Entity;
import com.emis.venus.report.VenusBillReport;
import com.emis.venus.synPosData.emisBMSynDataImpl;
import com.emis.venus.common.*;
import com.emis.venus.util.*;
import com.emis.venus.util.log4j.LogKit;
import com.freak.printtool.hardware.module.wifi.printerutil.SocketPrint;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.emis.venus.util.DialogUtil.createDialog;

public class LoginActivity extends BaseActivity {

  @BindView(R.id.edtUserID)
  EditText edtUserID_;
  @BindView(R.id.edtPassword)
  EditText edtPassword_;
  @BindView(R.id.edtStore)
  EditText edtStore_;
  @BindView(R.id.edtCashRegister)
  EditText edtCashRegister_;
  @BindView(R.id.txtMsg)
  TextView txtMsg_;
  @BindView(R.id.btnLogin)
  Button btnLogin;
  @BindView(R.id.btnGetData)
  Button btnGetData;
  @BindView(R.id.btnReset)
  Button btnClear;
  @BindView(R.id.txtDate)
  TextView txtDate;
  @BindView(R.id.txtDay)
  TextView txtDay;
  @BindView(R.id.keyboard)
  KeyboardView keyboardView;

  private final int SCAN_SUNMI = 1001;

  private String _sAppVersion, _sTitle;
  private String sUserID_, sS_NO_, sS_NAME_, sID_NO_;
  private int tryLoginNidinTimes, tryLoginNidinMQTTTimes, tryRegisterTimes;

  private KeyboardHelper helper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login_orange);

    ButterKnife.bind(this);

    String sTaxRate;
    String _sErrMsg = "";
    _sAppVersion = "V" + emisAndroidUtil.getAppVersion(this);
    _sTitle = emisAndroidUtil.getAppName(getApplicationContext());

    oContext_ = LoginActivity.this;

    setDefaultData();
    createContent();

    // System.out.println("Build.BRAND: " + Build.BRAND);
    // System.out.println("Build.DEVICE: " + Build.DEVICE);
    // System.out.println("Build.ID: " + Build.ID);
    // System.out.println("Build.SERIAL: " + Build.SERIAL);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      btnClear.setStateListAnimator(null);
      btnGetData.setStateListAnimator(null);
      btnLogin.setStateListAnimator(null);
    }

    if (true) {
      // btnDebug.setVisibility(View.INVISIBLE);
    }

    if (_sErrMsg.length() == 0) {
      // 當收銀機編號空白時，進入註冊畫面。
      /*if (sID_NO_.equals("")) {
        // registerProcess("https://freepos.retail.com.tw/venus_uat", "root@venus_uat", "7100");
        // registerProcess("http://10.2.5.222:8080/venus_tw", "", "123");
        // registerProcess("http://10.7.1.46:8081/venus_tw", "", "3259");
        registerProcess("", "", "");
      } else {
        if (sUserID_.equals("")) {
          edtUserID_.requestFocus();
        } else {
          edtPassword_.requestFocus();
        }
      }*/
    } else {
      emisAndroidUtil.showMessage(oContext_, getString(R.string.login_empty), _sErrMsg, null);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    tryLoginNidinTimes = 0;
    tryLoginNidinMQTTTimes = 0;
    tryRegisterTimes = 0;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case SCAN_SUNMI:
        Bundle bundle = null;
        ArrayList result;
        Iterator it;
        HashMap hashMap;

        String[] values;
        String registerLocation = "", orgDomain = "", registerCode = "";

        Pattern pattern = Pattern.compile("^http.*,.*,[1-9]{0,4}");
        Matcher matcher;

        if (resultCode == RESULT_OK) {
          bundle = data.getExtras();
          result = (ArrayList) bundle.getSerializable("data");
          it = result.iterator();
          while (it.hasNext()) {
            hashMap = (HashMap) it.next();

            Log.i("sunmi type", (String) hashMap.get("TYPE"));    // 扫码类型
            Log.i("sunmi value", (String) hashMap.get("VALUE"));  // 扫码结果

            matcher = pattern.matcher((String) hashMap.get("VALUE"));
            if (matcher.find()) {
              values = ((String) hashMap.get("VALUE")).split(",");

              registerLocation = values[0];
              orgDomain = values[1];
              registerCode = values[2];

              break;
            }
          }
        }

        registerProcess(registerLocation, orgDomain, registerCode);
        break;
    }
  }

  public void btnLoginClick(View view) {
    try {
      Entity _oEmisPropEntity = new Entity("emisprop");
      if (_oEmisPropEntity.load(null)) {
        _oEmisPropEntity.first();
        do {
          LogKit.info(_oEmisPropEntity.getString("VALUE"));
        } while (_oEmisPropEntity.next());
      }
      _oEmisPropEntity.cleanData();
      HashMap<String, Object> PK_Value = new HashMap<>();
      PK_Value.put("NAME", "test11");
      if (_oEmisPropEntity.load(PK_Value)) {
        _oEmisPropEntity.first();
        do {
          LogKit.info(_oEmisPropEntity.getString("VALUE"));
        } while (_oEmisPropEntity.next());
      }
      _oEmisPropEntity.executeUpdate(null, "delete from emisprop where NAME >= 'test11' and NAME <= 'test19'");
      _oEmisPropEntity.executeUpdate(null, "insert into emisprop (NAME,VALUE) VALUES('test11','t1')");
      _oEmisPropEntity.executeUpdate(null, "insert into emisprop (NAME,VALUE) VALUES('test12','t2')");
      _oEmisPropEntity.executeUpdate(null, "insert into emisprop (NAME,VALUE) VALUES('test13','t3')");
      _oEmisPropEntity.executeUpdate(null, "insert into emisprop (NAME,VALUE) VALUES('test14','t4')");
      _oEmisPropEntity.executeUpdate(null, "update emisprop set VALUE = 't11' where NAME = 'test11'");
      _oEmisPropEntity.executeUpdate(null, "delete from emisprop where NAME = 'test14'");
      _oEmisPropEntity.loadBySql("select * from emisprop where NAME >= 'test11' and NAME <= 'test19'"); //$NON-NLS-1$
      if (!emisUtil.isEmpty(_oEmisPropEntity)) {
        _oEmisPropEntity.first();
        do {
          _oEmisPropEntity.setString("VALUE", _oEmisPropEntity.getString("VALUE") + "1");
          LogKit.info(_oEmisPropEntity.getString("VALUE"));
        } while (_oEmisPropEntity.next());
        emisSQLiteWrapper db = emisDb.getInstance();
        _oEmisPropEntity.loadBySql(db, "select * from emisprop where NAME >= 'test11' and NAME <= 'test19'"); //$NON-NLS-1$
        try {
          db.beginTransaction();
          if (_oEmisPropEntity.updateAll(db)) {
            GHelper.showInfoMsgBox(oContext_, "批量修改成功");
          }
          if (_oEmisPropEntity.deleteAll(db)) {
            GHelper.showErrorMsgBox(oContext_, "删除成功");
          }
          _oEmisPropEntity.createNewRow();
          _oEmisPropEntity.setString("NAME", "test15");
          _oEmisPropEntity.setString("VALUE", "t5");
          _oEmisPropEntity.createNewRow();
          _oEmisPropEntity.setString("NAME", "test16");
          _oEmisPropEntity.setString("VALUE", "t6");
          if (_oEmisPropEntity.insertAll(db)) {
            GHelper.showOkCancelMsgBox(oContext_, "批量新增成功");
          }
          _oEmisPropEntity.createNewRow();
          _oEmisPropEntity.setString("NAME", "test17");
          _oEmisPropEntity.setString("VALUE", "t7");
          if (_oEmisPropEntity.insert(null)) {
            GHelper.showYesNoCancelMsgBox(oContext_, "新增成功");
          }
          _oEmisPropEntity.first();
          _oEmisPropEntity.setString("VALUE", "t1");
          if (_oEmisPropEntity.update(null)) {
            GHelper.showYesNoMsgBox(oContext_, "修改成功");
          }
          db.setTransactionSuccessful();
        } catch (Exception e) {
          LogKit.error(e, e);
        } finally {
          db.endTransaction();
        }
      }
    } catch (Exception e) {
      LogKit.error(e, e);
    }

    boolean booCheckError = false;

    Message message = new Message();
    Object _oFocused = null;
    String _sErrMsg = "";
    String _sPassword = edtPassword_.getText().toString();
    String Init = BaseUtil.getFromProp("INIT");
    String nextInvoiceNo = "";
    String checkBasedata = BaseUtil.isBasedataEmpty();
    String month = emisUtil.todayDateAD().substring(4, 6);
    String day = emisUtil.todayDateAD().substring(6);

    // AidlUtil t = new AidlUtil(this.getApplicationContext());
    emisLog.addLog("===== Start Login ======");

    sUserID_ = edtUserID_.getText().toString();

    /*if (!booCheckError) {
      if ("".equals(Init) || "N".equals(Init) || Init == null) {
        booCheckError = true;

        Toast.makeText(LoginActivity.this, "初始資料未獲取", Toast.LENGTH_SHORT).show();

        emisLog.addLog("初始資料未獲取");
        emisLog.addLog("======Login END======");
        emisLog.closeLog();
      }
    }*/

//chkLogin(booCheckError,checkBasedata);
    updateData(booCheckError, checkBasedata);
    // sendLogin(_sPassword);
  }

  public void updateData(boolean booCheckError, String checkBasedata) {
    Callback callback = new Callback() {

      @Override
      public void onFailure(Call call, IOException e) {
        LoginActivity.this.runOnUiThread(new Runnable() {

          @Override
          public void run() {
            // LoadingDialogUtil.closeDialog(LoadingDialog);

            Message message = new Message();
            message.what = 1;
            mHandler.dispatchMessage(message);

            emisAndroidUtil.showMessage(oContext_, "更新基本資料失敗", e.getMessage(), null);

            emisLog.addLog("更新基本資料發生錯誤，錯誤訊息：", e);
            emisLog.addLog("===== GetBasedata END =====");
            emisLog.closeLog();
            chkLogin(booCheckError, checkBasedata);
          }
        });
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        Message message = new Message();

        final String responseBody = response.body().string();

        System.out.println("GetBasedata response:" + responseBody);

        if (response.isSuccessful()) {
          LoginActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
              Message message = new Message();

              Gson gson = new Gson();
              Map<String, Object> map = (Map<String, Object>) gson.fromJson(responseBody, (new HashMap<String, Object>()).getClass());
              String _sCode = (String) map.get("code");
              String _sMsg = (String) map.get("msg");

              if (_sCode.equals("0")) {
                saveBasedata(map);
                message.what = 1;
                mHandler.dispatchMessage(message);
                Toast.makeText(LoginActivity.this, "更新基本資料完成", Toast.LENGTH_SHORT).show();
                emisLog.addLog("===== GetBasedata END =====");
                emisLog.closeLog();
                chkLogin(booCheckError, checkBasedata);
              } else if (_sCode.equals("000")) {
                saveBasedata(map);
                message.what = 1;
                mHandler.dispatchMessage(message);
                Toast.makeText(LoginActivity.this, "更新基本資料完成", Toast.LENGTH_SHORT).show();
                emisLog.addLog("===== GetBasedata END =====");
                emisLog.closeLog();
                chkLogin(booCheckError, checkBasedata);
              } else if (_sCode.equals("001")) {
                message.what = 1;
                mHandler.dispatchMessage(message);
                Toast.makeText(LoginActivity.this, "該機台已超過到期日", Toast.LENGTH_SHORT).show();
                emisLog.addLog("===== GetBasedata END =====");
                emisLog.closeLog();
                chkLogin(booCheckError, checkBasedata);
              } else if (_sCode.equals("002")) {
                message.what = 1;
                mHandler.dispatchMessage(message);
                Toast.makeText(LoginActivity.this, "該機台已停用", Toast.LENGTH_SHORT).show();
                emisLog.addLog("===== GetBasedata END =====");
                emisLog.closeLog();
                chkLogin(booCheckError, checkBasedata);
              } else if (_sCode.equals("003")) {
                message.what = 1;
                mHandler.dispatchMessage(message);
                Toast.makeText(LoginActivity.this, "該機台尚未啟用", Toast.LENGTH_SHORT).show();
                emisLog.addLog("===== GetBasedata END =====");
                emisLog.closeLog();
                chkLogin(booCheckError, checkBasedata);
              } else {
                message.what = 1;
                mHandler.dispatchMessage(message);
                Toast.makeText(LoginActivity.this, "更新基本資料時發生錯誤，錯誤代碼：" + _sCode + "，訊息：" + _sMsg, Toast.LENGTH_SHORT).show();
                System.out.println("更新基本資料時發生錯誤，錯誤代碼：" + _sCode + "，訊息：" + _sMsg);

                emisLog.addLog("更新基本資料時發生錯誤，錯誤代碼：" + response.code() + "，訊息：" + response.message());
                emisLog.addLog("===== GetBasedata END =====");
                emisLog.closeLog();
                chkLogin(booCheckError, checkBasedata);
              }
            }
          });
        } else {
          Toast.makeText(LoginActivity.this, "更新基本資料時發生錯誤，錯誤代碼：" + response.code() + "，訊息：" + response.message(), Toast.LENGTH_SHORT).show();

          System.out.println("更新基本資料時發生錯誤，錯誤代碼：" + response.code() + "，訊息：" + response.message());

          emisLog.addLog("更新基本資料時發生錯誤，錯誤代碼：" + response.code() + "，訊息：" + response.message());
          emisLog.addLog("===== GetBasedata END =====");
          emisLog.closeLog();

          message.what = 1;
          mHandler.dispatchMessage(message);
          chkLogin(booCheckError, checkBasedata);
        }
      }
    };
    getBasedata(sUserID_, sID_NO_, sS_NO_, sOrg_Domain, emisAndroidUtil.WWW_EMIS_URL, callback);
  }

  public void chkLogin(boolean booCheckError, String checkBasedata) {
    Object _oFocused = null;
    String _sErrMsg = "";
    String _sPassword = edtPassword_.getText().toString();
    Message message = new Message();
    if (!booCheckError) {
      if (!checkBasedata.contains("success")) {
        booCheckError = true;

        Toast.makeText(LoginActivity.this, checkBasedata, Toast.LENGTH_SHORT).show();
      }
    }

    /*if (!booCheckError) {
      if (!BaseUtil.isOverDate()) {
        booCheckError = true;
        Toast.makeText(LoginActivity.this, "超過七天未與後臺連綫，請重新獲取後臺資料", Toast.LENGTH_SHORT).show();
      }
    }*/

    if (!booCheckError) {
      if (sUserID_ == null || "".equals(sUserID_)) {
        _sErrMsg = "請輸入用戶名";
        _oFocused = edtUserID_;
      }
      if (_sPassword == null || "".equals(_sPassword)) {
        _sErrMsg += "請輸入密碼";
        if (_oFocused == null) _oFocused = edtPassword_;
      }

      if (_sErrMsg.length() > 0) {
        booCheckError = true;

        ((EditText) _oFocused).requestFocus();
        emisAndroidUtil.showMessage(oContext_, getString(R.string.login_empty), _sErrMsg, null);
      }
    }

    if (!booCheckError) {
      if (BaseUtil.checkUser(sUserID_, _sPassword)) {

//        emisKeeper.getInstance().setsInfo("USERID", sUserID_, this);
        emisKeeper.getInstance().setLogin(true);
        emisKeeper.getInstance().setClear(true);

        BaseUtil.setStoreNo(sS_NO_);
        BaseUtil.setCashRegisterID(sID_NO_);

        if (!BaseUtil.isYClear(BaseUtil.getOldDate(0))) emisKeeper.getInstance().setClear(false);
      } else {
        booCheckError = true;

        Toast.makeText(LoginActivity.this, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
      }
    }

    if (!booCheckError) {
      emisLog.addLog("===== Login Nidin Start =====");

      if (emisKeeper.getInstance().getbInfo("ActiveNidin", this) == true) {

        LoadingDialog = LoadingDialogUtil.createLoadingDialog(LoginActivity.this, "登入系統中...");

        loginNidin();
      } else {
        message.what = 1;
        mHandler.dispatchMessage(message);

        setResult(RESULT_OK, getIntent());
        finish();
      }
    }
  }

  public void btnGetDataClick(View view) {
    Callback callback = new Callback() {

      @Override
      public void onFailure(Call call, IOException e) {
        LoginActivity.this.runOnUiThread(new Runnable() {

          @Override
          public void run() {
            // LoadingDialogUtil.closeDialog(LoadingDialog);
            Message message = new Message();
            message.what = 1;
            mHandler.dispatchMessage(message);

            emisAndroidUtil.showMessage(oContext_, "取得基本資料失敗", e.getMessage(), null);

            emisLog.addLog("取得基本資料發生錯誤，錯誤訊息：", e);
            emisLog.addLog("===== GetBasedata END =====");
            emisLog.closeLog();
          }
        });
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        Message message = new Message();

        final String responseBody = response.body().string();

        System.out.println("GetBasedata response:" + responseBody);

        if (response.isSuccessful()) {
          LoginActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
              Message message = new Message();

              Gson gson = new Gson();
              Map<String, Object> map = (Map<String, Object>) gson.fromJson(responseBody, (new HashMap<String, Object>()).getClass());
              String _sCode = (String) map.get("code");
              String _sMsg = (String) map.get("msg");

              if (_sCode.equals("000")) {
                saveBasedata(map);

                message.what = 1;
                mHandler.dispatchMessage(message);

                Toast.makeText(LoginActivity.this, "取得基本資料完成", Toast.LENGTH_SHORT).show();

                emisLog.addLog("===== GetBasedata END =====");
                emisLog.closeLog();
              } else if (_sCode.equals("001")) {

                message.what = 1;
                mHandler.dispatchMessage(message);

                Toast.makeText(LoginActivity.this, "該機台已超過到期日", Toast.LENGTH_SHORT).show();

                emisLog.addLog("===== GetBasedata END =====");
                emisLog.closeLog();
              } else if (_sCode.equals("002")) {
                message.what = 1;
                mHandler.dispatchMessage(message);

                Toast.makeText(LoginActivity.this, "該機台已停用", Toast.LENGTH_SHORT).show();

                emisLog.addLog("===== GetBasedata END =====");
                emisLog.closeLog();
              } else if (_sCode.equals("003")) {
                message.what = 1;
                mHandler.dispatchMessage(message);

                Toast.makeText(LoginActivity.this, "該機台尚未啟用", Toast.LENGTH_SHORT).show();

                emisLog.addLog("===== GetBasedata END =====");
                emisLog.closeLog();
              } else {
                message.what = 1;
                mHandler.dispatchMessage(message);

                Toast.makeText(LoginActivity.this, "取得基本資料時發生錯誤，錯誤代碼：" + _sCode + "，訊息：" + _sMsg, Toast.LENGTH_SHORT).show();

                System.out.println("取得基本資料時發生錯誤，錯誤代碼：" + _sCode + "，訊息：" + _sMsg);

                emisLog.addLog("取得基本資料時發生錯誤，錯誤代碼：" + response.code() + "，訊息：" + response.message());
                emisLog.addLog("===== GetBasedata END =====");
                emisLog.closeLog();
              }
            }
          });
        } else {
          Toast.makeText(LoginActivity.this, "取得基本資料時發生錯誤，錯誤代碼：" + response.code() + "，訊息：" + response.message(), Toast.LENGTH_SHORT).show();

          System.out.println("取得基本資料時發生錯誤，錯誤代碼：" + response.code() + "，訊息：" + response.message());

          emisLog.addLog("取得基本資料時發生錯誤，錯誤代碼：" + response.code() + "，訊息：" + response.message());
          emisLog.addLog("===== GetBasedata END =====");
          emisLog.closeLog();

          message.what = 1;
          mHandler.dispatchMessage(message);
        }
      }
    };

    getBasedata(sUserID_, sID_NO_, sS_NO_, sOrg_Domain, emisAndroidUtil.WWW_EMIS_URL, callback);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  public void btnResetClick(View view) {
    com.freak.printtool.hardware.utils.ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(new Runnable() {
      @Override
      public void run() {
        // 结账小票
        VenusBillReport ticket = null;
        try {
          ticket = new VenusBillReport(LoginActivity.this);
          ticket.setSL_KEY(emisUtil.getSYSDATETIME()); //$NON-NLS-1$
          ticket.setPrintBuf(null);
          ticket.print();
        } catch (Exception e) {
          LogKit.error("print ticket Error:", e); //$NON-NLS-1$
        } finally {
          // 一定要关闭
          if (ticket != null) {
            ticket.close();
          }
        }
      }
    });
    edtUserID_.setText("");
    edtPassword_.setText("");

    edtUserID_.requestFocus();
  }

  public void btnCleanDataClick(View view) {
    emisSQLiteWrapper db = emisDb.getInstance();
    db.execSQL("delete from SALE_H");
    db.execSQL("delete from SALE_D");
    db.execSQL("delete from SALE_P");
    db.execSQL("delete from SALE_DIS");
  }

  public void sendLogin(String pw) {
    String _sAppID = emisAndroidUtil.getDeviceUUID();

    Intent intent = new Intent();
    intent.setClass(LoginActivity.this, MainActivity.class);
    try {
      final JSONObject jsonObj = new JSONObject();
      //final ExecutorService service = Executors.newSingleThreadExecutor();

      String encypted = null;
      try {
        jsonObj.put("Q1", sUserID_);
        jsonObj.put("Q2", pw);
        jsonObj.put("ID_NO", sID_NO_);
        jsonObj.put("K", _sAppID);
        emisLog.addLog(jsonObj.toString());
        encypted = AES.encrypt(jsonObj.toString());
        LoadingDialog = LoadingDialogUtil.createLoadingDialog(LoginActivity.this, "登入中...");

        final OkHttpClient okClient = new OkHttpClient()
          .newBuilder()
          .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
          .hostnameVerifier(SSLSocketClient.getHostnameVerifier()).build();
        final String _sURL = emisAndroidUtil.WWW_EMIS_URL + "/ws/sunmi/v1/login";
        System.out.println("@@LoginActivity.java.btnLoginClick #182:URL=" + _sURL);

        RequestBody body = RequestBody.create(emisAndroidUtil.JSON, encypted);
        Request request = new Request.Builder().addHeader("sUser", sOrg_Domain).url(_sURL).post(body).build();
        okClient.newCall(request).enqueue(new Callback() {
          @Override
          public void onFailure(Call call, IOException e) {
            LoginActivity.this.runOnUiThread(new Runnable() {
              @Override
              public void run() {
                emisAndroidUtil.showMessage(oContext_, "登入失敗", "登入時發生錯誤：" + e.getMessage(), null);
                emisLog.addLog("登入時發生錯誤：", e);
                emisLog.addLog("======Login END======");
                emisLog.closeLog();
                LoadingDialogUtil.closeDialog(LoadingDialog);
              }
            });
          }

          @Override
          public void onResponse(Call call, Response response) throws IOException {
            final String _sResponse = response.body().string();
            System.out.println("@@LoginActivity.java.onResponse #130:" + _sResponse);
            if (response.isSuccessful()) {
              // Parse and get server response text data.
              LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  Message message = new Message();
                  message.what = 1;
                  if (!response.isSuccessful()) {
                    mHandler.dispatchMessage(message);
                    txtMsg_.setText("登入失敗。回應碼:" + response.code() + " 訊息:" + response.message());
                    emisLog.addLog("登入時發生錯誤：" + response.message());
                    emisLog.addLog("======Login END======");
                    emisLog.closeLog();
                  } else {
                    txtMsg_.setText(_sResponse);
                    //SharedPreferences _oPersonalSettings = getSharedPreferences("personal", 0);
                    //_oPersonalSettings.edit().putBoolean("loggedin",true).apply();
                    Gson gson = new Gson();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = (Map<String, Object>) gson.fromJson(_sResponse, map.getClass());
                    String _sCode = (String) map.get("code");
                    String _sMsg = (String) map.get("msg");
                    if (!_sCode.equals("000")) {
                      Toast.makeText(LoginActivity.this, _sMsg, Toast.LENGTH_SHORT).show();
                      mHandler.dispatchMessage(message);
                      return;
                    }
                    emisLog.addLog("Login Success");
                    emisLog.addLog("======Login END======");
                    emisLog.closeLog();
                    sUserID_ = edtUserID_.getText().toString();
                    sUserID_ = (String) map.get("op_no");

                    BaseUtil.setStoreNo(sS_NO_);
                    BaseUtil.setCashRegisterID(sID_NO_);

                    String d = BaseUtil.getClearLastDate();
                    String yesterday = BaseUtil.getOldDate(0);
                    String t = BaseUtil.getClearLastTime();
                    mHandler.dispatchMessage(message);
                    if (!BaseUtil.isYClear(yesterday)) {
                      emisKeeper.getInstance().setClear(false);
                      Intent intent1 = getIntent();
                      setResult(RESULT_OK, intent1);
                      finish();
                    } else {
                      Intent intent1 = getIntent();
                      setResult(RESULT_OK, intent1);
                      finish();
                    }
                  }
                }
              });
            }
          }
        });
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void createContent() {
    this.setTitle(_sTitle + " " + _sAppVersion);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_login, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.user_p:
        //startActivity((new Intent()).setClass(LoginActivity.this, SettingsActivity.class));
        startActivity((new Intent()).setClass(LoginActivity.this, SQLEditorActivity.class));
        //Toast.makeText(this, "你點選了“使用者”按鍵！", Toast.LENGTH_SHORT).show();
        return true;
      default:
        return true;
    }
  }

  private void loginNidin() {
    Message message = new Message();

    final OkHttpClient okClient = new OkHttpClient().newBuilder().sslSocketFactory(SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(SSLSocketClient.getHostnameVerifier()).build();

    RequestBody body;
    Request request;
    JSONObject jsonObject;
    jsonObject = new JSONObject();

    try {
      jsonObject.put("S_NO", sS_NO_);

      emisLog.addLog("loginNidin:" + jsonObject.toString());
      System.out.println("loginNidin:" + jsonObject.toString());

      // body = RequestBody.create(emisAndroidUtil.JSON, AES.encrypt(jsonObject.toString()));
      body = RequestBody.create(emisAndroidUtil.JSON, jsonObject.toString());
      request = new Request.Builder().addHeader("sUser", sOrg_Domain).url(emisAndroidUtil.WWW_EMIS_URL + "/ws/nidin/v1/login").post(body).build();

      if (tryLoginNidinTimes >= 3) {
        message.what = 1;
        mHandler.dispatchMessage(message);

        Toast.makeText(LoginActivity.this, "無法登入你訂系統！！", Toast.LENGTH_SHORT).show();

        tryLoginNidinTimes = 0;

        emisLog.addLog("===== Login Nidin END =====");
        emisLog.closeLog();

        setResult(RESULT_OK, getIntent());
        finish();
      } else {
        okClient.newCall(request).enqueue(new Callback() {

          @Override
          public void onFailure(Call call, IOException e) {
            LoginActivity.this.runOnUiThread(new Runnable() {

              @Override
              public void run() {
                System.out.println("登入你訂系統失敗。錯誤訊息：" + e.getLocalizedMessage());

                emisLog.addLog("登入你訂系統失敗。錯誤訊息：", e);

                tryLoginNidinTimes++;

                loginNidin();
              }
            });
          }

          @Override
          public void onResponse(Call call, Response response) throws IOException {

            if (response.isSuccessful()) {
              String logResponse = response.body().string();

              // System.out.println("Response:" + logResponse);

              LoginActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                  try {
                    Map<String, Object> map = (Map<String, Object>) (new Gson()).fromJson(logResponse, (new HashMap<String, Object>()).getClass());

                    String code = (String) map.get("code");
                    String msg = (String) map.get("msg");

                    if (code.equals("200")) {
                      emisLog.addLog("===== Login Nidin END =====");
                      emisLog.closeLog();

                      loginNidinMQTT();
                    } else {
                      emisLog.addLog("登入你訂系統失敗。錯誤訊息：" + msg);

                      tryLoginNidinTimes++;

                      loginNidin();
                    }
                  } catch (Exception e) {
                    emisLog.addLog("登入你訂系統失敗。錯誤訊息：", e);

                    tryLoginNidinTimes++;

                    loginNidin();
                  }
                }
              });
            } else {
              Looper.prepare();

              System.out.println("連接網路失敗。回應碼：" + response.code() + "；錯誤訊息：" + response.message());

              emisLog.addLog("連接網路失敗。回應碼：" + response.code() + "；錯誤訊息：" + response.message());

              Toast.makeText(LoginActivity.this, "連接網路失敗。回應碼：" + response.code() + "；錯誤訊息：" + response.message(), Toast.LENGTH_SHORT).show();

              tryLoginNidinTimes++;

              loginNidin();

              Looper.loop();
            }
          }
        });
      }
    } catch (Exception e) {
      message.what = 1;
      mHandler.dispatchMessage(message);

      Toast.makeText(LoginActivity.this, "登入你訂系統過程有誤！錯誤訊息：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

      emisLog.addLog("登入你訂系統過程有誤！錯誤訊息：", e);

      setResult(RESULT_OK, getIntent());
      finish();
    }
  }

  private void loginNidinMQTT() {
    Message message = new Message();

    final OkHttpClient okClient = new OkHttpClient().newBuilder().sslSocketFactory(SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(SSLSocketClient.getHostnameVerifier()).build();

    RequestBody body;
    Request request;
    JSONObject jsonObject;

    jsonObject = new JSONObject();

    try {
      jsonObject.put("S_NO", sS_NO_);

      emisLog.addLog("loginNidinMQTT:" + jsonObject.toString());
      System.out.println("loginNidinMQTT:" + jsonObject.toString());

      // body = RequestBody.create(emisAndroidUtil.JSON, AES.encrypt(jsonObject.toString()));
      body = RequestBody.create(emisAndroidUtil.JSON, jsonObject.toString());
      request = new Request.Builder().addHeader("sUser", sOrg_Domain).url(emisAndroidUtil.WWW_EMIS_URL + "/ws/nidin/v1/nidi_mqtt_login").post(body).build();

      // System.out.println("loginNidinMQTT URL:" + emisAndroidUtil.WWW_EMIS_URL + "/ws/nidin/v1/nidi_mqtt_login");

      if (tryLoginNidinMQTTTimes >= 3) {

        message.what = 1;
        mHandler.dispatchMessage(message);

        Toast.makeText(LoginActivity.this, "無法登入你訂MQTT系統！！", Toast.LENGTH_SHORT).show();

        tryLoginNidinMQTTTimes = 0;

        emisLog.addLog("===== Login Nidin MQTT END =====");
        emisLog.closeLog();

        setResult(RESULT_OK, getIntent());
        finish();
      } else {

        okClient.newCall(request).enqueue(new Callback() {

          @Override
          public void onFailure(Call call, IOException e) {

            LoginActivity.this.runOnUiThread(new Runnable() {

              @Override
              public void run() {

                System.out.println("登入你訂MQTT系統失敗。錯誤訊息：" + e.getLocalizedMessage());

                emisLog.addLog("登入你訂MQTT系統失敗。錯誤訊息：", e);

                tryLoginNidinTimes++;

                loginNidinMQTT();
              }
            });
          }

          @Override
          public void onResponse(Call call, Response response) throws IOException {

            System.out.println("Login Nidin MQTT Response Code:" + response.code());

            if (response.isSuccessful()) {

              String responseBody = response.body().string();

              LoginActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                  try {

                    // Map<String, Object> map = (Map<String, Object>) (new Gson()).fromJson(responseBody, (new HashMap<String, Object>()).getClass());

                    // String code = (String) map.get("code");
                    // String msg = (String) map.get("msg");

                    // if (code.equals("200") || code.equals("204")) {
                    if ((response.code() == 200) || (response.code() == 204)) {

                      emisLog.addLog("===== Login Nidin MQTT END =====");
                      emisLog.closeLog();

                      System.out.println("已登入你訂MQTT系統。");

                      message.what = 1;
                      mHandler.dispatchMessage(message);

                      setResult(RESULT_OK, getIntent());
                      finish();
                    } else {

                      // System.out.println("登入你訂MQTT系統失敗。錯誤訊息：" + msg);

                      // emisLog.addLog("登入你訂MQTT系統失敗。錯誤訊息：" + msg);

                      tryLoginNidinMQTTTimes++;

                      loginNidinMQTT();
                    }
                  } catch (Exception e) {

                    message.what = 1;
                    mHandler.dispatchMessage(message);

                    System.out.println("登入你訂MQTT系統失敗。錯誤訊息：" + e.getLocalizedMessage());

                    emisLog.addLog("登入你訂MQTT系統失敗。錯誤訊息：", e);

                    tryLoginNidinMQTTTimes++;

                    loginNidinMQTT();
                  }
                }
              });
            } else {

              Looper.prepare();

              System.out.println("連接網路失敗。回應碼：" + response.code() + "；錯誤訊息：" + response.message());

              emisLog.addLog("連接網路失敗。回應碼：" + response.code() + "；錯誤訊息：" + response.message());

              Toast.makeText(LoginActivity.this, "連接網路失敗。回應碼：" + response.code() + "；錯誤訊息：" + response.message(), Toast.LENGTH_SHORT).show();

              tryLoginNidinMQTTTimes++;

              loginNidinMQTT();

              Looper.loop();
            }
          }
        });
      }
    } catch (Exception e) {
      message.what = 1;
      mHandler.dispatchMessage(message);

      emisLog.addLog("登入你訂系統過程有誤！錯誤訊息：", e);

      Toast.makeText(LoginActivity.this, "登入你訂系統過程有誤！錯誤訊息：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

      setResult(RESULT_OK, getIntent());
      finish();
    }
  }

  private void registerProcess(final String registerLocation, final String orgDomain, final String registerCode) {

    sOrg_Domain = orgDomain;
    AlertDialog.Builder builder = new AlertDialog.Builder(oContext_);
    // AlertDialog dialog;

    // View titleView = (LayoutInflater.from(oContext_)).inflate(R.layout.title_bar, null);
    View messageView = (LayoutInflater.from(oContext_)).inflate(R.layout.welcome_register, null);

    // ((TextView) titleView.findViewById(R.id.txtTitle)).setText("歡迎 - 註冊");

    ((TextView) messageView.findViewById(R.id.edtRegisterLocation)).setText(registerLocation);
    ((TextView) messageView.findViewById(R.id.edtOrgDomain)).setText(orgDomain);
    ((TextView) messageView.findViewById(R.id.edtRegisterCode)).setText(registerCode);

    builder.setNegativeButton("QRCODE", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        AlertDialog.Builder builder = new AlertDialog.Builder(oContext_);

        Intent intent = null;

        System.out.println("Build.BRAND: " + Build.BRAND);
        System.out.println("Build.DEVICE: " + Build.DEVICE);
        System.out.println("Build.SERIAL: " + Build.SERIAL);

        if (Build.BRAND.equals("SUNMI")) {
          intent = new Intent("com.summi.scan");
          intent.setPackage("com.sunmi.sunmiqrcodescanner");
          intent.putExtra("CURRENT_PPI", 0X0003);
          intent.putExtra("PLAY_SOUND", true);// 扫描完成声音提示  默认true
          intent.putExtra("PLAY_VIBRATE", false);
          intent.putExtra("IDENTIFY_INVERSE_QR_CODE", true);// 识别反色二维码，默认true
          intent.putExtra("IDENTIFY_MORE_CODE", false);// 识别画面中多个二维码，默认false
          intent.putExtra("IS_SHOW_SETTING", true);// 是否显示右上角设置按钮，默认true
          intent.putExtra("IS_SHOW_ALBUM", true);// 是否显示从相册选择图片按钮，默认true

          // startActivityForResult(new Intent(LoginActivity.this, ScannerUtil.class), SCAN_ACT_SUNMI);
          startActivityForResult(intent, SCAN_SUNMI);
        } else {
          builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
              registerProcess(registerLocation, orgDomain, registerCode);
            }
          });

          (createDialog("警告", "目前不支援此機型的掃描功能！", builder)).show();
        }
      }
    });

    builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        AlertDialog.Builder builder = new AlertDialog.Builder(oContext_);
        // AlertDialog alertDialog;

        boolean checkError = false;

        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            registerProcess(((TextView) messageView.findViewById(R.id.edtRegisterLocation)).getText().toString(), ((TextView) messageView.findViewById(R.id.edtOrgDomain)).getText().toString(), ((TextView) messageView.findViewById(R.id.edtRegisterCode)).getText().toString());
          }
        });

        if (checkError == false) {
          if (((TextView) messageView.findViewById(R.id.edtRegisterLocation)).getText().toString().equals("")) {
            checkError = true;

            // builder.setTitle("錯誤").setMessage("註冊位置不可空白");

            // alertDialog = builder.create();
            // alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            // alertDialog.show();

            (createDialog("錯誤", "註冊位置不可空白！", builder)).show();
          }
        }

        if (checkError == false) {
          if (((TextView) messageView.findViewById(R.id.edtRegisterCode)).getText().toString().equals("")) {
            checkError = true;

            // builder.setTitle("錯誤").setMessage("授權碼不可空白");

            // alertDialog = builder.create();
            // alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            // alertDialog.show();

            (createDialog("錯誤", "授權碼不可空白！", builder)).show();
          }
        }

        if (checkError == false) {
          LoadingDialog = LoadingDialogUtil.createLoadingDialog(LoginActivity.this, "獲取資料中...");

          emisLog.addLog("===== getIDNO_AUTH_ONE Start =====");

          getIDNO_AUTH_ONE(((TextView) messageView.findViewById(R.id.edtRegisterLocation)).getText().toString(), ((TextView) messageView.findViewById(R.id.edtOrgDomain)).getText().toString(), ((TextView) messageView.findViewById(R.id.edtRegisterCode)).getText().toString(), "");
        }
      }
    });

    // builder.setCustomTitle(titleView);
    // builder.setTitle("歡迎 - 註冊");
    builder.setView(messageView);

    // builder.show();
    // dialog = builder.show();
    // dialog.show();

    (createDialog("歡迎 - 註冊", builder)).show();
  }

  private void getIDNO_AUTH_ONE(final String registerLocation, final String orgDomain, final String registerCode, final String errMessage) {
    final OkHttpClient okClient = new OkHttpClient().newBuilder().sslSocketFactory(SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(SSLSocketClient.getHostnameVerifier()).build();

    LayoutInflater inflater = LayoutInflater.from(oContext_);

    Message message = new Message();
    RequestBody body;
    Request request;

    JSONObject jsonObject = new JSONObject();

    try {
      jsonObject.put("ID_AUTH", registerCode);
      jsonObject.put("SERIAL", ""); // 機器序號

      if (Build.BRAND.equals("SUNMI")) {
        jsonObject.put("SERIAL", Build.SERIAL);
      }

      emisLog.addLog("getIDNO_AUTH_ONE POST content:" + jsonObject.toString());
      System.out.println("getIDNO_AUTH_ONE POST content:" + jsonObject.toString());

      // body = RequestBody.create(emisAndroidUtil.JSON, jsonObject.toString());
      body = RequestBody.create(emisAndroidUtil.JSON, AES.encrypt(jsonObject.toString()));
      // request = new Request.Builder().addHeader("sUser", sOrg_Domain).url(registerLocation + "/ws/a920/v1/getIDNO_AUTH_ONE").post(body).build();
      // request = new Request.Builder().addHeader("sUser", sOrg_Domain).url(registerLocation + "/ws/sunmi/v1/getIDNO_AUTH_ONE").post(body).build();
      request = new Request.Builder().addHeader("sUser", orgDomain).url(registerLocation + "/ws/sunmi/v1/getIDNO_AUTH_ONE").post(body).build();

      if (tryRegisterTimes >= 3) {
        tryRegisterTimes = 0;

        runOnUiThread(new Runnable() {

          public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(oContext_);
            // AlertDialog dialog;
            try {
              message.what = 1;
              mHandler.dispatchMessage(message);

              emisLog.addLog("===== getIDNO_AUTH_ONE END =====");
              emisLog.closeLog();

              // Toast.makeText(LoginActivity.this, "無法取得註冊資料！！", Toast.LENGTH_SHORT).show();

              builder.setTitle("錯誤").setMessage("無法取得註冊資料！！\n\n錯誤訊息：" + errMessage);

              builder.setPositiveButton("再試一次", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                  LoadingDialog = LoadingDialogUtil.createLoadingDialog(LoginActivity.this, "獲取資料中...");

                  emisLog.addLog("===== getIDNO_AUTH_ONE Start =====");

                  getIDNO_AUTH_ONE(registerLocation, orgDomain, registerCode, "");
                }
              });

              builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                  registerProcess(registerLocation, orgDomain, registerCode);
                }
              });

              // dialog = builder.create();
              // dialog.setIcon(android.R.drawable.ic_dialog_alert);
              // dialog.show();

              (createDialog("錯誤", "無法取得註冊資料！！\n\n錯誤訊息：" + errMessage, builder)).show();
            } catch (Exception e) {

              e.printStackTrace();
            }
          }
        });
      } else {

        okClient.newCall(request).enqueue(new Callback() {

          @Override
          public void onFailure(Call call, IOException e) {

            System.out.println("取得註冊資料失敗。錯誤訊息：" + e.getLocalizedMessage());

            emisLog.addLog("取得註冊資料失敗。錯誤訊息：", e);

            tryRegisterTimes++;

            getIDNO_AUTH_ONE(registerLocation, orgDomain, registerCode, e.getLocalizedMessage());
          }

          @Override
          public void onResponse(Call call, Response response) throws IOException {
            String responseBody;
            if (response.isSuccessful()) {
              responseBody = AES.decrypt(response.body().string());

              System.out.println("Response:" + responseBody);

              LoginActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                  AlertDialog.Builder builder = new AlertDialog.Builder(oContext_);

                  // View titleView = getLayoutInflater().inflate(R.layout.title_bar, null);
                  View view = inflater.inflate(R.layout.register_basedata, null);

                  Map<String, Object> map;
                  String code, msg;

                  try {
                    map = (Map<String, Object>) (new Gson()).fromJson(responseBody, (new HashMap<String, Object>()).getClass());
                    code = (String) map.get("code");
                    msg = (String) map.get("msg");

                    if (code.equals("000")) {
                      emisLog.addLog("===== getIDNO_AUTH_ONE END =====");
                      emisLog.closeLog();

                      message.what = 1;
                      mHandler.dispatchMessage(message);

                      ((TextView) view.findViewById(R.id.edtINV_SELECT)).setText((String) map.get("s_no"));
                      ((TextView) view.findViewById(R.id.edtSL_NO)).setText((String) map.get("s_name"));
                      ((TextView) view.findViewById(R.id.edtID_NO)).setText((String) map.get("id_no"));

                      builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                          AlertDialog.Builder builder = new AlertDialog.Builder(oContext_);
                          // AlertDialog alertDialog;

                          Callback callback = new Callback() {

                            @Override
                            public void onFailure(Call call, IOException e) {

                              Message message = new Message();

                              AlertDialog.Builder builder = new AlertDialog.Builder(oContext_);

                              message.what = 1;
                              mHandler.dispatchMessage(message);

                              System.out.println("取得初始資料過程有誤。錯誤訊息：" + e.getLocalizedMessage());

                              // Toast.makeText(LoginActivity.this, "取得初始資料過程有誤。錯誤訊息：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                              Looper.prepare();

                              builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                  registerProcess(registerLocation, orgDomain, registerCode);
                                }
                              });

                              // builder.setTitle("錯誤").setMessage("取得初始資料過程有誤。錯誤訊息：" + e.getLocalizedMessage());

                              (createDialog("錯誤", "取得初始資料過程有誤。錯誤訊息：" + e.getLocalizedMessage(), builder)).show();

                              Looper.loop();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                              AlertDialog.Builder builder = new AlertDialog.Builder(oContext_);
                              // AlertDialog dialog;

                              final String responseBody = response.body().string();

                              if (response.isSuccessful()) {
                                LoginActivity.this.runOnUiThread(new Runnable() {

                                  @Override
                                  public void run() {
                                    Gson gson = new Gson();
                                    Map<String, Object> map = (Map<String, Object>) gson.fromJson(responseBody, (new HashMap<String, Object>()).getClass());
                                    String code = (String) map.get("code");
                                    String msg = (String) map.get("msg");

                                    Callback callback = new Callback() {

                                      @Override
                                      public void onFailure(Call call, IOException e) {

                                        LoginActivity.this.runOnUiThread(new Runnable() {

                                          @Override
                                          public void run() {
                                            // LoadingDialogUtil.closeDialog(LoadingDialog);

                                            Message message = new Message();

                                            AlertDialog.Builder builder = new AlertDialog.Builder(oContext_);

                                            message.what = 1;
                                            mHandler.dispatchMessage(message);

                                            // emisAndroidUtil.showMessage(oContext_, "取得基本資料失敗", e.getLocalizedMessage(), null);

                                            // registerProcess(registerLocation, orgDomain, registerCode);

                                            builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

                                              @Override
                                              public void onClick(DialogInterface dialog, int which) {
                                                registerProcess(registerLocation, orgDomain, registerCode);
                                              }
                                            });

                                            // builder.setTitle("錯誤").setMessage("取得基本資料發生錯誤，錯誤訊息：" + e.getLocalizedMessage());

                                            (createDialog("錯誤", "取得基本資料發生錯誤，錯誤訊息：" + e.getLocalizedMessage(), builder)).show();

                                            emisLog.addLog("取得基本資料發生錯誤，錯誤訊息：", e);
                                            emisLog.addLog("===== GetBasedata END =====");
                                            emisLog.closeLog();
                                          }
                                        });
                                      }

                                      @Override
                                      public void onResponse(Call call, Response response) throws IOException {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(oContext_);

                                        final String responseBody = response.body().string();

                                        Message message = new Message();

                                        if (response.isSuccessful()) {
                                          System.out.println("GetBasedata response:" + responseBody);

                                          LoginActivity.this.runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                              // txtMsg_.setText(_sResponse);
                                              // SharedPreferences _oPersonalSettings = getSharedPreferences("personal", 0);
                                              // _oPersonalSettings.edit().putBoolean("loggedin",true).apply();

                                              AlertDialog.Builder builder = new AlertDialog.Builder(oContext_);

                                              Gson gson = new Gson();
                                              Map<String, Object> map = (Map<String, Object>) gson.fromJson(responseBody, (new HashMap<String, Object>()).getClass());
                                              String _sCode = (String) map.get("code");
                                              String _sMsg = (String) map.get("msg");

                                              if (_sCode.equals("000")) {

                                                emisKeeper.getInstance().setsInfo("S_NO", ((TextView) view.findViewById(R.id.edtINV_SELECT)).getText().toString(), oContext_);
                                                emisKeeper.getInstance().setsInfo("S_NAME", ((TextView) view.findViewById(R.id.edtSL_NO)).getText().toString(), oContext_);
                                                emisKeeper.getInstance().setsInfo("ID_NO", ((TextView) view.findViewById(R.id.edtID_NO)).getText().toString(), oContext_);
                                                emisKeeper.getInstance().setsInfo("ORG_DOMAIN", orgDomain, oContext_);
                                                emisKeeper.getInstance().setsInfo("API_URL", registerLocation, oContext_);

                                                saveBasedata(map);

                                                setDefaultData();

                                                message.what = 1;
                                                mHandler.dispatchMessage(message);

                                                Toast.makeText(LoginActivity.this, "取得基本資料完成", Toast.LENGTH_SHORT).show();

                                                emisLog.addLog("===== GetBasedata END =====");
                                                emisLog.closeLog();
                                              } else if (_sCode.equals("001")) {
                                                message.what = 1;
                                                mHandler.dispatchMessage(message);

                                                Toast.makeText(LoginActivity.this, "該機台已超過到期日", Toast.LENGTH_SHORT).show();

                                                emisLog.addLog("===== GetBasedata END =====");
                                                emisLog.closeLog();
                                              } else if (_sCode.equals("002")) {
                                                message.what = 1;
                                                mHandler.dispatchMessage(message);

                                                Toast.makeText(LoginActivity.this, "該機台已停用", Toast.LENGTH_SHORT).show();

                                                emisLog.addLog("===== GetBasedata END =====");
                                                emisLog.closeLog();
                                              } else {
                                                message.what = 1;
                                                mHandler.dispatchMessage(message);

                                                System.out.println("取得基本資料時發生錯誤！！錯誤代碼：" + _sCode + "，訊息：" + _sMsg);

                                                emisLog.addLog("取得基本資料時發生錯誤！！錯誤代碼：" + response.code() + "，訊息：" + response.message());
                                                emisLog.addLog("===== GetBasedata END =====");
                                                emisLog.closeLog();

                                                // Toast.makeText(LoginActivity.this, "取得基本資料時發生錯誤，錯誤代碼：" + _sCode + "，訊息：" + _sMsg, Toast.LENGTH_SHORT).show();

                                                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

                                                  @Override
                                                  public void onClick(DialogInterface dialog, int which) {
                                                    registerProcess(registerLocation, orgDomain, registerCode);
                                                  }
                                                });

                                                // builder.setTitle("錯誤").setMessage("取得基本資料時發生錯誤！！\n\n錯誤代碼：" + _sCode + "\n訊息：" + _sMsg);

                                                // dialog = builder.create();
                                                // dialog.setIcon(android.R.drawable.ic_dialog_alert);
                                                // dialog.show();

                                                (createDialog("錯誤", "取得基本資料時發生錯誤！！\n\n錯誤代碼：" + _sCode + "\n訊息：" + _sMsg, builder)).show();
                                              }
                                            }
                                          });
                                        } else {

                                          message.what = 1;
                                          mHandler.dispatchMessage(message);

                                          System.out.println("取得基本資料時發生錯誤！！錯誤代碼：" + response.code() + "，錯誤訊息：" + response.message());

                                          // Toast.makeText(LoginActivity.this, "取得基本資料時發生錯誤，錯誤代碼：" + response.code() + "，訊息：" + response.message(), Toast.LENGTH_SHORT).show();

                                          // registerProcess(registerLocation, orgDomain, registerCode);

                                          emisLog.addLog("取得基本資料時發生錯誤，錯誤代碼：" + response.code() + "，錯誤訊息：" + response.message());
                                          emisLog.addLog("===== GetBasedata END =====");
                                          emisLog.closeLog();

                                          builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                              registerProcess(registerLocation, orgDomain, registerCode);
                                            }
                                          });

                                          // builder.setTitle("錯誤").setMessage("取得基本資料時發生錯誤！！\n\n錯誤代碼：" + response.code() + "\n錯誤訊息：" + response.message());

                                          (createDialog("錯誤", "取得基本資料時發生錯誤！！\n\n錯誤代碼：" + response.code() + "\n錯誤訊息：" + response.message(), builder)).show();
                                        }
                                      }
                                    };

                                    // txtMsg.setText(logResponse);

                                    if (code.equals("000")) {
                                      message.what = 1;
                                      mHandler.dispatchMessage(message);

                                      BaseUtil.saveInitData(oContext_, map);

                                      Toast.makeText(LoginActivity.this, "成功獲取初始資料", Toast.LENGTH_SHORT).show();

                                      // getBasedata("root", (String) map.get("id_no"), (String) map.get("s_no"), orgDomain, registerLocation, callback);  // 2020/07/08 comment Cody 測試用，傳入null字串。
                                      getBasedata("root", ((TextView) view.findViewById(R.id.edtID_NO)).getText().toString(), ((TextView) view.findViewById(R.id.edtINV_SELECT)).getText().toString(), orgDomain, registerLocation, callback);
                                    } else {

                                      message.what = 1;
                                      mHandler.dispatchMessage(message);

                                      // Toast.makeText(LoginActivity.this, "獲取初始資料失敗。回應碼：" + _sCode + "；訊息：" + _sMsg, Toast.LENGTH_SHORT).show();

                                      // registerProcess(registerLocation, orgDomain, registerCode);

                                      LoginActivity.this.runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                          AlertDialog.Builder builder = new AlertDialog.Builder(oContext_);

                                          builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                              registerProcess(registerLocation, orgDomain, registerCode);
                                            }
                                          });

                                          // builder.setTitle("錯誤").setMessage("初始資料連接網路失敗。\n\n錯誤代碼：" + code + "\n錯誤訊息：" + msg);

                                          // dialog = builder.create();
                                          // dialog.setIcon(android.R.drawable.ic_dialog_alert);
                                          // dialog.show();

                                          (createDialog("錯誤", "初始資料連接網路失敗。\n\n錯誤代碼：" + code + "\n錯誤訊息：" + msg, builder)).show();
                                        }
                                      });
                                    }
                                  }
                                });
                              } else {
                                message.what = 1;
                                mHandler.dispatchMessage(message);

                                System.out.println("初始資料連接網路失敗。錯誤代碼：" + response.code() + "；錯誤訊息：" + response.message());

                                // Toast.makeText(LoginActivity.this, "連接網路失敗。回應碼：" + response.code() + "；訊息：" + response.message(), Toast.LENGTH_SHORT).show();

                                // registerProcess(registerLocation, orgDomain, registerCode);

                                Looper.prepare();

                                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                    registerProcess(registerLocation, orgDomain, registerCode);
                                  }
                                });

                                // builder.setTitle("錯誤").setMessage("初始資料連接網路失敗。\n\n錯誤代碼：" + response.code() + "\n錯誤訊息：" + response.message());

                                // dialog = builder.create();
                                // dialog.setIcon(android.R.drawable.ic_dialog_alert);
                                // dialog.show();

                                (createDialog("錯誤", "初始資料連接網路失敗。\n\n錯誤代碼：" + response.code() + "\n錯誤訊息：" + response.message(), builder)).show();

                                Looper.loop();
                              }
                            }
                          };

                          try {
                            LoadingDialog = LoadingDialogUtil.createLoadingDialog(LoginActivity.this, "獲取資料中...");

                            BaseUtil.getInitData((String) map.get("s_no"), (String) map.get("id_no"), orgDomain, registerLocation, callback);
                          } catch (Exception e) {
                            System.out.println("初始資料處理過程有誤。錯誤訊息：" + e.getLocalizedMessage());

                            e.printStackTrace();

                            Looper.prepare();

                            // Toast.makeText(LoginActivity.this, "初始資料處理過程有誤。錯誤訊息：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                registerProcess(registerLocation, orgDomain, registerCode);
                              }
                            });

                            // builder.setTitle("錯誤").setMessage("初始資料處理過程有誤。錯誤訊息：" + e.getLocalizedMessage());

                            // alertDialog = builder.create();
                            // alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                            // alertDialog.show();

                            (createDialog("錯誤", "取得初始資料過程有誤。錯誤訊息：" + e.getLocalizedMessage(), builder)).show();

                            Looper.loop();
                          }
                        }
                      });

                      builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          registerProcess(registerLocation, orgDomain, registerCode);
                        }
                      });

                      // builder.setTitle("機台基本資料");
                      // ((TextView) titleView.findViewById(R.id.txtTitle)).setText("機台基本資料");
                      // builder.setCustomTitle(titleView);
                      builder.setView(view);
                      // builder.show();

                      (createDialog("機台基本資料", builder)).show();
                    } else {

                      System.out.println("取得註冊資料失敗。錯誤訊息：" + msg);

                      emisLog.addLog("取得註冊資料失敗。錯誤訊息：" + msg);

                      tryRegisterTimes++;

                      getIDNO_AUTH_ONE(registerLocation, orgDomain, registerCode, msg);
                    }
                  } catch (Exception e) {
                    System.out.println("註冊資料處理過程有誤。錯誤訊息：" + e.getLocalizedMessage());

                    emisLog.addLog("註冊資料處理過程有誤。錯誤訊息：", e);

                    tryRegisterTimes++;

                    getIDNO_AUTH_ONE(registerLocation, orgDomain, registerCode, e.getLocalizedMessage());
                  }
                }
              });
            } else {
              System.out.println("連接網路失敗。回應碼：" + response.code() + "；錯誤訊息：" + response.message());

              emisLog.addLog("連接網路失敗。回應碼：" + response.code() + "；錯誤訊息：" + response.message());

              // Toast.makeText(LoginActivity.this, "連接網路失敗。回應碼：" + response.code() + "；錯誤訊息：" + response.message(), Toast.LENGTH_SHORT).show();

              tryRegisterTimes++;

              getIDNO_AUTH_ONE(registerLocation, orgDomain, registerCode, response.message());
            }
          }
        });
      }
    } catch (Exception e) {

      emisLog.addLog("註冊過程有誤！錯誤訊息：", e);

      System.out.println("註冊過程有誤！錯誤訊息：" + e.getLocalizedMessage());

      tryRegisterTimes++;

      getIDNO_AUTH_ONE(registerLocation, orgDomain, registerCode, e.getLocalizedMessage());
    }
  }

  private void setDefaultData() {
    String date = emisUtil.todayDateAD("-");
    String[] dateArray = emisUtil.todayDateAD("-").split("-");

    emisKeeper.getInstance().refreshInfo(this);

    sUserID_ = emisKeeper.getInstance().getsInfo("USERID", this);
    sS_NO_ = emisKeeper.getInstance().getsInfo("S_NO", this);
    sS_NAME_ = emisKeeper.getInstance().getsInfo("S_NAME", this);
    sID_NO_ = emisKeeper.getInstance().getsInfo("ID_NO", this);
    sOrg_Domain = emisKeeper.getInstance().getsInfo("ORG_DOMAIN", this);

    emisAndroidUtil.WWW_EMIS_URL = emisKeeper.getInstance().getsInfo("API_URL", this);

    txtDate.setText(dateArray[1] + "/" + dateArray[2]);
    txtDay.setText(emisUtil.getWeek(date) + "\n" + dateArray[0]);

    // if (!"".equals(sUserID_)) edtUserID_.setText(sUserID_);
    edtUserID_.setText(sUserID_);

    edtStore_.setText(sS_NO_ + " " + sS_NAME_);
    edtStore_.setEnabled(false);
    edtCashRegister_.setText(sID_NO_);
    edtCashRegister_.setEnabled(false);
  }

  public void sendLog(String EXG_ID, String EXG_KIND, String message) {
    final OkHttpClient okClient = new OkHttpClient().newBuilder().sslSocketFactory(SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(SSLSocketClient.getHostnameVerifier()).build();

    JsonObject req = new JsonObject();

    req.addProperty("S_NO", sS_NO_);
    req.addProperty("ID_NO", sID_NO_);
    req.addProperty("EXG_ID", EXG_ID);
    req.addProperty("EXG_IDNAME", EXG_ID);
    req.addProperty("EXG_FILE", EXG_ID);
    req.addProperty("EXG_KIND", EXG_KIND);
    req.addProperty("EXG_DATE", emisUtil.todayDateAD());
    req.addProperty("EXG_TIME", Time.getOnlyTime());
    req.addProperty("EXG_LOG", message);

    RequestBody body = RequestBody.create(emisAndroidUtil.JSON, req.toString());
    Request request = new Request.Builder().addHeader("sUser", sOrg_Domain).url(emisKeeper.getInstance().getsInfo("API_URL", this) + "/ws/sunmi/v1/setApiLog").post(body).build();

    okClient.newCall(request).enqueue(new Callback() {

      @Override
      public void onFailure(Call call, IOException e) {
        LoginActivity.this.runOnUiThread(new Runnable() {

          @Override
          public void run() {
            emisAndroidUtil.showMessage(oContext_, "登入失敗", "登入時發生錯誤：" + e.getMessage(), null);
          }
        });
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        final String responseBody = response.body().string();

        System.out.println(EXG_ID + " sendLog response content:" + responseBody);

        if (response.isSuccessful()) {
          LoginActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
              Message message = new Message();

              if (!response.isSuccessful()) {
                message.what = 1;
                mHandler.dispatchMessage(message);
                // txtMsg_.setText("登入失敗。回應碼:" + response.code() + " 訊息:" + response.message());
              } else {
                // txtMsg_.setText(_sResponse);
                // SharedPreferences _oPersonalSettings = getSharedPreferences("personal", 0);
                // _oPersonalSettings.edit().putBoolean("loggedin",true).apply();

                Gson gson = new Gson();
                Map<String, Object> map = (Map<String, Object>) gson.fromJson(responseBody, (new HashMap<String, Object>()).getClass());
                String _sCode = (String) map.get("code");
                String _sMsg = (String) map.get("msg");

                if (!_sCode.equals("000")) {
                  Toast.makeText(LoginActivity.this, _sMsg, Toast.LENGTH_SHORT).show();
                }
              }
            }
          });
        }
      }
    });
  }

  private void saveBasedata(Map<String, Object> map) {
    System.out.println("Start Save Basedata");

    try {
      BaseUtil.synProp((ArrayList) ((Map<String, Object>) map.get("result")).get("prop"));

      new Thread(new Runnable() {
        @Override
        public void run() {
          emisProp.getInstance().setProp("SME_URL", "http://211.149.169.63:8035/vi_bk");
          emisProp.getInstance().setProp("S_NO", "ZH001");
          emisBMSynDataImpl impl = new emisBMSynDataImpl();
          impl.postAction("synPartData");
        }
      }, "synData").start();

      //sendLog("A920.FUNCTIONS_MPOS", "0", "");
    } catch (Exception e) {
      e.printStackTrace();
      //sendLog("A920.FUNCTIONS_MPOS", "1", e.getLocalizedMessage());
    }

    BaseUtil.updateFromProp("LastUpdateDate", Time.getOnlyDate());
    BaseUtil.updateFromProp("INIT", "Y");
  }

  private void getBasedata(final String userId, final String idNo, final String sNo, final String orgDomain, final String apiUrl, Callback callback) {
    // final ExecutorService service = Executors.newSingleThreadExecutor();

    final OkHttpClient okClient = new OkHttpClient().newBuilder().sslSocketFactory(SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(SSLSocketClient.getHostnameVerifier()).build();

    final JSONObject jsonObj = new JSONObject();
    RequestBody body;
    Request request;
    try {
      LoadingDialog = LoadingDialogUtil.createLoadingDialog(LoginActivity.this, "獲取資料中...");

      jsonObj.put("sNo", "ZH001");

      System.out.println("GetBasedata URL:" + "http://211.149.169.63:8035/vi_bk" + "/ws/wechatV3/bm/getProp");

      emisLog.addLog("GetBasedata post content:" + jsonObj.toString());
      System.out.println("GetBasedata post content::" + jsonObj.toString());

      //body = RequestBody.create(emisAndroidUtil.JSON, AES.encrypt(jsonObj.toString()));
      body = RequestBody.create(emisAndroidUtil.WWW_FORM, jsonObj.toString());
      request = new Request.Builder().addHeader("sUser", orgDomain).url("http://211.149.169.63:8035/vi_bk" + "/ws/wechatV3/bm/getProp").post(body).build();

      okClient.newCall(request).enqueue(callback);
    } catch (Exception e) {
      Toast.makeText(LoginActivity.this, "基本資料處理過程有誤。錯誤訊息：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

      System.out.println("基本資料處理過程有誤。錯誤訊息：" + e.getLocalizedMessage());

      e.printStackTrace();
    }
  }
}
