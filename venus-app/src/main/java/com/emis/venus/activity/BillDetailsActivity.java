package com.emis.venus.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.emis.venus.R;
import com.emis.venus.common.BaseUtil;
import com.emis.venus.common.KeyboardHelper;
import com.emis.venus.common.emisKeeper;
import com.emis.venus.entity.BillDEntity;
import com.emis.venus.util.KeyboardUtil;
import com.emis.venus.util.emisAndroidUtil;
import com.emis.venus.util.emisUtil;
import com.google.gson.Gson;
import com.google.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.emis.venus.common.BaseUtil.*;
import static com.emis.venus.util.DialogUtil.createDialog;

public class BillDetailsActivity extends BaseActivity {

  @BindView(R.id.btnPay)
  Button btnPay;
  @BindView(R.id.edtP_NO)
  EditText edtP_NO;
  @BindView(R.id.edtQty)
  EditText edtQty;
  @BindView(R.id.btnAdd)
  ImageButton btnAdd;
  @BindView(R.id.btnMinus)
  ImageButton btnMinus;
  @BindView(R.id.btnScan)
  ImageButton btnScan;
  @BindView(R.id.btnClear)
  ImageButton btnClear;
  @BindView(R.id.tableLayout)
  TableLayout tableLayout;
  @BindView(R.id.keyboard)
  KeyboardView keyboardView;

