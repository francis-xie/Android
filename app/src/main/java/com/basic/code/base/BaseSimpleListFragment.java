
package com.basic.code.base;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.umeng.analytics.MobclickAgent;
import com.basic.aop.annotation.MemoryCache;
import com.basic.aop.cache.XMemoryCache;
import com.basic.page.base.PageActivity;
import com.basic.page.base.PageFragment;
import com.basic.page.base.PageSimpleListFragment;
import com.basic.page.core.PageOption;
import com.basic.router.facade.service.SerializationService;
import com.basic.router.launcher.Router;
import com.basic.face.utils.DrawableUtils;
import com.basic.face.utils.ThemeUtils;
import com.basic.face.utils.WidgetUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.actionbar.TitleUtils;
import com.basic.code.R;

import java.io.Serializable;

/**
 
 * @since 2018/12/29 下午12:41
 */
public abstract class BaseSimpleListFragment extends PageSimpleListFragment {

    @Override
    protected void initPage() {
        initTitle();
        initViews();
        initListeners();
    }

    @Override
    protected void initSimply() {
        WidgetUtils.clearAllViewBackground(getListView());
    }

    protected TitleBar initTitle() {
        return TitleUtils.addTitleBarDynamic((ViewGroup) getRootView(), getPageTitle(), v -> popToBack())
                .setLeftImageDrawable(getNavigationBackDrawable(R.attr.face_actionbar_ic_navigation_back));
    }

    @MemoryCache
    private Drawable getNavigationBackDrawable(int navigationBackId) {
        return DrawableUtils.getSupportRTLDrawable(ThemeUtils.resolveDrawable(getContext(), navigationBackId));
    }

    @Override
    public void onDestroyView() {
//        KeyboardUtils.fixSoftInputLeaks(getContext());
        super.onDestroyView();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        //屏幕旋转时刷新一下title
        super.onConfigurationChanged(newConfig);
        XMemoryCache.getInstance().clear();
        ViewGroup root = (ViewGroup) getRootView();
        if (root.getChildAt(0) instanceof TitleBar) {
            root.removeViewAt(0);
            initTitle();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getPageName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getPageName());
    }


    /**
     * 打开一个新的页面
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openNewPage(Class<T> clazz) {
        return new PageOption(clazz)
                .setNewActivity(true)
                .open(this);
    }

    /**
     * 打开一个新的页面
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openNewPage(Class<T> clazz, @NonNull Class<? extends PageActivity> containActivityClazz) {
        return new PageOption(clazz)
                .setNewActivity(true)
                .setContainActivityClazz(containActivityClazz)
                .open(this);
    }

    /**
     * 打开一个新的页面
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openNewPage(Class<T> clazz, String key, Object value) {
        PageOption option = new PageOption(clazz).setNewActivity(true);
        return openPage(option, key, value);
    }

    public Fragment openPage(PageOption option, String key, Object value) {
        if (value instanceof Integer) {
            option.putInt(key, (Integer) value);
        } else if (value instanceof String) {
            option.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            option.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            option.putFloat(key, (Float) value);
        } else if (value instanceof Parcelable) {
            option.putParcelable(key, (Parcelable) value);
        } else if (value instanceof Serializable) {
            option.putSerializable(key, (Serializable) value);
        } else {
            option.putString(key, Router.getInstance().navigation(SerializationService.class).object2Json(value));
        }
        return option.open(this);
    }

    /**
     * 打开一个新的页面
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openPage(Class<T> clazz, boolean addToBackStack, String key, String value) {
        return new PageOption(clazz)
                .setAddToBackStack(addToBackStack)
                .putString(key, value)
                .open(this);
    }

    /**
     * 打开一个新的页面
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openPage(Class<T> clazz, String key, Object value) {
        return openPage(clazz, true, key, value);
    }

    /**
     * 打开一个新的页面
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openPage(Class<T> clazz, boolean addToBackStack, String key, Object value) {
        PageOption option = new PageOption(clazz).setAddToBackStack(addToBackStack);
        return openPage(option, key, value);
    }

    /**
     * 打开一个新的页面
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openPage(Class<T> clazz, String key, String value) {
        return new PageOption(clazz)
                .putString(key, value)
                .open(this);
    }

    /**
     * 打开页面,需要结果返回
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openPageForResult(Class<T> clazz, String key, Object value, int requestCode) {
        PageOption option = new PageOption(clazz).setRequestCode(requestCode);
        return openPage(option, key, value);
    }

    /**
     * 打开页面,需要结果返回
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openPageForResult(Class<T> clazz, String key, String value, int requestCode) {
        return new PageOption(clazz)
                .setRequestCode(requestCode)
                .putString(key, value)
                .open(this);
    }

    /**
     * 打开页面,需要结果返回
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openNewPageForResult(Class<T> clazz, String key, String value, int requestCode) {
        return new PageOption(clazz)
                .setNewActivity(true)
                .setRequestCode(requestCode)
                .putString(key, value)
                .open(this);
    }

    /**
     * 打开页面,需要结果返回
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openPageForResult(Class<T> clazz, int requestCode) {
        return new PageOption(clazz)
                .setRequestCode(requestCode)
                .open(this);
    }

}
