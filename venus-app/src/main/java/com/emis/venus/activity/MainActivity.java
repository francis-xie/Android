package com.emis.venus.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.emis.venus.R;
import com.emis.venus.common.BaseUtil;
import com.emis.venus.common.emisKeeper;
import com.emis.venus.db.emisDb;
import com.emis.venus.db.emisSQLiteWrapper;
import com.emis.venus.util.emisAndroidUtil;
import com.emis.venus.util.emisLog;
import com.emis.venus.util.emisUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.emis.venus.common.BaseUtil.LOGIN_ACT;

public class MainActivity extends BaseActivity {

  // @BindView(R.id.navigation) BottomNavigationView btnNavigation_;
  @BindView(R.id.txtLoginUser)
  TextView txtLoginUser_;
  @BindView(R.id.txtDate)
  TextView txtDate;
  @BindView(R.id.txtDay)
  TextView txtDay;
  //@BindView(R.id.txtTotalAmt)TextView txtTotalAmt;

  private TextView mTextMessage;
  private boolean isClear = true;
  private String _sAppVersion, _sAppName, sUserID_, sS_NO_, sD_NO, sS_NAME_, sID_NO_;
  public static IDAL idal = null;

  // API 23以上需要下列設定
  private static final int REQUEST_EXTERNAL_STORAGE = 1;
  private static String[] PERMISSIONS_STORAGE = {
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE};
  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
    = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      /*Intent intent = new Intent();
      switch (item.getItemId()) {
        case R.id.navigation_home:
//          mTextMessage.setText(R.string.title_home);
          return true;
        case R.id.navigation_sales:
          intent.setClass(getApplicationContext(), SalesSKUActivity.class);
          startActivityForResult(intent,SALES_SKU_ACT);
//          mTextMessage.setText(R.string.title_dashboard);
          return true;
        case R.id.navigation_settings:
          //mTextMessage.setText(R.string.title_notifications);
          intent.setClass(MainActivity.this, SettingsActivity.class);
          startActivity(intent);
          return true;
      }*/
      return false;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_orange);

    ButterKnife.bind(this);

    oContext_ = this;

    _sAppVersion = "V" + emisAndroidUtil.getAppVersion(this);
    _sAppName = emisAndroidUtil.getAppName(this);

    this.setTitle(_sAppName + " " + _sAppVersion);
    this.setTitleColor(0);
    // btnNavigation_.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    verifyStoragePermissions(this);

    emisDb.createInstance(this);
    if (!emisDb.exists()) BaseUtil.createTable();

    // 新增emisLog obj
    emisLog.checkLogFile();
    emisLog.addLog("===== Log Start =====");

    if (!emisKeeper.getInstance().ifLogin()) {
      startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), LOGIN_ACT);
    } else {
      refreshSetting();

      setLoginInfo(getIntent());

      createFunctionButton();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    System.out.println(emisUtil.getSYSDATETIME() + ": " + "onResume");

    if (emisKeeper.getInstance().ifLogin()) {
    }
  }

  private void setLoginInfo(Intent intent) {

    sS_NO_ = emisKeeper.getInstance().getsInfo("S_NO", this);
    sD_NO = emisKeeper.getInstance().getsInfo("D_NO", this);
    sS_NAME_ = emisKeeper.getInstance().getsInfo("S_NAME", this);
    sID_NO_ = emisKeeper.getInstance().getsInfo("ID_NO", this);
    sUserID_ = emisKeeper.getInstance().getsInfo("USERID", this);
    isClear = emisKeeper.getInstance().ifClear();

    BaseUtil.setLoginData(sUserID_, sS_NO_, sS_NAME_, sID_NO_);

    txtLoginUser_.setText(sS_NO_ + " " + sUserID_ + "\n" + sS_NAME_);

    String date = emisUtil.todayDateAD("-");
    String[] dateArray = date.split("-");
    String week = emisUtil.getWeek(date);
    txtDate.setText(dateArray[1] + "/" + dateArray[2]);
    txtDay.setText(week + "\n" + dateArray[0]);
  }

  public void btnClick(View view) {
    int _iFuncID = -1;
    Intent intent = null;

    /*switch (view.getId()) {
      case R.id.btnChangeUser:
      case R.id.txtChangeUser:
        _iFuncID = LOGIN_ACT;
        intent = new Intent(MainActivity.this, LoginActivity.class);
        break;
    }*/

    if (intent != null) {
      try {
        startActivityForResult(intent, _iFuncID);
      } catch (Exception e) {
        emisLog.addLog(e.getMessage());
        emisLog.closeLog();
        e.printStackTrace();
      }
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    int invoiceRest = 0;
    String einv_no = PreferenceManager.getDefaultSharedPreferences(oContext_).getString("EINV_SELECT", "");
    String inv_safe = PreferenceManager.getDefaultSharedPreferences(oContext_).getString("INV_SAFE", "");

    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case LOGIN_ACT:
        if (resultCode == RESULT_OK) {
          refreshSetting();

          setLoginInfo(data);

          createFunctionButton();

          Log.d("RESULT", sUserID_);

          if (Integer.valueOf(emisKeeper.getInstance().getsInfo("mchType", this)) == 1) {
            checkUpdate();
          }
        } else if (resultCode == BaseUtil.EXIT_NOW) {  // 重置時送出EXIT_NOW
          finish();
        } else {
          finish();
        }
        break;
    }
  }

  public static void verifyStoragePermissions(Activity activity) {
    // Check if we have write permission
    int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    if (permission != PackageManager.PERMISSION_GRANTED) {
      // We don't have permission so prompt the user
      ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
        REQUEST_EXTERNAL_STORAGE);
    }
  }

  private void refreshSetting() {
    emisKeeper.getInstance().refreshInfo(this);

    if (Integer.valueOf(emisKeeper.getInstance().getsInfo("mchType", this)) == 1) {
      try {
        idal = NeptuneLiteUser.getInstance().getDal(getApplicationContext());
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (null == idal) {
        Toast.makeText(MainActivity.this, "error occurred,DAL is null.", Toast.LENGTH_LONG).show();
      }
    }
  }

  protected void checkUpdate() {
    Intent it = new Intent("com.cybersoft.pos.internalaction.APPNOTIFY");
    it.putExtra("package_name", "com.emis.sunmi");
    sendBroadcast(it, "com.android.permission.Launcher");
  }

  private void createFunctionButton() {
    emisSQLiteWrapper db;
    SQLiteCursor cursor;

    List<Map<String, Object>> items = new ArrayList<>();
    Map<String, Object> item;
    SimpleAdapter adapter;

    try {
      db = emisDb.getInstance();
      cursor = db.executeQuery("select FUNC_ID, FUNC_NAME from FUNCTIONS_MPOS where (1 = 1) and (USED = 'Y') and ((FUNC_RIGHT = '')) order by SEQ_NO");

      while (cursor.moveToNext()) {
        if (!cursor.getString(0).equalsIgnoreCase("SWITCH_ACCOUNT")) {
          item = new HashMap<>();
          item.put("itemName", cursor.getString(1));

          if (item.get("itemImage") != null) items.add(item);
        }
      }

      item = new HashMap<>();
      item.put("itemName", "要货");
      //item.put("itemImage", R.drawable.switch_account);
      items.add(item);

      item = new HashMap<>();
      item.put("itemName", "切换账号");
      item.put("itemImage", R.drawable.switch_account);
      items.add(item);

      adapter = new SimpleAdapter(this, items, R.layout.grid_view_main, new String[]{"itemName", "itemImage"}, new int[]{R.id.itemName, R.id.itemImage});

      ((GridView) findViewById(R.id.gridViewMain)).setAdapter(adapter);
      ((GridView) findViewById(R.id.gridViewMain)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          // Toast.makeText(getApplicationContext(), itemName[position], Toast.LENGTH_SHORT).show();
          // System.out.println(((Map<String, Object>) items.get(position)).get("itemName"));

          if ("要货".equals((String) ((Map<String, Object>) items.get(position)).get("itemName"))) {
            startActivityForResult(new Intent(MainActivity.this, BillInActivity.class), LOGIN_ACT);
          } else if ((int) ((Map<String, Object>) items.get(position)).get("itemImage") == R.drawable.switch_account) {
            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), LOGIN_ACT);
          }
        }
      });
    } catch (Exception e) {
      Toast.makeText(getApplicationContext(), "建立功能列表過程有誤！錯誤訊息：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
      e.printStackTrace();
    }
  }
}
