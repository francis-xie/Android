
package com.basic.web.widget.indicator;

import android.webkit.WebView;


/**
 * 进度条控制器的默认实现
 *

 * @since 2019/1/4 上午10:45
 */
public class IndicatorHandler implements IndicatorController {
	private BaseIndicatorSpec mBaseIndicatorSpec;

	@Override
	public void progress(WebView v, int newProgress) {

		if (newProgress == 0) {
			reset();
		} else if (newProgress > 0 && newProgress <= 10) {
			showIndicator();
		} else if (newProgress > 10 && newProgress < 95) {
			setProgress(newProgress);
		} else {
			setProgress(newProgress);
			finish();
		}

	}

	@Override
	public BaseIndicatorSpec offerIndicator() {
		return this.mBaseIndicatorSpec;
	}

	public void reset() {

		if (mBaseIndicatorSpec != null) {
			mBaseIndicatorSpec.reset();
		}
	}

	@Override
	public void finish() {
		if (mBaseIndicatorSpec != null) {
			mBaseIndicatorSpec.hide();
		}
	}

	@Override
	public void setProgress(int n) {
		if (mBaseIndicatorSpec != null) {
			mBaseIndicatorSpec.setProgress(n);
		}
	}

	@Override
	public void showIndicator() {
		if (mBaseIndicatorSpec != null) {
			mBaseIndicatorSpec.show();
		}
	}

	public static IndicatorHandler getInstance() {
		return new IndicatorHandler();
	}


	public IndicatorHandler inJectIndicator(BaseIndicatorSpec baseIndicatorSpec) {
		this.mBaseIndicatorSpec = baseIndicatorSpec;
		return this;
	}
}
