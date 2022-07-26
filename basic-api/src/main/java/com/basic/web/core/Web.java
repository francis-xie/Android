
package com.basic.web.core;

import android.app.Activity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.*;
import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;
import com.basic.web.core.web.lifecycle.DefaultWebLifeCycleImpl;
import com.basic.web.core.url.HttpHeaders;
import com.basic.web.core.video.VideoImpl;
import com.basic.web.core.web.lifecycle.WebLifeCycle;
import com.basic.web.action.PermissionInterceptor;
import com.basic.web.core.client.DefaultChromeClient;
import com.basic.web.core.client.DefaultWebClient;
import com.basic.web.core.client.MiddlewareWebChromeBase;
import com.basic.web.core.client.MiddlewareWebClientBase;
import com.basic.web.core.client.WebListenerManager;
import com.basic.web.core.event.EventHandlerImpl;
import com.basic.web.core.event.EventInterceptor;
import com.basic.web.core.event.IEventHandler;
import com.basic.web.js.WebJsInterfaceCompat;
import com.basic.web.core.url.IUrlLoader;
import com.basic.web.core.url.UrlLoaderImpl;
import com.basic.web.utils.WebUtils;
import com.basic.web.utils.LogUtils;
import com.basic.web.core.video.IVideo;
import com.basic.web.core.web.AbsWebSettings;
import com.basic.web.core.web.WebConfig;
import com.basic.web.core.web.WebSettingsImpl;
import com.basic.web.core.web.WebUIControllerImplBase;
import com.basic.web.core.web.HookManager;
import com.basic.web.core.web.IWebSettings;
import com.basic.web.core.web.controller.AbsWebUIController;
import com.basic.web.widget.IWebLayout;
import com.basic.web.widget.indicator.BaseIndicatorView;
import com.basic.web.widget.indicator.DefaultWebCreator;
import com.basic.web.widget.indicator.IndicatorController;
import com.basic.web.widget.indicator.IndicatorHandler;
import com.basic.web.js.JsAccessEntrace;
import com.basic.web.js.JsAccessEntraceImpl;
import com.basic.web.js.JsInterfaceHolder;
import com.basic.web.js.JsInterfaceHolderImpl;
import com.basic.web.security.WebSecurityCheckLogic;
import com.basic.web.security.WebSecurityController;
import com.basic.web.security.WebSecurityControllerImpl;
import com.basic.web.security.WebSecurityLogicImpl;
import com.basic.web.widget.WebParentLayout;
import com.basic.web.widget.indicator.WebCreator;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * web核心api
 */
