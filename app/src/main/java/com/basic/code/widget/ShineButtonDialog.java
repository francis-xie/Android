
package com.basic.code.widget;

import android.content.Context;

import com.basic.face.widget.button.shinebutton.ShineButton;
import com.basic.face.widget.dialog.materialdialog.CustomMaterialDialog;
import com.basic.face.widget.dialog.materialdialog.MaterialDialog;
import com.basic.code.R;

public class ShineButtonDialog extends CustomMaterialDialog {
    ShineButton shineButton;
    ShineButton shineButton1;
    ShineButton shineButton2;
    ShineButton shineButton3;

    /**
     * 构造窗体
     *
     * @param context
     */
    public ShineButtonDialog(Context context) {
        super(context);
    }

    @Override
    protected MaterialDialog.Builder getDialogBuilder(Context context) {
        return new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_shine_button, true)
                .title("自定义对话框")
                .positiveText(R.string.lab_submit)
                .negativeText(R.string.lab_cancel);
    }

    @Override
    protected void initViews(Context context) {
        shineButton = findViewById(R.id.shine_button);
        shineButton1 = findViewById(R.id.shine_button_1);
        shineButton2 = findViewById(R.id.shine_button_2);
        shineButton3 = findViewById(R.id.shine_button_3);

        //修复在对话框中的显示问题
        shineButton.fitDialog(mDialog);
        shineButton1.fitDialog(mDialog);
        shineButton2.fitDialog(mDialog);
        shineButton3.fitDialog(mDialog);
    }
}
