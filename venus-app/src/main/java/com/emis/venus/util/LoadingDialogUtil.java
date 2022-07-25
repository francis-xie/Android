package com.emis.venus.util;

import android.app.Dialog;
import android.content.Context;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.emis.venus.R;

public class LoadingDialogUtil {

  public static Dialog createLoadingDialog(Context context, String msg) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
    LinearLayout layout = (LinearLayout) v
      .findViewById(R.id.dialog_loading_view);// 加载布局
    TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
    tipTextView.setText(msg);// 设置加载信息

    Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
    loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
    loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
    loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
    /**
     *将显示Dialog的方法封装在这里面
     */
    Window window = loadingDialog.getWindow();
    WindowManager.LayoutParams lp = window.getAttributes();
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    window.setGravity(Gravity.CENTER);
    window.setAttributes(lp);
    window.setWindowAnimations(R.style.PopWindowAnimStyle);
    loadingDialog.show();

    return loadingDialog;
  }

  /**
   * 关闭dialog
   *
   * @param mDialogUtils
   */
  public static void closeDialog(Dialog mDialogUtils) {
    if (mDialogUtils != null && mDialogUtils.isShowing()) {
      mDialogUtils.dismiss();
    }
  }

}
