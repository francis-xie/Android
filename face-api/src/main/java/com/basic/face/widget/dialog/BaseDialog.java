
package com.basic.face.widget.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.basic.face.R;
import com.basic.face.utils.KeyboardUtils;
import com.basic.face.utils.ResUtils;
import com.basic.face.utils.StatusBarUtils;
import com.basic.face.utils.WidgetUtils;

/**
 * 基类Dialog
 * 1.触摸Dialog屏幕以外的区域，dialog消失同时隐藏键盘
 * 2.可以同步系统控制器显示状态
 */
public class BaseDialog extends AppCompatDialog {

    private View mContentView;

    /**
     * 是否同步系统控制器显示状态，默认false【状态栏、三键导航栏等】
     */
    private boolean mIsSyncSystemUiVisibility;

    public BaseDialog(Context context, int layoutId) {
        this(context, R.style.FACEDialog_Custom, layoutId);
    }

    public BaseDialog(Context context, View contentView) {
        this(context, R.style.FACEDialog_Custom, contentView);
    }

    public BaseDialog(Context context) {
        super(context, R.style.FACEDialog_Custom);
    }

    public BaseDialog(Context context, int theme, int layoutId) {
        super(context, theme);
        init(layoutId);

    }

    public BaseDialog(Context context, int theme, View contentView) {
        super(context, theme);
        init(contentView);
    }

    public void init(int layoutId) {
        View view = getLayoutInflater().inflate(layoutId, null);
        init(view);
    }

    private void init(View view) {
        setContentView(view);
        mContentView = view;
        setCanceledOnTouchOutside(true);
    }

    /**
     * 设置弹窗的宽和高
     *
     * @param width  宽
     * @param height 高
     */
    public BaseDialog setDialogSize(int width, int height) {
        // 获取对话框当前的参数值
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams p = window.getAttributes();
            p.width = width;
            p.height = height;
            window.setAttributes(p);
        }
        return this;
    }

    @Override
    public <T extends View> T findViewById(int resId) {
        return mContentView.findViewById(resId);
    }

    public String getString(int resId) {
        return getContext().getResources().getString(resId);
    }

    public Drawable getDrawable(int resId) {
        return ResUtils.getDrawable(getContext(), resId);
    }

    /**
     * 设置是否同步系统控制器显示状态
     *
     * @param isSyncSystemUiVisibility 是否同步系统控制器显示状态
     * @return this
     */
    public BaseDialog setIsSyncSystemUiVisibility(boolean isSyncSystemUiVisibility) {
        mIsSyncSystemUiVisibility = isSyncSystemUiVisibility;
        return this;
    }

    /**
     * 显示加载
     */
    @Override
    public void show() {
        showIfSync(mIsSyncSystemUiVisibility);
    }

    /**
     * 显示弹窗，是否同步系统控制器显示状态
     *
     * @param isSyncSystemUiVisibility 是否同步系统控制器显示状态
     */
    public void showIfSync(boolean isSyncSystemUiVisibility) {
        if (isSyncSystemUiVisibility) {
            boolean isHandled = StatusBarUtils.showWindow(WidgetUtils.findActivity(getContext()), getWindow(), new StatusBarUtils.IWindowShower() {
                @Override
                public void show(Window window) {
                    performShow();
                }
            });
            if (!isHandled) {
                performShow();
            }
        } else {
            performShow();
        }
    }

    /**
     * 真正执行显示的方法
     */
    protected void performShow() {
        super.show();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        KeyboardUtils.dispatchTouchEvent(ev, this);
        return super.onTouchEvent(ev);
    }

}
