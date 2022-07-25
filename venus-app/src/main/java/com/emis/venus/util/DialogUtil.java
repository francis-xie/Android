package com.emis.venus.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.emis.venus.R;

public class DialogUtil {

  private static void setTitle(String title, AlertDialog.Builder builder) {
    View titleView = LayoutInflater.from(builder.getContext()).inflate(R.layout.title_bar, null);

    ((TextView) titleView.findViewById(R.id.txtTitle)).setText(title);
    builder.setCustomTitle(titleView);
  }

  private static void setButton(AlertDialog dialog) {
    Button btn;
    ViewGroup.MarginLayoutParams params;

    if (dialog.getButton(DialogInterface.BUTTON_POSITIVE) != null) {
      dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(dialog.getContext().getResources().getColor(R.color.black));
      dialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(dialog.getContext().getResources().getColor(R.color.orange));

      btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

      params = (ViewGroup.MarginLayoutParams) btn.getLayoutParams();
      params.width = 145;

      btn.setLayoutParams(params);
    }

    if (dialog.getButton(DialogInterface.BUTTON_NEGATIVE) != null) {
      dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(dialog.getContext().getResources().getColor(R.color.black));
      dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(dialog.getContext().getResources().getColor(R.color.orange));

      btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

      params = (ViewGroup.MarginLayoutParams) btn.getLayoutParams();
      params.width = 145;

      btn.setLayoutParams(params);
    }

    if ((dialog.getButton(DialogInterface.BUTTON_POSITIVE) != null) && (dialog.getButton(DialogInterface.BUTTON_NEGATIVE) != null)) {
      btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
      if (btn != null) {
        params = (ViewGroup.MarginLayoutParams) btn.getLayoutParams();
        // params.topMargin = yourValue;
        // params.bottomMargin = yourValue;
        params.leftMargin = 180;
        params.rightMargin = 50;

        btn.setLayoutParams(params);
      }
    }
  }

  public static AlertDialog createDialog(String title, AlertDialog.Builder builder) {
    AlertDialog result = null;

    setTitle(title, builder);

    result = builder.show();

    setButton(result);

    return result;
  }

  public static AlertDialog createDialog(String title, String message, AlertDialog.Builder builder) {
    AlertDialog result = null;

    // View titleView = LayoutInflater.from(builder.getContext()).inflate(R.layout.title_bar, null);

    // TextView txtMessage = (TextView) result.findViewById(android.R.id.message);

    // txtMessage.setTextColor(builder.getContext().getResources().getColor(R.color.black));

    // result.setCancelable(true); // 是否可以按“返回键”消失
    // result.setCanceledOnTouchOutside(true);  // 点击加载框以外的区域

    // ((TextView) titleView.findViewById(R.id.txtTitle)).setText(title);
    // builder.setCustomTitle(titleView);

    builder.setMessage(message);

    result = createDialog(title, builder);

    // result = builder.show();

    ((TextView) result.findViewById(android.R.id.message)).setTextColor(builder.getContext().getResources().getColor(R.color.black));

    setButton(result);

    return result;
  }
}