public final class Web {
	/**
	 * Web TAG
	 */
	private static final String TAG = Web.class.getSimpleName();
	/**
	 * Activity
	 */
	private Activity mActivity;
	/**
	 * 承载 WebParentLayout 的 ViewGroup
	 */
	private ViewGroup mViewGroup;
	/**
	 * 负责创建布局 WebView ，WebParentLayout  Indicator等。
	 */
	private WebCreator mWebCreator;
	/**
	 * 管理 WebSettings
	 */
	private IWebSettings mWebSettings;
	/**
	 * Web
	 */
	private Web mWeb = null;
	/**
	 * IndicatorController 控制Indicator
	 */
	private IndicatorController mIndicatorController;
	/**
	 * WebChromeClient
	 */
	private WebChromeClient mWebChromeClient;
	/**
	 * WebViewClient
	 */
	private WebViewClient mWebViewClient;
	/**
	 * is show indicator
	 */
	private boolean mEnableIndicator;
	/**
	 * IEventHandler 处理WebView相关返回事件
	 */
	private IEventHandler mIEventHandler;
	/**
	 * WebView 注入对象
	 */
	private ArrayMap<String, Object> mJavaObjects = new ArrayMap<>();
	/**
	 * flag
	 */
	private int TAG_TARGET = 0;
	/**
	 * WebListenerManager
	 */
	private WebListenerManager mWebListenerManager;
	/**
	 * 安全 Controller
	 */
	private WebSecurityController<WebSecurityCheckLogic> mWebSecurityController = null;
	/**
	 * WebSecurityCheckLogic
	 */
	private WebSecurityCheckLogic mWebSecurityCheckLogic = null;
	/**
	 * WebChromeClient
	 */
	private WebChromeClient mTargetChromeClient;
	/**
	 * flag security 's mode
	 */
	private SecurityType mSecurityType = SecurityType.DEFAULT_CHECK;
	/**
	 * Activity
	 */
	private static final int ACTIVITY_TAG = 0;
	/**
	 * Fragment
	 */
	private static final int FRAGMENT_TAG = 1;
	/**
	 * Web 默认注入对像
	 */
	private WebJsInterfaceCompat mWebJsInterfaceCompat = null;
	/**
	 * JsAccessEntrace 提供快速JS方法调用
	 */
	private JsAccessEntrace mJsAccessEntrace = null;
	/**
	 * URL Loader ， 提供了 WebView#loadUrl(url) reload() stopLoading（） postUrl()等方法
	 */
	private IUrlLoader mIUrlLoader = null;
	/**
	 * WebView 生命周期 ， 跟随生命周期释放CPU
	 */
	private WebLifeCycle mWebLifeCycle;
	/**
	 * Video 视屏播放管理类
	 */
	private IVideo mIVideo = null;
	/**
	 * WebViewClient 辅助控制开关
	 */
	private boolean mWebClientHelper = true;
	/**
	 * PermissionInterceptor 权限拦截
	 */
	private PermissionInterceptor mPermissionInterceptor;
	/**
	 * 是否拦截未知的Url， @link{DefaultWebClient}
	 */
	private boolean mIsInterceptUnkownUrl = false;
	/**
	 *
	 */
	private int mUrlHandleWays = -1;
	/**
	 * MiddlewareWebClientBase WebViewClient 中间件
	 */
	private MiddlewareWebClientBase mMiddleWrareWebClientBaseHeader;
	/**
	 * MiddlewareWebChromeBase WebChromeClient 中间件
	 */
	private MiddlewareWebChromeBase mMiddlewareWebChromeBaseHeader;
	/**
	 * 事件拦截
	 */
	private EventInterceptor mEventInterceptor;

	/**
	 * 注入对象管理类
	 */
	private JsInterfaceHolder mJsInterfaceHolder = null;


	private Web(AgentBuilder agentBuilder) {
		TAG_TARGET = agentBuilder.mTag;
		this.mActivity = agentBuilder.mActivity;
		this.mViewGroup = agentBuilder.mViewGroup;
		this.mIEventHandler = agentBuilder.mIEventHandler;
		this.mEnableIndicator = agentBuilder.mEnableIndicator;
		mWebCreator = agentBuilder.mWebCreator == null ? configWebCreator(agentBuilder.mBaseIndicatorView, agentBuilder.mIndex, agentBuilder.mLayoutParams, agentBuilder.mIndicatorColor, agentBuilder.mHeight, agentBuilder.mWebView, agentBuilder.mWebLayout) : agentBuilder.mWebCreator;
		mIndicatorController = agentBuilder.mIndicatorController;
		this.mWebChromeClient = agentBuilder.mWebChromeClient;
		this.mWebViewClient = agentBuilder.mWebViewClient;
		mWeb = this;
		this.mWebSettings = agentBuilder.mWebSettings;

		if (agentBuilder.mJavaObject != null && !agentBuilder.mJavaObject.isEmpty()) {
			this.mJavaObjects.putAll((Map<? extends String, ?>) agentBuilder.mJavaObject);
			LogUtils.i(TAG, "mJavaObject size:" + this.mJavaObjects.size());

		}
		this.mPermissionInterceptor = agentBuilder.mPermissionInterceptor == null ? null : new PermissionInterceptorWrapper(agentBuilder.mPermissionInterceptor);
		this.mSecurityType = agentBuilder.mSecurityType;
		this.mIUrlLoader = new UrlLoaderImpl(mWebCreator.create().getWebView(), agentBuilder.mHttpHeaders);
		if (this.mWebCreator.getWebParentLayout() instanceof WebParentLayout) {
			WebParentLayout mWebParentLayout = (WebParentLayout) this.mWebCreator.getWebParentLayout();
			mWebParentLayout.bindController(agentBuilder.mWebUIController == null ? WebUIControllerImplBase.build() : agentBuilder.mWebUIController);
			mWebParentLayout.setErrorLayoutRes(agentBuilder.mErrorLayout, agentBuilder.mReloadId);
			mWebParentLayout.setErrorView(agentBuilder.mErrorView);
		}
		this.mWebLifeCycle = new DefaultWebLifeCycleImpl(mWebCreator.getWebView());
		mWebSecurityController = new WebSecurityControllerImpl(mWebCreator.getWebView(), this.mWeb.mJavaObjects, this.mSecurityType);
		this.mWebClientHelper = agentBuilder.mWebClientHelper;
		this.mIsInterceptUnkownUrl = agentBuilder.mIsInterceptUnkownUrl;
		if (agentBuilder.mOpenOtherPage != null) {
			this.mUrlHandleWays = agentBuilder.mOpenOtherPage.getCode();
		}
		this.mMiddleWrareWebClientBaseHeader = agentBuilder.mMiddlewareWebClientBaseHeader;
		this.mMiddlewareWebChromeBaseHeader = agentBuilder.mChromeMiddleWareHeader;
		init();
	}


