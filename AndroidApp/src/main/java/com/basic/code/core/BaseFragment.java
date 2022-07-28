package com.basic.code.core;

import android.content.res.Configuration;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.umeng.analytics.MobclickAgent;
import com.basic.code.core.http.loader.ProgressLoader;
import com.basic.http2.subsciber.impl.IProgressLoader;
import com.basic.page.base.PageActivity;
import com.basic.page.base.PageFragment;
import com.basic.page.core.PageOption;
import com.basic.page.enums.CoreAnim;
import com.basic.page.utils.Utils;
import com.basic.router.facade.service.SerializationService;
import com.basic.router.launcher.Router;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.actionbar.TitleUtils;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * 基础fragment，使用Page框架搭建
 */
public abstract class BaseFragment<Binding extends ViewBinding> extends PageFragment {

    private IProgressLoader mIProgressLoader;

    /**
     * ViewBinding
     */
    protected Binding binding;

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        binding = viewBindingInflate(inflater, container);
        return binding.getRoot();
    }

    /**
     * 构建ViewBinding
     *
     * @param inflater  inflater
     * @param container 容器
     * @return ViewBinding
     */
    @NonNull
    protected abstract Binding viewBindingInflate(LayoutInflater inflater, ViewGroup container);

    /**
     * 获取Binding
     *
     * @return Binding
     */
    public Binding getBinding() {
        return binding;
    }

    @Override
    protected void initPage() {
        initTitle();
        initViews();
        initListeners();
    }

    protected TitleBar initTitle() {
        return TitleUtils.addTitleBarDynamic((ViewGroup) getRootView(), getPageTitle(), v -> popToBack());
    }

    @Override
    protected void initListeners() {

    }

    /**
     * 获取进度条加载者
     *
     * @return 进度条加载者
     */
    public IProgressLoader getProgressLoader() {
        if (mIProgressLoader == null) {
            mIProgressLoader = ProgressLoader.create(getContext());
        }
        return mIProgressLoader;
    }

    /**
     * 获取进度条加载者
     *
     * @param message 提示信息
     * @return 进度条加载者
     */
    public IProgressLoader getProgressLoader(String message) {
        if (mIProgressLoader == null) {
            mIProgressLoader = ProgressLoader.create(getContext(), message);
        } else {
            mIProgressLoader.updateMessage(message);
        }
        return mIProgressLoader;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        //屏幕旋转时刷新一下title
        super.onConfigurationChanged(newConfig);
        ViewGroup root = (ViewGroup) getRootView();
        if (root.getChildAt(0) instanceof TitleBar) {
            root.removeViewAt(0);
            initTitle();
        }
    }

    @Override
    public void onDestroyView() {
        if (mIProgressLoader != null) {
            mIProgressLoader.dismissLoading();
        }
        super.onDestroyView();
        binding = null;
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

    //==============================页面跳转api===================================//

    /**
     * 打开一个新的页面【建议只在主tab页使用】
     *
     * @param clazz 页面的类
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openNewPage(Class<T> clazz) {
        return new PageOption(clazz)
                .setNewActivity(true)
                .open(this);
    }

    /**
     * 打开一个新的页面【建议只在主tab页使用】
     *
     * @param pageName 页面名
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openNewPage(String pageName) {
        return new PageOption(pageName)
                .setAnim(CoreAnim.slide)
                .setNewActivity(true)
                .open(this);
    }


    /**
     * 打开一个新的页面【建议只在主tab页使用】
     *
     * @param clazz                页面的类
     * @param containActivityClazz 页面容器
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
     * 打开一个新的页面【建议只在主tab页使用】
     *
     * @param clazz 页面的类
     * @param key   入参的键
     * @param value 入参的值
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
        } else if (value instanceof Float) {
            option.putFloat(key, (Float) value);
        } else if (value instanceof String) {
            option.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            option.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            option.putLong(key, (Long) value);
        } else if (value instanceof Double) {
            option.putDouble(key, (Double) value);
        } else if (value instanceof Parcelable) {
            option.putParcelable(key, (Parcelable) value);
        } else if (value instanceof Serializable) {
            option.putSerializable(key, (Serializable) value);
        } else {
            option.putString(key, serializeObject(value));
        }
        return option.open(this);
    }

    /**
     * 打开页面
     *
     * @param clazz          页面的类
     * @param addToBackStack 是否加入回退栈
     * @param key            入参的键
     * @param value          入参的值
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
     * 打开页面
     *
     * @param clazz 页面的类
     * @param key   入参的键
     * @param value 入参的值
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openPage(Class<T> clazz, String key, Object value) {
        return openPage(clazz, true, key, value);
    }

    /**
     * 打开页面
     *
     * @param clazz          页面的类
     * @param addToBackStack 是否加入回退栈
     * @param key            入参的键
     * @param value          入参的值
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openPage(Class<T> clazz, boolean addToBackStack, String key, Object value) {
        PageOption option = new PageOption(clazz).setAddToBackStack(addToBackStack);
        return openPage(option, key, value);
    }

    /**
     * 打开页面
     *
     * @param clazz 页面的类
     * @param key   入参的键
     * @param value 入参的值
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
     * @param clazz       页面的类
     * @param key         入参的键
     * @param value       入参的值
     * @param requestCode 请求码
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
     * @param clazz       页面的类
     * @param key         入参的键
     * @param value       入参的值
     * @param requestCode 请求码
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
     * @param clazz       页面的类
     * @param requestCode 请求码
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openPageForResult(Class<T> clazz, int requestCode) {
        return new PageOption(clazz)
                .setRequestCode(requestCode)
                .open(this);
    }

    /**
     * 序列化对象
     *
     * @param object 需要序列化的对象
     * @return 序列化结果
     */
    public String serializeObject(Object object) {
        return Router.getInstance().navigation(SerializationService.class).object2Json(object);
    }

    /**
     * 反序列化对象
     *
     * @param input 反序列化的内容
     * @param clazz 类型
     * @return 反序列化结果
     */
    public <T> T deserializeObject(String input, Type clazz) {
        return Router.getInstance().navigation(SerializationService.class).parseObject(input, clazz);
    }


    @Override
    protected void hideCurrentPageSoftInput() {
        if (getActivity() == null) {
            return;
        }
        // 记住，要在xml的父布局加上android:focusable="true" 和 android:focusableInTouchMode="true"
        Utils.hideSoftInputClearFocus(getActivity().getCurrentFocus());
    }

}
