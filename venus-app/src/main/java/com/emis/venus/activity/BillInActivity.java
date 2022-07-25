package com.emis.venus.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import com.emis.venus.R;
import com.emis.venus.common.SSLSocketClient;
import com.emis.venus.db.emisDb;
import com.emis.venus.db.emisProp;
import com.emis.venus.util.emisAndroidUtil;
import com.emis.venus.util.emisLog;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import static com.emis.venus.common.BaseUtil.SALES_SKU_ACT;
import static com.emis.venus.util.DialogUtil.createDialog;

public class BillInActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bill_in);

    oContext_ = BillInActivity.this;
  }

  public void btnBillQry(View view) {
    pullData(view, "/ws/h5/bill/getPorList", "?userId=ZH001&userType=0&sNo=ZH001");
  }

  public void btnBillIn(View view) {
    AlertDialog.Builder builder = new AlertDialog.Builder(oContext_);

    View _View = (LayoutInflater.from(oContext_)).inflate(R.layout.add_bill_in, null);
    setSpinner(_View);
    pullData(_View, "/ws/h5/store/getPartMList", "?userId=ZH001&userType=0&bType=POR");
    pullData(_View, "/ws/h5/store/getStoreVList", "?userId=ZH001&userType=0");
    pullData(_View, "/ws/h5/bill/getPorKind", "?userId=ZH001&userType=0");
    setDateOnClick(_View, R.id.btnBILL_IN_DATE, R.id.edtBILL_IN_DATE);
    setDateOnClick(_View, R.id.btnBILL_IN_P_DATE, R.id.edtBILL_IN_P_DATE);
    setTimeOnClick(_View);

    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        insBillInData(_View);
      }
    });
    builder.setView(_View);
    (createDialog("要货", builder)).show();
  }

  public void btnBillDetails(View view) {
    Intent intent = new Intent();
    intent.setClass(getApplicationContext(), BillDetailsActivity.class);
    startActivityForResult(intent, SALES_SKU_ACT);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
  }

  private void setGridView(Map<String, Object> map) {
    System.out.println("Start setGridView");
    List<Map<String, Object>> items = new ArrayList<>();
    Map<String, Object> item;
    try {
      ArrayList listSource = ((ArrayList) ((Map<String, Object>) map.get("result")).get("porList"));
      String[] sItem = new String[listSource.size()];
      for (int index = 0; index < listSource.size(); index++) {
        map = (LinkedTreeMap<String, Object>) listSource.get(index);
        sItem[index] = (String) map.get("tName");

        item = new HashMap<>();
        item.put("itemPro", (String) map.get("porNo"));
        item.put("itemProName", (String) map.get("sName"));
        item.put("itemDate1", (String) map.get("porDate"));
        item.put("itemTV1", (String) map.get("vNo"));
        item.put("itemTVName1", (String) map.get("vName"));
        item.put("itemType1", (String) map.get("porKind"));
        item.put("itemDate21", (String) map.get("poDDate"));
        item.put("itemTime1", (String) map.get("poDTime"));
        item.put("itemPodTotalQty", "要货总量：" + (String) map.get("podTotalQty"));
        item.put("itemPodTotalAmt", "要货总额：" + (String) map.get("podTotalAmt"));
        items.add(item);
      }
      SimpleAdapter adapter = new SimpleAdapter(oContext_, items, R.layout.bill_view,
        new String[]{"itemPro", "itemProName", "itemDate1", "itemTV1", "itemTVName1", "itemType1", "itemDate21"
          , "itemTime1", "itemPodTotalQty", "itemPodTotalAmt"},
        new int[]{R.id.itemPro, R.id.itemProName, R.id.itemDate1, R.id.itemTV1, R.id.itemTVName1, R.id.itemType1, R.id.itemDate21
          , R.id.itemTime1, R.id.itemPodTotalQty, R.id.itemPodTotalAmt});
      ((GridView) findViewById(R.id.gridViewBill)).setAdapter(adapter);

      System.out.println("End setGridView");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void insBillInData(View view) {
    Callback callback = new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        BillInActivity.this.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            Message message = new Message();
            message.what = 1;
            mHandler.dispatchMessage(message);

            emisAndroidUtil.showMessage(oContext_, "更新资料失败", e.getMessage(), null);

            emisLog.addLog("更新资料发生错误，错误讯息：", e);
            emisLog.addLog("===== insBillInData END =====");
            emisLog.closeLog();
          }
        });
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        final String responseBody = response.body().string();
        System.out.println("insBillInData response:" + responseBody);

        Message message = new Message();

        if (response.isSuccessful()) {
          BillInActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
              Message message = new Message();

              Gson gson = new Gson();
              Map<String, Object> map = (Map<String, Object>) gson.fromJson(responseBody, (new HashMap<String, Object>()).getClass());
              String _sCode = (String) map.get("code");
              String _sMsg = (String) map.get("msg");

              if (_sCode.equals("0")) {
                message.what = 1;
                mHandler.dispatchMessage(message);
                Toast.makeText(BillInActivity.this, "更新资料完成", Toast.LENGTH_SHORT).show();
                emisLog.addLog("===== insBillInData END =====");
                emisLog.closeLog();

                pullData(view, "/ws/h5/bill/getPorList", "?userId=ZH001&userType=0&sNo=ZH001");
              }
            }
          });
        } else {
          Toast.makeText(BillInActivity.this, "更新资料时发生错误，错误代码：" + response.code()
            + "，讯息：" + response.message(), Toast.LENGTH_SHORT).show();

          System.out.println("更新资料时发生错误，错误代码：" + response.code() + "，讯息：" + response.message());
          emisLog.addLog("更新资料时发生错误，错误代码：" + response.code() + "，讯息：" + response.message());
          emisLog.addLog("===== insBillInData END =====");
          emisLog.closeLog();

          message.what = 1;
          mHandler.dispatchMessage(message);
        }
      }
    };
    createPor(view, emisProp.getInstance().getProp("SME_URL"), callback);
  }

  private void createPor(View view, final String apiUrl, Callback callback) {
    final OkHttpClient okClient = new OkHttpClient().newBuilder().sslSocketFactory(
      SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(SSLSocketClient.getHostnameVerifier()).build();

    final JSONObject jsonObj = new JSONObject();
    RequestBody body;
    Request request;
    try {
      //LoadingDialog = LoadingDialogUtil.createLoadingDialog(BillInActivity.this, "获取资料中...");

      Spinner billSNo = (Spinner) view.findViewById(R.id.spinnerBILL_S_NO);
      Spinner billInPm = (Spinner) view.findViewById(R.id.spinnerBILL_IN_PM);
      EditText billInDate = (EditText) view.findViewById(R.id.edtBILL_IN_DATE);
      Spinner billInTv = (Spinner) view.findViewById(R.id.spinnerBILL_IN_TV);
      EditText billInPDate = (EditText) view.findViewById(R.id.edtBILL_IN_P_DATE);
      TextView billInPTime = (TextView) view.findViewById(R.id.timeBILL_IN_P_TIME);
      Spinner billInType = (Spinner) view.findViewById(R.id.spinnerBILL_IN_TYPE);
      EditText billInRemark = (EditText) view.findViewById(R.id.edBILL_IN_REMARK);
      Map<String, Object> item = (HashMap<String, Object>) billSNo.getSelectedItem();
      Map<String, Object> item2 = (HashMap<String, Object>) billInTv.getSelectedItem();
      Map<String, Object> item3 = (HashMap<String, Object>) billInType.getSelectedItem();
      jsonObj.put("userId", ("ZH001"));
      jsonObj.put("userType", ("0"));
      jsonObj.put("sNo", item.get("itemNo"));
      jsonObj.put("porDate", billInDate.getText().toString().replaceAll("/", ""));
      jsonObj.put("vNo", item2.get("itemNo"));
      jsonObj.put("poDDate", billInPDate.getText().toString().replaceAll("/", ""));
      jsonObj.put("poDTime", billInPTime.getText().toString().replaceAll("时", "").replaceAll("分", ""));
      jsonObj.put("porKind", item3.get("itemNo"));

      RequestBody formBody = new FormBody.Builder()
        .add("userId", ("ZH001"))
        .add("userType", ("0"))
        .add("sNo", item.get("itemNo").toString())
        .add("porDate", billInDate.getText().toString().replaceAll("/", ""))
        .add("vNo", item2.get("itemNo").toString())
        .add("poDDate", billInPDate.getText().toString().replaceAll("/", ""))
        .add("poDTime", billInPTime.getText().toString().replaceAll("时", "").replaceAll("分", ""))
        .add("porKind", item3.get("itemNo").toString())
        .build();
      System.out.println("createPor URL:" + apiUrl + "/ws/h5/bill/createPor");
      emisLog.addLog("createPor post content:" + formBody.toString());
      System.out.println("createPor post content::" + formBody.toString());

      //body = RequestBody.create(emisAndroidUtil.JSON, AES.encrypt(jsonObj.toString()));
      //body = RequestBody.create(MediaType.get("application/x-www-form-urlencoded"), jsonObj.toString());
      request = new Request.Builder().url(apiUrl + "/ws/h5/bill/createPor").post(formBody).build();

      okClient.newCall(request).enqueue(callback);
    } catch (Exception e) {
      Toast.makeText(BillInActivity.this, "资料处理过程有误。错误讯息：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
      System.out.println("资料处理过程有误。错误讯息：" + e.getLocalizedMessage());
      e.printStackTrace();
    }
  }

  private void setSpinner(View view) {
    SQLiteCursor cursor = null;
    try {
      List<Map<String, Object>> items = new ArrayList<>();
      Map<String, Object> item;

      cursor = (emisDb.getInstance()).executeQuery("select * from store");
      //String[] sItem = new String[cursor.getCount()];
      //int iCnt = 0;
      if (cursor.moveToNext()) {
        //sItem[iCnt] = cursor.getString(cursor.getColumnIndex("S_NAME"));
        //iCnt++;

        item = new HashMap<>();
        item.put("itemNo", cursor.getString(cursor.getColumnIndex("S_NO")));
        item.put("itemName", cursor.getString(cursor.getColumnIndex("S_NAME")));
        items.add(item);
      }

      Spinner spinner = (Spinner) view.findViewById(R.id.spinnerBILL_S_NO);
      //spinner.setAdapter(new ArrayAdapter<>(view.getContext(), R.layout.spinner_item, sItem));
      spinner.setAdapter(new SimpleAdapter(view.getContext(), items, R.layout.spinner_item,
        new String[]{"itemNo", "itemName"}, new int[]{R.id.itemNo, R.id.itemName}));
      spinner.setSelection(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void pullData(View view, String apiUrl, String params) {
    Callback callback = new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        BillInActivity.this.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            Message message = new Message();
            message.what = 1;
            mHandler.dispatchMessage(message);

            emisAndroidUtil.showMessage(oContext_, "更新资料失败", e.getMessage(), null);

            emisLog.addLog("更新资料失败发生错误，错误讯息：", e);
            emisLog.addLog("===== pullData END =====");
            emisLog.closeLog();
          }
        });
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        final String responseBody = response.body().string();
        System.out.println("pullData response:" + responseBody);

        Message message = new Message();

        if (response.isSuccessful()) {
          BillInActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              Message message = new Message();

              Gson gson = new Gson();
              Map<String, Object> map = (Map<String, Object>) gson.fromJson(responseBody, (new HashMap<String, Object>()).getClass());
              String _sCode = (String) map.get("code");
              String _sMsg = (String) map.get("msg");

              if (_sCode.equals("0")) {
                if (responseBody.indexOf("porList") >= 0) {
                  setGridView(map);
                } else if (responseBody.indexOf("partMList") >= 0) {
                  setPartMList(view, map);
                } else if (responseBody.indexOf("storeVList") >= 0) {
                  setStoreVList(view, map);
                } else if (responseBody.indexOf("porKind") >= 0) {
                  setPorKind(view, map);
                }
                message.what = 1;
                mHandler.dispatchMessage(message);
                Toast.makeText(BillInActivity.this, "更新资料完成", Toast.LENGTH_SHORT).show();
                emisLog.addLog("===== pullData END =====");
                emisLog.closeLog();
              } else if (_sCode.equals("100")) {
                if (responseBody.indexOf("porList") >= 0) {
                  setGridView(map);
                }
              }
            }
          });
        } else {
          Toast.makeText(BillInActivity.this, "更新资料时发生错误，错误代码：" + response.code()
            + "，讯息：" + response.message(), Toast.LENGTH_SHORT).show();

          System.out.println("更新资料时发生错误，错误代码：" + response.code() + "，讯息：" + response.message());

          emisLog.addLog("更新资料时发生错误，错误代码：" + response.code() + "，讯息：" + response.message());
          emisLog.addLog("===== pullData END =====");
          emisLog.closeLog();

          message.what = 1;
          mHandler.dispatchMessage(message);
        }
      }
    };
    get(sOrg_Domain, apiUrl, params, callback);
  }

  private void get(String orgDomain, String apiUrl, String params, Callback callback) {
    final OkHttpClient okClient = new OkHttpClient().newBuilder().sslSocketFactory(
      SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(SSLSocketClient.getHostnameVerifier()).build();

    Request request;
    try {
      //LoadingDialog = LoadingDialogUtil.createLoadingDialog(BillInActivity.this, "获取资料中...");
      //String smeUrl = emisAndroidUtil.WWW_EMIS_URL;
      String smeUrl = emisProp.getInstance().getProp("SME_URL");

      System.out.println("get URL:" + smeUrl + apiUrl);
      emisLog.addLog("get content:" + params);
      System.out.println("get content::" + params);

      request = new Request.Builder().url(smeUrl + apiUrl + params).build();
      smeUrl = null;
      apiUrl = null;
      params = null;
      okClient.newCall(request).enqueue(callback);
    } catch (Exception e) {
      Toast.makeText(BillInActivity.this, "资料处理过程有误。错误讯息：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
      System.out.println("资料处理过程有误。错误讯息：" + e.getLocalizedMessage());
      e.printStackTrace();
    }
  }

  private void setPartMList(View view, Map<String, Object> map) {
    System.out.println("Start setPartMList");
    try {
      List<Map<String, Object>> items = new ArrayList<>();
      Map<String, Object> item;

      ArrayList listSource = ((ArrayList) ((Map<String, Object>) map.get("result")).get("partMList"));
      //String[] sItem = new String[listSource.size()];
      for (int index = 0; index < listSource.size(); index++) {
        map = (LinkedTreeMap<String, Object>) listSource.get(index);
        //sItem[index] = (String) map.get("pmName");

        item = new HashMap<>();
        item.put("itemNo", (String) map.get("pmNo"));
        item.put("itemName", (String) map.get("pmName"));
        items.add(item);
      }
      Spinner spinner = (Spinner) view.findViewById(R.id.spinnerBILL_IN_PM);
      //spinner.setAdapter(new ArrayAdapter<>(view.getContext(), R.layout.spinner_item, sItem));
      spinner.setAdapter(new SimpleAdapter(view.getContext(), items, R.layout.spinner_item,
        new String[]{"itemNo", "itemName"}, new int[]{R.id.itemNo, R.id.itemName}));
      spinner.setSelection(0);
      System.out.println("End setPartMList");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setStoreVList(View view, Map<String, Object> map) {
    System.out.println("Start setStoreVList");
    try {
      List<Map<String, Object>> items = new ArrayList<>();
      Map<String, Object> item;

      ArrayList listSource = ((ArrayList) ((Map<String, Object>) map.get("result")).get("storeVList"));
      //String[] sItem = new String[listSource.size()];
      for (int index = 0; index < listSource.size(); index++) {
        map = (LinkedTreeMap<String, Object>) listSource.get(index);
        //sItem[index] = (String) map.get("vName");

        item = new HashMap<>();
        item.put("itemNo", (String) map.get("vNo"));
        item.put("itemName", (String) map.get("vName"));
        items.add(item);
      }
      Spinner spinner = (Spinner) view.findViewById(R.id.spinnerBILL_IN_TV);
      //spinner.setAdapter(new ArrayAdapter<>(view.getContext(), R.layout.spinner_item, sItem));
      spinner.setAdapter(new SimpleAdapter(view.getContext(), items, R.layout.spinner_item,
        new String[]{"itemNo", "itemName"}, new int[]{R.id.itemNo, R.id.itemName}));
      spinner.setSelection(0);
      System.out.println("End setStoreVList");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setPorKind(View view, Map<String, Object> map) {
    System.out.println("Start setPorKind");
    try {
      List<Map<String, Object>> items = new ArrayList<>();
      Map<String, Object> item;

      ArrayList listSource = ((ArrayList) ((Map<String, Object>) map.get("result")).get("porKind"));
      //String[] sItem = new String[listSource.size()];
      for (int index = 0; index < listSource.size(); index++) {
        map = (LinkedTreeMap<String, Object>) listSource.get(index);
        //sItem[index] = (String) map.get("tName");

        item = new HashMap<>();
        item.put("itemNo", (String) map.get("tNo"));
        item.put("itemName", (String) map.get("tName"));
        items.add(item);
      }
      Spinner spinner = (Spinner) view.findViewById(R.id.spinnerBILL_IN_TYPE);
      //spinner.setAdapter(new ArrayAdapter<>(view.getContext(), R.layout.spinner_item, sItem));
      spinner.setAdapter(new SimpleAdapter(view.getContext(), items, R.layout.spinner_item,
        new String[]{"itemNo", "itemName"}, new int[]{R.id.itemNo, R.id.itemName}));
      spinner.setSelection(0);
      System.out.println("End setPorKind");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setDateOnClick(View view, int ibId, int etId) {
    EditText etDate = (EditText) view.findViewById(etId);
    ImageButton ibDate = (ImageButton) view.findViewById(ibId);
    ibDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (etDate.getText() != null && !"".equals(etDate.getText().toString())) {
          year = Integer.valueOf(etDate.getText().toString().substring(0, 4));
          month = Integer.valueOf(etDate.getText().toString().substring(5, 7)) - 1;
          day = Integer.valueOf(etDate.getText().toString().substring(8));
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker view, int year, int month, int day) {
            String dateTime;
            if (month < 9) {
              dateTime = String.valueOf(year) + "/0" + String.valueOf(month + 1);
            } else {
              dateTime = String.valueOf(year) + "/" + String.valueOf(month + 1);
            }
            if (day <= 9) {
              dateTime = dateTime + "/0" + String.valueOf(day);
            } else {
              dateTime = dateTime + "/" + String.valueOf(day);
            }
            etDate.setText(dateTime);
          }
        }, year, month, day);
        datePickerDialog.show();
      }
    });
  }

  private void setTimeOnClick(View view) {
    TextView tvTime = (TextView) view.findViewById(R.id.timeBILL_IN_P_TIME);
    tvTime.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 获取当前的时间
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog pickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
          @Override
          public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = (hourOfDay <= 9 ? "0" : "") + String.valueOf(hourOfDay) + "时"
              + (minute <= 9 ? "0" : "") + String.valueOf(minute) + "分";
            tvTime.setText(time);
            time = null;
          }
        }, hour, minute, true);
        pickerDialog.show();

        /*AlertDialog.Builder builder2 = new AlertDialog.Builder(oContext_);
        builder2.setPositiveButton("设置", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            // 获取当前的时间
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            StringBuffer time = new StringBuffer();

            if (time.length() > 0) { //清除上次记录的日期
              time.delete(0, time.length());
            }
            tvTime.setText(time.append(String.valueOf(hour)).append("时").append(String.valueOf(minute)).append("分"));
            dialog.dismiss();
          }
        });
        builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
        AlertDialog dialog2 = builder2.create();
        View dialogView2 = View.inflate(oContext_, R.layout.dialog_time, null);
        TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);//设置为24小时显示格式
        timePicker.setHour(hour); //当前小时
        timePicker.setMinute(minute); //当前分钟
        timePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);//设置父布局focus，子控件不会focus，以此禁止调起键盘
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
          @Override
          public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            //时间改变的监听事件
            long delayMills = (hourOfDay * 60 + minute) * 60 * 1000;
          }
        });
        dialog2.setTitle("设置时间");
        dialog2.setView(dialogView2);
        dialog2.show();*/
      }
    });
  }
}