	/**
	 * @return PermissionInterceptor 权限控制者
	 */
	public PermissionInterceptor getPermissionInterceptor() {
		return this.mPermissionInterceptor;
	}


	public WebLifeCycle getWebLifeCycle() {
		return this.mWebLifeCycle;
	}


	public JsAccessEntrace getJsAccessEntrace() {

		JsAccessEntrace mJsAccessEntrace = this.mJsAccessEntrace;
		if (mJsAccessEntrace == null) {
			this.mJsAccessEntrace = mJsAccessEntrace = JsAccessEntraceImpl.getInstance(mWebCreator.getWebView());
		}
		return mJsAccessEntrace;
	}


	public Web clearWebCache() {

		if (this.getWebCreator().getWebView() != null) {
			WebUtils.clearWebViewAllCache(mActivity, this.getWebCreator().getWebView());
		} else {
			WebUtils.clearWebViewAllCache(mActivity);
		}
		return this;
	}


	public static AgentBuilder with(@NonNull Activity activity) {
		if (activity == null) {
			throw new NullPointerException("activity can not be null .");
		}
		return new AgentBuilder(activity);
	}

	public static AgentBuilder with(@NonNull Fragment fragment) {
		Activity activity = null;
		if ((activity = fragment.getActivity()) == null) {
			throw new NullPointerException("activity can not be null .");
		}
		return new AgentBuilder(activity, fragment);
	}

	public boolean handleKeyEvent(int keyCode, KeyEvent keyEvent) {

		if (mIEventHandler == null) {
			mIEventHandler = EventHandlerImpl.getInstance(mWebCreator.getWebView(), getInterceptor());
		}
		return mIEventHandler.onKeyDown(keyCode, keyEvent);
	}

	public boolean back() {
		if (mIEventHandler == null) {
			mIEventHandler = EventHandlerImpl.getInstance(mWebCreator.getWebView(), getInterceptor());
		}
		return mIEventHandler.back();
	}


	public WebCreator getWebCreator() {
		return this.mWebCreator;
	}

	public IEventHandler getIEventHandler() {
		return this.mIEventHandler == null ? (this.mIEventHandler = EventHandlerImpl.getInstance(mWebCreator.getWebView(), getInterceptor())) : this.mIEventHandler;
	}


	public IWebSettings getWebSettings() {
		return this.mWebSettings;
	}

	public IndicatorController getIndicatorController() {
		return this.mIndicatorController;
	}

	public JsInterfaceHolder getJsInterfaceHolder() {
		return this.mJsInterfaceHolder;
	}

	public IUrlLoader getUrlLoader() {
		return this.mIUrlLoader;
	}

	public void destroy() {
		this.mWebLifeCycle.onDestroy();
	}

	public static class PreWeb {
		private Web mWeb;
		private boolean isReady = false;

		PreWeb(Web web) {
			this.mWeb = web;
		}


