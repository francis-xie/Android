
package com.basic.code.base.webview;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.basic.web.action.PermissionInterceptor;
import com.basic.web.core.Web;
import com.basic.web.core.client.DefaultWebClient;
import com.basic.web.core.client.MiddlewareWebChromeBase;
import com.basic.web.core.client.MiddlewareWebClientBase;
import com.basic.web.core.client.WebListenerManager;
import com.basic.web.core.web.AbsWebSettings;
import com.basic.web.core.web.WebConfig;
import com.basic.web.core.web.IWebSettings;
import com.basic.web.download.WebDownloader;
import com.basic.web.download.DefaultDownloadImpl;
import com.basic.web.download.DownloadListenerAdapter;
import com.basic.web.download.DownloadingService;
import com.basic.web.widget.IWebLayout;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.page.base.PageActivity;
import com.basic.page.base.PageFragment;
import com.basic.page.core.PageOption;
import com.basic.face.utils.DrawableUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;
import com.basic.code.utils.XToastUtils;
import com.basic.tools.common.logger.Logger;
import com.basic.tools.net.JsonUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

import static com.basic.code.base.webview.WebFragment.KEY_URL;

/**
 * 使用PageFragment
 */
@Page(params = {KEY_URL})
public class PageWebViewFragment extends BaseFragment {

    @BindView(R.id.iv_back)
    AppCompatImageView mIvBack;
    @BindView(R.id.view_line)
    View mLineView;
    @BindView(R.id.toolbar_title)
    TextView mTvTitle;

    protected Web mWeb;
    private PopupMenu mPopupMenu;

    private DownloadingService mDownloadingService;

    /**
     * 打开网页
     *
     * @param pageActivity
     * @param url
     * @return
     */
    public static Fragment openUrl(PageActivity pageActivity, String url) {
        return PageOption.to(PageWebViewFragment.class)
                .putString(KEY_URL, url)
                .open(pageActivity);
    }

