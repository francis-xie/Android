
package com.basic.code.utils.update;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basic.face.widget.dialog.DialogLoader;
import com.basic.code.utils.Utils;
import com.basic.renew.Update;

/**
 * 版本更新提示弹窗
 */
public class UpdateTipDialog extends AppCompatActivity implements DialogInterface.OnDismissListener {

    public static final String KEY_CONTENT = "com.basic.code.utils.update.KEY_CONTENT";

    /**
     * 显示版本更新重试提示弹窗
     *
     * @param content
     */
    public static void show(String content) {
        Intent intent = new Intent(Update.getContext(), UpdateTipDialog.class);
        intent.putExtra(KEY_CONTENT, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Update.getContext().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String content = getIntent().getStringExtra(KEY_CONTENT);
        if (TextUtils.isEmpty(content)) {
            content = "Github下载速度太慢了，是否考虑切换蒲公英下载[密码:zhiqiang]？";
        }

        DialogLoader.getInstance().showConfirmDialog(this, content, "是", (dialog, which) -> {
            dialog.dismiss();
            Utils.goWeb(UpdateTipDialog.this, "https://www.pgyer.com/");
        }, "否")
                .setOnDismissListener(this);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        finish();
    }
}