		public PreWeb ready() {
			if (!isReady) {
				mWeb.ready();
				isReady = true;
			}
			return this;
		}

		public Web go(@Nullable String url) {
			if (!isReady) {
				ready();
			}
			return mWeb.go(url);
		}


	}


	private void doSafeCheck() {

		WebSecurityCheckLogic mWebSecurityCheckLogic = this.mWebSecurityCheckLogic;
		if (mWebSecurityCheckLogic == null) {
			this.mWebSecurityCheckLogic = mWebSecurityCheckLogic = WebSecurityLogicImpl.getInstance();
		}
		mWebSecurityController.check(mWebSecurityCheckLogic);

	}

	private void doCompat() {
		mJavaObjects.put("web", mWebJsInterfaceCompat = new WebJsInterfaceCompat(this, mActivity));
	}

	private WebCreator configWebCreator(BaseIndicatorView progressView, int index, ViewGroup.LayoutParams lp, int indicatorColor, int height_dp, WebView webView, IWebLayout webLayout) {

		if (progressView != null && mEnableIndicator) {
			return new DefaultWebCreator(mActivity, mViewGroup, lp, index, progressView, webView, webLayout);
		} else {
			return mEnableIndicator ?
					new DefaultWebCreator(mActivity, mViewGroup, lp, index, indicatorColor, height_dp, webView, webLayout)
					: new DefaultWebCreator(mActivity, mViewGroup, lp, index, webView, webLayout);
		}
	}

	private Web go(String url) {
		this.getUrlLoader().loadUrl(url);
		IndicatorController mIndicatorController = null;
		if (!TextUtils.isEmpty(url) && (mIndicatorController = getIndicatorController()) != null && mIndicatorController.offerIndicator() != null) {
			getIndicatorController().offerIndicator().show();
		}
		return this;
	}

	private EventInterceptor getInterceptor() {

		if (this.mEventInterceptor != null) {
			return this.mEventInterceptor;
		}

		if (mIVideo instanceof VideoImpl) {
			return this.mEventInterceptor = (EventInterceptor) this.mIVideo;
		}

		return null;

	}

	private void init() {
		doCompat();
		doSafeCheck();
	}

	private IVideo getIVideo() {
		return mIVideo == null ? new VideoImpl(mActivity, mWebCreator.getWebView()) : mIVideo;
	}

	private WebViewClient getWebViewClient() {

		LogUtils.i(TAG, "getDelegate:" + this.mMiddleWrareWebClientBaseHeader);
		DefaultWebClient mDefaultWebClient = DefaultWebClient
				.createBuilder()
				.setActivity(this.mActivity)
				.setClient(this.mWebViewClient)
				.setWebClientHelper(this.mWebClientHelper)
				.setPermissionInterceptor(this.mPermissionInterceptor)
				.setWebView(this.mWebCreator.getWebView())
				.setInterceptUnkownUrl(this.mIsInterceptUnkownUrl)
				.setUrlHandleWays(this.mUrlHandleWays)
				.build();
		MiddlewareWebClientBase header = this.mMiddleWrareWebClientBaseHeader;
		if (header != null) {
			MiddlewareWebClientBase tail = header;
			int count = 1;
			MiddlewareWebClientBase tmp = header;
			while (tmp.next() != null) {
				tail = tmp = tmp.next();
				count++;
			}
			LogUtils.i(TAG, "MiddlewareWebClientBase middleware count:" + count);
			tail.setDelegate(mDefaultWebClient);
			return header;
		} else {
			return mDefaultWebClient;
		}

	}