  private boolean canLeave_ = false;  // 能否退回
  private Context oContext_;
  private int iP_NO_LEN_ = 13;
  private String sUserID_, sS_NO_, sS_NAME_, sID_NO_;
  private ArrayList<BillDEntity> lstSalesDetail_ = null;
  private static int ACT_ADD = 1;
  private static int ACT_UPD = 2;
  private int iAct_ = ACT_ADD;
  private static int _PART = 1;  // 商品資訊
  private static int _AMT = 2;  // 數量金額
  private static int _CHANGE_QTY = 3;  // 變更數量按鈕
  private int iCurrentRowId_ = -1;
  private int iTotal_ = 0;  // 合計金額
  private KeyboardHelper helper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bill_details);

    ButterKnife.bind(this);

    // oContext_ = this;
    oContext_ = BillDetailsActivity.this;

    sUserID_ = emisKeeper.getInstance().getsInfo("USERID", this);
    sS_NO_ = emisKeeper.getInstance().getsInfo("S_NO", this);
    sS_NAME_ = emisKeeper.getInstance().getsInfo("S_NAME", this);
    sID_NO_ = emisKeeper.getInstance().getsInfo("ID_NO", this);
    iP_NO_LEN_ = emisUtil.parseInt((PreferenceManager.getDefaultSharedPreferences(this)).getString("P_NO_LEN", "13"), 13);

    lstSalesDetail_ = emisKeeper.getInstance().getBillD();

    setTitle("商品入账");

    createContent();

    initSale();

    edtP_NO.requestFocus();

    // ActionBar actionBar = getSupportActionBar();
    getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.date_frame));

    emisAndroidUtil.hideKeyboard(BillDetailsActivity.this);

    if (helper != null)
      helper.hide();  // @comment 2020/08/03 Cody 隱藏Custom鍵盤。
  }

  private void queryPartLocal(String _sP_NO) {
    if ("".equals(_sP_NO)) {
      return;
    }
    String result = BaseUtil.getProduct(_sP_NO);

    // System.out.println(result);

    if (!result.contains("price")) {
      Toast.makeText(BillDetailsActivity.this, "查無該商品", Toast.LENGTH_LONG).show();
    } else {
      fillSalesDetailData(result, iAct_);
    }
  }

  private void calcSalesDetail() {
    int _iCount = 0, _iTotal = 0;

    for (int j = tableLayout.getChildCount(); _iCount < j; _iCount++) {
      View view = tableLayout.getChildAt(_iCount);
      if (view instanceof TableRow) {
        TableRow row = (TableRow) view;
        BillDEntity BillDEntity = (BillDEntity) row.getTag();
        int _iAmt = ((Double) BillDEntity.getSL_AMT()).intValue();
        _iTotal += _iAmt;
      }
    }

    iTotal_ = _iTotal;

    // txtMsg.setText("總件數：" + (_iCount) + " 總金額：" + emisUtil.getNumberWithComma(_iTotal));
    Toast.makeText(BillDetailsActivity.this, "總件數：" + (_iCount) + " 總金額：" + emisUtil.getNumberWithComma(_iTotal), Toast.LENGTH_SHORT).show();
  }

  @OnClick(R.id.btnOK)
  public void btnOKClicked() {
    String _sP_NO = edtP_NO.getText().toString();

    emisAndroidUtil.hideKeyboard(edtP_NO);

    if (_sP_NO.length() <= 0) {
      Toast.makeText(BillDetailsActivity.this, "請輸入商品編號", Toast.LENGTH_SHORT).show();
    } else {
      queryPartLocal(_sP_NO);

      if (helper != null)
        helper.hide();  // @comment 2020/08/03 Cody 隱藏Custom鍵盤。

      btnClearClicked();
    }
  }

  @OnClick(R.id.btnClear)
  public void btnClearClicked() {
    edtP_NO.setText("");
    edtQty.setText("1");
  }

  /**
   * 更新TableLayout
   *
   * @param sResponse
   * @param iAct,     ACT_ADD or ACT_UPD
   */
  private void fillSalesDetailData(String sResponse, int iAct) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    Gson gson = new Gson();
    Map<String, Object> map = (Map<String, Object>) gson.fromJson(sResponse, (new HashMap<String, Object>()).getClass());

    String _sP_NAME = (String) map.get("name");
    double[] _aP_PRICE = new double[6];
    _aP_PRICE[0] = Double.valueOf((String) map.get("price"));
    _aP_PRICE[1] = Double.valueOf((String) map.get("price2"));
    _aP_PRICE[2] = Double.valueOf((String) map.get("price3"));
    _aP_PRICE[3] = Double.valueOf((String) map.get("price4"));
    _aP_PRICE[4] = Double.valueOf((String) map.get("price5"));
    _aP_PRICE[5] = Double.valueOf((String) map.get("price6"));

    String _sP_NO = (String) map.get("p_no");
    String _sD_NO = (String) map.get("d_no");
    String _sSUBDEP = (String) map.get("subdep");
    String _sP_DEFC = (String) map.get("p_defc");
    String _sQty = edtQty.getText().toString();
    String _sPTax = (String) map.get("p_tax");
    String _sPriceType = (String) map.get("p_pu");
    int _iQty = emisUtil.parseInt(_sQty);
    int _iRowCount = tableLayout.getChildCount();

    final int rowCount = _iRowCount;
    TableRow tr;

    final EditText et = new EditText(this);

    if (iAct == ACT_UPD) _iRowCount = iCurrentRowId_;

    //MyDatabase db = new MyDatabase(this);
    BillDEntity BillDEntity = new BillDEntity();
    BillDEntity.setP_NO(_sP_NO);
    BillDEntity.setP_NAME(_sP_NAME);
    BillDEntity.setDP_NO(_sD_NO);
    BillDEntity.setDD_NO(_sSUBDEP);
    BillDEntity.setRECNO(_iRowCount + 1);
    BillDEntity.setSL_QTY(_iQty);
    BillDEntity.setP_TAX(_sPTax);

    tr = addTableRow(_iRowCount, BillDEntity);
    // tableLayout.addView(tr);
    tableLayout.addView(tr, 0);

    calcSalesDetail();
  }

  private TableRow addTableRow(int iRowCount, BillDEntity BillDEntity) {
    String _sP_NAME = BillDEntity.getP_NAME();
    String _sP_NO = BillDEntity.getP_NO();
    int _iQty = BillDEntity.getSL_QTY();
    Double _iPrice = BillDEntity.getSL_PRICE();
    Double _iAmt = BillDEntity.getSL_AMT();

    TableLayout.LayoutParams row_layout_params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

    TableRow.LayoutParams view_layout_params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
    view_layout_params.setMargins(0, 0, 10, 15);
    row_layout_params.setMargins(0, 0, 0, 3);

    final TableRow tr = new TableRow(this);
    tr.setId(iRowCount);
    tr.setTag(BillDEntity);
    tr.setPadding(0, 0, 0, 0);
    tr.setLayoutParams(row_layout_params);
    tr.setGravity(Gravity.LEFT);
    tr.setBackgroundColor(Color.WHITE);

    TextView txtView = new TextView(this);
    txtView.setText("#" + (iRowCount + 1));
    txtView.setTextSize(16);
    txtView.setPadding(5, 0, 5, 0);
    txtView.setLayoutParams(view_layout_params);
    txtView.setTextColor(getResources().getColor(R.color.black));
    tr.addView(txtView);

    txtView = new TextView(this);
    if (_sP_NAME.length() <= 8) {
      txtView.setText(_sP_NO + "\n" + _sP_NAME);
    } else {
      txtView.setText(_sP_NO + "\n" + _sP_NAME.substring(0, 8) + "...");
    }
    txtView.setTextSize(16);
    txtView.setWidth(200);
    txtView.setPadding(0, 0, 0, 0);
    txtView.setLayoutParams(view_layout_params);
    txtView.setTextColor(getResources().getColor(R.color.black));
    tr.addView(txtView);

    txtView = new TextView(this);
    txtView.setText(_iQty + "X" + _iPrice + "\n=" + _iAmt);
    txtView.setTextSize(16);
    txtView.setWidth(200);
    txtView.setLayoutParams(view_layout_params);
    txtView.setTextColor(getResources().getColor(R.color.black));
    tr.addView(txtView);

    final int _iCurrRow = iRowCount;

    // change qty. 換下變更數量後，由Button.getTag取回貨號與數量，填入edtP_NO, edtQty.
    // 按確定時用fillBillDetailData(sResponse, ACT_UPD)變更TableRow內容.
    Button btn = new Button(this);
    btn.setBackgroundResource(R.drawable.ic_exposure_black_24dp);
    btn.setWidth(10);
    btn.setHeight(10);
    btn.setId(iRowCount);
    btn.setTag(edtP_NO.getText().toString() + "," + edtQty.getText().toString());
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        changeQty(_iCurrRow);
      }
    });
    TableRow.LayoutParams lp = new TableRow.LayoutParams(80, 80);
    lp.setMargins(0, 20, 0, 0);
    btn.setLayoutParams(lp);
    tr.addView(btn);

    btn = new Button(this);
    btn.setBackgroundResource(android.R.drawable.ic_menu_delete);
    btn.setTextSize(12);
    btn.setId(iRowCount);
    btn.setTag(edtP_NO.getText().toString() + "," + edtQty.getText().toString());
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        tableLayout.removeView(tr);
        calcSalesDetail();
      }
    });
    btn.setLayoutParams(lp);
    tr.addView(btn);

    return tr;
  }

  private void changeQty(int iRow) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    View view = null;

    for (int i = 0; i < tableLayout.getChildCount() && view == null; i++) {
      View temp = tableLayout.getChildAt(i);

      if (tableLayout.getChildAt(i).getId() == iRow) view = tableLayout.getChildAt(i);
    }

    TableRow row = (TableRow) view;
    BillDEntity BillDEntity = (BillDEntity) row.getTag();
    String _sP_NO = BillDEntity.getP_NO();
    String _sName = BillDEntity.getP_NAME();
    int _iOrgQty = (int) BillDEntity.getSL_QTY();
    Double _dOrgPrice = (Double) BillDEntity.getSL_PRICE();
    int _iOrgPrice = _dOrgPrice.intValue();

    final EditText edtQty = new EditText(this);

    edtQty.setSingleLine();
    edtQty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    edtQty.setText("" + _iOrgQty);
    edtQty.setSelectAllOnFocus(true);
    edtQty.setTextColor(getResources().getColor(R.color.black));

    builder.setView(edtQty);

    builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        String _sNewQty = String.valueOf(edtQty.getText());
        int _iNewQty = emisUtil.parseInt(_sNewQty);
        double _iNewAmt = _iNewQty * _iOrgPrice;

        BillDEntity.setSL_QTY(_iNewQty);
        BillDEntity.setSL_AMT(_iNewAmt);
        row.setTag(BillDEntity);

        updateTableRow(iRow);
      }
    });

    (createDialog("更新數量", builder)).show();
  }

  /**
   * 更新TableRow的內容. 不變更tag裡的BillDEntity.
   *
   * @param iRow
   */
  private void updateTableRow(int iRow) {
    // 因為用remove再用add, 原來順序會變，例如修改第1筆後, 第1筆會排到第2筆後面，
    // getChildAt(0)會取到第2筆而不是第1筆. 因此用loop來判斷 getId()
    //for (int i = 0, j = tableLayout.getChildCount(); i < j; i++) {
    View view = tableLayout.getChildAt(iRow);
    if (view instanceof TableRow) {
      TableRow row = (TableRow) view;
      //if (row.getId() == iCurrentRowId_) {
      BillDEntity BillDEntityInRow = (BillDEntity) row.getTag();
      String _sP_NO = (String) BillDEntityInRow.getP_NO();
      String _sP_NAME = (String) BillDEntityInRow.getP_NAME();
      int _iQty = (int) BillDEntityInRow.getSL_QTY();
      int _iAmt = ((Double) BillDEntityInRow.getSL_AMT()).intValue();
      int _iPrice = ((Double) BillDEntityInRow.getSL_PRICE()).intValue();

      TextView view1 = (TextView) row.getVirtualChildAt(_PART);
      view1 = (TextView) row.getVirtualChildAt(_AMT);
      view1.setText(_iQty + "X" + _iPrice + "\n=" + _iAmt);
      Button view2 = (Button) row.getVirtualChildAt(_CHANGE_QTY);
      view2.setTag(edtP_NO.getText().toString() + "," + this.edtQty.getText().toString());

      emisAndroidUtil.hideKeyboard(BillDetailsActivity.this);

      calcSalesDetail();
      //break;
      //}
    }
    //}
  }

  @OnClick(R.id.edtP_NO)
  public void onedtP_NOClicked() {
  }

  @OnClick(R.id.edtQty)
  public void onEdtQtyClicked() {
  }

  @OnClick(R.id.btnAdd)
  public void onBtnAddClicked() {
    String _sQty = edtQty.getText().toString();
    int _iQty = emisUtil.parseInt(_sQty) + 1;
    edtQty.setText("" + _iQty);
  }

  @OnClick(R.id.btnMinus)
  public void onBtnMinusClicked() {
    String _sQty = edtQty.getText().toString();
    int _iQty = emisUtil.parseInt(_sQty) - 1;
    if (_iQty <= 0) _iQty = 1;
    edtQty.setText("" + _iQty);
  }

  @OnClick(R.id.viewMinus)
  public void onViewMinusClicked() {
    String _sQty = edtQty.getText().toString();
    int _iQty = emisUtil.parseInt(_sQty) - 1;
    if (_iQty <= 0) _iQty = 1;
    edtQty.setText("" + _iQty);
  }

  @OnClick(R.id.btnPay)
  public void onBtnPayClicked() {
    lstSalesDetail_ = new ArrayList<>();
    for (int i = 0, j = tableLayout.getChildCount(); i < j; i++) {
      View view = tableLayout.getChildAt(i);
      if (view instanceof TableRow) {
        TableRow row = (TableRow) view;
        BillDEntity BillDEntityInRow = (BillDEntity) row.getTag();
        BillDEntityInRow.setRECNO(i + 1);  // TableRow可能有被刪除，因此原來的RECNO要重新取
        lstSalesDetail_.add(BillDEntityInRow);
      }
    }
    if (lstSalesDetail_.size() <= 0) {
      Toast.makeText(this, "請輸入商品！", Toast.LENGTH_SHORT).show();
      return;
    }
    /*Intent intent = new Intent();
    intent.setClass(BillDetailsActivity.this, PayActivity.class);
    emisKeeper.getInstance().setSaleH(saleHEntity);
    emisKeeper.getInstance().setBillD(lstSalesDetail_);
    startActivityForResult(intent, PAY_ACT);*/
  }

  private void toggleNavigation(boolean isEnable) {
    canLeave_ = isEnable;
  }

  @Override
  public void onBackPressed() {
    if (canLeave_) {
      super.onBackPressed();
      setResult(RESULT_OK, getIntent());
      finish();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_sales_sku, menu);
    return true;
  }

  @OnClick(R.id.btnScan)
  public void onBtnScanClicked() {
    Intent intent = new Intent(BillDetailsActivity.this, CaptureActivity.class);
    startActivityForResult(intent, SCAN_ACT_SUNMI);

    /*Intent test = new Intent();
    test.setClass(BillDetailsActivity.this, ScannerUtil.class);
    startActivityForResult(test, SCAN_ACT_SUNMI);*/
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case PAY_ACT:  // 按小計後由PayActivity返回
        if (resultCode == RESULT_OK) {
          edtP_NO.setText("");
          tableLayout.removeAllViews();
          Log.d("RESULT", sUserID_);
          Toast.makeText(BillDetailsActivity.this, "交易成功", Toast.LENGTH_SHORT).show();
          initSale();
          //emisKeeper.getInstance().setSaleH(saleHEntity);
          emisKeeper.getInstance().setBillD(lstSalesDetail_);
        } else if (resultCode == EXIT_NOW) {  // 重置時送出EXIT_NOW
          finish();
        } else {
          //finish();
        }
        break;
      case SCAN_ACT_SUNMI:  // 按Scanner後由ScanActivity返回
        if (resultCode == RESULT_OK) {
          Bundle bundle = data.getExtras();
          String P_NO = bundle.getString("P_NO");
          //String P_NO = bundle.getString("result");
          queryPartLocal(P_NO);
        }
        break;
      case SALES_CASH_ACT:
      case SALES_SKU_ACT:
        if (resultCode == RESULT_CANCELED) {  // 按了Back
        } else {
          //finish();
        }
        break;
    }
  }

  private void createContent() {
    View.OnTouchListener otl = new View.OnTouchListener() {
      public boolean onTouch(View v, MotionEvent event) {
        return true; // the listener has consumed the event
      }
    };

    tableLayout.removeAllViews();
    tableLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.table_border));
    tableLayout.setShrinkAllColumns(true);

    if (0 == Integer.valueOf(emisKeeper.getInstance().getsInfo("mchType", this.oContext_))) {
      helper = new KeyboardHelper(this, keyboardView);
      helper.setEditText(edtP_NO);
      helper.setCallBack(new KeyboardHelper.KeyboardCallBack() {
        @Override
        public void keyCall(int code) {
          //回调键盘监听，根据回调的code值进行处理
        }
      });

      edtP_NO.addTextChangedListener(new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
          edtP_NO.removeTextChangedListener(this);
          try {
            String originalString = s.toString();
            // if (canAutoSubmit_ && (originalString.length() == iP_NO_LEN_)) {
            if (originalString.length() == iP_NO_LEN_) {
              btnOKClicked();
            } else {
              // @comment 2020/07/24 Cody 輸入資料並不會觸發顯示系統鍵盤？但是刪除資料則會？
              // emisAndroidUtil.showKeyboard(edtP_NO);
            }
            // canAutoSubmit_ = true;
          } catch (NumberFormatException e) {
            e.printStackTrace();
          }
          edtP_NO.addTextChangedListener(this);
        }
      });

      edtP_NO.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
          //多条件判断，防止重复显示
          // keyboardView.setBackground(getDrawable(R.drawable.keyboard_base));
          if (edtP_NO.hasFocus() && !helper.isVisibility() && event.getAction() == MotionEvent.ACTION_DOWN) {

            helper.setEditText(edtP_NO);
            helper.show();
          }
          return false;
        }
      });

      edtP_NO.setOnFocusChangeListener(new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
          if (!hasFocus) {
            helper.setEditText(edtP_NO);
            helper.hide();
          } else {
            // keyboardView.setBackground(getDrawable(R.drawable.keyboard_base));
            helper.setEditText(edtP_NO);
            helper.show();

            KeyboardUtil.hideSoftInput(edtP_NO);
          }
        }
      });

      edtQty.setOnTouchListener(new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
          //多条件判断，防止重复显示
          keyboardView.setBackground(getDrawable(R.drawable.number_base));
          if (edtQty.hasFocus() && !helper.isVisibility() && event.getAction() == MotionEvent.ACTION_DOWN) {
            helper.setEditText(edtQty);
            helper.show();
          }
          return false;
        }
      });

      edtQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
          if (!hasFocus) {
            helper.setEditText(edtQty);
            helper.hide();
          } else {
            keyboardView.setBackground(getDrawable(R.drawable.number_base));
            helper.setEditText(edtQty);
            helper.show();
            KeyboardUtil.hideSoftInput(edtQty);
          }
        }
      });
    }

    // edtP_NO.setFilters(new InputFilter[] {new InputFilter.LengthFilter(iP_NO_LEN_)});
  }

  private void initSale() {
    lstSalesDetail_ = emisKeeper.getInstance().getBillD();
    if (lstSalesDetail_ != null) {
      int num = 0;

      for (Iterator<BillDEntity> iterator = lstSalesDetail_.iterator(); iterator.hasNext(); ) {
        BillDEntity BillDEntity = iterator.next();

        TableRow tr = addTableRow(num, BillDEntity);
        num++;
        tableLayout.addView(tr);
      }

      if (num != 0) calcSalesDetail();
    } else {
      lstSalesDetail_ = new ArrayList();
    }
  }
}