    /**
     * 打开网页
     *
     * @param fragment
     * @param url
     * @return
     */
    public static Fragment openUrl(PageFragment fragment, String url) {
        return PageOption.to(PageWebViewFragment.class)
                .setNewActivity(true)
                .putString(KEY_URL, url)
                .open(fragment);
    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_web;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mWeb = Web.with(this)
                //传入Web的父控件。
                .setWebParent((LinearLayout) getRootView(), -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                //设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .useDefaultIndicator(-1, 3)
                //设置 IWebSettings。
                .setWebWebSettings(getSettings())
                //WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖Web DefaultWebClient,同时相应的中间件也会失效。
                .setWebViewClient(mWebViewClient)
                //WebChromeClient
                .setWebChromeClient(mWebChromeClient)
                //设置WebChromeClient中间件，支持多个WebChromeClient，Web 3.0.0 加入。
                .useMiddlewareWebChrome(getMiddlewareWebChrome())
                //设置WebViewClient中间件，支持多个WebViewClient， Web 3.0.0 加入。
                .useMiddlewareWebClient(getMiddlewareWebClient())
                //权限拦截 2.0.0 加入。
                .setPermissionInterceptor(mPermissionInterceptor)
                //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用WebView没影响。
                .setSecurityType(Web.SecurityType.STRICT_CHECK)
                //自定义UI  Web3.0.0 加入。
                .setWebUIController(new UIController(getActivity()))
                //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， Web 3.0.0 加入。
                .setMainFrameErrorView(R.layout.web_error_page, -1)
                .setWebLayout(getWebLayout())
                //打开其他页面时，弹窗质询用户前往其他应用 Web 3.0.0 加入。
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
                //拦截找不到相关页面的Url Web 3.0.0 加入。
                .interceptUnkownUrl()
                //创建Web。
                .createWeb()
                .ready()//设置 WebSettings。
                //WebView载入该url地址的页面并显示。
                .go(getUrl());

        WebConfig.debug();

        pageNavigator(View.GONE);
        // 得到 Web 最底层的控件
        addBackgroundChild(mWeb.getWebCreator().getWebParentLayout());

        // Web 没有把WebView的功能全面覆盖 ，所以某些设置 Web 没有提供，请从WebView方面入手设置。
        mWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
    }

    protected IWebLayout getWebLayout() {
        return new WebLayout(getActivity());
    }

    protected void addBackgroundChild(FrameLayout frameLayout) {
        TextView textView = new TextView(frameLayout.getContext());
        textView.setText("技术由 Web 提供");
        textView.setTextSize(16);
        textView.setTextColor(Color.parseColor("#727779"));
        frameLayout.setBackgroundColor(Color.parseColor("#272b2d"));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        final float scale = frameLayout.getContext().getResources().getDisplayMetrics().density;
        params.topMargin = (int) (15 * scale + 0.5f);
        frameLayout.addView(textView, 0, params);
    }


    private void pageNavigator(int tag) {
        //返回的导航按钮
        mIvBack.setVisibility(tag);
        mLineView.setVisibility(tag);
    }

    @SingleClick
    @OnClick({R.id.iv_back, R.id.iv_finish, R.id.iv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                // true表示Web处理了该事件
                if (!mWeb.back()) {
                    popToBack();
                }
                break;
            case R.id.iv_finish:
                popToBack();
                break;
            case R.id.iv_more:
                showPoPup(view);
                break;
            default:
                break;
        }
    }

    //=====================下载============================//

    /**
     * 更新于 Web 4.0.0，下载监听
     */
    protected DownloadListenerAdapter mDownloadListenerAdapter = new DownloadListenerAdapter() {
        /**
         *
         * @param url                下载链接
         * @param userAgent          UserAgent
         * @param contentDisposition ContentDisposition
         * @param mimeType           资源的媒体类型
         * @param contentLength      文件长度
         * @param extra              下载配置 ， 用户可以通过 Extra 修改下载icon ， 关闭进度条 ， 是否强制下载。
         * @return true 表示用户处理了该下载事件 ， false 交给 Web 下载
         */
        @Override
        public boolean onStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength, WebDownloader.Extra extra) {
            Logger.i("onStart:" + url);
            // 是否开启断点续传
            extra.setOpenBreakPointDownload(true)
                    //下载通知的icon
                    .setIcon(R.drawable.ic_file_download_black_24dp)
                    // 连接的超时时间
                    .setConnectTimeOut(6000)
                    // 以8KB位单位，默认60s ，如果60s内无法从网络流中读满8KB数据，则抛出异常
                    .setBlockMaxTime(10 * 60 * 1000)
                    // 下载的超时时间
                    .setDownloadTimeOut(Long.MAX_VALUE)
                    // 串行下载更节省资源哦
                    .setParallelDownload(false)
                    // false 关闭进度通知
                    .setEnableIndicator(true)
                    // 自定义请求头
                    .addHeader("Cookie", "xx")
                    // 下载完成自动打开
                    .setAutoOpen(true)
                    // 强制下载，不管网络网络类型
                    .setForceDownload(true);
            return false;
        }

        /**
         *
         * 不需要暂停或者停止下载该方法可以不必实现
         * @param url
         * @param downloadingService  用户可以通过 DownloadingService#shutdownNow 终止下载
         */
        @Override
        public void onBindService(String url, DownloadingService downloadingService) {
            super.onBindService(url, downloadingService);
            mDownloadingService = downloadingService;
            Logger.i("onBindService:" + url + "  DownloadingService:" + downloadingService);
        }

        /**
         * 回调onUnbindService方法，让用户释放掉 DownloadingService。
         * @param url
         * @param downloadingService
         */
        @Override
        public void onUnbindService(String url, DownloadingService downloadingService) {
            super.onUnbindService(url, downloadingService);
            mDownloadingService = null;
            Logger.i("onUnbindService:" + url);
        }

        /**
         *
         * @param url  下载链接
         * @param loaded  已经下载的长度
         * @param length    文件的总大小
         * @param usedTime   耗时 ，单位ms
         * 注意该方法回调在子线程 ，线程名 AsyncTask #XX 或者 Web # XX
         */
        @Override
        public void onProgress(String url, long loaded, long length, long usedTime) {
            int mProgress = (int) ((loaded) / (float) length * 100);
            Logger.i("onProgress:" + mProgress);
            super.onProgress(url, loaded, length, usedTime);
        }

        /**
         *
         * @param path 文件的绝对路径
         * @param url  下载地址
         * @param throwable    如果异常，返回给用户异常
         * @return true 表示用户处理了下载完成后续的事件 ，false 默认交给Web 处理
         */
        @Override
        public boolean onResult(String path, String url, Throwable throwable) {
            //下载成功
            if (null == throwable) {
                //do you work
            } else {//下载失败

            }
            // true  不会发出下载完成的通知 , 或者打开文件
            return false;
        }
    };