	private Web ready() {

		WebConfig.initCookiesManager(mActivity.getApplicationContext());
		IWebSettings mWebSettings = this.mWebSettings;
		if (mWebSettings == null) {
			this.mWebSettings = mWebSettings = WebSettingsImpl.getInstance();
		}

		if (mWebSettings instanceof AbsWebSettings) {
			((AbsWebSettings) mWebSettings).bindWeb(this);
		}
		if (mWebListenerManager == null && mWebSettings instanceof AbsWebSettings) {
			mWebListenerManager = (WebListenerManager) mWebSettings;
		}
		mWebSettings.toSetting(mWebCreator.getWebView());
		if (mJsInterfaceHolder == null) {
			mJsInterfaceHolder = JsInterfaceHolderImpl.getJsInterfaceHolder(mWebCreator.getWebView(), this.mSecurityType);
		}
		LogUtils.i(TAG, "mJavaObjects:" + mJavaObjects.size());
		if (mJavaObjects != null && !mJavaObjects.isEmpty()) {
			mJsInterfaceHolder.addJavaObjects(mJavaObjects);
		}

		if (mWebListenerManager != null) {
			mWebListenerManager.setDownloader(mWebCreator.getWebView(), null);
			mWebListenerManager.setWebChromeClient(mWebCreator.getWebView(), getChromeClient());
			mWebListenerManager.setWebViewClient(mWebCreator.getWebView(), getWebViewClient());
		}

		return this;
	}

	private WebChromeClient getChromeClient() {
		IndicatorController mIndicatorController =
				(this.mIndicatorController == null) ?
						IndicatorHandler.getInstance().inJectIndicator(mWebCreator.offer())
						: this.mIndicatorController;

		DefaultChromeClient mDefaultChromeClient =
				new DefaultChromeClient(this.mActivity,
						this.mIndicatorController = mIndicatorController,
						this.mWebChromeClient, this.mIVideo = getIVideo(),
						this.mPermissionInterceptor, mWebCreator.getWebView());

		LogUtils.i(TAG, "WebChromeClient:" + this.mWebChromeClient);
		MiddlewareWebChromeBase header = this.mMiddlewareWebChromeBaseHeader;
		if (header != null) {
			MiddlewareWebChromeBase tail = header;
			int count = 1;
			MiddlewareWebChromeBase tmp = header;
			for (; tmp.next() != null; ) {
				tail = tmp = tmp.next();
				count++;
			}
			LogUtils.i(TAG, "MiddlewareWebClientBase middleware count:" + count);
			tail.setDelegate(mDefaultChromeClient);
			return this.mTargetChromeClient = header;
		} else {
			return this.mTargetChromeClient = mDefaultChromeClient;
		}
	}


	public enum SecurityType {
		DEFAULT_CHECK, STRICT_CHECK;
	}


	public static final class AgentBuilder {
		private Activity mActivity;
		private Fragment mFragment;
		private ViewGroup mViewGroup;
		private boolean mIsNeedDefaultProgress;
		private int mIndex = -1;
		private BaseIndicatorView mBaseIndicatorView;
		private IndicatorController mIndicatorController = null;
		/*默认进度条是显示的*/
		private boolean mEnableIndicator = true;
		private ViewGroup.LayoutParams mLayoutParams = null;
		private WebViewClient mWebViewClient;
		private WebChromeClient mWebChromeClient;
		private int mIndicatorColor = -1;
		private IWebSettings mWebSettings;
		private WebCreator mWebCreator;
		private HttpHeaders mHttpHeaders = null;
		private IEventHandler mIEventHandler;
		private int mHeight = -1;
		private ArrayMap<String, Object> mJavaObject;
		private SecurityType mSecurityType = SecurityType.DEFAULT_CHECK;
		private WebView mWebView;
		private boolean mWebClientHelper = true;
		private IWebLayout mWebLayout = null;
		private PermissionInterceptor mPermissionInterceptor = null;
		private AbsWebUIController mWebUIController;
		private DefaultWebClient.OpenOtherPageWays mOpenOtherPage = null;
		private boolean mIsInterceptUnkownUrl = false;
		private MiddlewareWebClientBase mMiddlewareWebClientBaseHeader;
		private MiddlewareWebClientBase mMiddlewareWebClientBaseTail;
		private MiddlewareWebChromeBase mChromeMiddleWareHeader = null;
		private MiddlewareWebChromeBase mChromeMiddleWareTail = null;
		private View mErrorView;
		private int mErrorLayout;
		private int mReloadId;
		private int mTag = -1;


		public AgentBuilder(@NonNull Activity activity, @NonNull Fragment fragment) {
			mActivity = activity;
			mFragment = fragment;
			mTag = Web.FRAGMENT_TAG;
		}

