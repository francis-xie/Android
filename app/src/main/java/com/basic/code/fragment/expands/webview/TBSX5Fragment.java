
package com.basic.code.fragment.expands.webview;

import android.view.KeyEvent;

import androidx.fragment.app.Fragment;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.basic.page.annotation.Page;
import com.basic.page.base.PageFragment;
import com.basic.page.core.PageOption;
import com.basic.router.annotation.AutoWired;
import com.basic.router.launcher.Router;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;

/**

 * @since 2020/11/8 10:34 PM
 */
@Page(name = "腾讯X5浏览器")
public class TBSX5Fragment extends BaseFragment {

    public static final String KEY_URL = "com.basic.code.fragment.expands.webview.key_url";

    /**
     * 打开网页
     *
     * @param fragment 页面
     * @param url      打开的网页
     */
    public static Fragment openUrl(PageFragment fragment, String url) {
        return PageOption.to(TBSX5Fragment.class)
                .setNewActivity(true)
                .putString(KEY_URL, url)
                .open(fragment);
    }

    @BindView(R.id.webView)
    WebView webView;

    @AutoWired(name = KEY_URL)
    String url;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tbs_x5;
    }

    @Override
    protected void initArgs() {
        Router.getInstance().inject(this);
    }

    @Override
    protected void initViews() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView var1, int var2, String var3, String var4) {
                XToastUtils.error("网页加载失败");
            }
        });
        //进度条
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });
        webView.loadUrl(url);
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
