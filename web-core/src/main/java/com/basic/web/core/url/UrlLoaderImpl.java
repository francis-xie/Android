
package com.basic.web.core.url;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import com.basic.web.utils.WebUtils;

import java.util.Map;

/**
 
 * @since 2.0.0
 */
public class UrlLoaderImpl implements IUrlLoader {


	private Handler mHandler = null;
	private WebView mWebView;
	private HttpHeaders mHttpHeaders;

	public UrlLoaderImpl(WebView webView, HttpHeaders httpHeaders) {
		this.mWebView = webView;
		if (this.mWebView == null) {
			new NullPointerException("webview cannot be null .");
		}

		this.mHttpHeaders = httpHeaders;
		mHandler = new Handler(Looper.getMainLooper());
	}

	private void safeLoadUrl(final String url) {

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				loadUrl(url);
			}
		});
	}

	private void safeReload() {

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				reload();
			}
		});
	}

	@Override
	public void loadUrl(String url) {
		this.loadUrl(url, null);
	}

	@Override
	public void loadUrl(final String url, final Map<String, String> headers) {

		if (!WebUtils.isUIThread()) {
			WebUtils.runInUiThread(new Runnable() {
				@Override
				public void run() {
					loadUrl(url, headers);
				}
			});
		}
		if (headers == null || headers.isEmpty()) {
			this.mWebView.loadUrl(url);
		} else {
			this.mWebView.loadUrl(url, headers);
		}
	}

	@Override
	public void reload() {
		if (!WebUtils.isUIThread()) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					reload();
				}
			});
			return;
		}
		this.mWebView.reload();


	}

	@Override
	public void loadData(final String data, final String mimeType, final String encoding) {

		if (!WebUtils.isUIThread()) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					loadData(data, mimeType, encoding);
				}
			});
			return;
		}
		this.mWebView.loadData(data, mimeType, encoding);

	}

	@Override
	public void stopLoading() {

		if (!WebUtils.isUIThread()) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					stopLoading();
				}
			});
			return;
		}
		this.mWebView.stopLoading();

	}

	@Override
	public void loadDataWithBaseURL(final String baseUrl, final String data, final String mimeType, final String encoding, final String historyUrl) {

		if (!WebUtils.isUIThread()) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
				}
			});
			return;
		}
		this.mWebView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);

	}

	@Override
	public void postUrl(final String url, final byte[] postData) {

		if (!WebUtils.isUIThread()) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					postUrl(url, postData);
				}
			});
			return;
		}

		this.mWebView.postUrl(url, postData);
	}

	@Override
	public HttpHeaders getHttpHeaders() {
		return this.mHttpHeaders == null ? this.mHttpHeaders = HttpHeaders.create() : this.mHttpHeaders;
	}
}
