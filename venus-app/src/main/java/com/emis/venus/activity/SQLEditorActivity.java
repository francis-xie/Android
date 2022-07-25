package com.emis.venus.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.emis.venus.R;
import com.emis.venus.db.emisDb;
import com.emis.venus.db.emisSQLiteWrapper;
import com.emis.venus.util.LoadingDialogUtil;
import com.emis.venus.util.emisLog;

public class SQLEditorActivity extends BaseActivity {

  @BindView(R.id.edtSQLContent)
  EditText edtSQLContent;
  // @BindView(R.id.tableLayoutQueryResult) TableLayout tableLayoutQueryResult;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sql_editor);

    ButterKnife.bind(this);

    oContext_ = SQLEditorActivity.this;
  }

  @Override
  protected void onResume() {
    super.onResume();

    edtSQLContent.requestFocus();
    // edtSQLContent.setText("select * from part");
    edtSQLContent.setText("select * from store");
  }

  @OnClick(R.id.btnClear)
  protected void btnClearOnClick(View view) {
    edtSQLContent.setText("");
  }

  @OnClick(R.id.btnExecuteQuery)
  protected void btnExecuteQueryOnClick(View view) {
    // InputMethodManager imm = (InputMethodManager) getSystemService(oContext_.INPUT_METHOD_SERVICE);

    // 隱藏指定 view 的虛擬鍵盤, 通常會是 EditText
    ((InputMethodManager) getSystemService(oContext_.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);

    LoadingDialog = LoadingDialogUtil.createLoadingDialog(SQLEditorActivity.this, "資料查詢中...");  // 無法顯示？！

    runOnUiThread(new Runnable() {

      @Override
      public void run() {
        Message message = new Message();

        SQLiteCursor cursor;

        TableLayout.LayoutParams rowLayout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams viewLayout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        TableRow tr;
        TextView textViewCol;
        View view;

        int index;
        try {
          ((TableLayout) findViewById(R.id.tableLayoutHeaderRow)).removeAllViewsInLayout();
          ((TableLayout) findViewById(R.id.tableLayoutDataRow)).removeAllViewsInLayout();

          cursor = ((emisSQLiteWrapper) emisDb.getInstance()).executeQuery(edtSQLContent.getText().toString());

          // Column Name
//          tr = new TableRow(SQLEditorActivity.this);
//          tr.setLayoutParams(rowLayout);
//          tr.setGravity(Gravity.CENTER_HORIZONTAL);
//
//          for (index = 0; index < cursor.getColumnCount(); index++) {
//
//            textViewCol = new TextView(SQLEditorActivity.this);
//            textViewCol.setText(cursor.getColumnName(index));
//            textViewCol.setLayoutParams(viewLayout);
//            textViewCol.setBackgroundResource(R.drawable.sql_editor_query_textview);
//            textViewCol.setPadding(5, 5, 5, 5);
//            tr.addView(textViewCol);
//          }
//
//          ((TableLayout) findViewById(R.id.tableLayoutHeaderRow)).addView(tr);

          // Column Name
          tr = new TableRow(SQLEditorActivity.this);
          tr.setLayoutParams(rowLayout);
          tr.setGravity(Gravity.CENTER_HORIZONTAL);

          for (index = 0; index < cursor.getColumnCount(); index++) {
            textViewCol = new TextView(SQLEditorActivity.this);
            textViewCol.setText(cursor.getColumnName(index));
            textViewCol.setLayoutParams(viewLayout);
            textViewCol.setBackgroundResource(R.drawable.sql_editor_query_textview);
            textViewCol.setPadding(5, 5, 5, 5);
            tr.addView(textViewCol);
          }

          ((TableLayout) findViewById(R.id.tableLayoutDataRow)).addView(tr);

          // Column Data
          while (cursor.moveToNext()) {
            tr = new TableRow(SQLEditorActivity.this);
            tr.setLayoutParams(rowLayout);
            tr.setGravity(Gravity.CENTER_HORIZONTAL);

            for (index = 0; index < cursor.getColumnCount(); index++) {
              textViewCol = new TextView(SQLEditorActivity.this);
              textViewCol.setText(cursor.getString(index));
              textViewCol.setLayoutParams(viewLayout);
              textViewCol.setBackgroundResource(R.drawable.sql_editor_query_textview);
              textViewCol.setPadding(5, 5, 5, 5);
              textViewCol.setGravity(Gravity.LEFT);

              if (cursor.getType(index) != Cursor.FIELD_TYPE_STRING) textViewCol.setGravity(Gravity.RIGHT);

              tr.addView(textViewCol);
            }

            // tableLayoutQueryResult.addView(tr);
            ((TableLayout) findViewById(R.id.tableLayoutDataRow)).addView(tr);
          }

          // TableRow row = (TableRow) ((TableLayout) findViewById(R.id.tableLayoutDataRow)).getChildAt(0);

          // ((TableLayout) findViewById(R.id.tableLayoutDataRow)).removeView(row);
          // ((TableLayout) findViewById(R.id.tableLayoutHeaderRow)).addView(row);

          // System.out.println("headerRow Child 0 width:" + ((TableRow) ((TableLayout) findViewById(R.id.tableLayoutHeaderRow)).getChildAt(0)).getChildAt(0).getWidth());
          // System.out.println("dataRow Child 0 width:" + ((TableRow) ((TableLayout) findViewById(R.id.tableLayoutHeaderRow)).getChildAt(0)).getChildAt(0).getWidth());

          // ((TableRow) ((TableLayout) findViewById(R.id.tableLayoutDataRow)).getChildAt(0)).getChildAt(0).measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
          // View view = ((TableRow) ((TableLayout) findViewById(R.id.tableLayoutDataRow)).getChildAt(0)).getChildAt(0);
          // view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

          // System.out.println("dataRow Child 0 width:" + view.getMeasuredWidth());

          // Column Name
          tr = new TableRow(SQLEditorActivity.this);
          // tr.setLayoutParams(rowLayout);
          tr.setGravity(Gravity.CENTER_HORIZONTAL);

          for (index = 0; index < cursor.getColumnCount(); index++) {
            view = ((TableRow) ((TableLayout) findViewById(R.id.tableLayoutDataRow)).getChildAt(1)).getChildAt(index);
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            // System.out.println("dataRow child " + index + " MeasuredWidth:" + view.getMeasuredWidth() + ", Width:" + view.getWidth());

            TableRow.LayoutParams params = new TableRow.LayoutParams(view.getMeasuredWidth(), LayoutParams.MATCH_PARENT);
            // params.setMargins(2, 2, 0, 0);

            viewLayout = new TableRow.LayoutParams(view.getMeasuredWidth(), LayoutParams.WRAP_CONTENT);

            textViewCol = new TextView(SQLEditorActivity.this);
            textViewCol.setText(cursor.getColumnName(index));
            textViewCol.setLayoutParams(viewLayout);
            // textViewCol.setWidth(view.getMeasuredWidth());
            // textViewCol.setWidth(view.getMeasuredWidth());
            textViewCol.setBackgroundResource(R.drawable.sql_editor_query_textview);
            textViewCol.setPadding(5, 5, 5, 5);

            // tr.addView(textViewCol);
            tr.addView(textViewCol, params);
          }

          // ((TableLayout) findViewById(R.id.tableLayoutHeaderRow)).addView(tr);

          runOnUiThread(new Runnable() {

            @Override
            public void run() {
              message.what = 1;
              mHandler.dispatchMessage(message);

              Toast.makeText(getApplicationContext(), "資料查詢結束！", Toast.LENGTH_SHORT).show();
            }
          });
        } catch (Exception e) {
          // LoadingDialogUtil.closeDialog(LoadingDialog);
          message.what = 1;
          mHandler.dispatchMessage(message);
          e.printStackTrace();
          //Looper.prepare();
          Toast.makeText(getApplicationContext(), "查詢資料過程有誤，錯誤訊息：" + e.getMessage(), Toast.LENGTH_SHORT).show();
          //Looper.loop();
        }
      }
    });
  }

  @OnClick(R.id.btnExecuteUpdate)
  protected void btnExecuteUpdateOnClick(View view) {
    // SQLiteStatement stmt;
    try {
      // stmt = ((emisSQLiteWrapper) emisDb.getInstance()).compileStmt(edtSQLContent.getText().toString());

      // ((SQLiteStatement) ((emisSQLiteWrapper) emisDb.getInstance()).compileStmt(edtSQLContent.getText().toString())).execute();

      ((emisSQLiteWrapper) emisDb.getInstance()).execSQL(edtSQLContent.getText().toString());

      Toast.makeText(getApplicationContext(), "資料處理完成。", Toast.LENGTH_SHORT).show();
    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(getApplicationContext(), "資料處理過程有誤，錯誤訊息：" + e.getMessage(), Toast.LENGTH_SHORT).show();
      emisLog.addLog("資料處理過程有誤，錯誤訊息：", e);
    }
  }

  private Runnable createQueryResult = new Runnable() {

    @Override
    public void run() {
      Message message = new Message();

      SQLiteCursor cursor;

      TableLayout.LayoutParams rowLayout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      TableRow.LayoutParams viewLayout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

      TableRow tr;
      TextView textViewCol;

      int index;
      int count = 0;
      try {
        Looper.prepare();
        cursor = ((emisSQLiteWrapper) emisDb.getInstance()).executeQuery(edtSQLContent.getText().toString());

        // Column Name
        tr = new TableRow(SQLEditorActivity.this);
        for (index = 0; index < cursor.getColumnCount(); index++) {
          textViewCol = new TextView(SQLEditorActivity.this);
          textViewCol.setText(cursor.getColumnName(index));
          textViewCol.setLayoutParams(viewLayout);
          textViewCol.setBackgroundResource(R.drawable.sql_editor_query_textview);
          textViewCol.setPadding(5, 5, 5, 5);
          tr.addView(textViewCol);
        }
        tr.setLayoutParams(rowLayout);
        tr.setGravity(Gravity.CENTER_HORIZONTAL);

        // tableLayoutQueryResult.addView(tr);
        ((TableLayout) findViewById(R.id.tableLayoutHeaderRow)).addView(tr);

        // Column Data
        while (cursor.moveToNext()) {

          tr = new TableRow(SQLEditorActivity.this);
          for (index = 0; index < cursor.getColumnCount(); index++) {
            textViewCol = new TextView(SQLEditorActivity.this);
            textViewCol.setText(cursor.getString(index));
            textViewCol.setLayoutParams(viewLayout);
            textViewCol.setBackgroundResource(R.drawable.sql_editor_query_textview);
            textViewCol.setPadding(5, 5, 5, 5);
            textViewCol.setGravity(Gravity.LEFT);

            if (cursor.getType(index) != Cursor.FIELD_TYPE_STRING) textViewCol.setGravity(Gravity.RIGHT);

            tr.addView(textViewCol);
          }

          tr.setLayoutParams(rowLayout);
          tr.setGravity(Gravity.CENTER_HORIZONTAL);

          // tableLayoutQueryResult.addView(tr);
          System.out.println(count++);
        }
        Looper.loop();

        message.what = 1;
        mHandler.dispatchMessage(message);
      } catch (Exception e) {
        // LoadingDialogUtil.closeDialog(LoadingDialog);
        message.what = 1;
        mHandler.dispatchMessage(message);
        e.printStackTrace();
        Toast.makeText(getApplicationContext(), "查詢資料過程有誤，錯誤訊息：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        emisLog.addLog("查詢資料過程有誤，錯誤訊息：", e);
      }
    }
  };
}