    /**
     * 下载服务设置
     *
     * @return IWebSettings
     */
    public IWebSettings getSettings() {
        return new AbsWebSettings() {
            private Web mWeb;

            @Override
            protected void bindWebSupport(Web web) {
                this.mWeb = web;
            }

            /**
             * Web 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 Web Download 部分 ， 请依赖上 compile 'com.basic.web:download:4.0.0 ，
             * 如果你需要监听下载结果，请自定义 WebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            @Override
            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
                return super.setDownloader(webView,
                        DefaultDownloadImpl
                                .create(getActivity(),
                                        webView,
                                        mDownloadListenerAdapter,
                                        mDownloadListenerAdapter,
                                        mWeb.getPermissionInterceptor()));
            }
        };
    }

    //===================WebChromeClient 和 WebViewClient===========================//

    /**
     * 页面空白，请检查scheme是否加上， scheme://host:port/path?query&query 。
     *
     * @return mUrl
     */
    public String getUrl() {
        String target = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            target = bundle.getString(KEY_URL);
        }

        if (TextUtils.isEmpty(target)) {
            target = "https://github.com/zhiqiang";
        }
        return target;
    }

    /**
     * 和浏览器相关，包括和JS的交互
     */
    protected WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            //网页加载进度
        }
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mTvTitle != null && !TextUtils.isEmpty(title)) {
                if (title.length() > 10) {
                    title = title.substring(0, 10).concat("...");
                }
                mTvTitle.setText(title);
            }
        }
    };

    /**
     * 和网页url加载相关，统计加载时间
     */
    protected WebViewClient mWebViewClient = new WebViewClient() {
        private HashMap<String, Long> mTimer = new HashMap<>();

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl() + "");
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            //intent:// scheme的处理 如果返回false ， 则交给 DefaultWebClient 处理 ， 默认会打开该Activity  ， 如果Activity不存在则跳到应用市场上去.  true 表示拦截
            //例如优酷视频播放 ，intent://play?...package=com.youku.phone;end;
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
            return url.startsWith("intent://") && url.contains("com.youku.phone");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mTimer.put(url, System.currentTimeMillis());
            if (url.equals(getUrl())) {
                pageNavigator(View.GONE);
            } else {
                pageNavigator(View.VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Long startTime = mTimer.get(url);
            if (startTime != null) {
                long overTime = System.currentTimeMillis();
                //统计页面的使用时长
                Logger.i(" page mUrl:" + url + "  used time:" + (overTime - startTime));
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    };

    //=====================菜单========================//

    /**
     * 显示更多菜单
     *
     * @param view 菜单依附在该View下面
     */
    private void showPoPup(View view) {
        if (mPopupMenu == null) {
            mPopupMenu = new PopupMenu(getContext(), view);
            mPopupMenu.inflate(R.menu.menu_toolbar_web);
            mPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);
        }
        mPopupMenu.show();
    }

    /**
     * 菜单事件
     */
    private PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.refresh:
                    if (mWeb != null) {
                        mWeb.getUrlLoader().reload(); // 刷新
                    }
                    return true;
                case R.id.copy:
                    if (mWeb != null) {
                        toCopy(getContext(), mWeb.getWebCreator().getWebView().getUrl());
                    }
                    return true;
                case R.id.default_browser:
                    if (mWeb != null) {
                        openBrowser(mWeb.getWebCreator().getWebView().getUrl());
                    }
                    return true;
                case R.id.share:
                    if (mWeb != null) {
                        shareWebUrl(mWeb.getWebCreator().getWebView().getUrl());
                    }
                    return true;
                case R.id.capture:
                    if (mWeb != null) {
                        captureWebView();
                    }
                    return true;
                case R.id.default_clean:
                    toCleanWebCache();
                    return true;
                default:
                    return false;
            }

        }
    };

    /**
     * 打开浏览器
     *
     * @param targetUrl 外部浏览器打开的地址
     */
    private void openBrowser(String targetUrl) {
        if (TextUtils.isEmpty(targetUrl) || targetUrl.startsWith("file://")) {
            XToastUtils.toast(targetUrl + " 该链接无法使用浏览器打开。");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(targetUrl);
        intent.setData(uri);
        startActivity(intent);
    }

    /**
     * 分享网页链接
     *
     * @param url 网页链接
     */
    private void shareWebUrl(String url) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }


    /**
     * 网页截图保存
     */
    private void captureWebView() {
        //简单的截取当前网页可见的内容
//        Utils.showCaptureBitmap(mWeb.getWebCreator().getWebView());

        //网页长截图

        Utils.showCaptureBitmap(getContext(), DrawableUtils.createBitmapFromWebView(mWeb.getWebCreator().getWebView()));
    }

    /**
     * 清除 WebView 缓存
     */
    private void toCleanWebCache() {
        if (mWeb != null) {
            //清理所有跟WebView相关的缓存 ，数据库， 历史记录 等。
            mWeb.clearWebCache();
            XToastUtils.toast("已清理缓存");
            //清空所有 Web 硬盘缓存，包括 WebView 的缓存 , Web 下载的图片 ，视频 ，apk 等文件。
//            WebConfig.clearDiskCache(this.getContext());
        }

    }


    /**
     * 复制字符串
     *
     * @param context
     * @param text
     */
    private void toCopy(Context context, String text) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager == null) {
            return;
        }
        manager.setPrimaryClip(ClipData.newPlainText(null, text));
    }

    //===================生命周期管理===========================//

    @Override
    public void onResume() {
        if (mWeb != null) {
            mWeb.getWebLifeCycle().onResume();//恢复
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mWeb != null) {
            mWeb.getWebLifeCycle().onPause(); //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mWeb.getWebLifeCycle().onResume(); 恢复。
        }
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mWeb != null && mWeb.handleKeyEvent(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        if (mWeb != null) {
            mWeb.destroy();
        }
        super.onDestroyView();
    }


    //===================中间键===========================//


    /**
     * MiddlewareWebClientBase 是 Web 3.0.0 提供一个强大的功能，
     * 如果用户需要使用 Web 提供的功能， 不想重写 WebClientView方
     * 法覆盖Web提供的功能，那么 MiddlewareWebClientBase 是一个
     * 不错的选择 。
     *
     * @return
     */
    protected MiddlewareWebClientBase getMiddlewareWebClient() {
        return new MiddlewareWebViewClient() {
            /**
             *
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 拦截 url，不执行 DefaultWebClient#shouldOverrideUrlLoading
                if (url.startsWith("web")) {
                    return true;
                }
                // 执行 DefaultWebClient#shouldOverrideUrlLoading
                return super.shouldOverrideUrlLoading(view, url);
                // do you work
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        };
    }

    protected MiddlewareWebChromeBase getMiddlewareWebChrome() {
        return new MiddlewareChromeClient() {
        };
    }

    /**
     * 权限申请拦截器
     */
    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {
        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * @param url
         * @param permissions
         * @param action
         * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
         */
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            Logger.i("mUrl:" + url + "  permission:" + JsonUtil.toJson(permissions) + " action:" + action);
            return false;
        }
    };

}
