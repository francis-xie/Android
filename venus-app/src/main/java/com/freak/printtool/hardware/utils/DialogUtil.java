package com.freak.printtool.hardware.utils;



import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.freak.printtool.hardware.dialog.ToastDialogFragment;

public class DialogUtil {
    /**
     * 温馨提示弹窗
     * @param activity
     */
    public static void showToastDialog(AppCompatActivity activity, String title, String context, String cancel) {
        ToastDialogFragment dialogFragment = new ToastDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("context", context);
        args.putString("cancel", cancel);
        dialogFragment.setArguments(args);
        dialogFragment.setCancelable(false);
        dialogFragment.show(activity.getSupportFragmentManager(), "");
    }
}