		public AgentBuilder(@NonNull Activity activity) {
			mActivity = activity;
			mTag = Web.ACTIVITY_TAG;
		}


		public IndicatorBuilder setWebParent(@NonNull ViewGroup v, @NonNull ViewGroup.LayoutParams lp) {
			this.mViewGroup = v;
			this.mLayoutParams = lp;
			return new IndicatorBuilder(this);
		}

		public IndicatorBuilder setWebParent(@NonNull ViewGroup v, int index, @NonNull ViewGroup.LayoutParams lp) {
			this.mViewGroup = v;
			this.mLayoutParams = lp;
			this.mIndex = index;
			return new IndicatorBuilder(this);
		}


		private PreWeb buildWeb() {
			if (mTag == Web.FRAGMENT_TAG && this.mViewGroup == null) {
				throw new NullPointerException("ViewGroup is null,Please check your parameters .");
			}
			return new PreWeb(HookManager.hookWeb(new Web(this), this));
		}

		private void addJavaObject(String key, Object o) {
			if (mJavaObject == null) {
				mJavaObject = new ArrayMap<>();
			}
			mJavaObject.put(key, o);
		}

		private void addHeader(String k, String v) {

			if (mHttpHeaders == null) {
				mHttpHeaders = HttpHeaders.create();
			}
			mHttpHeaders.additionalHttpHeader(k, v);

		}
	}

	public static class IndicatorBuilder {
		private AgentBuilder mAgentBuilder = null;

		public IndicatorBuilder(AgentBuilder agentBuilder) {
			this.mAgentBuilder = agentBuilder;
		}

		public CommonBuilder useDefaultIndicator(int color) {
			this.mAgentBuilder.mEnableIndicator = true;
			this.mAgentBuilder.mIndicatorColor = color;
			return new CommonBuilder(mAgentBuilder);
		}

		public CommonBuilder useDefaultIndicator() {
			this.mAgentBuilder.mEnableIndicator = true;
			return new CommonBuilder(mAgentBuilder);
		}

		public CommonBuilder closeIndicator() {
			this.mAgentBuilder.mEnableIndicator = false;
			this.mAgentBuilder.mIndicatorColor = -1;
			this.mAgentBuilder.mHeight = -1;
			return new CommonBuilder(mAgentBuilder);
		}

		public CommonBuilder setCustomIndicator(@NonNull BaseIndicatorView v) {
			if (v != null) {
				this.mAgentBuilder.mEnableIndicator = true;
				this.mAgentBuilder.mBaseIndicatorView = v;
				this.mAgentBuilder.mIsNeedDefaultProgress = false;
			} else {
				this.mAgentBuilder.mEnableIndicator = true;
				this.mAgentBuilder.mIsNeedDefaultProgress = true;
			}

			return new CommonBuilder(mAgentBuilder);
		}

		public CommonBuilder useDefaultIndicator(@ColorInt int color, int height_dp) {
			this.mAgentBuilder.mIndicatorColor = color;
			this.mAgentBuilder.mHeight = height_dp;
			return new CommonBuilder(this.mAgentBuilder);
		}

	}


	public static class CommonBuilder {
		private AgentBuilder mAgentBuilder;

		public CommonBuilder(AgentBuilder agentBuilder) {
			this.mAgentBuilder = agentBuilder;
		}

		public CommonBuilder setEventHanadler(@Nullable IEventHandler iEventHandler) {
			mAgentBuilder.mIEventHandler = iEventHandler;
			return this;
		}

		public CommonBuilder closeWebViewClientHelper() {
			mAgentBuilder.mWebClientHelper = false;
			return this;
		}


		public CommonBuilder setWebChromeClient(@Nullable WebChromeClient webChromeClient) {
			this.mAgentBuilder.mWebChromeClient = webChromeClient;
			return this;

		}

		public CommonBuilder setWebViewClient(@Nullable WebViewClient webChromeClient) {
			this.mAgentBuilder.mWebViewClient = webChromeClient;
			return this;
		}

