
package com.basic.tools.system;

import android.app.Service;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.basic.tools.Util;

/**
 * <pre>
 *     desc   :	输入框震动效果帮助类

 *     time   : 2018/4/27 下午8:40
 * </pre>
 */
public class EditTextShakeHelper {

	private static EditTextShakeHelper sInstance;
	/**
	 * 震动动画
	 */
	private Animation mShakeAnimation;
	/**
	 * 振动器
	 */
	private final Vibrator mShakeVibrator;

	/**
	 * 获取实例
	 * @return
	 */
	public static EditTextShakeHelper get() {
		if (sInstance == null) {
			synchronized (EditTextShakeHelper.class) {
				if (sInstance == null) {
					sInstance = new EditTextShakeHelper();
				}
			}
		}
		return sInstance;
	}

	public EditTextShakeHelper() {
		// 初始化振动器
		mShakeVibrator = (Vibrator) Util.getContext().getSystemService(Service.VIBRATOR_SERVICE);
		// 初始化震动动画
		mShakeAnimation = new TranslateAnimation(0, 10, 0, 0);
		mShakeAnimation.setDuration(300);
		mShakeAnimation.setInterpolator(new CycleInterpolator(8));
	}

	/**
	 * 设置震动动画
	 * @param shakeAnimation
	 * @return
	 */
	public EditTextShakeHelper setShakeAnimation(Animation shakeAnimation) {
		mShakeAnimation = shakeAnimation;
		return this;
	}

	/**
	 * 开始震动
	 * 
	 * @param editTexts 震动的输入框
	 */
	public void shake(EditText... editTexts) {
		if (editTexts != null && editTexts.length > 0) {
			for (EditText editText : editTexts) {
				editText.startAnimation(mShakeAnimation);
			}
			mShakeVibrator.vibrate(new long[] { 0, 500 }, -1);
		}
	}

}
