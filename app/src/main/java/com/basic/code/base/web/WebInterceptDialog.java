
package com.basic.code.base.web;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basic.face.utils.ResUtils;
import com.basic.face.widget.dialog.DialogLoader;
import com.basic.code.R;
import com.basic.code.utils.ToastUtils;
import com.basic.tools.Util;
import com.basic.tools.app.ActivityUtils;

import java.net.URISyntaxException;

/**
 * WebView拦截提示
 */
public class WebInterceptDialog extends AppCompatActivity implements DialogInterface.OnDismissListener {

    private static final String KEY_INTERCEPT_URL = "key_intercept_url";
    public static final String APP_LINK_HOST = "zhiqiang.club";
    public static final String APP_LINK_ACTION = "com.basic.face.applink";

    /**
     * 显示WebView拦截提示
     *
     * @param url
     */
    public static void show(String url) {
        ActivityUtils.startActivity(WebInterceptDialog.class, KEY_INTERCEPT_URL, url);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra(KEY_INTERCEPT_URL);

        DialogLoader.getInstance().showConfirmDialog(
                this,
                getOpenTitle(url),
                ResUtils.getString(R.string.lab_yes),
                (dialog, which) -> {
                    dialog.dismiss();
                    if (isAppLink(url)) {
                        openAppLink(this, url);
                    } else {
                        openApp(url);
                    }
                },
                ResUtils.getString(R.string.lab_no),
                (dialog, which) -> dialog.dismiss()
        ).setOnDismissListener(this);

    }

    private String getOpenTitle(String url) {
        String scheme = getScheme(url);
        if ("mqqopensdkapi".equals(scheme)) {
            return "是否允许页面打开\"QQ\"?";
        } else {
            return ResUtils.getString(R.string.lab_open_third_app);
        }
    }

    private String getScheme(String url) {
        try {
            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            return intent.getScheme();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean isAppLink(String url) {
        Uri uri = Uri.parse(url);
        return uri != null
                && APP_LINK_HOST.equals(uri.getHost())
                && (url.startsWith("http") || url.startsWith("https"));
    }


    private void openApp(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Util.getContext().startActivity(intent);
        } catch (Exception e) {
            ToastUtils.toast("您所打开的第三方App未安装！");
        }
    }

    private void openAppLink(Context context, String url) {
        try {
            Intent intent = new Intent(APP_LINK_ACTION);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.toast("您所打开的第三方App未安装！");
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        finish();
    }
}