		public CommonBuilder useMiddlewareWebClient(@NonNull MiddlewareWebClientBase middleWrareWebClientBase) {
			if (middleWrareWebClientBase == null) {
				return this;
			}
			if (this.mAgentBuilder.mMiddlewareWebClientBaseHeader == null) {
				this.mAgentBuilder.mMiddlewareWebClientBaseHeader = this.mAgentBuilder.mMiddlewareWebClientBaseTail = middleWrareWebClientBase;
			} else {
				this.mAgentBuilder.mMiddlewareWebClientBaseTail.enq(middleWrareWebClientBase);
				this.mAgentBuilder.mMiddlewareWebClientBaseTail = middleWrareWebClientBase;
			}
			return this;
		}

		public CommonBuilder useMiddlewareWebChrome(@NonNull MiddlewareWebChromeBase middlewareWebChromeBase) {
			if (middlewareWebChromeBase == null) {
				return this;
			}
			if (this.mAgentBuilder.mChromeMiddleWareHeader == null) {
				this.mAgentBuilder.mChromeMiddleWareHeader = this.mAgentBuilder.mChromeMiddleWareTail = middlewareWebChromeBase;
			} else {
				this.mAgentBuilder.mChromeMiddleWareTail.enq(middlewareWebChromeBase);
				this.mAgentBuilder.mChromeMiddleWareTail = middlewareWebChromeBase;
			}
			return this;
		}

		public CommonBuilder setMainFrameErrorView(@NonNull View view) {
			this.mAgentBuilder.mErrorView = view;
			return this;
		}

		public CommonBuilder setMainFrameErrorView(@LayoutRes int errorLayout, @IdRes int clickViewId) {
			this.mAgentBuilder.mErrorLayout = errorLayout;
			this.mAgentBuilder.mReloadId = clickViewId;
			return this;
		}

		public CommonBuilder setWebWebSettings(@Nullable IWebSettings webSettings) {
			this.mAgentBuilder.mWebSettings = webSettings;
			return this;
		}

		public PreWeb createWeb() {
			return this.mAgentBuilder.buildWeb();
		}


		public CommonBuilder addJavascriptInterface(@NonNull String name, @NonNull Object o) {
			this.mAgentBuilder.addJavaObject(name, o);
			return this;
		}

		public CommonBuilder setSecurityType(@NonNull SecurityType type) {
			this.mAgentBuilder.mSecurityType = type;
			return this;
		}

		public CommonBuilder setWebView(@Nullable WebView webView) {
			this.mAgentBuilder.mWebView = webView;
			return this;
		}

		public CommonBuilder setWebLayout(@Nullable IWebLayout iWebLayout) {
			this.mAgentBuilder.mWebLayout = iWebLayout;
			return this;
		}

		public CommonBuilder additionalHttpHeader(String k, String v) {
			this.mAgentBuilder.addHeader(k, v);

			return this;
		}

		public CommonBuilder setPermissionInterceptor(@Nullable PermissionInterceptor permissionInterceptor) {
			this.mAgentBuilder.mPermissionInterceptor = permissionInterceptor;
			return this;
		}

		public CommonBuilder setWebUIController(@Nullable WebUIControllerImplBase webUIController) {
			this.mAgentBuilder.mWebUIController = webUIController;
			return this;
		}

		public CommonBuilder setOpenOtherPageWays(@Nullable DefaultWebClient.OpenOtherPageWays openOtherPageWays) {
			this.mAgentBuilder.mOpenOtherPage = openOtherPageWays;
			return this;
		}

		public CommonBuilder interceptUnkownUrl() {
			this.mAgentBuilder.mIsInterceptUnkownUrl = true;
			return this;
		}

	}

	private static final class PermissionInterceptorWrapper implements PermissionInterceptor {

		private WeakReference<PermissionInterceptor> mWeakReference;

		private PermissionInterceptorWrapper(PermissionInterceptor permissionInterceptor) {
			this.mWeakReference = new WeakReference<PermissionInterceptor>(permissionInterceptor);
		}

		@Override
		public boolean intercept(String url, String[] permissions, String a) {
			if (this.mWeakReference.get() == null) {
				return false;
			}
			return mWeakReference.get().intercept(url, permissions, a);
		}
	}


}
